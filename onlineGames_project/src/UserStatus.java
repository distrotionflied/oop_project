import java.io.Serializable;

public class UserStatus implements Serializable{
    //principle of status
    private static final long  serialVersionUID = 221056L;
    String Username;
    String Destination;
    //object *other status
    WaitingRoomStatus wrs;
    ChatMessage chat;
    GameStatus gameStatus;
    public UserStatus() {
        WaitingRoomStatus wrs = new WaitingRoomStatus(this);
        ChatMessage chat      = new ChatMessage(this);
        GameStatus gameStatus = new GameStatus(this);
    }  
    public String getUsername() {
        return Username;
    }
    public ChatMessage getChat() {
        return chat;
    }
    public WaitingRoomStatus getWrs() {
        return wrs;
    }
    public GameStatus getGameStatus() {
        return gameStatus;
    }
}
//chat
class ChatMessage{
    String content;
    UserStatus userStatus;
    public ChatMessage(UserStatus userStatus) {
        this.userStatus = userStatus;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
    }
}

//waiting room
class WaitingRoomStatus{
    boolean ReadyStatus;
    UserStatus userStatus;

    public WaitingRoomStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public void setReadyStatus(boolean ReadyStatus) {
        this.ReadyStatus = ReadyStatus;
    }
}

//game
class GameStatus{
    UserStatus userStatus;
    public GameStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }
}