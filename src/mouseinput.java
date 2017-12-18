import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;

public class mouseinput implements MouseListener{

    @Override
    public void mouseClicked(MouseEvent e) {
	System.out.println(e.getX() + " " + e.getY());
    }

    @Override
    public void mouseDragged(MouseEvent arg0) {
	
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
	
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
	
    }

    @Override
    public void mouseMoved(MouseEvent arg0) {
	
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
	
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
	
    }

    @Override
    public void mouseWheelMoved(MouseEvent e) {
	float r = renderer.unitsWide;
	if(e.getRotation()[1] > 0)
	{
	    renderer.unitsWide = Math.min(50,r + 5);
	}else
	{
	    renderer.unitsWide = Math.max(10,r - 5);
	}
    }

}
