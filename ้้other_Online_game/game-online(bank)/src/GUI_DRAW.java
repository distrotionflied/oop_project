import javax.swing.*;
import java.awt.*;

class GUI_DRAW extends JPanel {
    ImageIcon bg, logo, howto;
    boolean setting, online;

    public void setImage(ImageIcon bg, ImageIcon logo, ImageIcon howto) {
        this.bg = bg;
        this.logo = logo;
        this.howto = howto;
    }

    public void setaction(boolean setting, boolean online) {
        this.setting = setting;
        this.online  = online;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bg.getImage(), 0,0, getWidth(), getHeight(), this);

        if(setting) {
            g.setColor(new Color(0,0,0,100));
            g.fillRect(0,0,getWidth(),getHeight());

            g.setColor(Color.white);
            g.drawRect(getWidth()/2-400,getHeight()/2-250,800,500);

            g.drawImage(howto.getImage(), getWidth()/2-395, getHeight()/2-168, 460,300,null);

        } else if(online) {
            g.setColor(new Color(0,0,0,100));
            g.fillRect(0,0,getWidth(),getHeight());

            g.setColor(Color.white);
            g.drawRect(getWidth()/2-400,getHeight()/2-250,800,500);
        } else {
            g.drawImage(logo.getImage(), getWidth()/2-128, getHeight()/2-350,256,256, null);
        }
    }
}