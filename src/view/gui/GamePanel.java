package view.gui;

import controller.GameController;
import model.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Main game panel with REVEALING SYSTEM + TEAM TRADING.
 *
 * @author Acil HAMIEH, Dana SLEIMAN
 * @version 5.2 - UI fix: Hide lecture hall in TEAM mode
 */
public class GamePanel extends JPanel {
    private final MainWindow mainWindow;
    private final GameController controller;

    // UI Components
    private HandPanel handPanel;
    private OtherPlayersPanel otherPlayersPanel;
    private LectureHallPanel lectureHallPanel;
    private JPanel lectureHallWrapper; // ✅ NEW

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
        applyGameModeVisibility(); // ✅ APPLY MODE VISIBILITY
        updateDisplay();
    }

    private void initializeComponents() {
        add(createTopPanel(), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        JScrollPane otherPlayersScroll = createOtherPlayersPanel();
        centerPanel.add(otherPlayersScroll);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        lectureHallWrapper = createLectureHallPanel();
        centerPanel.add(lectureHallWrapper);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        handPanel = new HandPanel(this);
        handPanel.setMinimumSize(new Dimension(0, 120));
        handPanel.setPreferredSize(new Dimension(0, 135));
        handPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        centerPanel.add(handPanel);

        add(centerPanel, BorderLayout.CENTER);
        add(createControlPanel(), BorderLayout.SOUTH);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                adjustLayoutForSize();
            }
        });
    }

    /**
     * ✅ NEW: Hide lecture hall in TEAM mode
     */
    private void applyGameModeVisibility() {
        if (controller.getGame().getGameMode().isTeamMode()) {
            lectureHallWrapper.setVisible(false);
        } else {
            lectureHallWrapper.setVisible(true);
        }
        revalidate();
        repaint();
    }

    private JScrollPane createOtherPlayersPanel() {
        otherPlayersPanel = new OtherPlayersPanel(this);
        JScrollPane scroll = new JScrollPane(otherPlayersPanel);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);
        scroll.setPreferredSize(new Dimension(0, 180));
        return scroll;
    }

    private JPanel createLectureHallPanel() {
        lectureHallPanel = new LectureHallPanel(this);
        lectureHallPanel.setPreferredSize(new Dimension(0, 350));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(lectureHallPanel, BorderLayout.CENTER);
        return wrapper;
    }

    private void adjustLayoutForSize() {
        int height = getHeight();
        if (height < 700) {
            otherPlayersPanel.setCompactMode(true);
            handPanel.setPreferredSize(new Dimension(0, 120));
        } else {
            otherPlayersPanel.setCompactMode(false);
            handPanel.setPreferredSize(new Dimension(0, 135));
        }
        revalidate();
        repaint();
    }

    private JPanel createTopPanel() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(70, 130, 180));
        top.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        playerLabel = new JLabel();
        playerLabel.setForeground(Color.WHITE);
        playerLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        instructionLabel = new JLabel("", SwingConstants.CENTER);
        instructionLabel.setForeground(Color.WHITE);

        scoreLabel = new JLabel();
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        top.add(playerLabel, BorderLayout.WEST);
        top.add(instructionLabel, BorderLayout.CENTER);
        top.add(scoreLabel, BorderLayout.EAST);
        return top;
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 8));
        panel.setBackground(new Color(220, 220, 220));

        clearRevealButton = new JButton("Clear & End Turn");
        clearRevealButton.addActionListener(e -> clearAndEndTurn());
        panel.add(clearRevealButton);

        JButton quit = new JButton("Quit");
        quit.setBackground(new Color(220, 20, 60));
        quit.setForeground(Color.WHITE);
        quit.addActionListener(e -> mainWindow.endGame());
        panel.add(quit);

        return panel;
    }

    public void updateDisplay() {
        Student current = controller.getCurrentPlayer();
        if (current == null) return;

        playerLabel.setText("Player: " + current.getName());
        scoreLabel.setText("ECTS: " + current.getEctsCredits() + " | Trios: " + current.getTrioCount());

        handPanel.updateDisplay(current.getHand());
        otherPlayersPanel.updateDisplay(controller.getGame().getStudents(), current);

        if (!controller.getGame().getGameMode().isTeamMode()) {
            lectureHallPanel.updateDisplay(controller.getGame().getLectureHall());
        }

        clearReveals();
        updateInstructionLabel();
    }

    /* ===== EVERYTHING BELOW IS UNCHANGED GAME/UI LOGIC ===== */

    public void onHandCardSelected(Card card, int position) {
        revealCard(card, "hand", null, position);
    }

    public void onOtherPlayerCardSelected(Card card, int position, String playerName) {
        revealCard(card, "other_player", playerName, position);
    }

    public void onHallCardSelected(Card card, int position) {
        revealCard(card, "hall", null, position);
    }

    private void revealCard(Card card, String source, String playerName, int position) {
        Student currentPlayer = controller.getCurrentPlayer();
        String result = controller.getGame().revealCard(currentPlayer, card, source, playerName, position);

        switch (result) {
            case "revealed":
                handleRevealed(card, source, playerName, position);
                break;
            case "trio_complete":
                handleTrioComplete(card, source, playerName, position, currentPlayer);
                break;
            case "mismatch":
                handleMismatch(card, source, playerName, position);
                break;
        }
    }

    private void handleRevealed(Card card, String source, String playerName, int position) {
        markCardRevealed(card, source, playerName, position);
        updateInstructionLabel();
    }

    private void handleTrioComplete(Card card, String source, String playerName, int position, Student currentPlayer) {
        markCardRevealed(card, source, playerName, position);
        controller.getGame().completeRevealedTrio(currentPlayer);
        updateDisplay();
        checkVictory();
    }

    private void handleMismatch(Card card, String source, String playerName, int position) {
        markCardRevealed(card, source, playerName, position);
        new Timer(2000, e -> {
            controller.getGame().handleMismatch();
            updateDisplay();
        }).start();
    }

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

    private void clearReveals() {
        handPanel.clearReveals();
        otherPlayersPanel.clearReveals();
        lectureHallPanel.clearReveals();
    }

    private void clearAndEndTurn() {
        controller.getGame().handleMismatch();
        updateDisplay();
    }

    private void updateInstructionLabel() {
        int count = controller.getGame().getRevealState().getRevealCount();
        instructionLabel.setText(count == 0 ? "Reveal 1st card: Click any card!"
                : "Revealing cards...");
    }

    private void checkVictory() {
        Student winner = controller.getGame().checkVictoryConditions();
        if (winner != null) {
            JOptionPane.showMessageDialog(this,
                    "🎉 " + winner.getName() + " wins!",
                    "Victory", JOptionPane.INFORMATION_MESSAGE);
            clearRevealButton.setEnabled(false);
        }
    }

    /**
     * Handle card deselected (required by CardSelectionListener)
     * Not used in revealing mode, but must exist for compilation
     */
    public void onCardDeselected(Card card, int position) {
        // No action needed in revealing mode
    }
}
