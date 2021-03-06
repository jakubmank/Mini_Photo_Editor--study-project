import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
 /**
  * The class for the obligatory function 2 ( rotating an image). 
  * @author Aleksander Lipka
  *
  */
 
public class RotateFunction{

        HashMap<JButton,File> map;
        Image spiral;
        static List<BufferedImage> list_with_images = new ArrayList<BufferedImage>();
        

       /**
        * Main method of RotateFunction
        * Takes images from global HashMap, rotate them
        * and put them into the list.
        * @return list
        */
         public List<BufferedImage> main(){
        	 list_with_images.clear();
        	 map = AddDirectory.all_chosen;
                int counter = 0;
               
                if (spiral == null){
                        for(Map.Entry<JButton, File> entry: map.entrySet())   //////Moving through hashMap
                        {
                    try {
                        spiral = getImage(entry.getValue().getAbsolutePath());
                        counter++;
                        System.out.println("Path of image " + counter + " : " +entry.getValue().getAbsolutePath());  
                        if (PhotoEdit.check == true){
                        	rotateImage(PhotoEdit.deg, null);
                        	System.out.println("Rotating image "+counter+" by "+PhotoEdit.deg+" degrees (edge cut)");
                        }
                        else{
                        	rotateImage1(PhotoEdit.deg, null);
                        	System.out.println("Rotating image "+counter+" by "+PhotoEdit.deg+" degrees (bigger frame)"); 	
                        }
                        
                       BufferedImage tmp = toBufferedImage(spiral);
                       list_with_images.add(tmp);
                    }
                    catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                    }
                   
                        } 
                        PhotoEdit.all_chosen_images.clear();
                        System.out.println("Images have been successfully rotated");
                }
                return list_with_images;
        }

         /**
          * Getting an image from given path
          * @param path
          * @return
          */
        public Image getImage(String path){
                Image tempImage = null;
                try
                {
                        tempImage = Toolkit.getDefaultToolkit().getImage(path);
                }
                catch (Exception e)
                {
                        System.out.println("An error occured - " + e.getMessage());
                }
                return tempImage;
        }
       
        ////////////////////////////////////////////////////////////////////
       
        ///////////////////IMAGE ROTATION /////////////////////////////////
       /**
        * Rotating given image cutting the edges
        * @param degrees
        * @param o
        */
        public void rotateImage(double degrees, ImageObserver o){
                ImageIcon icon = new ImageIcon(this.spiral);
                BufferedImage blankCanvas = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = (Graphics2D)blankCanvas.getGraphics();
                if(PhotoEdit.black)
                	g2.setColor(Color.BLACK);
                else
                	g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, icon.getIconWidth(), icon.getIconHeight());
                g2.rotate(Math.toRadians(degrees), icon.getIconWidth()/2, icon.getIconHeight()/2);
                g2.drawImage(this.spiral, 0, 0, o);
                this.spiral = blankCanvas;
        }
        /**
         * Rotating given image in bigger frame - without cutting edges
         * @param degrees
         * @param o
         */
        public void rotateImage1(double degrees, ImageObserver o){
        	
        	double sin = Math.abs(Math.sin(Math.toRadians(degrees)));
        	double cos = Math.abs(Math.cos(Math.toRadians(degrees)));
            ImageIcon icon = new ImageIcon(this.spiral);
            int w = icon.getIconWidth();
        	int h = icon.getIconHeight();
            int neww = (int)Math.floor(w*cos+h*sin);
            int newh = (int)Math.floor(h*cos+w*sin);
            BufferedImage blankCanvas = new BufferedImage(neww, newh, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D)blankCanvas.getGraphics();
            if(PhotoEdit.black)
            	g2.setColor(Color.BLACK);
            else
            	g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, neww, newh);
            g2.translate((neww-w)/2, (newh-h)/2);
            g2.rotate(Math.toRadians(degrees), icon.getIconWidth()/2, icon.getIconHeight()/2);
           
            g2.drawImage(this.spiral, 0, 0, o);
            this.spiral = blankCanvas;
    }
    //////////////////////////////////////////////////////////////////////////
        
        /**
         * Converting Image to BufferedImage
         * @param img
         * @return
         */
        public static BufferedImage toBufferedImage(Image img)
        {
            if (img instanceof BufferedImage)
            {
                return (BufferedImage) img;
            }

            // Create a buffered image with transparency
            BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

            // Draw the image on to the buffered image
            Graphics2D bGr = bimage.createGraphics();
            bGr.drawImage(img, 0, 0, null);
            bGr.dispose();

            // Return the buffered image
            return bimage;
        }
 
}
