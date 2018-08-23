import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class PictoMinpulation {
	public static void main(String[] args) {
		try {
			PictoManipulation.i = 5;
			
		//BufferedImage image = ImageIO.read(new File("C:\\Users\\matthewdennerlein\\Downloads\\testPic.png"));
			BufferedImage image = ImageIO.read(new File("C:\\Users\\Matthew Dennerlein\\Desktop\\pngTest.png"));
			File outputFile = new File("C:\\Users\\Matthew Dennerlein\\Desktop\\pngTestEncrpto.png");
			//if pixel blue doesn't equal 1 or 0 then make it even or odd to determine binary (0 is even 1 is odd)
			
			int width = image.getWidth();
			int height = image.getHeight();
			int[][] result = new int[height][width];


			for (int row = 0; row < height; row++) {
	    	  for (int col = 0; col < width; col++) {
	            //result[row][col] = image.getRGB(col, row);
	    		//Color c = new Color(255, 255, 255);
	            //image.setRGB(col, row, c.getRGB());
	            //Color c = new Color(image.getRGB(col, row); 
	            
	            //System.out.printf("Red: %s Green: %s Blue: %s\n", c.getRed(), c.getGreen(), c.getBlue());
	            //Thread.sleep(2);
//	            System.out.println(image.getRGB(col, row));
	    	  }
		    }
			System.out.println("Done");
			ImageIO.write(image, "png", outputFile);
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
