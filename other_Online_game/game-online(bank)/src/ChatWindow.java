// ChatWindow.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;
import java.awt.BorderLayout;

public class ChatWindow extends JFrame {
    private JTextArea chatArea;
    private JTextField inputField;
    private Consumer<String> onSendMessage;

    public ChatWindow(String title, Consumer<String> onSendMessage) {
        this.onSendMessage = onSendMessage;

        setTitle(title);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // ปิดแค่แชท ไม่ปิดทั้งโปรแกรม
        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Tahoma", Font.PLAIN, 13));
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        inputField = new JTextField();
        inputField.addActionListener(this::sendMessage);
        add(inputField, BorderLayout.SOUTH);

        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void sendMessage(ActionEvent e) {
        String msg = inputField.getText().trim();
        if (!msg.isEmpty()) {
            onSendMessage.accept(msg);
            inputField.setText("");
        }
    }

    public void clear() {
        SwingUtilities.invokeLater(() -> chatArea.setText(""));
    }

    public void appendMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            chatArea.append(message + "\n");
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
    }
}