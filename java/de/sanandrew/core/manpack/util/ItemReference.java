/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP, SilverChiren and CliffracerX
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

/**
 * A class to wrap an Item with additional data.
 * Like an ItemStack, but without the overhead of it and designed for the ItemRefMap.
 */
public class ItemReference
{
    private final Item itemInst_;
    private final int damage_;
    private final NBTTagCompound nbtData_;

    public ItemReference(Item item) {
        this.itemInst_ = item;
        this.damage_ = OreDictionary.WILDCARD_VALUE;
        this.nbtData_ = null;
    }

    public ItemReference(Block block) {
        this.itemInst_ = Item.getItemFromBlock(block);
        this.damage_ = OreDictionary.WILDCARD_VALUE;
        this.nbtData_ = null;
    }

    public ItemReference(Item item, int damage) {
        this.itemInst_ = item;
        this.damage_ = damage;
        this.nbtData_ = null;
    }

    public ItemReference(Block block, int damage) {
        this.itemInst_ = Item.getItemFromBlock(block);
        this.damage_ = damage;
        this.nbtData_ = null;
    }

    public ItemReference(Item item, int damage, NBTTagCompound nbt) {
        this.itemInst_ = item;
        this.damage_ = damage;
        this.nbtData_ = nbt;
    }

    public ItemReference(Block block, int damage, NBTTagCompound nbt) {
        this.itemInst_ = Item.getItemFromBlock(block);
        this.damage_ = damage;
        this.nbtData_ = nbt;
    }

    public ItemReference(ItemStack stack) {
        this.itemInst_ = stack.getItem();
        this.damage_ = stack.getItemDamage();
        this.nbtData_ = stack.getTagCompound();
    }

    public Item getItem() {
        return this.itemInst_;
    }

    public int getDamage() {
        return this.damage_;
    }

    public NBTTagCompound getNbtData() {
        return this.nbtData_ == null ? null : (NBTTagCompound) this.nbtData_.copy();
    }

    public ItemStack getReferenceAsStack() {
        ItemStack stack = new ItemStack(this.itemInst_, 1, this.damage_);
        if( this.nbtData_ != null ) {
            stack.setTagCompound((NBTTagCompound) this.nbtData_.copy());
        }

        return stack;
    }

    @Override
    public String toString() {
        return getClass().getName() + "[" + this.itemInst_.toString() + "|" + (this.damage_ == OreDictionary.WILDCARD_VALUE ? "WILDCARD" : this.damage_)
               + (this.nbtData_ != null ? this.nbtData_.toString() : "NO_NBT") + "]";
    }
}
