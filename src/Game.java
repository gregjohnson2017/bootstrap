import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Game {

    // grid size (for panning)
    // this is number of hexagons!
    public static int gridX = 20;
    public static int gridY = 20;

    //maximum grid size (beyond 30 it gets laggy...)
    public static int maxGridX = 30;
    public static int maxGridY = 30;

    public static ArrayList<Cell> cells = new ArrayList<Cell>();

    // selected cell
    private static Cell selectedCell = null;
    private static boolean selected = false; // something is selected

    public static int gameMode = -1; // 0 = regular gameplay, 1 = editor, 2 = test grid (-1 = quit at title screen)

    public static String levelName = ""; // level name, either playing or editing (no .txt)

    public static int makeCell = 0; // integer to determine which cell to make

    /**
     * Runs game in usual gameplay.
     */
    public static void runGame() {
        String[] opts = {"Load Level",
                "Level Editor",
                "Holiday Test Grid"};
        gameMode = JOptionPane.showOptionDialog(null,
                "Bootstrap, a game by Alex Johnson. Please select:",
                "Bootstrap",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opts,
                opts[0]);

        switch (gameMode) {
            case 0:
                // level selection
                gameMode = 0;
                break;
            case 1:
                // level editor
                gameMode = 1;
                levelEditor();
                break;
            case 2:
                // test grid
                gameMode = 2;
                testGrid();
                break;
            default:
                System.exit(0);
                break;
        }
    }

    /**
     * Sets up level editor
     */
    public static void levelEditor() {
        askGridSize();
        levelName = askLevelName();
        //draws level editor grid

        // sets initial zoom and maximum zoom
        Renderer.unitsWide = Math.max(2 * gridX, 2 * gridY);
        Renderer.maxUnitsWide = Renderer.unitsWide * 2;

        for (int i = 0; i < gridX; i++) {
            for (int j = 0; j < gridY; j++) {
                Cell c = new Cell();
                c.row = i - (gridX / 2);
                c.col = j - (gridY / 2);
                c.cellType = 0; // empty space
                c.autoProp();
                cells.add(c);
            }
        }

        Renderer.init();

    }


    /**
     * Returns text to be displayed in bottom of window
     * @return
     */
    public static String getBotText() {
        switch(gameMode) {

            case 0:
                return "Good luck!";
            case 1:
                // gets name of cell that player can make by clicking
                return "Make Cell: " + Cell.getCellName(makeCell);
            case 2:
                return "Merry Christmas!";
            default:
                break;
        }

        return "Nothing to display.";
    }

    public static void setCell(Cell c, int type) {
        // sets this cell to this type
        c.cellType = type;
        c.autoProp();
    }


    /**
     * Prompts user for level name
     * @return
     */
    public static String askLevelName() {
        boolean goodname = false;
        String name = "";

        //creates directory if no levels (hmmm...)
        File f = new File("./levels");
        if(!f.exists())
        {
            //must generate
            System.out.println("Generated new levels folder!");
            f.mkdirs();
        }

        File[] listOfFiles = f.listFiles();
        while(!goodname)
        {
            name = JOptionPane.showInputDialog("Name Level:","Level " + listOfFiles.length);
            goodname = true;
            if(name == null)
            {
                //cancelled
                System.exit(0);
            }

            //makes sure name is not already in use; prompts user otherwise

            for (int i = 0; i < listOfFiles.length; i++)
            {
                if((name+".txt").equals(listOfFiles[i].getName()))
                {
                    //bad name, asks again
                    JOptionPane.showMessageDialog(null, "There already exists a level with this name!");
                    goodname = false;
                    break;
                }
            }
        }
        return name;
    }

    /**
     * Test grid for dev purposes
     */
    public static void testGrid() {
        // creates test grid (dev purposes)
        askGridSize();

        // sets initial zoom and maximum zoom
        Renderer.unitsWide = Math.max(2 * gridX, 2 * gridY);
        Renderer.maxUnitsWide = Renderer.unitsWide * 2;

        // creates test grid
        for (int i = 0; i < gridX; i++) {
            for (int j = 0; j < gridY; j++) {
                Cell c = new Cell();
                c.row = i - (gridX / 2);
                c.col = j - (gridY / 2);
                // christmas colors lol
                if (j % 2 == 0) {
                    c.setRGBA(1, 0, 0, 0.5f);
                } else {
                    c.setRGBA(0, 1, 0, 0.5f);
                }
                cells.add(c);
            }
        }

        Renderer.init();
    }


    /**
     * Asks user for grid size and sets static variables.
     */
    public static void askGridSize() {
        SpinnerNumberModel sModel = new SpinnerNumberModel(10, 1, 30, 1);
        JSpinner spinner = new JSpinner(sModel);
        JOptionPane.showMessageDialog(null, spinner, "Enter Grid Size X", JOptionPane.QUESTION_MESSAGE, null);
        gridX = (int) spinner.getValue();

        JOptionPane.showMessageDialog(null, spinner, "Enter Grid Size Y", JOptionPane.QUESTION_MESSAGE, null);
        gridY = (int) spinner.getValue();
    }

    public static boolean hasCell(int row, int col) {
        for (Cell c : cells) {
            if (c.row == row & c.col == col) {
                return true;
            }
        }
        return false;
    }

    public static Cell getCell(int row, int col) {
        // assumes only one cell of a given row and column
        for (Cell c : cells) {
            if (c.row == row & c.col == col) {
                return c;
            }
        }
        return null;
    }

    public static void selectCell(int row, int col) {
        // tries to "select" this cell by turning it white
        // deselects if it's already selected!
        if (hasCell(row, col)) {
            Cell c = getCell(row, col);
            // sets new cell if old one is not yet selected
            if (c.equals(selectedCell)) {
                // clicked same cell twice, deselects
                deselect();
            } else {
                // deselects, then sets new
                deselect();
                c.selected = true;
                selected = true;
                selectedCell = c;
            }
        }
    }

    public static void deselect() {
        // deselects selected cell
        if (selected && selectedCell != null) {
            selectedCell.selected = false;
            selectedCell = null;
            selected = false;
        }
    }

}
