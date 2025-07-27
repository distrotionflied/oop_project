<<<<<<< Updated upstream
import javax.swing.JFrame;
import javax.swing.JTable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

=======
import java.awt.*;
import java.util.concurrent.Flow;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.awt.event.*;
import java.io.BufferedReader;
import javax.swing.event.DocumentEvent;   // สำหรับ DocumentEvent
import javax.swing.event.DocumentListener; // สำหรับ DocumentListener
import javax.swing.Timer;                 // ถ้าใช้ Timer สำหรับดีเลย์
>>>>>>> Stashed changes
public class application {
    public static void main(String[] args) {
        MyFrame frame = new MyFrame();
        frame.setVisible(true);
    }
}

class MyFrame extends JFrame {
    private GraphPanel graphPanel;
    private JTable table = new JTable(5, 4);

<<<<<<< Updated upstream
    MyFrame() {
        // สร้างข้อมูลสำหรับ 3 เส้นกราฟ
        List<Double> redScores = generateRandomData(10, 10);
        List<Double> blueScores = generateRandomData(10, 10);
        List<Double> greenScores = generateRandomData(10, 10);
=======
    private JPanel buttomJPanel;
    private JButton loadFile;
    private JLabel textDeep;
    private JTextField textCal;
    private JPanel panelNear;
    private JButton Calculate;
    private JLabel textDistance;
    private LoadFileListener loadFileListener = new LoadFileListener();
    private Timer updateTimer = new Timer(1000, e -> updateAutomatically()); 
    private OutputPanel outputPanel;
    
    private void updateAutomatically() {
        if (loadFileListener.getloadFileSuccess_Status()) {
            try {
                int newFluidContact = Integer.parseInt(textCal.getText());
                loadFileListener.getVGC().setbaseHolizonValue(newFluidContact);
                loadFileListener.getVGC().findGasVolume();
                outputPanel.refreshPanel();
            } catch (NumberFormatException ex) {
                // ไม่ต้องแจ้งเตือนขณะพิมพ์
            }
        }
    }

    BufferedReader bufferedReader; //ตัวอ่านไฟล์

    //ส่งค่า ผ่านคลาส ด้วย เมธอด
    public JTextField getTextCal() {
        return textCal;
    }
>>>>>>> Stashed changes

        // สร้างเส้นกราฟ 3 เส้นด้วยสีที่กำหนด
        grapLine redLine = new grapLine(redScores, new Color(255, 0, 0, 180), new Color(200, 0, 0, 180));    // แดง
        grapLine blueLine = new grapLine(blueScores, new Color(0, 0, 200, 180), new Color(0, 0, 150, 180));  // น้ำเงินเข้ม
        grapLine greenLine = new grapLine(greenScores, new Color(0, 180, 0, 180), new Color(0, 150, 0, 180)); // เขียว

        // สร้าง GraphPanel และเพิ่มเส้นกราฟ
        graphPanel = new GraphPanel(redLine); // เพิ่มเส้นแรก
        graphPanel.addLine(blueLine);         // เพิ่มเส้นที่สอง
        graphPanel.addLine(greenLine);        // เพิ่มเส้นที่สาม

        // ตั้งค่าหน้าต่าง
        setTitle("กราฟ 3 เส้น (แดง, น้ำเงิน, เขียว)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 500);
        setLocationRelativeTo(null);

        // จัดวางคอมโพเนนต์
        add(table, BorderLayout.NORTH);
        add(graphPanel, BorderLayout.CENTER);
    }

<<<<<<< Updated upstream
    // เมธอดสร้างข้อมูลสุ่ม
    private List<Double> generateRandomData(int count, int max) {
        List<Double> data = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            data.add(random.nextDouble() * max);
=======
    //ทำguiด้านล่าง============================================================================
    private void bottomGui() {
        buttomJPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 150, 25));
        buttomJPanel.setPreferredSize(new Dimension(0, 80));
        buttomJPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        buttomJPanel.setFont(tahomaFont);

        loadFile = new JButton("loadFile");
        loadFile.setFont(tahomaFont);
        loadFile.addActionListener(loadFileListener);

        Calculate = new JButton("CALCULATE");
        Calculate.setFont(tahomaFont);

        //textใส่ความลึก
        panelNear = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panelNear.setFont(tahomaFont);

        textDeep = new JLabel("Calculate deep level Fluid contact");
        textDeep.setFont(tahomaFont);

        textDistance = new JLabel("Meter");
        textDistance.setFont(tahomaFont);
        
        textCal = new JTextField("2500", 10);
        textCal.setFont(tahomaFont);

        //ที่add
        panelNear.add(textDeep);
        panelNear.add(textCal);
        panelNear.add(textDistance);
        panelNear.add(Calculate);
        panelNear.setOpaque(false);

        buttomJPanel.add(loadFile);
        buttomJPanel.add(panelNear);
        
        //  panel สำหรับ  output
        OutputPanel outputPanel = new OutputPanel(loadFileListener);
        add(outputPanel,BorderLayout.CENTER);

        //ปุ่ม calculate
            Calculate.addActionListener(e -> {
        try {
            int newFluidContact = Integer.parseInt(textCal.getText()); // ดึงค่าล่าสุด
            loadFileListener.getVGC().setbaseHolizonValue(newFluidContact); // อัปเดตค่า
            loadFileListener.getVGC().findGasVolume();; // คำนวณใหม่
            outputPanel.refreshPanel();
        } catch (NumberFormatException ex) {
            // แจ้งเตือนข้อผิดพลาด
        }
    });
        

    textCal.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            updateAutomatically();
        }
        @Override
        public void removeUpdate(DocumentEvent e) {
            updateAutomatically();
        }
        @Override
        public void changedUpdate(DocumentEvent e) {
            // ไม่จำเป็นสำหรับ JTextField ปกติ
        }

