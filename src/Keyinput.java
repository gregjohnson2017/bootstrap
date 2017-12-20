import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;

public class Keyinput implements KeyListener {


    @Override
    public void keyPressed(KeyEvent e) {
       // System.out.println("KEYCODE: " + e.getKeyCode());
        // depends on game mode for what keys do
        switch (e.getKeyCode()) {
            case 27:
                //escape key
                //opens escape menu
                System.out.println("Escape menu");
                break;
            case 87:
                //W key
                switch(Game.gameMode) {
                    case 0:
                        break;
                    case 1:
                        // wall
                        Game.makeCell = 1;
                        break;
                    default:
                        break;
                }
                break;
            case 68:
                //D key
                switch(Game.gameMode) {
                    case 0:
                        break;
                    case 1:
                        // empty space
                        Game.makeCell = 0;
                        break;
                    default:
                        break;
                }
                break;
            default:
                //other key
                break;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
