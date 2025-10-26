import java.io.Serializable;

// ข้อมูลที่ client ส่งไปหา server เมื่อกด "พร้อม"
public class PlayerUpdate implements Serializable {
    private static final long serialVersionUID = 1L;
    private String playerName;
    private boolean ready;

    public PlayerUpdate(String playerName, boolean ready) {
        this.playerName = playerName;
        this.ready = ready;
    }

    // Getters
    public String getPlayerName() { return playerName; }
    public boolean isReady() { return ready; }
}