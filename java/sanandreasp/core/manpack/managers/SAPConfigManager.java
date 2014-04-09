package sanandreasp.core.manpack.managers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import sanandreasp.core.manpack.helpers.SAPUtils;

/**
 * A Configuration manager for Minecraft modifications. It comes with an auto-assign feature for block and item IDs. Version 2.0
 * is completely rewritten and supports now UTF encoded files. Also it's easier to handle, because you need only one method for
 * different property datatypes, if you use the non-primitive ones for your properties like Integer or Float. There are wrapper
 * methods for primitive datatypes like int or float (including primitive arrays like int[] or float[]).
 * @author sanandreasp
 * @version 2.0
 */
public class SAPConfigManager
{
	private static boolean registeredItems[] = new boolean[Item.itemsList.length];
	private static boolean registeredBlocks[] = new boolean[Block.blocksList.length];
	private static boolean preRegisteredItems[] = new boolean[Item.itemsList.length];
	private static boolean preRegisteredBlocks[] = new boolean[Block.blocksList.length];
	
	private Map<String, SAPGroup> groups = new HashMap<String, SAPGroup>();
	private File configFile;
	private String modName;
	
	/**
	 * The basic constructor to initialize the config manager.
	 * 
	 * @param par1ModName The mod name.
	 * @param par2Path The relative path to the config file. Note that the absolute path incl. file will be
	 * {@code "[minecraft location]/config/[your path]/[mod name].txt"}.
	 * 
	 * @throws RuntimeException if the config file can't be written or read (for example if it's write-protected)
	 * 
	 * @see SAPConfigManager
	 */
	public SAPConfigManager(String par1ModName, String par2Path) {
		initConfigManager(par1ModName, par2Path, par1ModName+".txt");
	}
	
	/**
	 * An extended constructor to initialize the config manager.
	 * 
	 * @param par1ModName The mod name.
	 * @param par2ConfigName The filename (incl. custom extension) of the config file.
	 * @param par3Path The relative path to the config file. Note that the absolute path incl. file will be
	 * {@code "[minecraft location]/config/[your path]/[your filename]"}.
	 * 
	 * @throws RuntimeException if the config file can't be written or read (for example if it's write-protected)
	 * 
	 * @see SAPConfigManager
	 */
	public SAPConfigManager(String par1ModName, String par2ConfigName, String par3Path) {
		initConfigManager(par1ModName, par3Path, par2ConfigName);
	}
	
	private void initConfigManager(String par1ModName, String par2Path, String par3FileName) {
		addGroup("Block IDs");
		addGroup("Item IDs");
		addGroup("Achievement IDs");
		addGroup("Enchantment IDs");
		
		modName = par1ModName;
		configFile = SAPUtils.getMCDir("/config"+par2Path);
		configFile.mkdirs();
		
		configFile = new File(configFile, par3FileName);
		
		if( (!configFile.canRead() || !configFile.canWrite()) && configFile.exists() )
			throw new RuntimeException(String.format("[SAP-ConfigMan] Can't read or write File for %s!", modName));
	}
	
	/**
	 * Adds a group to the current config manager instance. Note that the groups {@code "Block IDs"}, {@code "Item IDs"} and
	 * {@code "Achievement IDs"} already exist!
	 * 
	 * @param par1Name The name of the new group.
	 */
	public void addGroup(String par1Name) {
		if( groups.containsKey(par1Name) )
			FMLLog.log("SAP-ConfigManager", Level.WARNING, "Group %s already exists for %s! Skipping.", par1Name, modName);
		else
			groups.put(par1Name, new SAPGroup(par1Name));
	}
	
	/**
	 * Adds a property value for the specified group.
	 * 
	 * @param par1PropName The name of the property.
	 * @param par2GroupName the group name assigned to the property.
	 * @param par3Value The default value for the property.
	 * 
	 * @see DataTypes
	 */
	public void addProperty(String par1PropName, String par2GroupName, Object par3Value) {
		if( !groups.containsKey(par2GroupName) ) {
			FMLLog.log("SAP-ConfigManager", Level.WARNING, "Group %s cannot be found for %s!", par2GroupName, modName);
			return;
		}
		groups.get(par2GroupName).addProp(par1PropName, par3Value);
	}

