package de.sanandrew.core.manpack.mod.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

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
    public ModelRenderer skirt1;
    public ModelRenderer skirt2;
    public ModelRenderer breast;
    public ModelRenderer quadTail1;
    public ModelRenderer quadTail2;
    public ModelRenderer quadTail3;
    public ModelRenderer quadTail4;

    public ModelSanPlayer() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.armLeft = new ModelRenderer(this, 40, 16);
        this.armLeft.mirror = true;
        this.armLeft.setRotationPoint(5.0F, 2.0F, 0.5F);
        this.armLeft.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 3, 0.0F);
        this.setRotateAngle(armLeft, -0.0033161255787892262F, 0.0F, -0.09983283321407566F);
        this.armLeft2 = new ModelRenderer(this, 40, 32);
        this.armLeft2.mirror = true;
        this.armLeft2.setRotationPoint(5.0F, 2.0F, 0.5F);
        this.armLeft2.addBox(-1.0F, 5.0F, -2.0F, 3, 5, 3, 0.0F);
        this.setRotateAngle(armLeft2, -0.0033161255787892262F, 0.0F, -0.09983283321407566F);
        this.hair = new ModelRenderer(this, 0, 0);
        this.hair.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.hair.addBox(-3.5F, -7.0F, -3.5F, 7, 7, 7, 0.0F);
        this.quadTail4 = new ModelRenderer(this, 0, 32);
        this.quadTail4.setRotationPoint(0.0F, -3.0F, 0.0F);
        this.quadTail4.addBox(-1.5F, 3.0F, 0.9F, 2, 6, 2, 0.0F);
        this.setRotateAngle(quadTail4, 0.6373942428283291F, 0.0F, 0.6108652381980153F);
        this.head = new ModelRenderer(this, 28, 0);
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.head.addBox(-3.5F, -7.0F, -3.5F, 7, 7, 7, 0.0F);
        this.armRight = new ModelRenderer(this, 40, 16);
        this.armRight.setRotationPoint(-4.0F, 2.0F, 0.5F);
        this.armRight.addBox(-3.0F, -2.0F, -2.0F, 3, 12, 3, 0.0F);
        this.setRotateAngle(armRight, 0.0F, 0.0F, 0.09983283321407528F);
        this.skirt2 = new ModelRenderer(this, 16, 44);
        this.skirt2.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.skirt2.addBox(-4.5F, 3.0F, -3.0F, 9, 4, 6, 0.0F);
        this.armRight2 = new ModelRenderer(this, 40, 32);
        this.armRight2.setRotationPoint(-4.0F, 2.0F, 0.5F);
        this.armRight2.addBox(-3.0F, 5.0F, -2.0F, 3, 5, 3, 0.0F);
        this.setRotateAngle(armRight2, 0.0F, 0.0F, 0.09983283321407566F);
        this.skirt1 = new ModelRenderer(this, 16, 36);
        this.skirt1.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.skirt1.addBox(-4.5F, 0.0F, -2.5F, 9, 3, 5, 0.0F);
        this.legLeft = new ModelRenderer(this, 0, 16);
        this.legLeft.mirror = true;
        this.legLeft.setRotationPoint(2.5F, 12.0F, 0.0F);
        this.legLeft.addBox(-1.5F, 0.0F, -1.5F, 3, 12, 3, 0.0F);
        this.quadTail1 = new ModelRenderer(this, 8, 32);
        this.quadTail1.mirror = true;
        this.quadTail1.setRotationPoint(0.0F, -3.0F, 0.0F);
        this.quadTail1.addBox(-0.5F, 3.0F, 0.9F, 2, 9, 2, 0.0F);
        this.setRotateAngle(quadTail1, 0.4363323129985824F, 0.0F, -2.2689280275926285F);
        this.breast = new ModelRenderer(this, 18, 21);
        this.breast.setRotationPoint(0.0F, 3.0F, -3.5F);
        this.breast.addBox(-3.5F, 0.0F, 0.0F, 7, 3, 3, 0.0F);
        this.setRotateAngle(breast, 0.8726646259971648F, 0.0F, 0.0F);
        this.legRight = new ModelRenderer(this, 0, 16);
        this.legRight.setRotationPoint(-2.5F, 12.0F, 0.0F);
        this.legRight.addBox(-1.5F, 0.0F, -1.5F, 3, 12, 3, 0.0F);
        this.body = new ModelRenderer(this, 16, 16);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
        this.quadTail3 = new ModelRenderer(this, 0, 32);
        this.quadTail3.mirror = true;
        this.quadTail3.setRotationPoint(0.0F, -3.0F, 0.0F);
        this.quadTail3.addBox(-0.5F, 3.0F, 0.9F, 2, 6, 2, 0.0F);
        this.setRotateAngle(quadTail3, 0.6373942428283291F, 0.0F, -0.6108652381980153F);
        this.quadTail2 = new ModelRenderer(this, 8, 32);
        this.quadTail2.setRotationPoint(0.0F, -3.0F, 0.0F);
        this.quadTail2.addBox(-1.5F, 3.0F, 0.9F, 2, 9, 2, 0.0F);
        this.setRotateAngle(quadTail2, 0.4363323129985824F, 0.0F, 2.2689280275926285F);
        this.head.addChild(this.quadTail4);
        this.body.addChild(this.skirt2);
        this.body.addChild(this.skirt1);
        this.head.addChild(this.quadTail1);
        this.body.addChild(this.breast);
        this.head.addChild(this.quadTail3);
        this.head.addChild(this.quadTail2);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);

        this.armLeft.render(f5);
        GL11.glPushMatrix();
        GL11.glTranslatef(this.armLeft2.offsetX, this.armLeft2.offsetY, this.armLeft2.offsetZ);
        GL11.glTranslatef(this.armLeft2.rotationPointX * f5, this.armLeft2.rotationPointY * f5, this.armLeft2.rotationPointZ * f5);
        GL11.glScaled(1.05D, 1.05D, 1.05D);
        GL11.glTranslatef(-this.armLeft2.offsetX, -this.armLeft2.offsetY, -this.armLeft2.offsetZ);
        GL11.glTranslatef(-this.armLeft2.rotationPointX * f5, -this.armLeft2.rotationPointY * f5, -this.armLeft2.rotationPointZ * f5);
        this.armLeft2.render(f5);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glScalef(1.1F, 1.1F, 1.1F);
        GL11.glPushMatrix();
        GL11.glTranslatef(this.hair.offsetX, this.hair.offsetY, this.hair.offsetZ);
        GL11.glTranslatef(this.hair.rotationPointX * f5, this.hair.rotationPointY * f5, this.hair.rotationPointZ * f5);
        GL11.glScaled(1.05D, 1.05D, 1.05D);
        GL11.glTranslatef(-this.hair.offsetX, -this.hair.offsetY, -this.hair.offsetZ);
        GL11.glTranslatef(-this.hair.rotationPointX * f5, -this.hair.rotationPointY * f5, -this.hair.rotationPointZ * f5);
        this.hair.render(f5);
        GL11.glPopMatrix();
        this.head.render(f5);

        GL11.glPopMatrix();
        this.armRight.render(f5);
        GL11.glPushMatrix();
        GL11.glTranslatef(this.armRight2.offsetX, this.armRight2.offsetY, this.armRight2.offsetZ);
        GL11.glTranslatef(this.armRight2.rotationPointX * f5 - 0.025F, this.armRight2.rotationPointY * f5, this.armRight2.rotationPointZ * f5);
        GL11.glScaled(1.05D, 1.05D, 1.05D);
        GL11.glTranslatef(-this.armRight2.offsetX, -this.armRight2.offsetY, -this.armRight2.offsetZ);
        GL11.glTranslatef(-this.armRight2.rotationPointX * f5 + 0.025F, -this.armRight2.rotationPointY * f5, -this.armRight2.rotationPointZ * f5);
        this.armRight2.render(f5);
        GL11.glPopMatrix();
        this.legLeft.render(f5);
        this.legRight.render(f5);
        this.body.render(f5);
    }

    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_)
    {
        super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, p_78087_7_);

        this.setRotateAngle(this.head, this.bipedHead.rotateAngleX, this.bipedHead.rotateAngleY, this.bipedHead.rotateAngleZ);
        this.setRotateAngle(this.body, this.bipedBody.rotateAngleX * 0.5F, this.bipedBody.rotateAngleY, this.bipedBody.rotateAngleZ);
        this.setRotateAngle(this.hair, this.bipedHead.rotateAngleX, this.bipedHead.rotateAngleY, this.bipedHead.rotateAngleZ);
        this.setRotateAngle(this.legLeft, this.bipedLeftLeg.rotateAngleX * 0.5F, this.bipedLeftLeg.rotateAngleY, this.bipedLeftLeg.rotateAngleZ);
        this.setRotateAngle(this.legRight, this.bipedRightLeg.rotateAngleX * 0.5F, this.bipedRightLeg.rotateAngleY, this.bipedRightLeg.rotateAngleZ);
        this.setRotateAngle(this.armLeft, this.bipedLeftArm.rotateAngleX, this.bipedLeftArm.rotateAngleY, this.bipedLeftArm.rotateAngleZ);
        this.setRotateAngle(this.armLeft2, this.bipedLeftArm.rotateAngleX, this.bipedLeftArm.rotateAngleY, this.bipedLeftArm.rotateAngleZ);
        this.setRotateAngle(this.armRight, this.bipedRightArm.rotateAngleX, this.bipedRightArm.rotateAngleY, this.bipedRightArm.rotateAngleZ);
        this.setRotateAngle(this.armRight2, this.bipedRightArm.rotateAngleX, this.bipedRightArm.rotateAngleY, this.bipedRightArm.rotateAngleZ);

        if( this.isSneak ) {
            this.legLeft.rotationPointZ = 3F;
            this.legRight.rotationPointZ = 3F;
            this.legLeft.rotateAngleX -= 0.15F;
            this.legRight.rotateAngleX -= 0.15F;
        } else {
            this.legLeft.rotationPointZ = 0F;
            this.legRight.rotationPointZ = 0F;
        }
