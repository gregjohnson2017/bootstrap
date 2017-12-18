import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;

public class mouseinput implements MouseListener{

    private int x = 0;
    private int y = 0;
    
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
	    float speed = renderer.unitsWide/1375;
	    renderer.centerOffX += speed*(x - e.getX());
	    renderer.centerOffY += speed*(e.getY() - y);	    
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
	float r = renderer.unitsWide;
	if(e.getRotation()[1] > 0)
	{
	    renderer.unitsWide = Math.min(100,r + 5);
	}else
	{
	    renderer.unitsWide = Math.max(10,r - 5);
	}
    }

}
