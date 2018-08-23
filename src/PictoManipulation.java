import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;



import javax.imageio.ImageIO;


public class PictoManipulation {
	
	static final String FROM_FILE = "C:\\Users\\Matthew Dennerlein\\Desktop\\minecraftIm.png";
	static final String TO_FILE = "C:\\Users\\Matthew Dennerlein\\Desktop\\minecraftImOfEncroto.png";
	protected static int i = 4;
	static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) throws Exception {
		startPrompt();
	}
	
	private static void startPrompt() throws Exception{
		System.out.println("To Decrypt Image Type 1, To Encrypt 2: ");
		String input = sc.nextLine();
		
		switch(input){
			case "1":
				decode(TO_FILE);
				break;
			case "2":
				System.out.println("Enter a message");
				encrypt(sc.nextLine(), FROM_FILE, TO_FILE);
				break;
		}
	}
	
	private static String toBinary(String s){
		byte[] hiddenBytes = s.getBytes();
		
		StringBuilder textToBinary = new StringBuilder();
		for(byte b : hiddenBytes){
			int validation = b;
			
			for(int i = 0; i<8; i++){
				textToBinary.append((validation & 128) == 0 ? 0 : 1);
				validation <<= 1;
			}
		}
		
		return textToBinary.toString();
	}
	
	private static void encrypt(String input, String fileLoc, String outputFileLoc) throws Exception{
		BufferedImage image = ImageIO.read(new File(fileLoc));
		File outputFile = new File(outputFileLoc);
		
		// gets the width and height of the image for later use
		int width = image.getWidth();
		int height = image.getHeight();


		// takes the user's input and converts it into binary
		String binaryString = toBinary(input);
		System.out.printf("To Binary: %s\n", binaryString);
		
		// sets the total length of the binary string and how many are left to encode
		final int totalLegnth = binaryString.length();
		int lengthLeft = totalLegnth;
		char[] binaryBytes = binaryString.toCharArray();
		
		// an array to see if pixels were changed
		boolean[] pixelChanged = {false, false, false};
		
		// sets the done boolean to false. (the loop breaks when this is true)
		boolean done = false;
		
		// will cycle though every pixel in the image
		for (int row = 0; row < height; row++) {
			if(done) break;
			for (int col = 0; col < width; col++) {
				
				// gets the pixel's color to gets each of the pixel's RGB values
				Color pixelColor = new Color(image.getRGB(col, row), true);
				short[] pixelColors = {(short) pixelColor.getRed(), (short) pixelColor.getGreen(), (short) pixelColor.getBlue()};
				short alpha = (short) pixelColor.getAlpha();


				// makes sure that each pixel color value is what it should and changes the ones that are not
				for(int i = 0; i<3; i++){
					if(lengthLeft > 0){
						if(binaryBytes[totalLegnth-lengthLeft] == '0') pixelColors[i] = changeParity(pixelColors[i], true);
						else pixelColors[i] = changeParity(pixelColors[i], false);
						
						pixelChanged[i] = true;
					}
					lengthLeft--;
				}
				
				// this gets entered when all of the user's binary is done
				if(lengthLeft <= 0){
					// this sets all of the bits to 0
					for(int k = 0; k<3; k++)
						if((pixelColors[k] & 1) != 0 && !pixelChanged[k]) pixelColors[k]--;
					
					if(lengthLeft < -24){
						done = true;
						break;
					}
				}
				
				for(int j = 0; j<3; j++) pixelChanged[j] = false;
				
				image.setRGB(col, row, new Color(pixelColors[0], pixelColors[1], pixelColors[2], alpha).getRGB());
			}
		}
		System.out.println("Done");
		ImageIO.write(image, "png", outputFile);
	}
	
	private static short changeParity(short byteToCheck, boolean toEven){
		short newByte = byteToCheck;
		
		if(newByte > 0){ // if the short is bigger than zero
			if(toEven){ // if it is supposed to be even
				if( (newByte & 1) != 0) // if not even then make even by subtracting one
					newByte--;
			}else{ // if it is supposed to be odd
				if((newByte & 1) == 0) // if not odd then make odd by adding one
					newByte++;
			}
		}else if(!toEven){ // if the bit is zero and we want it to be odd add one (0 is even)
			newByte++;
		}
		
		return newByte;
	}
	
	private static String binaryToText(String binary){
		String text = "";
		String convertedBinary = binary;
		
		for(int i = 0; i < binary.length()/8; i++){
			int charCode = Integer.parseInt(convertedBinary.substring(0, 8), 2);
			convertedBinary = convertedBinary.substring(8, convertedBinary.length());
			
			text += new Character((char)charCode).toString();
		}
		
		return text;
	}
	
	private static void decode(String fileLoc){
		try{
			BufferedImage image = ImageIO.read(new File(fileLoc));
			
			String binary = "";
			int width = image.getWidth();
			int height = image.getHeight();
			
			boolean done = false;
			
			String pattern = "[0]+";
			
			for (int row = 0; row < height; row++) {
				if(done) break;
				for (int col = 0; col < width; col++) {
					Color pixelColor = new Color(image.getRGB(col, row));
					
					short red = (short) pixelColor.getRed();
					short green = (short) pixelColor.getGreen();
					short blue = (short) pixelColor.getBlue();
					
					binary += (red & 1) == 0 ? "0" : "1";
					binary += (green & 1) == 0 ? "0" : "1";
					binary += (blue & 1) == 0 ? "0" : "1";
					
					if(binary.length() > 16 && binary.substring(binary.length()-16, binary.length()).matches(pattern)){
						while(binary.length()%8 != 0) binary = binary.substring(0, binary.length() - 1);
						done = true;
						break;
					}
				}
			}
			
			while(binary.substring(binary.length()-8).matches(pattern)) binary = binary.substring(0, binary.length()-8);
			
			System.out.println(binaryToText(binary));
			System.exit(0);
		}catch(Exception e) {e.printStackTrace();}
	}
}
