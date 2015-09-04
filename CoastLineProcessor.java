

import coastline.CoastLine;
import coastline.TestCoastLine;

public class CoastLineProcessor
{
	public static void main(String args[])
	{
		TestCoastLine cl = new TestCoastLine();
		cl.draw(100 * 1000, 10);
		CoastLine newCL = CoastLine.windowSmoothing(cl, 0.75);
		newCL.draw(100 * 1000, 10);


	}
}