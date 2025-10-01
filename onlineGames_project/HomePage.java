import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class HomePage{
    public static void main(String[] args) {
        HomePageFrame frame = new HomePageFrame();
        frame.setVisible(true);
    }
}
class HomePageFrame extends JFrame {
    public HomePageFrame() {
        setTitle("Home Page");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        JTextField username = new JTextField("Enter Your Username",20);
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout());
        titlePanel.add(new JLabel("Welcome to the Online Game"));

        username.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if(username.getText().equals("Enter Your Username")){
                    username.setText("");
                }
            }
        });
        username.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                if (username.getText().equals("Enter Your Username")) {
                    username.setText("");
                }
            }
        });

        add(titlePanel, BorderLayout.NORTH);

        JButton hostButton = new JButton("Host Game");
        hostButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (username.getText().trim().isEmpty() || username.getText().equals("Enter Your Username")) {
                    JOptionPane.showMessageDialog(HomePageFrame.this, "Please enter a valid username.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }else{
                    new HostPageFrame().setVisible(true);
                    dispose();
                }
            }
        });

        JButton joinButton = new JButton("Join Game");
        joinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (username.getText().trim().isEmpty() || username.getText().equals("Enter Your Username")) {
                    JOptionPane.showMessageDialog(HomePageFrame.this, "Please enter a valid username.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }else{
                    new JoinPageFrame().setVisible(true);
                    dispose();
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 10, 10));
        buttonPanel.add(hostButton);
        buttonPanel.add(joinButton);

        add(buttonPanel, BorderLayout.CENTER);
        add(titlePanel, BorderLayout.NORTH);
        add(username, BorderLayout.SOUTH);
    }
}
