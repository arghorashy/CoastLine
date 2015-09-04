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

		double resolution = 0.001;

		// Create coarse coast
		for (double x = 0; x <= 400; x += resolution)
		{
			coastline.add(new Point2D.Double(x, this.coarseCoastFunction(x)));
		}

		this.setCoastline(coastline);

		// Add a pier at x = 200
		int pierStartIndex = (int)(200 / resolution);
		double pierWidth = 4;
		double pierLength = 10;
		this.addPier(resolution, pierStartIndex, pierWidth, pierLength);

		pierStartIndex = (int)(100 / resolution);
		this.addPier(resolution, pierStartIndex, pierWidth, pierLength);

		pierStartIndex = (int)(325 / resolution);
		this.addPier(resolution, pierStartIndex, pierWidth, pierLength);

									// The pier will be 25 wide
		//Point derivative = new Point();

		// Add a protrusion

		
	}

	private double coarseCoastFunction(double x)
	{
		return 3.0 * x / 4.0 + 35*Math.cos(x/35) + 35*Math.sin(x/70);
	}
}