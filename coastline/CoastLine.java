package coastline;

import tools.*;
import java.awt.geom.Point2D;
import 	java.awt.geom.Line2D;
import java.util.*;
import java.awt.Point;

/**************************************************************************
* Class: CoastLine
* A class used to represent a CoastLine.  Its single attribute is a list
* points that make up the outline of a shoreline.  the class has numerous 
* funtions for drawing, smoothing, subsampling and modifying the object.
**************************************************************************/

public class CoastLine implements Iterable<Point2D>
{
	private List<Point2D> coastline;
	private static int defaultDrawHeight = 400;	// Default window size for drawing
	private static int defaultDrawWidth = 400;

	public CoastLine() 
	{
		this.coastline = new ArrayList<Point2D>(); 
	}

	public CoastLine(CoastLine cl)
	{
		this.coastline = cl.coastline;
	}

	public CoastLine(List<Point2D> cl)
	{
		this.coastline = cl;
	}

	public CoastLine(double[][] points)
	{
		this();
		if (points.length > 0 && points[0].length == 2)
		{
			for (double[] point : points)
			{
				this.coastline.add(new Point2D.Double(point[0], point[1]));
			}
		}
	}

	public int getNumberOfPoints()
	{
		return this.coastline.size();
	}

	public void add(Point2D pt)
	{
		this.coastline.add(pt);
	}

	public void setCoastline(List<Point2D> coastline)
	{
		this.coastline = coastline;
	}

	public Iterator<Point2D> iterator()
	{
		return this.coastline.iterator();
	}

	/**************************************************************************
	* Function: draw()
	* All the draw functions in this class pass a CoastLine object to a 
	* CoastLineComponent object that knows how to draw a CoastLine object.
	* The CoastLineComponent is displayed in a window by a 
	* CoastLineViewController.
	**************************************************************************/
	public static void draw(CoastLine cl)
	{
		CoastLineViewController clvc = new CoastLineViewController(
									   new CoastLineComponent(cl, CoastLine.defaultDrawWidth, 
									   	                          CoastLine.defaultDrawHeight));
		clvc.display();
	}

	public void draw()
	{
		CoastLine.draw(this);
	}

	/**************************************************************************
	* Function: draw(int centreIndex, double zoom)
	* This draw function makes it possible to choose an arbitrary point on the 
	* coastline to be the centre and to apply a zoom.
	* 
	* The centre point is specified by specifying the point's index in the 
	* coastline array (obviously this is not ideal).
	**************************************************************************/
	public void draw(int centreIndex, double zoom) throws IndexOutOfBoundsException
	{
		// Determine the coordinates of the specified centre point.
		double x = this.coastline.get(centreIndex).getX();
		double y = this.coastline.get(centreIndex).getY();

		// Determine the offset required to centre the centre point
		// after zoom is applied
		double plusX = CoastLine.defaultDrawWidth  / 2 - x * zoom;
		double plusY = CoastLine.defaultDrawHeight / 2 - y * zoom;

		// Recalculate point positions after zoom and offset
		List<Point2D> zoomedCL = new ArrayList<Point2D>();
		for (Point2D pt : this.coastline)
		{
			zoomedCL.add(new Point2D.Double(pt.getX() * zoom + plusX, pt.getY() * zoom + plusY));
		}

		// Draw recalculated CoastLine
		CoastLine.draw(new CoastLine(zoomedCL));
	}	

	/**************************************************************************
	* Function: addPier(double resolution, int pierStartIndex, 
	*                   double pierWidth, double pierLength)
	* This function adds piers (small rectangular protrusions) out of existing
	* coastlines. pierStartIndex specifies the start location of the pier 
	* via the index in the coastline array (again, not ideal).  Then the pier
	* length, width and the resolution of the pier can also be specified.
	**************************************************************************/
	public void addPier(double resolution, int pierStartIndex, 
		                double pierWidth, double pierLength)
	{
		int pierEndIndex = pierStartIndex + 1;

		// Remove all points along the width of the future pier.
		while (this.coastline.get(pierStartIndex)
			       .distance(this.coastline.get(pierEndIndex)) < pierWidth)
		{
			coastline.remove(pierEndIndex);
		}
		
		// Get a unit vector in the direction of the shore, where the pier
		// is to be inserted.
		Point2D parallelUnitVector = PointTools.getUnitVector(
						this.coastline.get(pierStartIndex), this.coastline.get(pierEndIndex));
		double deltaXParallel = parallelUnitVector.getX();
		double deltaYParallel = parallelUnitVector.getY();		

		double deltaXPerpendicular = deltaYParallel;
		double deltaYPerpendicular = -1 * deltaXParallel;

		int currentIndex = pierStartIndex + 1;


		// Insert points going out from the shore to make one side of the pier.
		currentIndex = this.addLine(currentIndex, deltaXPerpendicular, deltaYPerpendicular, 
												  pierLength, resolution);

		// Insert points parallel to the shore to form the end of the pier.
		currentIndex = this.addLine(currentIndex, deltaXParallel, deltaYParallel, 
												  pierWidth, resolution);

		// Insert points coming back towards the shore to form the last side of the pier.
		currentIndex = this.addLine(currentIndex, -1 * deltaXPerpendicular, 
			                                      -1 * deltaYPerpendicular, pierLength, resolution);
	}

