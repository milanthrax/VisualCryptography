package visualkernel;

import java.awt.image.BufferedImage;

public class TwoOfTwo extends VisualScheme{

	boolean blocks[][][];
	boolean blockscomp[][][];
		
	public TwoOfTwo(){
		super();
		//Efficient two of two blocks as in Naor & Shamir and their compliments
		blocks=new boolean[6][][];
		blockscomp=new boolean[6][][];
		
		blocks[0]=new boolean[][]{{false,false},{true,true}};
		blockscomp[0]=new boolean[][]{{true,true},{false,false}};
		
		blocks[1]=new boolean[][]{{true,true},{false,false}};
		blockscomp[1]=new boolean[][]{{false,false},{true,true}};
		
		blocks[2]=new boolean[][]{{false,true},{false,true}};
		blockscomp[2]=new boolean[][]{{true,false},{true,false}};
		
		blocks[3]=new boolean[][]{{true,false},{true,false}};
		blockscomp[3]=new boolean[][]{{false,true},{false,true}};
		
		blocks[4]=new boolean[][]{{false,true},{true,false}};
		blockscomp[4]=new boolean[][]{{true,false},{false,true}};
		
		blocks[5]=new boolean[][]{{true,false},{false,true}};
		blockscomp[5]=new boolean[][]{{false,true},{true,false}};
	}
	
	/**
	 * 
	 * @param image1: First image
	 * @param image2: Second image
	 * @param x: x coordinate of the pixel
	 * @param y: y coordinate of the pixel
	 * @param dark: Is this pixel meant to be colored dark or light
	 */
	private void paintPixels(BufferedImage image1, BufferedImage image2, int x, int y, boolean dark){
		int index=rand.nextInt(2)+4;
		if(dark){
			image1.setRGB(x, y, (blocks[index][0][0]?DARK:LIGHT).getRGB());
			image2.setRGB(x, y, (blockscomp[index][0][0]?DARK:LIGHT).getRGB());
			image1.setRGB(x+1, y, (blocks[index][0][1]?DARK:LIGHT).getRGB());
			image2.setRGB(x+1, y, (blockscomp[index][0][1]?DARK:LIGHT).getRGB());
			image1.setRGB(x, y+1, (blocks[index][1][0]?DARK:LIGHT).getRGB());
			image2.setRGB(x, y+1, (blockscomp[index][1][0]?DARK:LIGHT).getRGB());
			image1.setRGB(x+1, y+1, (blocks[index][1][1]?DARK:LIGHT).getRGB());
			image2.setRGB(x+1, y+1, (blockscomp[index][1][1]?DARK:LIGHT).getRGB());
		}
		else{
			image1.setRGB(x, y, (blocks[index][0][0]?DARK:LIGHT).getRGB());
			image2.setRGB(x, y, (blocks[index][0][0]?DARK:LIGHT).getRGB());
			image1.setRGB(x+1, y, (blocks[index][0][1]?DARK:LIGHT).getRGB());
			image2.setRGB(x+1, y, (blocks[index][0][1]?DARK:LIGHT).getRGB());
			image1.setRGB(x, y+1, (blocks[index][1][0]?DARK:LIGHT).getRGB());
			image2.setRGB(x, y+1, (blocks[index][1][0]?DARK:LIGHT).getRGB());
			image1.setRGB(x+1, y+1, (blocks[index][1][1]?DARK:LIGHT).getRGB());
			image2.setRGB(x+1, y+1, (blocks[index][1][1]?DARK:LIGHT).getRGB());
		}
	}
	
	@Override
	public BufferedImage[] createShares(BufferedImage iOriginal) {
		int x,y;
		BufferedImage biImage[]= new BufferedImage[2];
		biImage[0]=new BufferedImage(iOriginal.getWidth()*2, iOriginal.getHeight()*2,BufferedImage.TYPE_INT_RGB);
		biImage[1]=new BufferedImage(iOriginal.getWidth()*2, iOriginal.getHeight()*2,BufferedImage.TYPE_INT_RGB);
		for(x=0;x<iOriginal.getWidth(null);x++)
			for(y=0;y<iOriginal.getHeight(null);y++)
				//paintPixels(biImage[0], biImage[1], x*2, y*2, iOriginal.getRGB(x, y)==DARK.getRGB());
				paintPixels(biImage[0], biImage[1], x*2, y*2, iOriginal.getRGB(x, y)!=LIGHT.getRGB());
		setGuides(biImage);
		return biImage;
	}

}
