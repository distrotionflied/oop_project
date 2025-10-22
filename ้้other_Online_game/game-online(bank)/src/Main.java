import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;


/*

    -- Layer
    === 0 - 1 Menu ===
    0 คือเฟรมแรกสุด menu_frame (JFrame) และพื้นหลัง
    1 ใช้กับ component ในหน้า menu

    === 2 - 5 Setting ===
    2



 */



class menu_frame extends JFrame {

    //เรียกหา file setting
    Properties setting = new Properties();
    FileInputStream input;
    int widthPos, heightPos;

    {
        try {
            input = new FileInputStream("Setting.properties");
            setting.load(input);

            widthPos  = Integer.parseInt(setting.getProperty("setWidth", "1260"));
            heightPos = Integer.parseInt(setting.getProperty("setHeight", "680"));
        } catch (IOException e) {
            System.out.println("Can't loading file Setting.properties...");
            System.exit(1);
        }
    }

    //กำหนด layer
    JLayeredPane layer = new JLayeredPane();

    //

    ImageIcon bg    = new ImageIcon("assets\\bg_image.png");
    ImageIcon logo  = new ImageIcon("assets\\logo.png");
    ImageIcon howto = new ImageIcon("assets\\howto.png");

    //
    String[] menu_namebtn = {"GAME START", "SETTING", "ONLINE", "QUIT"};
    JButton[] menu_btn = new JButton[4];

    //

    //////////////////////////////////////////////////////////////////////////////////////////////
    //                                           ตัวแปรต่างๆ                                       //
    boolean ch_setting = false;
    boolean ch_online  = false;





    //////////////////////////////////////////////////////////////////////////////////////////////



    JTextArea sub = new JTextArea(
                    "WELCOME TO MY FINAL PROJECT  \n" + "Developed for the Object-Oriented Programming Course.\n" +
                    "\n" +
                    "This game represents my journey through coding, logic, and creativity.  \n" +
                    "I hope you enjoy playing it as much as I enjoyed building it.\n");

