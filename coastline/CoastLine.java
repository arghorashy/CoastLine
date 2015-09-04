package coastline;

import java.awt.geom.Point2D;
import java.util.*;
import java.awt.Point;

public class CoastLine implements Iterable<Point2D>
{
	private List<Point2D> coastline;

	public int getNumberOfPoints()
	{
		return this.coastline.size();
	}

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

	public void addPier(double resolution, int pierStartIndex, double pierWidth, double pierLength)
	{
		int pierEndIndex = pierStartIndex + 1;

		while (this.coastline.get(pierStartIndex).distance(this.coastline.get(pierEndIndex)) < pierWidth)
		{
			coastline.remove(pierEndIndex);
		}
		
		double deltaXParallel = this.coastline.get(pierEndIndex).getX() - this.coastline.get(pierStartIndex).getX();
		double deltaYParallel = this.coastline.get(pierEndIndex).getY() - this.coastline.get(pierStartIndex).getY();		
		double truePierWidth = Math.sqrt(Math.pow(deltaXParallel, 2) + Math.pow(deltaYParallel, 2));
		deltaXParallel /= truePierWidth;
		deltaYParallel /= truePierWidth;
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
			System.out.print(i);
			System.out.print("   ");
			System.out.println(startingIndex + (int)(length / resolution));
			last = this.coastline.get(i-1);
			this.coastline.add(i, new Point2D.Double(last.getX() + deltaX * resolution, last.getY() + deltaY * resolution));
		}

		return i;
	}


}


