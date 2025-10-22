import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/* อธิบายโครงสร้างโค๊ดของผม ผมรู้แหละไม่มีใครอ่านหรอก แม้แต่ผมเอง

    class
    - maingame  ( เก็บข้อมูล และควบคุม component และ action ต่างๆ )
    - panelGUI  ( เน้นไปที่การแสดงผล กำหนดการแสดงผล บันได และ สไลด์เดอร์ )
    - My_Thread ( กำหนดเงื่อนไขเกม บันได ช่องการเดิน เงื่อนไขการชนะ )
    - newgame   ( ใช้รันโปรแกรม )

    - จุดที่อาจสงสัย
    panelGUI หรือ gui   คือตัวแปรสำหรับ gui โดยมีการกำหนดขนาด panel และใช้ในการส่ง setter getter เข้าไปยัง gui
    ผมใช้ขนาดหน้าจอ music และอื่นๆ เช่น การเปิด dev mode จากหน้า setting menu -ตอนแรกจะทำ command แต่มันอธิบายยาก

    **ไฟล์ save คือ setting.properties

    ชื่อตัวแปรที่ควรรู้
    - moveX[array] = ตำแหน่ง x ของ ตำแหน่่ง player สามารถเคลื่อนที่ไปได้
    - moveY[array] = เหมือนกัน แค่เป็นตำแหน่ง y
    - player       = ตัวผู้เล่น
    - index        = ตำแหน่งที่ player อยู่ตอนนี้
    - finish       = ตรวจสอบจบเกม
    - press        = ใช้ตรวจสอบการกด space bar(keyListener)
    - running      = ตรวจสอบการทำงานการย้ายตำแหน่ง player

    dev mode
    - save         = เปิดโหมด auto จนกว่า จะถึงตำแหน่งที่กำหนด หากใส่ 100 คือให้ run auto จนจบได้เช่นกัน

 */

class mainGame extends JFrame {
    Properties setting = new Properties();
    FileInputStream input;
    int widthPOS, heightPOS;

    {
        try {
            input = new FileInputStream("setting.properties");
            setting.load(input);

            widthPOS  = Integer.parseInt(setting.getProperty("setWidth", "1260"));
            heightPOS = Integer.parseInt(setting.getProperty("setHeight", "680"));
        } catch (IOException e) {
            System.exit(1);
        }
    }

    //option อื่นๆ เผื่ออยากเพิ่ม
    ImageIcon fullmap = new ImageIcon("assets\\show_fullmaps.png");
    Image scaled = fullmap.getImage().getScaledInstance(200, 60, Image.SCALE_SMOOTH);
    JLabel showmap = new JLabel("Show full maps");
    JButton btnmap = new JButton();


    //ตัวแปรคำนวนตำแหน่งและการทำงาน
    private boolean press;
    int roll, index = 0;
    int save  = 1;     //devs mode สร้างเสร็จลบได้เลย ขี้เกียจอธิบาย
    //boolean my_tern = true //รอการอัพเดท
    boolean auto = true;
    boolean running = false;