//        this.head.rotateAngleY = p_78087_4_ / (180F / (float)Math.PI);
//        this.head.rotateAngleX = p_78087_5_ / (180F / (float)Math.PI);
//        this.hair.rotateAngleY = this.head.rotateAngleY;
//        this.hair.rotateAngleX = this.head.rotateAngleX;
//        this.armRight.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F + (float) Math.PI) * 2.0F * p_78087_2_ * 0.5F;
//        this.armLeft.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F) * 2.0F * p_78087_2_ * 0.5F;
//        this.armRight.rotateAngleZ = 0.0F;
//        this.armLeft.rotateAngleZ = 0.0F;
//        this.legRight.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F) * 1.4F * p_78087_2_;
//        this.legLeft.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F + (float)Math.PI) * 1.4F * p_78087_2_;
//        this.legRight.rotateAngleY = 0.0F;
//        this.legLeft.rotateAngleY = 0.0F;
//
//        if (this.isRiding)
//        {
//            this.armRight.rotateAngleX += -((float)Math.PI / 5F);
//            this.armLeft.rotateAngleX += -((float)Math.PI / 5F);
//            this.legRight.rotateAngleX = -((float)Math.PI * 2F / 5F);
//            this.legLeft.rotateAngleX = -((float)Math.PI * 2F / 5F);
//            this.legRight.rotateAngleY = ((float)Math.PI / 10F);
//            this.legLeft.rotateAngleY = -((float)Math.PI / 10F);
//        }
//
//        if (this.heldItemLeft != 0)
//        {
//            this.armLeft.rotateAngleX = this.armLeft.rotateAngleX * 0.5F - ((float)Math.PI / 10F) * (float)this.heldItemLeft;
//        }
//
//        if (this.heldItemRight != 0)
//        {
//            this.armRight.rotateAngleX = this.armRight.rotateAngleX * 0.5F - ((float)Math.PI / 10F) * (float)this.heldItemRight;
//        }
//
//        this.armRight.rotateAngleY = 0.0F;
//        this.armLeft.rotateAngleY = 0.0F;
//        float f6;
//        float f7;
//
//        if (this.onGround > -9990.0F)
//        {
//            f6 = this.onGround;
//            this.body.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(f6) * (float)Math.PI * 2.0F) * 0.2F;
//            this.armRight.rotationPointZ = MathHelper.sin(this.body.rotateAngleY) * 5.0F;
//            this.armRight.rotationPointX = -MathHelper.cos(this.body.rotateAngleY) * 5.0F;
//            this.armLeft.rotationPointZ = -MathHelper.sin(this.body.rotateAngleY) * 5.0F;
//            this.armLeft.rotationPointX = MathHelper.cos(this.body.rotateAngleY) * 5.0F;
//            this.armRight.rotateAngleY += this.body.rotateAngleY;
//            this.armLeft.rotateAngleY += this.body.rotateAngleY;
//            this.armLeft.rotateAngleX += this.body.rotateAngleY;
//            f6 = 1.0F - this.onGround;
//            f6 *= f6;
//            f6 *= f6;
//            f6 = 1.0F - f6;
//            f7 = MathHelper.sin(f6 * (float)Math.PI);
//            float f8 = MathHelper.sin(this.onGround * (float)Math.PI) * -(this.head.rotateAngleX - 0.7F) * 0.75F;
//            this.armRight.rotateAngleX = (float)((double)this.armRight.rotateAngleX - ((double)f7 * 1.2D + (double)f8));
//            this.armRight.rotateAngleY += this.body.rotateAngleY * 2.0F;
//            this.armRight.rotateAngleZ = MathHelper.sin(this.onGround * (float)Math.PI) * -0.4F;
//        }
//
//        if (this.isSneak)
//        {
//            this.body.rotateAngleX = 0.5F;
//            this.armRight.rotateAngleX += 0.4F;
//            this.armLeft.rotateAngleX += 0.4F;
//            this.legRight.rotationPointZ = 4.0F;
//            this.legLeft.rotationPointZ = 4.0F;
//            this.legRight.rotationPointY = 9.0F;
//            this.legLeft.rotationPointY = 9.0F;
//            this.head.rotationPointY = 1.0F;
//            this.hair.rotationPointY = 1.0F;
//        }
//        else
//        {
//            this.body.rotateAngleX = 0.0F;
//            this.legRight.rotationPointZ = 0.1F;
//            this.legLeft.rotationPointZ = 0.1F;
//            this.legRight.rotationPointY = 12.0F;
//            this.legLeft.rotationPointY = 12.0F;
//            this.head.rotationPointY = 0.0F;
//            this.hair.rotationPointY = 0.0F;
//        }
//
//        this.armRight.rotateAngleZ += MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
//        this.armLeft.rotateAngleZ -= MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
//        this.armRight.rotateAngleX += MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
//        this.armLeft.rotateAngleX -= MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
//
//        if (this.aimedBow)
//        {
//            f6 = 0.0F;
//            f7 = 0.0F;
//            this.armRight.rotateAngleZ = 0.0F;
//            this.armLeft.rotateAngleZ = 0.0F;
//            this.armRight.rotateAngleY = -(0.1F - f6 * 0.6F) + this.head.rotateAngleY;
//            this.armLeft.rotateAngleY = 0.1F - f6 * 0.6F + this.head.rotateAngleY + 0.4F;
//            this.armRight.rotateAngleX = -((float)Math.PI / 2F) + this.head.rotateAngleX;
//            this.armLeft.rotateAngleX = -((float)Math.PI / 2F) + this.head.rotateAngleX;
//            this.armRight.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
//            this.armLeft.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
//            this.armRight.rotateAngleZ += MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
//            this.armLeft.rotateAngleZ -= MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
//            this.armRight.rotateAngleX += MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
//            this.armLeft.rotateAngleX -= MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
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
