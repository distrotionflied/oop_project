import java.io.Serializable;
import java.util.*;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Integer> playerPositions = new HashMap<>();
    private List<String> playerOrder = new ArrayList<>();
    private String currentPlayer;
    private boolean gameStarted = false;
    private String winner;
    
    public void addPlayer(String name) {
        playerPositions.put(name, 0);
        playerOrder.add(name);
        if (currentPlayer == null) currentPlayer = name;
    }
    
    public void movePlayer(String name, int newPosition) {
        playerPositions.put(name, newPosition);
    }
    
    public void nextTurn() {
        int currentIndex = playerOrder.indexOf(currentPlayer);
        currentPlayer = playerOrder.get((currentIndex + 1) % playerOrder.size());
    }
    
    // Getters and Setters
    public Map<String, Integer> getPlayerPositions() { return playerPositions; }
    public List<String> getPlayerOrder() { return playerOrder; }
    public String getCurrentPlayer() { return currentPlayer; }
    public void setCurrentPlayer(String player) { currentPlayer = player; }
    public boolean isGameStarted() { return gameStarted; }
    public void setGameStarted(boolean started) { gameStarted = started; }
    public String getWinner() { return winner; }
    public void setWinner(String winner) { this.winner = winner; }
}