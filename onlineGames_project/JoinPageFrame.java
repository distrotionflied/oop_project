import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Label;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.event.MouseListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;

public class JoinPageFrame extends JFrame {
    JPanel buttonPanel = new JPanel();
    JButton button = new JButton("JOIN");
    boolean activeFrame;
    JoinPageFrame(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Join Room");
        setSize(250,100);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        JTextField roomNumberField = new JTextField("Host Room Number");
        roomNumberField.setEditable(false);
        roomNumberField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if(roomNumberField.getText().equals("Host Room Number")){
                    roomNumberField.setText("");
                    roomNumberField.setEditable(true);
                }
            }
        });
        roomNumberField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                if (roomNumberField.getText().equals("Host Room Number")) {
                        roomNumberField.setText("");
                        roomNumberField.setEditable(true);
                }
            }
        });
        add(roomNumberField, BorderLayout.NORTH);
        add(buttonPanel,BorderLayout.CENTER);
        buttonPanel.add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (roomNumberField.getText().trim().isEmpty() || roomNumberField.getText().equals("Host Room Number")) {
                    JOptionPane.showMessageDialog(JoinPageFrame.this, "Please enter a valid room number.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }else{
                    HostPageFrame hostPage = new HostPageFrame();
                    hostPage.setVisible(true);
                    setVisible(false);
                }
            }
        });
    }
}

