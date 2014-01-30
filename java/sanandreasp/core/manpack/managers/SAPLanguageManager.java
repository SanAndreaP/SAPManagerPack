package sanandreasp.core.manpack.managers;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sanandreasp.core.manpack.helpers.CommonUsedStuff;

import com.google.common.collect.Maps;






import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class SAPLanguageManager {
	private HashMap<String, File> langFiles = Maps.newHashMap();
	private File path;
	@SuppressWarnings("unused")
	private String version, modname;
	private List<String> localizations = new ArrayList<String>();
//	private static StringTranslate strTrans = new StringTranslate();
	
	public SAPLanguageManager(String par1Path, String par2LangVer, String par3Mod) {
		try {
			String cfgpath = "config/langs"+par1Path;
			path = CommonUsedStuff.getMCDir(cfgpath);
			if( !(path.exists()) )
				path.mkdirs();
		} catch(Throwable e) {
			e.printStackTrace();
		}
		
		version = par2LangVer;
		modname = par3Mod;
		
		File[] files = path.listFiles(new LangFileFilter());
		
		if( files == null ) return;
		
		for( File langFile : files ) {
			String lang = langFile.getName().replaceAll(".(txt|lang|SAPlang)", "");
//			if( StringTranslate.containsTranslateKey(lang) )
				langFiles.put(lang, langFile);
		}
	}
	
	public void addLangPropS(String property, String defName) {
		LanguageRegistry.instance().addStringLocalization(property, "en_US", defName);
		this.localizations.add(property);
	}
	
	public void loadLangs() {
		String[] langs = langFiles.keySet().toArray(new String[langFiles.size()]);
		for( String cLang : langs ) {
			File cFile = langFiles.get(cLang);
			if( !cFile.canRead() || this.localizations.isEmpty() )
				continue;
			
			try {
				FileInputStream fis = new FileInputStream(cFile);
				UnicodeInputStreamReader input = new UnicodeInputStreamReader(fis, "UTF-8");
				BufferedReader in = new BufferedReader(input);

				do {
					String s = in.readLine();
					if( s == null || s.isEmpty() )
						continue;
					
					if( s.matches("[ ]{0,}.*?[ ]{0,}=[ ]{0,}.*") ) {
						Matcher matcher = Pattern.compile("[ ]{0,}(.*?)[ ]{0,}=[ ]{0,}(.*)").matcher(s);
						
						if( matcher.find() ) {
							String prop = matcher.group(1);
							String value = matcher.group(2);
							
							if( this.localizations.contains(prop) ) {
								LanguageRegistry.instance().addStringLocalization(prop, cLang, value);
							}
						}
					}
				} while(in.ready());
				in.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.writeDefaultLFile();
	}
	
	private void writeDefaultLFile() {
		try {
			FileOutputStream defLangFile = new FileOutputStream(new File(path, "DEF_LANG.txt"));
			
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(defLangFile, Charset.forName("UTF-8").newEncoder()));
			out.write('\ufeff');
			
			String title = String.format(" %s Language File example, made by SanAndreasPs Language Manager", this.modname);
			String line = "";
			for( int i = 0; i < title.length()+1; i++ ) line += "-";

			out.write(line+"\n");
			out.write(title+"\n");
			out.write(line+"\n");
			
			out.write("// This is an example of the language file for the default language set by the\n// mod (english (US)) which is used as default in all Minecraft languages.\n");
			out.write("// To make your own translation based on an other language (for example spanish)\n// copy this file, rename it into the language localization you want\n");
			out.write("// (for example \"es_ES.txt\") and change the values after the 'equal'-sign (=) to\n// anything you seem it's fitting.\n");
			out.write("// Supported file extensions for the language files are '.txt',\n");
			out.write("// '.lang' and '.SAPlang'.\n// A list of all available language localizations supported by Minecraft can be found here:\n// https://dl.dropbox.com/u/56920617/languages.txt\n");
			out.write("// WARNING: This file cannot be used as language file, because it'll be overridden!\n");
			out.write("// NOTE: The %s in the localization values are variables! Keep them and place this variable somewhere you see it's fitting\n\n");
			for( String prop : this.localizations ) {
				if( !prop.isEmpty() ) {
					String value = getTranslated(prop, "en_US");
					out.write(String.format("%s = %s\n", prop, value));
				}
			}
			
			out.close();
			defLangFile.close();

		} catch (IOException e) {
			FMLLog.log("SAP-LanguageManager", Level.WARNING, "An error occured during creating %s default lang file!", this.modname);
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void addLangProp(Object obj, String defName) {
		String objectName;
		
		if( obj == null )
            throw new IllegalArgumentException(String.format("[SAP-LanguageManager] Illegal object for naming %s",obj));
		
		if( obj instanceof String ) {
            this.addLangPropS((String)obj, defName);
            return;
        } else if( obj instanceof Item ) {
            objectName=((Item)obj).getUnlocalizedName();
        } else if( obj instanceof Block ) {
            objectName=((Block)obj).getUnlocalizedName();
        } else if( obj instanceof ItemStack ) {
            objectName=((ItemStack)obj).getItem().getUnlocalizedName((ItemStack)obj);
        } else if( obj instanceof Class && (Entity.class.isAssignableFrom((Class)obj)) ){
        	objectName="entity."+((String)EntityList.classToStringMapping.get((Class)obj));
        } else {
            throw new IllegalArgumentException(String.format("[SAP-LanguageManager] Illegal object for naming %s",obj));
        }
        objectName+=".name";
        
        this.addLangPropS(objectName, defName);
	}
	
	public static String getTranslatedEn(String key) {
		return LanguageRegistry.instance().getStringLocalization(key, "en_US");
	}
	
	public static String getTranslated(String key) {
		return StatCollector.translateToLocal(key);
	}
	
	public static String getTranslated(String key, String lang) {
		return LanguageRegistry.instance().getStringLocalization(key, lang);
	}
	
	public static String getTranslatedFormat(String key, Object data) {
		return StatCollector.translateToLocal(key);
	}
	
	public static String getTranslatedFormat(String key, String lang, Object... data) {
		String str = LanguageRegistry.instance().getStringLocalization(key, lang);

		try {
            return String.format(str, data);
        } catch( IllegalFormatException illegalformatexception ) {
            return "Format error: " + str;
        }
	}

	public static class LangFileFilter implements FilenameFilter {
	
		@Override
		public boolean accept(File dir, String name) {
			if( name.endsWith(".txt") || name.endsWith(".SAPlang") || name.endsWith(".lang") )
				return true;
			else return false;
		}
		
	}
}
