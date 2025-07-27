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
        });


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
                            int fluiidContract = 2500;
                            try {
                                    fluiidContract =  Integer.parseInt(textCal.getText());
                                } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(MyFrame.this,
                                    "Data Invalid Please check your Textflied",
                                    "Invalid Data Error",
                                    JOptionPane.INFORMATION_MESSAGE); 
                                    return;
                                }
                            vgc = new VolumeOfGasCalculator(rowThatCanUsed, colOfLineChecker,fluiidContract);
                        
                        for (int i = 0; i < baseHolizonDataTable.size(); i++) {
                            vgc.setbaseHolizonValuePerline(i,baseHolizonDataTable.get(i));
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
        }
    }
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
        if(lfl.getloadFileSuccess_Status()){
            setBackground(Color.WHITE);
            setLayout(new GridLayout(row,col,1,1));
            VolumeOfGasCalculator vgc = lfl.getVGC();
            double[][] percent = vgc.getPercentVolume();
            int [][] volume = vgc.getVolume();
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                String percentStr = String.format(
                                        "<html><div style='text-align: center;'>" +
                                        "<font size='5'><b>%.2f%%</b></font><br/>" +  // ตัวใหญ่ + ตัวหนา (บรรทัดบน)
                                        "<font size='3'>(%d)</font>" +               // ตัวเล็ก (บรรทัดล่าง)
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
        }else{
            setBackground(Color.RED);
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
    public void setbaseHolizonValuePerline(int row,List<Integer> dataTable){
        for (int i = 0; i < this.row; i++) {
            // ตรวจสอบขนาดคอลัมน์ในแต่ละแถว (ถ้าเป็นไปได้)
            if (dataTable.size() != this.col) {
                 System.err.println("Warning: Row " + i + " has inconsistent column count. Expected " + this.col + ", got " + dataTable.size());
                 // คุณอาจจะเลือกจัดการตรงนี้อย่างไร: เช่น ปรับ this.col, หรือข้ามแถวนี้
            }
            for (int j = 0; j < this.col; j++) {
                // ตรวจสอบป้องกัน IndexOutOfBoundsException หากแถวสั้นกว่าที่คาด
                if (j < dataTable.size()) {
                    this.baseHolizon[i][j] = dataTable.get(j);
                } else {
                    // จัดการกรณีที่คอลัมน์ขาดหายไป เช่น กำหนดเป็น 0 หรือค่า default
                    this.baseHolizon[i][j] = 0;
                }
            }
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
    public void findGasVolume(){
        for (int i = 0; i < baseHolizon.length; i++) {
            for (int j = 0; j < baseHolizon[0].length; j++) {
                topHolizon[i][j] = baseHolizon[i][j] - 200;
                volume[i][j] = 150*150*(fluidContract - (topHolizon[i][j]));
                percenrVolume[i][j] =  ((double)volume[i][j] / (double)(150*150*(200))) * 100;
                if(percenrVolume[i][j] > 100 || percenrVolume[i][j] < 0){
                    infoOfDataIsReal = false;
                }
            }
        }                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
    }
}
}