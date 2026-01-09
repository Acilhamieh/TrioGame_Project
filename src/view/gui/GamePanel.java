package view.gui;

import controller.GameController;
import model.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.util.List;


public class GamePanel extends JPanel {

    private final MainWindow mainWindow;
    private final GameController controller;

    private HandPanel handPanel;
    private OtherPlayersPanel otherPlayersPanel;
    private LectureHallPanel lectureHallPanel;
    private JPanel lectureHallWrapper;

    private JButton clearRevealButton;

    private JLabel playerLabel;
    private JLabel scoreLabel;
    private JLabel instructionLabel;

    private Timer flipBackTimer;


    private boolean mismatchInProgress = false;

    public GamePanel(MainWindow mainWindow, GameController controller) {
        this.mainWindow = mainWindow;
        this.controller = controller;

        setLayout(new BorderLayout(5, 5));
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        initializeComponents();
        applyGameModeVisibility();
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
        centerPanel.add(handPanel);

        add(centerPanel, BorderLayout.CENTER);
        add(createControlPanel(), BorderLayout.SOUTH);
    }

    private void applyGameModeVisibility() {
        lectureHallWrapper.setVisible(!controller.getGame().getGameMode().isTeamMode());
    }

    private JScrollPane createOtherPlayersPanel() {
        otherPlayersPanel = new OtherPlayersPanel(this);
        JScrollPane scroll = new JScrollPane(otherPlayersPanel);
        scroll.setBorder(null);
        return scroll;
    }

    private JPanel createLectureHallPanel() {
        lectureHallPanel = new LectureHallPanel(this);
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(lectureHallPanel, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel createTopPanel() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(70, 130, 180));

        playerLabel = new JLabel();
        playerLabel.setForeground(Color.WHITE);

        instructionLabel = new JLabel("", SwingConstants.CENTER);
        instructionLabel.setForeground(Color.WHITE);

        scoreLabel = new JLabel();
        scoreLabel.setForeground(Color.WHITE);

        top.add(playerLabel, BorderLayout.WEST);
        top.add(instructionLabel, BorderLayout.CENTER);
        top.add(scoreLabel, BorderLayout.EAST);

        return top;
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel();

        clearRevealButton = new JButton("Clear & End Turn");
        clearRevealButton.addActionListener(e -> clearAndEndTurn());
        panel.add(clearRevealButton);

        JButton quit = new JButton("Quit");
        quit.addActionListener(e -> mainWindow.endGame());
        panel.add(quit);

        return panel;
    }

    public void updateDisplay() {
        Student current = controller.getCurrentPlayer();
        if (current == null) return;

        mismatchInProgress = false; //  reset lock

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



    public void onHandCardSelected(Card card, int position) {
        if (mismatchInProgress) return;
        revealCard(card, "hand", null, position);
    }

    public void onOtherPlayerCardSelected(Card card, int position, String playerName) {
        if (mismatchInProgress) return;
        revealCard(card, "other_player", playerName, position);
    }

    public void onHallCardSelected(Card card, int position) {
        if (mismatchInProgress) return;
        revealCard(card, "hall", null, position);
    }

    private void revealCard(Card card, String source, String playerName, int position) {
        Student currentPlayer = controller.getCurrentPlayer();
        String result = controller.getGame().revealCard(currentPlayer, card, source, playerName, position);

        switch (result) {
            case "revealed":
            case "auto_reveal":
                markCardRevealed(card, source, playerName, position);
                updateInstructionLabel();
                break;

            case "trio_complete":
                handleTrioComplete(currentPlayer);
                break;

            case "mismatch":
                handleMismatch();
                break;
        }
    }

    private void handleTrioComplete(Student currentPlayer) {
        boolean success = controller.getGame().completeRevealedTrio(currentPlayer);
        if (success) {
            updateDisplay();
            checkVictory();
        }
    }

    private void handleMismatch() {
        if (mismatchInProgress) return;

        mismatchInProgress = true;
        instructionLabel.setText("Mismatch! Switching turn...");

        if (flipBackTimer != null && flipBackTimer.isRunning()) {
            flipBackTimer.stop();
        }

        flipBackTimer = new Timer(2000, e -> {
            controller.getGame().handleMismatch();
            updateDisplay();
        });
        flipBackTimer.setRepeats(false);
        flipBackTimer.start();
    }

    private void clearAndEndTurn() {
        if (mismatchInProgress) return;

        if (controller.getGame().getRevealState().getRevealCount() > 0) {
            controller.getGame().handleMismatch();
            updateDisplay();
        }
    }

    private void markCardRevealed(Card card, String source, String playerName, int position) {
        switch (source) {
            case "hand" -> handPanel.markCardRevealed(card, position);
            case "other_player" -> otherPlayersPanel.markCardRevealed(playerName, card, position);
            case "hall" -> lectureHallPanel.markCardRevealed(card, position);
        }
    }

    private void clearReveals() {
        handPanel.clearReveals();
        otherPlayersPanel.clearReveals();
        lectureHallPanel.clearReveals();
    }

    private void updateInstructionLabel() {
        int count = controller.getGame().getRevealState().getRevealCount();
        instructionLabel.setText(count == 0 ? "Reveal a card" : "Revealing...");
    }

    private void checkVictory() {
        Student winner = controller.getGame().checkVictoryConditions();
        if (winner != null) {
            JOptionPane.showMessageDialog(this,
                    winner.getName() + " wins!",
                    "Victory", JOptionPane.INFORMATION_MESSAGE);
            clearRevealButton.setEnabled(false);
        }
    }

    public void onCardDeselected(Card card, int position) {
        // required for interface
    }
}
