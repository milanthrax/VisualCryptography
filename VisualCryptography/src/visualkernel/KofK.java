package visualkernel;

import java.awt.image.BufferedImage;
import java.math.BigInteger;

/**
 * This is the K out of K scheme.
 * @author juancamilocorena
 *
 */
public class KofK extends VisualScheme{

	private int k,rows,cols;
	private boolean s0[][],s1[][],binRep[][];

	public KofK(int k){
		this.k=k;
		rows=k;
		cols=(int)Math.pow(2, k);
		s0=new boolean[rows][cols];
		s1=new boolean[rows][cols];
		binRep=new boolean[cols][k];
		//Compute binary representation for k bits
		for(int i=0;i<cols;i++){
			String temp=new BigInteger(i+"").toString(2);
			while(temp.length()<k) temp="0"+temp;
			for(int j=temp.length()-1;j>=0;j--){
				if(temp.charAt(j)=='1')
					binRep[i][j]=true; 
			}
		}
		//
		generateS0();
		generateS1();
	}

	private boolean dotProductGF2(boolean J[], boolean x[]){
		boolean res=false;
		for(int i=0;i<J.length;i++)
			res^=(J[i]&x[i]);
		return res;
	}
	
	private void generateS0(){
		//Generate J0
		boolean J0[][]=new boolean [k][k];
		for(int i=0;i<J0.length;i++)
			for(int j=0;j<J0[i].length;j++)
				J0[i][j]=(i==j?true:false);
		for(int j=0;j<J0[k-1].length-1;j++)
			J0[k-1][j]=true;
		J0[k-1][k-1]=false;
		//Generate S0
		for(int i=0;i<rows;i++)
			for(int j=0;j<cols;j++)
				s0[i][j]=dotProductGF2(J0[i],binRep[j]);
	}

	private void generateS1(){
		//Generate J0
		boolean J1[][]=new boolean [k][k];
		for(int i=0;i<k;i++)
			J1[i][i]=true;
		//Now compute s1
		for(int i=0;i<rows;i++)
			for(int j=0;j<cols;j++)
				s1[i][j]=dotProductGF2(J1[i],binRep[j]);
	}

	public void permuteColumns(boolean m[][]){
		int perms=1,col1,col2;
		boolean temp;
		for(int i=0;i<perms;i++){
			col1=rand.nextInt(cols);
			do{
				col2=rand.nextInt(cols);
			}while(col1==col2);
			for(int j=0;j<k;j++){
				temp=m[j][col1];
				m[j][col1]=m[j][col2];
				m[j][col2]=temp;
			}
		}
	}
	
	private void paintPixels(BufferedImage im[], int x, int y, boolean m[][]){
		permuteColumns(m);
		for(int i=0;i<im.length;i++){
			for(int j=0;j<cols;j++)
				im[i].setRGB(x*cols+j, y, LIGHT.getRGB());
			//set the black pixel in the corresponging column
			for(int j=0;j<cols;j++)
				if(m[i][j])
					im[i].setRGB(x*cols+j, y, DARK.getRGB());
		}
	}
	
	@Override
	public BufferedImage[] createShares(BufferedImage iOriginal) {
		int x,y;
		BufferedImage biImage[]= new BufferedImage[k];
		for(int i=0;i<biImage.length;i++)
			biImage[i]=new BufferedImage(iOriginal.getWidth()*cols, iOriginal.getHeight(),BufferedImage.TYPE_INT_RGB);
		for(x=0;x<iOriginal.getWidth(null);x++){
			for(y=0;y<iOriginal.getHeight(null);y++){
				if(iOriginal.getRGB(x, y)==LIGHT.getRGB())
					paintPixels(biImage, x, y, s0);
				else
					paintPixels(biImage, x, y, s1);					
			}
		}
		setGuides(biImage);
		return biImage;
	}

	public static void main(String[] args) {
		KofK c=new KofK(3);
		c.generateS0();
		c.generateS1();
		
	}
}
