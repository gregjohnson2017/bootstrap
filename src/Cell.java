
public class Cell {

    public float red = 0;
    public float green = 0;
    public float blue = 0;
    public float alpha = 0;

    // alpha values for selected and deselected
    public float selAlpha = 1;
    public float deselAlpha = 0.6f;

    public int row = 0;
    public int col = 0;

    public boolean selected = false;

    // some cells are labelled (e.g. doors link to buttons, players)
    public String label = "";
    public boolean labelled = false;

    // unique cells cannot appear twice in a grid
    // for players, this is true but only per label (for editor usage)
    public boolean unique = false;

    /**
     * 0 = empty space
     * 1 = wall
     */
    private int cellType = 0; //cell type (for automatic color setting)

    public void propFromString(String propString) {
        //gets properties from a string, (assumes well-formatted!)
        //first gets row
        int i1 = propString.indexOf('|');
        row = Integer.parseInt(propString.substring(1, i1));
        String rest1 = propString.substring(i1 + 1);
        int i2 = rest1.indexOf('|');
        col = Integer.parseInt(rest1.substring(0, i2));
        String rest2 = rest1.substring(i2 + 1);
        int i3 = rest2.indexOf('|');
        cellType = Integer.parseInt(rest2.substring(0, i3));
        String rest3 = rest2.substring(i3 + 1);
        label = rest3.substring(0, rest3.length() - 1);
        autoProp();
    }

    public String writePropString() {
        // writes all properties to string (for saving)
        // only writes properties that cannot be inferred (e.g. does not include RGBA)
        String propString = "[" + row + "|" + col + "|" + cellType + "|" + label + "]";
        return propString;
    }

    /**
     * Automatically sets cell colors and properties due to cell type
     */
    public void autoProp() {
        switch (cellType) {
            case 0:
                // empty space
                red = 0.9f;
                green = 0.9f;
                blue = 0.9f;
                labelled = false;
                break;
            case 1:
                // wall
                red = 0.05f;
                green = 0.05f;
                blue = 0.05f;
                labelled = false;
                break;
            case 2:
                // button
                red = 0.5f;
                green = 0;
                blue = 0;
                labelled = true;
                break;
            case 3:
                // player
                red = 0;
                green = 1;
                blue = 0;
                labelled = true;
                break;
            default:
                System.out.println("Unknown cell type! autoProp failed.");
                break;
        }
        // sets unique
        unique = isUnique(cellType);
        // makes sure doesn't have label from past cell type
        if (!labelled) {
            label = "";
        }
    }

    public static boolean isUnique(int cellType) {
        switch (cellType) {
            case 0:
                return false; //empty space
            case 1:
                return false; //wall
            case 2:
                return false; //button
            case 3:
                return true; //player
            default:
                break;
        }
        return false;
    }

    public void setType(int cellType) {
        // sets type and formats cell
        this.cellType = cellType;
        autoProp();
    }

    public int getType() {
        return cellType;
    }

    public void setRGBA(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public float[] getRGBA() {
        // gets color, including "selected color"
        float[] RGBA = new float[4];
        RGBA[0] = red;
        RGBA[1] = green;
        RGBA[2] = blue;
        if (selected) {
            RGBA[3] = selAlpha;
        } else {
            RGBA[3] = deselAlpha;
        }

        return RGBA;
    }

    public static String getCellName(int type) {
        // gets name of cell with this type
        switch (type) {
            case 0:
                return "Empty Space";
            case 1:
                return "Wall";
            case 2:
                return "Button";
            case 3:
                return "Player";
            default:
                return "INVALID CELL TYPE";
        }
    }
}
