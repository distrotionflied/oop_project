import java.awt.Color;
import javax.swing.JFrame;

public class AsteroidMain {
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
//คลาสสำหรับเก็บค่าต่างๆ และ เอา ฟอร์ม กับ behavior มาประกอบกัน
class Asteroid_Entity {
    //attribute
    /*ค่าของ thread sleep เพื่อกำหนดความเร็วของอุกกาบาต
    ถ้าค่านี้เยอะ อุกกาบาตจะช้า | ถ้าค่าน้อย อุกกาบาตจะเร็ว*/
    Long theadSleepSpeed; 
    
}

//สำหรับรุปร่างลักษณะ
class Asteroid_From {
    //attribute
    /*ค่าของ thread sleep เพื่อกำหนดความเร็วของอุกกาบาต
    ถ้าค่านี้เยอะ อุกกาบาตจะช้า | ถ้าค่าน้อย อุกกาบาตจะเร็ว*/
    Long theadSleepSpeed; 
    
}

//สำหรับพฤติกรรม การตอบโต้ต่อสิ่งแวดล้อม
class Asteroid_Bahavior {
    
}
