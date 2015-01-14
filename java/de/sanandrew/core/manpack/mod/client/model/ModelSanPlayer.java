package de.sanandrew.core.manpack.mod.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;

/**
 * SanPlayer - SanAndreasP
 * Created using Tabula 4.1.1
 */
public class ModelSanPlayer extends ModelBiped
{
    public ModelRenderer armRight;
    public ModelRenderer body;
    public ModelRenderer armLeft;
    public ModelRenderer armRight2;
    public ModelRenderer armLeft2;
    public ModelRenderer legRight;
    public ModelRenderer legLeft;
    public ModelRenderer hair;
    public ModelRenderer head;
    public ModelRenderer hatBase;
    public ModelRenderer breast;
    public ModelRenderer skirt1;
    public ModelRenderer skirt2;
    public ModelRenderer quadTail1;
    public ModelRenderer quadTail2;
    public ModelRenderer quadTail3;
    public ModelRenderer quadTail4;
    public ModelRenderer shape29;
    public ModelRenderer shape30;
    public ModelRenderer shape32;

    public Map<String, ResourceLocation> armorResources = new HashMap<>();

    public String armoredHat = null;
    public String armoredChest = null;
    public String armoredPants = null;
    public String armoredFeet = null;

    private boolean isArmor;

    public ModelSanPlayer(float scaling, boolean isArmor) {
        this.isArmor = isArmor;

        this.textureWidth = 64;
        this.textureHeight = 64;
        this.legLeft = new ModelRenderer(this, 0, 16);
        this.legLeft.mirror = true;
        this.legLeft.setRotationPoint(2.5F, 12.0F, 0.0F);
        this.legLeft.addBox(-1.5F, 0.0F, -1.5F, 3, 12, 3, scaling);
        this.skirt2 = new ModelRenderer(this, 16, 44);
        this.skirt2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.skirt2.addBox(-4.5F, 11.0F, -3.0F, 9, 4, 6, 0.0F);
        this.quadTail2 = new ModelRenderer(this, 0, 40);
        this.quadTail2.setRotationPoint(0.0F, -3.0F, 0.0F);
        this.quadTail2.addBox(-1.5F, 3.0F, 1.0F, 3, 10, 3, scaling);
        this.setRotateAngle(quadTail2, 0.5235987755982988F, 0.0F, 2.2689280275926285F);
        this.hatBase = new ModelRenderer(this, 0, 0);
        this.hatBase.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.hatBase.addBox(-5.0F, -8.5F, -4.5F, 10, 2, 9, scaling);
        this.armRight = new ModelRenderer(this, 40, 16);
        this.armRight.setRotationPoint(-4.0F, 2.0F, 0.5F);
        this.armRight.addBox(-3.0F, -2.0F, -2.0F, 3, 12, 3, scaling);
        this.setRotateAngle(armRight, 0.0F, 0.0F, 0.09983283321407566F);
        this.armRight2 = new ModelRenderer(this, 40, 32);
        this.armRight2.setRotationPoint(-4.0F, 2.0F, 0.5F);
        this.armRight2.addBox(-3.0F, 5.0F, -2.0F, 3, 5, 3, scaling);
        this.setRotateAngle(armRight2, 0.0F, 0.0F, 0.09983283321407566F);
        this.shape30 = new ModelRenderer(this, 29, 0);
        this.shape30.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shape30.addBox(-2.0F, -11.5F, -2.0F, 4, 2, 4, scaling);
        this.head = new ModelRenderer(this, 28, 0);
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.head.addBox(-3.5F, -7.0F, -3.5F, 7, 7, 7, scaling);
        this.shape32 = new ModelRenderer(this, 45, 0);
        this.shape32.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shape32.addBox(-1.5F, -13.5F, -1.5F, 3, 2, 3, scaling);
        this.armLeft2 = new ModelRenderer(this, 40, 32);
        this.armLeft2.mirror = true;
        this.armLeft2.setRotationPoint(5.0F, 2.0F, 0.5F);
        this.armLeft2.addBox(-1.0F, 5.0F, -2.0F, 3, 5, 3, scaling);
        this.setRotateAngle(armLeft2, -0.0033161255787892262F, 0.0F, -0.09983283321407566F);
        this.skirt1 = new ModelRenderer(this, 16, 36);
        this.skirt1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.skirt1.addBox(-4.5F, 8.0F, -2.5F, 9, 3, 5, 0.0F);
        this.body = new ModelRenderer(this, 16, 16);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, scaling);
        this.armLeft = new ModelRenderer(this, 40, 16);
        this.armLeft.mirror = true;
        this.armLeft.setRotationPoint(5.0F, 2.0F, 0.5F);
        this.armLeft.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 3, scaling);
        this.setRotateAngle(armLeft, -0.0033161255787892262F, 0.0F, -0.09983283321407566F);
        this.hair = new ModelRenderer(this, 0, 0);
        this.hair.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.hair.addBox(-3.5F, -7.0F, -3.5F, 7, 7, 7, scaling);
        this.quadTail1 = new ModelRenderer(this, 0, 40);
        this.quadTail1.mirror = true;
        this.quadTail1.setRotationPoint(0.0F, -3.0F, 0.0F);
        this.quadTail1.addBox(-1.5F, 3.0F, 1.0F, 3, 10, 3, scaling);
        this.setRotateAngle(quadTail1, 0.5235987755982988F, 0.0F, -2.2689280275926285F);
        this.shape29 = new ModelRenderer(this, 0, 11);
        this.shape29.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shape29.addBox(-3.0F, -9.5F, -3.0F, 6, 2, 6, scaling);
        this.legRight = new ModelRenderer(this, 0, 16);
        this.legRight.setRotationPoint(-2.5F, 12.0F, 0.0F);
        this.legRight.addBox(-1.5F, 0.0F, -1.5F, 3, 12, 3, scaling);
        this.quadTail4 = new ModelRenderer(this, 0, 32);
        this.quadTail4.setRotationPoint(0.0F, -3.0F, 0.0F);
        this.quadTail4.addBox(-1.5F, 3.0F, 0.9F, 2, 6, 2, scaling);
        this.setRotateAngle(quadTail4, 0.6373942428283291F, 0.0F, 0.6108652381980153F);
        this.quadTail3 = new ModelRenderer(this, 0, 32);
        this.quadTail3.mirror = true;
        this.quadTail3.setRotationPoint(0.0F, -3.0F, 0.0F);
        this.quadTail3.addBox(-0.5F, 3.0F, 0.9F, 2, 6, 2, scaling);
        this.setRotateAngle(quadTail3, 0.6373942428283291F, 0.0F, -0.6108652381980153F);
        this.breast = new ModelRenderer(this, 0, 54);
        this.breast.setRotationPoint(0.0F, 3.0F, -3.5F);
        this.breast.addBox(-3.5F, 0.0F, 0.0F, 7, 3, 3, scaling);
        this.setRotateAngle(breast, 0.8726646259971648F, 0.0F, 0.0F);

        this.head.addChild(this.quadTail2);
        this.hatBase.addChild(this.shape30);
        this.hatBase.addChild(this.shape32);
        this.head.addChild(this.quadTail1);
        this.hatBase.addChild(this.shape29);
        this.head.addChild(this.quadTail4);
        this.head.addChild(this.quadTail3);
        this.body.addChild(this.breast);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
