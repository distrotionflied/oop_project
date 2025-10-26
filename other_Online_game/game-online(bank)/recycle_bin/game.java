import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


class mainGame1 extends JFrame {
    Properties setting = new Properties();
    FileInputStream input;

    int index = 0;
    int widthPOS , heightPOS;
    GUI guiPanel;
    {
        try {
            input = new FileInputStream("setting.properties");
            setting.load(input);

            widthPOS = Integer.parseInt(setting.getProperty("setWidth"));
            heightPOS = Integer.parseInt(setting.getProperty("setHeight"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public mainGame1() {
        setSize(widthPOS,heightPOS);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);
        setLayout(null);

        JButton btn = new JButton("Press");
        btn.setBounds(getWidth()- 200, getHeight()/2, 130,40);

        guiPanel = new GUI(index);
        guiPanel.setBounds(0,0, getWidth(), getHeight());
        add(guiPanel);

        add(btn);

        btn.addActionListener(e -> {
            if(index < 100) {
                int for_move;
                for_move = (int) (Math.random()*6 + 1);

                System.out.println("Pressed F" + for_move);
                index += for_move;
                guiPanel.setIndex(index, for_move);
                guiPanel.repaint();
                System.out.println(""+index);
                repaint();
            }else {
                btn.setEnabled(false);
            }
        });

        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_F) {
                    if(index < 100) {
                        int for_move;
                        for_move = (int) (Math.random()*6 + 1);

                        for(int i=0;i<for_move;i++) {
                            System.out.println("Pressed F" + for_move);
                            index++;
                            guiPanel.setIndex(index, for_move);
                            guiPanel.repaint();
                            System.out.println(""+index);
                            repaint();
                        }
                    }else {
                        btn.setEnabled(false);
                    }
                }
            }
        });
        setFocusable(true);
        requestFocusInWindow();
        add(new GUI(index));
    }
}

class GUI extends JPanel {
    int index;
    int for_move;
    ImageIcon bg = new ImageIcon("assets\\bg_game1.jpg");
    ImageIcon player1 = new ImageIcon("assets\\player.png");
    ImageIcon winner = new ImageIcon("assets\\logo_menu.png");

    public GUI(int index) {
        this.index = index;
    }

    public void setIndex(int index, int for_move) {
        this.index = index;
        this.for_move = for_move;
    }


    int startX = 300, startY = 570;
    int moveX, moveY;

    // วาด player ตาม index ด้วยการจำลองการเดินทีละสเต็ป
    Font big = new Font("big",Font.BOLD,55);
    private void drawPlayer(Graphics g) {
        g.setFont(big);
        g.drawString("" + for_move, getWidth() / 2 - 130, getHeight() / 2 - 10);

            if(index == 0) {
                moveX = startX; moveY = startY;
            } else if(index == 1) {
                moveX += 90; moveY += 17;
            } else if(index == 100) {
                moveX -= 51*for_move;
                g.drawImage(winner.getImage(),500,50,300,200,this);
                System.out.println("You WIN");
            } else if(index % 10 == 1) {
                moveY -= 58;
            }else if(index % 20 >= 1 && index % 20 <= 10) {
                moveX += 51;
            }else if(index % 20 >= 11 && index % 20 <= 20 || index % 20 == 0) {
                moveX -= 51;
            }
            g.drawImage(player1.getImage(),moveX,moveY,20,32,this);
        }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bg.getImage(), 0, 0, getWidth(), getHeight(), this);
        drawPlayer(g);
    }
}

class game {
    public static void main(String[] args) {
        mainGame1 mygame = new mainGame1();
        mygame.setVisible(true);
    }
}