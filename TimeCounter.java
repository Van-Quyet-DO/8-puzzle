import java.awt.Graphics;
import java.text.SimpleDateFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class TimeCounter extends JPanel {
// https://stackoverflow.com/questions/28337718/java-swing-timer-countdown
        private long startTime = -1;
        private JLabel label;
        boolean stop = false;
        
        public TimeCounter() {
        	startTime = System.currentTimeMillis();
        	label = new JLabel();    
        	// add(label);  Can we add first then edit
		}
        
        @Override 
    	protected void paintComponent(Graphics g) {
    		super.paintComponent(g);
    		
    		SimpleDateFormat df = new SimpleDateFormat("mm:ss:SSS");
    		if(!stop) label.setText(df.format(System.currentTimeMillis() - startTime));
            add(label);  
            repaint();
        }
        
        public void resetTimer() {
        	startTime = System.currentTimeMillis();
        	stop = false;
        }
                
        public void offTimer() {
        	label.setText("Off");
        	add(label);  
        }
}