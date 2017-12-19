import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class Game {

    //maximum grid size (for panning)
    //this is number of hexagons!
    public static int maxGridX = 20;
    public static int maxGridY = 20;
    
    public static ArrayList<Cell> cells = new ArrayList<Cell>();
    
    //selected cell
    private static Cell selCell = null;
    private static boolean selected = false; //something is selected
    
    
    public Game() {
	//the class that controls the actual gameplay (not graphics stuff)
	
	JOptionPane.showMessageDialog(null, "Boostrap \n"
		+ "by Alex Johnson");
	//creates test grid (dev purposes)
	testGrid();
    }
    
    public void testGrid() {
	SpinnerNumberModel sModel = new SpinnerNumberModel(10, 0, 100, 1);
	JSpinner spinner = new JSpinner(sModel);
	JOptionPane.showMessageDialog(null,
		spinner, "Enter Grid Size X",
		JOptionPane.INFORMATION_MESSAGE, null);
	maxGridX = (int) spinner.getValue();

	JOptionPane.showMessageDialog(null,
		spinner, "Enter Grid Size Y",
		JOptionPane.INFORMATION_MESSAGE, null);
	maxGridY = (int) spinner.getValue();
	
	//sets initial zoom and maximum zoom
	Renderer.unitsWide = Math.max(2*maxGridX, 2*maxGridY);
	Renderer.maxUnitsWide = Renderer.unitsWide*2;
	
	
	
	//creates test grid
	for(int i = 0; i < maxGridX; i++) {
	    for(int j = 0; j < maxGridY; j++) {
		Cell c = new Cell();
		c.row = i-(maxGridX/2);
		c.col = j-(maxGridY/2);
		//christmas colors lol
		if(j%2 == 0) {
		    c.setRBG(1, 0, 0);
		}else {
		    c.setRBG(0, 0, 1);
		}
		cells.add(c);
	    }
	}
    }
    
    public static boolean hasCell(int row, int col)
    {
	for(Cell c: cells) {
	    if(c.row == row & c.col == col) {
		return true;
	    }
	}
	return false;
    }
    
    public static Cell getCell(int row, int col)
    {
	//assumes only one cell of a given row and column
	Cell r = null;
	for(Cell c: cells) {
	    if(c.row == row & c.col == col) {
		r = c;
		break;
	    }
	}
	return r;
    }
    
    public static void selCell(int row, int col) {
	//tries to "select" this cell by turning it white
	//deselects if it's already selected!
	if(hasCell(row,col)) {
	    Cell c = getCell(row, col);
	    //sets new cell if old one is not yet selected
	    if(c.equals(selCell)) {
		//clicked same cell twice, deselects
		deselect();
	    }
	    else {
		//deselects, then sets new
		deselect();
		c.selected = true;
		selected = true;
		selCell = c;
	    }
	}
    }
    
    public static void deselect() {
	//deselects selected cell
	if(selected && selCell != null) {
	    selCell.selected = false;
	    selCell = null;
	    selected = false;
	}
    }
    
}
