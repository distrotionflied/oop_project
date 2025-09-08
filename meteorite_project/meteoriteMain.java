import java.awt.Color;
import javax.swing.JFrame;

public class meteoriteMain {
    public static void main(String[] args) {
        Frame_window fw = new Frame_window();
        fw.setVisible(true);
    }
}
//frame class
class Frame_window extends JFrame {
    //attribute
    int Frame_width = 700;
    int Frame_height = 600;
    public Frame_window() {
        setTitle("Meteorite Frame");
        setSize(Frame_width, Frame_height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        getContentPane().setBackground(Color.BLACK);//สีดำ
        setLayout(null);
    }
}
class Meteorite {
    //attribute
    /*ค่าของ thread sleep เพื่อกำหนดความเร็วของอุกกาบาต
    ถ้าค่านี้เยอะ อุกกาบาตจะช้า | ถ้าค่าน้อย อุกกาบาตจะเร็ว*/
    Long theadSleepSpeed; 
    
    

}
