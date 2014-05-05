package de.sanandrew.core.manpack.util;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.Level;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import net.minecraftforge.oredict.OreDictionary;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import de.sanandrew.core.manpack.mod.ModCntManPack;

/**
 * A helper class for common used stuff, which is not found somewhere else and
 * not as easy and short to write. It's goal is to shorten and cleanse the
 * sourcecode with unnecessary stuff.
 *
 * @author SanAndreasP
 */
public final class SAPUtils
{
    public static final Random RANDOM = new Random();

    /**
     * decreases the ItemStack stackSize by 1
     *
     * @param is
     *            ItemStack, whose stackSize will be decreased
     * @return The stackSize-decreased ItemStack or null if stackSize <= 0
     **/
    public static ItemStack decrStackSize(ItemStack is) {
        return decrStackSize(is, 1);
    }

    /**
     * decreases the ItemStack stackSize by the amount value
     *
     * @param is
     *            ItemStack, whose stackSize will be decreased
     * @param amount
     *            The amount which will be subtracted from the stackSize
     * @return The stackSize-decreased ItemStack or null if stackSize <= 0
     **/
    public static ItemStack decrStackSize(ItemStack is, int amount) {
        is.stackSize -= amount;
        if( is.stackSize <= 0 ) {
            return null;
        }
        return is;
    }

    /**
     * Compares two ItemStacks if they are equal. If one of the ItemStacks has
     * the block wildcard as damage value, only the item instances will be
     * compared.
     *
     * @param is1
     *            The first ItemStack
     * @param is2
     *            the second ItemStack
     * @return true, if stacks are equal, false otherwise.
     */
    public static boolean areStacksEqualWithWCV(ItemStack is1, ItemStack is2) {
        if( is1.isItemEqual(is2) ) {
            return true;
        }
        if( is1.getItemDamage() == OreDictionary.WILDCARD_VALUE || is2.getItemDamage() == OreDictionary.WILDCARD_VALUE ) {
            return is1.getItem() == is2.getItem();
        }
        return false;
    }

    /**
     * Splits the ItemStack into multiple ("good") ones if stackSize >
     * getMaxStackSize().
     *
     * @param is
     *            The ItemStack which shall be splitted into "good" ItemStacks
     * @return An Array of "good" ItemStacks. If stackSize was smaller than the
     *         maxStackSize, the original ItemStack is in Field 0 inside the
     *         Array.
     */
    public static ItemStack[] getGoodItemStacks(ItemStack is) {
        List<ItemStack> isMap = new ArrayList<ItemStack>();
        if( is.stackSize <= is.getMaxStackSize() && is.stackSize > 0 ) {
            isMap.add(is);
        } else if( is.stackSize > 0 ) {
            int stk = is.stackSize;
            for( int i = 0; i < MathHelper.ceiling_float_int((float) is.stackSize / (float) is.getMaxStackSize()); i++ ) {
                ItemStack is1 = is.copy();
                if( stk > is.getMaxStackSize() ) {
                    stk -= is1.stackSize = is.getMaxStackSize();
                } else {
                    is1.stackSize = stk;
                }
                isMap.add(is1);
            }
        }
        return isMap.toArray(new ItemStack[isMap.size()]);
    }

    public static int getInBetweenVal(int var1, int var2) {
        int maxVal = Math.max(var1, var2);
        int minVal = Math.min(var1, var2);
        return maxVal - Math.round((maxVal - minVal) / 2F);
    }

    public static ItemStack getSilkBlock(Block block, int meta) {
        Class<?>[] methodPT = new Class[] { int.class };
        Object[] methodPV = new Object[] { meta };
        return SAPReflectionHelper.invokeCachedMethod(Block.class, block, "createStackedBlock", "func_71880_c_", methodPT, methodPV);
    }

    public static ItemStack dropBlockAsItem(Block block, World world, int x, int y, int z, ItemStack stack) {
        EntityItem item = new EntityItem(world, x + 0.5D, y + 0.5D, z + 0.5D, stack);
        world.spawnEntityInWorld(item);
        return null;
    }

