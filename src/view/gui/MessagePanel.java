package view.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Panel displaying game messages and feedback.
 * Shows success, error, and informational messages.
 *
 * @author Acil HAMIEH
 * @version 1.0
 */
public class MessagePanel extends JPanel {
    private JLabel messageLabel;
    private MessageType currentType;

    /**
     * Message types with associated colors
     */
    public enum MessageType {
        INFO(new Color(70, 130, 180), Color.WHITE),      // Steel Blue
        SUCCESS(new Color(34, 139, 34), Color.WHITE),    // Forest Green
        ERROR(new Color(220, 20, 60), Color.WHITE),      // Crimson
        WARNING(new Color(255, 165, 0), Color.BLACK);    // Orange

        private final Color background;
        private final Color foreground;

        MessageType(Color background, Color foreground) {
            this.background = background;
            this.foreground = foreground;
        }

        public Color getBackground() {
            return background;
        }

        public Color getForeground() {
            return foreground;
        }
    }

    /**
     * Constructor for MessagePanel
     */
    public MessagePanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, 100));
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2),
                "Messages",
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 12)
        ));

        createComponents();
        setMessage("Select 3 cards to form a trio", MessageType.INFO);
    }

    /**
     * Create components
     */
    private void createComponents() {
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        messageLabel.setOpaque(true);

        // Enable HTML for line wrapping
        messageLabel.setText("<html><div style='text-align: center; width: 200px;'></div></html>");

        add(messageLabel, BorderLayout.CENTER);
    }

    /**
     * Set message with type
     * @param message Message text
     * @param type Message type
     */
    public void setMessage(String message, MessageType type) {
        currentType = type;

        // Wrap in HTML for better display
        String htmlMessage = "<html><div style='text-align: center; padding: 5px;'>"
                + message + "</div></html>";

        messageLabel.setText(htmlMessage);
        messageLabel.setBackground(type.getBackground());
        messageLabel.setForeground(type.getForeground());

        repaint();
    }

    /**
     * Set message with default INFO type
     * @param message Message text
     */
    public void setMessage(String message) {
        setMessage(message, MessageType.INFO);
    }

    /**
     * Clear message
     */
    public void clearMessage() {
        messageLabel.setText("");
        messageLabel.setBackground(Color.WHITE);
    }

    /**
     * Show temporary message that auto-clears
     * @param message Message text
     * @param type Message type
     * @param durationMs Duration in milliseconds
     */
    public void showTemporaryMessage(String message, MessageType type, int durationMs) {
        setMessage(message, type);

        Timer timer = new Timer(durationMs, e -> clearMessage());
        timer.setRepeats(false);
        timer.start();
    }
}