        private void updateAutomatically() {
            if (loadFileListener.getloadFileSuccess_Status()) {
                    try {
                        int newFluidContact = Integer.parseInt(textCal.getText());
                        // 1. อัปเดตค่าใน VolumeOfGasCalculator
                        loadFileListener.getVGC().setbaseHolizonValue(newFluidContact);
                        // 2. คำนวณใหม่
                        loadFileListener.getVGC().findGasVolume();
                        // 3. รีเฟรชผลลัพธ์
                        outputPanel.refreshPanel();
                    } catch (NumberFormatException ex) {
                        // ไม่ต้องแจ้งเตือนขณะพิมพ์ (รอจนผู้ใช้หยุดพิมพ์)
                    }
                }
            }
        }
        );


        add(buttomJPanel, BorderLayout.SOUTH);
    }
    //END======================================================================================

    //ทำให้เปิดหน้าต่างเลือกไฟล์ได้==================================================================
        class LoadFileListener implements ActionListener {
        private VolumeOfGasCalculator vgc;
        double[][] allPercent;
        int[][] allValue;
        //สำหรับเป็น แถวและหลัก ที่โหลดจากไฟล์มาได้
        int loadRow;
        int loadCol;
        boolean loadFileSuccess = false;
        public int getLoadCol() {
                return loadCol;
        }
        public int getLoadRow() {
                return loadRow;
        }
        public boolean getloadFileSuccess_Status(){
            return loadFileSuccess;
        }
        public double[][] getAllPercent() {
            return allPercent;
        }
        public int[][] getAllValue() {
            return allValue;
        }
        public VolumeOfGasCalculator getVGC() {
            return vgc;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooseFile = new JFileChooser();
            chooseFile.setFileFilter(new FileNameExtensionFilter("Text File (*.txt)", "txt"));
            if (chooseFile.showOpenDialog(MyFrame.this) == JFileChooser.APPROVE_OPTION) {
                // ผู้ใช้เลือกไฟล์และกด "Open"
                File selectedFile = chooseFile.getSelectedFile(); // ได้รับ File object ที่ผู้ใช้เลือกมา
                List<List<Integer>> baseHolizonDataTable = new ArrayList<>();
                // 1. ตรวจสอบว่าเป็นไฟล์ว่างเปล่าหรือไม่
                if (selectedFile.length() == 0) {
                    JOptionPane.showMessageDialog(MyFrame.this,
                            "The selected file is empty. Please choose a file with content.",
                            "Empty File Error",
                            JOptionPane.WARNING_MESSAGE);
                    return; // ออกจากเมธอด ไม่ต้องทำอะไรต่อ
                }

                // 2. ตรวจสอบว่าข้อมูลทั้งหมดในไฟล์เป็นตัวเลขทั้งหมดหรือไม่
                try {
                    // อ่านทุกบรรทัดในไฟล์เป็น List ของ String
                    List<String> allLines = Files.readAllLines(selectedFile.toPath());
                    boolean allNumbers = true; // ตั้งสถานะเริ่มต้นว่าทุกบรรทัดเป็นตัวเลข
                    String [] dataStr; //สำหรับเก็บเช็คทีละตัว
                    int [] dataInt;
                    int colOfLineChecker = 0; //สำหรับตรวจสอบว่าคอลัมพ์เท่ากันไหม และจะเป็นตัวอินพุตจำนวนคอลัมน์
                    boolean isColEqual = true;//สถานะ สำหรับตรวจสอบว่าคอลัมพ์เท่ากันไหม
                    int rowThatCanUsed = allLines.size();//จำนวนบรรทัดที่ไม่นับบรรทัดที่ว่าง และจะเป็นจำนวนของ row
                    if (allLines.isEmpty()) { // กรณีไฟล์ไม่ว่างเปล่าแต่ไม่มีบรรทัดข้อมูล (เช่น มีแค่ enter)
                        JOptionPane.showMessageDialog(MyFrame.this,
                                "The selected file contains no discernible data lines.",
                                "No Data Error",
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    for (String line : allLines) {
                        // ลบช่องว่างหัวท้ายบรรทัดก่อนตรวจสอบ
                        System.out.print(line);
                        if (line.isEmpty()) {
                                rowThatCanUsed--;//จะมีการลบทีละ 1 เมื่อพบบบรรทัดที่ว่าง
                                continue;
                        }else{
                            //split
                            dataStr = line.split(" ");
                            dataInt = new int[dataStr.length];
                            for (String dataStrtoDecimal : dataStr) {
                                // ตรวจสอบว่าแต่ละบรรทัดเป็นตัวเลขหรือไม่
                                try {
                                    dataInt
                                    [
                                        Arrays.asList(dataStr).indexOf(dataStrtoDecimal)
                                    ]
                                     = Integer.parseInt(dataStrtoDecimal); // พยายามแปลงเป็นตัวเลข

                                } catch (NumberFormatException ex) {
                                    // ถ้าแปลงไม่ได้ แสดงว่าไม่ใช่ตัวเลข
                                    allNumbers = false;
                                    break; // เจออันที่ไม่ใช่ตัวเลข ก็หยุดตรวจสอบทันที
                                }
                            }
                            if(colOfLineChecker == 0){
                                colOfLineChecker = dataStr.length;
                            }else if(colOfLineChecker != dataStr.length){
                                isColEqual = false;
                            }
                        }
                        if (!allNumbers || !isColEqual){break;}
                        else{
                            baseHolizonDataTable.add(Arrays.stream(dataInt).boxed().collect(Collectors.toList()));//เมื่อผ่านการตรวจสอบจะเก็บอาเรย์ที่ผ่าน ใส่ใน arraylist
                            //ใช้ .stream เพื่อให้ arraylist ยอมรับ int[]
                        }
                    }
                    if(!isColEqual){
                        JOptionPane.showMessageDialog(MyFrame.this,
                                "The number of column in each row is not equal",
                                "Invalid Data Error",
                                JOptionPane.ERROR_MESSAGE);
                                return;
                    }else if (!allNumbers) {
                        JOptionPane.showMessageDialog(MyFrame.this,
                                "The selected file contains non-numeric data. Please ensure all data are numbers.",
                                "Invalid Data Error",
                                JOptionPane.ERROR_MESSAGE);
                                return;
                    } else {
                        // ถ้าไฟล์ไม่ว่าง ข้อมูลทั้งหมดเป็นตัวเลข และ จำนวนคอลัมน์ เท่ากันในทุกแถว
                        // *** ใส่โค้ดสำหรับประมวลผลไฟล์ต่อจากนี้ ***
                        //ประกาศออปเจคของคลาส
                            int fluidContact = 2500;
                            try {
                                    fluidContact =  Integer.parseInt(textCal.getText());
                                } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(MyFrame.this,
                                    "Data Invalid Please check your Textflied",
                                    "Invalid Data Error",
                                    JOptionPane.INFORMATION_MESSAGE); 
                                    return;
                                }

                                // ใน LoadFileListener ก่อนสร้าง vgc
                                    for (List<Integer> row : baseHolizonDataTable) {
                                        for (int value : row) {
                                            if (value <= 0) {
                                                JOptionPane.showMessageDialog(MyFrame.this,
                                                    "value less than zero",
                                                    "data invalid",
                                                    JOptionPane.ERROR_MESSAGE);
                                                return;
                                            }
                                        }
                                    }

                                // ใน LoadFileListener.actionPerformed()
                                vgc = new VolumeOfGasCalculator(rowThatCanUsed, colOfLineChecker, fluidContact);
                                for (int i = 0; i < rowThatCanUsed; i++) {
                                    List<Integer> rowData = baseHolizonDataTable.get(i);
                                    for (int j = 0; j < colOfLineChecker; j++) {
                                        vgc.setbaseHolizonValuePerCell(i, j, rowData.get(j));
                                        System.out.print(rowData.get(j) + " "); // Debug output
                                    }
                                    System.out.println();
                                }
                        vgc.findGasVolume(); //คำนวณหาค่า
                        allPercent = vgc.getPercentVolume();
                        allValue = vgc.getVolume();
                        loadCol = colOfLineChecker;
                        loadRow = rowThatCanUsed;
                        if(vgc.isInfoOfDataIsReal()){
                        JOptionPane.showMessageDialog(MyFrame.this,
                                "File loaded successfully! All data are numbers.",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                                loadFileSuccess = true;      
                        }else{
                            JOptionPane.showMessageDialog(MyFrame.this,
                                "Your data isn't base on Reality.",
                                "Invalid Data Error",
                                JOptionPane.WARNING_MESSAGE);
                                return;
                        }
                    }

                } catch (IOException ex) {
                    // จัดการข้อผิดพลาดที่อาจเกิดขึ้นระหว่างการอ่านไฟล์
                    JOptionPane.showMessageDialog(MyFrame.this,
                            "An error occurred while reading the file: " + ex.getMessage(),
                            "File Reading Error",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace(); // พิมพ์ stack trace เพื่อดูรายละเอียดข้อผิดพลาด
                }
            } else {
                // ผู้ใช้กด "Cancel" หรือปิดหน้าต่าง JFileChooser
                // System.out.println("File selection cancelled.");
            }
>>>>>>> Stashed changes
        }
        return data;
    }
<<<<<<< Updated upstream
}
=======
    //=========================================================================================

//class สำหรับ พาเนบแสดงผล
class OutputPanel extends JPanel{
    LoadFileListener lfl;
    int col;
    int row;
    public OutputPanel(LoadFileListener lfl) {  
        this.lfl = lfl;
         refreshPanel(); // เรียกครั้งแรกเมื่อสร้าง
    }
    void refreshPanel(){
        row = this.lfl.getLoadRow();
        col = this.lfl.getLoadCol();
        this.removeAll(); // ลบคอมโพเนนต์ทั้งหมดใน panel
        if (lfl == null || !lfl.getloadFileSuccess_Status()) {
        setBackground(Color.RED);
        return;
        }

        VolumeOfGasCalculator vgc = lfl.getVGC();
        if (vgc == null) return;
        
            setLayout(new GridLayout(row,col,1,1));
            double[][] percent = vgc.getPercentVolume();
            int [][] volume = vgc.getVolume();
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                String percentStr = String.format(
                        "<html><div style='text-align: center;'>" +
                        "<font size='5'><b>%.2f%%</b></font><br/>" +  // แสดงทศนิยม 2 ตำแหน่ง
                        "<font size='3'>(%,d)</font>" +               // แสดงจำนวนเต็มมี comma
                        "</div></html>",
                        percent[i][j], 
                        volume[i][j]
                    ); // ค่าร้อยละ และ ปริมาตร
                JLabel label = new JLabel(percentStr, SwingConstants.CENTER);
                label.setOpaque(true);
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                if (percent[i][j] >= 50) {
                    label.setBackground(Color.GREEN);
                } else if (percent[i][j] == 0) {
                    label.setBackground(Color.RED);
                } else if(percent[i][j] < 50){
                    label.setBackground(Color.YELLOW);
                }
                this.add(label); // ❗❗ สำคัญ
                }
            }
        this.revalidate(); // บังคับให้ JPanel คำนวณเลย์เอาต์ใหม่
        this.repaint();    // วาดใหม่
    }
}


