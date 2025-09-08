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
    private OutputPanel outputPanel;
    boolean infoOfDataIsReal = true;
    
    /*
     * UpDateAotomatically ใช้สำหรับอัพเดพค่า fluid contact และคำนวนปริมาตร แบบอัติโนมัติ
     * ทำงานแค่ตอนที่โหลดไฟล์เสร็จเท่านั้น
     * 
     */
    private void updateAutomatically() {
        if (loadFileListener.getloadFileSuccess_Status()) {
            try {
                int newFluidContact = Integer.parseInt(textCal.getText());
                loadFileListener.getVGC().setFluidContract(newFluidContact);
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

    
    // สีธีมหลัก
    Color bgColor = new Color(245, 248, 250); // สีพื้นหลังอ่อน
    Color btnColor = new Color(33, 150, 243); // สีปุ่มน้ำเงิน
    Color btnTextColor = Color.WHITE;
    Color borderColor = new Color(200, 200, 200);

    // สร้าง panel ด้านล่าง
    buttomJPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 150, 25));
    buttomJPanel.setPreferredSize(new Dimension(0, 90));
    buttomJPanel.setBackground(bgColor);
    buttomJPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, borderColor));

    // ปุ่ม load file
    loadFile = new RoundedButton("Load File");
    loadFile.setFont(tahomaFont);
    loadFile.setBackground(btnColor);
    loadFile.setForeground(btnTextColor);
    loadFile.setFocusPainted(false);
    loadFile.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
    loadFile.addActionListener(loadFileListener);

    // ปุ่มคำนวณ
    Calculate = new RoundedButton("Calculate");
    Calculate.setFont(tahomaFont);
    Calculate.setBackground(new Color(76, 175, 80)); // สีเขียว
    Calculate.setForeground(Color.WHITE);
    Calculate.setFocusPainted(false);
    Calculate.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

    // panel ด้านขวา
    panelNear = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
    panelNear.setOpaque(false);

    // label และ input
    textDeep = new JLabel("Fluid Contact:");
    textDeep.setFont(tahomaFont);
    textDeep.setForeground(new Color(50, 50, 50));

    textCal = new JTextField("2500", 10);
    textCal.setFont(tahomaFont);
    textCal.setBorder(BorderFactory.createLineBorder(borderColor));

    textDistance = new JLabel("meters");
    textDistance.setFont(tahomaFont);
    textDistance.setForeground(new Color(80, 80, 80));

    //ที่add
    panelNear.add(textDeep);
    panelNear.add(textCal);
    panelNear.add(textDistance);
    panelNear.add(Calculate);

    // เพิ่มทุกอย่างลง panel ล่าง
    buttomJPanel.add(loadFile);
    buttomJPanel.add(panelNear);

    // Panel แสดงผล
    outputPanel = new OutputPanel(loadFileListener);
    add(outputPanel, BorderLayout.CENTER);

    // ฟังชันก์ปุ่ม Calculate
    /*
     * เอาไว้กดปุ่มCalcualate
     * ก็คือจะแปลงค่าที่ใส่ลงไปในช่องcalculate จากนั้นจะเป็นจากStringในช่องนั้นให้เป็น int 
     * ถ้าไม่ใช่ พวกตัวเลขเป็นพวก ABC พวกตัวอักษร มันจะเกิด Exception NumberFormat เเล้วมันจะโชว์ข้อความว่า
     * Please enter a valid number 
     * ก็คือต้องใส่พวกตัวเลขเท่านั้น
     */
    Calculate.addActionListener(e -> {
        try {
            int newFluidContact = Integer.parseInt(textCal.getText());
            loadFileListener.getVGC().setFluidContract(newFluidContact);
            loadFileListener.getVGC().findGasVolume();
            outputPanel.refreshPanel();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid number.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        }
    });

    // ตรวจค่าทันทีเมื่อพิมพ์
    /*
     * ตัวDocumentจะรอจับการเปลี่ยนแปลงของค่าในช่อง Calculate เเล้วถ้าเกิดการเปลี่ยนแปลงจะทำการ อัพเดตการคำนวนให้ทั้งที่ ไม่ว่าจะลบ หรือ เพิ่ม จำนวนของข้อมูล
     */
    textCal.getDocument().addDocumentListener(new DocumentListener() {
        @Override public void insertUpdate(DocumentEvent e) { updateAutomatically(); }//เอาไว้ตรวจสอบว่ามีการเพิ่มข้อมูลใหม่
        @Override public void removeUpdate(DocumentEvent e) { updateAutomatically(); }//เอาไว้ตรวจว่ามีการลบข้อมูล
        @Override public void changedUpdate(DocumentEvent e) {}
    });

    add(buttomJPanel, BorderLayout.SOUTH);
}

    //END======================================================================================

