import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JButton;


public class MergeImageAND {
	
	/**
	 * The array storing all images on which there will be performed merge operation.
	 */
	static BufferedImage[] input;
	/**
	 * Images  as files which are values for JButtons. 
	 * 
	 */
	static HashMap<JButton,File> list_of_images;
	/**
	 * The variable storing the option for situation when images are different sizes.
	 *  2 - Enlarge smaller one
	 *  3 - Shrink bigger one
	 *  4 - cut the bigger to the smaller one. 
	 */
	static int different_sizes= 0;
	
	/**
	 * constructor 
	 * @param images All selected images from given directories.
	 * @param mode  Which option should be performed when images are of different sizes.
	 */
	MergeImageAND(HashMap<JButton,File> images,int mode)
	{
		list_of_images=images;
		System.out.println(list_of_images.values());
		input = new BufferedImage[list_of_images.size()]; 
		this.different_sizes = mode;
		
		
	}
	/**
	 * Transferring selected images from hashmap to the ArrayList
	 */
	private static void getImagesFromHashmapIntoArray()
	{

        // Load each input image.  
       int counter = 0;
        for ( File i : list_of_images.values() ) {  
            try {  
                File f = i;   
                input[counter] = ImageIO.read( f );  
                counter++;
            }  
            catch ( IOException x ) {  
                // Complain if there is any problem loading   
                // an input image.  
                x.printStackTrace();  
            }  
        }  
	}
	/**
	 * Method which merge two given images. 
	 * 
	 * @param img1 First image from the ArrayList of chosen images
	 * @param img2 Second image from the ArrayList of chosen images
	 * @return Merged image from the given two. 
	 */
	private static BufferedImage andImages(BufferedImage img1,BufferedImage img2)
	{
		BufferedImage img_merged = new BufferedImage(img1.getWidth(), img1.getHeight(), img1.getType());
	
		for(int x = 0; x < img1.getWidth(); x++)	
	        for(int y = 0; y < img1.getHeight(); y++) 
	        {
	        	int argb0 = img1.getRGB(x, y);
	        	int argb1 = img2.getRGB(x, y);
	
	        	// Here the 'b' stands for 'blue' as well
	        	// as for 'brightness' :-)
	        	int b0 = argb0 & 0xFF;
	        	int b1 = argb1 & 0xFF;
	        	int bDiff = 0;
	        	if(b1>b0 || b1==b0)
	        	{
	        		bDiff = b0;
	        	}
	        	else if(b1<b0)
	        	{
	        		bDiff = b1;
	        	}
	        	
	
	        	int diff = 
	            	    (255 << 24) | (bDiff << 16) | (bDiff << 8) | bDiff;
	        	
	            img_merged.setRGB(x, y, diff);
	        }
		
		return img_merged;
		      
	}
	/**
	 * Method responsible for changing a resolution of the given image.
	 * @param originalImage 
	 * @param type
	 * @param destination_Widht
	 * @param destination_Height
	 * @return
	 */
	
