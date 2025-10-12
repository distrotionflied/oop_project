import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GuiMonitor {
    HomeFrame hmf;
    public GuiMonitor(){
        hmf = new HomeFrame();
        ListenHomeFrame listenerhome = new ListenHomeFrame(hmf);
        hmf.setVisible(true);
    }
}
class ListenHomeFrame{
    HomeFrame hmf;
    HostFrame hsf;
    JoinFrame jnf;
    public ListenHomeFrame(HomeFrame hmf) {
        this.hmf = hmf;
        hsf = new HostFrame();
        jnf = new JoinFrame();
        this.hmf.getPanel().getHostButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hsf.setVisible(true);
                hmf.setVisible(false);
            }
        });
        this.hmf.getPanel().getServerButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hmf.setVisible(false);
            }
        });
        this.hmf.getPanel().getJoinButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jnf.setVisible(true);
                hmf.setVisible(false);
            }
        });
    }
}