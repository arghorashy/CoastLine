- I made a class called CoastLine.  You can import it into your own program and use it.
- This class basically contains an ArrayList of points which defines a coastline.
- The class can be constructed, point by point, with an ArrayList of points or with a 2D array of doubles (x, y).
- The function responsible for downsampling the coastline is "subsample(double accuracy)".  It downsamples the coastline and returns a new CoastLine object.
- This function only depends on one other function: "arePointsOutOfRange(Line2D line, int ptsStartInd, int ptsEndInd, double tolerance)"

I have also prepared a small demo that shows off what "subsample(double accuracy)" can do.  
