

import coastline.CoastLine;
import coastline.TestCoastLine;
import java.util.*;

public class CoastLineProcessor
{
	public static void main(String args[])
	{
		// User chooses between a demo and prompt
		Scanner reader = new Scanner(System.in);
		String cmd;
		while(true)
		{
			System.out.println("Choose: \"demo\" or \"prompt\"");
			cmd = reader.next();

			if (cmd.equals("demo")) demo();
			else if (cmd.equals("prompt")) prompt();
			else System.out.println("Invalid command!");
		}
	}

	public static void demo()
	{
		int num;
		TestCoastLine cl = new TestCoastLine(0.001, 0.001);

		System.out.println();

		System.out.println("This is a demo of the coastline downsampling functionality.");
		System.out.println("Throughout the demo, you will be prompted to press a key to");
		System.out.println("move the demo forward.  You may interrupt the demo at any");
		System.out.println("time by pressing ctrl+C.\n");
		pressAnyKeyToContinue();

		cl.draw();
		num = cl.getNumberOfPoints();
		System.out.println("Here is a detailed test coastline.  It is made up of ");
		System.out.println(num + " points. Notice the piers along the coastline.\n");
		pressAnyKeyToContinue();

		cl.draw(220000, 50);

		System.out.println("If we zoom in near one of the piers, it is clear that there");
		System.out.println("is a high level of detail along the shoreline.\n");
		pressAnyKeyToContinue();

		System.out.println("If we just want to display a zoomed out version of this shoreline,");
		System.out.println("it is not necessary to plot all " + num + " of the points.\n");
		pressAnyKeyToContinue();

		CoastLine newCL = cl.subsample(0.1);
		num = newCL.getNumberOfPoints();
		newCL.draw();
		System.out.println("By subsampling the shoreline, the number of points can be reduced as");
		System.out.println("much as 99%.  This plot only has " + num + " points with little effect on the");
		System.out.println("zoomed out profile of the shoreline.\n");
		pressAnyKeyToContinue();
		
		newCL.draw(760, 50);
		System.out.println("Only when zoomed in does the difference become more apparent.\n");
		pressAnyKeyToContinue();

		CoastLine new2CL = cl.subsample(0.1);
		num = new2CL.getNumberOfPoints();
		new2CL.draw();
		System.out.println("Further savings can be had by reapplying the algorithm. This plot only has");
		System.out.println(num + " points.\n");
		pressAnyKeyToContinue();

	}

	public static void prompt()
	{


		Map<String, CoastLine> mem = new HashMap<String, CoastLine>();
		mem.put("init", new TestCoastLine(0.001, 0.001));
		Scanner reader = new Scanner(System.in);

		showPromptHelp();

		while (true)
		{
			System.out.println("Select a command: cp, function, draw, show_mem, exit:");

			String cmd = reader.next();
			if (cmd.equals("cp"))
			{
				System.out.println("Choose a coastline to copy:\n" + listMemory(mem));
				String clName = reader.next();

				if (! mem.containsKey(clName))
				{
					System.out.println("That name is not defined!");
					continue;
				}

				System.out.println("Choose a destination name:");
				String newName = reader.next();

				mem.put(newName, new CoastLine(mem.get(clName)));
			}
			else if (cmd.equals("function"))
			{
				System.out.println("Choose a coastline to process:\n" + listMemory(mem));
				String clName = reader.next();

				if (! mem.containsKey(clName))
				{
					System.out.println("That name is not defined!");
					continue;
				}

				System.out.println("Choose a function:\n" 
						+ "\tsubsample\n"
						+ "\twindow\n");

				String function = reader.next();

				if (function.equals("subsample"))
				{
					System.out.println("How much accuracy? (Can be decimal.  Smaller means less subsampling.):");

					if (! reader.hasNextDouble())
					{
						System.out.println("That's not a valid decimal number!");
						continue;	
					}

					double accuracy = reader.nextDouble();

					if (accuracy < 0)
					{
						System.out.println("Accuracy must be positive.");
						continue;		
					}

					mem.put(clName, mem.get(clName).subsample(accuracy));
				}
				else if (function.equals("window"))
				{
					System.out.println("Window radius? (Can be decimal.  Smaller is less smoothing):");

					if (! reader.hasNextDouble())
					{
						System.out.println("That's not a valid decimal number!");
						continue;	
					}

					double radius = reader.nextDouble();

					if (radius < 0)
					{
						System.out.println("Radius must be positive.");
						continue;		
					}

					mem.put(clName, mem.get(clName).windowSmoothing(radius));

				}

			}
			else if (cmd.equals("draw"))
			{
				System.out.println("Choose a coastline to draw:\n" + listMemory(mem));
				String clName = reader.next();
				
				if (! mem.containsKey(clName))
				{
					System.out.println("That name is not defined!");
					continue;
				}

				System.out.println("Zoom? (y/n):");
				String yn = reader.next();

				if (yn.equals("n"))
				{
					mem.get(clName).draw();
					continue;
				}
				else if (! yn.equals("y"))
				{
					System.out.println("That's not a valid response!");
					continue;	
				}

				System.out.println("How much zoom? (Can be a decimal):");

				if (! reader.hasNextDouble())
				{
					System.out.println("That's not a valid decimal number!");
					continue;	
				}

				double zoom = reader.nextDouble();

				if (zoom < 0)
				{
					System.out.println("Zoom must be positive.");
					continue;		
				}

				int maxIndex = mem.get(clName).getNumberOfPoints();
				System.out.println("Where should be drawing be centred? (Must be int between 0 and " + maxIndex + ".):");

				if (! reader.hasNextInt())
				{
					System.out.println("That's not a valid integer!");
					continue;	
				}

				int centre = reader.nextInt();

				if (centre < 0 || centre > maxIndex)
				{
					System.out.println("Specified centre out of range.");
					continue;		
				}

				mem.get(clName).draw(centre, zoom);

			}
			else if (cmd.equals("show_mem"))
			{
				System.out.println("Names used in memory:\n" + listMemory(mem));
			}
			else if (cmd.equals("exit"))
			{
				System.exit(0);
			}
			else
			{
				System.out.println("Invalid command: Try again!");
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

	private static void showPromptHelp()
	{
		System.out.println("");
		System.out.println("Command Descriptions");
		System.out.println("--------------------");
		System.out.println("cp 		 - A coastline from one name in memory to another.");
		System.out.println("function - Process a coastline in memory in-place.");
		System.out.println("draw 	 - Draw a coastline in memory.");
		System.out.println("show_mem - Show the coastlines currently in memory.");
		System.out.println("           Always starts with \"init\" defined.");
		System.out.println("exit 	 - Quit the program.");
		System.out.println("");
	}

	private static void pressAnyKeyToContinue()
	{ 
        System.out.println("Press any key to continue...");
        try
        {
            System.in.read();
        }  
        catch(Exception e) {}  
	}
}