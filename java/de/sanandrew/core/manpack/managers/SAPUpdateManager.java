/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.managers;

import com.google.common.collect.Maps;
import com.google.gson.*;
import cpw.mods.fml.common.FMLLog;
import de.sanandrew.core.manpack.mod.ModCntManPack;
import de.sanandrew.core.manpack.util.MutableString;
import de.sanandrew.core.manpack.util.javatuples.Triplet;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.logging.log4j.Level;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class SAPUpdateManager
{
	private boolean checkedForUpdate = false;
	private Version version;
	private String modName;
	private URL updURL;
	private String modInfoURL;
    private File modPackedJar;
    private UpdateFile updInfo = new UpdateFile();
	private final int mgrId;

	public static final List<Triplet<SAPUpdateManager, MutableBoolean, MutableString>> UPD_MANAGERS = new ArrayList<>();
	public static final Map<Integer, Boolean> IS_IN_RENDER_QUEUE = Maps.newHashMap();

	public static synchronized void setChecked(int mgrId) {
        UPD_MANAGERS.get(mgrId).getValue1().setTrue();
	}

    public static synchronized void setHasUpdate(int mgrId, String version) {
        setChecked(mgrId);
        UPD_MANAGERS.get(mgrId).getValue2().set(version);
    }

    public static void setInRenderQueue(int mgrId) {
        IS_IN_RENDER_QUEUE.put(mgrId, true);
    }

    private SAPUpdateManager(String modName, int majorNr, int minorNr, int revisionNr, String updateURL, String modURL, File modJar) {
        this.modName = modName;
        this.version = new Version(majorNr, minorNr, revisionNr);
        this.modInfoURL = modURL;
        this.modPackedJar = modJar;

        try {
            this.updURL = new URL(updateURL);
        } catch( MalformedURLException | NullPointerException e ) {
            this.updURL = null;
            FMLLog.log(ModCntManPack.UPD_LOG, Level.WARN, "The URL to the mod version file is invalid!");
            e.printStackTrace();
        }

        this.mgrId = UPD_MANAGERS.size();
    }

    @Deprecated
	public SAPUpdateManager(String modName, int majorNr, int minorNr, int revisionNr, String updateURL, String modURL, String modJar) {
        this(modName, majorNr, minorNr, revisionNr, updateURL, modURL, (File) null);
	}

    @Deprecated
	public SAPUpdateManager(String modName, int majorNr, int minorNr, int revisionNr, String updateURL, String modURL) {
        this(modName, majorNr, minorNr, revisionNr, updateURL, modURL, (File) null);
	}

    @Deprecated
    public SAPUpdateManager(String modName, String version, String updateURL, String modURL, String modJar) {
        this(modName, 0, 0, 0, updateURL, modURL, (File) null);
        this.version = new Version(version);
    }

    @Deprecated
	public SAPUpdateManager(String modName, String version, String updateURL, String modURL) {
	    this(modName, version, updateURL, modURL, null);
	}

    public static SAPUpdateManager createUpdateManager(String modName, int majorNr, int minorNr, int revisionNr, String updateURL, String modURL, File modJar) {
        SAPUpdateManager updMgr = new SAPUpdateManager(modName, majorNr, minorNr, revisionNr, updateURL, modURL, modJar);

        if( updMgr.updURL != null ) {
            UPD_MANAGERS.add(Triplet.with(updMgr, new MutableBoolean(false), new MutableString("")));
            IS_IN_RENDER_QUEUE.put(updMgr.mgrId, false);
        }

        return updMgr;
    }

	private void check() {
	    Runnable threadProcessor = new Runnable() {
            @Override
            public void run() {
                try {
                    FMLLog.log(ModCntManPack.UPD_LOG, Level.INFO, "Checking for %s update", SAPUpdateManager.this.getModName());
                    if( SAPUpdateManager.this.getUpdateURL() == null ) {
                        throw new MalformedURLException("[NULL]");
                    }

                    Gson gson = new GsonBuilder().registerTypeAdapter(UpdateFile.class, new AnnotatedDeserializer<UpdateFile>()).create();

                    try( BufferedReader in = new BufferedReader(new InputStreamReader(SAPUpdateManager.this.getUpdateURL().openStream())) ) {
                        SAPUpdateManager.this.updInfo = gson.fromJson(in, UpdateFile.class);
                    } catch( IOException | JsonSyntaxException e ) {
                        FMLLog.log(ModCntManPack.UPD_LOG, Level.WARN, e, "Check for Update failed!");
                    }

                    if( SAPUpdateManager.this.updInfo.version == null || SAPUpdateManager.this.updInfo.version.length() < 1 ) {
                        SAPUpdateManager.setChecked(SAPUpdateManager.this.getId());
                        return;
                    }

                    Version webVersion = new Version(SAPUpdateManager.this.updInfo.version);
                    SAPUpdateManager.this.updInfo.version = webVersion.toString();              // reformat the version number to the format major.minor.revision

                    if( webVersion.major > SAPUpdateManager.this.getVersion().major ) {
                        FMLLog.log(ModCntManPack.UPD_LOG, Level.INFO, "New major update for %s is out: %s", SAPUpdateManager.this.getModName(),
                                   SAPUpdateManager.this.updInfo.version);
                        SAPUpdateManager.setHasUpdate(SAPUpdateManager.this.mgrId, SAPUpdateManager.this.updInfo.version);
                        return;
                    } else if( webVersion.major == SAPUpdateManager.this.getVersion().major ) {
                        if( webVersion.minor > SAPUpdateManager.this.getVersion().minor ) {
                            FMLLog.log(ModCntManPack.UPD_LOG, Level.INFO, "New minor update for %s is out: %s", SAPUpdateManager.this.getModName(),
                                       SAPUpdateManager.this.updInfo.version);
                            SAPUpdateManager.setHasUpdate(SAPUpdateManager.this.mgrId, SAPUpdateManager.this.updInfo.version);
                            return;
                        } else if( webVersion.minor == SAPUpdateManager.this.getVersion().minor ) {
                            if( webVersion.revision > SAPUpdateManager.this.getVersion().revision ) {
                                FMLLog.log(ModCntManPack.UPD_LOG, Level.INFO, "New bugfix update for %s is out: %s", SAPUpdateManager.this.getModName(),
                                           SAPUpdateManager.this.updInfo.version);
                                SAPUpdateManager.setHasUpdate(SAPUpdateManager.this.mgrId, SAPUpdateManager.this.updInfo.version);
                                return;
                            }
                        }
                    }

                    FMLLog.log(ModCntManPack.UPD_LOG, Level.INFO, "No new update for %s is available.", SAPUpdateManager.this.getModName());
                } catch( IOException e ) {
                    FMLLog.log(ModCntManPack.UPD_LOG, Level.WARN, "Update Check for %s failed!", SAPUpdateManager.this.getModName());
                    e.printStackTrace();
                }

                SAPUpdateManager.setChecked(SAPUpdateManager.this.getId());
            }
        };

        (new Thread(threadProcessor, "SAPUpdateThread")).start();
	}

	public void checkForUpdate() {
		if( !this.checkedForUpdate ) {
			this.check();
			this.checkedForUpdate = true;
		}
	}

    public Version getVersion() {
        return this.version;
    }

    public String getModName() {
        return this.modName;
    }

    public URL getUpdateURL() {
        return this.updURL;
    }

    public String getModInfoURL() {
        return this.modInfoURL;
    }

    public File getModJar() {
        return this.modPackedJar;
    }

    public int getId() {
        return this.mgrId;
    }

    public UpdateFile getUpdateInfo() {
        return this.updInfo;
    }

    public EnumUpdateSeverity getVersionDiffSeverity() {
        if( this.updInfo.severityOverride != null && this.updInfo.severityOverride.length() > 0 ) {
            return EnumUpdateSeverity.valueOf(this.updInfo.severityOverride);
        }

        Version updVersion = new Version(this.updInfo.version);

        if( updVersion.major > this.version.major ) {
            return EnumUpdateSeverity.SEVERE;
        } else if( updVersion.major == this.version.major ) {
            if( updVersion.minor >= this.version.minor + 4 ) {
                return EnumUpdateSeverity.SEVERE;
            } else if( updVersion.minor > this.version.minor ) {
                return EnumUpdateSeverity.MAJOR;
            } else if( updVersion.minor == this.version.minor ) {
                if( updVersion.revision >= this.version.revision + 8 ) {
                    return EnumUpdateSeverity.SEVERE;
                } else if( updVersion.revision >= this.version.revision + 4 ) {
                    return EnumUpdateSeverity.MAJOR;
                } else if( updVersion.revision > this.version.revision ) {
                    return EnumUpdateSeverity.MINOR;
                }
            }
        }

        return EnumUpdateSeverity.UNKNOWN;
    }

    public static class UpdateFile
    {
        @JsonRequired
        public String version;
        public String downloadUrl;
        public String description;
        public String severityOverride;
        public String[] changelog;

        public UpdateFile() { }
    }

    public static enum EnumUpdateSeverity
    {
        MINOR(EnumChatFormatting.GREEN),
        MAJOR(EnumChatFormatting.YELLOW),
        SEVERE(EnumChatFormatting.RED),
        UNKNOWN(EnumChatFormatting.WHITE);

        public final EnumChatFormatting format;

        private EnumUpdateSeverity(EnumChatFormatting formatting) {
            this.format = formatting;
        }
    }

    public static class Version
    {
        public final int revision;
        public final int minor;
        public final int major;

        private static final Pattern[] VERSION_PATTERNS = new Pattern[] {
                Pattern.compile("\\d+\\.\\d+[\\.|_]\\d+-(\\d+)\\.(\\d+)[\\.|_](\\d+)"),     // 1.7.2-1.0.4 or 1.7_01-1.5_02
                Pattern.compile("(\\d+)\\.(\\d+)[\\.|_](\\d+)")                             // 1.0.4 or 1.5_02
        };

        public Version(int majorNr, int minorNr, int revisionNr) {
            this.major = majorNr;
            this.minor = minorNr;
            this.revision = revisionNr;
        }

        public Version(String version) {
            if( version != null ) {
                Matcher matcher;
                for( Pattern verPattern : VERSION_PATTERNS ) {
                    matcher = verPattern.matcher(version);
                    if( matcher.find() ) {
                        this.major = Integer.valueOf(matcher.group(1));
                        this.minor = Integer.valueOf(matcher.group(2));
                        this.revision = Integer.valueOf(matcher.group(3));
                        return;
                    }
                }
            }

            this.major = -1;
            this.minor = -1;
            this.revision = -1;
//	        FMLLog.log(ModCntManPack.UPD_LOG, Level.WARN,
//	                   "Version Number for the mod %s could not be compiled! The version number %s does not have the required formatting!",
//	                   this.modName, version);
        }

        @Override
        public String toString() {
            return String.format("%d.%d.%d", this.major, this.minor, this.revision);
        }
    }


    /**
     * code from
     */

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface JsonRequired
    { }

    class AnnotatedDeserializer<T> implements JsonDeserializer<T>
    {
        public T deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException
        {
            T pojo = new Gson().fromJson(je, type);

            Field[] fields = pojo.getClass().getDeclaredFields();
            for( Field f : fields ) {
                if( f.getAnnotation(JsonRequired.class) != null ) {
                    try {
                        f.setAccessible(true);
                        if (f.get(pojo) == null) {
                            throw new JsonParseException("Missing field in JSON: " + f.getName());
                        }
                    } catch( IllegalArgumentException | IllegalAccessException ex ) {
                        FMLLog.log(ModCntManPack.UPD_LOG, Level.WARN, null, ex);
                    }
                }
            }

            return pojo;
        }
    }
}
