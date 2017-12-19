import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;

public class Mouseinput implements MouseListener {

    private int x = 0;
    private int y = 0;

    @Override
    public void mouseClicked(MouseEvent e) {

	// left click to select cells :P
	if (e.getButton() == 1) {
	    // selects given cell, if possible
	    // first finds what unit we are in
	    float unitsWide = Renderer.unitsWide;
	    float unitsTall = Renderer.getUnitsTall(Renderer.unitsWide);
	    // computes which unit we are in based on position of mouse relative
	    // to screen size
	    // also adjusts for offset
	    // "from center" is from true center

	    float clickUnitXfromCenter = unitsWide * ((float) e.getX() / Renderer.getWindowWidth() - 0.5f)
		    + Renderer.centerOffX;
	    float clickUnitYfromCenter = unitsTall * ((float) e.getY() / Renderer.getWindowHeight() - 0.5f)
		    - Renderer.centerOffY;

	    // now figures out which hexagon that is, shifts a bit
	    // "parity" determined by Y

	    float a = 0;
	    // must flip the Y (around -1...it works...)
	    // this is just because the way the hexagons are drawn vs "indexed"
	    int clickHexY = -1 - Math.round((clickUnitYfromCenter - 1.9f) / 1.9f);
	    if (clickHexY % 2 != 0) {
		// must shift
		a = 1f;
	    }
	    int clickHexX = Math.round((clickUnitXfromCenter - 1f + a) / 2);

	    // checks to see if in bounds
	    if (-Game.maxGridX / 2 <= clickHexX && clickHexX < (float) Game.maxGridX / 2
		    && -Game.maxGridY / 2 <= clickHexY && clickHexY < (float) Game.maxGridY / 2) {
		// selects this cell
		Game.selectCell(clickHexX, clickHexY);
	    } else {
		// deselects
		Game.deselect();
	    }
	}
    }

    @Override
    public void mouseDragged(MouseEvent e) {

	if (e.getButton() == 3) {
	    // holding down right click to drag screen
	    // speed is a function of what the zoom is at
	    float speed = Renderer.unitsWide / 1375;
	    // check bounds
	    float newOffX = Renderer.centerOffX + speed * (x - e.getX());
	    float newOffY = Renderer.centerOffY + speed * (e.getY() - y);
	    // checks to see if in bounds, or at least the change is in the
	    // right direction

	    if (Math.abs(newOffX) < Renderer.maxOffX
		    || (Renderer.maxOffX - newOffX) * Math.signum((speed * (x - e.getX()))) > 0) {
		// in bounds OR travelling back towards bounds
		Renderer.centerOffX = newOffX;
	    }
	    if (Math.abs(newOffY) < Renderer.maxOffY
		    || (Renderer.maxOffY - newOffY) * Math.signum((speed * (y - e.getY()))) < 0) {
		// in bounds OR travelling back towards bounds
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
	// update coordinates
	x = e.getX();
	y = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {

    }

    @Override
    public void mouseWheelMoved(MouseEvent e) {
	float r = Renderer.unitsWide;
	if (e.getRotation()[1] > 0) {
	    Renderer.unitsWide = Math.min(Renderer.maxUnitsWide, r + 5);
	} else {
	    Renderer.unitsWide = Math.max(Renderer.minUnitsWide, r - 5);
	}
	// updates bounds too
	getBounds();
    }

    public static void getBounds() {

	float unitsWide = Renderer.unitsWide;
	float unitsTall = Renderer.getUnitsTall(Renderer.unitsWide);

	// updates bounds based on screen size
	// scale by 2 for size of hexagon in X, 1.9 for size of hexagon in Y
	// plus slight fudge to see edges

	Renderer.maxOffX = Math.max(0, (float) (Game.maxGridX - unitsWide / 2) + 2);
	Renderer.maxOffY = (float) Math.max(0, (float) (Game.maxGridY - unitsTall / 1.9) + 2);
    }

}
