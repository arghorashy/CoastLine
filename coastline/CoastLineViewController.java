package coastline;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

class CoastLineViewController implements Runnable
{
    private CoastLineComponent clc;

    CoastLineViewController(CoastLineComponent clc)
    {
        this.clc = clc;
    }

    public void run()
    {
        JOptionPane.showMessageDialog(null, this.clc);
    }

    public void display()
    {
        SwingUtilities.invokeLater(this);
    }
}
