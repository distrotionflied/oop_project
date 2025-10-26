
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
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

    Layer layout number
    - 0            = ส่วนของ GUI
    - 1            = ปุ่ม กับ หัวใจ (setHeart)
    - 2            = Heart  drawing
    - 5            = pop-up "UP - DOWN" เวลาขึ้นหรือตกช่องที่กำหนดลูกศรไว้
    - 6            = แสดง From - To
    - 10           = finish จบเกมเท่านั้น
 */

class mainGame extends JFrame {
    private String playerName;
    private boolean isHost;
    private ObjectOutputStream gameOut;
    menu_frame menu = new menu_frame();

    JLayeredPane myLayer = new JLayeredPane();
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

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                        Attribute                                                 //
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    ImageIcon winner = new ImageIcon("assets\\logo.png");

    public boolean press;
    int roll, index = 0;
    int save  = 1;

    int total_heart , heartX;
    //boolean my_tern = true //รอการอัพเดท
    boolean auto = true;
    boolean running = false;

    String mode_roll = "NORMAL";   //ค่าเริ่มต้นการทอยลูกเต๋า

    boolean animation; //ไว้เพื่อแสดงผล pop-up โดยเฉพาะ
    boolean up_play, down_play;
    int     newindex;

    boolean endgame;
    boolean hidetext = false;

    public Font myfontsize, myFontsmall;

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                          Setter                                                  //
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    //      จะเริ่มทำการย้ายตำแหน่ง setter เพื่อง่ายต่อการอธิบายโค๊ด

    public void setFinish(boolean endgame) {
        this.endgame = endgame;
        CheckFinish();
    }

    public void setAnimation(boolean animation, boolean up_play, boolean down_play, int newindex) {
        this.animation = animation;
        this.up_play   = up_play;
        this.down_play = down_play;
        this.newindex  = newindex;
        popUp_Drawing();
    }

    public void setHeart(int total_heart) {
        this.total_heart = total_heart;
        System.out.println("setter ฝั่ง mainGame ทำงาน.. "+ total_heart);
        HeartUpdate();
        repaint();
    }

    public void sethide(boolean hidetext) {
        this.hidetext = hidetext;
        HeartUpdate();
    }

    //==================================================================================================//
    //                                         Finish Game                                              //
    //==================================================================================================//
    //for used
    public void CheckFinish() {
        panelGUI guiPanel = new panelGUI();
        guiPanel.setBounds(0, 0, widthPOS, heightPOS);
        myLayer.add(guiPanel, Integer.valueOf(0));

        try {
            myfontsize = Font.createFont(Font.TRUETYPE_FONT,new File("CheeseRegular.ttf"));
            myfontsize = myfontsize.deriveFont(Font.BOLD, 17);
        } catch (FontFormatException|IOException e) {
            myfontsize = new Font("mid", Font.BOLD, 17);
        }

        int xpos  = -10;
        String[] name = {"SPECTATOR" , "BACK TO MENU"};
        JButton[] btn = new JButton[2];

        JLabel logo_win = new JLabel(winner);
        logo_win.setVisible(true);
        logo_win.setBounds(widthPOS/2-256, 0,512,512);
        myLayer.add(logo_win, Integer.valueOf(10));

        for(int i=0; i<btn.length; i++) {
            btn[i] = new JButton(name[i]);
            btn[i].setFont(myfontsize);
            btn[i].setBounds((widthPOS/2-170)+xpos, heightPOS/2+50, 170, 40);

            btn[i].setForeground(Color.white);
            btn[i].setBackground(new Color(255,255,255,100));
            btn[i].setOpaque(false);
            btn[i].setFocusPainted(false);
            myLayer.add(btn[i], Integer.valueOf(10));
            xpos += 190;
        }
        btn[0].addActionListener(spectator ->{
            logo_win.setVisible(false);
        });
        btn[1].addActionListener(back_menu -> {
            menu.setVisible(true);

            Timer mytime = new Timer(1000, el-> {
                dispose();
            });
            mytime.start();
        });


    }



