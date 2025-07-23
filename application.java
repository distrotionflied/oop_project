import javax.swing.JPanel;
import java.util.List;
import java.util.ArrayList;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JTable;
public class application{
    public static void main(String[] args){
        MyFrame frame = new MyFrame();
        frame.setVisible(true); 
    }
}

class MyFrame extends JFrame{
    List<Double> myScores  = new ArrayList<>();
    
    JTable table = new JTable(5, 4);
    BorderLayout borderLayout = new BorderLayout(10,10);
    MyFrame(){
        myScores.add(95.5);
        myScores.add(88.0);
        myScores.add(72.3);
        myScores.add(91.7);
        GraphPanel graph = new GraphPanel(myScores);
        setTitle("app");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000,500);
        setLocationRelativeTo(null); // ต้องอยู่ก่อน set size
        // Initialize and populate myScores
        //add(table);
        add(table,BorderLayout.NORTH);
        add(graph,BorderLayout.CENTER);
    }
}

