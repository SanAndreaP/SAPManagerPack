/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP, SilverChiren and CliffracerX
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.mod.client.render;

import de.sanandrew.core.manpack.mod.client.model.ModelSanPlayer;
import de.sanandrew.core.manpack.util.client.helpers.ItemRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderSanPlayer
        extends RenderPlayer
{
    public static final ResourceLocation TEXTURE = new ResourceLocation("sapmanpack", "textures/entity/player/SanPlayer.png");

    private ModelSanPlayer myModel = new ModelSanPlayer(0.0F, false);
    private ModelSanPlayer myModelArmor = new ModelSanPlayer(0.0F, true);

    public RenderSanPlayer() {
        super();
        this.mainModel = this.myModel;
        this.modelBipedMain = this.myModel;
        this.modelArmorChestplate = new ModelBiped();
        this.modelArmor = new ModelBiped();
    }

    @Override
    protected int shouldRenderPass(AbstractClientPlayer player, int renderPass, float partTicks) {
        if( player.inventory.getStackInSlot(0) != null && player.inventory.getStackInSlot(0) != player.getCurrentEquippedItem() && renderPass == 0 ) {
            this.myModel.body.postRender(0.0625F);
            GL11.glPushMatrix();
            GL11.glRotatef(-80.0F, 0.0F, 0.0F, 1.0F);
            GL11.glScalef(0.6F, 0.6F, 0.6F);
            GL11.glTranslatef(-1.0F, -0.4F, 0.3F);
            ItemRenderHelper.renderIconIn3D(player.inventory.getStackInSlot(0).getIconIndex(), player.inventory.getStackInSlot(0).getItem() instanceof ItemBlock, false, 0xFFFFFF);
            GL11.glPopMatrix();
        }

        this.myModel.skirt1.showModel = !(player.inventory.armorItemInSlot(1) != null && player.inventory.armorItemInSlot(1).getItem() instanceof ItemArmor);
        this.myModel.skirt2.showModel = this.myModel.skirt1.showModel;

        this.myModel.armRight2.showModel = !(player.inventory.armorItemInSlot(2) != null && player.inventory.armorItemInSlot(2).getItem() instanceof ItemArmor);
        this.myModel.armLeft2.showModel = this.myModel.armRight2.showModel;

        ItemStack itemstack = player.inventory.armorItemInSlot(3 - renderPass);

//        net.minecraftforge.client.event.RenderPlayerEvent.SetArmorModel event = new net.minecraftforge.client.event.RenderPlayerEvent.SetArmorModel(player, this, 3 - renderPass, partTicks, itemstack);
//        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
//        if (event.result != -1) {
//            return event.result;
//        }

        if (itemstack != null) {
            Item item = itemstack.getItem();

            if (item instanceof ItemArmor)
            {
                ItemArmor itemarmor = (ItemArmor)item;

                switch( renderPass  ) {
                    case 0:
                        this.bindTexture(new ResourceLocation("sapmanpack", "textures/entity/player/SanPlayer_Hat_" + itemstack.getUnlocalizedName() + ".png"));
                        break;
                    case 1:
                        this.bindTexture(new ResourceLocation("sapmanpack", "textures/entity/player/SanPlayer_Chest_" + itemstack.getUnlocalizedName() + ".png"));
                        break;
                    case 2:
                        this.bindTexture(new ResourceLocation("sapmanpack", "textures/entity/player/SanPlayer_Leggings_" + itemstack.getUnlocalizedName() + ".png"));
                        break;
                    case 3:
                        this.bindTexture(new ResourceLocation("sapmanpack", "textures/entity/player/SanPlayer_Boots_" + itemstack.getUnlocalizedName() + ".png"));
                        break;
                }

//                this.bindTexture(RenderBiped.getArmorResource(player, itemstack, renderPass, null));
                myModelArmor.hatBase.showModel = renderPass == 0;
                myModelArmor.body.showModel = renderPass == 1 || renderPass == 2;
                myModelArmor.armLeft.showModel = renderPass == 1;
                myModelArmor.armLeft2.showModel = renderPass == 1;
                myModelArmor.armRight.showModel = renderPass == 1;
                myModelArmor.armRight2.showModel = renderPass == 1;
                myModelArmor.skirt1.showModel = renderPass == 2;
                myModelArmor.skirt2.showModel = renderPass == 2;
                myModelArmor.legLeft.showModel = renderPass == 2 || renderPass == 3;
                myModelArmor.legRight.showModel = renderPass == 2 || renderPass == 3;
//                myModelArmor = net.minecraftforge.client.ForgeHooksClient.getArmorModel(player, itemstack, renderPass, myModelArmor);
                this.setRenderPassModel(myModelArmor);
                myModelArmor.onGround = this.myModel.onGround;
                myModelArmor.isRiding = this.myModel.isRiding;
                myModelArmor.isChild = this.myModel.isChild;
                myModelArmor.isSneak = this.myModel.isSneak;

                //Move outside if to allow for more then just CLOTH
                int j = itemarmor.getColor(itemstack);
                if (j != -1)
                {
                    float f1 = (j >> 16 & 255) / 255.0F;
                    float f2 = (j >> 8 & 255) / 255.0F;
                    float f3 = (j & 255) / 255.0F;
                    GL11.glColor3f(f1, f2, f3);

                    if (itemstack.isItemEnchanted())
                    {
                        return 15;
                    }

                    return 1;
                }

                GL11.glColor3f(1.0F, 1.0F, 1.0F);

                if (itemstack.isItemEnchanted())
                {
                    return 15;
                }

                return 1;
            }
        }

        return -1;
    }

    @Override
    protected ResourceLocation getEntityTexture(AbstractClientPlayer clientPlayer) {
        return TEXTURE;
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
                this.bindTexture(TEXTURE);
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
