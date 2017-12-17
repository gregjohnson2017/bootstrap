import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

public class eventlistener implements GLEventListener{

    @Override
    public void display(GLAutoDrawable drawable) {
	GL2 gl = drawable.getGL().getGL2();
	
	gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
    
	gl.glColor3f(0,0,1);
	gl.glBegin(GL2.GL_QUADS);
	gl.glVertex2f(-1,-1);
	gl.glVertex2f(1,-1);
	gl.glVertex2f(1,1);
	gl.glVertex2f(-1,1);
	gl.glEnd();
	
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
	
    }

    @Override
    public void init(GLAutoDrawable drawable) {
	GL2 gl = drawable.getGL().getGL2();
	
	gl.glClearColor(0,0,0,1);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
	GL2 gl = drawable.getGL().getGL2();
	
	gl.glMatrixMode(GL2.GL_PROJECTION);
	gl.glLoadIdentity();
	
	float unitsTall = renderer.getWindowHeight()/ (renderer.getWindowWidth()/renderer.unitsWide);
	
	gl.glOrtho(-renderer.unitsWide/2,renderer.unitsWide/2,-unitsTall/2,unitsTall/2,-1,1);
	gl.glMatrixMode(GL2.GL_MODELVIEW);
    }
    

}
