import java.awt.BorderLayout;
import java.awt.Color;
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

class HomeFrame extends JFrame{
    Dimension size = new Dimension(500, 600);
    panelOfFrame panel = new panelOfFrame();
    public HomeFrame(){
        setSize(size);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(panel);
    }
    panelOfFrame getPanel(){     
        return this.panel;
    }
}

class panelOfFrame extends JPanel{
    Dimension size = new Dimension(500, 600);
    JButton hostButton = new JButton("HOST");
    JButton joinButton = new JButton("JOIN");
    JButton serverButton = new JButton("SERVER");
    JPanel  panel = new JPanel();//พาเนลตรงกลางที่บรรจุปุ่ม และ โลโก้
    JLabel  logoLabel = new JLabel();
    Image img;
    public panelOfFrame() {
        setPreferredSize(size);
        setLayout(new BorderLayout());
        panel.setOpaque(false);
        add(panel,BorderLayout.CENTER);
        //ใส่ประกบให้มันว่างเฉยๆ ใช้ Anymous inner class แบบพิเศษใส่ไข่
        add(new JPanel(){{setOpaque(false);}},BorderLayout.NORTH);
        add(new JPanel(){{setOpaque(false);}},BorderLayout.SOUTH);
        add(new JPanel(){{setOpaque(false);}},BorderLayout.EAST);
        add(new JPanel(){{setOpaque(false);}},BorderLayout.WEST);

        //รูปพื้นหลัง
        try {
            img = ImageIO.read(new File("C:\\Users\\names\\Downloads\\11312031-seamless-snake-pattern.jpg")); 
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        panel.setLayout(new GridLayout(4,1,10,10));
        panel.setBorder(new EmptyBorder(150,100,150,100));

        logoLabel.setText("GAME ONLINE");
        logoLabel.setForeground(Color.BLACK);
        logoLabel.setBackground(Color.WHITE);
        logoLabel.setHorizontalAlignment(0);
        logoLabel.setFont(new Font("Tahoma",Font.BOLD,20));
        logoLabel.setOpaque(true);

        panel.add(logoLabel);
        panel.add(hostButton); //ทั้งจอย ทั้งเป็นเซิฟเวอร์
        panel.add(joinButton); //จอยเฉยๆ
        panel.add(serverButton); //เป็นเซิฟเวอร์เฉยๆ
    }
    @Override
    public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if(img != null)g.drawImage(img, 0, 0, this);
            System.err.println("work");
    }
    public JButton getJoinButton() {
        return joinButton;
    }
    public JButton getHostButton() {
        return hostButton;
    }
    public JButton getServerButton() {
        return serverButton;
    }
}
