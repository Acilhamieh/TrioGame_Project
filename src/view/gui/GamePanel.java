package view.gui;

import controller.GameController;
import model.*;
import javax.swing.*;
import javax.swing.Timer;  // EXPLICIT: Use Swing Timer, not util.Timer
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Main game panel with REVEALING SYSTEM + TEAM TRADING.
 * Players reveal cards one by one, checking for trios.
 * In team mode, opposing teams can trade after each trio.
 *
 * @author Acil HAMIEH, Dana SLEIMAN
 * @version 5.1 - Code cleanup + warnings fixed
 */
public class GamePanel extends JPanel {
    private final MainWindow mainWindow;
    private final GameController controller;

    // UI Components
    private HandPanel handPanel;
    private OtherPlayersPanel otherPlayersPanel;
    private LectureHallPanel lectureHallPanel;

    // Control buttons
    private JButton clearRevealButton;

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
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // Center panel - RESPONSIVE
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        // Other players
        JScrollPane otherPlayersScroll = createOtherPlayersPanel();
        centerPanel.add(otherPlayersScroll);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        // Lecture hall
        JPanel hallWrapper = createLectureHallPanel();
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
        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.SOUTH);

        // Responsive sizing
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                adjustLayoutForSize();
            }
        });
    }

    /**
     * ✅ EXTRACTED METHOD: Create other players scroll panel
     */
    private JScrollPane createOtherPlayersPanel() {
        otherPlayersPanel = new OtherPlayersPanel(this);
        JScrollPane otherPlayersScroll = new JScrollPane(otherPlayersPanel);
        otherPlayersScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        otherPlayersScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        otherPlayersScroll.setBorder(null);
        otherPlayersScroll.setMinimumSize(new Dimension(0, 150));
        otherPlayersScroll.setPreferredSize(new Dimension(0, 180));
        otherPlayersScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
        return otherPlayersScroll;
    }

    /**
     * ✅ EXTRACTED METHOD: Create lecture hall panel
     */
    private JPanel createLectureHallPanel() {
        lectureHallPanel = new LectureHallPanel(this);
        lectureHallPanel.setMinimumSize(new Dimension(0, 200));
        lectureHallPanel.setPreferredSize(new Dimension(0, 350));
        JPanel hallWrapper = new JPanel(new BorderLayout());
        hallWrapper.setOpaque(false);
        hallWrapper.add(lectureHallPanel, BorderLayout.CENTER);
        return hallWrapper;
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

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
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

        return topPanel;
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 8));
        controlPanel.setBackground(new Color(220, 220, 220));

        clearRevealButton = new JButton("Clear & End Turn");
        clearRevealButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        clearRevealButton.setPreferredSize(new Dimension(150, 35));
        clearRevealButton.addActionListener(e -> clearAndEndTurn());
        controlPanel.add(clearRevealButton);

        JButton quitButton = new JButton("Quit");
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

        return controlPanel;
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

        // ✅ FIXED: Use switch instead of if statements
        switch (result) {
            case "revealed":
                handleRevealed(card, source, playerName, position);
                break;

            case "auto_reveal":
                handleAutoReveal(card, source, playerName, position, currentPlayer);
                break;

            case "trio_complete":
                handleTrioComplete(card, source, playerName, position, currentPlayer);
                break;

            case "mismatch":
                handleMismatch(card, source, playerName, position);
                break;

            case "invalid":
                handleInvalid();
                break;

            default:
                System.err.println("Unknown reveal result: " + result);
                break;
        }
    }

    /**
     * ✅ EXTRACTED: Handle normal reveal
     */
    private void handleRevealed(Card card, String source, String playerName, int position) {
        markCardRevealed(card, source, playerName, position);
        updateInstructionLabel();
    }

    /**
     * ✅ EXTRACTED: Handle auto-reveal of duplicates
     */
    private void handleAutoReveal(Card card, String source, String playerName, int position, Student currentPlayer) {
        // Mark original card + auto-revealed duplicates
        markCardRevealed(card, source, playerName, position);

        // Mark all auto-revealed cards
        RevealState revealState = controller.getGame().getRevealState();
        for (RevealState.RevealedCard rc : revealState.getRevealedCards()) {
            markCardRevealed(rc.card, rc.source, rc.playerName, rc.position);
        }

        // Check if trio complete after auto-reveal
        if (revealState.getRevealCount() == 3 && revealState.isValidTrio()) {
            instructionLabel.setText("AUTO-TRIO! All duplicates found!");

            // Auto-complete the trio
            boolean success = controller.getGame().completeRevealedTrio(currentPlayer);
            if (success) {
                String courseCode = revealState.getRevealedCourseCode();
                JOptionPane.showMessageDialog(this,
                        "AUTO-TRIO! " + courseCode + " × 3\nAll duplicates revealed!\n+2 ECTS! Bonus turn!",
                        "Auto Success!", JOptionPane.INFORMATION_MESSAGE);

                updateDisplay();

                // Team mode trading
                if (controller.getGame().getGameMode().isTeamMode()) {
                    showTradingForOpposingTeams(currentPlayer);
                }

                checkVictory();
            }
        } else {
            updateInstructionLabel();
        }
    }

    /**
     * ✅ EXTRACTED: Handle trio completion
     */
    private void handleTrioComplete(Card card, String source, String playerName, int position, Student currentPlayer) {
        // Mark card revealed
        markCardRevealed(card, source, playerName, position);
        instructionLabel.setText("TRIO! Processing...");

        // Complete trio in backend
        boolean success = controller.getGame().completeRevealedTrio(currentPlayer);

        if (success) {
            // Show success message
            RevealState revealState = controller.getGame().getRevealState();
            String courseCode = revealState.getRevealedCourseCode();

            JOptionPane.showMessageDialog(this,
                    "Valid Trio! " + courseCode + " × 3\n+2 ECTS! Bonus turn!",
                    "Success!", JOptionPane.INFORMATION_MESSAGE);

            // Update display (cards removed, hands refilled)
            updateDisplay();

            // ✅ TEAM MODE: After trio, opposing teams can trade
            if (controller.getGame().getGameMode().isTeamMode()) {
                showTradingForOpposingTeams(currentPlayer);
            }

            checkVictory();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Error processing trio!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * ✅ EXTRACTED: Handle mismatch
     */
    private void handleMismatch(Card card, String source, String playerName, int position) {
        // Mark last card revealed, then flip back after 2 seconds
        markCardRevealed(card, source, playerName, position);
        instructionLabel.setText("Cards don't match! Flipping back...");

        // Disable all cards
        disableAllCards();

        // Schedule flip back
        scheduleFlipBack();
    }

    /**
     * ✅ EXTRACTED: Handle invalid reveal
     */
    private void handleInvalid() {
        JOptionPane.showMessageDialog(this,
                "Invalid reveal! Card not accessible.",
                "Invalid", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Mark a card as revealed in UI
     */
    private void markCardRevealed(Card card, String source, String playerName, int position) {
        switch (source) {
            case "hand":
                handPanel.markCardRevealed(card, position);
                break;
            case "other_player":
                otherPlayersPanel.markCardRevealed(playerName, card, position);
                break;
            case "hall":
                lectureHallPanel.markCardRevealed(card, position);
                break;
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
        RevealState revealState = controller.getGame().getRevealState();

        if (revealState.getRevealCount() > 0) {
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
        RevealState revealState = controller.getGame().getRevealState();
        int count = revealState.getRevealCount();

        if (count == 0) {
            instructionLabel.setText("Reveal 1st card: Click any card!");
        } else if (count == 1) {
            String courseCode = revealState.getRevealedCourseCode();
            instructionLabel.setText("Revealed 1/3: " + courseCode + " - Click 2nd card!");
        } else if (count == 2) {
            String courseCode = revealState.getRevealedCourseCode();
            instructionLabel.setText("Revealed 2/3: " + courseCode + " - Click 3rd card!");
        }
    }

    /**
     * ✅ RENAMED: Disable all cards (was always called with false)
     */
    private void disableAllCards() {
        handPanel.setCardsEnabled(false);
        otherPlayersPanel.setCardsEnabled(false);
        lectureHallPanel.setCardsEnabled(false);
    }

    /**
     * ✅ FIXED: Check victory with proper team mode support
     */
    private void checkVictory() {
        Student winner = controller.getGame().checkVictoryConditions();
        if (winner != null) {
            String message;

            // Check if team mode
            if (controller.getGame().getGameMode().isTeamMode() && winner.getTeam() != null) {
                Team team = winner.getTeam();
                List<Student> members = team.getMembers();

                // Count combined trios
                int totalTrios = 0;
                for (Student member : members) {
                    totalTrios += member.getTrioCount();
                }

                message = "🎉 CONGRATULATIONS! 🎉\n\n" +
                        team.getTeamName() + " graduated!\n" +
                        "Members: " + members.get(0).getName() + " & " + members.get(1).getName() + "\n" +
                        "Team ECTS: " + team.getTeamScore() + " | Team Trios: " + totalTrios;
            } else {
                message = "🎉 CONGRATULATIONS! 🎉\n\n" +
                        winner.getName() + " graduated!\n" +
                        "ECTS: " + winner.getEctsCredits() + " | Trios: " + winner.getTrioCount();
            }

            JOptionPane.showMessageDialog(this,
                    message,
                    "Victory!",
                    JOptionPane.INFORMATION_MESSAGE);

            clearRevealButton.setEnabled(false);
        }
    }

    /**
     * ✅ NEW: Show trading dialogs for opposing teams after a trio is won
     */
    private void showTradingForOpposingTeams(Student playerWhoWonTrio) {
        Team winningTeam = playerWhoWonTrio.getTeam();
        if (winningTeam == null) return;

        List<Team> allTeams = controller.getGame().getTeams();

        // Each opposing team can trade
        for (Team team : allTeams) {
            if (!team.equals(winningTeam)) {
                List<Student> members = team.getMembers();
                if (members.size() == 2) {
                    // Show trading dialog
                    int choice = JOptionPane.showConfirmDialog(this,
                            team.getTeamName() + " - Would you like to trade cards?",
                            "Trading Opportunity",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);

                    if (choice == JOptionPane.YES_OPTION) {
                        TradingDialog tradingDialog = new TradingDialog(
                                (Frame) SwingUtilities.getWindowAncestor(this),
                                members.get(0),
                                members.get(1));
                        tradingDialog.setVisible(true);

                        if (tradingDialog.wasTradeMade()) {
                            // Update display after trade
                            updateDisplay();
                        }
                    }
                }
            }
        }
    }
}
