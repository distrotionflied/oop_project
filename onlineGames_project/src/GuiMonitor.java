
import java.awt.Dimension;
import java.awt.HeadlessException;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GuiMonitor {
    public GuiMonitor(){
        HomeFrame hf = new HomeFrame();
        hf.setVisible(true);
    }
}

class HomeFrame extends JFrame{
    Dimension size = new Dimension(500, 600);
    JPanel panel = mew JPanel();
    
    public HomeFrame(){
        setSize(size);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
