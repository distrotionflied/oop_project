import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class GuiMonitor {
    public GuiMonitor(){
        HomeFrame hf = new HomeFrame();
        hf.setVisible(true);
    }
}

class HomeFrame extends JFrame{
    Dimension size = new Dimension(500, 600);
    panelJ panelJ = new panelJ();
    public HomeFrame(){
        setSize(size);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(panelJ);
    }
}

class panelJ extends JPanel{
    Dimension size = new Dimension(500, 600);
    JButton hostButton = new JButton("HOST");
    JButton joinButton = new JButton("JOIN");
    JPanel  panel = new JPanel();
    Image img;
    public panelJ() {
        setPreferredSize(size);
        setLayout(new BorderLayout());
        panel.setOpaque(false);
        add(panel,BorderLayout.CENTER);
        add(new JPanel(){
            @Override
            public void setOpaque(boolean isOpaque) {
                super.setOpaque(false);
            }
        },BorderLayout.NORTH);
        add(new JPanel(){
            @Override
            public void setOpaque(boolean isOpaque) {
                super.setOpaque(false);
            }
        },BorderLayout.SOUTH);
        add(new JPanel(){
            @Override
            public void setOpaque(boolean isOpaque) {
                super.setOpaque(false);
            }
        },BorderLayout.EAST);
        add(new JPanel(){
            @Override
            public void setOpaque(boolean isOpaque) {
                super.setOpaque(false);
            }
        },BorderLayout.WEST);
        try {
            img = ImageIO.read(new File("C:\\Users\\names\\Downloads\\11312031-seamless-snake-pattern.jpg")); 
        } catch (Exception e) {
            // TODO: handle exception
        }
        panel.setLayout(new GridLayout(4,1,10,10));
        panel.setBorder(new EmptyBorder(150,100,150,100));
        panel.add(new JLabel(){
            @Override
            public void setText(String text) {
                super.setText("GAME ONLINE");
            }

            @Override
            public void setForeground(Color fg) {
                super.setForeground(Color.BLACK);
            }

            @Override
            public void setHorizontalAlignment(int alignment) {
                super.setHorizontalAlignment(0);
            }

            @Override
            public void setFont(Font font) {
                font = new Font("Tahoma",Font.BOLD,20);
                super.setFont(font);
            }

            @Override
            public void setBackground(Color bg) {
                super.setBackground(Color.WHITE);
            }

            @Override
            public void setOpaque(boolean isOpaque) {
                super.setOpaque(true);
            }

            
        });
        panel.add(hostButton);
        panel.add(joinButton);
        panel.add(new JPanel(){
            @Override
            public Component add(Component comp) {
                return super.add(comp);
            }

            @Override
            public void setOpaque(boolean isOpaque) {
                super.setOpaque(false);
            }
        });
    }
    @Override
    public void paintComponent(Graphics g) {
        // TODO Auto-generated method stub
            super.paintComponent(g);
            if(img != null)g.drawImage(img, 0, 0, this);
            System.err.println("work");

    }
}
