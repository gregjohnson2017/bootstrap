import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class Game {

    // grid size (for panning)
    // this is number of hexagons!
    public static int gridRows = 5;
    public static int gridCols = 5;


    public static final int gridMax = 10;//maximum grid size (terrible renderer gets laggy at 10 already...)
    public static final int gridDefault = 5;//default grid size (X and Y)
    public static final int maxKeys = 3; // max keys in a cell/player (any given array)

    public static ArrayList<Cell> cells = new ArrayList<Cell>();
    public static ArrayList<Cell> newCells = new ArrayList<Cell>(); // for loading

    public static String currentLevelString = ""; // string for currently loaded level

    // selected cell
    public static Cell selectedCell = null;
    public static boolean selected = false; // something is selected

    public static int gameMode = -1; // 0 = regular gameplay, 1 = editor, 2 = test grid (-1 = quit at title screen)

    public static String levelName = "NO LEVEL"; // level name, either playing or editing (no .txt)

    public static int makeCellType = 0; // integer to determine which cell to make

    public static boolean firstLoad = true; // load at beginning (for animations...)

    public static int command = 0; // command in game (move or interact)

    public static int currentTimestep = 0; // current time step
    public static boolean lockTimestep = false;

    public static int highestBootnum = 0; // highest bootnum of any player (needs to be kept updated...!)
    public static ArrayList<Player> players = new ArrayList<Player>(); // array of players

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

        //gives first player, even if user only uses editor
        players.add(new Player());

        String[] opts = {"Play Level",
                "Level Editor"};
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
                //load previous level
                if (!loadLevel(false)) {
                    // user cancelled
                    // just ends code, I'm too lazy to return user to a previous menu at this time
                    System.exit(0);
                }
                // needs to set cells
                cells.addAll(newCells);
                newCells.clear();
                firstLoad = false;
                Eventlistener.introAnimation = true;
                Renderer.init();
                break;
            case 1:
                // level editor
                gameMode = 1;
                levelEditor();
                break;
            case 2:
                // test grid
                // option has been removed for now :(
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
                if (!loadLevel(false)) {
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
                Renderer.setUnitsWide(Math.max(3 * gridRows, 3 * gridCols));

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

    public static boolean loadLevel(boolean reloadCurrent) {
        // load level dialogue
        // returns boolean if successful
        // outro animation then pauses once complete
        //the event listener calls another method to finish loading
        Eventlistener.pauseRendering = true;
        boolean success = true;
        if (!reloadCurrent) {
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
                currentLevelString = fileText;
            } else {
                success = false;
            }

        }

        if (currentLevelString.isEmpty()) {
            return false;
        }

        // gets new cells, either from current level to restart or new level from above file read
        newCellsFromString(currentLevelString);

        Eventlistener.pauseRendering = false;

        if (success) {
            if (firstLoad) {
                // only do intro animation
                //sets up renderer again
                Renderer.setUnitsWide(Math.max(3 * Game.gridRows, 3 * Game.gridCols));
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
        // saves current level to file, and sets current level text too
        // also returns the string, if any other method wants to load it right away
        // assumes level is already named, (in game class)
        // right now doesn't matter if in editor or not, but option is only available in editor
        // overwrites anything of the same name!

        String saveString = "";
        for (Cell c : cells) {
            saveString += c.writePropString();
        }
        saveString = levelName + "[" + gridRows + "][" + gridCols + "]" + saveString;
        currentLevelString = saveString;
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
        // also checks if type = 3, means set player to empty cell
        for (Cell c : cells) {
            if (c.getType() == type || (type == 3 && c.player != null)) {
                c.player = null;
                c.setType(0);
            }
        }
    }

    /**
     * Returns text to be displayed in bottom of window
     */
    public static String getBotText() {
        switch (gameMode) {
            case 0:
                String s = "";
                // timestep
                s += "Timestep: " + currentTimestep;
                // now selected cell
                if (selected) {
                    s += " | Selected: " + selectedCell.getThisCellName();
                } else {
                    s += " | No Selection";
                }
                // now move or interact depending on command
                s += " | Command: ";
                switch (command) {
                    case 0:
                        s += "Move";
                        break;
                    case 1:
                        s += "Interact";
                        break;
                    default:
                        s += "NO COMMAND (this is an error...)";
                        break;
                }
                return s;
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

    public static void advanceTimestep() {
        // code for advancing timestep including animations, new grid, etc.
        // tells eventlistener to do animation IF anything is moving
        // only does this if not locked! (in animation for example)
        if (!lockTimestep) {
            lockTimestep = true;
            boolean movingPlayers = false;
            for (Cell c : cells) {
                if (c.hasMovingPlayer()) {
                    Eventlistener.moveAnimation = true;
                    // eventlistener will tell Game to finish timestpe
                    movingPlayers = true;
                    break;
                }
            }
            if (!movingPlayers) {
                // this method will finish timestep
                finishTimestep();
            }
            // advances timestep integer
            currentTimestep++;
        }
    }

    public static void finishTimestep() {
        // updates cell array and stuff for moving players
        for (Cell c : cells) {
            if (c.hasMovingPlayer()) {
                // gives player to new cell, removes from old
                // also merges keys (assumes no overflow! this was checked eariler...)
                Cell d = getCell(c.player.mRow, c.player.mCol);
                c.addAllKeys(d.getKeyArray()); //adds keys to old cell
                d.player = c.player; //sets new cell to player
                c.player = null;
                // player should have key array...
                c.clearKeys();
                d.player.move();
            }
        }
        // unlocks
        lockTimestep = false;
    }

    public static void setCell(Cell c, int type) {
        // sets this cell to this type
        // except for keys and players! these are handled differently.
        if (type != 7 && type != 3) {
            c.setType(type);
        }
        // must be in level editor, and either be labelling something OR be giving a key and be allowed to do so
        if (gameMode == 1 && ((c.customLabel && type != 7) || (type == 7 && c.canHaveKey
                && c.firstEmptyKeyIndex() >= 0))) {
            // forbids user from using brackets in label!!!
            boolean goodLabel = false;
            String proposedLabel = null;
            while (!goodLabel) {
                proposedLabel = JOptionPane.showInputDialog("Enter label for " + c.getCellName(type),
                        "0");
                //can have empty label, but NOT empty key
                goodLabel = true;
                if (proposedLabel == null || proposedLabel.equals("")) {
                    if (type != 7) {
                        proposedLabel = ""; // sets to empty
                        goodLabel = true;
                    } else {
                        //cannot have empty key!
                        goodLabel = false;
                    }
                } else if (proposedLabel.contains("[") || proposedLabel.contains("]")
                        || proposedLabel.contains("|") || proposedLabel.contains(",")) {
                    JOptionPane.showMessageDialog(null, "Label cannot contain '[' ']' '|' " +
                                    "or ','!",
                            "Naming Level",
                            JOptionPane.WARNING_MESSAGE);
                    goodLabel = false;
                }
            }
            if (type != 7) {
                c.label = proposedLabel;
            } else {
                c.addKey(proposedLabel);
            }
        } else if (type == 7 && c.canHaveKey
                && c.firstEmptyKeyIndex() == -1) {
            // too many keys
            JOptionPane.showMessageDialog(null, "Too many keys!",
                    "Cannot add key",
                    JOptionPane.WARNING_MESSAGE);
        }
        // otherwise, is setting to player (on empty space)
        // this is called during level editor, so resets keys here
        if (type == 3) {
            c.setType(0);
            c.player = players.get(0);
            c.player.resetKeys();
        }

    }

    public static boolean checkAdjacent(Cell c1, Cell c2) {
        // checks adjacency between cells
        // returns false if same cell!
        if (c1.row == c2.row && Math.abs(c1.col - c2.col) == 1) {
            // same row, one apart
            return true;
        }
        // otherwise, has to switch on parity of first cell's row
        switch (c1.row % 2) {
            case 0:
                // even rows keep column to left
                if (Math.abs(c1.row - c2.row) == 1 && (c1.col == c2.col || c1.col == c2.col - 1)) {
                    return true;
                }
                break;
            default:
                // odd rows keep column to right
                if (Math.abs(c1.row - c2.row) == 1 && (c1.col == c2.col || c1.col == c2.col + 1)) {
                    return true;
                }
                break;
        }
        return false;
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
        Renderer.unitsWide = Math.max(3 * gridRows, 3 * gridCols);
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

    public static void showEditorHelp() {
        JOptionPane.showMessageDialog(null,
                "Scrolling will zoom in and out, and holding right click will pan. \n" +
                        "Left click on a cell to change its type, (you may be prompted for a label.) \n" +
                        "Keys must have a nonempty label at can only be put on a player or empty space. \n" +
                        "To remove a key or change a label, put down the same cell on top. \n" +
                        "Buttons, doors, and time machines can have an empty label. Labels determine linkage. \n" +
                        "There can only be one player and one exit. Putting down another will remove the old one. \n" +
                        "\n" +
                        "Press a key to change cell type: \n" +
                        "W = Wall (Dark Gray) \n" +
                        "E = Empty Space (Light Gray) \n" +
                        "P = Player (Green)\n" +
                        "B = Button (Red)\n" +
                        "O = Open Door (Light Purple)\n" +
                        "C = Closed Door (Dark Purple)\n" +
                        "L = Locked Door (Brown)\n" +
                        "K = Key (Orange Text)\n" +
                        "X = Exit (Yellow)\n" +
                        "T = Time Machine (Blue)\n"
                , "Bootstrap Level Editor", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showGameHelp() {
        JOptionPane.showMessageDialog(null,
                "Bootstrap is a puzzle game that incorporates time travel. \n" +
                        "Click on a cell to select or deselect it, (if the cell is selectable.) \n" +
                        "Press (I) to interact, and (M) to move. (CTRL) toggles between them. \n" +
                        "\n" +
                        "Selecting a player and then clicking an adjacent cell in move mode will plan a move. \n" +
                        "This move will be completed in the next time step. Click on the player to cancel. \n" +
                        "Players can move through empty space, open doors, onto buttons, and time machines. \n" +
                        "To win the puzzle, move onto the exit after fulfilling all bootstraps (see below).  \n" +
                        "\n" +
                        "If a player moves onto a button, it will open or close doors with the same label. \n" +
                        "A button will deactivate if the player moves off. \n" +
                        "A closing door will kill any player under it! Players move first, then doors open/close. \n" +
                        "\n" +
                        "If you move onto an empty space with a key, that player will automatically pick it up. \n" +
                        "Interact with another player to give them a key, or empty space to drop it. \n" +
                        "Keys can be used to unlock doors, which will turn them into open doors. \n " +
                        "Each player can only hold one key, (they are extremely heavy.) \n" +
                        "\n" +
                        "Press (SPACE) to advance timestep. This will manifest all movements. \n" +
                        "This will also cause players to age, displayed in red text on the player. \n" +
                        "A question mark indicates that the player is at least this old, (if bootstrapped.) \n" +
                        "More than one question mark indicates the order in which the players were bootstrapped. \n" +
                        "\n" +
                        "Time machines are the exciting element that makes Bootstrap a unique puzzle game. \n" +
                        "Players can interact with time machines in two ways: Bootstrapping, and Fulfilling. \n" +
                        "When bootstrapping, the player summons a version of their self from the future to appear. \n" +
                        "This player can be bootstrapped holding a key, itself said to be bootstrapped. \n" +
                        "Every additional bootstrapped player will receive another question mark by their age. \n " +
                        "The time machine must be vacant at the moment of bootstrapping! \n" +
                        "All bootstrapped players and keys must be 'fulfilled' before completing the puzzle. \n" +
                        "This means a younger player (perhaps with a key) must be sent back in time. \n" +
                        "You must fulfill bootstraps with a player exactly one question mark older. \n" +
                        "This is to prevent older players from 'growing up' to become younger players. \n" +
                        "\n" +
                        "After completing a puzzle, you can 'view' your solution through the eyes of the player! \n" +
                        "This is the only time you actually experience time travel. The game is otherwise linear. \n"
                , "Bootstrap", JOptionPane.INFORMATION_MESSAGE);
    }

}