class RoundedButton extends JButton {
    //เป็นการแต่งปุ่มโหลดไฟล์เฉยๆ
    public RoundedButton(String label) {
        super(label);
        setOpaque(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setForeground(Color.WHITE);
        setFont(new Font("SansSerif", Font.BOLD, 14));
        setBackground(new Color(33, 150, 243)); // สีพื้นหลังปุ่ม
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //ทำให้พวกปุ่มโค้งไม่เป็นหยักให้ดูสวยขึ้น
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); //ทำให้มันด
        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    public void paintBorder(Graphics g) {
        // ไม่วาด border เพื่อความเนียน
    }

    
}
    //ทำให้เปิดหน้าต่างเลือกไฟล์ได้==================================================================
        class LoadFileListener implements ActionListener {
        private VolumeOfGasCalculator vgc;
        double[][] allPercent;
        int[][] allValue;
        //สำหรับเป็น ข้อมูลแถวและหลัก ที่โหลดจากไฟล์มาได้
        int loadRow;
        int loadCol;
        //ตรวจสอบว่าโหลดไฟลืเสร็จแล้วใข่ไหม
        boolean loadFileSuccess = false;
        //เมธอดรับค่าต่างๆ
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
        //action listner
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
                    int rowThatCanUsed = (int)allLines.stream().filter(line -> !line.trim().isEmpty()).count();//จำนวนบรรทัดที่ไม่นับบรรทัดที่ว่าง และจะเป็นจำนวนของ row
                    if (allLines.isEmpty()) { // กรณีไฟล์ไม่ว่างเปล่าแต่ไม่มีบรรทัดข้อมูล (เช่น มีแค่ enter)
                        JOptionPane.showMessageDialog(MyFrame.this,
                                "The selected file contains no discernible data lines.",
                                "No Data Error",
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    System.out.println(rowThatCanUsed);
                    for (String line : allLines) {
                            //split
                            dataStr = line.split("\\s+");
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
                            }else if(colOfLineChecker != dataStr.length &&colOfLineChecker != dataInt.length){
                                isColEqual = false;
                            }
                        
                        if (!allNumbers && !isColEqual){break;}
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
                            vgc.setbaseHolizonValuePerline(baseHolizonDataTable);
                        //เรียกเมธอดมาคำนวณ และ ทำการเก็บค่า ลงใน attribute ของคลาส 
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
                                "Your data isn't base on Reality plese fix your data in your file or fix number of fluidContract level again for better output.",
                                "Warning Invalid Data",
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
    //attribute ของคลาส
    LoadFileListener lfl;
    int col;
    int row;
    public OutputPanel(LoadFileListener lfl) {  
        //ที่ผมเรียกใช้ LoadFileListener เพราะว่า เรามีเมธอด และ attribute นี้ในคลาสแต่เดิมที่จะใช้ในการคำนวณอยู่แล้ว
        this.lfl = lfl;
        //รีเฟรช พาเนล
         refreshPanel(); // เรียกครั้งแรกเมื่อสร้าง
    }
    void refreshPanel(){
        row = this.lfl.getLoadRow();
        col = this.lfl.getLoadCol();
        this.removeAll(); // ลบคอมโพเนนต์ทั้งหมดใน panel
        if(lfl.getloadFileSuccess_Status()){
            setBackground(Color.WHITE);
            //set Layout ของ panel
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
                    label.setBackground(new Color(144, 238, 144)); //สีเขียว
                } else if (percent[i][j] <= 0) {
                    label.setBackground(Color.RED);
                } else if(percent[i][j] < 50){
                    label.setBackground(new Color(240, 230, 140)); //สีเหลือง
                }
                //เพิ่ม label ลงใน พาเนล
                this.add(label); // ❗❗ สำคัญ
                }
            }
        }else{
            setBackground(Color.LIGHT_GRAY);
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

    //คอนสตรัคเตอร์
    public VolumeOfGasCalculator(int row, int col , int fluidContract){
        this.row = row;
        this.col = col;
        baseHolizon = new int[this.row][this.col];  
        topHolizon = new int[this.row][this.col];  
        volume = new int[this.row][this.col]; 
        percenrVolume = new double[this.row][this.col]; 
        this.fluidContract = fluidContract;
    }

    //เมธอดเช็ทค่า
    public void setbaseHolizonValuePerline(List<List<Integer>> dataTable){
        for (int i = 0; i < this.row; i++) {
            // ตรวจสอบขนาดคอลัมน์ในแต่ละแถว (ถ้าเป็นไปได้)
            if (dataTable.get(0).size() != this.col) {
                System.err.println("Warning: Row " + i + " has inconsistent column count. Expected " + this.col + ", got " + dataTable.size());
                 // คุณอาจจะเลือกจัดการตรงนี้อย่างไร: เช่น ปรับ this.col, หรือข้ามแถวนี้
            }
            for (int j = 0; j < this.col; j++) {
                // ตรวจสอบป้องกัน IndexOutOfBoundsException หากแถวสั้นกว่าที่คาด
                if (j < dataTable.get(0).size()) {
                    this.baseHolizon[i][j] = dataTable.get(i).get(j);
                } else {
                    // จัดการกรณีที่คอลัมน์ขาดหายไป เช่น กำหนดเป็น 0 หรือค่า default
                    this.baseHolizon[i][j] = 0;
                }
            }
        }
    }
    public void setFluidContract(int data){
        fluidContract = data;
    }

    //เมธอดดึงค่า
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


    //ตรวจสอบว่า ข้อมูล ถูกต้อง ตามที่มันควรเป็นไหม
    public boolean isInfoOfDataIsReal(){
        return infoOfDataIsReal;
    }
    //ส่วนหาค่าปริมาตร และเปอร์เซ็นต์เพื่อการแสดงผล
    public void findGasVolume(){
        for (int i = 0; i < baseHolizon.length; i++) {
            for (int j = 0; j < baseHolizon[0].length; j++) {
                //หาค่า topHolizon จากการนำ baseHolizon[i][j] - 200
                topHolizon[i][j] = baseHolizon[i][j] - 200;
                //คำนวณหาปริมาณแก๊ส และ เปอร์เซ็นต์
                volume[i][j] = 150*150*(fluidContract - (topHolizon[i][j]));
                percenrVolume[i][j] =  ((double)volume[i][j] / (double)(150*150*(200))) * 100;
                //ฟังก์ชั่นตรวจสอบค่า เมื่อ percenrVolume มีค่ามากกว่า 100 หรือ น้อยกว่า 0
                if(percenrVolume[i][j] > 100 || percenrVolume[i][j] < 0){
                    //อยากแก้ให้ปกติ แก้ตรงนี้ นะ เปลี่ยนเป็น false แล้ว ลบ 2 บรรทัดล่างนี้ออก ยกเว้น infoOfDataIsReal
                    if(percenrVolume[i][j] > 100){percenrVolume[i][j] = 100;}
                    if(percenrVolume[i][j] < 0){percenrVolume[i][j] = 0;}
                    infoOfDataIsReal = true;
                }
            }
        }                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
    }
}
}