    //ติดปัญหานี้ 1วัน กับการหาวิธีเริ่ม gif ใหม่แบบสามารถอธิบายง่ายๆ
    //==================================================================================================//
    //                                  UP / DOWN pop-up (Animation                                     //
    //==================================================================================================//
    //for used
    public void popUp_Drawing () {
        Component[] comps = myLayer.getComponentsInLayer(5);

        for (int i = 0; i < comps.length; i++) {
            myLayer.remove(comps[i]);
            myLayer.revalidate();
            myLayer.repaint();
            System.gc(); //ช่วยเก็บค่าที่ remove ทิ้ง เพื่อลบ แคชที่ไมไ่ด้ใช้
        }

        // ladders
        ImageIcon ladder[] = new ImageIcon[9];
        int[] ladder_start = {1, 4, 9, 21, 28, 36, 51, 61, 71};
        int[] ladder_end   = {38, 14, 31, 42, 84, 44, 67, 81, 91};

        // chutes
        ImageIcon chute[] = new ImageIcon[10];
        int[] chute_start = {16, 47, 49, 59, 62, 64, 84, 93, 95, 98};
        int[] chute_end    = {6, 26, 11, 44, 2, 41, 21, 73, 75, 63};

        int num_forup = 403 , num_fordown = 403;
        JLabel[] Image_up  = new JLabel[ladder_end.length];
        JLabel[] Image_down = new JLabel[chute_end.length];

        for(int i=0; i<ladder_end.length; i++) {
            ladder[i] = new ImageIcon("assets\\upper-down\\ladder_"+ladder_start[i]+".gif");
            Image_up[i]  = new JLabel(ladder[i]);
            Image_up[i].setBounds(widthPOS/2-300,heightPOS/2+100,600,250);
        } for(int i=0; i<chute_end.length; i++) {
            chute[i] = new ImageIcon("assets\\upper-down\\down_"+chute_start[i]+".gif");
            Image_down[i] = new JLabel(chute[i]);
            Image_down[i].setBounds(widthPOS/2-300,heightPOS/2+100,600,250);
        }
        System.out.println("index="+index+" newIndex="+newindex+" up_play="+up_play+" down_play="+down_play);


        for(int i=0; i<ladder_end.length; i++) {
            if(ladder_end[i] == newindex) {
                num_forup = i;
                break;
            } else {
                num_forup = 404;
            }
        }

        for(int i=0; i<chute_end.length; i++) {
            if(chute_end[i] == newindex) {
                num_fordown = i;
                break;
            } else {
                num_fordown = 404;
            }
        }

        System.out.println("num_up=" + num_forup + " num_down=" + num_fordown);

        Timer delay;

        int widthSize = 1260, heightSize = 680;
        ImageIcon upper = new ImageIcon("assets\\UFO_UP(resize).gif");
        ImageIcon down  = new ImageIcon("assets\\UFO_DOWN(resize).gif");

        JLabel up_text   = new JLabel(upper);
        JLabel down_text = new JLabel(down);

        if(animation) {
            if(up_play) {
                up_text.setBounds(widthSize/2-300, heightSize/2-125, 600, 250);

                myLayer.add(up_text, Integer.valueOf(5));
                if(num_forup >= 0 && num_forup <= (ladder_end.length-1)) {
                    myLayer.add(Image_up[num_forup], Integer.valueOf(5));
                    System.out.println("up ทำงาน!!");
                }

                int finalNum_forup = num_forup;
                delay = new Timer(2350, mytime -> {
                    up_text.setVisible(false);
                    Image_up[finalNum_forup].setVisible(false);
                    animation = false;
                    up_play = false;
                });


                delay.start();

            } else if(down_play) {
                down_text.setBounds(widthSize/2-300, heightSize/2-125, 600, 250);

                myLayer.add(down_text, Integer.valueOf(5));
                if(num_fordown >= 0 && num_fordown <= (chute_end.length-1)) {
                    myLayer.add(Image_down[num_fordown], Integer.valueOf(5));
                    System.out.println("down ทำงาน!!");
                }

                int finalNum_fordown = num_fordown;
                delay = new Timer(2350, mytime -> {
                    down_text.setVisible(false);
                    Image_down[finalNum_fordown].setVisible(false);
                    animation = false;
                    down_play = false;
                });
                delay.start();
            }
        }
        revalidate();
        repaint();
    }

