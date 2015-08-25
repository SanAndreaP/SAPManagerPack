/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.util;

public enum ReflectionNames
{
    RENDER_HAND("renderHand", "func_78476_b"),
    FIELD_150914_C("field_150914_c", "field_150914_c");

    public final String mcpName;
    public final String srgName;

    ReflectionNames(String mcp, String srg) {
        this.mcpName = mcp;
        this.srgName = srg;
    }
}
