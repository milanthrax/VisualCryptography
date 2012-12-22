package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class DrawingPanel extends JPanel implements MouseListener,MouseMotionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int IMAGES_PER_ROW=1;
	public static final int HORIZONTAL_GAP=30;
	public static final int VERTICAL_GAP=20;
	//Coordinates of the uper left corner
	private Point2D.Double position[];
	private BufferedImage im[];
	private int mousex;
	private int mousey;
	private int dragindex;
	private boolean easyAlignment;
	
	public DrawingPanel(BufferedImage im[], boolean easyAlignment){
		this.mousex=-1;
		this.mousey=-1;
		this.dragindex=-1;
		this.im=im;
		this.easyAlignment=easyAlignment;
		if(im!=null)
			setOriginalPositions();
	}

	public void setOriginalPositions(){
		if(im!=null){
			int row=0,col=0;
			position=new Point2D.Double[im.length];
			for(int i=0;i<position.length;i++){
				position[i]=new Point2D.Double();
				position[i].x=((im[0].getWidth()+HORIZONTAL_GAP)*col)+HORIZONTAL_GAP;
				position[i].y=((im[0].getHeight()+VERTICAL_GAP)*row)+VERTICAL_GAP;
				col++;
				if(col==IMAGES_PER_ROW){
					row++;
					col=0;
				}
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		Color crtColor;
		int x,y,width,height;
		super.paint(g);
		BufferedImage background=new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_RGB);
		Graphics2D g2=(Graphics2D)background.getGraphics();
		Graphics2D thisg2=(Graphics2D)g;
		//Reset panel
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, background.getWidth(), background.getHeight());
		g2.setColor(Color.BLACK);
		if(im!=null){
			for(int i=0;i<im.length;i++){
				drawImage(g2,im[i],position[i]);
				//g2.drawImage(im[i], (int)position[i].x, (int)position[i].y, null, null);
				//g2.drawRect((int)position[i].x, (int)position[i].y, 0, 0);
				if(mousex!=-1 && mousey!=-1){
					if(i==dragindex){
						x=(int)position[i].x;
						y=(int)position[i].y;
						width=im[i].getWidth();
						height=im[i].getHeight();
						crtColor=g2.getColor();
						g2.setColor(Color.GREEN);
						g2.drawRect(x,y,width,height);
						g2.setColor(crtColor);
					}
				}
			}
		}
		g2.dispose();
		thisg2.drawImage(background,0,0,null,null);
	}

	public void drawImage(Graphics2D g2, BufferedImage im, Point2D.Double position){
		Color temp=g2.getColor();
		for(int x=0;x<im.getWidth();x++){
			for(int y=0;y<im.getHeight();y++){
				if(im.getRGB(x, y)!=Color.WHITE.getRGB()){
					g2.setColor(new Color(im.getRGB(x, y)));
					g2.drawRect((int)position.x+x, (int)position.y+y, 0, 0);
				}
			}
		}
		g2.setColor(temp);
	}

	private int pointInImage(boolean changedrag){
		int x,y,width,height;
		if(im==null)
			return -1;
		for(int i=0;i<im.length;i++){
			x=(int)position[i].x;
			y=(int)position[i].y;
			width=im[i].getWidth();
			height=im[i].getHeight();
			if(mousex>=x && mousex<=x+width && mousey>=y && mousey<=y+height){
				if(changedrag)
					dragindex=i;
				return i;
			}
		}
		if(changedrag)
			dragindex=-1;
		return -1;
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		mousex=e.getX();
		mousey=e.getY();
		if(pointInImage(true)!=-1)
			repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(easyAlignment && dragindex!=-1){
			//Make rectangles from information
			Rectangle2D.Double rect[]=new Rectangle2D.Double[im.length];
			for(int i=0;i<rect.length;i++)
				rect[i]=new Rectangle2D.Double(position[i].x, position[i].y, im[i].getWidth(), im[i].getHeight());
			//Check for an intersection between the dragged rectangle and another rectangle
			for(int i=0;i<im.length;i++){
				if(i!=dragindex){
					if(rect[i].intersects(rect[dragindex])){
						position[dragindex].x=position[i].x;
						position[dragindex].y=position[i].y;
						break;
					}
				}
			}
		}
		//Free data
		mousex=-1;
		mousey=-1;
		dragindex=-1;
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(dragindex!=-1){
			position[dragindex].x+=e.getX()-mousex;
			position[dragindex].y+=e.getY()-mousey;
			mousex=e.getX();
			mousey=e.getY();
			repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {}

	public void setIm(BufferedImage[] im) {
		this.im = im;
		setOriginalPositions();
		repaint();
	}

	public void setEasyAlignment(boolean easyAlignment) {
		this.easyAlignment = easyAlignment;
	}
}