    //==================================================================================================//
    //                                       Heart GUI drawing                                          //
    //==================================================================================================//
    //for used
    public void HeartUpdate() {
        if(total_heart % 3 == 0 && total_heart != 0) {
            heartX = total_heart/3;
        }

        Component[] comps = myLayer.getComponentsInLayer(2);
        for (int i = 0; i < comps.length; i++) {
            myLayer.remove(comps[i]);
            myLayer.revalidate();
            myLayer.repaint();
            System.gc();
        }

        //==================================================================================================//
        //                                     Add Component for GUI                                        //
        //==================================================================================================//
        ImageIcon heart   = new ImageIcon("assets\\myheart.gif");
        ImageIcon black_heart = new ImageIcon("assets\\nigheart.png");
        ImageIcon select   = new ImageIcon("assets\\arrow_select.gif");

        myFontsmall = new Font("small", Font.BOLD, 16);
        try {
            myfontsize = Font.createFont(Font.TRUETYPE_FONT,new File("CheeseRegular.ttf"));
            myfontsize = myfontsize.deriveFont(Font.BOLD, 20);
        } catch (FontFormatException|IOException e) {
            myfontsize = new Font("mid", Font.BOLD, 20);
        }

        ///////////////////////////////////////////////

        String[] btn_namelist = {"EVEN", "ODD", "CENCER"};
        JButton[] Even = new JButton[3];
        JLabel downX;
        if(heartX == 3) {
            downX = new JLabel("Limit");
        } else {
            downX = new JLabel("Down x"+(total_heart%3));
        }

        downX.setBounds(280,300,130,40);
        downX.setFont(myFontsmall);
        downX.setForeground(Color.GRAY);
        myLayer.add(downX, Integer.valueOf(2));

        int ybtn = 680/2+65;

        for(int i=0; i<Even.length; i++) {
            Even[i] = new JButton(""+btn_namelist[i]);
            Even[i].setBounds(55,ybtn,110,40);

            Even[i].setFont(myfontsize);
            Even[i].setForeground(Color.white);
            Even[i].setOpaque(false);
            Even[i].setBorderPainted(false);
            Even[i].setFocusPainted(false);
            Even[i].setFocusable(false);       //สำคัญมากๆ ไม่ใส่พังทั้งเกมของจริง เพราะ space bar จะไปโฟกัสปุ่มทันที
            Even[i].setBackground(new Color(0,0,0,0));

            Even[i].setHorizontalAlignment(SwingConstants.LEFT);

            if(i==2) {
                Even[i].setFont(myFontsmall);
                Even[i].setBounds(230,ybtn-40,110,40);
            }
            myLayer.add(Even[i], Integer.valueOf(1));
            ybtn += 40;
        }

        ///////////////////////////////////////////////

        JLabel[] total = new JLabel[3];     //หัวใจแดง
        JLabel[] total_ = new JLabel[3];    //หัวใจเทา
        int xbtn = 110, xbtn2 = 250;
        for(int i=0; i<heartX; i++) {
            total[i] = new JLabel(heart);
            total[i].setBounds(xbtn, 680/2+20, 40,40);

            myLayer.add(total[i], Integer.valueOf(1));
            xbtn += 70;

        }for(int j=0; j<(total_.length) - heartX; j++) {
            total_[j] = new JLabel(black_heart);
            total_[j].setBounds(xbtn2, 680/2+20, 40,40);

            myLayer.add(total_[j], Integer.valueOf(2));
            xbtn2 -= 70;
        }

        /////////////////////////////////////////////// ปุ่มสำหรับเลือก คู่คี่

        JLabel arrow = new JLabel(select);
        JLabel alert = new JLabel("Heart required.");
        alert.setForeground(Color.red);
        alert.setFont(myFontsmall);
        if(hidetext) {
            arrow.setVisible(false);
        }

        Even[0].addActionListener(even -> {
            if(heartX > 0) {
                mode_roll = "EVEN";
                arrow.setVisible(true);
                arrow.setBounds(90,680/2+45,80,65);
                myLayer.add(arrow, Integer.valueOf(2));
                repaint();
            } else {
                alert.setVisible(true);
                alert.setBounds(210,680/2+70,150,40);
                Timer delay = new Timer(2000, ch_delay -> {
                    alert.setVisible(false);
                });
                delay.start();
                myLayer.add(alert, Integer.valueOf(2));
            }
        });

        Even[1].addActionListener(odd -> {
            if(heartX > 0) {
                mode_roll = "ODD";
                arrow.setVisible(true);
                arrow.setBounds(90, 680 / 2 + 85, 80, 65);
                myLayer.add(arrow, Integer.valueOf(2));
                repaint();
            } else {
                alert.setVisible(true);
                alert.setBounds(210,680 /2+70,150,40);
                Timer delay = new Timer(2000, ch_delay -> {
                    alert.setVisible(false);
                });
                delay.start();
                myLayer.add(alert, Integer.valueOf(2));
            }
        });

        Even[2].addActionListener(Cancel -> {
            arrow.setVisible(false);
            mode_roll = "NORMAL";
            System.out.println("Mode set to Normal mode (Cancel)");
        });

        revalidate();
        repaint();

        SwingUtilities.invokeLater(() -> {
            requestFocusInWindow();     //เพื่อแน่ใจ 100% ว่า space bar สามารถกดได้ ไม่ถูก Even[] ทับไป
            //** ปัญหาที่เกิดจากการ add แบบ Layer -.- โคตรไม่สนุก
        });
    }

