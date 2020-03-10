import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

// https://cis.gvsu.edu/~dulimarh/CS367/Notes/Qt-vs-Swing.html
// https://www.geeksforgeeks.org/jlabel-java-swing/
// http://javadevwannabe.blogspot.com/2012/02/swing-components-and-containers.html

public class Interface extends JPanel implements ActionListener, KeyListener{
	private MyGame board;
	private TimeCounter timer;
	private int X_SIZE = 100;
	private int Y_SIZE = 100;
	private int FONT_SIZE = 40;
	private int numCol = 3;
	private int numRow = 3;
	private BufferedImage[] img;
	private boolean pencilMarking = false;
	private String source = "ngoc.png";
	
	public void setPencilMarking(boolean b) {
		pencilMarking = b;
		repaint();
	}
	
	public Interface(int[] size, TimeCounter t) throws IOException {
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = t;
		initialize(size);
	}
	
	public boolean initialize(int[] size) throws IOException {
		if(size[0] == 0 || size[1] == 0) return false;
		
		numCol = size[1]; numRow = size[0];
		board = new MyGame(numRow,numCol);
		// this.setLayout(new GridLayout(numRow, numCol, 0, 0));
		loadAndSplitImage();
		repaint();
		return true;
	}
	
	public int height() { return numRow*Y_SIZE;}
	public int width() { return numCol*X_SIZE;}
	
	public boolean chooseImage() throws IOException {
		// https://mkyong.com/swing/java-swing-jfilechooser-example/
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setDialogTitle("Select an image");
		jfc.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG and PNG Images", "png", "jpg");
		jfc.addChoosableFileFilter(filter);

		int returnValue = jfc.showOpenDialog(null);
		// int returnValue = jfc.showSaveDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = jfc.getSelectedFile();
			System.out.println(selectedFile.getAbsolutePath());
			source = selectedFile.getAbsolutePath();
			loadAndSplitImage();
			repaint();
			return true;
		}
		else return false;
	}
	// https://stackoverflow.com/questions/25193738/split-an-image-into-parts/25193878
	// https://gist.github.com/madan712/3672616
	public void loadAndSplitImage() throws IOException {
		final BufferedImage source = ImageIO.read(new File(this.source));
		X_SIZE = source.getWidth()/numCol;
		Y_SIZE = source.getHeight()/numRow;
		FONT_SIZE = Y_SIZE/3;
		int numPieces =numCol*numRow; 
		img = new BufferedImage[numPieces];
		for(int j = 0; j < numRow; ++j)
			for (int i = 0; i < numCol; ++i) {
			    img[(j*numCol +i+1)%numPieces] = source.getSubimage(i*X_SIZE, j*Y_SIZE, X_SIZE, Y_SIZE);
			    System.out.println("Extract the "+(j*numCol +i+1)+"-th part");
			}
	}
	
	// https://www.youtube.com/watch?v=p9Y-NBg8eto
	@Override
	public void actionPerformed(ActionEvent e) {// Useless!
		// repaint(); 
		if(board.win()) {
			System.out.println("You win!");
			// add dialog here
			JDialog d = new JDialog();
            JLabel l = new JLabel("You have completed!"); 
            d.add(l);
            d.setSize(100, 100); 
            d.setVisible(true); 
		}
	}
	@Override 
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// Drawing numbers and render images
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));
        int adjust = (X_SIZE - FONT_SIZE)/2;
        
        
        for (int i = 0; i < numCol; i += 1) // i = col
            for (int j = 0; j < numRow; j += 1) { // j = row
            	if(board.grid[j][i] == 0) {
            		g2.setColor(Color.LIGHT_GRAY);
            	    g2.fillRect(X_SIZE * i, Y_SIZE * j, X_SIZE, Y_SIZE);
            	}
            	else 
            		g.drawImage(img[board.grid[j][i]],X_SIZE * i, Y_SIZE * j,null);
                g2.setColor(Color.black);
        		g2.setFont(new Font("TimesRoman", Font.PLAIN, FONT_SIZE));
        		if(pencilMarking)
        			g2.drawString(board.grid[j][i] + "", i * X_SIZE + adjust, j * Y_SIZE + Y_SIZE/2);
                
            }
        
        // Drawing boundary
        for (int i = 0; i <= numRow; i += 1) {
            // horizontal lines
            g2.drawLine(0, i * Y_SIZE, numCol*X_SIZE, i * Y_SIZE);
        }
        for (int i = 0; i <= numCol; i += 1) {
            // vertical lines
            g2.drawLine(i * X_SIZE, 0, i * X_SIZE, numRow*Y_SIZE);
        }
	}
	
	public boolean win() {
		return board.win();
	}
	
	// KeyEvent Handler
	@Override
	public void keyPressed(KeyEvent e) {
		// System.out.println("You press a key");
		char row = board.empty_x;
		int col = board.empty_y;
	    int key = e.getKeyCode();
	    
	    switch(key) {
		    case KeyEvent.VK_LEFT:
		    	System.out.println("Press Left Key");
		    	board.operate(row, col+1);
		    	break;
		    case KeyEvent.VK_RIGHT:
		    	board.operate(row, col-1);
		    	System.out.println("Press Right Key");
		    	break;
		    case KeyEvent.VK_UP:
		    	board.operate((char)(row+1), col);
		    	System.out.println("Press Up Key");
		    	break;
		    case KeyEvent.VK_DOWN:
		    	board.operate((char)(row-1), col);
		    	System.out.println("Press Down Key");
		    	break;
		    default: 
		    	System.out.println("Press other key "+key);
	    }
	    repaint();
	    // board.display();
	    if(board.win()) {
			System.out.println("You win!");
			timer.stop = true;
			// add dialog here
			JOptionPane.showMessageDialog(null, "You win the game!");
			
		}
	}
	
	public void keyReleased(KeyEvent e) {}
		
	public void keyTyped(KeyEvent e) {}

}