    public menu_frame() {
        //เรียกใช้ฟอนต์
        Font myfont, midfont, mymid;
        midfont = new Font("myfont", Font.BOLD, 14);
        try {
            myfont = Font.createFont(Font.TRUETYPE_FONT, new File("CheeseRegular.ttf"));
            myfont = myfont.deriveFont(Font.BOLD, 24);
            mymid  = myfont.deriveFont(Font.BOLD, 20);
        } catch (FontFormatException|IOException e) {
            myfont = new Font("myfont", Font.BOLD, 24);
            mymid  = new Font("mid", Font.BOLD, 20);
        }

        setSize(widthPos, heightPos);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setLayout(null);

        // กำหนดขนาด layer เหมือนกับ gui ทั่วไปเลย
        layer.setBounds(0,0,widthPos,heightPos);
        add(layer);

        /*==================================================================================================
                                                    Background
        ==================================================================================================*/

        GUI_DRAW mygui = new GUI_DRAW();
        mygui.setBounds(0,0,getWidth(),getHeight());
        mygui.setImage(bg,logo, howto);
        layer.add(mygui , Integer.valueOf(0)); //กำหนดให้อยู่ layer ที่ 0 หรือก็คือ value 0

        /*==================================================================================================
                                                    Component
        ==================================================================================================*/


        int y = 220;
        for(int i=0; i<menu_btn.length; i++) {
            menu_btn[i] = new JButton(menu_namebtn[i]);
            menu_btn[i].setBounds(getWidth()/2-120,y,240,40);
            menu_btn[i].setFont(myfont);
            menu_btn[i].setForeground(Color.white);

            // องค์ประกอบเพิ่มเติมสำหรับปุ่ม
            menu_btn[i].setOpaque(false); //ทำพื้นหลังโปร่ง คือมองเห็น bg ( แต่ต้อง setbg ด้วย )
            menu_btn[i].setFocusPainted(false);
            //menu_btn[i].setBorderPainted(false);
            menu_btn[i].setBackground(new Color(0,0,0,0));
            layer.add(menu_btn[i], Integer.valueOf(1));
            y += 70;
        }

        sub.setFocusable(false);
        sub.setOpaque(false);
        sub.setBackground(new Color(0,0,0,0));
        sub.setBounds(getWidth()-520,getHeight()-120,500,200);
        sub.setFont(midfont);
        sub.setForeground(Color.white);
        layer.add(sub, Integer.valueOf(1));

        int[] vul = new int[3];
        /*==================================================================================================
                                             Action Listenner / Event
        ==================================================================================================*/

        // GAME START
        menu_btn[0].addActionListener(game -> {
            mainGame mygame = new mainGame();
            mygame.setVisible(true);

            Timer delay_close = new Timer(2000 ,close -> {
                dispose();
            });
            delay_close.start();
        });

        // SETTING
        Font finalMymid = mymid;
        menu_btn[1].addActionListener(Mysetting -> {
            //ดึงข้อมูลก่อนหน้าจาก Setting.properties ก่อน
            try(FileInputStream input = new FileInputStream("Setting.properties")) {
                setting.load(input);
                vul[0] = Integer.parseInt(setting.getProperty("setMaster"));
                vul[1] = Integer.parseInt(setting.getProperty("setMusic" ));
                vul[2] = Integer.parseInt(setting.getProperty("setEffect"));
            }catch (IOException e) {

            }

            // clear frame
            ch_setting = true;
            mygui.setaction(ch_setting,ch_online);
            sub.setVisible(false);
            for(int i=0; i<menu_btn.length; i++) {
                menu_btn[i].setVisible(false);
            }

            String[] name_volume = {"master", "music", "effects"};
            String[] name_btn    = {"BACK", "APPLY"};

            JSlider[] volume = new JSlider[3];
            JLabel[] name_volume_ = new JLabel[3];

            JButton[] btn = new JButton[2];

            //1 = master , 2 = sfx , 3 = bgm
            int ypos = getHeight()/2 -100;
            for(int i=0;i<3;i++) {

                volume[i] = new JSlider(0,100,vul[i]);
                volume[i].setBounds(getWidth()/2+60, ypos, 300, 40);
                volume[i].setOpaque(false);
                volume[i].setFocusable(false);

                name_volume_[i] = new JLabel(name_volume[i]);
                name_volume_[i].setForeground(Color.white);
                name_volume_[i].setFont(finalMymid);
                name_volume_[i].setBounds(getWidth()/2+70, ypos-35, 150, 40);


                layer.add(volume[i], Integer.valueOf(2));
                layer.add(name_volume_[i], Integer.valueOf(3));
                ypos   +=70;
            }

            int myX = getWidth()/2+280, myY = getHeight()/2-230;
            for(int i=0; i< btn.length; i++) {
                btn[i] = new JButton(name_btn[i]);
                btn[i].setForeground(Color.white);
                btn[i].setBounds(myX,myY,100,40);
                btn[i].setOpaque(false);
                btn[i].setFont(finalMymid);
                btn[i].setFocusPainted(false);
                btn[i].setBorderPainted(false);
                btn[i].setBackground(new Color(0,0,0,0));
                layer.add(btn[i], Integer.valueOf(2));
                myX -= 10;  myY += 340;
            }

            btn[1].addActionListener(click -> {
                JLabel show = new JLabel("Saved Success..");
                show.setForeground(Color.GREEN);
                show.setBounds(getWidth()/2+270, getHeight()/2+140,100,40);
                layer.add(show, Integer.valueOf(2));
                Timer delay = new Timer(1700, time-> {
                    show.setVisible(false);
                });
                delay.start();

                for(int i=0; i<vul.length; i++) {
                    vul[i] = volume[i].getValue();
                }

                try (FileOutputStream output = new FileOutputStream("Setting.properties")) {
                    setting.setProperty("setMaster",    String.valueOf(vul[0]));
                    setting.setProperty("setMusic",     String.valueOf(vul[1]));
                    setting.setProperty("setEffect",    String.valueOf(vul[2]));
                    setting.store(output, "comment File");
                } catch (IOException save) { }
            });

            btn[0].addActionListener(back_ -> {
                ch_setting = false;
                sub.setVisible(true);
                for(int i=0; i<menu_btn.length; i++) {
                    menu_btn[i].setVisible(true);
                } for(int i=0; i<volume.length; i++) {
                    volume[i].setVisible(false);
                    name_volume_[i].setVisible(false);
                } for(int i=0; i < btn.length; i++) {
                    btn[i].setVisible(false);
                }
                layer.setLayer(btn[0], Integer.valueOf(0));
                mygui.setaction(ch_setting,ch_online);
                mygui.repaint();
            });

            revalidate();
            repaint();
        }) ;

        // Online

        menu_btn[2].addActionListener(online -> {
            // clear frame
            ch_online = true;
            mygui.setaction(ch_setting,ch_online);
            sub.setVisible(false);
            for(int i=0; i<menu_btn.length; i++) {
                menu_btn[i].setVisible(false);
            }

            JButton back = new JButton("BACK");

            back.setForeground(Color.white);
            back.setBounds(getWidth()/2+280,getHeight()/2-230,100,40);
            back.setOpaque(false);
            back.setFont(finalMymid);
            back.setFocusPainted(false);
            back.setBorderPainted(false);
            back.setBackground(new Color(0,0,0,0));
            layer.add(back, Integer.valueOf(2));

            back.addActionListener(back_ -> {
                ch_online = false;
                sub.setVisible(true);
                for(int i=0; i<menu_btn.length; i++) {
                    menu_btn[i].setVisible(true);
                }

                layer.setLayer(back, Integer.valueOf(0));
                mygui.setaction(ch_setting,ch_online);
                mygui.repaint();
            });

            revalidate();
            repaint();
        });



        // QUIT
        menu_btn[3].addActionListener(close -> {
            System.exit(0);
        });
    }
}

class Main {
    public static void main(String[] args) {
        menu_frame menu = new menu_frame();
        menu.setVisible(true);
    }
}