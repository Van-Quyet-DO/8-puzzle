import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

// https://cis.gvsu.edu/~dulimarh/CS367/Notes/Qt-vs-Swing.html
// https://www.geeksforgeeks.org/jlabel-java-swing/
// https://www.youtube.com/watch?v=p9Y-NBg8eto

public class MainWindow{
	public static int[] resizeDialog() {
		// Input dialog
		JTextField field1 = new JTextField("3");
		JTextField field2 = new JTextField("3");
		Object[] message = {
		    "Number of rows (at least 3):", field1,
		    "Number of columns (at least 3):", field2,
		};
		
		int option = JOptionPane.showConfirmDialog(null, message, "Enter all your values", JOptionPane.OK_CANCEL_OPTION);
		int numRow = 3, numCol = 3; 
		if (option == JOptionPane.OK_OPTION) {
		    numRow = Integer.parseInt(field1.getText());
		    numCol = Integer.parseInt(field2.getText());
		    return new int[] {numRow,numCol};
		}
		else
			return new int[] {0,0};
		
	}

	public static void main(String[] args) throws IOException, 
		ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		// System Look and Feel
		UIManager.setLookAndFeel(
	            UIManager.getSystemLookAndFeelClassName());
		/* try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }
        */
		
		// Main frame, panel, timer
		JFrame frame = new JFrame("8 Puzzle");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		TimeCounter timer = new TimeCounter();
		Interface gui = new Interface(new int[] {3,3}, timer);
		
		// Pencil Marking toggled button
        JToggleButton pencilMarking = new JToggleButton("Pencil Marking"); 
        ItemListener pmListener = new ItemListener() { 
            public void itemStateChanged(ItemEvent itemEvent) { 
                gui.setPencilMarking(itemEvent.getStateChange() == ItemEvent.SELECTED);
                System.out.println("Something is pressed or clicked!");
            } 
        };
        // Attach Listeners 
        pencilMarking.addItemListener(pmListener);
        pencilMarking.setSize(100, 100);
        pencilMarking.setFocusable(false);
        
        // Choose Image clicked button
        JButton chooseImage = new JButton("New"); 
        ActionListener ciListener = new ActionListener(){  
        	public void actionPerformed(ActionEvent e){  
                try {
					if(gui.chooseImage()) {
						timer.resetTimer();
						frame.setSize(gui.width()+20, gui.height()+70);
					}
				} catch (IOException evt) {
					// TODO Auto-generated catch block
					evt.printStackTrace();
				}
            }  
        };
        // Attach Listeners 
        chooseImage.addActionListener(ciListener);
        chooseImage.setFocusable(false);
        
        // Choose Image clicked button
        JButton resize = new JButton("Resize"); 
        ActionListener rsListener = new ActionListener(){  
        	public void actionPerformed(ActionEvent e){  
                try {
					if(gui.initialize(resizeDialog())) {
						timer.resetTimer();
					}
				} catch (IOException evt) {
					// TODO Auto-generated catch block
					evt.printStackTrace();
				}
            }  
        };
        // Attach Listeners 
        resize.addActionListener(rsListener);
        resize.setFocusable(false);
  
        // Swing components and containers
        // http://javadevwannabe.blogspot.com/2012/02/swing-components-and-containers.html
        JPanel buttonPanel = new JPanel(new GridLayout(1,3));
        buttonPanel.add(chooseImage);
        buttonPanel.add(resize);
        buttonPanel.add(pencilMarking);
        buttonPanel.add(timer);
        buttonPanel.setFocusable(false);
        
        frame.setLayout(new BorderLayout());
        frame.add(buttonPanel,BorderLayout.NORTH);
        frame.add(gui,BorderLayout.CENTER);
        frame.setBounds(100,100,gui.width()+20, gui.height()+70);
		frame.setVisible(true);
		
		// add timer to count how long players need to win the game
		// add dialog to notify the completion
		
	}
}

/* Tasks
- Upgrade the core to play in customized size (DONE)
- fix size of each rectangular piece (SQ -> X, Y) (DONE)
- compress the original images if necessary
- Choose image + Interface (may use toolbar, container) (DONE) Fix a little bit (See PA1 of Java course)
- Focus to panel, Focus Priority https://docs.oracle.com/javase/tutorial/uiswing/misc/focus.html (DONE)
- Pencil marking in each pieces (DONE)
- add clock to count time
- add hint + AI, save tool, record -> JMenuBar. Should not have pause, because it is unfair
- window look and feel (DONE) https://docs.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
- Convert to Android

*/