	/**
	 * Adds multiple properties for the specified group. Note that the arrays for the property names and values must have the exact same length!
	 * 
	 * @param par1PropNames An array which contains the property names.
	 * @param par2GroupName The group name assigned to the properties.
	 * @param par3Values An array which contains the default property values for each property.
	 * 
	 * @see DataTypes
	 */
	public void addProperties(String[] par1PropNames, String par2GroupName, Object par3Values) {
		this.addProperties(par1PropNames, par2GroupName, getArray(par3Values));
	}

	/**
	 * Adds multiple properties for the specified group. Note that the arrays for the property names and values must have the exact same length!
	 * 
	 * @param par1PropNames An array which contains the property names.
	 * @param par2GroupName The group name assigned to the properties.
	 * @param par4Values An array which contains the default property values for each property.
	 * 
	 * @see DataTypes
	 */
	public void addProperties(String[] par1PropNames, String par2GroupName, Object[] par3Values) {
		if( par3Values == null ) {
			FMLLog.log("SAP-ConfigManager", Level.WARNING, "Values array is null in %s for %s!", par2GroupName, modName);
			return;
		}
		if( par1PropNames.length != par3Values.length ) {
			FMLLog.log("SAP-ConfigManager", Level.WARNING, "Property name and value lengths are not equal in %s for %s!", par2GroupName, modName);
			return;
		}
		for( int i = 0; i < par1PropNames.length; i++ ) {
			groups.get(par2GroupName).addProp(par1PropNames[i], par3Values[i]);
		}
	}
	
	private final Class<?>[] ARRAY_PRIMITIVE_TYPES = { 
		        int[].class, float[].class, double[].class, boolean[].class, 
		        byte[].class, short[].class, long[].class, char[].class
	        };

	private Object[] getArray(Object val){
	    Class<?> valKlass = val.getClass();
	    Object[] outputArray = null;

	    for( Class<?> arrKlass : ARRAY_PRIMITIVE_TYPES ){
	        if( valKlass.isAssignableFrom(arrKlass) ) {
	            int arrlength = Array.getLength(val);
	            outputArray = new Object[arrlength];
	            for( int i = 0; i < arrlength; ++i ) {
	                outputArray[i] = Array.get(val, i);
	            }
	            break;
	        }
	    }
	    if( outputArray == null ) // not primitive type array
	        outputArray = (Object[])val;

	    return outputArray;
	}

	/**
	 * Gets a property value from a specified property in a group.
	 * 
	 * @param par1PropName The name of the property.
	 * @param par2GroupName The name of the group.
	 * 
	 * @return The property value, if the group exist.
	 */
	public SAPProp getSAPProperty(String par1PropName, String par2GroupName) {
		if( !groups.containsKey(par2GroupName) ) {
			FMLLog.log("SAP-ConfigManager", Level.WARNING, "Group %s cannot be found for %s!", par2GroupName, modName);
			return null;
		}
		return groups.get(par2GroupName).getProp(par1PropName);
	}
	
	/**
	 * Gets an array of property values from the specified property names in a group.
	 * @param par1PropNames An array which contains the property names.
	 * @param par2GroupName The name of the group.
	 * @return An array of property values, if the group exist.
	 */
	private SAPProp[] getSAPProperties(String[] par1PropNames, String par2GroupName) {
		if( !groups.containsKey(par2GroupName) ) {
			FMLLog.log("SAP-ConfigManager", Level.WARNING, "Group %s cannot be found for %s!", par2GroupName, modName);
			return null;
		}
		SAPProp[] values = new SAPProp[par1PropNames.length];
		for( int i = 0; i < par1PropNames.length; i++ )
			values[i] = groups.get(par2GroupName).getProp(par1PropNames[i]);
		return values;
	}
	
	/**
	 * A wrapper method of getProperties(...) for arrays with the primitive long datatype.
	 * @param par1PropNames An array which contains the property names.
	 * @param par2GroupName The name of the group.
	 * @return A long array of property values, if the group exist.
	 */
	public long[] getPropertiesLong(String[] par1PropNames, String par2GroupName) {
		SAPProp[] inArray = getSAPProperties(par1PropNames, par2GroupName);
		long[] retArray = new long[inArray.length];
		for( int i = 0; i < inArray.length; i++) retArray[i] = inArray[i].getLong( );
		return retArray;
	}

	/**
	 * A wrapper method of getProperties(...) for arrays with the primitive int datatype.
	 * @param par1PropNames An array which contains the property names.
	 * @param par2GroupName The name of the group.
	 * @return An int array of property values, if the group exist.
	 */
	public int[] getPropertiesInt(String[] par1PropNames, String par2GroupName) {
		SAPProp[] inArray = getSAPProperties(par1PropNames, par2GroupName);
		int[] retArray = new int[inArray.length];
		for( int i = 0; i < inArray.length; i++) retArray[i] = inArray[i].getInt( );
		return retArray;
	}
	
