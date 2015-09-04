package coastline;

import tools.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.awt.Point;

public class CoastLine implements Iterable<Point2D>
{
	private List<Point2D> coastline;
	private static int defaultDrawHeight = 400;
	private static int defaultDrawWidth = 400;

	public CoastLine() 
	{
		this.coastline = new ArrayList<Point2D>(); 
	}

	public CoastLine(List<Point2D> cl)
	{
		this.coastline = cl;
	}

	public int getNumberOfPoints()
	{
		return this.coastline.size();
	}

	public void add(Point2D pt)
	{
		this.coastline.add(pt);
	}

	public void draw()
	{
		CoastLine.draw(this);
	}

	public void draw(int centreIndex, double zoom)
	{
		double x = this.coastline.get(centreIndex).getX();
		double y = this.coastline.get(centreIndex).getY();

		System.out.print(x);
		System.out.print("   ");
		System.out.println(y);

		double plusX = CoastLine.defaultDrawWidth  / 2 - x * zoom;
		double plusY = CoastLine.defaultDrawHeight / 2 - y * zoom;

		List<Point2D> zoomedCL = new ArrayList<Point2D>();
		for (Point2D pt : this.coastline)
		{
			zoomedCL.add(new Point2D.Double(pt.getX() * zoom + plusX, pt.getY() * zoom + plusY));
		}

		CoastLine.draw(new CoastLine(zoomedCL));
	}	

	public static void draw(CoastLine cl)
	{
		CoastLineViewController clvc = new CoastLineViewController(
									   new CoastLineComponent(cl, CoastLine.defaultDrawWidth, CoastLine.defaultDrawHeight));
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

	public void addPier(double resolution, int pierStartIndex, double pierWidth, double pierLength)
	{
		int pierEndIndex = pierStartIndex + 1;

		while (this.coastline.get(pierStartIndex).distance(this.coastline.get(pierEndIndex)) < pierWidth)
		{
			coastline.remove(pierEndIndex);
		}
		
		Point2D parallelUnitVector = PointTools.getUnitVector(this.coastline.get(pierStartIndex), this.coastline.get(pierEndIndex));
		double deltaXParallel = parallelUnitVector.getX();
		double deltaYParallel = parallelUnitVector.getY();		

		double deltaXPerpendicular = deltaYParallel;
		double deltaYPerpendicular = -1 * deltaXParallel;

		// Go out from the shore
		int currentIndex = pierStartIndex + 1;
		currentIndex = this.addLine(currentIndex, deltaXPerpendicular, deltaYPerpendicular, pierLength, resolution);
		currentIndex = this.addLine(currentIndex, deltaXParallel, deltaYParallel, pierWidth, resolution);
		currentIndex = this.addLine(currentIndex, -1 * deltaXPerpendicular, -1 * deltaYPerpendicular, pierLength, resolution);
	}

	// deltaX, deltaY should be a unit vector
	// returns index at end of line
	private int addLine(int startingIndex, double deltaX, double deltaY, double length, double resolution)
	{
		int i = startingIndex;
		Point2D last;
		for ( ; i < startingIndex + (int)(length / resolution); i++)
		{

			last = this.coastline.get(i-1);
			this.coastline.add(i, new Point2D.Double(last.getX() + deltaX * resolution, last.getY() + deltaY * resolution));
		}

		return i;
	}

	public static CoastLine windowSmoothing(CoastLine cl, double radius)
	{
		CoastLine newCL = new CoastLine();
		for (int i = 0; i < cl.getNumberOfPoints(); i++)
		{
			int count = 1;
			double sumX = cl.coastline.get(i).getX();
			double sumY = cl.coastline.get(i).getY();

			int j = 1;
			while (i - j >= 0 && cl.coastline.get(i).distance(cl.coastline.get(i - j)) < radius)
			{
				sumX += cl.coastline.get(i - j).getX();
				sumY += cl.coastline.get(i - j).getY();
				count ++;
				j++;
			}

			j = 1;
			while (i + j < cl.getNumberOfPoints() && cl.coastline.get(i).distance(cl.coastline.get(i + j)) < radius)
			{
				sumX += cl.coastline.get(i + j).getX();
				sumY += cl.coastline.get(i + j).getY();
				count ++;
				j++;
			}

			System.out.println(sumX / count);
			newCL.add(new Point2D.Double(sumX / count, sumY / count));
		}

		return newCL;

	}


}


