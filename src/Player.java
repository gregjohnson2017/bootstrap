public class Player {
    public int bootNum = 0; // number bootstrapped in total
    public int birthday = 0;

    // current location
    public int col = 0;
    public int row = 0;
    // proposed move location
    public int mCol = 0;
    public int mRow = 0;
    public boolean willMove = false;

    public String key = "";
    public boolean hasKey = false;

    public String getAge() {
        // returns age string
        String age = "";
        for (int i = 0; i < bootNum; i++) {
            age += "?";
        }
        if(bootNum > 0) {
            age += " + ";
        }
        age += "" + (Game.currentTimestep - birthday);
        return age;
    }

    public void move() {
        // moves
        row = mRow;
        col = mCol;
        willMove = false;
    }
}
