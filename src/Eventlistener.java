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


    @Override
    public void display(GLAutoDrawable drawable) {

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
                if (Game.hasCell(i, j)) {
                    RGBA = Game.getCell(i, j).getRGBA();
                } else {
                    RGBA[0] = 0;
                    RGBA[1] = 0;
                    RGBA[2] = 0;
                    RGBA[3] = 1;
                }

                Graphics.fillHex(2 * i + a, 1.8 * j, rad, rot, RGBA);
            }
        }


        // for rotating (startup)

        // later add a boolean to only do this at level load (also at end)

        if (rad <= 0.98) {
            rot += (double) 36 / 5;
            rad += 0.02;
        } else {
            rot = 0;
            rad = 1;
        }

        // now renders the bottom text
        TextRenderer rend = new TextRenderer(new Font("Ariel", Font.BOLD, 18));
        rend.beginRendering(Renderer.getWindowWidth(),Renderer.getWindowHeight());
        rend.setColor(1, 1, 1, 0.8f);
        rend.draw(Game.getBotText(), 5, 7);
        rend.endRendering();




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
