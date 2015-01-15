/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP, SilverChiren and CliffracerX
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.mod.client.render;

import cpw.mods.fml.common.FMLLog;
import de.sanandrew.core.manpack.mod.ModCntManPack;
import de.sanandrew.core.manpack.mod.client.model.ModelSanPlayer;
import de.sanandrew.core.manpack.util.client.helpers.AverageColorHelper;
import de.sanandrew.core.manpack.util.client.helpers.ItemRenderHelper;
import de.sanandrew.core.manpack.util.helpers.SAPUtils.RGBAValues;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RenderSanPlayer
        extends RenderPlayer
{
    public static final ResourceLocation TEXTURE = new ResourceLocation("sapmanpack", "textures/entity/player/SanPlayer.png");
    public static final ResourceLocation TEXTURE_SLEEP = new ResourceLocation("sapmanpack", "textures/entity/player/SanPlayer_sleeping.png");

    private ModelSanPlayer myModel = new ModelSanPlayer(0.0F, false);
    private ModelSanPlayer myModelArmor = new ModelSanPlayer(0.05F, true);

    private Map<String, RGBAValues> unknownTextureColorMap = new HashMap<>();

    public RenderSanPlayer() {
        super();
        this.mainModel = this.myModel;
        this.modelBipedMain = this.myModel;
        this.modelArmorChestplate = new ModelBiped();
        this.modelArmor = new ModelBiped();
    }

    @Override
    protected int shouldRenderPass(AbstractClientPlayer player, int renderPass, float partTicks) {
        this.myModel.skirt1.showModel = !(player.inventory.armorItemInSlot(1) != null && player.inventory.armorItemInSlot(1).getItem() instanceof ItemArmor);
        this.myModel.skirt2.showModel = this.myModel.skirt1.showModel;

        this.myModel.armRight2.showModel = !(player.inventory.armorItemInSlot(2) != null && player.inventory.armorItemInSlot(2).getItem() instanceof ItemArmor);
        this.myModel.armLeft2.showModel = this.myModel.armRight2.showModel;

        ItemStack itemstack = player.inventory.armorItemInSlot(3 - renderPass);

        if (itemstack != null) {
            Item item = itemstack.getItem();

            if (item instanceof ItemArmor)
            {
                ItemArmor itemarmor = (ItemArmor)item;
                String unlocName = itemstack.getUnlocalizedName();

                if( this.unknownTextureColorMap.containsKey(unlocName) ) {
                    switch( renderPass  ) {
                        case 0:
                            this.bindTexture(new ResourceLocation("sapmanpack", "textures/entity/player/SanPlayer_Hat_unknown.png"));
                            break;
                        case 1:
                            this.bindTexture(new ResourceLocation("sapmanpack", "textures/entity/player/SanPlayer_Chest_unknown.png"));
                            break;
                        case 2:
                            this.bindTexture(new ResourceLocation("sapmanpack", "textures/entity/player/SanPlayer_Leggings_unknown.png"));
                            break;
                        case 3:
                            this.bindTexture(new ResourceLocation("sapmanpack", "textures/entity/player/SanPlayer_Boots_unknown.png"));
                            break;
                    }
                } else {
                    ResourceLocation resLoc = null;
                    switch( renderPass ) {
                        case 0:
                            resLoc = new ResourceLocation("sapmanpack", "textures/entity/player/SanPlayer_Hat_" + unlocName + ".png");
                            try {
                                Minecraft.getMinecraft().getResourceManager().getResource(resLoc);
                            } catch( IOException ex ) {
                                FMLLog.log(ModCntManPack.MOD_LOG, Level.WARN, "Can't load hat texture for item %s!", unlocName);
                                this.unknownTextureColorMap.put(unlocName, AverageColorHelper.getAverageColor(RenderBiped.getArmorResource(player, itemstack, renderPass, null)));
                                resLoc = new ResourceLocation("sapmanpack", "textures/entity/player/SanPlayer_Hat_unknown.png");
                            }
                            break;
                        case 1:
                            resLoc = new ResourceLocation("sapmanpack", "textures/entity/player/SanPlayer_Chest_" + unlocName + ".png");
                            try {
                                Minecraft.getMinecraft().getResourceManager().getResource(resLoc);
                            } catch( IOException ex ) {
                                FMLLog.log(ModCntManPack.MOD_LOG, Level.WARN, "Can't load hat texture for item %s!", unlocName);
                                this.unknownTextureColorMap.put(unlocName, AverageColorHelper.getAverageColor(RenderBiped.getArmorResource(player, itemstack, renderPass, null)));
                                resLoc = new ResourceLocation("sapmanpack", "textures/entity/player/SanPlayer_Chest_unknown.png");
                            }
                            break;
                        case 2:
                            resLoc = new ResourceLocation("sapmanpack", "textures/entity/player/SanPlayer_Leggings_" + unlocName + ".png");
                            try {
                                Minecraft.getMinecraft().getResourceManager().getResource(resLoc);
                            } catch( IOException ex ) {
                                FMLLog.log(ModCntManPack.MOD_LOG, Level.WARN, "Can't load hat texture for item %s!", unlocName);
                                this.unknownTextureColorMap.put(unlocName, AverageColorHelper.getAverageColor(RenderBiped.getArmorResource(player, itemstack, renderPass, null)));
                                resLoc = new ResourceLocation("sapmanpack", "textures/entity/player/SanPlayer_Leggings_unknown.png");
                            }
                            break;
                        case 3:
                            resLoc = new ResourceLocation("sapmanpack", "textures/entity/player/SanPlayer_Boots_" + unlocName + ".png");
                            try {
                                Minecraft.getMinecraft().getResourceManager().getResource(resLoc);
                            } catch( IOException ex ) {
                                FMLLog.log(ModCntManPack.MOD_LOG, Level.WARN, "Can't load hat texture for item %s!", unlocName);
                                this.unknownTextureColorMap.put(unlocName, AverageColorHelper.getAverageColor(RenderBiped.getArmorResource(player, itemstack, renderPass, null)));
                                resLoc = new ResourceLocation("sapmanpack", "textures/entity/player/SanPlayer_Boots_unknown.png");
                            }
                            break;
                    }

                    this.bindTexture(resLoc);
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
                myModelArmor.aimedBow = this.myModel.aimedBow;
                myModelArmor.heldItemLeft = this.myModel.heldItemLeft;
                myModelArmor.heldItemRight = this.myModel.heldItemRight;

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
                } else if( this.unknownTextureColorMap.containsKey(unlocName) ) {
                    RGBAValues rgba = this.unknownTextureColorMap.get(unlocName);
                    GL11.glColor3f(rgba.getRed() / 255.0F, rgba.getGreen() / 255.0F, rgba.getBlue() / 255.0F);

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
    protected void renderEquippedItems(AbstractClientPlayer player, float partTicks) {
        super.renderEquippedItems(player, partTicks);

        GL11.glPushMatrix();
        this.myModel.body.postRender(0.0625F);
        if( player.inventory.getStackInSlot(0) != null && player.inventory.getStackInSlot(0) != player.getCurrentEquippedItem() ) {
            GL11.glPushMatrix();
            GL11.glRotatef(-80.0F, 0.0F, 0.0F, 1.0F);
            GL11.glScalef(0.6F, 0.6F, 0.6F);
            GL11.glTranslatef(-1.0F, -0.4F, 0.3F);
            ItemRenderHelper.renderIconIn3D(player.inventory.getStackInSlot(0).getIconIndex(), player.inventory.getStackInSlot(0).getItem() instanceof ItemBlock, false, 0xFFFFFF);
            GL11.glPopMatrix();
            GL11.glTranslatef(0.0F, 0.0F, 0.05F);
        }

        if( player.inventory.getStackInSlot(1) != null && player.inventory.getStackInSlot(1) != player.getCurrentEquippedItem() ) {
            GL11.glPushMatrix();
            GL11.glRotatef(-10.0F, 0.0F, 0.0F, 1.0F);
            GL11.glScalef(0.6F, 0.6F, 0.6F);
            GL11.glTranslatef(-0.6F, -0.0F, 0.3F);
            ItemRenderHelper.renderIconIn3D(player.inventory.getStackInSlot(1).getIconIndex(), player.inventory.getStackInSlot(1).getItem() instanceof ItemBlock, false, 0xFFFFFF);
            GL11.glPopMatrix();
        }
        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(AbstractClientPlayer clientPlayer) {
        return clientPlayer.isPlayerSleeping() ? TEXTURE_SLEEP : TEXTURE;
    }

    @Override
    public void renderFirstPersonArm(EntityPlayer player) {
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        this.modelBipedMain.onGround = 0.0F;
        this.modelBipedMain.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, player);
        if( this.renderManager.renderEngine != null ) {
            if( player.getCurrentArmor(2) != null ) {
                this.bindTexture(this.getEntityTexture(player));
                this.myModel.armRight.render(0.0625F);
                String armoredChest = player.getCurrentArmor(2).getUnlocalizedName();

                if( this.unknownTextureColorMap.containsKey(armoredChest) ) {
                    Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("sapmanpack", "textures/entity/player/SanPlayer_Chest_unknown.png"));
                } else {
                    ResourceLocation resLoc = new ResourceLocation("sapmanpack", "textures/entity/player/SanPlayer_Chest_" + armoredChest + ".png");
                    try {
                        Minecraft.getMinecraft().getResourceManager().getResource(resLoc);
                    } catch( IOException ex ) {
                        FMLLog.log(ModCntManPack.MOD_LOG, Level.WARN, "Can't load hat texture for item %s!", armoredChest);
                        this.unknownTextureColorMap.put(armoredChest, AverageColorHelper.getAverageColor(RenderBiped.getArmorResource(player, player.getCurrentArmor(2), 1, null)));
                        resLoc = new ResourceLocation("sapmanpack", "textures/entity/player/SanPlayer_Chest_unknown.png");
                    }

                    Minecraft.getMinecraft().getTextureManager().bindTexture(resLoc);
                }


                this.myModel.armRight.render(0.0625F);
                GL11.glPushMatrix();
                GL11.glScalef(1.05F, 1.05F, 1.05F);
                GL11.glTranslatef(0.015F, 0.00F, 0.0F);
                int j = ((ItemArmor)player.getCurrentArmor(2).getItem()).getColor(player.getCurrentArmor(2));
                if( j != -1 ) {
                    float f1 = (j >> 16 & 255) / 255.0F;
                    float f2 = (j >> 8 & 255) / 255.0F;
                    float f3 = (j & 255) / 255.0F;
                    GL11.glColor3f(f1, f2, f3);
                } else if( this.unknownTextureColorMap.containsKey(armoredChest) ) {
                    RGBAValues rgba = this.unknownTextureColorMap.get(armoredChest);
                    GL11.glColor3f(rgba.getRed() / 255.0F, rgba.getGreen() / 255.0F, rgba.getBlue() / 255.0F);
                }

                this.myModel.armRight2.showModel = true;
                this.myModel.armRight2.render(0.0625F);
                GL11.glColor3f(1.0F, 1.0F, 1.0F);
                GL11.glPopMatrix();
            } else {
                this.bindTexture(this.getEntityTexture(player));
                this.myModel.armRight.render(0.0625F);
                GL11.glPushMatrix();
                GL11.glScalef(1.05F, 1.05F, 1.05F);
                GL11.glTranslatef(0.015F, 0.00F, 0.0F);
                this.myModel.armRight2.showModel = true;
                this.myModel.armRight2.render(0.0625F);
                GL11.glPopMatrix();
            }
        }
    }
}