	/**
	 * A wrapper method of getProperties(...) for arrays with the primitive short datatype.
	 * @param par1PropNames An array which contains the property names.
	 * @param par2GroupName The name of the group.
	 * @return A short array of property values, if the group exist.
	 */
	public short[] getPropertiesShort(String[] par1PropNames, String par2GroupName) {
		SAPProp[] inArray = getSAPProperties(par1PropNames, par2GroupName);
		short[] retArray = new short[inArray.length];
		for( int i = 0; i < inArray.length; i++) retArray[i] = inArray[i].getShort( );
		return retArray;
	}
	
	/**
	 * A wrapper method of getProperties(...) for arrays with the primitive byte datatype.
	 * @param par1PropNames An array which contains the property names.
	 * @param par2GroupName The name of the group.
	 * @return A byte array of property values, if the group exist.
	 */
	public byte[] getPropertiesByte(String[] par1PropNames, String par2GroupName) {
		SAPProp[] inArray = getSAPProperties(par1PropNames, par2GroupName);
		byte[] retArray = new byte[inArray.length];
		for( int i = 0; i < inArray.length; i++) retArray[i] = inArray[i].getByte( );
		return retArray;
	}
	
	/**
	 * A wrapper method of getProperties(...) for arrays with the primitive float datatype.
	 * @param par1PropNames An array which contains the property names.
	 * @param par2GroupName The name of the group.
	 * @return A float array of property values, if the group exist.
	 */
	public float[] getPropertiesFloat(String[] par1PropNames, String par2GroupName) {
		SAPProp[] inArray = getSAPProperties(par1PropNames, par2GroupName);
		float[] retArray = new float[inArray.length];
		for( int i = 0; i < inArray.length; i++) retArray[i] = inArray[i].getFloat( );
		return retArray;
	}
	
	/**
	 * A wrapper method of getProperties(...) for arrays with the primitive double datatype.
	 * @param par1PropNames An array which contains the property names.
	 * @param par2GroupName The name of the group.
	 * @return A double array of property values, if the group exist.
	 */
	public double[] getPropertiesDouble(String[] par1PropNames, String par2GroupName) {
		SAPProp[] inArray = getSAPProperties(par1PropNames, par2GroupName);
		double[] retArray = new double[inArray.length];
		for( int i = 0; i < inArray.length; i++) retArray[i] = inArray[i].getDouble( );
		return retArray;
	}
	
	/**
	 * A wrapper method of getProperties(...) for arrays with the primitive char datatype.
	 * @param par1PropNames An array which contains the property names.
	 * @param par2GroupName The name of the group.
	 * @return A char array of property values, if the group exist.
	 */
	public char[] getPropertiesChar(String[] par1PropNames, String par2GroupName) {
		SAPProp[] inArray = getSAPProperties(par1PropNames, par2GroupName);
		char[] retArray = new char[inArray.length];
		for( int i = 0; i < inArray.length; i++) retArray[i] = inArray[i].getChar( );
		return retArray;
	}
	
	/**
	 * A wrapper method of getProperties(...) for arrays with the primitive boolean datatype.
	 * @param par1PropNames An array which contains the property names.
	 * @param par2GroupName The name of the group.
	 * @return A boolean array of property values, if the group exist.
	 */
	public boolean[] getPropertiesBool(String[] par1PropNames, String par2GroupName) {
		SAPProp[] inArray = getSAPProperties(par1PropNames, par2GroupName);
		boolean[] retArray = new boolean[inArray.length];
		for( int i = 0; i < inArray.length; i++) retArray[i] = inArray[i].getBool( );
		return retArray;
	}
	
	/**
	 * A wrapper method of getProperties(...) for arrays with the primitive boolean datatype.
	 * @param par1PropNames An array which contains the property names.
	 * @param par2GroupName The name of the group.
	 * @return A boolean array of property values, if the group exist.
	 */
	public String[] getPropertiesStr(String[] par1PropNames, String par2GroupName) {
		SAPProp[] inArray = getSAPProperties(par1PropNames, par2GroupName);
		String[] retArray = new String[inArray.length];
		for( int i = 0; i < inArray.length; i++) retArray[i] = inArray[i].getString( );
		return retArray;
	}
	
