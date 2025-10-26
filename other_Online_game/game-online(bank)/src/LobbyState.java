// LobbyState.java
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LobbyState implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Boolean> readyStatus = new HashMap<>();
    private List<String> chatMessages = new ArrayList<>();

    public void setReady(String name, boolean ready) {
        readyStatus.put(name, ready);
    }

    public Map<String, Boolean> getReadyStatus() {
        return new HashMap<>(readyStatus);
    }

    // สมมติว่า "Host" เป็นชื่อคงที่ของ host (แต่ไม่ได้ใช้ตรวจสอบ!)
    public boolean areAllNonHostPlayersReady() {
        for (Map.Entry<String, Boolean> e : readyStatus.entrySet()) {
            if (!"Host".equals(e.getKey()) && !e.getValue()) {
                return false;
            }
        }
        // ต้องมีผู้เล่นอย่างน้อย 1 คน (ไม่นับ host)
        return readyStatus.size() > 1 || (readyStatus.size() == 1 && readyStatus.containsKey("Host"));
    }

    public void addChatMessage(String message) {
        if (chatMessages.size() >= 50) chatMessages.remove(0); // เก็บแค่ 50 บรรทัด
        chatMessages.add(message);
    }

    public List<String> getChatMessages() {
        return new ArrayList<>(chatMessages);
    }
}