package sanandreasp.core.manpack;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.io.Reader;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.ReflectionHelper.UnableToFindMethodException;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

/**
 * A helper class for common used stuff, which is not found somewhere else and not as easy and short to write.
 * It's goal is to shorten and cleanse the sourcecode with unnecessary stuff.
 * @author SanAndreasP
 *
 */
public class CommonUsedStuff {
	/**
	 * A static variable to decrease paperwork. Import it as following:<br>
	 * <code>import static sanandreasp.mods.managers.CommonUsedStuff.CUS;</code>
	**/
	public static CommonUsedStuff CUS;
	
	/**
	 * decreases the ItemStack stackSize by 1
	 * @param is ItemStack, whose stackSize will be decreased
	 * @return The stackSize-decreased ItemStack or null if stackSize <= 0
	**/
	public static ItemStack decrStackSize(ItemStack is) {
		return decrStackSize(is, 1);
	}

	/**
	 * decreases the ItemStack stackSize by the amount value
	 * @param is ItemStack, whose stackSize will be decreased
	 * @param amount The amount which will be subtracted from the stackSize
	 * @return The stackSize-decreased ItemStack or null if stackSize <= 0
	**/
	public static ItemStack decrStackSize(ItemStack is, int amount) {
		is.stackSize -= amount;
		if(is.stackSize <= 0) {
			return null;
		}
		return is;
	}
	
	/**
	 * Compares two ItemStacks if they are equal. If one of the ItemStacks has the block wildcard as damage value,
	 * only the itemIDs will be compared.
	 * @param is1 The first ItemStack
	 * @param is2 the second ItemStack
	 * @return true, if stacks are equal, false otherwise.
	 */
	public static boolean areStacksEqualWithWCV(ItemStack is1, ItemStack is2) {
		if(is1.isItemEqual(is2))
			return true;
		if(is1.getItemDamage() == OreDictionary.WILDCARD_VALUE || is2.getItemDamage() == OreDictionary.WILDCARD_VALUE)
			return is1.itemID == is2.itemID;
		return false;
	}
	
	/**
	 * Splits the ItemStack into multiple ("good") ones if stackSize > getMaxStackSize().
	 * @param is The ItemStack which shall be splitted into "good" ItemStacks
	 * @return An Array of "good" ItemStacks. If stackSize was smaller than the maxStackSize, the original ItemStack is in Field 0 inside the Array.
	 */
	public static ItemStack[] getGoodItemStacks(ItemStack is) {
		List<ItemStack> isMap = new ArrayList<ItemStack>();
		if(is.stackSize <= is.getMaxStackSize() && is.stackSize > 0) {
			isMap.add(is);
		} else if(is.stackSize > 0) {
			int stk = is.stackSize;
			for(int i = 0; i < MathHelper.ceiling_float_int((float) is.stackSize / (float) is.getMaxStackSize()); i++) {
				ItemStack is1 = is.copy();
				if(stk > is.getMaxStackSize()) {
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
		return maxVal - Math.round((float)(maxVal-minVal) / 2F);
	}
	
	public static ItemStack getSilkBlock(Block block, int meta) {
		try {
			Method method = ReflectionHelper.findMethod(Block.class, block, new String[] {"createStackedBlock", "func_71880_c_"}, int.class);
			return (ItemStack) method.invoke(block, meta);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (UnableToFindMethodException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ItemStack dropBlockAsItem_do(Block block, World world, int x, int y, int z, ItemStack stack) {
		EntityItem item = new EntityItem(world, x+0.5D, y+0.5D, z+0.5D, stack);
		world.spawnEntityInWorld(item);
		return null;
	}
	
	public static void dropXpOnBlockBreak(Block block, World world, int x, int y, int z, int xp) {
		try {
			Method method = ReflectionHelper.findMethod(Block.class, block, new String[] {"dropXpOnBlockBreak", "func_71923_g"}, World.class, int.class, int.class, int.class, int.class);
			method.invoke(block, world, x, y, z, xp);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (UnableToFindMethodException e) {
			e.printStackTrace();
		}
	}

	public static void dropBlockXP(Block block, World world, int X, int Y, int Z, int meta, int fortune) {

        if (block.idDropped(meta, world.rand, fortune) != block.blockID)
        {
            int j1 = 0;

            if (block.blockID == Block.oreCoal.blockID)
                j1 = MathHelper.getRandomIntegerInRange(world.rand, 0, 2);
            else if (block.blockID == Block.oreDiamond.blockID)
                j1 = MathHelper.getRandomIntegerInRange(world.rand, 3, 7);
            else if (block.blockID == Block.oreEmerald.blockID)
                j1 = MathHelper.getRandomIntegerInRange(world.rand, 3, 7);
            else if (block.blockID == Block.oreLapis.blockID)
                j1 = MathHelper.getRandomIntegerInRange(world.rand, 2, 5);
            else if (block.blockID == Block.oreNetherQuartz.blockID)
                j1 = MathHelper.getRandomIntegerInRange(world.rand, 2, 5);
            else if (block.blockID == Block.oreRedstone.blockID || block.blockID == Block.oreRedstoneGlowing.blockID)
            	j1 = 1 + world.rand.nextInt(5);
            else if (block.blockID == Block.mobSpawner.blockID)
            	j1 = 15 + world.rand.nextInt(15) + world.rand.nextInt(15);

            dropXpOnBlockBreak(block, world, X, Y, Z, j1);
        }
	}
	
	public static boolean isItemInStackArray(ItemStack base, ItemStack... stackArray) {
		for(ItemStack stack : stackArray) {
			if(base != null && stack != null && areStacksEqualWithWCV(base, stack))
				return true;
		}
		return false;
	}
	
	public static Block[] getToolBlocks(ItemTool tool) {
		return ObfuscationReflectionHelper.getPrivateValue(ItemTool.class, tool, "blocksEffectiveAgainst", "field_77863_c");
	}
	
	public static boolean isToolEffective(Block[] effectives, Block block) {
		for(Block currBlock : effectives) {
			if(block == currBlock)
				return true;
		}
		return false;
	}
	
	public static <T> T[] getArrayFromList(List<T> list, Class clazz) {
		if(list.size() == 0)
			return null;
		return list.toArray((T[])Array.newInstance(clazz, list.size()));
	}
	
	public static ItemStack addItemStackToInventory(ItemStack is, IInventory inv) {
		int invSize = inv.getSizeInventory() - (inv instanceof InventoryPlayer ? 4 : 0);
		for(int i1 = 0; i1 < invSize && is != null; i1++) {
			ItemStack invIS = inv.getStackInSlot(i1);
			if(invIS != null && is.isItemEqual(invIS)) {
				int combinedCount = is.stackSize + invIS.stackSize;
				int maxStack = Math.min(invIS.getMaxStackSize(), inv.getInventoryStackLimit());
				if(combinedCount <= maxStack) {
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
		for(int i2 = 0; i2 < invSize && is != null; i2++) {
			ItemStack invIS = inv.getStackInSlot(i2);
			if(invIS == null && inv.isStackValidForSlot(i2, is)) {
				if(is.stackSize <= inv.getInventoryStackLimit()) {
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
		if(FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			return new File(Minecraft.getMinecraftDir(), path);
		}
		return new File(".", path);
	}
}