	/**
	 * Adds one or more new properties in the {@code "Block IDs"} group. The datatype will always be an integer.
	 * The default value is an auto-assigned block ID.
	 * @param par1PropName The name of the property.
	 */
	public void addNewBlockIDs(String... par1PropNames) {
		for( String name : par1PropNames )
			this.addProperty(name, "Block IDs", this.getFreeBlockID());
	}

	/**
	 * Adds one or more new properties in the {@code "Item IDs"} group. The datatype will always be an integer.
	 * The default value is an auto-assigned item ID.
	 * @param par1PropName The name of the property.
	 */
	public void addNewItemIDs(String... par1PropNames) {
		for( String name : par1PropNames )
			this.addProperty(name, "Item IDs", this.getFreeItemID());
	}
	
	/**
	 * Adds new properties in the {@code "Block IDs"} group. The datatype will always be an integer.
	 * The default values are pre-defined block IDs.
	 * @param par1PropName An array with the names of the property.
	 * @param par2Values An array of pre-defined values.
	 */
	public void addStaticBlockIDs(String[] par1PropNames, int[] par2Values) {
		if( par1PropNames.length == par2Values.length ) {
			this.addProperties(par1PropNames, "Block IDs", par2Values);
		}
	}
	
	/**
	 * Adds new properties in the {@code "Block IDs"} group. The datatype will always be an integer.
	 * The default values are pre-defined block IDs.
	 * @param par1PropName An array with the names of the property.
	 * @param par2Values An array of pre-defined values.
	 */
	public void addStaticBlockIDs(String[] par1PropNames, Integer[] par2Values) {
		if( par1PropNames.length == par2Values.length ) {
			this.addProperties(par1PropNames, "Block IDs", par2Values);
		}
	}

	/**
	 * Adds new properties in the {@code "Item IDs"} group. The datatype will always be an integer.
	 * The default values are pre-defined item IDs.
	 * @param par1PropName An array with the names of the property.
	 * @param par2Values An array of pre-defined values.
	 */
	public void addStaticItemIDs(String[] par1PropNames, int[] par2Values) {
		if( par1PropNames.length == par2Values.length ) {
			this.addProperties(par1PropNames, "Item IDs", par2Values);
		}
	}

	/**
	 * Adds new properties in the {@code "Item IDs"} group. The datatype will always be an integer.
	 * The default values are pre-defined item IDs.
	 * @param par1PropName An array with the names of the property.
	 * @param par2Values An array of pre-defined values.
	 */
	public void addStaticItemIDs(String[] par1PropNames, Integer[] par2Values) {
		if( par1PropNames.length == par2Values.length ) {
			this.addProperties(par1PropNames, "Item IDs", par2Values);
		}
	}
	
	/**
	 * Adds new properties in the {@code "Achievement IDs"} group. The datatype will always be an integer.
	 * The default values are pre-defined achievement IDs.
	 * @param par1PropName An array with the names of the property.
	 * @param par2Values An array of pre-defined values.
	 */
	public void addAchievementIDs(String[] par1PropNames, int[] par2Values) {
		if( par1PropNames.length == par2Values.length ) {
			this.addProperties(par1PropNames, "Achievement IDs", par2Values);
		}
	}
	
	/**
	 * Adds new properties in the {@code "Achievement IDs"} group. The datatype will always be an integer.
	 * The default values are pre-defined achievement IDs.
	 * @param par1PropName An array with the names of the property.
	 * @param par2Values An array of pre-defined values.
	 */
	public void addAchievementIDs(String[] par1PropNames, Integer[] par2Values) {
		if( par1PropNames.length == par2Values.length ) {
			this.addProperties(par1PropNames, "Achievement IDs", par2Values);
		}
	}

	/**
	 * Adds a new property in the {@code "Block IDs"} group. The datatype will always be an integer.
	 * @param par1PropName The name of the property.
	 * @param par2BlockID The default block ID.
	 */
	public void addStaticBlockID(String par1PropName, int par2BlockID) {
		this.addProperty(par1PropName, "Block IDs", par2BlockID);
	}

	/**
	 * Adds a new property in the {@code "Item IDs"} group. The datatype will always be an integer.
	 * @param par1PropName The name of the property.
	 * @param par2ItemID The default item ID.
	 */
	public void addStaticItemID(String par1PropName, int par2ItemID) {
		this.addProperty(par1PropName, "Item IDs", par2ItemID);
	}

