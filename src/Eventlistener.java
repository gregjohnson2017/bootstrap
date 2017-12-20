import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

public class Eventlistener implements GLEventListener {

    public static GL2 gl = null;
    private double rot = 0;
    private double rad = 0;


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

        for (int i = (int) (-1 * Renderer.maxUnitsWide); i < (int) (1 * Renderer.maxUnitsWide); i++) {
            for (int j = (int) (-1 * Renderer.getUnitsTall(Renderer.maxUnitsWide)); j < (int) (1
                    * Renderer.getUnitsTall(Renderer.maxUnitsWide)); j++) {
                // parity for grid offset
                if (j % 2 == 0) {
                    a = 1;
                } else {
                    a = 0;
                }
                // adds colored cell if it can, otherwise sets it to black
                float[] RGB = new float[3];
                if (Game.hasCell(i, j)) {
                    RGB = Game.getCell(i, j).getRGB();
                } else {
                    RGB[0] = 0;
                    RGB[1] = 0;
                    RGB[2] = 0;
                }

//                if(i == 0 && j == 0) {
//                    System.out.println((double)(2f * i + a) + ", " + (double)(1.85f * j));
//                }

                Graphics.fillHex(2 * i + a, 1.85 * j, rad, rot, RGB);
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

//      System.out.println(Renderer.centerOffX + ", " + Renderer.centerOffY);
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
