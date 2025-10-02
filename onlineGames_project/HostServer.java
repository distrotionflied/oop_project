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
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

class HostPageFrame extends JFrame {

    HomePageFrame homePage;
    JPanel panel = new JPanel();
    JPanel panel2 = new JPanel();
    String hostRoomNumber = "Invalid";
    JLabel roomJLabel;
    String data[][];
    ArrayList<String> playerList = new ArrayList<String>();
    String readyCheckString = "Not Ready";
    String header[] = { "Username", "Status"};  
    JButton readyButton = new JButton("Ready"); 

    HostPageFrame(HomePageFrame homePage,boolean userStatus) {
        this.homePage = homePage;
        playerList.add(homePage.username.getText());
        data = new String[playerList.size()][2];
        for(int i=0;i<playerList.size();i++){
            data[i][0] = playerList.get(i);
            data[i][1] = readyCheckString;
        }
        //ย้าย การจัดข้อมูล tableList ไปไว้ในส่วนของ เธรด
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

        try {
			InetAddress a = InetAddress.getLocalHost();
			hostRoomNumber = a.getHostAddress();
            System.out.println("working");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

        roomJLabel = new JLabel("Host Room Number : " + hostRoomNumber);

        panel2.setBorder(new EmptyBorder(5,5,5,5));
        panel2.setLayout(new BorderLayout());
        panel2.add(scrollPane,BorderLayout.CENTER);

        roomJLabel.setHorizontalAlignment(0);
        add(roomJLabel,BorderLayout.NORTH);
        add(panel2,BorderLayout.CENTER);
        add(panel,BorderLayout.SOUTH);

        readyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                if(readyButton.getText().equals("Ready")){
                    readyButton.setText("Not Ready");
                    tableList.setValueAt("Ready", 0, 1);
                }else{
                    readyButton.setText("Ready");
                    tableList.setValueAt("Not Ready", 0, 1);
                }
            }
        });

        panel.setLayout(new FlowLayout());
        panel.add(readyButton);
    }
    public void setHostRoomNumber(String hostRoomNumber) {
        this.hostRoomNumber = hostRoomNumber;
    }
}

class ServerThread extends Thread {
	// Frame
	HostPageFrame server;
	public ServerThread(HostPageFrame server) {
		this.server = server;
	}
	@Override
	public void run() {
        System.out.println("Server Thread Running");
		try {
			// Open port 50101 for receiving data
			ServerSocket servSocket = new ServerSocket(50101);
			// Loop for waiting data
			while (true) {
				try {
					String line = "";
					// Waiting for data
					Socket socket = servSocket.accept();
					// Data in then convert to String
					InputStream input = socket.getInputStream();
					InputStreamReader inputStream = new InputStreamReader(input);
					BufferedReader bufferIn = new BufferedReader(inputStream);
					// Read all lines to TextArea
					while ((line = bufferIn.readLine()) != null) {
						//server.setHostRoomNumber(line);
                        //อ่านข้อมูลจาก client มาแสดงที่ label
					}
					bufferIn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
