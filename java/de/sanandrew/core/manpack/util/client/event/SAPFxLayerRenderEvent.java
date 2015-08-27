/**
 * ****************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 * *****************************************************************************************************************
 */
package de.sanandrew.core.manpack.util.client.event;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;

@SideOnly( Side.CLIENT )
public class SAPFxLayerRenderEvent
        extends Event
{
    public final int layerId;

    public SAPFxLayerRenderEvent(int layer) {
        this.layerId = layer;
    }

    public static class Pre
            extends SAPFxLayerRenderEvent
    {
        public final Tessellator tessellator;

        public Pre(int layer, Tessellator tess) {
            super(layer);
            this.tessellator = tess;
        }
    }

    public static class Post
            extends SAPFxLayerRenderEvent
    {
        public Post(int layer) {
            super(layer);
        }
    }
}