	/**
	 * Adds a new property in the {@code "Achievement IDs"} group. The datatype will always be an integer.
	 * @param par1PropName The name of the property.
	 * @param par2AchievementID The default item ID.
	 */
	public void addAchievementID(String par1PropName, int par2AchievementID) {
		this.addProperty(par1PropName, "Achievement IDs", par2AchievementID);
	}
	
	/**
	 * Gets the property value from the specified property inside the {@code "Block IDs"} group.
	 * @param par1PropName The name of the property.
	 * @return The property value as an int
	 */
	public int getBlockID(String par1PropName) {
		return this.getSAPProperty(par1PropName, "Block IDs").getInt();
	}
	
	/**
	 * Gets the property values from the specified properties inside the {@code "Block IDs"} group.
	 * @param par1PropNames The names of the properties.
	 * @return The property values as an int array
	 */
	public int[] getBlockIDs(String... par1PropNames) {
		return getPropertiesInt(par1PropNames, "Block IDs");
	}
	
	/**
	 * Gets the property value from the specified property inside the {@code "Item IDs"} group.
	 * @param par1PropName The name of the property.
	 * @return The property value as an int
	 */
	public int getItemID(String par1PropName) {
		return this.getSAPProperty(par1PropName, "Item IDs").getInt();
	}
	
	/**
	 * Gets the property values from the specified properties inside the {@code "Item IDs"} group.
	 * @param par1PropNames The name of the properties.
	 * @return The property values as an int array
	 */
	public int[] getItemIDs(String... par1PropNames) {
		return this.getPropertiesInt(par1PropNames, "Item IDs");
	}

	/**
	 * Gets the property value from the specified property inside the {@code "Achievement IDs"} group.
	 * @param par1PropName The name of the property.
	 * @return The property value as an int
	 */
	public int getAchievementID(String par1PropName) {
		return this.getSAPProperty(par1PropName, "Achievement IDs").getInt();
	}

	/**
	 * Gets the property values from the specified properties inside the {@code "Achievement IDs"} group.
	 * @param par1PropNames The name of the properties.
	 * @return The property values as an int array
	 */
	public int[] getAchievementIDs(String... par1PropNames) {
		return this.getPropertiesInt(par1PropNames, "Achievement IDs");
	}

	/**
	 * Gets a free block ID from the Block.blocksList array which isn't occupied by anything.
	 * @return a free block ID
	 * @throws RuntimeException If there are no block IDs left.
	 */
	public int getFreeBlockID() {
		for( int i = Block.blocksList.length-1; i >= 0; i-- ) {
			if( Block.blocksList[i] == null && !registeredBlocks[i] && !preRegisteredBlocks[i] ) {
				preRegisteredBlocks[i] = true;
				return i;
			}
		}
		throw new RuntimeException(String.format("[SAP_ConfManII] No more free block IDs left for %s! Maybe too many mods installed?", modName));
	}

	/**
	 * Gets a free item ID from the Item.itemsList array which isn't occupied by anything.
	 * @return a free item ID
	 * @throws RuntimeException If there are no item IDs left
	 */
	public int getFreeItemID() {
		for( int i = Item.itemsList.length-1; i >= 0; i-- ) {
			if( Item.itemsList[i] == null && !registeredItems[i] && !preRegisteredItems[i] ) {
				preRegisteredItems[i] = true;
				return i;
			}
		}
		throw new RuntimeException(String.format("[SAP_ConfManII] No more free item IDs left for %s! Maybe too many mods installed?", modName));
	}
	
