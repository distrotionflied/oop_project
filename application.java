import javax.swing.JFrame;
public class application{
    public static void main(String[] args){
        MyFrame frame = new MyFrame();
        frame.setVisible(true);
    }
}
 class MyFrame extends JFrame{
        MyFrame(){
            setTitle("app");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(500,500);
        }
    }