	  private static BufferedImage resizeImage(BufferedImage originalImage, int type,int destination_Widht,int destination_Height)
	  {
			BufferedImage resizedImage = new BufferedImage(destination_Widht, destination_Height, type);
			Graphics2D g = resizedImage.createGraphics();
			g.drawImage(originalImage, 0, 0, destination_Widht, destination_Height, null);
			g.dispose();
		 
			return resizedImage;
	  }
	  /**
		 * Method which will merge all images from the ArrayList with the selected directories.
		 * @return The final merged image. 
		 */
	  private static BufferedImage mergeAll()
		{
			BufferedImage image = null;
			// check for the first two images if they are same resolution
			
			
			if(different_sizes ==2) // ENLARGE
			{
				if(input[0].getType() != input[1].getType())
				{
					input[1]=resizeImage(input[1],input[0].getType(),input[1].getWidth(),input[1].getHeight());
				}
				if(input[0].getHeight() != input[1].getHeight() || input[0].getWidth() != input[1].getWidth())
				{	
					if(input[0].getHeight()  > input[1].getHeight())	
						input[1]=resizeImage(input[1],input[1].getType(),input[1].getWidth(),input[0].getHeight());
					if(input[0].getHeight() < input[1].getHeight())	
						input[0]=resizeImage(input[0],input[0].getType(),input[0].getWidth(),input[1].getHeight());
					if(input[0].getWidth() > input[1].getWidth())
						input[1]=resizeImage(input[1], input[1].getType(), input[0].getWidth(),input[1].getHeight());
					if(input[0].getWidth() < input[1].getWidth())
						input[0]=resizeImage(input[0], input[0].getType(), input[1].getWidth(),input[0].getHeight());
				}
				//make merge for first two images
				
				image = andImages(input[0],input[1]);
			
				for(int i = 2; i<input.length;i++ ) 
				{	
					if(image.getType() != input[i].getType())
					{
						input[i]=resizeImage(input[i],image.getType(),input[i].getWidth(),input[i].getHeight());
					}
					if(image.getHeight()  != input[i].getHeight() || image.getWidth() != input[i].getHeight())	// check if images have same resolution
					{	
						if(image.getHeight() > input[i].getHeight()) // check if the first image is bigger than input[1]
							input[i]=resizeImage(input[i],input[i].getType(),input[i].getWidth(),image.getHeight()); // enlarge smaller image to resolution of second image
						if(image.getHeight() < input[i].getHeight())
							image=resizeImage(image,image.getType(),image.getWidth(),input[i].getHeight());
						if(image.getWidth() > input[i].getWidth())
							input[i]=resizeImage(input[i], input[i].getType(), image.getWidth(),input[i].getHeight());
						if(image.getWidth() < input[i].getWidth())
							image=resizeImage(image, image.getType(), input[i].getWidth(),image.getHeight());
					}
					image = andImages(image,input[i]);
				}
			}
			
			else if(different_sizes == 3 ) // shrink
			{
				if(input[0].getType() != input[1].getType())
				{
					input[1]=resizeImage(input[1],input[0].getType(),input[1].getWidth(),input[1].getHeight());
				}

				if(input[0].getHeight() != input[1].getHeight() ||input[0].getWidth() != input[1].getWidth())
				{
					if(input[0].getHeight()  > input[1].getHeight())    
						input[0]=resizeImage(input[0],input[0].getType(),input[0].getWidth(),input[1].getHeight());
					if(input[0].getHeight() < input[1].getHeight())    
						input[1]=resizeImage(input[1],input[0].getType(),input[1].getWidth(),input[0].getHeight());
					if(input[0].getWidth() > input[1].getWidth())
						input[0]=resizeImage(input[0], input[0].getType(), input[1].getWidth(),input[0].getHeight());
					if(input[0].getWidth() < input[1].getWidth())
						input[1]=resizeImage(input[1], input[0].getType(), input[0].getWidth(),input[1].getHeight());
				}
				image = andImages(input[0],input[1]);
				for(int i = 2; i<input.length;i++ ) 
				{
					if(image.getType() != input[i].getType())
					{
						input[i]=resizeImage(input[i],image.getType(),input[i].getWidth(),input[i].getHeight());
					}

					if(image.getHeight() != input[i].getHeight() ||image.getWidth() != input[i].getWidth())
					{
						if(image.getHeight()  > input[i].getHeight())    
							image=resizeImage(image,image.getType(),image.getWidth(),input[i].getHeight());
						if(image.getHeight() < input[i].getHeight())    
							input[i]=resizeImage(input[i],image.getType(),input[i].getWidth(),image.getHeight());
						if(image.getWidth() > input[i].getWidth())
							image=resizeImage(image, image.getType(), input[i].getWidth(),image.getHeight());
						if(image.getWidth() < input[i].getWidth())
							input[i]=resizeImage(input[i], image.getType(), image.getWidth(),input[i].getHeight());
					}
					image = andImages(image,input[i]);
				}
				
			}
			else if(different_sizes == 4)
			{
				if(input[0].getType() != input[1].getType())
				{
					input[1]=resizeImage(input[1],input[0].getType(),input[1].getWidth(),input[1].getHeight());
				}
				if(input[0].getHeight() != input[1].getHeight() ||input[0].getWidth() != input[1].getWidth());
				{
					if(input[0].getHeight() * input[0].getWidth() > input[1].getHeight() * input[1].getWidth() )
						input[0]=input[0].getSubimage((input[0].getWidth()/2)-(input[1].getWidth()/2), (input[0].getHeight()/2)-(input[1].getHeight()/2), input[1].getWidth(), input[1].getHeight());
					else
						input[1]=input[1].getSubimage((input[1].getWidth()/2)-(input[0].getWidth()/2), (input[1].getHeight()/2)-(input[0].getHeight()/2), input[0].getWidth(), input[0].getHeight());
					
						
				
				
				}
				image = andImages (input[0],input[1]);
				
				for(int i = 2; i<input.length;i++ ) 
				{
					if(image.getType() != input[i].getType())
					{
						input[i]=resizeImage(input[i],image.getType(),input[i].getWidth(),input[i].getHeight());
					}
					if(image.getHeight() != input[i].getHeight() ||image.getWidth() != input[i].getWidth())
					{

						if(image.getHeight() != input[i].getHeight() ||image.getWidth() != input[i].getWidth())
						{
							if(image.getHeight() * image.getWidth() > input[i].getHeight() * input[i].getWidth() )
								image=image.getSubimage((image.getWidth()/2)-(input[i].getWidth()/2), (image.getHeight()/2)-(input[i].getHeight()/2), input[i].getWidth(), input[i].getHeight());
							else
								input[i]=input[i].getSubimage((input[i].getWidth()/2)-(image.getWidth()/2), (input[i].getHeight()/2)-(image.getHeight()/2), image.getWidth(), image.getHeight());
						}
						
					}
					image = andImages (image,input[i]);
				}
				
				
			}
			return image;
		}
		

	
	public static BufferedImage returnImage() // 
	{
		getImagesFromHashmapIntoArray();
		BufferedImage img = mergeAll();
		return img;
	}	
	
}
