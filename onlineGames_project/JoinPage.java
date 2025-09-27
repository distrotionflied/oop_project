import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Label;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.event.MouseListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class JoinPage extends JFrame {
    JPanel buttonPanel = new JPanel();
    JButton button = new JButton("JOIN");
    boolean activeFrame;
    JoinPage(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Join Room");
        setSize(250,100);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        JTextField roomNumberField = new JTextField("Room Number");
        roomNumberField.setEditable(false);
        roomNumberField.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // Optional: clear text on click
                roomNumberField.setText("");
                roomNumberField.setEditable(true);
            }
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {}
            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {}
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {}
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {}
        });
        add(roomNumberField, BorderLayout.NORTH);
        add(buttonPanel,BorderLayout.CENTER);
        //buttonPanel.setLayout(new GridLayout(4,3,10,10));
        //buttonPanel.setBorder(new EmptyBorder(1,1,1,1));
        buttonPanel.add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                HostPage hostPage = new HostPage();
                hostPage.setVisible(true);
                setVisible(false);
            }
        });
    }
}

