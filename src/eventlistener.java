import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

public class eventlistener implements GLEventListener{

    public static GL2 gl = null;
    
    @Override
    public void display(GLAutoDrawable drawable) {
	gl = drawable.getGL().getGL2();
	
	gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f );
	gl.glClear(gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT);
	
	gl.glMatrixMode(GL2.GL_PROJECTION);
	gl.glLoadIdentity();
	
	float unitsTall = renderer.getWindowHeight()/ (renderer.getWindowWidth()/renderer.unitsWide);
	
	gl.glOrtho(-renderer.unitsWide/2,renderer.unitsWide/2,-unitsTall/2,unitsTall/2,-1,1);
	gl.glMatrixMode(GL2.GL_MODELVIEW);
	
	int a = 0;
	for(int i = -100; i < 100; i++)
	{
	    for(int j = -100; j < 100; j++)
	    {
		if(j%2 == 0)
		{
		    graphics.setColor(1,0,0);
		    a = 1;
		}else
		{
		    graphics.setColor(0,1,0);
		    a = 0;
		}
		graphics.fillHex(2*i+a,(float) (1.9*j),1);
	    }
	}
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
