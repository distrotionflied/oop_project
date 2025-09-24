import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;

public class AsteroidMain {
    public static void main(String[] args) {
        if(args[0] != null){
            Frame_window fw = new Frame_window(args[0]);
            fw.setVisible(true);
            System.out.println(args[0]);
        }else{
            System.out.println("EROR! Plaese Read Manual Of RunFlie Fist");
        }
    }
}
//frame class
class Frame_window extends JFrame {
    //attribute
    int Frame_width = 700;
    int Frame_height = 600;
    int amount = 30;
    CollisionReporter report;
    ArrayList <Asteroid_Entity> asteriod = new ArrayList<>();
    public Frame_window(String amountStr) {
        setTitle("Meteorite Frame");
        setSize(Frame_width, Frame_height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        getContentPane().setBackground(Color.BLACK);//สีดำ
        setLayout(null);
        if(amountStr != null && Integer.parseInt(amountStr) != 0){
            amount = Integer.parseInt(amountStr);
            System.out.println("work!");
        }
        for (int i = 0; i < amount; i++) {
            asteriod.add(new Asteroid_Entity(this));
            add(asteriod.get(i).getFrom());
        }
        report = new CollisionReporter(asteriod,this);
        report.start();
    }
    @Override
    public void remove(Component comp) {
        // TODO Auto-generated method stub
        super.remove(comp);
        repaint();
        revalidate();
    }
}
//คลาสสำหรับเก็บค่าต่างๆ และ เอา ฟอร์ม กับ behavior มาประกอบกัน
class Asteroid_Entity {
    //attribute
    Asteroid_From aFrom;
    Asteroid_Behavior aBehavior;
    Dimension size = new Dimension(60, 60);
    Dimension FrameAreaSize;
    Asteroid_Entity(Frame_window frame){
        FrameAreaSize = new Dimension(frame.getWidth()-size.width,frame.getHeight()-size.height);
        aFrom = new Asteroid_From(this);
        aBehavior = new Asteroid_Behavior(this);
        aBehavior.start();
    }
    public Dimension getFrameAreaSize() {
        return FrameAreaSize;
    }
    public Dimension getSize() {
        return size;
    }
    public Asteroid_From getFrom() {
        return aFrom;
    }
    public Asteroid_Behavior getBahavior() {
        return aBehavior;
    }
}

//สำหรับรุปร่างลักษณะ
class Asteroid_From extends JLabel{
    Asteroid_Entity SE;
    Image img;
    Image scaledImg;
    int ExploWidth,ExploHeight;
    String BeginPath = System.getProperty("user.dir")+File.separator+"resource"; 
    Asteroid_From(Asteroid_Entity SE){
        this.SE = SE;
        setBounds(randomPosition().x,
                  randomPosition().y,
                  SE.size.width, SE.size.height
        );
        try {
            //สุ่มลูก อุกกาบาต
            img = getToolkit().getImage(BeginPath+File.separator+"Asteroid"+Integer.toString(new Random().nextInt(3))+".png");
            scaledImg = img.getScaledInstance(SE.getSize().width, SE.getSize().height,  Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(scaledImg);
            if(icon == null || img == null) {
                System.out.println("asteroid pic eror");
            }
            setIcon(icon);
        } catch (Exception e) {
            System.out.println("eror");
        }
        ExploWidth = SE.getSize().width +20;
        ExploHeight = SE.getSize().height +20;
    }
    Point randomPosition(){
            return new Point(
                new Random().nextInt(SE.getFrameAreaSize().width),
                new Random().nextInt(SE.getFrameAreaSize().height) 
            );
    }
    void getExplotionIcon(){
        try {
            ImageIcon gifOfExplotion = new ImageIcon(BeginPath+File.separator+"Explotion_gif.gif");
            Image imgOfExplotion = gifOfExplotion.getImage();
            Image scaledImage = imgOfExplotion.getScaledInstance(ExploWidth, ExploHeight, Image.SCALE_DEFAULT);
            ImageIcon scaledGifIcon = new ImageIcon(scaledImage);
            this.setIcon(scaledGifIcon);
            this.setSize(ExploWidth,ExploHeight);
        } catch (Exception e) {
            System.out.println("explo pic eror");
            // TODO: handle exception
        }
    }
}

//สำหรับพฤติกรรม การตอบโต้ต่อสิ่งแวดล้อม
class Asteroid_Behavior extends Thread{
    /*ค่าของ thread sleep เพื่อกำหนดความเร็วของอุกกาบาต
    ถ้าค่านี้เยอะ อุกกาบาตจะช้า | ถ้าค่าน้อย อุกกาบาตจะเร็ว*/
    Long theadSleepSpeed = Integer.toUnsignedLong(new Random().nextInt(30)+10);
    Asteroid_Entity SE;
    boolean flag;
    int motion_number = new Random().nextInt(7);
    boolean knockTop,knockButtom,knockRight,knockLeft;
    Long distance = 0L;
    Asteroid_Behavior(Asteroid_Entity SE){
        this.SE = SE;
        flag = true;
    }
    @Override
    public void run() {
        while (flag) {
            try {
                motion();
                sleep(theadSleepSpeed);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    void motion(){
        knockTop = (SE.getFrom().getY() <= 0);
        knockButtom = (SE.getFrom().getY() >= SE.getFrameAreaSize().height-20);//อาจจะต้องแก้เพราะยัง fix เกินไป
        knockRight = (SE.getFrom().getX() >= SE.getFrameAreaSize().width);
        knockLeft = (SE.getFrom().getX() <= 0);

        if(motion_number == 0){//ขวา
            motionExecution(1,0);
        }
        if(motion_number == 1){//ซ้าย
            motionExecution(-1,0);
        }
        if(motion_number == 2){//ขวาบน
            motionExecution(1,-1);
        }
        if(motion_number == 3){//ขวาล่าง
            motionExecution(1,1);
        }
        if(motion_number == 4){//ซ้ายบน
            motionExecution(-1,-1);
        }
        if(motion_number == 5){//ซ้ายล่าง
            motionExecution(-1,1);
        }
        if(motion_number == 6){//บน
            motionExecution(0,-1);
        }
        if(motion_number == 7){//ล่าง
            motionExecution(0,1);
        }
        
        if(knockRight){
            if(motion_number == 0) randomMotion(1,4,5);
            if(motion_number == 2) randomMotion(1,5);
            if(motion_number == 3) randomMotion(1,4);
        }
        if(knockLeft){
            if(motion_number == 1) randomMotion(0,2,3);
            if(motion_number == 4) randomMotion(0,3);
            if(motion_number == 5) randomMotion(0,2);
        }
        if(knockTop){
            if(motion_number == 6) randomMotion(7,3,5);
            if(motion_number == 2) randomMotion(7,5);
            if(motion_number == 4) randomMotion(7,3);
        }
        if(knockButtom){
            if(motion_number == 7) randomMotion(6,2,4);
            if(motion_number == 3) randomMotion(6,4);
            if(motion_number == 5) randomMotion(6,2);
        }
        if(knockLeft && knockTop){
            randomMotion(0,3,7);
        }
        if(knockLeft && knockButtom){
            randomMotion(0,2,6);
        }
        if(knockRight && knockButtom){
            randomMotion(1,4,6);
        }
        if(knockRight && knockTop){
            randomMotion(1,5,7);
        }
    }
    void motionExecution(int plusX,int plusY){
       SE.getFrom().setLocation(SE.getFrom().getX()+plusX, SE.getFrom().getY()+plusY);
       distance++;
        if(distance % 10 == 0){
            theadSleepSpeed++;
        }
    }
    void randomMotion(int... motionNumArray){
        motion_number = motionNumArray[new Random().nextInt(motionNumArray.length)];
        if(theadSleepSpeed > 21L){
            theadSleepSpeed -= 20L;
        }
    }
    void giveTerminetPill(){
        flag = false;
    }
}

class CollisionReporter extends Thread{
    Frame_window frame;
    ArrayList<Asteroid_Entity> asteroid;
    CollisionReporter(ArrayList <Asteroid_Entity> asteroid,Frame_window frame){
        this.frame = frame;
        this.asteroid = asteroid; 
    }
    @Override
    public void run() {
        while(true){
            ArrayList<Asteroid_Entity> toRemove = new ArrayList<>();
            for (int i = 0; i < asteroid.size(); i++) {
                Asteroid_Entity current = asteroid.get(i);
                if (current == null) continue;
                
                for (int j = i + 1; j < asteroid.size(); j++) {
                    Asteroid_Entity other = asteroid.get(j);
                    if (other == null) continue;
                    Rectangle r1 = new Rectangle(current.getFrom().getX(),
                                                current.getFrom().getY(),
                                                current.getFrom().getWidth() - 20,
                                                current.getFrom().getHeight() - 20
                    
                    );
                    Rectangle r2 = new Rectangle(other.getFrom().getX(),
                                                other.getFrom().getY(),
                                                other.getFrom().getWidth() - 20,
                                                other.getFrom().getHeight() - 20);
                    if (r1.intersects(r2)) {
                        // สุ่มเลือกตัวที่จะลบ
                        Asteroid_Entity removeTarget = 
                            (Math.random() > 0.5) ? current : other;
                        toRemove.add(removeTarget);
                        break; // ลบแค่หนึ่งคู่ต่อการชน
                    }
                }
            }
            
            // ลบทีหลังทั้งหมด
            for (Asteroid_Entity removeTarget : toRemove) {
                removeAsteroid(removeTarget);
            }
        }
    }
    private void removeAsteroid(Asteroid_Entity asteroid) {
        asteroid.getBahavior().giveTerminetPill();
        playExplotionSound();
        new Thread(() -> {
            try{
                asteroid.getFrom().getExplotionIcon();
                sleep(700); // หน่วงเวลา 500 มิลลิวินาที
                frame.remove(asteroid.getFrom());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        this.asteroid.remove(asteroid); // ลบโดยใช้ object reference
        frame.repaint();
    }
    void playExplotionSound(){
        try {
            File file = new File(System.getProperty("user.dir")+File.separator+"resource"+File.separator+"Explotion_sound.wav");
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            clip.start();
            // ไม่ต้องปิดทันที รอให้เล่นจบเอง
        } catch (Exception e) {
            System.out.println("เสียง error: " + e.getMessage());
        }
    }
}
