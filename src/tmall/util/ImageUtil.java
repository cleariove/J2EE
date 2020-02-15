package tmall.util;

import java.awt.*;
import java.awt.image.*;
import java.io.File;

public class ImageUtil
{
    public static BufferedImage change2jpg(File f)
    {
        try
        {
            Image image = Toolkit.getDefaultToolkit().createImage(f.getAbsolutePath());
            PixelGrabber pg = new PixelGrabber(image,-1,-1,0,0,true);
            pg.grabPixels();
            int width = pg.getWidth();
            int height = pg.getHeight();
            final int[] RGB_MASK = {0xFF0000,0xFF00,0xFF};
            final ColorModel RGB_OPAQUE = new DirectColorModel(32,RGB_MASK[0],RGB_MASK[1],RGB_MASK[2]);
            DataBuffer dataBuffer = new DataBufferInt((int[]) pg.getPixels(),pg.getHeight() * pg.getWidth());
            WritableRaster raster = Raster.createPackedRaster(dataBuffer,width,height,width,RGB_MASK,null);
            BufferedImage bufferedImage = new BufferedImage(RGB_OPAQUE,raster,false,null);
            return bufferedImage;
        } catch (InterruptedException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static void resizeImage(File srcFile,int width,int height,File destFile)
    {

    }

    public static Image resizeImage(Image srcImg,int width,int height)
    {
        return null;
    }
}
