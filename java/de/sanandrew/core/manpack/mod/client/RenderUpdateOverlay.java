package de.sanandrew.core.manpack.mod.client;

import net.minecraft.client.gui.Gui;

import net.minecraftforge.client.event.RenderGameOverlayEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class RenderUpdateOverlay extends Gui
{
    @SubscribeEvent
    public void renderGameOverlay( RenderGameOverlayEvent.Text event ) {

//        Tessellator tessellator = Tessellator.instance;
//        GL11.glEnable(GL11.GL_BLEND);
//        GL11.glDisable(GL11.GL_TEXTURE_2D);
//        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
////        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
//        GL11.glColor4f(1F, 1F, 1F, 0.5F);
//        tessellator.startDrawingQuads();
//        tessellator.addVertex(0, 100, 0.0D);
//        tessellator.addVertex(100, 100, 0.0D);
//        tessellator.addVertex(100, 0, 0.0D);
//        tessellator.addVertex(0, 0, 0.0D);
//        tessellator.draw();
//        GL11.glEnable(GL11.GL_TEXTURE_2D);
//        GL11.glDisable(GL11.GL_BLEND);
        drawGradientRect(0, 0, 100, 100, 0x80000000, 0xFF000000);;
    }
}
