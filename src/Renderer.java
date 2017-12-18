import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;

public class Renderer {

    
    private static GLWindow window = null;
    
    public static int screenW = 640;
    public static int screenH = 360;
    
    public static float unitsWide = 50;
    
    public static float centerOffX = 0;
    public static float centerOffY = 0;
    
    public static void init(){
	GLProfile.initSingleton();
	GLProfile profile = GLProfile.get(GLProfile.GL2);
	GLCapabilities caps = new GLCapabilities(profile);
	
	window = GLWindow.create(caps);
	window.setSize(screenW,screenH);
	window.setTitle("Bootstrap");
	//window.setResizable(false);
	window.addGLEventListener(new Eventlistener());
	window.addMouseListener(new Mouseinput());
	window.requestFocus();
	
	window.addWindowListener(new WindowAdapter() {
		@Override
		public void windowDestroyed(WindowEvent e) {
			System.exit(0);
		}
		
		@Override
		public void windowResized(WindowEvent e) {
		    Mouseinput.getBounds();
		}
	});
	
	FPSAnimator animator = new FPSAnimator(window,60);
	animator.start();
	
	window.setVisible(true);
    }
    
    public static int getWindowWidth(){
	return window.getWidth();
    }

    public static int getWindowHeight(){
	return window.getHeight();
    }
}