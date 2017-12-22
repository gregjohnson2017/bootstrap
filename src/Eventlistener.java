import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.util.awt.TextRenderer;

import java.awt.Font;

public class Eventlistener implements GLEventListener {

    public static GL2 gl = null;
    private double rot = 0;
    private double rad = 0;

    public static boolean pauseRendering = false;

    public static boolean introAnimation = false;
    public static boolean introAnimationBegun = false;

    public static boolean outroAnimation = false;
    public static boolean outroAnimationBegun = false;

    // after display finishes outro animation, should finish the loading process in Game method
    public static boolean finishLoad = false;


    @Override
    public void display(GLAutoDrawable drawable) {

        // will pause rendering when game is loading a new level or something like that
        if (!pauseRendering) {

            // must prepare for intro animation if this is the case
            if (introAnimation && !introAnimationBegun) {
                rad = 0;
                rot = 0;
                introAnimationBegun = true;
            }
            if (outroAnimation && !outroAnimationBegun) {
                rad = 1;
                rot = 0;
                outroAnimationBegun = true;
            }

            gl = drawable.getGL().getGL2();
            gl.glEnable(GL.GL_BLEND);
            gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

            gl.glClearColor(0.3f, 0.3f, 0.3f, 1.0f);
            gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

            gl.glMatrixMode(GL2.GL_PROJECTION);
            gl.glLoadIdentity();

            float unitsTall = Renderer.getWindowHeight() / (Renderer.getWindowWidth() / Renderer.unitsWide);

            //shifts slightly to account for hexagon sizing
            gl.glOrtho(-Renderer.unitsWide / 2 + Renderer.centerOffX, Renderer.unitsWide / 2 + Renderer.centerOffX,
                    -unitsTall / 2 + Renderer.centerOffY, unitsTall / 2 + Renderer.centerOffY, -1, 1);
            gl.glMatrixMode(GL2.GL_MODELVIEW);


            int a = 0;

            // will draw hexagons as far as you can zoom, (taking maximum dimension)
            int totalSize = (int) Math.max(Renderer.maxUnitsWide, Renderer.getUnitsTall(Renderer.maxUnitsWide));

            for (int i = -totalSize; i < totalSize; i++) {
                for (int j = -totalSize; j < totalSize; j++) {
                    // parity for grid offset
                    if (j % 2 == 0) {
                        a = 1;
                    } else {
                        a = 0;
                    }
                    // adds colored cell if it can, otherwise sets it to black
                    float[] RGBA = new float[4];
                    String s = null;
                    String k = null;
                    if (Game.hasCell(i, j)) {
                        Cell c = Game.getCell(i, j);
                        RGBA = c.getRGBA();
                        if (c.labelled) {
                            s = c.label;
                        }
                        if(c.hasKey) {
                            k = c.key;
                        }
                    } else {
                        RGBA[0] = 0;
                        RGBA[1] = 0;
                        RGBA[2] = 0;
                        RGBA[3] = 1;
                    }
                    Graphics.fillHex(2 * i + a, 1.75 * j, rad, rot, RGBA, s,k);
                }
            }


            // intro animation
            if (rad <= 0.98 && introAnimation) {
                rot += (double) 360 / 50; //360 degrees divided by 50 radius increases of size 0.02 up to 1
                rad += 0.02;
            } else if (rad > 0 && outroAnimation) {
                rot -= (double) 360 / 50;
                rad -= 0.02;
            } else {
                rot = 0;
                rad = 1;
                // resets booleans for next animation
                introAnimation = false;
                introAnimationBegun = false;

                if (finishLoad) {
                    // this means it just completed its outro animation
                    // now can finish loading new level
                    // THIS MOST LIKELY HAS BUGS OR AT LEAST INCOMPATIBLE WITH FUTURE FEATURES!!!
                    // the only reason this goes here is because I want the new level to be rendered only after
                    // the animation here has finished, and only eventlistener has the thread to know when this is done
                    Game.cells.clear();
                    Game.cells.addAll(Game.newCells);
                    Game.newCells.clear();
                    //sets up renderer again
                    Renderer.setUnitsWide(Math.max(2 * Game.gridRows, 2 * Game.gridCols));
                    Renderer.setWindowTitle();
                    //does intro animation
                    introAnimation = true;
                    introAnimationBegun = false;
                    finishLoad = false;
                }

                outroAnimation = false;
                outroAnimationBegun = false;
            }

            // now renders the bottom text
            TextRenderer botRend = new TextRenderer(new Font("Ariel", Font.BOLD, 18));
            botRend.beginRendering(Renderer.getWindowWidth(), Renderer.getWindowHeight());
            botRend.setColor(1, 1, 1, 0.8f);
            botRend.draw(Game.getBotText(), 5, 7);
            botRend.endRendering();

        }
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glClearColor(0, 0, 0, 1);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        float unitsTall = Renderer.getUnitsTall(Renderer.unitsWide);

        gl.glOrtho(-Renderer.unitsWide / 2, Renderer.unitsWide / 2, -unitsTall / 2, unitsTall / 2, -1, 1);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }
}
