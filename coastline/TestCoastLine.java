package coastline;

import tools.*;
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

		// Create coarse coast with noise
		Random rNum = new Random();
		double currBias = 0;
		double lastX = 0.1;
		double lastY = this.coarseCoastFunction(lastX);
		Point2D slope;
		Point2D normal;
		for (double x = 0; x <= 400; x += resolution)
		{
			currBias += (rNum.nextDouble() - 0.5) * 30;
			double y = this.coarseCoastFunction(x);

			slope = PointTools.getUnitVector(new Point2D.Double(lastX, lastY), new Point2D.Double(x, y));
			normal = new Point2D.Double(-1 * slope.getY() * currBias * resolution, slope.getX() * currBias * resolution);

			coastline.add(new Point2D.Double(x + normal.getX(), this.coarseCoastFunction(x) + normal.getY()));

			lastX = x;
			lastY = y;
		}

		this.setCoastline(coastline);

		// Add a some piers
		int pierStartIndex = (int)(200 / resolution);
		double pierWidth = 4;
		double pierLength = 10;
		this.addPier(resolution, pierStartIndex, pierWidth, pierLength);

		pierStartIndex = (int)(100 / resolution);
		this.addPier(resolution, pierStartIndex, pierWidth, pierLength);

		pierStartIndex = (int)(325 / resolution);
		this.addPier(resolution, pierStartIndex, pierWidth, pierLength);
		
	}

	private double coarseCoastFunction(double x)
	{
		return 3.0 * x / 4.0 + 35*Math.cos(x/35) + 35*Math.sin(x/70);
	}
}