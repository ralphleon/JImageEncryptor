import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.File;

public class ImageRead extends JPanel{

	private BufferedImage image;

	JMenuBar menuBar;

	public ImageRead()
	{
		this.image = null;
	
		setFocusable(true);

    	setLayout(null);
		setOpaque(true);

	}

	/** Sets the main Image **/
	public void setImage(BufferedImage image){
	
		this.image = image;
		repaint();
	}
	
	/** returns the image 
	 * @return The image shown by this panel
	 */
	public BufferedImage getImage(){
		return image;
	}

	/** Overwritten paint event for drawing **/
	public void paintComponent(Graphics g) {
		g.setColor(new Color(50,50,50));
		g.fillRect(0,0,getSize().width,getSize().height);	
			
		if(image != null){

			int center_x = getSize().width/2 - image.getWidth() /2;
			int center_y = getSize().height/2 - image.getHeight() /2;

			if(center_x < 10){ center_x = 10;}
			if(center_y < 10){ center_y = 10;}
		
			g.drawImage(image,center_x,center_y,null);
		}
	}

}
