/**
 * ****************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 * *****************************************************************************************************************
 */
package de.sanandrew.core.manpack.util;

//FIXME: REMOVE WHEN UPDATING TO NEW MC VERSION!
/**
 * A helper enum for the NBT Types.<br>
 * <b>It's deprecated, though, so use {@link net.minecraftforge.common.util.Constants.NBT} instead! Thanks.</b>
 */
@Deprecated
public enum EnumNbtTypes
{
    NBT_END,
    NBT_BYTE,
    NBT_SHORT,
    NBT_INT,
    NBT_LONG,
    NBT_FLOAT,
    NBT_DOUBLE,
    NBT_BYTE_ARRAY,
    NBT_STRING,
    NBT_LIST,
    NBT_COMPOUND,
    NBT_INT_ARRAY
}
