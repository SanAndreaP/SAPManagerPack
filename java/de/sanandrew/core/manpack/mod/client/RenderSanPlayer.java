/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP, SilverChiren and CliffracerX
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.mod.client;

import de.sanandrew.core.manpack.mod.client.model.ModelSanPlayer;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderSanPlayer
        extends RenderPlayer
{
    private ResourceLocation texture = new ResourceLocation("sapmanpack", "textures/entity/player/SanPlayer.png");

    public RenderSanPlayer()
    {
        super();
        this.mainModel = new ModelSanPlayer();
        this.modelBipedMain = (ModelBiped)this.mainModel;
        this.modelArmorChestplate = new ModelBiped(1.0F);
        this.modelArmor = new ModelBiped(0.5F);
    }

    @Override
    protected ResourceLocation getEntityTexture(AbstractClientPlayer clientPlayer) {
        return this.texture;
    }

    @Override
    public void renderFirstPersonArm(EntityPlayer player) {
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        this.modelBipedMain.onGround = 0.0F;
        this.modelBipedMain.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, player);
        if( this.renderManager.renderEngine != null ) {
            this.bindTexture(this.texture);
        }
        ((ModelSanPlayer)this.modelBipedMain).armRight.render(0.0625F);
        GL11.glPushMatrix();
        GL11.glScalef(1.05F, 1.05F, 1.05F);
        GL11.glTranslatef(0.015F, 0.00F, 0.0F);
        ((ModelSanPlayer)this.modelBipedMain).armRight2.render(0.0625F);
        GL11.glPopMatrix();
    }
}
