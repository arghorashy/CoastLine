

import coastline.CoastLine;
import coastline.TestCoastLine;
import java.util.*;

public class CoastLineProcessor
{
	public static void main(String args[])
	{
		//demo();
		inputProcessor();

	}

	public static void demo()
	{
		TestCoastLine cl = new TestCoastLine();
		cl.draw(100 * 1000, 10);
		//CoastLine newCL = CoastLine.windowSmoothing(cl, 0.75);
		CoastLine newCL = CoastLine.subsample(cl, 0.5);
		newCL.draw();
	}

	public static void inputProcessor()
	{
		Map<String, CoastLine> mem = new HashMap<String, CoastLine>();
		mem.put("init", new TestCoastLine());
		Scanner reader = new Scanner(System.in);

		while (true)
		{
			System.out.println("\nSelect a command: cp, function, draw, show_mem, exit:");

			String cmd = reader.next();
			if (cmd.equals("cp"))
			{
				System.out.println("\nChoose a coastline to copy:\n" + listMemory(mem));
				String clName = reader.next();

				if (! mem.containsKey(clName))
				{
					System.out.println("\nThat name is not defined!");
					continue;
				}

				System.out.println("\nChoose a destination name:");
				String newName = reader.next();

				mem.put(newName, new CoastLine(mem.get(clName)));
			}
			else if (cmd.equals("function"))
			{
				System.out.println("\nChoose a coastline to process:\n" + listMemory(mem));
				String clName = reader.next();

				if (! mem.containsKey(clName))
				{
					System.out.println("\nThat name is not defined!");
					continue;
				}

				System.out.println("\nChoose a function:\n" 
						+ "\tsubsample\n"
						+ "\twindow\n");

				String function = reader.next();

				if (function.equals("subsample"))
				{
					System.out.println("\nHow much accuracy? (Can be decimal.  Smaller means less subsampling.):");

					if (! reader.hasNextDouble())
					{
						System.out.println("\nThat's not a valid decimal number!");
						continue;	
					}

					double accuracy = reader.nextDouble();

					if (accuracy < 0)
					{
						System.out.println("\nAccuracy must be positive.");
						continue;		
					}

					mem.put(clName, CoastLine.subsample(mem.get(clName), accuracy));
				}
				else if (function.equals("window"))
				{
					System.out.println("\nWindow radius? (Can be decimal.  Smaller is less smoothing):");

					if (! reader.hasNextDouble())
					{
						System.out.println("\nThat's not a valid decimal number!");
						continue;	
					}

					double radius = reader.nextDouble();

					if (radius < 0)
					{
						System.out.println("\nRadius must be positive.");
						continue;		
					}

					mem.put(clName, CoastLine.windowSmoothing(mem.get(clName), radius));

				}

			}
			else if (cmd.equals("draw"))
			{
				System.out.println("\nChoose a coastline to draw:\n" + listMemory(mem));
				String clName = reader.next();
				
				if (! mem.containsKey(clName))
				{
					System.out.println("\nThat name is not defined!");
					continue;
				}

				System.out.println("\nZoom? (y/n):");
				String yn = reader.next();

				if (yn.equals("n"))
				{
					mem.get(clName).draw();
					continue;
				}
				else if (! yn.equals("y"))
				{
					System.out.println("\nThat's not a valid response!");
					continue;	
				}

				System.out.println("\nHow much zoom? (Can be a decimal):");

				if (! reader.hasNextDouble())
				{
					System.out.println("\nThat's not a valid decimal number!");
					continue;	
				}

				double zoom = reader.nextDouble();

				if (zoom < 0)
				{
					System.out.println("\nZoom must be positive.");
					continue;		
				}

				int maxIndex = mem.get(clName).getNumberOfPoints();
				System.out.println("\nWhere should be drawing be centred? (Must be int between 0 and " + maxIndex + ".):");

				if (! reader.hasNextInt())
				{
					System.out.println("\nThat's not a valid integer!");
					continue;	
				}

				int centre = reader.nextInt();

				if (centre < 0 || centre > maxIndex)
				{
					System.out.println("\nSpecified centre out of range.");
					continue;		
				}

				mem.get(clName).draw(centre, zoom);

			}
			else if (cmd.equals("show_mem"))
			{
				System.out.println("\nNames used in memory:\n" + listMemory(mem));
			}
			else if (cmd.equals("exit"))
			{
				System.exit(0);
			}
			else
			{
				System.out.println("\nInvalid command: Try again!");
			}
		}
	}


	public static String listMemory(Map<String, CoastLine> map)
	{
		StringBuilder sb = new StringBuilder();

		for (String key : map.keySet())
		{
			sb.append("\t" + key + "\n");
		}

		return sb.toString();
	}
}