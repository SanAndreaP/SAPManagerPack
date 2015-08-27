/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP, SilverChiren and CliffracerX
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.util.annotation;

/**
 * A <i>dummy</i> interface for an IDE to suppress the "unused" warning on methods and fields purely called by reflection.<br>
 * <b>It does not prevent direct calls of a method/field with this annotation!</b>
 */
public @interface UsedByReflection
{
}
