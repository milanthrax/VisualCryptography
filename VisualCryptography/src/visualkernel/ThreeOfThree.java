package visualkernel;

import java.awt.image.BufferedImage;

public class ThreeOfThree extends VisualScheme {
	private int c0[][],c1[][],n;
	
	public ThreeOfThree(){
		c0=new int[][]{{0,0,1,1},{0,1,0,1},{0,1,1,0}};
		c1=new int[][]{{1,1,0,0},{1,0,1,0},{1,0,0,1}};
		n=3;
	}
	
	private void paintPixels(BufferedImage im[], int x, int y, int m[][]){
		permuteColumns(m,4);
		for(int i=0;i<im.length;i++){
			for(int j=0;j<n;j++)
				im[i].setRGB(x*n+j, y, LIGHT.getRGB());
			//set the black pixel in the corresponging column
			for(int j=0;j<n;j++)
				if(m[i][j]==1)
					im[i].setRGB(x*n+j, y, DARK.getRGB());
		}
	}
	
	@Override
	public BufferedImage[] createShares(BufferedImage iOriginal) {
		int x,y;
		BufferedImage biImage[]= new BufferedImage[n];
		for(int i=0;i<biImage.length;i++)
			biImage[i]=new BufferedImage(iOriginal.getWidth()*n, iOriginal.getHeight(),BufferedImage.TYPE_INT_RGB);
		for(x=0;x<iOriginal.getWidth(null);x++){
			for(y=0;y<iOriginal.getHeight(null);y++){
				if(iOriginal.getRGB(x, y)==LIGHT.getRGB())
					paintPixels(biImage, x, y, c0);
				else
					paintPixels(biImage, x, y, c1);					
			}
		}
		setGuides(biImage);
		return biImage;
	}
}