//        if( !this.isArmor ) {
            this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
//        }

        if( !this.isArmor ) {
            GL11.glPushMatrix();
            GL11.glTranslatef(this.head.offsetX, this.head.offsetY, this.head.offsetZ);
            GL11.glTranslatef(this.head.rotationPointX * f5, this.head.rotationPointY * f5, this.head.rotationPointZ * f5);
            GL11.glScaled(1.1D, 1.1D, 1.1D);
            GL11.glTranslatef(-this.head.offsetX, -this.head.offsetY, -this.head.offsetZ);
            GL11.glTranslatef(-this.head.rotationPointX * f5, -this.head.rotationPointY * f5, -this.head.rotationPointZ * f5);
            this.head.render(f5);
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glTranslatef(this.hair.offsetX, this.hair.offsetY, this.hair.offsetZ);
            GL11.glTranslatef(this.hair.rotationPointX * f5, this.hair.rotationPointY * f5, this.hair.rotationPointZ * f5);
            GL11.glScaled(1.15D, 1.15D, 1.15D);
            GL11.glTranslatef(-this.hair.offsetX, -this.hair.offsetY, -this.hair.offsetZ);
            GL11.glTranslatef(-this.hair.rotationPointX * f5, -this.hair.rotationPointY * f5, -this.hair.rotationPointZ * f5);
            this.hair.render(f5);
            GL11.glPopMatrix();
        } else {
            this.hatBase.render(f5);
        }

        this.legLeft.render(f5);
        this.legRight.render(f5);
        this.body.render(f5);

        GL11.glPushMatrix();
        GL11.glTranslatef(this.armLeft2.offsetX, this.armLeft2.offsetY, this.armLeft2.offsetZ);
        GL11.glTranslatef(this.armLeft2.rotationPointX * f5 + 0.025F, this.armLeft2.rotationPointY * f5, this.armLeft2.rotationPointZ * f5);
        GL11.glScaled(1.05D, 1.05D, 1.05D);
        GL11.glTranslatef(-this.armLeft2.offsetX, -this.armLeft2.offsetY, -this.armLeft2.offsetZ);
        GL11.glTranslatef(-this.armLeft2.rotationPointX * f5 - 0.025F, -this.armLeft2.rotationPointY * f5, -this.armLeft2.rotationPointZ * f5);
        this.armLeft2.render(f5);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslatef(this.armRight2.offsetX, this.armRight2.offsetY, this.armRight2.offsetZ);
        GL11.glTranslatef(this.armRight2.rotationPointX * f5 - 0.025F, this.armRight2.rotationPointY * f5, this.armRight2.rotationPointZ * f5);
        GL11.glScaled(1.05D, 1.05D, 1.05D);
        GL11.glTranslatef(-this.armRight2.offsetX, -this.armRight2.offsetY, -this.armRight2.offsetZ);
        GL11.glTranslatef(-this.armRight2.rotationPointX * f5 + 0.025F, -this.armRight2.rotationPointY * f5, -this.armRight2.rotationPointZ * f5);
        this.armRight2.render(f5);
        GL11.glPopMatrix();
        this.armLeft.render(f5);
        this.armRight.render(f5);
        this.skirt1.render(f5);
        this.skirt2.render(f5);
    }

    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_)
    {
        super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, p_78087_7_);

        this.setRotateAngle(this.head, this.bipedHead.rotateAngleX, this.bipedHead.rotateAngleY, this.bipedHead.rotateAngleZ);
        this.setRotateAngle(this.hatBase, this.bipedHead.rotateAngleX, this.bipedHead.rotateAngleY, this.bipedHead.rotateAngleZ);
        this.setRotateAngle(this.body, this.bipedBody.rotateAngleX, this.bipedBody.rotateAngleY, this.bipedBody.rotateAngleZ);
        this.setRotateAngle(this.skirt1, this.bipedBody.rotateAngleX, this.bipedBody.rotateAngleY, this.bipedBody.rotateAngleZ);
        this.setRotateAngle(this.skirt2, this.bipedBody.rotateAngleX, this.bipedBody.rotateAngleY, this.bipedBody.rotateAngleZ);
