package tmall.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

public class ImageUtil
{
    public static BufferedImage change2jpg(File f)
    {
        try
        {
            Image image = Toolkit.getDefaultToolkit().createImage(f.getAbsolutePath());
            PixelGrabber pg = new PixelGrabber(image,0,0,-1,-1,true);
            pg.grabPixels();
            int width = pg.getWidth();
            int height = pg.getHeight();
            final int[] RGB_MASK = {0xFF0000,0xFF00,0xFF};
            final ColorModel RGB_OPAQUE = new DirectColorModel(32,RGB_MASK[0],RGB_MASK[1],RGB_MASK[2]);
            DataBuffer dataBuffer = new DataBufferInt((int[]) pg.getPixels(),pg.getHeight() * pg.getWidth());
            WritableRaster raster = Raster.createPackedRaster(dataBuffer,width,height,width,RGB_MASK,null);
            return new BufferedImage(RGB_OPAQUE,raster,false,null);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static void resizeImage(File srcFile,int width,int height,File destFile)
    {
        try {
            Image image = ImageIO.read(srcFile);
            image = resizeImage(image,width,height);
            ImageIO.write((RenderedImage) image,"jpg",destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Image resizeImage(Image srcImg,int width,int height)
    {
        BufferedImage bufferedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_BGR);
        bufferedImage.getGraphics().drawImage(srcImg.getScaledInstance(width,height,Image.SCALE_SMOOTH)
                                                ,0,0,null);
        return bufferedImage;
    }
}
