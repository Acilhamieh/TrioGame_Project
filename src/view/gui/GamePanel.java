package view.gui;

import controller.GameController;
import model.*;
import javax.swing.*;
import javax.swing.Timer;  // EXPLICIT: Use Swing Timer, not util.Timer
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * Main game panel with REVEALING SYSTEM.
 * Players reveal cards one by one, checking for trios.
 *
 * @author Acil HAMIEH, Dana SLEIMAN
 * @version 4.0 - Complete revealing system
 */
public class GamePanel extends JPanel {
    private final MainWindow mainWindow;
    private final GameController controller;

    // UI Components
    private JPanel topPanel;
    private HandPanel handPanel;
    private OtherPlayersPanel otherPlayersPanel;
    private LectureHallPanel lectureHallPanel;
    private JPanel controlPanel;

    // Control buttons
    private JButton clearRevealButton;
    private JButton quitButton;

    // Labels
    private JLabel playerLabel;
    private JLabel scoreLabel;
    private JLabel instructionLabel;

    // Revealing state
    private Timer flipBackTimer;

    public GamePanel(MainWindow mainWindow, GameController controller) {
        this.mainWindow = mainWindow;
        this.controller = controller;

        setLayout(new BorderLayout(5, 5));
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        initializeComponents();
        updateDisplay();
    }

