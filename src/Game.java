import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class Game {

    //maximum grid size (for panning)
    //this is number of hexagons!
    public static int maxGridX = 20;
    public static int maxGridY = 20;
    
    public Game() {
	//the class that controls the actual gameplay (not graphics stuff)
	
	JOptionPane.showMessageDialog(null, "Boostrap \n"
		+ "by Alex Johnson");
	
	SpinnerNumberModel sModel = new SpinnerNumberModel(20, 0, 100, 1);
	JSpinner spinner = new JSpinner(sModel);
	JOptionPane.showMessageDialog(null, spinner, "Enter Grid Size X", JOptionPane.OK_CANCEL_OPTION);
	maxGridX = (int) spinner.getValue();

	JOptionPane.showMessageDialog(null, spinner, "Enter Grid Size Y", JOptionPane.OK_CANCEL_OPTION);
	maxGridY = (int) spinner.getValue();
	
    }
}
