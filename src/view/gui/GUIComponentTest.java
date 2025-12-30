package view.gui;

import model.*;
import enums.*;
import javax.swing.*;
import java.awt.*;

/**
 * Visual test for individual GUI components - MEMORY GAME VERSION
 * Shows each component in isolation
 *
 * @author Acil HAMIEH, Dana SLEIMAN
 * @version 2.0 - Memory game compatible
 */
public class GUIComponentTest {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            testCardComponent();
            testCardComponentWithVisibility();
            testMessagePanel();
            testScoreBoard();
        });
    }

    /**
     * Test CardComponent display - VISIBLE cards
     */
    private static void testCardComponent() {
        JFrame frame = new JFrame("Card Component Test - Visible Cards");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(700, 300);
        frame.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 50));

        // Create sample cards with IDs
        Card card1 = new Card("SY41", Branch.COMPUTER_SCIENCE, 12);
        Card card2 = new Card("GI21", Branch.INDUSTRIAL_ENGINEERING, 8);
        Card card3 = new Card("MQ41", Branch.MECHANICAL_ENGINEERING, 4);
        Card card4 = new Card("EN21", Branch.ENERGY_ENGINEERING, 2);
        Card card5 = new Card("PFE", Branch.SPECIAL, 1);

        // NEW listener with position parameter
        CardComponent.CardSelectionListener listener = new CardComponent.CardSelectionListener() {
            @Override
            public void onCardSelected(Card card, int position) {
                System.out.println("Selected: " + card.getCourseCode() + " (ID:" + card.getId() + ", Pos:" + position + ")");
            }

            @Override
            public void onCardDeselected(Card card, int position) {
                System.out.println("Deselected: " + card.getCourseCode() + " (ID:" + card.getId() + ", Pos:" + position + ")");
            }
        };

        // Add visible cards
        frame.add(new CardComponent(card1, listener, true, 0));
        frame.add(new CardComponent(card2, listener, true, 1));
        frame.add(new CardComponent(card3, listener, true, 2));
        frame.add(new CardComponent(card4, listener, true, 3));
        frame.add(new CardComponent(card5, listener, true, 4));

        JLabel instructions = new JLabel("All cards VISIBLE with IDs. Click to select/deselect.");
        instructions.setFont(new Font("SansSerif", Font.BOLD, 14));
        frame.add(instructions);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        System.out.println("✓ Card Component Test (Visible): Try clicking cards!");
    }

    /**
     * Test CardComponent with HIDDEN cards
     */
    private static void testCardComponentWithVisibility() {
        JFrame frame = new JFrame("Card Component Test - Memory Game (Hidden Cards)");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 300);
        frame.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 50));

        // Create sample cards
        Card card1 = new Card("SY41", Branch.COMPUTER_SCIENCE, 12);
        Card card2 = new Card("IA41", Branch.COMPUTER_SCIENCE, 11);
        Card card3 = new Card("AP4B", Branch.COMPUTER_SCIENCE, 9);
        Card card4 = new Card("MQ41", Branch.MECHANICAL_ENGINEERING, 4);
        Card card5 = new Card("EN21", Branch.ENERGY_ENGINEERING, 2);
        Card card6 = new Card("PFE", Branch.SPECIAL, 1);

        CardComponent.CardSelectionListener listener = new CardComponent.CardSelectionListener() {
            @Override
            public void onCardSelected(Card card, int position) {
                System.out.println("Selected: Pos " + position + " - " +
                        (card != null ? card.getCourseCode() + " (ID:" + card.getId() + ")" : "Hidden"));
            }

            @Override
            public void onCardDeselected(Card card, int position) {
                System.out.println("Deselected: Pos " + position);
            }
        };

        // Simulate hand: First visible, middle hidden, last visible
        frame.add(new CardComponent(card1, listener, true, 0));   // VISIBLE
        frame.add(new CardComponent(card2, listener, false, 1));  // HIDDEN
        frame.add(new CardComponent(card3, listener, false, 2));  // HIDDEN
        frame.add(new CardComponent(card4, listener, false, 3));  // HIDDEN
        frame.add(new CardComponent(card5, listener, false, 4));  // HIDDEN
        frame.add(new CardComponent(card6, listener, true, 5));   // VISIBLE

        JLabel instructions = new JLabel("Memory Game Mode: First & Last VISIBLE, Middle HIDDEN [?]");
        instructions.setFont(new Font("SansSerif", Font.BOLD, 14));
        instructions.setForeground(new Color(220, 20, 60)); // Crimson
        frame.add(instructions);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        System.out.println("✓ Card Component Test (Hidden): See memory game visibility!");
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
        success.setMessage("Valid Trio! +2 ECTS!", MessagePanel.MessageType.SUCCESS);

        MessagePanel error = new MessagePanel();
        error.setMessage("Invalid Trio! Cards don't match!", MessagePanel.MessageType.ERROR);

        MessagePanel warning = new MessagePanel();
        warning.setMessage("Remember hidden cards!", MessagePanel.MessageType.WARNING);

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
                    info.setMessage("Turn: Player " + count[0], MessagePanel.MessageType.INFO);
                    break;
                case 1:
                    success.setMessage("Bonus turn! Success " + count[0], MessagePanel.MessageType.SUCCESS);
                    break;
                case 2:
                    error.setMessage("Turn passes. Error " + count[0], MessagePanel.MessageType.ERROR);
                    break;
                case 3:
                    warning.setMessage("Memory challenge! " + count[0], MessagePanel.MessageType.WARNING);
                    break;
            }
        });
        frame.add(changeButton);

        frame.setLocationByPlatform(true);
        frame.setVisible(true);

        System.out.println("✓ Message Panel Test: See different message types!");
    }

    /**
     * Test ScoreBoard with memory game
     */
    private static void testScoreBoard() {
        JFrame frame = new JFrame("ScoreBoard Test - Memory Game");
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

        System.out.println("✓ ScoreBoard Test: Shows current scores with memory game!");
    }
}
