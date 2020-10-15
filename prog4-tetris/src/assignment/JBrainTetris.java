package assignment;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class JBrainTetris extends JTetris {
	 public static void main(String[] args) {
	        createGUI(new JBrainTetris());
	 }
	 
	 protected static final int BRAIN_DELAY = 4;
	 protected javax.swing.Timer brainTimer;
	 
	 JBrainTetris() {
		 	Brain brain = new CheeseBrain();
	        // Use timer for brain inputs
	        brainTimer = new javax.swing.Timer(BRAIN_DELAY, new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                tick(brain.nextMove(board));
	            }
	        });
	    }
	 
	 public void startGame() {
		 super.startGame();
		 brainTimer.start();
	 }
	 
	 public void stopGame() {
		 super.stopGame();
		 brainTimer.stop();
	 }
}
