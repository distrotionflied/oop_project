import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.Border;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Panel;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Flow;

public class application {
    public static void main(String[] args) {
        MyFrame frame = new MyFrame();
        frame.setVisible(true);
    }
}

class MyFrame extends JFrame {
    private GraphPanel graphPanel;
    private JTable table = new JTable(5, 4);
    private Button btnOpen, btnCal;
    private Panel pnOpen;

    MyFrame() {
        // สร้างข้อมูลสำหรับ 3 เส้นกราฟ
        List<Double> redScores = generateRandomData(10, 10);
        List<Double> blueScores = generateRandomData(10, 10);
        List<Double> greenScores = generateRandomData(10, 10);

        // สร้างเส้นกราฟ 3 เส้นด้วยสีที่กำหนด
        grapLine redLine = new grapLine(redScores, new Color(255, 0, 0, 180), new Color(200, 0, 0, 180)); // แดง
        grapLine blueLine = new grapLine(blueScores, new Color(0, 0, 200, 180), new Color(0, 0, 150, 180)); // น้ำเงินเข้ม
        grapLine greenLine = new grapLine(greenScores, new Color(0, 180, 0, 180), new Color(0, 150, 0, 180)); // เขียว

        // สร้าง GraphPanel และเพิ่มเส้นกราฟ
        graphPanel = new GraphPanel(redLine); // เพิ่มเส้นแรก
        graphPanel.addLine(blueLine); // เพิ่มเส้นที่สอง
        graphPanel.addLine(greenLine); // เพิ่มเส้นที่สาม

        // ตั้งค่าหน้าต่าง
        setTitle("กราฟ 3 เส้น (แดง, น้ำเงิน, เขียว)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // เพิ่มปุ่ม
        JPanel panel = new JPanel();
        Button btnOpen = new Button("Open File");
        Button btnCal = new Button("Calculate");
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panel.setLayout(new FlowLayout());
        panel.add(btnOpen);
        panel.add(btnCal);

        // จัดวางคอมโพเนนต์
        add(table, BorderLayout.NORTH);
        add(graphPanel, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

    }

    // เมธอดสร้างข้อมูลสุ่ม
    private List<Double> generateRandomData(int count, int max) {
        List<Double> data = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            data.add(random.nextDouble() * max);
        }
        return data;
    }
}