    private void initializeComponents() {
        createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // Center panel - RESPONSIVE
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        // Other players
        otherPlayersPanel = new OtherPlayersPanel(this);
        JScrollPane otherPlayersScroll = new JScrollPane(otherPlayersPanel);
        otherPlayersScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        otherPlayersScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        otherPlayersScroll.setBorder(null);
        otherPlayersScroll.setMinimumSize(new Dimension(0, 150));
        otherPlayersScroll.setPreferredSize(new Dimension(0, 180));
        otherPlayersScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
        centerPanel.add(otherPlayersScroll);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        // Lecture hall
        lectureHallPanel = new LectureHallPanel(this);
        lectureHallPanel.setMinimumSize(new Dimension(0, 200));
        lectureHallPanel.setPreferredSize(new Dimension(0, 350));
        JPanel hallWrapper = new JPanel(new BorderLayout());
        hallWrapper.setOpaque(false);
        hallWrapper.add(lectureHallPanel, BorderLayout.CENTER);
        centerPanel.add(hallWrapper);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        // Your hand
        handPanel = new HandPanel(this);
        handPanel.setMinimumSize(new Dimension(0, 120));
        handPanel.setPreferredSize(new Dimension(0, 135));
        handPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        centerPanel.add(handPanel);

        add(centerPanel, BorderLayout.CENTER);

        // Control panel
        createControlPanel();
        add(controlPanel, BorderLayout.SOUTH);

        // Responsive sizing
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                adjustLayoutForSize();
            }
        });
    }

    private void adjustLayoutForSize() {
        int height = getHeight();
        if (height < 700) {
            otherPlayersPanel.setCompactMode(true);
            handPanel.setPreferredSize(new Dimension(0, 120));
        } else if (height < 900) {
            otherPlayersPanel.setCompactMode(false);
            handPanel.setPreferredSize(new Dimension(0, 135));
        } else {
            otherPlayersPanel.setCompactMode(false);
            handPanel.setPreferredSize(new Dimension(0, 145));
        }
        revalidate();
        repaint();
    }

    private void createTopPanel() {
        topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(70, 130, 180));
        topPanel.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        playerLabel = new JLabel("Player: Loading...");
        playerLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        playerLabel.setForeground(Color.WHITE);
        topPanel.add(playerLabel, BorderLayout.WEST);

        instructionLabel = new JLabel("Click cards to reveal them!");
        instructionLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        instructionLabel.setForeground(Color.WHITE);
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(instructionLabel, BorderLayout.CENTER);

        scoreLabel = new JLabel("ECTS: 0 | Trios: 0");
        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        scoreLabel.setForeground(Color.WHITE);
        topPanel.add(scoreLabel, BorderLayout.EAST);
    }

    private void createControlPanel() {
        controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 8));
        controlPanel.setBackground(new Color(220, 220, 220));

        clearRevealButton = new JButton("Clear & End Turn");
        clearRevealButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        clearRevealButton.setPreferredSize(new Dimension(150, 35));
        clearRevealButton.addActionListener(e -> clearAndEndTurn());
        controlPanel.add(clearRevealButton);

        quitButton = new JButton("Quit");
        quitButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        quitButton.setPreferredSize(new Dimension(100, 35));
        quitButton.setBackground(new Color(220, 20, 60));
        quitButton.setForeground(Color.WHITE);
        quitButton.addActionListener(e -> {
            if (mainWindow != null) {
                mainWindow.endGame();
            }
        });
        controlPanel.add(quitButton);
    }

    public void updateDisplay() {
        Student currentPlayer = controller.getCurrentPlayer();
        if (currentPlayer == null) return;

        playerLabel.setText("Player: " + currentPlayer.getName());
        scoreLabel.setText("ECTS: " + currentPlayer.getEctsCredits() +
                " | Trios: " + currentPlayer.getTrioCount());

        handPanel.updateDisplay(currentPlayer.getHand());
        List<Student> allPlayers = controller.getGame().getStudents();
        otherPlayersPanel.updateDisplay(allPlayers, currentPlayer);
        lectureHallPanel.updateDisplay(controller.getGame().getLectureHall());

        clearReveals();
        updateInstructionLabel();
    }

    /**
     * Handle card selected from YOUR hand
     */
    public void onHandCardSelected(Card card, int position) {
        revealCard(card, "hand", null, position);
    }

    /**
     * Handle card selected from OTHER player
     */
    public void onOtherPlayerCardSelected(Card card, int position, String playerName) {
        revealCard(card, "other_player", playerName, position);
    }

    /**
     * Handle card selected from LECTURE HALL
     */
    public void onHallCardSelected(Card card, int position) {
        revealCard(card, "hall", null, position);
    }

    /**
     * Handle card deselected (not used in revealing mode)
     */
    public void onCardDeselected(Card card, int position) {
        // Not used in revealing mode - cards are clicked to reveal, not selected
    }

    /**
     * CORE: Reveal a card
     */
    private void revealCard(Card card, String source, String playerName, int position) {
        Student currentPlayer = controller.getCurrentPlayer();

        // Call backend
        String result = controller.getGame().revealCard(currentPlayer, card, source, playerName, position);

        // Update UI based on result
        switch (result) {
            case "revealed":
                // Mark card as revealed in UI
                markCardRevealed(card, source, playerName, position);
                updateInstructionLabel();
                break;

            case "mismatch":
                // Mark last card revealed, then flip back after 2 seconds
                markCardRevealed(card, source, playerName, position);
                instructionLabel.setText("Cards don't match! Flipping back...");

                // Disable all cards
                setAllCardsEnabled(false);

                // Schedule flip back
                scheduleFlipBack();
                break;

            case "trio_complete":
                // Mark card revealed
                markCardRevealed(card, source, playerName, position);
                instructionLabel.setText("TRIO! Processing...");

                // Complete trio in backend
                boolean success = controller.getGame().completeRevealedTrio(currentPlayer);

                if (success) {
                    // Show success message
                    RevealState state = controller.getGame().getRevealState();
                    String courseCode = state.getRevealedCourseCode();

                    JOptionPane.showMessageDialog(this,
                            "Valid Trio! " + courseCode + " × 3\n+2 ECTS! Bonus turn!",
                            "Success!", JOptionPane.INFORMATION_MESSAGE);

                    // Update display (cards removed, hands refilled)
                    updateDisplay();
                    checkVictory();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Error processing trio!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
                break;

            case "invalid":
                JOptionPane.showMessageDialog(this,
                        "Invalid reveal! Card not accessible.",
                        "Invalid", JOptionPane.WARNING_MESSAGE);
                break;
        }
    }

    /**
     * Mark a card as revealed in UI
     */
    private void markCardRevealed(Card card, String source, String playerName, int position) {
        if (source.equals("hand")) {
            handPanel.markCardRevealed(card, position);
        } else if (source.equals("other_player")) {
            otherPlayersPanel.markCardRevealed(playerName, card, position);
        } else if (source.equals("hall")) {
            lectureHallPanel.markCardRevealed(card, position);
        }
    }

    /**
     * Schedule flip back after mismatch (2 seconds)
     */
    private void scheduleFlipBack() {
        if (flipBackTimer != null) {
            flipBackTimer.stop();
        }

        flipBackTimer = new Timer(2000, e -> handleMismatchFlipBack());
        flipBackTimer.setRepeats(false);
        flipBackTimer.start();
    }

    /**
     * Handle mismatch: flip cards back, end turn
     */
    private void handleMismatchFlipBack() {
        // Tell backend to handle mismatch (advances turn)
        controller.getGame().handleMismatch();

        // Update display for next player
        updateDisplay();

        JOptionPane.showMessageDialog(this,
                "Turn passes to next player.",
                "Mismatch", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Clear reveals from UI
     */
    private void clearReveals() {
        handPanel.clearReveals();
        otherPlayersPanel.clearReveals();
        lectureHallPanel.clearReveals();
    }

    /**
     * Clear and end turn manually
     */
    private void clearAndEndTurn() {
        RevealState state = controller.getGame().getRevealState();

        if (state.getRevealCount() > 0) {
            int choice = JOptionPane.showConfirmDialog(this,
                    "End turn without completing trio?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                controller.getGame().handleMismatch();
                updateDisplay();
            }
        }
    }

    /**
     * Update instruction label based on reveal state
     */
    private void updateInstructionLabel() {
        RevealState state = controller.getGame().getRevealState();
        int count = state.getRevealCount();

        if (count == 0) {
            instructionLabel.setText("Reveal 1st card: Click any card!");
        } else if (count == 1) {
            String courseCode = state.getRevealedCourseCode();
            instructionLabel.setText("Revealed 1/3: " + courseCode + " - Click 2nd card!");
        } else if (count == 2) {
            String courseCode = state.getRevealedCourseCode();
            instructionLabel.setText("Revealed 2/3: " + courseCode + " - Click 3rd card!");
        }
    }

    /**
     * Enable/disable all cards
     */
    private void setAllCardsEnabled(boolean enabled) {
        handPanel.setCardsEnabled(enabled);
        otherPlayersPanel.setCardsEnabled(enabled);
        lectureHallPanel.setCardsEnabled(enabled);
    }

    private void checkVictory() {
        Student winner = controller.getGame().checkVictoryConditions();
        if (winner != null) {
            JOptionPane.showMessageDialog(this,
                    "🎉 CONGRATULATIONS! 🎉\n\n" +
                            winner.getName() + " graduated!\n" +
                            "ECTS: " + winner.getEctsCredits() + " | Trios: " + winner.getTrioCount(),
                    "Victory!",
                    JOptionPane.INFORMATION_MESSAGE);

            clearRevealButton.setEnabled(false);
        }
    }
}
