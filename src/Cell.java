
public class Cell {

    public float red = 0;
    public float green = 0;
    public float blue = 0;

    public int row = 0;
    public int col = 0;

    public boolean selected = false;
    public boolean selectable = true;

    public Cell() {
        // a hexagonal cell in the game
    }

    public void setRBG(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public float[] getRBG() {
        // gets color, including "selected color"
        float[] RGB = new float[3];
        if (selected) {
            // selected color (white)
            RGB[0] = 1;
            RGB[1] = 1;
            RGB[2] = 1;
        } else {
            RGB[0] = red;
            RGB[1] = green;
            RGB[2] = blue;
        }

        return RGB;
    }
}
