package coastline;

import java.lang.Math;
import java.util.*;
import java.awt.geom.Point2D;
import java.awt.Point;


public class TestCoastLine extends CoastLine implements Iterable<Point2D> 
{
	public TestCoastLine()
	{
		List<Point2D> coastline = new ArrayList<Point2D>();

		// Create coarse coast
		for (double x = 0; x <= 350; x += 0.001)
		{
			coastline.add(new Point2D.Double(x, this.coarseCoastFunction(x)));
		}

		// Add a pier at x = 5
		//Point derivative = new Point();

		// Add a protrusion

		this.setCoastline(coastline);
	}

	private double coarseCoastFunction(double x)
	{
		return 3.0 * x / 4.0 + 35*Math.cos(x/35) + 35*Math.sin(x/70);
	}
}