import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class Game {

    // grid size (for panning)
    // this is number of hexagons!
    public static int gridRows = 20;
    public static int gridCols = 20;


    public static int gridMax = 30;//maximum grid size (beyond 30 it gets laggy...)
    public static int gridDefault = 5;//default grid size (X and Y)

    public static ArrayList<Cell> cells = new ArrayList<Cell>();
    public static ArrayList<Cell> newCells = new ArrayList<Cell>(); // for loading

    // selected cell
    private static Cell selectedCell = null;
    private static boolean selected = false; // something is selected

    public static int gameMode = -1; // 0 = regular gameplay, 1 = editor, 2 = test grid (-1 = quit at title screen)

    public static String levelName = "NO LEVEL"; // level name, either playing or editing (no .txt)

    public static int makeCellType = 0; // integer to determine which cell to make

    public static boolean firstLoad = true; // load at beginning (for animations...)

    /**
     * Runs game in usual gameplay.
     */
    public static void runGame() {

        //creates directory if it does not exist
        File f = new File("./levels");
        if (!f.exists()) {
            //must generate
            System.out.println("Generated new levels folder!");
            f.mkdirs();
        }


        String[] opts = {"Play Level",
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
        //first asks if player wants to load level or create new
        String[] opts = {"Load Level to Edit",
                "Create New Level"};
        int loadOrCreate = JOptionPane.showOptionDialog(null,
                "Load level to edit or create new?",
                "Bootstrap",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opts,
                opts[0]);

        switch (loadOrCreate) {
            case 0:
                //load previous level
                if (!loadLevel()) {
                    // user cancelled
                    // just ends code, I'm too lazy to return user to a previous menu at this time
                    System.exit(0);
                }
                // needs to set cells
                cells.addAll(newCells);
                newCells.clear();
                break;
            case 1:
                //create new level
                levelName = askLevelName();
                askGridSize();
                //draws level editor grid

                // sets initial zoom and maximum zoom
                Renderer.setUnitsWide(Math.max(2 * gridRows, 2 * gridCols));

                for (int i = 0; i < gridRows; i++) {
                    for (int j = 0; j < gridCols; j++) {
                        Cell c = new Cell();
                        c.row = i - (gridRows / 2);
                        c.col = j - (gridCols / 2);
                        c.setType(0); // empty space
                        cells.add(c);
                    }
                }
                break;
            default:
                System.exit(0);
                break;
        }
        firstLoad = false;
        Eventlistener.introAnimation = true;
        Renderer.init();
    }

    public static boolean loadLevel() {
        // load level dialogue
        // returns boolean if successful
        // outro animation then pauses once complete
        //the event listener calls another method to finish loading
        Eventlistener.pauseRendering = true;
        boolean success = true;
        String fileText = "";
        final JFileChooser fc = new JFileChooser("./levels");
        int returnVal = fc.showOpenDialog(null);
        File f;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            f = fc.getSelectedFile();
            FileReader fr = null;
            try {
                fr = new FileReader(f.getPath());
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            BufferedReader br = new BufferedReader(fr);
            try {
                fileText = br.readLine(); //should only be one line...
            } catch (IOException e) {
                e.printStackTrace();
            }

            newCellsFromString(fileText);
        } else {
            success = false;
        }
        Eventlistener.pauseRendering = false;

        if (success) {
            if (firstLoad) {
                // only do intro animation
                //sets up renderer again
                Renderer.setUnitsWide(Math.max(2 * Game.gridRows, 2 * Game.gridCols));
            } else {
                // have event listener handle outro and rest of load
                Eventlistener.finishLoad = true;
                Eventlistener.outroAnimationBegun = false;
                Eventlistener.outroAnimation = true;
                // since eventlistener knows how long this takes, it handles the rest of the load (kinda ugly... whatever)
            }
        }
        return success;
    }

    public static void newCellsFromString(String saveString) {
        // loads level from string to "newcells"
        // assumes renderer is already initialized, this just sets the cell array
        // also assumed saveString is well formatted
        // does not clear cells!!! this is handled elsewhere...
        String savemod = saveString;
        int i0 = savemod.indexOf('['); //assumes no brackets in save name, or else this breaks big time!
        levelName = savemod.substring(0, i0);
        savemod = savemod.substring(i0);
        i0 = savemod.indexOf(']');
        gridRows = Integer.parseInt(savemod.substring(1, i0));
        savemod = savemod.substring(i0 + 1);
        i0 = savemod.indexOf(']');
        gridCols = Integer.parseInt(savemod.substring(1, i0));
        savemod = savemod.substring(i0 + 1);
        // now goes through a loop chopping away at the string and adding cells
        while (savemod.length() > 0) {
            int i1 = savemod.indexOf(']');
            Cell c = new Cell();
            c.propFromString(savemod.substring(0, i1 + 1));
            newCells.add(c);
            savemod = savemod.substring(i1 + 1);
        }
    }

    public static String saveLevel() {
        // saves current level to file
        // also returns the string, if any other method wants to load it right away
        // assumes level is already named, (in game class)
        // right now doesn't matter if in editor or not, but option is only available in editor
        // overwrites anything of the same name!

        String saveString = "";
        for (Cell c : cells) {
            saveString += c.writePropString();
        }
        saveString = levelName + "[" + gridRows + "][" + gridCols + "]" + saveString;
        //writes to file
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("levels\\" + levelName + ".txt", "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        writer.print(saveString);
        writer.close();

        return saveString;
    }

    public static void clearCellOfType(int type) {
        // removes first appearance of this cell
        // called to remove unique cells, so this should suffice
        for (Cell c : cells) {
            if (c.getType() == type) {
                c.setType(0);
            }
        }
    }

    /**
     * Returns text to be displayed in bottom of window
     *
     * @return
     */
    public static String getBotText() {
        switch (gameMode) {
            case 0:
                return "Good luck!";
            case 1:
                // gets name of cell that player can make by clicking
                return "Make Cell: " + Cell.getCellName(makeCellType);
            case 2:
                return "Merry Christmas!";
            default:
                break;
        }

        return "Nothing to display.";
    }

    public static void setCell(Cell c, int type) {
        // sets this cell to this type
        // except for keys! these are handled differently.
        if(type != 7) {
            c.setType(type);
        }
        // must be in level editor, and either be labelling something OR be giving a key and be allowed to do so
        if (gameMode == 1 && ((c.customLabel && type != 7) || (type == 7 && c.canHaveKey))) {
            // forbids user from using brackets in label!!!
            boolean goodLabel = false;
            String proposedLabel = null;
            while (!goodLabel) {
                proposedLabel = JOptionPane.showInputDialog("Enter label for " + c.getCellName(type),
                        "0");
                //can have empty label, but NOT empty key
                goodLabel = true;
                if(proposedLabel == null || proposedLabel.equals("")) {
                    if( type != 7) {
                        proposedLabel = ""; // sets to empty
                        goodLabel = true;
                    } else {
                        //cannot have empty key!
                        goodLabel = false;
                    }
                } else if (proposedLabel.contains("[") || proposedLabel.contains("]")) {
                    JOptionPane.showMessageDialog(null, "Label cannot contain '[' or '']!",
                            "Naming Level",
                            JOptionPane.WARNING_MESSAGE);
                    goodLabel = false;
                }
            }
            if(type != 7) {
                c.label = proposedLabel;
            } else {
                c.hasKey = true;
                c.key = proposedLabel;
            }
        }

    }

    /**
     * Prompts user for level name
     *
     * @return
     */
    public static String askLevelName() {
        boolean goodname = false;
        String name = "";

        File f = new File("./levels");


        File[] listOfFiles = f.listFiles();
        while (!goodname) {
            name = JOptionPane.showInputDialog("Name Level:", "Level " + listOfFiles.length);
            goodname = true;
            if (name == null) {
                //cancelled
                System.exit(0);
            }

            //makes sure name is not already in use; prompts user otherwise

            for (int i = 0; i < listOfFiles.length; i++) {
                if ((name + ".txt").equals(listOfFiles[i].getName())) {
                    //bad name, asks again
                    int useAnyway = JOptionPane.showConfirmDialog(null,
                            "There already exists a level with this name! Overwrite?",
                            "Naming Level",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);
                    if (useAnyway == 0) {
                        goodname = true;
                    } else {
                        goodname = false;

                    }
                    break;
                }
            }

            if (name.contains("[") || name.contains("]")) {
                JOptionPane.showMessageDialog(null, "Name cannot contain '[' or '']!",
                        "Naming Level",
                        JOptionPane.WARNING_MESSAGE);
                goodname = false;
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
        Renderer.unitsWide = Math.max(2 * gridRows, 2 * gridCols);
        Renderer.maxUnitsWide = Renderer.unitsWide * 2;

        // creates test grid
        for (int i = 0; i < gridRows; i++) {
            for (int j = 0; j < gridCols; j++) {
                Cell c = new Cell();
                c.row = i - (gridRows / 2);
                c.col = j - (gridCols / 2);
                // christmas colors lol
                if (j % 2 == 0) {
                    c.setRGBA(1, 0, 0, 0.5f);
                } else {
                    c.setRGBA(0, 1, 0, 0.5f);
                }
                cells.add(c);
            }
        }
        Eventlistener.introAnimation = true;
        Renderer.init();
    }


    /**
     * Asks user for grid size and sets static variables.
     */
    public static void askGridSize() {
        SpinnerNumberModel sModel = new SpinnerNumberModel(gridDefault, 1, gridMax, 1);
        JSpinner spinner = new JSpinner(sModel);
        JOptionPane.showMessageDialog(null, spinner, "Enter Grid Rows", JOptionPane.QUESTION_MESSAGE, null);
        gridRows = (int) spinner.getValue();

        JOptionPane.showMessageDialog(null, spinner, "Enter Grid Columns", JOptionPane.QUESTION_MESSAGE, null);
        gridCols = (int) spinner.getValue();
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