    public static void dropBlockXP(Block block, World world, int x, int y, int z, int meta, int fortune) {
        block.dropXpOnBlockBreak(world, x, y, z, block.getExpDrop(world, meta, fortune));
    }

    public static boolean isItemInStackArray(ItemStack base, ItemStack... stackArray) {
        for( ItemStack stack : stackArray ) {
            if( base != null && stack != null && areStacksEqualWithWCV(base, stack) ) {
                return true;
            }
        }
        return false;
    }

    public static Block[] getToolBlocks(ItemTool tool) {
        return SAPReflectionHelper.getCachedFieldValue(ItemTool.class, tool, "blocksEffectiveAgainst", "field_77863_c");
    }

    public static boolean isToolEffective(Block[] effectives, Block block) {
        for( Block currBlock : effectives ) {
            if( block == currBlock ) {
                return true;
            }
        }
        return false;
    }

    public static boolean areItemInstEqual(Object instance1, Object instance2) {
        if( instance1 instanceof Item ) {
            if( instance2 instanceof Item ) {
                return instance1 == instance2;
            } else if( instance2 instanceof ItemStack ) {
                return instance1 == ((ItemStack) instance2).getItem();
            }
        } else if( instance1 instanceof Block ) {
            if( instance2 instanceof Block ) {
                return instance1 == instance2;
            } else if( instance2 instanceof ItemStack && ((ItemStack) instance2).getItem() instanceof ItemBlock ) {
                return instance1 == Block.getBlockFromItem(((ItemStack)instance2).getItem());
            }
        } else if( instance1 instanceof ItemStack ) {
            if( instance2 instanceof Block || instance2 instanceof Item ) {
                return SAPUtils.areItemInstEqual(instance2, instance1);
            } else if( instance2 instanceof ItemStack ) {
                return SAPUtils.areStacksEqualWithWCV((ItemStack) instance1, (ItemStack) instance2);
            }
        }
        return false;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> T[] getArrayFromList(List<T> list, Class clazz) {
        if( list.size() == 0 ) {
            return null;
        }
        return list.toArray((T[]) Array.newInstance(clazz, list.size()));
    }

    public static void registerBlocks(String prefix, Block... blocks) {
        int cnt = 0;
        for( Block blk : blocks ) {
            String suffix = (new String("0000" + (++cnt)));
            GameRegistry.registerBlock(blk, prefix + "_" + suffix.substring(suffix.length() - 4));
        }
    }

    public static void registerItems(String prefix, Item... items) {
        int cnt = 0;
        for( Item itm : items ) {
            String suffix = (new String("0000" + (++cnt)));
            GameRegistry.registerItem(itm, prefix + "_" + suffix.substring(suffix.length() - 4));
        }
    }

    public static ItemStack addItemStackToInventory(ItemStack is, IInventory inv) {
        int invSize = inv.getSizeInventory() - (inv instanceof InventoryPlayer ? 4 : 0);
        for( int i1 = 0; i1 < invSize && is != null; i1++ ) {
            ItemStack invIS = inv.getStackInSlot(i1);
            if( invIS != null && is.isItemEqual(invIS) ) {
                int combinedCount = is.stackSize + invIS.stackSize;
                int maxStack = Math.min(invIS.getMaxStackSize(), inv.getInventoryStackLimit());
                if( combinedCount <= maxStack ) {
                    invIS.stackSize = combinedCount;
                    inv.setInventorySlotContents(i1, invIS.copy());
                    is = null;
                    break;
                } else {
                    int rest = combinedCount - maxStack;
                    invIS.stackSize = maxStack;
                    inv.setInventorySlotContents(i1, invIS.copy());
                    is.stackSize = rest;
                }
            }
        }

        // if the given stack is not empty yet, search for an empty slot and put it there
        for( int i2 = 0; i2 < invSize && is != null; i2++ ) {
            ItemStack invIS = inv.getStackInSlot(i2);
            if( invIS == null && inv.isItemValidForSlot(i2, is) ) {
                if( is.stackSize <= inv.getInventoryStackLimit() ) {
                    inv.setInventorySlotContents(i2, is.copy());
                    is = null;
                    break;
                } else {
                    int rest = is.stackSize - inv.getInventoryStackLimit();
                    is.stackSize = inv.getInventoryStackLimit();
                    inv.setInventorySlotContents(i2, is.copy());
                    is.stackSize = rest;
                }
            }
        }
        return is;
    }

    public static File getMCDir(String path) {
        return new File(".", path);
    }

    @SideOnly(Side.CLIENT)
    public static void setSelectedBtn(GuiScreen inst, GuiButton btn) {
        SAPReflectionHelper.setCachedFieldValue(GuiScreen.class, inst, "selectedButton", "field_73883_a", btn);
    }

    @SideOnly(Side.CLIENT)
    public static GuiButton getSelectedBtn(GuiScreen inst) {
        return SAPReflectionHelper.getCachedFieldValue(GuiScreen.class, inst, "selectedButton", "field_73883_a");
    }

    @SideOnly(Side.CLIENT)
    public static String getTranslated(String key) {
        return I18n.format(key);
    }

    @SideOnly(Side.CLIENT)
    public static String getTranslated(String key, Object... data) {
        return I18n.format(key, data);
    }

    public static DamageSource getNewDamageSource(String type) {
        try {
            Constructor<DamageSource> dmgsrcConst = DamageSource.class.getDeclaredConstructor(String.class);
            dmgsrcConst.setAccessible(true);
            return dmgsrcConst.newInstance(type);
        } catch( Throwable e ) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getMaxDmgFactorAM(ItemArmor.ArmorMaterial aMaterial) {
        return SAPReflectionHelper.getCachedFieldValue(ItemArmor.ArmorMaterial.class, aMaterial, "maxDamageFactor", "field_78048_f");
    }

    public static void restartApp() throws IOException {
        try {

            String java = System.getProperty("java.home") + "/bin/javaw"; // java binary

            List<String> vmArguments = ManagementFactory.getRuntimeMXBean().getInputArguments(); // vm arguments
            StringBuffer vmArgsOneLine = new StringBuffer();
            for (String arg : vmArguments) {
                if (!arg.contains("-agentlib")) { // if it's the agent argument : we ignore it otherwise the
                    vmArgsOneLine.append(arg);    // address of the old application and the new one will be in conflict
                    vmArgsOneLine.append(" ");
                }
            }

            final StringBuffer cmd = new StringBuffer("\"" + java + "\" " + vmArgsOneLine); // init the command to execute, add the vm args

            String[] mainCommand = System.getProperty("sun.java.command").split(" "); // program main and program arguments
            if( mainCommand[0].endsWith(".jar") ) { // program main is a jar, add -jar mainJar
                cmd.append("-jar " + new File(mainCommand[0]).getPath());
            } else { // else it's a .class, add the classpath and mainClass
                cmd.append("-cp \"" + System.getProperty("java.class.path") + "\" " + mainCommand[0]);
            }

            for (int i = 1; i < mainCommand.length; i++) { // finally add program arguments
                cmd.append(" ");
                cmd.append(mainCommand[i]);
            }

            // execute the command in a shutdown hook, to be sure that all the
            // resources have been disposed before restarting the application
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                        ProcessBuilder builder = new ProcessBuilder(cmd.toString());
                        builder.inheritIO();    // inherit the console output from the super process (the process initiating the restart)
                        builder.start();        // start the new process
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            // exit
            try {
                System.out.println();
                FMLLog.log(ModCntManPack.MOD_LOG, Level.INFO, "---=== Restarting Minecraft Client! ===---");    // try to shutdown Minecraft applet
                System.out.println();
                Minecraft.getMinecraft().shutdownMinecraftApplet();
            } catch( NoClassDefFoundError ex ) {
                System.out.println();
                FMLLog.log(ModCntManPack.MOD_LOG, Level.INFO, "---=== Restarting Minecraft Server! ===---");    // if Minecraft class was not found
                System.out.println();                                                                           // (usually the case on a dedi-server),
                MinecraftServer.getServer().initiateShutdown();                                                 // then shutdown server
            }
        } catch (Throwable e) {
            throw new IOException("Error while trying to restart the application", e);
        }
    }
}