//        this.setRotateAngle(this.body, this.bipedBody.rotateAngleX * 0.5F, this.bipedBody.rotateAngleY, this.bipedBody.rotateAngleZ);
//        this.setRotateAngle(this.skirt1, this.bipedBody.rotateAngleX * 0.5F, this.bipedBody.rotateAngleY, this.bipedBody.rotateAngleZ);
//        this.setRotateAngle(this.skirt2, this.bipedBody.rotateAngleX * 0.5F, this.bipedBody.rotateAngleY, this.bipedBody.rotateAngleZ);
        this.setRotateAngle(this.hair, this.bipedHead.rotateAngleX, this.bipedHead.rotateAngleY, this.bipedHead.rotateAngleZ);
//        if( this.isRiding ) {
            this.setRotateAngle(this.legLeft, this.bipedLeftLeg.rotateAngleX, this.bipedLeftLeg.rotateAngleY, this.bipedLeftLeg.rotateAngleZ);
            this.setRotateAngle(this.legRight, this.bipedRightLeg.rotateAngleX, this.bipedRightLeg.rotateAngleY, this.bipedRightLeg.rotateAngleZ);
//        } else {
//            this.setRotateAngle(this.legLeft, this.bipedLeftLeg.rotateAngleX * 0.5F, this.bipedLeftLeg.rotateAngleY, this.bipedLeftLeg.rotateAngleZ);
//            this.setRotateAngle(this.legRight, this.bipedRightLeg.rotateAngleX * 0.5F, this.bipedRightLeg.rotateAngleY, this.bipedRightLeg.rotateAngleZ);
//        }
        this.setRotateAngle(this.armLeft, this.bipedLeftArm.rotateAngleX, this.bipedLeftArm.rotateAngleY, this.bipedLeftArm.rotateAngleZ);
        this.setRotateAngle(this.armLeft2, this.bipedLeftArm.rotateAngleX, this.bipedLeftArm.rotateAngleY, this.bipedLeftArm.rotateAngleZ);
        this.setRotateAngle(this.armRight, this.bipedRightArm.rotateAngleX, this.bipedRightArm.rotateAngleY, this.bipedRightArm.rotateAngleZ);
        this.setRotateAngle(this.armRight2, this.bipedRightArm.rotateAngleX, this.bipedRightArm.rotateAngleY, this.bipedRightArm.rotateAngleZ);

//        if( this.isSneak ) {
//            if( this.isArmor ) {
//                this.legLeft.rotationPointZ = 0F;
//                this.legRight.rotationPointZ = 0F;
//                this.legLeft.rotateAngleX -= 0.3F;
//                this.legRight.rotateAngleX -= 0.3F;
//                this.armLeft.rotateAngleX += 0.4F;
//                this.armLeft2.rotateAngleX += 0.4F;
//                this.armRight.rotateAngleX += 0.4F;
//                this.armRight2.rotateAngleX += 0.4F;
//            } else {
//                this.legLeft.rotationPointZ = 3F;
//                this.legRight.rotationPointZ = 3F;
//                this.legLeft.rotateAngleX -= 0.15F;
//                this.legRight.rotateAngleX -= 0.15F;
//                this.armLeft.rotateAngleX += 0.2F;
//                this.armLeft2.rotateAngleX += 0.2F;
//                this.armRight.rotateAngleX += 0.2F;
//                this.armRight2.rotateAngleX += 0.2F;
//            }
//        } else {
//            this.legLeft.rotationPointZ = 0F;
//            this.legRight.rotationPointZ = 0F;
//        }
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
