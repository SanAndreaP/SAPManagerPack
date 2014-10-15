package de.sanandrew.core.manpack.managers;

import com.google.common.collect.Maps;
import cpw.mods.fml.common.FMLLog;
import de.sanandrew.core.manpack.mod.ModCntManPack;
import de.sanandrew.core.manpack.util.javatuples.Quartet;
import de.sanandrew.core.manpack.util.javatuples.Triplet;
import org.apache.logging.log4j.Level;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SAPUpdateManager
{
	private boolean checkedForUpdate = false;
	private Triplet<Integer, Integer, Integer> version; // major, minor, revision
	private String modName;
	private URL updURL;
	private String modInfoURL;
    private URL directDL;
    private String modPackedJar;
	private final int mgrId;

	private Quartet<String, Integer, Integer, Integer> newVersion; // MC version, major, minor, revision

	public static final List<Triplet<SAPUpdateManager, Boolean, String>> UPD_MANAGERS = new ArrayList<>();
	public static final Map<Integer, Boolean> IS_IN_RENDER_QUEUE = Maps.newHashMap();

	public static synchronized void setChecked(int mgrId) {
	    UPD_MANAGERS.set(mgrId, UPD_MANAGERS.get(mgrId).setAt1(true));
	}

    public static synchronized void setHasUpdate(int mgrId, String version) {
        setChecked(mgrId);
        UPD_MANAGERS.set(mgrId, UPD_MANAGERS.get(mgrId).setAt2(version));
    }

    public static void setInRenderQueue(int mgrId) {
        IS_IN_RENDER_QUEUE.put(mgrId, true);
    }

	public SAPUpdateManager(String modName, int majorNr, int minorNr, int revisionNr, String updateURL, String modURL, String modJar) {
		this.modName = modName;
		this.version = Triplet.with(majorNr, minorNr, revisionNr);
		this.modInfoURL = modURL;
		this.modPackedJar = modJar;

        URL newUrl = null;
		try {
		    newUrl = new URL(updateURL);
        } catch( MalformedURLException | NullPointerException e ) {
            FMLLog.log(ModCntManPack.UPD_LOG, Level.WARN, "The URL to the mod version file is invalid!");
            e.printStackTrace();
        }
		this.updURL = newUrl;

		this.mgrId = UPD_MANAGERS.size();
		UPD_MANAGERS.add(Triplet.with(this, false, (String)null));
		IS_IN_RENDER_QUEUE.put(this.mgrId, false);
	}

	public SAPUpdateManager(String modName, int majorNr, int minorNr, int revisionNr, String updateURL, String modURL) {
        this(modName, majorNr, minorNr, revisionNr, updateURL, modURL, null);
	}

    public SAPUpdateManager(String modName, String version, String updateURL, String modURL, String modJar) {
        this(modName, 0, 0, 0, updateURL, modURL, modJar);
        this.version = this.getVersionFromStr(version);
    }

	public SAPUpdateManager(String modName, String version, String updateURL, String modURL) {
	    this(modName, version, updateURL, modURL, null);
	}

	private Triplet<Integer, Integer, Integer> getVersionFromStr(String version) {
	    Pattern pattern;
	    Matcher matcher;

	    String[] patterns = new String[] {
	        "\\d+\\.\\d+[\\.|_]\\d+-(\\d+)\\.(\\d+)[\\.|_](\\d+)",     // 1.7.2-1.0.4 or 1.7_01-1.5_02
	        "(\\d+)\\.(\\d+)[\\.|_](\\d+)"                             // 1.0.4 or 1.5_02
	    };

	    int i = 0;
	    boolean isValid = false;
	    do {
    	    pattern = Pattern.compile(patterns[i++]);
    	    matcher = pattern.matcher(version);
	    } while( !(isValid = matcher.find()) && i < patterns.length );

	    if( isValid ) {
	        return Triplet.with(Integer.valueOf(matcher.group(1)), Integer.valueOf(matcher.group(2)), Integer.valueOf(matcher.group(3)));
	    } else {
	        FMLLog.log(ModCntManPack.UPD_LOG, Level.WARN,
	                   "Version Number for the mod %s could not be compiled! The version number %s does not have the required formatting!",
	                   this.modName, version);
	        return Triplet.with(-1, -1, -1);
	    }
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

                    String fileVersion;
                    try( BufferedReader in = new BufferedReader(new InputStreamReader(SAPUpdateManager.this.getUpdateURL().openStream())) ) {
                        fileVersion = in.readLine();
                        if( in.ready() ) {
                            try {
                                SAPUpdateManager.this.directDL = new URL(in.readLine());
                            } catch( MalformedURLException ignored ) {  }
                        }
                    }

                    if( fileVersion == null || fileVersion.length() < 1 ) {
                        SAPUpdateManager.setChecked(SAPUpdateManager.this.getId());
                        return;
                    }

                    Triplet<Integer, Integer, Integer> webVersion = SAPUpdateManager.this.getVersionFromStr(fileVersion);
                    fileVersion = String.format("%d.%d.%d", webVersion.toArray());

                    if( webVersion.getValue0() > SAPUpdateManager.this.getVersion().getValue0() ) {
                        FMLLog.log(ModCntManPack.UPD_LOG, Level.INFO, "New major update for %s is out: %s", SAPUpdateManager.this.getModName(), fileVersion);
                        SAPUpdateManager.setHasUpdate(SAPUpdateManager.this.mgrId, fileVersion);
                        return;
                    } else if( webVersion.getValue0() == SAPUpdateManager.this.getVersion().getValue0() ) {
                        if( webVersion.getValue1() > SAPUpdateManager.this.getVersion().getValue1() ) {
                            FMLLog.log(ModCntManPack.UPD_LOG, Level.INFO, "New minor update for %s is out: %s", SAPUpdateManager.this.getModName(), fileVersion);
                            SAPUpdateManager.setHasUpdate(SAPUpdateManager.this.mgrId, fileVersion);
                            return;
                        } else if( webVersion.getValue1() == SAPUpdateManager.this.getVersion().getValue1() ) {
                            if( webVersion.getValue2() > SAPUpdateManager.this.getVersion().getValue2() ) {
                                FMLLog.log(ModCntManPack.UPD_LOG, Level.INFO, "New bugfix update for %s is out: %s", SAPUpdateManager.this.getModName(), fileVersion);
                                SAPUpdateManager.setHasUpdate(SAPUpdateManager.this.mgrId, fileVersion);
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

        (new Thread(threadProcessor, "SAP Update Thread")).start();
	}

	public void checkForUpdate() {
		if( !this.checkedForUpdate ) {
			this.check();
			this.checkedForUpdate = true;
		}
	}

    public Triplet<Integer, Integer, Integer> getVersion() {
        return this.version;
    }

    public String getFormattedVersion() {
        return String.format("%1$d.%2$d.%3$d", this.version.toArray());
    }

    public Quartet<String, Integer, Integer, Integer> getUpdVersion() {
        return this.newVersion;
    }

    public String getFormattedUpdVersion() {
        return String.format("%1$s-%2$d.%3$d.%4$d", this.newVersion.toArray());
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

    public URL getDirectDlLink() {
        return this.directDL;
    }

    public String getModJar() {
        return this.modPackedJar;
    }

    public int getId() {
        return this.mgrId;
    }
}
