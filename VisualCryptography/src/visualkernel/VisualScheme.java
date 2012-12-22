package visualkernel;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;

import javax.imageio.ImageIO;

/**
 * All visual schemes must be derived from this class.
 * @author juancamilocorena
 *
 */
public abstract class VisualScheme {
	/**
	 * Random generator.
	 */
	SecureRandom rand;
	
	/**
	 * Definition for the dark color
	 */
	protected static Color DARK=null;
	
	/**
	 * Definition for the light color
	 */
	protected static Color LIGHT=null;
	
	/**
	 * Image containing the containing the cross you see on images 
	 */
	private static BufferedImage CROSS;
	
	/**
	 * Image containing the circles you see on images
	 */
	private static BufferedImage CIRCLE;
	
	/**
	 * Some static initialization for the values we wanted
	 */
	static {
        DARK=Color.BLACK;
        LIGHT=new Color(255,255,255,255);
        try {
			CROSS = ImageIO.read(new File("images/cross.bmp"));
			CIRCLE = ImageIO.read(new File("images/circle.bmp"));
		}
        catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	/**
	 * Default constructor
	 */
	public VisualScheme(){
		rand=new SecureRandom();
	}
	
	/**
	 * This method is the core of each class; it must implement the given visual scheme.
	 * @param iOriginal: The original image to be divided in shares.
	 * @return An array containing the different shares.
	 */
	public abstract BufferedImage[] createShares(BufferedImage iOriginal);
	
	/**
	 * Method use to add the aligning images to the shares. This version uses a crosshair
	 * and a circle.
	 * @param im: Images of the different guides.
	 */
	protected void setGuides(BufferedImage im[]){
		BufferedImage guide;
		for(int i=0;i<im.length;i++){
			//Even and odd images have different guides
			guide=(i%2==0?CROSS:CIRCLE); 
			Graphics2D g2 = (Graphics2D)im[i].getGraphics();
			//Left up
			g2.drawImage(guide, 0, 0, guide.getWidth(), guide.getHeight(), 0, 0, guide.getWidth(), guide.getHeight(), Color.WHITE, null);
			//Left down
			g2.drawImage(guide, 0, im[i].getHeight()-guide.getHeight(), guide.getWidth(), im[i].getHeight(), 0, 0, guide.getWidth(), guide.getHeight(), Color.WHITE, null);
			//Right up
			g2.drawImage(guide, im[i].getWidth()-guide.getWidth(),0, im[i].getWidth(), guide.getHeight(), 0, 0, guide.getWidth(), guide.getHeight(), Color.WHITE, null);
		}
	}
	
	/**
	 * This method permutes two columns from a given matrix.
	 * @param m: matrix to be permuted.
	 * @param n: number of columns of the matrix.
	 */
	protected void permuteColumns(int m[][], int n){
		int perms=3,col1,col2,temp;
		for(int i=0;i<perms;i++){
			col1=rand.nextInt(n);
			do{
				col2=rand.nextInt(n);
			}while(col1==col2);
			for(int j=0;j<m.length;j++){
				temp=m[j][col1];
				m[j][col1]=m[j][col2];
				m[j][col2]=temp;
			}
		}
	}
	
	/**
	 * This method overlaps two images and stores the result in the first image of the array.
	 * @param share: images to overlap
	 * @param coord: will be used in future versions
	 * @return overlapped images at position 0.
	 */
	public BufferedImage overlapImage(BufferedImage share[], Point2D.Double coord[]){
		BufferedImage ret=new BufferedImage(share[0].getWidth(),share[0].getHeight(),BufferedImage.TYPE_INT_RGB);
		for(int x=0;x<ret.getWidth();x++){
			for(int y=0;y<ret.getHeight();y++){
				ret.setRGB(x, y, (share[0].getRGB(x, y)==DARK.getRGB() || share[1].getRGB(x, y)==DARK.getRGB())?DARK.getRGB():LIGHT.getRGB());
			}
		}
		return ret;
	}
}
