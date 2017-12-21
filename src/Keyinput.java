import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;

import javax.swing.*;

public class Keyinput implements KeyListener {


    @Override
    public void keyPressed(KeyEvent e) {
         System.out.println("KEYCODE: " + e.getKeyCode());
        // depends on game mode for what keys do
        switch (e.getKeyCode()) {
            case 27:
                //escape key
                //opens escape menu

                switch (Game.gameMode) {
                    case 0:
                        break;
                    case 1:
                        //escape menu for level editor
                        String[] opts = {"Save and Play Level",
                                "Save Level",
                                "Rename",
                                "Load Level to Edit",
                                "Show Help"};
                        int a = JOptionPane.showOptionDialog(null,
                                "DO NOT SCROLL MOUSE IN THIS MENU! WILL CRASH GAME! Hit ESC again to close.",
                                "Boostrap Level Editor",
                                JOptionPane.YES_NO_CANCEL_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                opts,
                                opts[0]);
                        switch (a) {
                            case 0:
                                // save and play
                                Game.newCellsFromString(Game.saveLevel());
                                Game.gameMode = 1;
                                break;
                            case 1:
                                // save
                                Game.saveLevel();
                                break;
                            case 2:
                                // rename
                                Game.levelName = Game.askLevelName();
                                Renderer.setWindowTitle();
                                break;
                            case 3:
                                // load to edit
                                // if it doesn't work, doesn't matter - just returns user to editing
                                Game.loadLevel();
                                break;
                            case 4:
                                // show help
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }


                break;
            case 87:
                //W key
                switch (Game.gameMode) {
                    case 0:
                        break;
                    case 1:
                        // wall
                        Game.makeCellType = 1;
                        break;
                    default:
                        break;
                }
                break;
            case 69:
                //E key
                switch (Game.gameMode) {
                    case 0:
                        break;
                    case 1:
                        // empty space
                        Game.makeCellType = 0;
                        break;
                    default:
                        break;
                }
                break;
            case 66:
                //B key
                switch(Game.gameMode) {
                    case 0:
                        break;
                    case 1:
                        // button
                        Game.makeCellType = 2;
                        break;
                    default:
                        break;
                }
                break;
            case 80:
                //P key
                switch(Game.gameMode) {
                    case 0:
                        break;
                    case 1:
                        // player
                        Game.makeCellType = 3;
                        break;
                    default:
                        break;
                }
            default:
                //other key
                break;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
