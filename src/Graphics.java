import com.jogamp.opengl.GL2;

public class Graphics {

    public static GL2 gl = null;
    private static float red = 0;
    private static float blue = 0;
    private static float green = 0;
    
    public static void fillHex(float x, float y, float r) {
	gl = Eventlistener.gl;
	    
	gl.glColor3f(red,green,blue);
	gl.glBegin(GL2.GL_POLYGON);
	for(int i = 0; i < 6; ++i) {
	    gl.glVertex2d(r*Math.sin(i/6.0*2*Math.PI) + x,
	               r*Math.cos(i/6.0*2*Math.PI) + y);
	}
	gl.glEnd();
    }
    
    public static void setColor(float r, float b, float g) {
	red = r;
	blue = b;
	green = g;
    }
}
