import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;

public class Mouseinput implements MouseListener{

    private int x = 0;
    private int y = 0;
    
    private static float maxOffX = 200;
    private static float maxOffY = 200;
    
    
    @Override
    public void mouseClicked(MouseEvent e) {
	System.out.println(e.getX() + " " + e.getY());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
	
	if(e.getButton() == 3)
	{
	    //holding down right click to drag screen
	    //speed is a function of what the zoom is at
	    float speed = Renderer.unitsWide/1375;
	    //check bounds
	    float newOffX = Renderer.centerOffX + speed*(x - e.getX());
	    float newOffY =  Renderer.centerOffY + speed*(e.getY() - y);
	    //checks to see if in bounds, or at least the change is in the right direction
	    
	    if(Math.abs(newOffX) < maxOffX || (maxOffX - newOffX)*Math.signum((speed*(x - e.getX()))) > 0) {
		//in bounds OR travelling back towards bounds
		Renderer.centerOffX = newOffX;
	    }	
	    if(Math.abs(newOffY) < maxOffY || (maxOffY - newOffY)*Math.signum((speed*(y - e.getY()))) < 0) {
		//in bounds OR travelling back towards bounds
		Renderer.centerOffY = newOffY;
	    }
	    
	    
	}
	x = e.getX();
	y = e.getY();
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
	
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
	
    }

    @Override
    public void mouseMoved(MouseEvent e) {
	
    }

    @Override
    public void mousePressed(MouseEvent e) {
	//update coordinates
	x = e.getX();
	y = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
	
    }

    @Override
    public void mouseWheelMoved(MouseEvent e) {
	float r = Renderer.unitsWide;
	if(e.getRotation()[1] > 0)
	{
	    Renderer.unitsWide = Math.min(100,r + 5);
	}else
	{
	    Renderer.unitsWide = Math.max(10,r - 5);
	}
	//updates bounds too
	getBounds();
    }
    
    public static void getBounds()
    {
	
	float unitsWide = Renderer.unitsWide;
	float unitsTall = Renderer.getWindowHeight() / (Renderer.getWindowWidth()/Renderer.unitsWide);

	//updates bounds based on screen size
	//scale by 2 for size of hexagon in X, 1.9 for size of hexagon in Y
	//plus slight fudge to see edges
	
	maxOffX = Math.max(0,(float) (Game.maxGridX - unitsWide/2) + 2);
	maxOffY = (float) Math.max(0,(float) (Game.maxGridY - unitsTall/1.9) + 2);
    }

}
