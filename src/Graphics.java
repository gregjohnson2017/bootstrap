import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.awt.TextRenderer;

import java.awt.*;

public class Graphics {
    public static GL2 gl = null;

    private static double[] sch = new double[12]; // sch = sin cos hex coords (only need to compute once)

    private static TextRenderer hexText = new TextRenderer(new Font("Ariel", Font.BOLD, 12));

    public static void fillHex(double x, double y, double rad, double rot, float[] RGBA, String s) {
        gl = Eventlistener.gl;

        gl.glColor4f(RGBA[0], RGBA[1], RGBA[2], RGBA[3]);

        gl.glTranslated(x, y, 0);
        gl.glRotated(rot, 0, 0, 1);

        gl.glBegin(GL2.GL_POLYGON);
        for (int i = 0; i < 6; ++i) {
            gl.glVertex2d(rad * sch[i], rad * sch[6 + i]);
        }
        gl.glEnd();

        if (s != null) {
            hexText.beginRendering(Renderer.getWindowWidth(), Renderer.getWindowHeight());
            hexText.setColor(1, 1, 1, 1);
            // this should draw to the middle of the hexagon
            // note that the draw method takes pixels from bottom left of window, NOT units!
            // this is why it looks so ugly and opaque... ~~~but it works!~~~
            hexText.draw(s, (int)((x-Renderer.centerOffX+Renderer.unitsWide/2)*Renderer.getPixelsPerUnit()
                            - s.length()*2),
                    (int)((Renderer.getUnitsTall(Renderer.unitsWide)/2+y-Renderer.centerOffY)
                            *Renderer.getPixelsPerUnit())); // "- s.length()*2" shifts over to center text kinda
            hexText.endRendering();
        }

        gl.glRotated(-rot, 0, 0, 1);
        gl.glTranslated(-x, -y, 0);
    }

    public static void computeStuff() {
        for (int i = 0; i < 6; i++) {
            sch[i] = Math.sin(i / 6.0 * 2 * Math.PI);
            sch[6 + i] = Math.cos(i / 6.0 * 2 * Math.PI);
        }
    }
}