//คลาสสำหรับคำนวณ ปริมาตรแก็ส
class VolumeOfGasCalculator {
    //atrribute 
    private int row;
    private int col;
    private int fluidContract;
    private int [][] baseHolizon;  
    private int [][] topHolizon; 
    private int [][] volume;
    private double [][] percenrVolume;
    boolean infoOfDataIsReal = true;

    public VolumeOfGasCalculator(int row, int col , int fluidContract){
        this.row = row;
        this.col = col;
        baseHolizon = new int[this.row][this.col];  
        topHolizon = new int[this.row][this.col];  
        volume = new int[this.row][this.col]; 
        percenrVolume = new double[this.row][this.col]; 
        this.fluidContract = fluidContract;
    }
    public void setbaseHolizonValuePerCell(int row, int col, int value) {
        if (row >= 0 && row < this.row && col >= 0 && col < this.col) {
            this.baseHolizon[row][col] = value;
        }
    }
    public void setbaseHolizonValue(int data){
        fluidContract = data;
    }
    public int getCol() {
        return col;
    }
    public int getRow() {
        return row;
    }
    public int[][] getVolume(){
        return volume;
    }
    public double[][] getPercentVolume(){
        return percenrVolume;
    }
    public boolean isInfoOfDataIsReal(){
        return infoOfDataIsReal;
    }
    public void findGasVolume() {
    for (int i = 0; i < baseHolizon.length; i++) {
        for (int j = 0; j < baseHolizon[0].length; j++) {
            // ตรวจสอบค่าฐานก่อนคำนวณ
            topHolizon[i][j] = baseHolizon[i][j] - 200;
            // คำนวณปกติ
            volume[i][j] =  150*150*(fluidContract - topHolizon[i][j]);
             System.out.println("="+volume[i][j]);
            percenrVolume[i][j] = ((double)volume[i][j] / (double)(200*150*150)) * 100;
            System.out.println(percenrVolume[i][j] + "%");
            if(percenrVolume[i][j] < 0 || percenrVolume[i][j] > 100){
                infoOfDataIsReal = false;
            }
        }
    }
}
} 

}



>>>>>>> Stashed changes
