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

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

public class SAPUpdateManager {
	private boolean checkedForUpdate = false;
	private int majNmbr, minNmbr, revNmbr;
	private String updateURL, modName, modURL;
	
	public static List<SAPUpdateManager> updMgrs = new ArrayList<SAPUpdateManager>();
	
	public SAPUpdateManager(String md, int mj, int mn, int rev, String updURL, String mdURL) {
		this.modName = md;
		this.majNmbr = mj;
		this.minNmbr = mn;
		this.revNmbr = rev;
		this.updateURL = updURL;
		this.modURL = mdURL;
		this.updMgrs.add(this);
	}
	
	public String getFormattedVersion() {
		Formatter form = new Formatter();
		String ver = form.format("%1$d.%2$02d_%3$02d", this.majNmbr, this.minNmbr, this.revNmbr).toString();
		form.close();
	    return ver;
	}
	
	private void addMessage(String s, Level level) {
		if(FMLCommonHandler.instance().getSide().isClient()) {
			Minecraft.getMinecraft().thePlayer.addChatMessage(s);
		}
		FMLLog.log("SAP-UpdateManager", level, s.replaceAll("\247.", ""));
	}
	
	private void check() {
		final String mName = this.modName;
		final String mUrl = this.modURL;
		final String udUrl = this.updateURL;
		final int maj = this.majNmbr;
		final int min = this.minNmbr;
		final int rev = this.revNmbr;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					FMLLog.log("SAP-UpdateManager", Level.INFO, "Checking for %s Update", mName);
				    URL url = new URL(udUrl);
				    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
				    String str = in.readLine();
				    
				    in.close();
				    
				    if(str == null || str.length() < 1) return;
				    
				    Pattern pattern = Pattern.compile("major:(\\d+);minor:(\\d+);revision:(\\d+)");
				    Matcher matches = pattern.matcher(str);
				    
				    int[] webVer = new int[] {0, 0, 0};
				    
				    if(matches.find()) {
				    	webVer[0] = Integer.valueOf(matches.group(1));
				    	webVer[1] = Integer.valueOf(matches.group(2));
				    	webVer[2] = Integer.valueOf(matches.group(3));
				    }
				    
					Formatter form = new Formatter();
					String newVer = form.format("%1$d.%2$02d_%3$02d", webVer[0], webVer[1], webVer[2]).toString();
					form.close();
				    			    
				    if(webVer[0] > maj) {
				    	addMessage(String.format("\247cNew major update (%s) for \2476%s \247cis out:", newVer, modName), Level.WARNING);
				    	addMessage("\247c"+mUrl, Level.WARNING);
				    } else if(webVer[0] == maj && webVer[1] > min) {
				    	addMessage(String.format("\247eNew feature update (%s) for \2476%s \247eis out:", newVer, modName), Level.INFO);
				    	addMessage("\247e"+mUrl, Level.INFO);
				    } else if(webVer[0] == maj && webVer[1] == min && webVer[2] > rev) {
				    	addMessage(String.format("\247bNew bugfix update (%s) for \2476%s \247bis out:", newVer, modName), Level.INFO);
				    	addMessage("\247b"+mUrl, Level.INFO);
				    } else {
				    	FMLLog.log("SAP-UpdateManager", Level.INFO, "No new update for %s available. Everything is fine.", mName);
				    }
				} catch (MalformedURLException e) {
					FMLLog.log("SAP-UpdateManager", Level.WARNING, "Update Check for %s failed - Malformed URL: >>%s<<", mName, udUrl);
			    } catch (IOException e) {
					FMLLog.log("SAP-UpdateManager", Level.WARNING, "Update Check for %s failed - Not accessible: >>%s<<", mName, udUrl);
			    }
			}
		}).start();
	}
	
	public void checkForUpdate() {
		if(!this.checkedForUpdate) {
			this.check();
			this.checkedForUpdate = true;
		}
	}
}