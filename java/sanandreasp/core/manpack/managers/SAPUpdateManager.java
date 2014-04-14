package sanandreasp.core.manpack.managers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.Minecraft;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;

public class SAPUpdateManager
{
	private boolean checkedForUpdate = false;
	private int majNmbr, minNmbr, revNmbr;
	private String updURL, mdName, mdURL;

	public static List<SAPUpdateManager> updMgrs = new ArrayList<SAPUpdateManager>();

	public SAPUpdateManager(String modName, int majorNr, int minorNr, int revisionNr, String updateURL, String modURL) {
		this.mdName = modName;
		this.majNmbr = majorNr;
		this.minNmbr = minorNr;
		this.revNmbr = revisionNr;
		this.updURL = updateURL;
		this.mdURL = modURL;
		SAPUpdateManager.updMgrs.add(this);
	}

	public String getFormattedVersion() {
		try( Formatter form = new Formatter() ) {
		    String ver = form.format("%1$d.%2$02d_%3$02d", this.majNmbr, this.minNmbr, this.revNmbr).toString();
		    return ver;
		}
	}

	private void addMessage(String s, Level level) {
		if( FMLCommonHandler.instance().getSide().isClient() ) {
			Minecraft.getMinecraft().thePlayer.addChatMessage(s);
		}
		FMLLog.log("SAP-UpdateManager", level, s.replaceAll("\247.", ""));
	}

	private void check() {
		final String mName = this.mdName;
		final String mUrl = this.mdURL;
		final String udUrl = this.updURL;
		final int maj = this.majNmbr;
		final int min = this.minNmbr;
		final int rev = this.revNmbr;
		new Thread(new Runnable()
			{
				@Override
				public void run() {
					try {
						FMLLog.log("SAP-UpdateManager", Level.INFO, "Checking for %s Update", mName);
					    URL url = new URL(udUrl);
					    String str = null;

					    try( BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream())) ) {
					        str = in.readLine();
					    }

					    if( str == null || str.length() < 1 ) {
                            return;
                        }

					    Pattern pattern = Pattern.compile("major:(\\d+);minor:(\\d+);revision:(\\d+)");
					    Matcher matches = pattern.matcher(str);

					    int[] webVer = new int[] {0, 0, 0};

					    if( matches.find() ) {
					    	webVer[0] = Integer.valueOf(matches.group(1));
					    	webVer[1] = Integer.valueOf(matches.group(2));
					    	webVer[2] = Integer.valueOf(matches.group(3));
					    }

					    String newVer = "[UNKNOWN]";
						try( Formatter form = new Formatter() ) {
						    newVer = form.format("%1$d.%2$02d_%3$02d", webVer[0], webVer[1], webVer[2]).toString();
						}

					    if( webVer[0] > maj ) {
					    	SAPUpdateManager.this.addMessage(String.format("\247cNew major update (%s) for \2476%s \247cis out:", newVer, SAPUpdateManager.this.mdName), Level.WARNING);
					    	SAPUpdateManager.this.addMessage("\247c"+mUrl, Level.WARNING);
					    } else if( webVer[0] == maj && webVer[1] > min ) {
					    	SAPUpdateManager.this.addMessage(String.format("\247eNew feature update (%s) for \2476%s \247eis out:", newVer, SAPUpdateManager.this.mdName), Level.INFO);
					    	SAPUpdateManager.this.addMessage("\247e"+mUrl, Level.INFO);
					    } else if( webVer[0] == maj && webVer[1] == min && webVer[2] > rev ) {
					    	SAPUpdateManager.this.addMessage(String.format("\247bNew bugfix update (%s) for \2476%s \247bis out:", newVer, SAPUpdateManager.this.mdName), Level.INFO);
					    	SAPUpdateManager.this.addMessage("\247b"+mUrl, Level.INFO);
					    } else {
					    	FMLLog.log("SAP-UpdateManager", Level.INFO, "No new update for %s available. Everything is fine.", mName);
					    }
					} catch (MalformedURLException e) {
						FMLLog.log("SAP-UpdateManager", Level.WARNING, "Update Check for %s failed - Malformed URL: >>%s<<", mName, udUrl);
				    } catch (IOException e) {
						FMLLog.log("SAP-UpdateManager", Level.WARNING, "Update Check for %s failed - Not accessible: >>%s<<", mName, udUrl);
				    }
				}
			}
		).start();
	}

	public void checkForUpdate() {
		if( !this.checkedForUpdate ) {
			this.check();
			this.checkedForUpdate = true;
		}
	}
}