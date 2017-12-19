import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

public class Eventlistener implements GLEventListener {

    public static GL2 gl = null;

    @Override
    public void display(GLAutoDrawable drawable) {
        gl = drawable.getGL().getGL2();

        gl.glClearColor(0.3f, 0.3f, 0.3f, 1.0f);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        float unitsTall = Renderer.getWindowHeight() / (Renderer.getWindowWidth() / Renderer.unitsWide);

        gl.glOrtho(-Renderer.unitsWide / 2 + Renderer.centerOffX, Renderer.unitsWide / 2 + Renderer.centerOffX,
                -unitsTall / 2 + Renderer.centerOffY, unitsTall / 2 + Renderer.centerOffY, -1, 1);
        gl.glMatrixMode(GL2.GL_MODELVIEW);

        int a = 0;

        // will draw hexagons as far as you can zoom

        for (int i = (int) (-2 * Renderer.maxUnitsWide); i < (int) (2 * Renderer.maxUnitsWide); i++) {
            for (int j = (int) (-2 * Renderer.getUnitsTall(Renderer.maxUnitsWide)); j < (int) (2
                    * Renderer.getUnitsTall(Renderer.maxUnitsWide)); j++) {
                // parity for grid offset
                if (j % 2 == 0) {
                    a = 1;
                } else {
                    a = 0;
                }
                // adds colored cell if it can, otherwise sets it to black
                if (Game.hasCell(i, j)) {
                    Cell c = Game.getCell(i, j);
                    float[] RGB = c.getRBG();
                    Graphics.setColor(RGB[0], RGB[1], RGB[2]);
                } else {
                    Graphics.setColor(0, 0, 0);
                }
                Graphics.fillHex(2 * i + a, (float) (1.9 * j), 1);
            }
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
