package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import visualkernel.KofK;
import visualkernel.ThreeOfThree;
import visualkernel.TwoOfN;
import visualkernel.TwoOfTwo;
import visualkernel.VisualScheme;

public class guiShowImages extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DrawingPanel centerPanel;
	private JPanel southPanel;
	
	
	
	JComboBox jccK;
	JComboBox jccN;
	JButton jbApply;
	JCheckBoxMenuItem easyAlignMenuItem;
	//
	private VisualScheme vs;
	private BufferedImage original;
		
	public guiShowImages() throws Exception{
		//Initialize variables containing information about the shares
		vs=null;
		original=null;
		//Initialize gui
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Visual Cryptography");
		setMinimumSize(new Dimension(640,480));
		setLayout(new BorderLayout());
		setJMenuBar(createMenu());
		//Center panel		
		centerPanel=new DrawingPanel(null,false);
		centerPanel.addMouseListener(centerPanel);
		centerPanel.addMouseMotionListener(centerPanel);
		centerPanel.setEasyAlignment(true);
		add(centerPanel,BorderLayout.CENTER);
		//South panel
		southPanel=new JPanel();
		southPanel.setBorder(BorderFactory.createTitledBorder("Visual cryptography parameters"));
		southPanel.setLayout(new GridLayout());
		jbApply=new JButton("Apply changes");
		jbApply.addActionListener(this);
		jbApply.setActionCommand("Apply changes");
		jccK=new JComboBox(new Integer[]{2,3,4,5,6});
		jccN=new JComboBox(new Integer[]{2,3,4,5,6});
		southPanel.add(jccK);
		southPanel.add(jccN);
		southPanel.add(jbApply);
		add(southPanel,BorderLayout.SOUTH);
		//
		pack();
		setVisible(true);
	}
	
	private JMenuBar createMenu(){
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu optionsMenu=new JMenu("Options");
		menuBar.add(fileMenu);
		menuBar.add(optionsMenu);
				
		JMenuItem openMenuItem = new JMenuItem("Open");
		openMenuItem.addActionListener(this);
		openMenuItem.setActionCommand("Open");
		fileMenu.add(openMenuItem);

		JMenuItem closeMenuItem = new JMenuItem("Close");
		closeMenuItem.addActionListener(this);
		closeMenuItem.setActionCommand("Close");
		fileMenu.add(closeMenuItem);
		fileMenu.addSeparator();

		JMenuItem saveMenuItem = new JMenuItem("Save images");
		saveMenuItem.addActionListener(this);
		saveMenuItem.setActionCommand("Save");
		fileMenu.add(saveMenuItem);
		fileMenu.addSeparator();
		
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(this);
		exitMenuItem.setActionCommand("Exit");
		fileMenu.add(exitMenuItem);
		
		easyAlignMenuItem=new JCheckBoxMenuItem("Easy alignment",true);
		easyAlignMenuItem.addActionListener(this);
		easyAlignMenuItem.setActionCommand("Easy alignment");
		optionsMenu.add(easyAlignMenuItem);
		
		return menuBar;
	}
	
	private void saveImageArray(BufferedImage im[], String path){
		try{
			for(int i=0;i<im.length;i++){
				File outputfile = new File(path+(i<10?"0":"")+i+".png");
				ImageIO.write(im[i], "png", outputfile);
			}
		}
		catch(IOException e){e.printStackTrace();}
	}
	
	public void open(boolean selectFile){
		try {
			if(selectFile){
				JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
				// Show open dialog; this method does not return until the dialog is closed
				fc.showOpenDialog(this);
				File file = fc.getSelectedFile();
				if(file==null)
					return;
				original = ImageIO.read(file);
			}
			if(jccK.getSelectedItem().equals(2) && jccN.getSelectedItem().equals(2))
				vs=new TwoOfTwo();
			else if(jccK.getSelectedItem().equals(2) && !jccN.getSelectedItem().equals(2)){
				vs=new TwoOfN((Integer)jccN.getSelectedItem());
			}
			else if(jccK.getSelectedItem().equals(3) && jccN.getSelectedItem().equals(3)){
				vs=new ThreeOfThree();
			}
			else if(jccK.getSelectedItem().equals(jccN.getSelectedItem())){
				vs=new KofK((Integer)jccN.getSelectedItem());
			}
			else{
				JOptionPane.showMessageDialog(this, "Not yet supported.");
			}
				
			if(original!=null){
				BufferedImage im[]=vs.createShares(original);
				centerPanel.setIm(im);
			}
		}
		catch (IOException e1) {
			JOptionPane.showMessageDialog(this, "Error opening file.");
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String command=e.getActionCommand();
		if(command.equals("Open")){
			open(true);
		}
		else if(command.equals("Close")){
			vs=null;
			original=null;
			centerPanel.setIm(null);
		}
		else if(command.equals("Save")){
			JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
			fc.showSaveDialog(this);
			if(fc.getSelectedFile()==null)
				return;
			String path=fc.getSelectedFile().getAbsolutePath();
			BufferedImage im[]=vs.createShares(original);
			saveImageArray(im,path);
		}
		else if(command.equals("Exit")){
			System.exit(0);
		}
		else if(command.equals("Apply changes")){
			open(false);
		}
		else if(command.equals("Easy alignment")){
			centerPanel.setEasyAlignment(easyAlignMenuItem.isSelected());
		}
	}
	
	public static void main(String[] args)throws Exception {
		new guiShowImages();
	}
}
