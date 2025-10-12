import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerProvide {
    public static void main(String[] args) {
        
    }
}

class SeverThread extends Thread {
    private  static final int PORT = 59415;
     // Thread-Safe List สำหรับเก็บช่องทางส่งข้อมูลของ Client ทุกคน
    static List<ObjectOutputStream> clientRequriedList = new CopyOnWriteArrayList<>();

    @Override
    public void run() {
        try{
            System.out.println("Sever Start At : " + InetAddress.getLocalHost().getHostAddress());
            try(ServerSocket serverSocket = new ServerSocket(PORT)){
                while (true) {
                Socket socket = serverSocket.accept(); // Blocking: รอรับ Client ใหม่
                // สร้าง Thread ใหม่เพื่อจัดการ Client คนนี้
                ClientHandler handler = new ClientHandler(socket);
                handler.start(); 
            }
            } catch (IOException e) {
                System.out.println("IO Eror "+e.getMessage());
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        System.out.println("Checking for new messages...");

    }

    

    public static void broadcast(UserStatus userStat) {
        System.out.println("BROADCAST: " + userStat);
        for (ObjectOutputStream oos : clientRequriedList) {
            try {
                oos.writeObject(userStat); 
                oos.flush();
            } catch (Exception e) {
                // ต้องจัดการลบผู้เล่นที่หลุดออกจากรายการอย่างเหมาะสม
                clientRequriedList.remove(oos);
            }
        }
    }
}

// Thread ใหม่สำหรับ Client แต่ละคน
class ClientHandler extends Thread {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null; // ObjectOutputStream สำหรับส่งออก
        try {
            // สำคัญ: ต้องสร้าง ObjectOutputStream ก่อน ObjectInputStream เสมอ!
            // เพราะ ObjectOutputStream จะเขียน Header ลงใน Stream ทันทีที่สร้าง
            oos = new ObjectOutputStream(socket.getOutputStream());
            
            // !!! แก้ไขตรงนี้: เพิ่ม ObjectOutputStream เข้าในรายการ !!!
            SeverThread.clientRequriedList.add(oos); 
            
            // สร้าง Stream สำหรับรับ Object ที่ถูก Serialized
            ois = new ObjectInputStream(socket.getInputStream());

            while (true) {
                // Blocking: รอรับ Object Message จาก Client
               UserStatus message = (UserStatus) ois.readObject();
                
                System.out.println("Received from [" + message.getUsername() + "]: " + message.getChat().getContent());
                
                // Broadcast Message Object กลับไปให้ Client ทุกคน
                SeverThread.broadcast(message); 
            }
        } catch (EOFException | SocketException e) {
            // EOFException มักเกิดเมื่อ Client ปิดการเชื่อมต่ออย่างถูกต้อง
            System.out.println("Client Disconnected.");
        } catch (Exception e) {
            System.out.println("Handler Error: " + e.getClass().getSimpleName() + ": " + e.getMessage());
        } finally {
            // 1. ตรวจสอบและลบ oos ออกจากรายการ Broadcasting
            if (oos != null) {
                SeverThread.clientRequriedList.remove(oos);
                System.out.println("Removed ObjectOutputStream from broadcast list.");
            }
            // 2. ปิด Socket และ Stream
            try { 
                if (ois != null) ois.close();
                if (oos != null) oos.close(); 
                socket.close(); 
            } catch (IOException e) { /* ignore */ }
        }
    }
}