import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

public class Eventlistener implements GLEventListener{

    public static GL2 gl = null;
    
    @Override
    public void display(GLAutoDrawable drawable) {
	gl = drawable.getGL().getGL2();
	
	gl.glClearColor(0.3f, 0.3f, 0.3f, 1.0f );
	gl.glClear(gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT);
	
	gl.glMatrixMode(GL2.GL_PROJECTION);
	gl.glLoadIdentity();
	
	float unitsTall = Renderer.getWindowHeight()/ (Renderer.getWindowWidth()/Renderer.unitsWide);
	
	gl.glOrtho(-Renderer.unitsWide/2 + Renderer.centerOffX,Renderer.unitsWide/2 + Renderer.centerOffX,
		-unitsTall/2 + Renderer.centerOffY,unitsTall/2 + Renderer.centerOffY,-1,1);
	gl.glMatrixMode(GL2.GL_MODELVIEW);
	
	int a = 0;
	
	//fills in black squares for quadruple the size, except center where Christmas colored for now
	
	for(int i = -2*Game.maxGridX; i < 2*Game.maxGridX; i++)
	{
	    for(int j = -2*Game.maxGridY; j < 2*Game.maxGridY; j++)
	    {
		//parity for grid offset
		if(j%2 == 0)
		{
		    Graphics.setColor(1,0,0);
		    a = 1;
		}else
		{
		    Graphics.setColor(0,0,1);
		    a = 0;
		}
		//now set to black the others
		if(!(-Game.maxGridX/2 <= i && i < Game.maxGridX/2) || !(-Game.maxGridY/2 <= j && j < Game.maxGridY/2))
		{
		    Graphics.setColor(0,0,0);
		}
		
		Graphics.fillHex(2*i+a,(float) (1.9*j),1);
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
	
	float unitsTall = Renderer.getWindowHeight()/ (Renderer.getWindowWidth()/Renderer.unitsWide);
	
	gl.glOrtho(-Renderer.unitsWide/2,Renderer.unitsWide/2,-unitsTall/2,unitsTall/2,-1,1);
	gl.glMatrixMode(GL2.GL_MODELVIEW);
    }
    

}
