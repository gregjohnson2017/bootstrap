import com.jogamp.opengl.GL2;

public class Graphics {
    public static GL2 gl = null;

    private static double[] sch = new double[12]; // sch = sin cos hex coords (only need to compute once)

    public static void fillHex(double x, double y, double rad, double rot, float[] RGBA) {
        gl = Eventlistener.gl;

        gl.glColor4f(RGBA[0], RGBA[1], RGBA[2], RGBA[3]);

        gl.glTranslated(x,y,0);
        gl.glRotated(rot, 0,0,1);

        gl.glBegin(GL2.GL_POLYGON);
        for (int i = 0; i < 6; ++i) {
            gl.glVertex2d(rad * sch[i], rad * sch[6+i]);
        }
        gl.glEnd();

        gl.glRotated(-rot, 0,0,1);
        gl.glTranslated(-x,-y,0);
    }

    public static void computeStuff()
    {
        for(int i = 0; i < 6; i++) {
            sch[i] = Math.sin(i / 6.0 * 2 * Math.PI);
            sch[6+i] = Math.cos(i / 6.0 * 2 * Math.PI);
        }
    }
}
