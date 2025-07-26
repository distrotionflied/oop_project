import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class application {
    public static void main(String[] args) {
        MyFrame frame = new MyFrame();
        frame.setVisible(true);

    }
}

class MyFrame extends JFrame {

    private JPanel buttomJPanel;
    private JButton loadFile;
    private JLabel textDeep;
    private JTextField textCal;
    private JPanel panelNear;
    private JButton Calculate;
    private JLabel textDistance;

    private Font tahomaFont = new Font("Tahoma", Font.PLAIN, 16); //เปลี่ยนฟอนต์

    public MyFrame() {
        setTitle("Jewel Suite");
        setSize(1000, 1000);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        bottomGui();
        setVisible(true);
        
    }

    //ทำguiด้านล่าง============================================================================
    private void bottomGui() {
        getContentPane().setBackground(new Color(245, 245, 250));
        
        buttomJPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 150, 25));
        buttomJPanel.setPreferredSize(new Dimension(0, 100));
        buttomJPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        buttomJPanel.setBackground(new Color(255, 255, 255));
        buttomJPanel.setFont(tahomaFont);

        loadFile = new JButton("loadFile");
        loadFile.setFont(tahomaFont);
        loadFile.setBackground(new Color(70, 130, 180));
        loadFile.setForeground(Color.WHITE);
        loadFile.setFocusPainted(false);
        loadFile.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        loadFile.addActionListener(new LoadFileListener());

        Calculate = new JButton("CALCULATE");
        Calculate.setFont(tahomaFont);
        Calculate.setBackground(new Color(60, 179, 113));
        Calculate.setForeground(Color.WHITE);
        Calculate.setFocusPainted(false);
        Calculate.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        //textใส่ความลึก
        panelNear = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panelNear.setFont(tahomaFont);
        panelNear.setOpaque(false);

        textDeep = new JLabel("Calculate deep level Fluid contact");
        textDeep.setFont(tahomaFont);

        textDistance = new JLabel("Meter");
        textDistance.setFont(tahomaFont);
        
        textCal = new JTextField("2500", 10);
        textCal.setFont(tahomaFont);
        textCal.setFont(tahomaFont);
        textCal.setHorizontalAlignment(JTextField.CENTER);
        textCal.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        //  panel สำหรับ  output
        OutputPanel outputPanel = new OutputPanel();

        //ที่add
        panelNear.add(textDeep);
        panelNear.add(textCal);
        panelNear.add(textDistance);
        panelNear.add(Calculate);
        panelNear.setOpaque(false);

        buttomJPanel.add(loadFile);
        buttomJPanel.add(panelNear);
        
        add(outputPanel,BorderLayout.CENTER);//พาเนลสำหรับ เอาท์พุต ใส่ไว้ก่อน แต่ควรมาแก้ออกทีหลัง
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
    }
}
//class สำหรับ พาเนบแสดงผล
class OutputPanel extends JPanel{
    public OutputPanel() {
        setLayout(new GridLayout(10, 10, 2, 2));
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // 10x10 ช่อง
        setBackground(new Color(240, 240, 240)); // พื้นหลังสุดเท่

        for (int i = 0; i < 100; i++) {
            int percent = (int)(Math.random()*100); // สุ่มเปอร์เซ็นต์ฺ for test gui
            String percentStr = Integer.toString(percent)+"%"; // สมมุติค่าร้อยละ
            
            JLabel label = new JLabel(percentStr, SwingConstants.CENTER);
            label.setOpaque(true);
            label.setFont(new Font("Tahoma", Font.BOLD, 12));
            label.setForeground(Color.BLACK);
            label.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            
            if (percent >= 50) {
                label.setBackground(Color.GREEN); // สีเขียว
            } else if (percent == 0) {
                label.setBackground(Color.RED); // สีแดง
            } else {
                label.setBackground(Color.YELLOW); // สีเหลือง
            }

            this.add(label); // ❗❗ สำคัญ
        }
    }
}

//คลาสสำหรับคำนวณ ปริมาตรแก็ส
class VolumeOfGasCalculator {
    //atrribute
    private int row;
    private int col;

    //public VolumeOfGasCalculator(int row,int col){
    //
    //}
    public int getCol() {
        return col;
    }
    public int getRow() {
        return row;
    }
    //public int getGasVolume(){
    //    return
    //}
} 