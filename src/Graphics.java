import com.jogamp.opengl.GL2;

public class Graphics {
    public static GL2 gl = null;

    private static float[] sch = new float[12]; // sch = sin cos hex coords (only need to compute once)

    public static void fillHex(float x, float y, float rad, float rot, float[] RGB) {
        gl = Eventlistener.gl;

        gl.glColor3f(RGB[0], RGB[1], RGB[2]);

        gl.glTranslatef(x,y,0);
        gl.glRotatef(rot, 0,0,1);

        gl.glBegin(GL2.GL_POLYGON);
        for (int i = 0; i < 6; ++i) {
            gl.glVertex2d(rad * sch[i], rad * sch[6+i]);
        }
        gl.glEnd();

        gl.glRotatef(-rot, 0,0,1);
        gl.glTranslatef(-x,-y,0);
    }

    public static void computeStuff()
    {
        for(int i = 0; i < 6; i++) {
            sch[i] = (float) Math.sin(i / 6.0 * 2 * Math.PI);
            sch[6+i] = (float) Math.cos(i / 6.0 * 2 * Math.PI);
        }
    }
}
