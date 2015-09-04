package coastline;

import tools.*;
import java.lang.Math;
import java.util.*;
import java.awt.geom.Point2D;
import java.awt.Point;


/**************************************************************************
* Class: TestCoastLine
* Subclass of CoastLine used to quickly mock test cases.
* The idea is that a function is used to generate the overall shape of the
* coastline.  The noise/detail is added on by a adding the running sum
* of a random number generator to each point.  Piers are added afterwards
* to simulate medium level detail.
**************************************************************************/

public class TestCoastLine extends CoastLine implements Iterable<Point2D> 
{


	public TestCoastLine(double resolution, double pierResolution)
	{
		List<Point2D> coastline = new ArrayList<Point2D>();
	
		Random rNum = new Random();

		// Create coarse coast with noise (detail)
		double currBias = 0;	// accumulated bias (used to create noise (detail))
		double lastX = 0.1;		// last x and y values of coastline shape function
		double lastY = this.coarseCoastFunction(lastX);
		Point2D slope;			// slope of coastline shape function
		Point2D normal;			// normal of coastline shape function


		for (double x = 0; x <= 400; x += resolution)
		{
			currBias += (rNum.nextDouble() - 0.5) * 30;	// update bias
			double y = this.coarseCoastFunction(x);		// get y value

			// calculate normal unit vector
			slope = PointTools.getUnitVector(new Point2D.Double(lastX, lastY), new Point2D.Double(x, y));
			normal = new Point2D.Double(-1 * slope.getY() * currBias * resolution, slope.getX() * currBias * resolution);

			// add x,y to coastline with currBias
			coastline.add(new Point2D.Double(x + normal.getX(), this.coarseCoastFunction(x) + normal.getY()));

			lastX = x;
			lastY = y;
		}


		this.setCoastline(coastline);

		// Add a some piers
		int pierStartIndex = (int)(200 / resolution);
		double pierWidth = 4;
		double pierLength = 10;
		this.addPier(pierResolution, pierStartIndex, pierWidth, pierLength);

		pierStartIndex = (int)(100 / resolution);
		this.addPier(pierResolution, pierStartIndex, pierWidth, pierLength);

		pierStartIndex = (int)(325 / resolution);
		this.addPier(pierResolution, pierStartIndex, pierWidth, pierLength);

		
	}

	private double coarseCoastFunction(double x)
	{
		return 3.0 * x / 4.0 + 35*Math.cos(x/35) + 35*Math.sin(x/70);
	}
}