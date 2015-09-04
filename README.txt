Hello Pasha,

As you'll soon be able to tell, I really enjoyed working on this problem.  As we discussed, I started off writing a function 
to downsample a detailed shoreline, so that it can be rendered more easily.  However, once I finished, I wasn't 
satisfied as I wanted to see how it worked.  As a result, I ended up writing a bunch of other functions / classes so that
I could understand / debug / visualise what my program was doing.

Obviously, I don't want you to have to dig around all the code though, so here is all you really need to know:

- I made a class called CoastLine.  You can import it into your own program and use it.
- This class basically contains an ArrayList of points which defines a coastline (much like we discussed).
- The class can be constructed, point by point, with an ArrayList of points or with a 2D array of doubles (x, y) (as you mentioned).
- The function responsible for downsampling the coastline is "subsample(double accuracy)".  It downsamples the coastline and returns a new CoastLine object.  This is the function you'll be most interested in.
- This function only depends on one other function: "arePointsOutOfRange(Line2D line, int ptsStartInd, int ptsEndInd, double tolerance)"

I have also prepared a small demo that shows off what "subsample(double accuracy)" can do.  If you are interested, you can go through it in 2 minutes by running the main program ("CoastLineProcessing.java") and by selecting "demo".

Of course, if you have any questions please do not hesitate to let me know!

Regards,
Afsheen