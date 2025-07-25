import java.awt.*;
import java.util.concurrent.Flow;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;


public class application {
    public static void main(String[] args) {
        MyFrame frame = new MyFrame();
        
    }
}
class MyFrame extends JFrame{
        
        private JPanel buttomJPanel;
        private JButton loadFile;
        private JLabel textDeep;
        private JTextField textCal;
        private JPanel panelNear;
        private JButton Calculate;
        private JLabel textDistance;

        public MyFrame(){
        setTitle("Jewel Suite");
        setSize(1000,1000);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        bottomGui();
        setVisible(true);
       }

       //ทำguiด้านล่าง============================================================================
        private void bottomGui(){
        buttomJPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,150,25));
        buttomJPanel.setPreferredSize(new Dimension(0,80));;
        buttomJPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        loadFile = new JButton("loadFile");
        loadFile.addActionListener(new LoadFileListener());
        Calculate = new JButton("CALCULATE");

        //textใส่ความลึก
        panelNear = new JPanel(new FlowLayout(FlowLayout.LEFT,8,0));
        textDeep = new JLabel("Calculate deep level Fluid contact");
        textDistance = new JLabel("Meter");
        textCal = new JTextField("2500",10);

        //ที่add
        panelNear.add(textDeep);
        panelNear.add(textCal);
        panelNear.add(textDistance);
        panelNear.add(Calculate);
        panelNear.setOpaque(false);
        buttomJPanel.add(loadFile);
        buttomJPanel.add(panelNear);
        


        add(buttomJPanel, BorderLayout.SOUTH);
        }
        //END======================================================================================

        //ทำให้เปิดหน้าต่างเลือกไฟล์ได้==================================================================
        class LoadFileListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
        JFileChooser chooseFile = new JFileChooser();
        chooseFile.setFileFilter(new FileNameExtensionFilter("Text File (*.txt)", "txt"));
        if (chooseFile.showOpenDialog(MyFrame.this) == JFileChooser.APPROVE_OPTION) {
        }
        
    }
        //END=====================================================================================
}
}

