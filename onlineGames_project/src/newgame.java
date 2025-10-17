import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


class mainGame extends JFrame {
    Properties setting = new Properties();
    FileInputStream input;
    int widthPOS, heightPOS;

    {
        try {
            input = new FileInputStream("D:\\OOP\\tester\\game_snake\\setting.properties");
            setting.load(input);

            widthPOS = Integer.parseInt(setting.getProperty("setWidth"));
            heightPOS = Integer.parseInt(setting.getProperty("setHeight"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //ตัวแปรคำนวนตำแหน่ง
    private boolean press;
    int roll, index = 0;
    int save  = 90;
    int moveX = 300;
    int moveY = 570;
    //boolean my_tern = true //รอการอัพเดท
    boolean auto = true;

    public mainGame() {
        setSize(widthPOS,heightPOS);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setLayout(null);

        panelGUI guiPanel = new panelGUI();
        guiPanel.setBounds(0, 0, getWidth(), getHeight());
        add(guiPanel);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE && !press && index != 100 && auto && index < save) {
                    press = true;
                    new Thread(() -> {
                        while (index < save) {
                            roll = (int) (Math.random() * 6 + 1);

                            My_Thread thread = new My_Thread(roll, guiPanel, index, moveX, moveY);
                            thread.start();

                            try {
                                thread.join();
                                index = thread.getIndex();
                                moveX = thread.getMoveX();
                                moveY = thread.getMoveY();
                            } catch (InterruptedException ex) {}

                            // ถ้าถึงจุด save แล้วก็หยุด
                            if (index >= save) {
                                auto = false;
                                System.out.println("== Auto stopped at index " + index + " ==");
                                break;
                            }

                            try {
                                Thread.sleep(300); // พักนิดนึงก่อนทอยรอบต่อไป
                            } catch (InterruptedException ex) {}
                        }
                        press = false;
                    }).start();
                }

                else if (e.getKeyCode() == KeyEvent.VK_SPACE && index != 100 && !press && !auto && index < 100) {
                    press = true;
                    roll = (int) (Math.random() * 6 + 1);

                    My_Thread thread = new My_Thread(roll, guiPanel, index, moveX, moveY);
                    thread.start();

                    new Thread(() -> {
                        try {
                            thread.join();
                            index = thread.getIndex();
                            moveX = thread.getMoveX();
                            moveY = thread.getMoveY();
                        } catch (InterruptedException ex) {}
                        press = false;
                    }).start();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) press = false;
            }
        });
        setFocusable(true);
        requestFocusInWindow(); //ใช้เพื่อให้ window สามารถโฟกัสได้ หากมี Jcomponent มันจะใส่ใจอันนั้นก่อนจึงต้องใช้

    }
}

class panelGUI extends JPanel {
    int moveX = 300 , moveY = 570;
    boolean finish;

    public void setGUI(int moveX, int moveY, boolean finish) {
        this.moveX = moveX;
        this.moveY = moveY;
        this.finish = finish;
        repaint();
    }

    ImageIcon bg = new ImageIcon("assets\\bg_game1.jpg");
    ImageIcon winner = new ImageIcon("assets\\logo_menu.png");
    ImageIcon player = new ImageIcon("assets\\player.png");

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bg.getImage(), 0, 0, getWidth(), getHeight(), this);
        g.drawImage(player.getImage(), moveX, moveY, 20, 32, this);

        if(finish == true) {
            g.drawImage(winner.getImage(), getWidth()/2-234,100,468,246, this);
        }
    }
}

class My_Thread extends Thread {
    int roll;
    panelGUI gui;

    //ตัวแปรใหม่
    boolean finish = false;
    int index, limit, cal;
    int moveX, moveY;


    public My_Thread (int roll, panelGUI gui, int index, int moveX, int moveY) {
        this.roll = roll;
        this.gui  = gui;
        this.index = index;
        this.moveX = moveX;
        this.moveY = moveY;
    }

    public void run() {
        cal = index+roll;
        if(cal > 100) {
            limit = cal - 100;
        }

        for(int i=0; i<roll; i++) {
            index++;
            if (index == 1) {
                moveX += 90;
                moveY += 17;
            }else if(index >= 100) {
                if(index > 100) {
                    moveX += 51;
                    limit--;
                    System.out.print("\tI : (" + i + ")\troll : " + roll + "\t");
                    if ((i == (roll - 1))) {
                        index = 100 - (cal - 100);
                        System.out.println("Stop Stop Stop Stop");
                    }
                } else {
                    moveX -= 51;
                }
            } else if (index % 10 == 1) {
                moveY -= 58;
            } else if (index % 20 >= 1 && index % 20 <= 10) {
                moveX += 51;
            } else if(index % 20 >= 11 && index % 20 <= 20 || index % 20 == 0) {
                moveX -= 51;
            }

            if(index == 100 && limit == 0) {
                System.out.println("Winner");
                finish = true;
            }
            System.out.println("("+(i+1)+") : "+index+"\t||\t");
            gui.setGUI(moveX, moveY, finish);
            gui.repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }System.out.println("roll: "+roll+"\tlimit: "+limit+"\nmoveX: "+moveX+"\tmoveY: "+moveY
                            +"\nindex: "+index+"\tcal: "+cal+"\n-----------------------");
    }
    public int getIndex() {
        return index;
    } public int getMoveX() {
        return moveX;
    } public int getMoveY() {
        return moveY;
    }
}

class newgame {
    public static void main(String[] args) {
        mainGame mygame = new mainGame();
        mygame.setVisible(true);
    }
}