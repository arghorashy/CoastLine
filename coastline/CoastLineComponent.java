package coastline;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import javax.swing.JComponent;

/**************************************************************************
* Class: CoastLineCompnent
* A class used to draw a CoastLine.
**************************************************************************/

public class CoastLineComponent extends JComponent
{
    private CoastLine cl;
    private int width;
    private int height;

    public CoastLineComponent(CoastLine cl)
    {
        this(cl, 400, 400);
    }

    public CoastLineComponent(CoastLine cl, int width, int height)
    {
        super();
        this.cl = cl;
        this.width = width;
        this.height = height;
        this.setPreferredSize(new Dimension(width,height));
    }

    public void paintComponent(Graphics g) 
    {
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());
        Dimension d = getPreferredSize();
        g.setColor(Color.black);

        Graphics2D g2 = (Graphics2D) g;

        Point2D ptLast = null;
        for (Point2D pt : this.cl) 
        {
            if (ptLast != null)
            {
                g2.draw(new Line2D.Double(ptLast.getX(), ptLast.getY(), pt.getX(), pt.getY()));
            }

            ptLast = pt;
        }
    }
}