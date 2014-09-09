/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.util.client;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;

import java.util.List;

public class SAPClientUtils
{
    @SuppressWarnings("unchecked")
    public static void addEffectWithNoLimit(EffectRenderer effectRenderer, EntityFX particle) {
        ((List[]) ObfuscationReflectionHelper.getPrivateValue(EffectRenderer.class, effectRenderer, "fxLayers", ""))[particle.getFXLayer()].add(particle);
    }
}
