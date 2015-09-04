package tools;

import java.awt.geom.Point2D;

public class PointTools
{

	/**************************************************************************
	* Function: getUnitVector(Point2D p1, Point2D p2)
	* Takes 2 points and return a sinle point representing a vector pointing 
	* from p1 to p2 with a length of 1.
	*
	**************************************************************************/
	public static Point2D getUnitVector(Point2D p1, Point2D p2)
	{
		double deltaX = p2.getX() - p1.getX();
		double deltaY = p2.getY() - p1.getY();		
		double length = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
		deltaX /= length;
		deltaY /= length;

		return new Point2D.Double(deltaX, deltaY);
	}
}
