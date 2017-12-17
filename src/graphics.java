import com.jogamp.opengl.GL2;

public class graphics {

    public static void fillHex(float x, float y, float r){
	GL2 gl = eventlistener.gl;
	    
	gl.glColor3f(1,1,1);
	gl.glBegin(GL2.GL_POLYGON);
	for(int i = 0; i < 6; ++i) {
	    gl.glVertex2d(r*Math.sin(i/6.0*2*Math.PI) + x,
	               r*Math.cos(i/6.0*2*Math.PI) + y);
	}
	gl.glEnd();
    }
}
