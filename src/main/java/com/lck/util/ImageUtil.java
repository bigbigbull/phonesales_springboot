package com.lck.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

/**
 * describe:
 *
 * @author lichangkai
 * @date 2018/11/29
 */
public class ImageUtil {
    /**
     * 确保图片的二进制格式是jpg，
     *仅通过ImageIO.write(img, "jpg", file);
     *不足以保证转换出来的jpg文件显示正常
     */
    public static BufferedImage change2jpg(File file){
        try {
            Image i = Toolkit.getDefaultToolkit().createImage(file.getAbsolutePath());
            PixelGrabber pg = new PixelGrabber(i, 0, 0, -1, -1, true);
            pg.grabPixels();
            int width = pg.getWidth(), height = pg.getHeight();
            final int[] rgbMasks = { 0xFF0000, 0xFF00, 0xFF };
            final ColorModel rgbOpaque = new DirectColorModel(32, rgbMasks[0], rgbMasks[1], rgbMasks[2]);
            DataBuffer buffer = new DataBufferInt((int[]) pg.getPixels(), pg.getWidth() * pg.getHeight());
            WritableRaster raster = Raster.createPackedRaster(buffer, width, height, width, rgbMasks, null);
            BufferedImage img = new BufferedImage(rgbOpaque, raster, false, null);
            return img;
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 用于改变图片大小
     * */
    public static void resizeImage(File srcFile,int width,int height,File destFile){
        try {
            if(!destFile.getParentFile().exists()){
                destFile.getParentFile().mkdirs();
            }
            Image i = ImageIO.read(srcFile);
            i = resizeImage(i,width,height);
            ImageIO.write((RenderedImage) i,"jpg",destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用于改变图片大小
     * */
    public static Image resizeImage(Image srcImage,int width,int height){
        try{
            BufferedImage bufferedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
            bufferedImage.getGraphics().drawImage(srcImage.getScaledInstance(width,height,Image.SCALE_SMOOTH),0,0,null);
            return bufferedImage;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
