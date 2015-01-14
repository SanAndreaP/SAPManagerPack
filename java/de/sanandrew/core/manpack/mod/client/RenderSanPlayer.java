/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP, SilverChiren and CliffracerX
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.mod.client;

import de.sanandrew.core.manpack.mod.client.model.ModelSanPlayer;
import de.sanandrew.core.manpack.util.client.helpers.ItemRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderSanPlayer
        extends RenderPlayer
{
    public static final ResourceLocation texture = new ResourceLocation("sapmanpack", "textures/entity/player/SanPlayer.png");

    private ModelSanPlayer myModel = new ModelSanPlayer();

    public RenderSanPlayer()
    {
        super();
        this.mainModel = this.myModel;
        this.modelBipedMain = this.myModel;
        this.modelArmorChestplate = new ModelBiped(1.0F);
        this.modelArmor = new ModelBiped(0.5F);
    }

    @Override
    protected int shouldRenderPass(AbstractClientPlayer p_77032_1_, int p_77032_2_, float p_77032_3_) {
        if( p_77032_1_.getCurrentArmor(3) != null ) {
            this.myModel.armoredHat = p_77032_1_.getCurrentArmor(3).getUnlocalizedName();
        } else {
            this.myModel.armoredHat = null;
        }

        if( p_77032_1_.getCurrentArmor(2) != null ) {
            this.myModel.armoredChest = p_77032_1_.getCurrentArmor(2).getUnlocalizedName();
        } else {
            this.myModel.armoredChest = null;
        }

        if( p_77032_1_.getCurrentArmor(1) != null ) {
            this.myModel.armoredPants = p_77032_1_.getCurrentArmor(1).getUnlocalizedName();
        } else {
            this.myModel.armoredPants = null;
        }

        if( p_77032_1_.getCurrentArmor(0) != null ) {
            this.myModel.armoredFeet = p_77032_1_.getCurrentArmor(0).getUnlocalizedName();
        } else {
            this.myModel.armoredFeet = null;
        }

        if( p_77032_1_.inventory.getStackInSlot(0) != null && p_77032_1_.inventory.getStackInSlot(0) != p_77032_1_.getCurrentEquippedItem() && p_77032_2_ == 0 ) {
            this.myModel.body.postRender(0.0625F);
            GL11.glPushMatrix();
            GL11.glRotatef(-80.0F, 0.0F, 0.0F, 1.0F);
            GL11.glScalef(0.6F, 0.6F, 0.6F);
            GL11.glTranslatef(-1.0F, -0.4F, 0.3F);
            ItemRenderHelper.renderIconIn3D(p_77032_1_.inventory.getStackInSlot(0).getIconIndex(), p_77032_1_.inventory.getStackInSlot(0).getItem() instanceof ItemBlock, false, 0xFFFFFF);
            GL11.glPopMatrix();
        }

        return -1;
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
            if( player.getCurrentArmor(2) != null ) {
                String armoredChest = player.getCurrentArmor(2).getUnlocalizedName();
                if( !this.myModel.armorResources.containsKey("chest_" + armoredChest) ) {
                    this.myModel.armorResources.put("chest_" + armoredChest, new ResourceLocation("sapmanpack", "textures/entity/player/SanPlayer_Chest_" + armoredChest + ".png"));
                }

                Minecraft.getMinecraft().getTextureManager().bindTexture(this.myModel.armorResources.get("chest_" + armoredChest));
            } else {
                this.bindTexture(this.texture);
            }
        }


        ((ModelSanPlayer)this.modelBipedMain).armRight.render(0.0625F);
        GL11.glPushMatrix();
        GL11.glScalef(1.05F, 1.05F, 1.05F);
        GL11.glTranslatef(0.015F, 0.00F, 0.0F);
        ((ModelSanPlayer)this.modelBipedMain).armRight2.render(0.0625F);
        GL11.glPopMatrix();
    }
}