    public mainGame() {
        setSize(widthPOS,heightPOS);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setLayout(null);

        panelGUI guiPanel = new panelGUI();
        guiPanel.setBounds(0, 0, getWidth(), getHeight());
        add(guiPanel);

        //ทำบน mainframe เพราะมันทำแค่ครั้งเดียว จะได้ไม่เปลืองทรัพยากรด้วย
        //================================================================================================

        int[] moveX = new int[101];
        int[] moveY = new int[101];

        for(int i = 0; i < moveX.length; i++) {
            if(i == 0) {
                moveX[i] = 300;
                moveY[i] = 570;
            } else if (i == 1) {
                moveX[i] = moveX[(i-1)] + 90;
                moveY[i] = moveY[(i-1)] + 17;
            } else if (i % 10 == 1) {
                moveY[i] = moveY[(i-1)] - 58;
                moveX[i] = moveX[i] + moveX[(i-1)];
            } else if (i % 20 >= 1 && i % 20 <= 10) {
                moveX[i] = moveX[(i-1)] + 51;
                moveY[i] = moveY[(i-1)];
            } else if (i % 20 >= 11 && i % 20 <= 20 || i % 20 == 0) {
                moveX[i] = moveX[(i-1)] - 51;
                moveY[i] = moveY[(i-1)];
            }
            System.out.println("[ " +(i+1)+ " ] position \nmoveX: "+moveX[i]+"\nmoveY: "+moveY[i]);
        }

        guiPanel.setpositionGUI(moveX, moveY);

        // ================================================================================================
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE && !press && index != 100 && auto && index < save && !running) {
                    press = true;
                    new Thread(() -> {
                        while (index < save) {
                            roll = (int) (Math.random() * 6 + 1);

                            My_Thread thread = new My_Thread(roll, guiPanel, index, running);
                            thread.start();

                            try {
                                thread.join();
                                index = thread.getIndex();
                                running = thread.getrunning();
                            } catch (InterruptedException ex) {}

                            // ถ้าถึงจุด save แล้วก็หยุด
                            if (index >= save) {
                                auto = false;
                                System.out.println("== Auto stopped at index " + index + " ==");
                                break;
                            }

                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException ex) {}
                        }
                    }).start();
                    press = false;
                    System.out.println("try / catch : running = "+running+"\tpress = "+press);
                }


                else if (e.getKeyCode() == KeyEvent.VK_SPACE && index != 100 && !press && !auto && index < 100 && running) {
                    press = true;
                    running = false;
                    roll = (int) (Math.random() * 6 + 1);

                    My_Thread thread = new My_Thread(roll, guiPanel, index, running);
                    thread.start();

                    new Thread(() -> {
                        try {
                            thread.join();
                            index = thread.getIndex();
                            running = thread.getrunning();
                        } catch (InterruptedException ex) {}
                    }).start();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) press = false;
            }
        });
        setFocusable(true);     //บอกให้ Event มันโฟกัสคำสั่งที่ส่งเข้ามา หากไม่ใส่ Event จะมองไม่เห็น ทำให้ไม่ทำงาน
        requestFocusInWindow(); //ใช้เพื่อให้ window สามารถโฟกัสได้ หากมี Jcomponent มันจะใส่ใจอันนั้นก่อนจึงต้องใช้

    }
}



/*=====================================================================================================================
                                                    GUI
//=====================================================================================================================*/



class panelGUI extends JPanel {
    int index, setpoint;
    int[] moveX;
    int[] moveY;

    boolean running;
    boolean finish;

    public void setpositionGUI(int[] moveX, int[] moveY) {
        this.moveX = moveX;
        this.moveY = moveY;
    }

    public void setGUI(boolean finish, int index, boolean running) {
        this.finish = finish;
        this.index = index;
        this.running = running;
        repaint();
    }

    ImageIcon bg = new ImageIcon("assets\\bg_game1.jpg");
    ImageIcon winner = new ImageIcon("assets\\logo_menu.png");
    ImageIcon player = new ImageIcon("assets\\player.png");

    // ladders
    ImageIcon ladder[] = new ImageIcon[9];
    int[] ladder_start = {1, 4, 9, 21, 28, 36, 51, 61, 71};
    int[] ladder_end   = {38, 14, 31, 42, 84, 44, 67, 81, 91};

    // chutes
    ImageIcon chute[] = new ImageIcon[10];
    int[] chute_start = {16, 47, 49, 59, 62, 64, 84, 93, 95, 98};
    int[] chute_end    = {6, 26, 11, 44, 2, 41, 21, 73, 75, 63};

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(index > 0 && index <= 100) {
            setpoint = index;
        }else {
            setpoint = 0;
        }


        for(int i = 0; i < 9; i++) {
            ladder[i] = new ImageIcon("assets\\new(ps)\\ladder\\ladder_"+(i+1)+".png");
        }
        for(int i = 0; i < 10; i++) {
            chute[i] = new ImageIcon( "assets\\new(ps)\\chute\\chute_"+(i+1)+".png");
        }

        g.drawImage(bg.getImage(), 0, 0, getWidth(), getHeight(), this);

        //calculator ช่วงการมองเห็น
        int start = Math.max(0, index - 6);
        int end = Math.min(100, index + 6);

        //แสดงบันไดที่มองเห็น
        for (int i = 0; i < ladder_start.length; i++) {
            int s = ladder_start[i];
            int e = ladder_end[i];

            if ((s >= start && s <= end || setpoint == e && !running)) {
                g.drawImage(ladder[i].getImage(), 0, 0, getWidth(), getHeight(), this);
                if(setpoint >= 21 && setpoint <= 34) {
                    g.drawImage(chute[6].getImage(), 0, 0, getWidth(), getHeight(), this);
                }
            }
        }

