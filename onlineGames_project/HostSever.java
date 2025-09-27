import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HostSever {
    public static void main(String[] args) {
        JoinPage jp = new JoinPage();
        jp.setVisible(true);
    }
}
class HostPage extends JFrame {
    JPanel panel = new JPanel();
    JPanel panel2 = new JPanel();
    JLabel rommJLabel = new JLabel("Room Number : 200");
    String data[][] = {
            { "Row1/1", "Row1/2"},
            { "Row2/1", "Row2/2"},
            { "Row3/1", "Row3/2"},
            { "Row4/1", "Row4/2"}, 
    };
    String header[] = { "Username", "Status"};  
    JButton readyButton = new JButton("Ready"); 
    HostPage(){
        JTable tableList = new JTable(data,header){
            @Override
            public boolean isCellEditable(int row, int column) {
                // TODO Auto-generated method stub
                return false;
            }
        };
        JScrollPane scrollPane = new JScrollPane(tableList);
        setTitle("Waiting Room");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300,400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        panel2.setBorder(new EmptyBorder(5,5,5,5));
        panel2.setLayout(new BorderLayout());
        panel2.add(scrollPane,BorderLayout.CENTER);

        rommJLabel.setHorizontalAlignment(0);
        add(rommJLabel,BorderLayout.NORTH);
        add(panel2,BorderLayout.CENTER);
        add(panel,BorderLayout.SOUTH);

        readyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                if(readyButton.getText().equals("Ready")){
                    readyButton.setText("Not Ready");
                }else{
                    readyButton.setText("Ready");
                }
            }
        });

        panel.setLayout(new FlowLayout());
        panel.add(readyButton);
    }
}
