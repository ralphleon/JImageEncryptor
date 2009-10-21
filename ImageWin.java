import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.File;


class ImageWin extends JFrame implements ActionListener{

	/** Image Display panel */
	private ImageRead panel;

	/** Image encrypter class */
	private ImageEncrypt encrypter;
	
	/** Internal filename */
	private File fileName;

	/** Constructor */
	public ImageWin(){
		
		panel = new ImageRead();
		getContentPane().add(panel);
		pack();
		
		setSize(300,300);

		//Construct the menu
		setJMenuBar(constructMenu());

		encrypter = new ImageEncrypt();
		encrypter.test();

	}

	/** Extention of the constructor for menu creation */
	private JMenuBar constructMenu(){
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		
		fileMenu.add(new JMenuItem("New Blank Image ..."));
		
		menuBar.add(fileMenu);
	
		fileMenu.add(new JMenuItem("Open ..."));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem("Save"));
		fileMenu.add(new JMenuItem("Save As..."));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem("Quit"));
		
		// Add the listeners
		for(int i=0;i<fileMenu.getItemCount();i++){
			JMenuItem me = fileMenu.getItem(i);
			if(me != null) me.addActionListener(this);
		}

		JMenu editMenu = new JMenu("Cipher");
		menuBar.add(editMenu);

		editMenu.add(new JMenuItem("Set Key"));
		editMenu.addSeparator();
		editMenu.add(new JMenuItem("Encrypt"));
		editMenu.add(new JMenuItem("Decrypt"));
		editMenu.addSeparator();
		editMenu.add(new JMenuItem("Encrypt w/ Block Trick"));
		editMenu.add(new JMenuItem("Decrypt w/ Block Trick"));
		editMenu.addSeparator();
		editMenu.add(new JMenuItem("Two Image Attack..."));

		// Add the listeners
		for(int i=0;i<editMenu.getItemCount();i++){
			JMenuItem me = editMenu.getItem(i);
			if(me != null) me.addActionListener(this);
		}

		return menuBar;
	}


	/** Sets the image file being used and changes the frame title to match
	 * @param file the file for the imag
	 */
	private void setFile(File file){
		fileName = file;
		setTitle("JImageCipher ~ " + file.getName());
	}


	/** Action listener **/
	public void actionPerformed(ActionEvent e) {

		String text = e.getActionCommand();
	
		try{
			// File actions
			if(text =="New Blank Image ..."){ actionNewBlank(); }
			else if(text == "Open ..."){ actionLoadImage(null); }
			else if(text == "Save"){ actionSaveImage(fileName); }
			else if(text == "Save As..."){ actionSaveImage(null);}
			else if(text == "Quit"){ System.exit(0);}
		
			// Edit actions
			else if(text == "Set Key"){ 
				actionKeyDialog();
			}else if(text == "Encrypt"){ 
				panel.setImage(encrypter.map(panel.getImage(),true,false));
			}else if(text == "Decrypt"){
				panel.setImage(encrypter.map(panel.getImage(),false,false));
			}else if(text == "Encrypt w/ Block Trick"){ 
				panel.setImage(encrypter.map(panel.getImage(),true,true));
			}else if(text == "Decrypt w/ Block Trick"){
				panel.setImage(encrypter.map(panel.getImage(),false,true));
			}else if(text == "Two Image Attack..."){
				actionAttack();
			}

		}catch(Exception err){ System.out.println("ERROR:" + err);}
	}

	/** Action call-back */
	public void actionAttack() throws Exception{    	
			
		JFileChooser fc = new JFileChooser(fileName);
    	fc.showOpenDialog(this);
  	  	File imageFile1 = fc.getSelectedFile();
	
		if(imageFile1 == null){return;}

    	fc.showOpenDialog(this);
  	  	File imageFile2 = fc.getSelectedFile();
		
		if(imageFile2 == null){return;}

		BufferedImage img1 = imageFromFile(imageFile1);
		BufferedImage img2 = imageFromFile(imageFile2);

		BufferedImage attack = encrypter.attack(img1,img2);	
		panel.setImage(attack);
		setFile(new File("attack.png"));
	}

	/** Set the key **/
	public void actionKeyDialog(){
		String key = new String(encrypter.getKey());

		key = (String)JOptionPane.showInputDialog(this,
						"Enter a 16 byte key (current key= " + 
						key.getBytes().length + " bytes)",key);

		while(key != null && key.getBytes().length != 16){
			
			key = (String)JOptionPane.showInputDialog(this,
						"Enter a 16 byte key (current key= " + 
						key.getBytes().length + " bytes)",key);
		}

		if(key != null) encrypter.setKey(key.getBytes());
	}

	/** Loads a new blank Image */
	public void actionNewBlank(){

		Color newColor = JColorChooser.showDialog(
                     this,
                     "Choose Fill Color",
                     Color.white);
	
		if(newColor != null){
			panel.setImage(encrypter.blankImage(newColor,
					panel.getImage().getWidth(),
					panel.getImage().getHeight()));
			setFile( new File("blank.png"));
		}			
		
	}

	/** Load an image from a file 
	 * @param file the name of the file to load, use "null" to access a dialog
	 */
	public void actionLoadImage(File imageFile){

		if(imageFile == null){
    		JFileChooser fc = new JFileChooser(fileName);
    		fc.showOpenDialog(this);
  	  		imageFile = fc.getSelectedFile();
		}

		if(imageFile != null){

			panel.setImage(imageFromFile(imageFile));
			setFile(imageFile);	
		}
	} 

	/** Load an image from a file 
	 * @param file the filename
	 * @return the buffered image, "null" if file did not exist
	 */
	private BufferedImage imageFromFile(File file){
		
		System.out.println("Loading File ... " + file.getName());

		BufferedImage img = null;
		try{
			img = ImageIO.read(file);
		}catch(Exception e){
			System.out.println("Error:" + e);
		}
		
		return img;
	}
			

	/** Save an image from a file 
	 * @param file the name of the file to save, use "null" to access a dialog
	 */
	public void actionSaveImage(File imageFile){
	
		if(imageFile == null){
			JFileChooser fc = new JFileChooser(fileName);
    		fc.showSaveDialog(this);
  			imageFile = fc.getSelectedFile();
		}

		if(imageFile != null){
		
			System.out.println("Saving File ... " + imageFile.getName());
	
			try{	
				ImageIO.write(panel.getImage(), "png", imageFile);
			}catch(Exception e){
				System.out.println("Error:" + e);
			}
			setFile(imageFile);
		}
	}


	/** Main function **/
	public static void main(String args[]) 
	{		
		ImageWin win = new ImageWin();
		win.setVisible(true);
		
		if(args.length > 0){
			win.actionLoadImage(new File(args[0]));
	
		}
	}
}