    //==================================================================================================//
    //                                       mainGame Running                                           //
    //==================================================================================================//

    public mainGame() {
        this.playerName = playerName;
        this.isHost = isHost;
        
        setupNetwork(); // ← เพิ่มการเชื่อมต่อ network
        setSize(widthPOS,heightPOS);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setLayout(null);

        myLayer.setBounds(0,0,getWidth(),getHeight());
        add(myLayer);

        panelGUI guiPanel = new panelGUI();
        guiPanel.setBounds(0, 0, getWidth(), getHeight());
        myLayer.add(guiPanel, Integer.valueOf(0));

        //ทำบน mainframe เพราะมันทำแค่ครั้งเดียว จะได้ไม่เปลืองทรัพยากรด้วย
        //================================================================================================

        int[] moveX = new int[101];
        int[] moveY = new int[101];

        int plus = 56;
        int plusy = 61;
        int yfor = 1;

        for(int i = 0; i < moveX.length; i++) {
            if(yfor == 5) {
                plusy = 63;
            }

            if(i == 0) {
                moveX[i] = 300;
                moveY[i] = 570;
            } else if (i == 1) {
                moveX[i] = moveX[(i-1)] + 120;
                moveY[i] = moveY[(i-1)] + 48;
            } else if (i % 10 == 1 && i != 100) {
                moveY[i] = moveY[(i-1)] - plusy;
                moveX[i] = moveX[i] + moveX[(i-1)];
                yfor++;
            } else if (i % 20 >= 1 && i % 20 <= 10) {
                moveX[i] = moveX[(i-1)] + plus;
                moveY[i] = moveY[(i-1)];
            } else if (i % 20 >= 11 && i % 20 <= 20 || i % 20 == 0) {
                moveX[i] = moveX[(i-1)] - plus;
                moveY[i] = moveY[(i-1)];
            }
            System.out.println("[ " +(i+1)+ " ] position \nmoveX: "+moveX[i]+"\nmoveY: "+moveY[i]);
        }

        guiPanel.setpositionGUI(moveX, moveY);
        HeartUpdate();

        // ================================================================================================
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE && !press && index != 100 && auto && index < save && !running) {
                    press = true;
                    new Thread(() -> {
                        while (index < save) {
                            roll = (int) (Math.random() * 6 + 1);

                            My_Thread thread = new My_Thread(roll, guiPanel, index, running, myLayer, mainGame.this, total_heart, mode_roll);
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
                    System.out.println("try / catch : running = "+running+"\tpress = "+press);
                    press = false;
                }


                else if (e.getKeyCode() == KeyEvent.VK_SPACE && index != 100 && !press && !auto && index < 100 && running && !animation) {
                    press = true;
                    running = false;
                    if(mode_roll.equals("EVEN")) {
                        int[] evenNum = {2, 4, 6};
                        roll = evenNum[(int)(Math.random() * 3)];
                    }
                    else if(mode_roll.equals("ODD")) {
                        int[] oddNum = {1, 3, 5};
                        roll = oddNum[(int)(Math.random() * 3)];
                    }
                    else {
                        roll = (int)(Math.random() * 6 + 1);
                    }

                    // หลังจากทอยเต๋าและเคลื่อนที่เสร็จ ในส่วนที่ index ถูกอัปเดต
                    try {
                        if (gameOut != null) {
                            gameOut.writeObject("MOVE:" + playerName + ":" + index);
                            gameOut.flush();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    My_Thread thread = new My_Thread(roll, guiPanel, index, running, myLayer, mainGame.this, total_heart, mode_roll);
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

    // เพิ่มเมธอดสำหรับตั้งค่า multiplayer
    public void setupMultiplayer(String playerName, boolean isHost) {
        this.playerName = playerName;
        this.isHost = isHost;
        setupNetwork();
    }

    private void setupNetwork() {
        try {
            Socket socket = new Socket("localhost", 8888);
            gameOut = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Cannot connect to server for game updates");
        }
    }
}




/*=====================================================================================================================
                                                    GUI(panel)
//=====================================================================================================================*/




class panelGUI extends JPanel {
    Font myfont;
    {
        try {
            myfont = Font.createFont(Font.TRUETYPE_FONT, new File("CheeseRegular.ttf"));
            myfont = myfont.deriveFont(Font.BOLD, 24f);
        } catch (FontFormatException|IOException e){
            myfont = new Font("myfont", Font.BOLD, 20);
        }
    }

    int index, setpoint;
    int[] moveX;
    int[] moveY;

    boolean running;
    boolean finish;
    boolean show_logo = false;

    public void setFont(Font myfont) {
        this.myfont = myfont;
    }

    public void setpositionGUI(int[] moveX, int[] moveY) {
        this.moveX = moveX;
        this.moveY = moveY;
    }

    public void setGUI(boolean finish, int index, boolean running) {
        this.finish  = finish;
        this.index   = index;
        this.running = running;
        repaint();
    }

    ImageIcon bg = new ImageIcon("assets\\newmap.png");
    ImageIcon player    = new ImageIcon("assets\\player.png");
    ImageIcon arrow_up  = new ImageIcon("assets\\up.gif");
    ImageIcon arrow_down= new ImageIcon("assets\\down.gif");
    ImageIcon card_bg  = new ImageIcon("assets\\Card X5.png");

    // ladders
    ImageIcon ladder[] = new ImageIcon[9];
    int[] ladder_start = {1, 4, 9, 21, 28, 36, 51, 61, 71};
    int[] ladder_end   = {38, 14, 31, 42, 84, 44, 67, 81, 91};

    // chutes
    ImageIcon chute[] = new ImageIcon[10];
    int[] chute_start = {16, 47, 49, 59, 62, 64, 84, 93, 95, 98};
    int[] chute_end    = {6, 26, 11, 44, 2, 41, 21, 73, 75, 63};

    //ทำกระพริบข้อความ press
    boolean show_text = true;
    Timer delay1 , delay2;

    public panelGUI() {
        delay1 = new Timer(700, sleep-> {
            if(!running && !finish) {
                show_text = !show_text;
                repaint();
            } else if(running) {
                show_text = false;
            }
        });
        delay1.start();
    }

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

        g.drawImage(bg.getImage(), 0, 0, bg.getIconWidth(), getHeight(), this);

        g.drawImage(card_bg.getImage(), 50, getHeight()/2, 300, 170, null);

        int ypos = 22;
        for(int i=0; i<ladder_start.length; i++) {
            if(i >= 7) {
                ypos += 2;
            }
            g.drawImage(arrow_up.getImage(), moveX[ladder_start[i]]+10, moveY[ladder_start[i]]-ypos, 25,50, null);
        }
        for(int i=0; i<chute_start.length; i++) {
            if(i >= 7) {
                ypos += 2;
            }
            g.drawImage(arrow_down.getImage(), moveX[chute_start[i]]+10, moveY[chute_start[i]]-ypos, 25,50, null);
        }

        g.drawImage(player.getImage(), moveX[setpoint], moveY[setpoint]-5, 20, 32, this);

        if(show_text && !running && !finish) {


        }
        running = false;
    }
}



/*=====================================================================================================================
                                                    Thread
//=====================================================================================================================*/


class My_Thread extends Thread {
    panelGUI gui;
    mainGame game;

    // ladders
    int[] ladder_start = {1, 4, 9, 21, 28, 36, 51, 61, 71};
    int[] ladder_end   = {38, 14, 31, 42, 84, 44, 67, 81, 91};

    //chute
    int[] chute_start  = {16, 47, 49, 59, 62, 64, 84, 93, 95, 98};
    int[] chute_end    = {6, 26, 11, 44, 2, 41, 21, 73, 75, 63};

    //{16, 47, 49, 59, 62, 64, //84, //93, //95, //98};

    //-------------
    int roll;
    //-------------
    boolean ladder_run = false;
    //-------------
    boolean finish = false;
    boolean running;
    JLayeredPane myLayer;
    int index, limit, cal;
    //-------------
    boolean animation = false;
    boolean upper     = false;
    boolean downer    = false;
    //-------------
    int heart;
    //-------------
    String mode_roll;
    //-------------

    public My_Thread (int roll, panelGUI gui, int index, boolean running, JLayeredPane myLayer, mainGame game, int heart, String mode_roll) {
        this.roll = roll;
        this.gui  = gui;
        this.index = index;
        this.running = running;
        this.myLayer = myLayer;
        this.game = game;
        this.heart = heart;
        this.mode_roll = mode_roll;
    }

    public void run() {
        //--------------------------------
        //เช็คการทำงานลูกเเต๋าก่อนเลย
        if (!mode_roll.equals("NORMAL")) {
            if (heart >= 3) {
                heart-=3; // ใช้หัวใจ 1 ดวงเพื่อกำหนดการทอยลูกเต๋าเอง หรือก็คือ 3 heart(ชื่อตัวแปร) เพราะเอาไปแปลงใหม่อยู่ดี
                game.setHeart(heart);
            } else {
                mode_roll = "NORMAL";
            }
        }

        cal = index + roll;

        //loop สำหรับตรวจสอบการเดิน (เสร็จแล้ว) แก้ใหม่เป็นอาเรย์เพื่อความง่ายต่อการทำบันได
        //==============================================================================================//
        //                                      UPPER AND DOWN                                          //
        //==============================================================================================//

        for(int i=0; i<roll; i++) {
            if(cal == 100 && index == 99) {
                System.out.println("U winner nigga now get out in the here!");
                finish = true;
                index++;
                gui.setGUI(finish, index, running);
                game.setFinish(true);
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
                    animation = false;
                    upper = false;
                    downer = false;

                    if (index == ladder_start[j]) {
                        index = ladder_end[j];
                        ladder_run = true;
                        moved = true;

                        if (ladder_start[j] == 28 && ladder_end[j] == 84) {
                            // ────────────── ขึ้น 28 -> 84 ──────────────
                            game.setAnimation(true, true, false, index);
                            gui.setGUI(finish, 84, running);
                            try {
                                Thread.sleep(2500);
                            } catch (InterruptedException e) {
                            }

                            // ────────────── ตก 84 -> 21 ──────────────
                            index = 21;
                            game.setAnimation(true, false, true, index);
                            gui.setGUI(finish, index, running);
                            if(heart < 10) {
                                heart++;
                            }
                            try {
                                Thread.sleep(2500);
                            } catch (InterruptedException e) {
                            }

                            // ────────────── ขึ้น 21 -> 42 ──────────────
                            index = 42;
                            game.setAnimation(true, true, false, index);
                            gui.setGUI(finish, index, running);
                            try {
                                Thread.sleep(2500);
                            } catch (InterruptedException e) {
                            }

                            // อัปเดตสุดท้ายและออกจากลูปบันได
                            gui.setGUI(finish, index, running);
                            System.out.println("super combo 28→84→21→42 ทำงานครบ");
                            break;
                        }

                        // ────────────── เคสบันไดทั่วไป ──────────────
                        System.out.println("ระบบขึ้นบันได ทำงาน..");
                        game.setAnimation(true, true, false, index);
                        gui.setGUI(finish, index, running);
                        try {
                            Thread.sleep(2350);
                        } catch (InterruptedException e) {
                        }
                        break;
                    }
                }


                if(index == 84) {
                    moved = false;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {}
                }
                if(!moved) {
                    for (int j = 0; j < chute_start.length; j++) {
                        if (index == chute_start[j]) {
                            index = chute_end[j];
                            if(heart < 10) {
                                heart++;
                                SwingUtilities.invokeLater(() -> game.setHeart(heart)); // Swing ไม่อนุญาติให้ใครแก้ UI เพราะงั้นวิธีนี้ชัวร์สุด + ท้ายบรรทัด
                                // ไม่ทำแบบนี้ ปัญหาโง่ๆนี่จะอธิบ่ายตอน Pre ยากขึ้นมากๆ
                                // แต่เพราะ Thread จะเอาไว้คำนวนเลยไม่อยากให้มันยุ่งกันได้
                            }

                            animation = true;
                            upper     = false;
                            downer    = true;

                            gui.setGUI(finish, index, running);
                            System.out.println("ตกบันไดทำงาน.. "+heart);

                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {}
                        } else if (index == ladder_start[3]) {
                            index = ladder_end[3];

                            animation = true;
                            upper     = true;
                            downer    = false;
                            gui.setGUI(finish, index, running);
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {}
                        }
                    }
                }
            }

            if(animation == true) {
                game.setAnimation(animation, upper, downer, index);
                running = true;
                try {
                    Thread.sleep(2350);
                    gui.setGUI(finish, index, running);
                } catch (InterruptedException e) {}
            } else {
                game.setAnimation(animation, upper, downer, index);
                running = true;
                try {
                    Thread.sleep(300);
                    gui.setGUI(finish, index, running);
                } catch (InterruptedException e) {
                }
            }
        }
        if (!mode_roll.equals("NORMAL")) {
            game.mode_roll = "NORMAL"; // กลับไปยังการทอยแบบปกติ
            game.sethide(true);
        }else {
            game.sethide(true);
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