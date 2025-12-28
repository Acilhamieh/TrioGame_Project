package view.gui;

import model.*;
import enums.*;
import javax.swing.*;
import java.awt.*;

/**
 * Visual test for individual GUI components
 * Shows each component in isolation
 */
public class GUIComponentTest {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            testCardComponent();
            testMessagePanel();
            testScoreBoard();
        });
    }

    /**
     * Test CardComponent display
     */
    private static void testCardComponent() {
        JFrame frame = new JFrame("Card Component Test");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 300);
        frame.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 50));

        // Create sample cards
        Card card1 = new Card("AP4B", Branch.COMPUTER_SCIENCE);
        Card card2 = new Card("GI40", Branch.INDUSTRIAL_ENGINEERING);
        Card card3 = new Card("MQ40", Branch.MECHANICAL_ENGINEERING);
        Card card4 = new Card("ER40", Branch.ENERGY_ENGINEERING);
        Card card5 = new Card("PFE", Branch.SPECIAL);

        // Add card components
        CardComponent.CardSelectionListener listener = new CardComponent.CardSelectionListener() {
            @Override
            public void onCardSelected(Card card) {
                System.out.println("Selected: " + card.getCourseCode());
            }

            @Override
            public void onCardDeselected(Card card) {
                System.out.println("Deselected: " + card.getCourseCode());
            }
        };

        frame.add(new CardComponent(card1, listener));
        frame.add(new CardComponent(card2, listener));
        frame.add(new CardComponent(card3, listener));
        frame.add(new CardComponent(card4, listener));
        frame.add(new CardComponent(card5, listener));

        JLabel instructions = new JLabel("Click cards to select/deselect. Hover to see effect.");
        instructions.setFont(new Font("SansSerif", Font.BOLD, 14));
        frame.add(instructions);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        System.out.println("✓ Card Component Test: Try clicking and hovering cards!");
    }

    /**
     * Test MessagePanel
     */
    private static void testMessagePanel() {
        JFrame frame = new JFrame("Message Panel Test");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(5, 1, 10, 10));

        // Create message panels with different types
        MessagePanel info = new MessagePanel();
        info.setMessage("This is an INFO message", MessagePanel.MessageType.INFO);

        MessagePanel success = new MessagePanel();
        success.setMessage("This is a SUCCESS message", MessagePanel.MessageType.SUCCESS);

        MessagePanel error = new MessagePanel();
        error.setMessage("This is an ERROR message", MessagePanel.MessageType.ERROR);

        MessagePanel warning = new MessagePanel();
        warning.setMessage("This is a WARNING message", MessagePanel.MessageType.WARNING);

        frame.add(info);
        frame.add(success);
        frame.add(error);
        frame.add(warning);

        JButton changeButton = new JButton("Cycle Messages");
        final int[] count = {0};
        changeButton.addActionListener(e -> {
            count[0]++;
            switch (count[0] % 4) {
                case 0:
                    info.setMessage("Info updated: " + count[0], MessagePanel.MessageType.INFO);
                    break;
                case 1:
                    success.setMessage("Success updated: " + count[0], MessagePanel.MessageType.SUCCESS);
                    break;
                case 2:
                    error.setMessage("Error updated: " + count[0], MessagePanel.MessageType.ERROR);
                    break;
                case 3:
                    warning.setMessage("Warning updated: " + count[0], MessagePanel.MessageType.WARNING);
                    break;
            }
        });
        frame.add(changeButton);

        frame.setLocationByPlatform(true);
        frame.setVisible(true);

        System.out.println("✓ Message Panel Test: See different message types!");
    }

    /**
     * Test ScoreBoard (requires controller)
     */
    private static void testScoreBoard() {
        JFrame frame = new JFrame("ScoreBoard Test");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 500);

        // Create mock game
        controller.GameController controller = new controller.GameController();
        java.util.List<String> names = java.util.Arrays.asList("Dana", "Acil", "Alice");
        controller.initializeGame(3, GameMode.INDIVIDUAL_SIMPLE, Difficulty.NORMAL, names);
        controller.startGame();

        // Give some ECTS for display
        controller.getGame().getStudents().get(0).addEcts(4);
        controller.getGame().getStudents().get(1).addEcts(2);
        controller.getGame().getStudents().get(2).addEcts(3);

        ScoreBoardPanel scoreBoard = new ScoreBoardPanel(controller);
        frame.add(scoreBoard);

        frame.setLocationByPlatform(true);
        frame.setVisible(true);

        System.out.println("✓ ScoreBoard Test: Shows current scores!");
    }
}
