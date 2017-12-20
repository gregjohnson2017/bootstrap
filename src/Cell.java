
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
    public boolean selectable = true;

    /**
     * 0 = empty space
     * 1 = wall
     */
    public int cellType = 0; //cell type (for automatic color setting)

    public Cell() {
        // a hexagonal cell in the game
    }

    /**
     * Automatically sets cell colors and properties due to cell type
     */
    public void autoProp() {
        switch(cellType) {
            case 0:
                // empty space
                red = 0.8f;
                green = 0.8f;
                blue = 0.8f;
                break;
            case 1:
                // wall
                red = 0.1f;
                green = 0.1f;
                blue = 0.1f;
                break;
            default:
                System.out.println("Unknown cell type! autoProp failed.");
                break;
        }
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
        switch(type) {
            case 0:
                return "Empty Space";
            case 1:
                return "Wall";
            default:
                return "INVALID CELL TYPE";
        }
    }
}