	private void writeNewFile(boolean update) {
		if( update ) {
			FMLLog.log("SAP-ConfigManager", Level.INFO, "Updating %s config file!", this.modName);
		} else {
			FMLLog.log("SAP-ConfigManager", Level.INFO, "Creating %s config file!", this.modName);
		}
		
		if( configFile.exists() )
			configFile.delete();
		
		try {
			OutputStreamWriter osr = new OutputStreamWriter(new FileOutputStream(configFile), "UTF-8");
			BufferedWriter out = new BufferedWriter(new FileWriter(configFile));
			
			String title = String.format(" %s Configuration File, made by SanAndreasPs ConfigManager", this.modName);
			String line = "";
			for( int i = 0; i < title.length()+1; i++ ) line += "-";

			out.write(line+"\n");
			out.write(title+"\n");
			out.write(line+"\n");
			for( SAPGroup group : this.groups.values() ) {
				if( !group.props.isEmpty() ) {
					out.write(String.format("[ %s ]", group.groupName)+"\n");
					for( int i = 0; i < group.props.size(); i++ ) {
						SAPProp prop = group.props.row(i).values().iterator().next();
						Object value = prop.value;
						if( !update && group.groupName.equals("Block IDs") && (registeredBlocks[(Integer ) value]
								|| Block.blocksList[(Integer) value] != null)) {
							FMLLog.log("SAP-ConfigManager", Level.WARNING, "Block ID %s already occupied! Reassign free one for %s", value, this.modName);
							value = new Integer(this.getFreeBlockID());
						}
						if( !update && group.groupName.equals("Item IDs") && (registeredItems[(Integer ) value]
								|| Item.itemsList[(Integer) value] != null)) {
							FMLLog.log("SAP-ConfigManager", Level.WARNING, "Item ID %s already occupied! Reassign free one for %s", value, this.modName);
							value = new Integer(this.getFreeItemID());
						}
							
						out.write(String.format("#   %s = %s", prop.propName, value.toString())+"\n");
						if( group.groupName.equals("Block IDs") && !update )
							registeredBlocks[(Integer) value] = true;
						else if( group.groupName.equals("Item IDs") && !update )
							registeredItems[(Integer) value] = true;
					}
				}
			}
			
			osr.close();
			out.close();

			if( update ) {
				FMLLog.log("SAP-ConfigManager", Level.INFO, "%s config file successfully updated!", this.modName);
			} else {
				FMLLog.log("SAP-ConfigManager", Level.INFO, "%s config file successfully created!", this.modName);
			}
		} catch (IOException e) {
			if( update ) {
				FMLLog.log("SAP-ConfigManager", Level.WARNING, "An error occured during updating %s config file!", this.modName);
			} else {
				FMLLog.log("SAP-ConfigManager", Level.WARNING, "An error occured during creating %s config file!", this.modName);
			}
			e.printStackTrace();
		}
	}
	
