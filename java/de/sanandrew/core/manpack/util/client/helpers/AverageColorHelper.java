/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP, SilverChiren and CliffracerX
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.util.client.helpers;

import de.sanandrew.core.manpack.util.helpers.SAPUtils.RGBAValues;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public final class AverageColorHelper
{
    public static RGBAValues getAverageColor(ResourceLocation res) {
        try {
            // read texture as BufferedImage from ResourceLocation
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(res).getInputStream();
            BufferedImage bi = ImageIO.read(is);

            // holds the added RGB values of the whole texture and pixel counter
            double red = 0.0D;
            double green = 0.0D;
            double blue = 0.0D;
            double count = 0;
            for( int x = 0; x < bi.getWidth(); x++ ) {          // loop through the pixels
                for( int y = 0; y < bi.getHeight(); y++ ) {
                    int color = bi.getRGB(x, y);
                    if( ((color >> 24) & 0xFF) == 0x00 ) {      // check if it isn't fully transparent, if it is, then ignore this color, since it will darken it,
                        continue;                               // because those pixels are usually black and you don't see them anyway
                    }

                    red += ((color >> 16) & 0xFF);              // add RGB from the pixel to the RGB storage variables, increase pixel counter
                    green += ((color >> 8) & 0xFF);
                    blue += (color & 0xFF);
                    count += 1D;
                }
            }

            int avgRed = (int) (red / count);       // calculating the average of each channel
            int avgGreen = (int) (green / count);
            int avgBlue = (int) (blue / count);

            return new RGBAValues(avgRed, avgGreen, avgBlue, 255); // return combined RGB channels, format 0xRRGGBB
        } catch( IOException e ) {
            e.printStackTrace();        // something went wrong with reading the texture
        }

        return new RGBAValues(0, 0, 0, 0);
    }
}
