package coastline;

import java.awt.geom.Point2D;
import java.util.*;

public class CoastLine implements Iterable<Point2D>
{
	private List<Point2D> coastline;





	public void draw()
	{
		CoastLineViewController clvc = new CoastLineViewController(new CoastLineComponent(this));
		clvc.display();

	}


	public Iterator<Point2D> iterator()
	{
		return this.coastline.iterator();
	}

	public void setCoastline(List<Point2D> coastline)
	{
		this.coastline = coastline;
	}


}