	private boolean isValueLegit(SAPProp par1Group, String par2Value) {
		try {
			switch(par1Group.dataType) {
				case STRING:
					return true;
				case LONG:
					Long.decode(par2Value);
				case INT:
					Integer.decode(par2Value);
					break;
				case SHORT:
					Short.decode(par2Value);
					break;
				case BYTE:
					Byte.decode(par2Value);
					break;
				case FLOAT:
					Float.parseFloat(par2Value);
					break;
				case DOUBLE:
					Double.parseDouble(par2Value);
					break;
				case CHAR:
					if( par2Value.length() == 1 ) {
						return true;
					}
					break;
				default:
					break;
			}
		} catch(NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	private <T> T getParsedValue(String par1Value, DataTypes par2DataType) {
		switch(par2DataType) {
			case LONG: return (T) Long.decode(par1Value);
			case INT: return (T) Integer.decode(par1Value);
			case SHORT: return (T) Short.decode(par1Value);
			case BYTE: return (T) Byte.decode(par1Value);
			case DOUBLE: return (T) Double.valueOf(par1Value);
			case FLOAT: return (T) Float.valueOf(par1Value);
			case BOOL: return (T) Boolean.valueOf(par1Value);
			case STRING: return (T) par1Value;
			case CHAR: return (T) Character.valueOf(par1Value.charAt(0));
		}
		return null;
	}
	
	public void loadConfig() {
		if( !configFile.exists() ) {
			writeNewFile(false);
		} else {
			try {
				boolean updateFile = false;
				
				BufferedReader var1In = new BufferedReader(new UnicodeInputStreamReader(new FileInputStream(configFile), "UTF-8"));
				for( int i = 0; i < 3; i++) var1In.readLine( );
				
				String currLine = "";
				String var2String = "";
				
				Map<String, Boolean> groupsMap = new HashMap<String, Boolean>();
				for( SAPGroup group : groups.values()) groupsMap.put(group.groupName, Boolean.valueOf(group.props.isEmpty()) );
				
				while(var1In.ready()) {
					var2String = currLine.isEmpty() ? var1In.readLine() : currLine;
					Matcher matcher = Pattern.compile("\\[ {0,}(.*?) {0,}\\]").matcher(var2String);
					
					if( matcher.find() ) {
						String var3GroupName = matcher.group(1);
						if( this.groups.containsKey(var3GroupName) ) {
							SAPGroup var4Group = this.groups.get(var3GroupName);
							
							Map<String, Boolean> var5Map = new HashMap<String, Boolean>();
							for( SAPProp var6Prop : var4Group.props.values()) var5Map.put(var6Prop.propName, Boolean.FALSE );
							
							boolean finishedGroup = false;
							while(var1In.ready() && !finishedGroup) {
								var2String = var1In.readLine();
								matcher = Pattern.compile("# {0,}(.*?) {0,}= {0,}(.*)").matcher(var2String);
								
								if( matcher.find() ) {
									String var8Key = matcher.group(1);
									String var9Value = matcher.group(2);
									
									if( var4Group.props.containsColumn(var8Key) ) {
										SAPProp prop = var4Group.getProp(var8Key);
										if( this.isValueLegit(prop, var9Value) ) {
											Object value = getParsedValue(var9Value, prop.getDataType());
											if( var4Group.groupName.equals("Block IDs") ) {
												if( Block.blocksList[(Integer) value] != null || registeredBlocks[(Integer) value] ) {
													FMLLog.log("SAP-ConfigManager", Level.SEVERE, "Block ID conflict with property %s in %s config file! Change this block ID!",
															var8Key, this.modName);
												}
												registeredBlocks[(Integer) value] = true;
											} else if( var4Group.groupName.equals("Item IDs") ) {
												if( Item.itemsList[(Integer) value] != null || registeredItems[(Integer) value] ) {
													FMLLog.log("SAP-ConfigManager", Level.SEVERE, "Item ID conflict with property %s in %s config file! Change this item ID!",
															var8Key, this.modName);
												}
												registeredItems[(Integer) value] = true;
											}
											SAPProp var10Prop = new SAPProp(var8Key, value);
											var4Group.updateProp(var10Prop.propName, var10Prop);
											var5Map.put(prop.propName, Boolean.TRUE);
										} else {
											FMLLog.log("SAP-ConfigManager", Level.WARNING, "Cannot assign value %s for property %s in %s config file! Invalid value!",
													var9Value, var8Key, this.modName);
											var5Map.put(prop.propName, Boolean.FALSE);
										}
									} else {
										FMLLog.log("SAP-ConfigManager", Level.WARNING, "Cannot find property %s in group %s from %s config file! Skipping!",
												var8Key, var4Group.groupName, this.modName);
									}
								} else {
									finishedGroup = true;
									currLine = var2String;
								}
							}
							
							if( var5Map.containsValue(Boolean.FALSE) ) {
								FMLLog.log("SAP-ConfigManager", Level.WARNING, "One or more properties are missing or invalid in %s config file!",
										this.modName);
								updateFile = true;
							}
							groupsMap.put(var3GroupName, Boolean.TRUE);
						} else {
							FMLLog.log("SAP-ConfigManager", Level.WARNING, "Cannot find group %s from %s config file! Skipping!",
									var3GroupName, this.modName);
							currLine = "";
						}
					}
				}
				
				if( groupsMap.containsValue(Boolean.FALSE) ) {
					FMLLog.log("SAP-ConfigManager", Level.WARNING, "One or more groups are missing in %s config file!",
							this.modName);
					updateFile = true;
				}
				
				var1In.close();
				
				if( updateFile )
					writeNewFile(true);
				else
					FMLLog.log("SAP-ConfigManager", Level.INFO, "%s config file successfully readed!", this.modName);
			} catch (FileNotFoundException e) {
				writeNewFile(false);
			} catch (IOException e) {
				e.printStackTrace();
				writeNewFile(true);
			}
		}
	}
	
	/**
	 * An enumerator to determine the datatype for a property. Available are:
	 * <ul>
	 * <li><b>LONG</b> {@link java.lang.Long}</li>
	 * <li><b>INT</b> {@link java.lang.Integer}</li>
	 * <li><b>SHORT</b> {@link java.lang.Short}</li>
	 * <li><b>BYTE</b> {@link java.lang.Byte}</li>
	 * <li><b>DOUBLE</b> {@link java.lang.Double}</li>
	 * <li><b>FLOAT</b> {@link java.lang.Float}</li>
	 * <li><b>STRING</b> {@link java.lang.String}</li>
	 * <li><b>CHAR</b> {@link java.lang.Character}</li>
	 * <li><b>BOOL</b> {@link java.lang.Boolean}</li>
	 * </ul>
	 * @author sanandreasp
	 *
	 */
	public static enum DataTypes {
		LONG(Long.class, long.class),
		INT(Integer.class, int.class),
		SHORT(Short.class, short.class),
		BYTE(Byte.class, byte.class),
		DOUBLE(Double.class, double.class),
		FLOAT(Float.class, float.class),
		STRING(String.class),
		CHAR(Character.class, char.class),
		BOOL(Boolean.class, boolean.class);

		@SuppressWarnings("rawtypes")
		private final Class[] dtClass;

		@SuppressWarnings("rawtypes")
		private DataTypes(Class... clazz) {
			dtClass = clazz;
		}
		
		@SuppressWarnings("rawtypes")
		public boolean isDTClass(Class clazz) {
			for( Class cls : dtClass ) {
				if( clazz == cls )
					return true;
			}
			return false;
		}
	}
	
	public static class SAPGroup {
		public final String groupName;
		public Table<Integer, String, SAPProp> props = HashBasedTable.create();//new HashMap<String, SAPProp>();
		public SAPGroup(String par1Name) {
			groupName = par1Name;
		}
		
		public void addProp(String par1PropName, Object par3Value) {
			if( par3Value == null ) {
				FMLLog.log("SAP-ConfigManager", Level.WARNING, "Property value cannot be null for property %s in group %s!",
						par1PropName, groupName);
			} else {
				props.put(props.size(), par1PropName, new SAPProp(par1PropName, par3Value));
			}
		}
		
		public SAPProp getProp(String par1Name) {
			if( !props.containsColumn(par1Name) ) {
				FMLLog.log("SAP-ConfigManager", Level.WARNING, "Property %s cannot be found!", par1Name);
				return null;
			}
			return props.column(par1Name).values().iterator().next();
		}
		
		public SAPProp getSortedProp(int id, String par1Name) {
			if( !props.row(id).containsKey(par1Name) ) {
				FMLLog.log("SAP-ConfigManager", Level.WARNING, "Property %s cannot be found!", par1Name);
				return null;
			}
			return props.row(id).get(par1Name);
		}
		
		public void updateProp(String name, SAPProp value) {
			int row = this.props.column(name).keySet().iterator().next();
			this.props.row(row).put(name, value);
		}
	}
	
	public static class SAPProp {
		private final String propName;
		private final Object value;
		private final DataTypes dataType;
		
		public SAPProp(String par1PropName, Object par2Value, DataTypes par3DataType) {
			this.propName = par1PropName;
			this.value = par2Value;
			if( par3DataType == null ) {
				FMLLog.log("SAP-ConfigManager", Level.SEVERE, "Value %s is not supported!", par2Value.getClass());
				throw new RuntimeException();
			}
			this.dataType = par3DataType;
		}
		
		public SAPProp(String par1PropName, Object par2Value) {
			this(par1PropName, par2Value, getDTFromClass(par2Value.getClass()));
		}
		
		public String getPropName() {
			return this.propName;
		}
		
		public DataTypes getDataType() {
			return this.dataType;
		}
		
		@SuppressWarnings("unchecked")
		public <T>T getValue() {
			switch(this.dataType) {
				case LONG: return (T) new Long(getLong());
				case INT: return (T) new Integer(getInt());
				case SHORT: return (T) new Short(getShort());
				case BYTE: return (T) new Byte(getByte());
				case BOOL: return (T) new Boolean(getBool());
				case FLOAT: return (T) new Float(getFloat());
				case DOUBLE: return (T) new Double(getDouble());
				case CHAR: return (T) new Character(getChar());
				case STRING: return (T) new String(getString());
			}
			return null;
		}
		
		public long getLong() {
			return ((Long)this.value).longValue();
		}
		
		public int getInt() {
			return ((Integer)this.value).intValue();
		}
		
		public short getShort() {
			return ((Short)this.value).shortValue();
		}
		
		public byte getByte() {
			return ((Byte)this.value).byteValue();
		}
		
		public float getFloat() {
			return ((Float)this.value).floatValue();
		}
		
		public double getDouble() {
			return ((Double)this.value).doubleValue();
		}
		
		public boolean getBool() {
			return ((Boolean)this.value).booleanValue();
		}
		
		public String getString() {
			return (String)this.value;
		}
		
		public char getChar() {
			return (Character)this.value;
		}
		
		@SuppressWarnings("rawtypes")
		private static DataTypes getDTFromClass(Class clazz) {
			for( DataTypes dt : DataTypes.values() ) {
				if( dt.isDTClass(clazz) )
					return dt;
			}
			return null;
		}
	}
}