	/**************************************************************************
	* Function: addLine(int startingIndex, double deltaX, double deltaY, 
	 									   double length, double resolution)
	* Insert a straight line into a coastline.
	* Note: (deltaX, deltaY) must be a unit vector indicating the direction 
	* of the line.
	**************************************************************************/
	private int addLine(int startingIndex, double deltaX, double deltaY, 
										   double length, double resolution)
	{
		int i = startingIndex;
		Point2D last;

		for ( ; i <= startingIndex + (int)(length / resolution); i++)
		{
			last = this.coastline.get(i-1);
			this.coastline.add(i, new Point2D.Double(last.getX() + deltaX * resolution, 
											         last.getY() + deltaY * resolution));
		}


		return i;
	}

	/**************************************************************************
	* Function: subsample(double accuracy)
	* This function removes shoreline details and reduces the number of points 
	* needed to accurately represent a shoreline.
	*
	* It does this by skipping points.  Once it has decided to include a 
	* point A, only includes point B as the next point if:
	* 	- all the points between point A and point B are within "accuracy"
	* 	  of the line between point A and point B (see function 
	*	  "arePointsInRange"), and if
	* 	- skipping point B and including the next point instead would cause 
	*     the first condition to be violated.
	*
	* The idea here is that the more points one tries to skip, the more liekly 
	* the approximate line will be far from the points that are being skipped.
	*
	* Therefore, to exclude the most number of points possible, it is
	* necessary to find the point (breaking point) where the first condition 
	* goes from being satisified to unsastisifed.
	*
	* To find this breaking point, an exponential search is used, then a 
	* binary search.
	**************************************************************************/
	public CoastLine subsample(double accuracy)
	{
		CoastLine newCL = new CoastLine();

		// Always take the first point
		newCL.add(this.coastline.get(0));

		// For each point selected, now choose the next point to be taken
		for (int i = 0; i < this.getNumberOfPoints() - 1; )
		{
			// min and max will determine the range for the later binary search
			// for now, use an exponential search to find the abovementioned
			// breaking point
			int count = 1;
			int max;
			int min;
			while(true)
			{
				// Note: Each min value is a previous max value
				max = i + (int)Math.pow(2, count);
				min = i + (int)Math.pow(2, count - 1);
				if (max >= this.getNumberOfPoints()) 
				{
					max = this.getNumberOfPoints() - 1;
					break;
				}

				Line2D line = new Line2D.Double(this.coastline.get(i), this.coastline.get(max));
				boolean outOfRange = this.arePointsOutOfRange(line, i + 1, max - 1, accuracy);

				// If the points between i and max are not within "accuracy" of the line between
				// i and max, we know that the breaking point is somewhere between min and max
				if (outOfRange) break;
				else count ++;
			}

			// If max is i + 2 and min is i + 1, and i + 2 is not admissible, then no
			// need for binary search
			if (count > 1)
			{
				// Now, use binary search to find the breaking point
				while (min < max)
				{
					int mid = (min + max) / 2;

					Line2D line = new Line2D.Double(this.coastline.get(i), this.coastline.get(mid));
					boolean outOfRange = this.arePointsOutOfRange(line, i + 1, mid - 1, accuracy);

					if (outOfRange) max = mid;
					else min = mid + 1;
				}
			}

			// Add the found point and use this point as the basis for looking
			// for the next point
			i = Math.min(min, max);
			newCL.add(this.coastline.get(i));
		}
		return newCL;

	}

	/**************************************************************************
	* Function: arePointsOutOfRange(Line2D line, int ptsStartInd, 
								 	int ptsEndInd, double tolerance)
	* Determines whether the points between ptsStartInd and ptsEndInd are within
	* "tolerance" of line.
	**************************************************************************/
	private boolean arePointsOutOfRange(Line2D line, int ptsStartInd, 
										int ptsEndInd, double tolerance)
	{
		for (int k = ptsStartInd; k <= ptsEndInd; k++)
		{
			if (line.ptLineDist(this.coastline.get(k)) > tolerance)
			{
				return true;
			}
		}	
		return false;
	}

	/**************************************************************************
	* Function: windowSmoothing(double radius)
	* This function takes a window radius and, for each point, recalculates its
	* position based on the average position of all the points within the window.
	*
	* This was the first idea, but it's slow, mildy effective and is not very
	* good at reducing the point count.
	**************************************************************************/
	public CoastLine windowSmoothing(double radius)
	{
		CoastLine newCL = new CoastLine();
		for (int i = 0; i < this.getNumberOfPoints(); i++)
		{
			int count = 1;
			double sumX = this.coastline.get(i).getX();
			double sumY = this.coastline.get(i).getY();

			int j = 1;
			while (i - j >= 0 && this.coastline.get(i).distance(this.coastline.get(i - j)) < radius)
			{
				sumX += this.coastline.get(i - j).getX();
				sumY += this.coastline.get(i - j).getY();
				count ++;
				j++;
			}

			j = 1;
			while (i + j < this.getNumberOfPoints() 
				&& this.coastline.get(i).distance(this.coastline.get(i + j)) < radius)
			{
				sumX += this.coastline.get(i + j).getX();
				sumY += this.coastline.get(i + j).getY();
				count ++;
				j++;
			}

			System.out.println(sumX / count);
			newCL.add(new Point2D.Double(sumX / count, sumY / count));
		}
		return newCL;
	}	
}


