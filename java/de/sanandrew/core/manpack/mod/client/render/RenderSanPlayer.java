/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.mod.client.render;

import com.google.gson.Gson;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.sanandrew.core.manpack.init.ManPackLoadingPlugin;
import de.sanandrew.core.manpack.mod.client.model.ModelSanPlayer;
import de.sanandrew.core.manpack.util.client.helpers.AverageColorHelper;
import de.sanandrew.core.manpack.util.client.helpers.ItemRenderHelper;
import de.sanandrew.core.manpack.util.client.helpers.ModelBoxBuilder;
import de.sanandrew.core.manpack.util.helpers.SAPUtils.RGBAValues;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.GL11;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@SideOnly( Side.CLIENT )
public class RenderSanPlayer
        extends RenderPlayer
        implements IResourceManagerReloadListener
{
    public static final ResourceLocation TEXTURE = new ResourceLocation("sapmanpack", "textures/entity/player/SanPlayer.png");
    public static final ResourceLocation TEXTURE_SLEEP = new ResourceLocation("sapmanpack", "textures/entity/player/SanPlayer_sleeping.png");

    private ModelSanPlayer myModel = new ModelSanPlayer(0.0F, false);
    private ModelSanPlayer myModelArmor = new ModelSanPlayer(0.05F, true);

    private Map<String, RGBAValues> unknownTextureColorMap = new HashMap<>();
    private Map<String, CubeLoader> hatRenderList = new HashMap<>();

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

        ItemStack stack = player.inventory.armorItemInSlot(3 - renderPass);

        if( renderPass == 0 ) {
            this.myModel.hideTails = false;
        }

        if( stack != null ) {
            Item item = stack.getItem();
            this.myModelArmor.hatBase = null;

            if( item instanceof ItemArmor ) {
                ItemArmor armorItem = (ItemArmor) item;
                String unlocName = stack.getUnlocalizedName().replace(':', '_');

                switch( renderPass ) {
                    case 0:
                        this.bindTexture(tryLoadArmorPiece("Hat", unlocName, player, stack, renderPass));
                        if( !this.hatRenderList.containsKey(unlocName) ) {
                            CubeLoader cubes = CubeLoader.loadFromResource(unlocName);
                            cubes.initCubeInstances(this.myModelArmor);
                            hatRenderList.put(unlocName, cubes);
                        }

                        this.myModelArmor.hatBase = this.hatRenderList.get(unlocName).getCubeParent();
                        this.myModel.hideTails = this.hatRenderList.get(unlocName).hideTails;
                        break;
                    case 1:
                        this.bindTexture(tryLoadArmorPiece("Chest", unlocName, player, stack, renderPass));
                        break;
                    case 2:
                        this.bindTexture(tryLoadArmorPiece("Leggings", unlocName, player, stack, renderPass));
                        break;
                    case 3:
                        this.bindTexture(tryLoadArmorPiece("Boots", unlocName, player, stack, renderPass));
                        break;
                }

//                myModelArmor.hatBase.showModel = renderPass == 0;
                myModelArmor.body.showModel = renderPass == 1 || renderPass == 2;
                myModelArmor.bipedLeftArm.showModel = renderPass == 1;
                myModelArmor.armLeft2.showModel = renderPass == 1;
                myModelArmor.bipedRightArm.showModel = renderPass == 1;
                myModelArmor.armRight2.showModel = renderPass == 1;
                myModelArmor.skirt1.showModel = renderPass == 2;
                myModelArmor.skirt2.showModel = renderPass == 2;
                myModelArmor.legLeft.showModel = renderPass == 2 || renderPass == 3;
                myModelArmor.legRight.showModel = renderPass == 2 || renderPass == 3;
                this.setRenderPassModel(myModelArmor);
                myModelArmor.onGround = this.myModel.onGround;
                myModelArmor.isRiding = this.myModel.isRiding;
                myModelArmor.isChild = this.myModel.isChild;
                myModelArmor.isSneak = this.myModel.isSneak;
                myModelArmor.aimedBow = this.myModel.aimedBow;
                myModelArmor.heldItemLeft = this.myModel.heldItemLeft;
                myModelArmor.heldItemRight = this.myModel.heldItemRight;

                int armorColor = armorItem.getColor(stack);
                if( armorColor != -1 ) {
                    float red = (armorColor >> 16 & 255) / 255.0F;
                    float green = (armorColor >> 8 & 255) / 255.0F;
                    float blue = (armorColor & 255) / 255.0F;
                    GL11.glColor3f(red, green, blue);
                } else if( this.unknownTextureColorMap.containsKey(unlocName) ) {
                    RGBAValues rgba = this.unknownTextureColorMap.get(unlocName);
                    GL11.glColor3f(rgba.getRed() / 255.0F, rgba.getGreen() / 255.0F, rgba.getBlue() / 255.0F);
                } else {
                    GL11.glColor3f(1.0F, 1.0F, 1.0F);
                }

                if( stack.isItemEnchanted() ) {
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
        ItemStack slot = player.inventory.getStackInSlot(0);
        if( slot != null && slot != player.getCurrentEquippedItem() ) {
            GL11.glPushMatrix();
            GL11.glRotatef(-80.0F, 0.0F, 0.0F, 1.0F);
            GL11.glScalef(0.6F, 0.6F, 0.6F);
            GL11.glTranslatef(-1.0F, -0.4F, 0.3F);
            ItemRenderHelper.renderItemIn3D(slot);
            GL11.glPopMatrix();
            GL11.glTranslatef(0.0F, 0.0F, 0.05F);
        }

        slot = player.inventory.getStackInSlot(1);
        if( slot != null && slot != player.getCurrentEquippedItem() ) {
            GL11.glPushMatrix();
            GL11.glRotatef(-10.0F, 0.0F, 0.0F, 1.0F);
            GL11.glScalef(0.6F, 0.6F, 0.6F);
            GL11.glTranslatef(-0.6F, -0.0F, 0.3F);
            ItemRenderHelper.renderItemIn3D(slot);
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
        boolean isRidingPrev = this.modelBipedMain.isRiding;
        this.modelBipedMain.isRiding = false;
        this.modelBipedMain.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, player);
        this.modelBipedMain.isRiding = isRidingPrev;
        if( this.renderManager.renderEngine != null ) {
            if( player.getCurrentArmor(2) != null ) {
                this.bindTexture(this.getEntityTexture(player));
                this.myModel.bipedLeftArm.render(0.0625F);
                String armoredChest = player.getCurrentArmor(2).getUnlocalizedName().replace(':', '_');
                boolean prevArmR2Visible = this.myModel.armRight2.showModel;

                Minecraft.getMinecraft().getTextureManager().bindTexture(tryLoadArmorPiece("Chest", armoredChest, player, player.getCurrentArmor(2), 1));

                this.myModel.bipedRightArm.render(0.0625F);
                GL11.glPushMatrix();
                GL11.glScalef(1.05F, 1.05F, 1.05F);
                GL11.glTranslatef(0.015F, 0.00F, 0.0F);
                int armorColor = ((ItemArmor) player.getCurrentArmor(2).getItem()).getColor(player.getCurrentArmor(2));
                if( armorColor != -1 ) {
                    float red = (armorColor >> 16 & 255) / 255.0F;
                    float green = (armorColor >> 8 & 255) / 255.0F;
                    float blue = (armorColor & 255) / 255.0F;
                    GL11.glColor3f(red, green, blue);
                } else if( this.unknownTextureColorMap.containsKey(armoredChest) ) {
                    RGBAValues rgba = this.unknownTextureColorMap.get(armoredChest);
                    GL11.glColor3f(rgba.getRed() / 255.0F, rgba.getGreen() / 255.0F, rgba.getBlue() / 255.0F);
                }

                this.myModel.armRight2.showModel = true;
                this.myModel.armRight2.render(0.0625F);
                this.myModel.armRight2.showModel = prevArmR2Visible;
                GL11.glColor3f(1.0F, 1.0F, 1.0F);
                GL11.glPopMatrix();
            } else {
                boolean prevArmR2Visible = this.myModel.armRight2.showModel;

                this.bindTexture(this.getEntityTexture(player));
                this.myModel.bipedRightArm.render(0.0625F);
                GL11.glPushMatrix();
                GL11.glScalef(1.05F, 1.05F, 1.05F);
                GL11.glTranslatef(0.015F, 0.00F, 0.0F);
                this.myModel.armRight2.showModel = true;
                this.myModel.armRight2.render(0.0625F);
                this.myModel.armRight2.showModel = prevArmR2Visible;
                GL11.glPopMatrix();
            }
        }
    }

    private ResourceLocation tryLoadArmorPiece(String part, String unlocName, EntityPlayer player, ItemStack stack, int pass) {
        ResourceLocation resLoc;
        if( this.unknownTextureColorMap.containsKey(unlocName) ) {
            resLoc = new ResourceLocation("sapmanpack", "textures/entity/player/SanPlayer_" + part + "_unknown.png");
        } else {
            resLoc = new ResourceLocation("sapmanpack", "textures/entity/player/SanPlayer_" + part + '_' + unlocName + ".png");
            try {
                Minecraft.getMinecraft().getResourceManager().getResource(resLoc);
            } catch( IOException ex ) {
                ManPackLoadingPlugin.MOD_LOG.printf(Level.WARN, "Can't load armor texture for item %s!", unlocName);
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(String.format("Can't load armor texture for item %s!", unlocName)));
                resLoc = RenderBiped.getArmorResource(player, stack, pass, null);
                try( InputStream textureStream = Minecraft.getMinecraft().getResourceManager().getResource(resLoc).getInputStream() ) {
                    this.unknownTextureColorMap.put(unlocName, AverageColorHelper.getAverageColor(textureStream));
                } catch( IOException ex2 ) {
                    ManPackLoadingPlugin.MOD_LOG.printf(Level.WARN, "Can't get avg. color for armor texture %s!", unlocName);
                    ManPackLoadingPlugin.MOD_LOG.log(Level.WARN, "", ex2);
                    this.unknownTextureColorMap.put(unlocName, new RGBAValues(255, 255, 255, 255));
                }

                resLoc = new ResourceLocation("sapmanpack", "textures/entity/player/SanPlayer_" + part + "_unknown.png");
            }
        }

        return resLoc;
    }

    @Override
    public void onResourceManagerReload(IResourceManager resManager) {
        this.unknownTextureColorMap.clear();
        this.hatRenderList.clear();
    }

    public static class CubeLoader
    {
        public CubeLoaderCube[] cubes = new CubeLoaderCube[0];
        public boolean hideTails;
        private ModelRenderer[] cubeInsts = new ModelRenderer[1];

        public static CubeLoader loadFromResource(String unlocName) {
            try( BufferedReader in = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager()
                                                                                       .getResource(new ResourceLocation("sapmanpack", "model/hats/" + unlocName + ".json"))
                                                                                       .getInputStream())) )
            {
                return new Gson().fromJson(in, CubeLoader.class);
            } catch( IOException ex ) {
                ManPackLoadingPlugin.MOD_LOG.printf(Level.WARN, "Can't load hat model for item %s!", unlocName);
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(String.format("Can't load hat model for item %s!", unlocName)));
                return new CubeLoader();
            }
        }

        public void initCubeInstances(ModelBase model) {
            this.cubeInsts = new ModelRenderer[this.cubes.length];
            ModelRenderer parent = null;
            for( int index = 0; index < this.cubes.length; index++ ) {
                CubeLoaderCube cubeDef = this.cubes[index];
                this.cubeInsts[index] = ModelBoxBuilder.newBuilder(model).setTexture(cubeDef.textureX, cubeDef.textureY, cubeDef.mirror)
                                                       .setLocation(cubeDef.rotationPointX, cubeDef.rotationPointY, cubeDef.rotationPointZ)
                                                       .setRotation(cubeDef.rotationX, cubeDef.rotationY, cubeDef.rotationZ)
                                                       .getBox(cubeDef.boxX, cubeDef.boxY, cubeDef.boxZ, cubeDef.sizeX, cubeDef.sizeY,
                                                               cubeDef.sizeZ, cubeDef.scale);
                if( index == 0 ) {
                    parent = this.cubeInsts[index];
                } else {
                    parent.addChild(this.cubeInsts[index]);
                }
            }
        }

        public ModelRenderer getCubeParent() {
            return this.cubeInsts.length > 0 ? this.cubeInsts[0] : null;
        }
    }

    public static class CubeLoaderCube
    {
        public int textureX;
        public int textureY;
        public boolean mirror;
        public float boxX;
        public float boxY;
        public float boxZ;
        public int sizeX;
        public int sizeY;
        public int sizeZ;
        public float rotationPointX;
        public float rotationPointY;
        public float rotationPointZ;
        public float rotationX;
        public float rotationY;
        public float rotationZ;
        public float scale;
    }
}