        //สไลด์เดอร์
        for (int i = 0; i < chute_start.length; i++) {
            int s = chute_start[i];
            int e = chute_end[i];

            if ((s >= start && s <= end)) {
                g.drawImage(chute[i].getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        }

        g.drawImage(player.getImage(), moveX[setpoint], moveY[setpoint], 20, 32, this);


        if(finish) {
            g.drawImage(winner.getImage(), getWidth()/2 - 234, 100, 468, 246, this);
        }

        g.setColor(Color.white);
        g.drawString("VISIBLE RANGE: " + start + " → " + end, 20, 20);
    }
}



/*=====================================================================================================================
                                                    Thread
//=====================================================================================================================*/


class My_Thread extends Thread {
    Font turnPress;
    {
        try {
            turnPress = Font.createFont(Font.TRUETYPE_FONT, new File("CheeseRegular.ttf"));
            turnPress = turnPress.deriveFont(Font.BOLD, 24f);
        } catch (FontFormatException|IOException e){
            turnPress = new Font("myfont", Font.BOLD, 20);
        }
    }

    JLabel pressTurn = new JLabel("(Your turn) Press - Space bar - for move");


    panelGUI gui;

    // ladders
    int[] ladder_start = {1, 4, 9, 21, 28, 36, 51, 61, 71};
    int[] ladder_end   = {38, 14, 31, 42, 84, 44, 67, 81, 91};

    //chute
    int[] chute_start  = {16, 47, 49, 59, 62, 64, 84, 93, 95, 98};
    int[] chute_end    = {6, 26, 11, 44, 2, 41, 21, 73, 75, 63};

    //-------------
    int roll;
    //-------------
    boolean finish = false;
    boolean running;
    int index, limit, cal;

    public My_Thread (int roll, panelGUI gui, int index, boolean running) {
        this.roll = roll;
        this.gui  = gui;
        this.index = index;
        this.running = running;
    }

    public void run() {
        cal = index + roll;
        for(int i=0; i<roll; i++) {

            //loop สำหรับตรวจสอบการเดิน (เสร็จแล้ว) แก้ใหม่เป็นอาเรย์เพื่อความง่ายต่อการทำบันได
            if(cal == 100 && index == 99) {
                System.out.println("U winner nigga now get out in the here!");
                finish = true;
                index++;
                gui.setGUI(finish, index, running);
            }
            else if(cal > 100) {
                if (limit != 0) {
                    limit--;
                    index--;
                } else if (index == 100) {
                    limit = cal - (index);
                    index--;
                } else if (index < 100 && limit == 0) {
                    index++;
                }
                gui.setGUI(finish, index, running);
            }else {
                index++;
                gui.setGUI(finish, index, running);
            }

            if(i == (roll-1)) {
                boolean moved = false;

                for (int j = 0; j < ladder_start.length; j++) {
                    if (index == ladder_start[j]) {
                        index = ladder_end[j];
                        moved = true;
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {}
                        gui.setGUI(finish, index, running);
                        break;
                    }
                }
                if(index == 84) {
                    moved = false;
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException e) {}
                }
                if(!moved) {
                    for (int j = 0; j < chute_start.length; j++) {
                        if (index == chute_start[j]) {
                            index = chute_end[j];
                            gui.setGUI(finish, index, running);
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {}
                        } else if (index == ladder_start[3]) {
                            index = ladder_end[3];
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {}
                        }
                    }
                }
            }

            gui.setGUI(finish, index, running);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
            }
            running = true;
        }
        System.out.println("=====================================================");
        System.out.println("roll: "+roll+"\tindex: "+index+"\t finish: "+finish);
        System.out.println("cal: "+cal+"\tlimit: "+limit+"\n");
    }
    public int getIndex() {
        return index;
    } public boolean getrunning() {
        return  running;
    }
}


/*=====================================================================================================================
                                                    main
//=====================================================================================================================*/


class newgame {
    public static void main(String[] args) {
        mainGame mygame = new mainGame();
        mygame.setVisible(true);
    }
}