package sanandreasp.core.manpack.helpers.client;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Icon;

public class GuiItemTab extends GuiButton {
	
    protected static RenderItem itemRenderer = new RenderItem();
    
    protected static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    
    public ResourceLocation baseTexture = TextureMap.locationItemsTexture;
	
	protected Icon renderedIcon;
	
	protected ResourceLocation texture = null;
	
    protected boolean isRight;
    
    protected boolean hasEffect;
    
    public int textureBaseX = 0;
    public int textureBaseY = 0;

	public GuiItemTab(int id, int xPos, int yPos, String name, Icon icon, boolean right, boolean hasEff, ResourceLocation tabTexture)
	{
		super(id, xPos, yPos, name);
		this.width = 26;
		this.height = 26;
		this.renderedIcon = icon;
		this.isRight = right;
		this.hasEffect = hasEff;
		this.texture = tabTexture;
	}
	
	public void setIcon(Icon ico)
	{
		this.renderedIcon = ico;
	}
	
    public void drawButton(Minecraft par1Minecraft, int par2, int par3)
    {
        if( this.drawButton ) {
        	FontRenderer var4 = par1Minecraft.fontRenderer;
        	par1Minecraft.getTextureManager().bindTexture(this.texture);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.field_82253_i = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
            int var5 = this.getHoverState(this.field_82253_i);
            this.drawTexturedModalRect(this.xPosition, this.yPosition, this.textureBaseX+26*(isRight?0:1), this.textureBaseY+var5*26, this.width, this.height);
            this.mouseDragged(par1Minecraft, par2, par3);

            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            RenderHelper.enableGUIStandardItemLighting();
            GL11.glPushMatrix();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            
            
            itemRenderer.zLevel = 300.0F;
            this.drawIcon(this.renderedIcon, this.xPosition + 5, this.yPosition + 5, par1Minecraft, 0xFFFFFF);
            itemRenderer.zLevel = 0.0F;
            GL11.glPopMatrix();
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);

            if( this.field_82253_i ) {
            	this.drawTabHoveringText(this.displayString, this.xPosition - (this.isRight ? var4.getStringWidth(this.displayString) + 5 : - 5), this.yPosition + 21, var4);
            }
            
            RenderHelper.disableStandardItemLighting();
        }
    }
    
    protected void drawTabHoveringText(String par1Str, int par2, int par3, FontRenderer fontRenderer)
    {
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        int var4 = fontRenderer.getStringWidth(par1Str);
        int var5 = par2 + 12;
        int var6 = par3 - 12;
        byte var8 = 8;
        this.zLevel = 300.0F;
        int var9 = -267386864;
        this.drawGradientRect(var5 - 3, var6 - 4, var5 + var4 + 3, var6 - 3, var9, var9);
        this.drawGradientRect(var5 - 3, var6 + var8 + 3, var5 + var4 + 3, var6 + var8 + 4, var9, var9);
        this.drawGradientRect(var5 - 3, var6 - 3, var5 + var4 + 3, var6 + var8 + 3, var9, var9);
        this.drawGradientRect(var5 - 4, var6 - 3, var5 - 3, var6 + var8 + 3, var9, var9);
        this.drawGradientRect(var5 + var4 + 3, var6 - 3, var5 + var4 + 4, var6 + var8 + 3, var9, var9);
        int var10 = 1347420415;
        int var11 = (var10 & 16711422) >> 1 | var10 & -16777216;
        this.drawGradientRect(var5 - 3, var6 - 3 + 1, var5 - 3 + 1, var6 + var8 + 3 - 1, var10, var11);
        this.drawGradientRect(var5 + var4 + 2, var6 - 3 + 1, var5 + var4 + 3, var6 + var8 + 3 - 1, var10, var11);
        this.drawGradientRect(var5 - 3, var6 - 3, var5 + var4 + 3, var6 - 3 + 1, var10, var10);
        this.drawGradientRect(var5 - 3, var6 + var8 + 2, var5 + var4 + 3, var6 + var8 + 3, var11, var11);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        fontRenderer.drawStringWithShadow(par1Str, var5, var6, -1);
        this.zLevel = 0.0F;
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableStandardItemLighting();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
    }

	private void drawIcon(Icon par1Icon, int par2, int par3, Minecraft mc, int color)
	{
        float f;
        int i1;
        float f1;
        float f2;

        GL11.glDisable(GL11.GL_LIGHTING);
        ResourceLocation resourcelocation = this.baseTexture;
        mc.renderEngine.bindTexture(resourcelocation);

        if( this.renderedIcon == null ) {
        	this.renderedIcon = ((TextureMap)Minecraft.getMinecraft().getTextureManager().getTexture(resourcelocation)).getAtlasSprite("missingno");
        }

        i1 = color;
        f = (float)(i1 >> 16 & 255) / 255.0F;
        f1 = (float)(i1 >> 8 & 255) / 255.0F;
        f2 = (float)(i1 & 255) / 255.0F;
        
        GL11.glColor4f(f, f1, f2, 1.0F);

        GuiItemTab.itemRenderer.renderIcon(par2, par3, this.renderedIcon, 16, 16);
        GL11.glEnable(GL11.GL_LIGHTING);

        if( this.hasEffect ) {
        	this.renderEffect(mc.renderEngine, par2, par3);
        }
        GL11.glEnable(GL11.GL_CULL_FACE);

	}

    protected void renderEffect(TextureManager manager, int x, int y)
    {
        GL11.glDepthFunc(GL11.GL_GREATER);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDepthMask(false);
        manager.bindTexture(GuiItemTab.RES_ITEM_GLINT);
        GuiItemTab.itemRenderer.zLevel -= 50.0F;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_DST_COLOR);
        GL11.glColor4f(0.5F, 0.25F, 0.8F, 1.0F);
        this.renderGlint(x * 431278612 + y * 32178161, x - 2, y - 2, 20, 20);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
        GuiItemTab.itemRenderer.zLevel += 50.0F;
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
    }

    private void renderGlint(int par1, int par2, int par3, int par4, int par5)
    {
        for( int j1 = 0; j1 < 2; ++j1 ) {
            if( j1 == 0 ) {
                GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
            }

            if( j1 == 1 ) {
                GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
            }

            float f = 0.00390625F;
            float f1 = 0.00390625F;
            float f2 = (float)(Minecraft.getSystemTime() % (long)(3000 + j1 * 1873)) / (3000.0F + (float)(j1 * 1873)) * 256.0F;
            float f3 = 0.0F;
            Tessellator tessellator = Tessellator.instance;
            float f4 = 4.0F;

            if( j1 == 1 ) {
                f4 = -1.0F;
            }

            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV((double)(par2 + 0), (double)(par3 + par5), (double)this.zLevel, (double)((f2 + (float)par5 * f4) * f), (double)((f3 + (float)par5) * f1));
            tessellator.addVertexWithUV((double)(par2 + par4), (double)(par3 + par5), (double)this.zLevel, (double)((f2 + (float)par4 + (float)par5 * f4) * f), (double)((f3 + (float)par5) * f1));
            tessellator.addVertexWithUV((double)(par2 + par4), (double)(par3 + 0), (double)this.zLevel, (double)((f2 + (float)par4) * f), (double)((f3 + 0.0F) * f1));
            tessellator.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), (double)this.zLevel, (double)((f2 + 0.0F) * f), (double)((f3 + 0.0F) * f1));
            tessellator.draw();
        }
    }
}
