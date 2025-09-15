import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class DebateChatMySQL extends JFrame {
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private Connection conn;

    public DebateChatMySQL() {
        setTitle("AI Debate Partner with MySQL");
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);

        inputField = new JTextField();
        sendButton = new JButton("Send");

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        try {
            conn = DBConnection.getConnection();
            System.out.println("✅ Connected to MySQL.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sendButton.addActionListener(e -> handleUserMessage());
        inputField.addActionListener(e -> handleUserMessage());
    }

    private void handleUserMessage() {
        String userMessage = inputField.getText().trim();
        if (userMessage.isEmpty()) return;

        appendMessage("User", userMessage);
        String aiResponse = generateAIResponse(userMessage);
        appendMessage("AI", aiResponse);

        inputField.setText("");
    }

    private void appendMessage(String speaker, String message) {
        chatArea.append(speaker + ": " + message + "\n");

        try {
            String sql = "INSERT INTO ConversationHistory (speaker, message) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, speaker);
            ps.setString(2, message);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String generateAIResponse(String userMessage) {
        return "Interesting point! (mock AI reply)";
    }

    public void showConversationSummary() {
        try {
            String sql = "SELECT speaker, message, timestamp FROM ConversationHistory ORDER BY id";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            StringBuilder summary = new StringBuilder("Conversation Summary:\n\n");
            while (rs.next()) {
                summary.append(rs.getTimestamp("timestamp"))
                        .append(" - ")
                        .append(rs.getString("speaker"))
                        .append(": ")
                        .append(rs.getString("message"))
                        .append("\n");
            }

            JOptionPane.showMessageDialog(this, summary.toString(), "Debate Summary", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DebateChatMySQL app = new DebateChatMySQL();
        app.setVisible(true);

        app.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                app.showConversationSummary();
                System.exit(0);
            }
        });
    }
}




