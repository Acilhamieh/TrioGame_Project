package view.gui;

import controller.*;
import model.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Main game panel containing all gameplay components.
 * Coordinates between hand, lecture hall, scoreboard, and messages.
 *
 * @author Acil HAMIEH
 * @version 1.0
 */
public class GamePanel extends JPanel {
    private MainWindow mainWindow;
    private GameController gameController;

    // Sub-panels
    private HandPanel handPanel;
    private LectureHallPanel lectureHallPanel;
    private ScoreBoardPanel scoreBoardPanel;
    private MessagePanel messagePanel;

    // Control buttons
    private JButton formTrioButton;
    private JButton viewScoresButton;
    private JButton quitButton;

    // Selected cards
    private List<Card> selectedCards;

    /**
     * Constructor for GamePanel
     * @param mainWindow Reference to main window
     * @param gameController Game controller
     */
    public GamePanel(MainWindow mainWindow, GameController gameController) {
        this.mainWindow = mainWindow;
        this.gameController = gameController;
        this.selectedCards = new ArrayList<>();

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        createComponents();
    }

    /**
     * Create all components
     */
    private void createComponents() {
        // Top panel: Current player info and controls
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // Center panel: Lecture hall
        lectureHallPanel = new LectureHallPanel(this);
        add(lectureHallPanel, BorderLayout.CENTER);

        // Bottom panel: Player's hand
        handPanel = new HandPanel(this);
        add(handPanel, BorderLayout.SOUTH);

        // Right panel: Scoreboard and messages
        JPanel rightPanel = createRightPanel();
        add(rightPanel, BorderLayout.EAST);
    }

    /**
     * Create top panel with player info and controls
     * @return Top panel
     */
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(70, 130, 180)); // Steel Blue
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Player info panel
        JPanel infoPanel = createPlayerInfoPanel();
        panel.add(infoPanel, BorderLayout.CENTER);

        // Control buttons
        JPanel buttonPanel = createControlButtons();
        panel.add(buttonPanel, BorderLayout.EAST);

        return panel;
    }

    /**
     * Create player info panel
     * @return Player info panel
     */
    private JPanel createPlayerInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
        panel.setBackground(new Color(70, 130, 180));

        Student currentPlayer = gameController.getCurrentPlayer();
        Game game = gameController.getGame();

        // Current player
        JLabel playerLabel = new JLabel("Current Player: " + currentPlayer.getName());
        playerLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        playerLabel.setForeground(Color.WHITE);

        // ECTS progress
        JLabel ectsLabel = new JLabel("ECTS: " + currentPlayer.getEctsCredits() + "/6");
        ectsLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        ectsLabel.setForeground(Color.YELLOW);

        // Round info
        JLabel roundLabel = new JLabel("Round: " + game.getTurnManager().getRoundNumber() +
                " | Mode: " + game.getGameMode().getDisplayName());
        roundLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        roundLabel.setForeground(Color.WHITE);

        panel.add(playerLabel);
        panel.add(ectsLabel);
        panel.add(roundLabel);

        return panel;
    }

    /**
     * Create control buttons
     * @return Button panel
     */
    private JPanel createControlButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        panel.setBackground(new Color(70, 130, 180));

        // Form Trio button
        formTrioButton = new JButton("Form Trio");
        formTrioButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        formTrioButton.setPreferredSize(new Dimension(120, 35));
        formTrioButton.setBackground(new Color(34, 139, 34));
        formTrioButton.setForeground(Color.WHITE);
        formTrioButton.setEnabled(false);
        formTrioButton.addActionListener(e -> formTrio());

        // View Scores button
        viewScoresButton = new JButton("Scores");
        viewScoresButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        viewScoresButton.setPreferredSize(new Dimension(100, 35));
        viewScoresButton.addActionListener(e -> showDetailedScores());

        // Quit button
        quitButton = new JButton("Quit");
        quitButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        quitButton.setPreferredSize(new Dimension(100, 35));
        quitButton.setBackground(new Color(220, 20, 60));
        quitButton.setForeground(Color.WHITE);
        quitButton.addActionListener(e -> mainWindow.endGame());

        panel.add(formTrioButton);
        panel.add(viewScoresButton);
        panel.add(quitButton);

        return panel;
    }

    /**
     * Create right panel with scoreboard and messages
     * @return Right panel
     */
    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setPreferredSize(new Dimension(280, 0));
        panel.setBackground(new Color(245, 245, 245));

        // Scoreboard
        scoreBoardPanel = new ScoreBoardPanel(gameController);
        panel.add(scoreBoardPanel, BorderLayout.CENTER);

        // Message panel
        messagePanel = new MessagePanel();
        panel.add(messagePanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Handle card selection
     * @param card Selected card
     * @param isFromHand True if from hand, false if from lecture hall
     */
    public void onCardSelected(Card card, boolean isFromHand) {
        selectedCards.add(card);

        // Count cards from hand and hall
        int handCards = 0;
        int hallCards = 0;

        for (Card c : selectedCards) {
            if (gameController.getCurrentPlayer().getHand().contains(c)) {
                handCards++;
            } else {
                hallCards++;
            }
        }

        // Update message
        messagePanel.setMessage("Selected: " + handCards + " from hand, " +
                hallCards + " from hall");

        // Enable form trio button if we have 3 cards (2 from hand, 1 from hall)
        if (selectedCards.size() == 3) {
            if (handCards == 2 && hallCards == 1) {
                formTrioButton.setEnabled(true);
                messagePanel.setMessage("Ready! Click 'Form Trio'", MessagePanel.MessageType.INFO);
            } else {
                clearSelection();
                messagePanel.setMessage("Must select 2 from hand + 1 from hall!",
                        MessagePanel.MessageType.ERROR);
            }
        } else if (selectedCards.size() > 3) {
            clearSelection();
            messagePanel.setMessage("Too many cards! Select 3 only.",
                    MessagePanel.MessageType.ERROR);
        }
    }

    /**
     * Handle card deselection
     * @param card Deselected card
     */
    public void onCardDeselected(Card card) {
        selectedCards.remove(card);
        formTrioButton.setEnabled(false);

        if (selectedCards.isEmpty()) {
            messagePanel.setMessage("Select 3 cards to form a trio",
                    MessagePanel.MessageType.INFO);
        }
    }

    /**
     * Form trio with selected cards
     */
    private void formTrio() {
        if (selectedCards.size() != 3) {
            return;
        }

        // Find indices
        Hand hand = gameController.getCurrentPlayer().getHand();
        LectureHall hall = gameController.getGame().getLectureHall();

        int handIndex1 = -1, handIndex2 = -1, hallIndex = -1;
        int handCount = 0;

        for (Card card : selectedCards) {
            if (hand.contains(card)) {
                if (handCount == 0) {
                    handIndex1 = hand.getAllCards().indexOf(card);
                } else {
                    handIndex2 = hand.getAllCards().indexOf(card);
                }
                handCount++;
            } else {
                hallIndex = hall.getAllCards().indexOf(card);
            }
        }

        // Execute turn
        int[] indices = {handIndex1, handIndex2, hallIndex};
        boolean success = gameController.executeTurn(indices);

        if (success) {
            messagePanel.setMessage("Valid trio! +ECTS! You get a bonus turn!",
                    MessagePanel.MessageType.SUCCESS);
        } else {
            messagePanel.setMessage("Invalid trio. Turn passes to next player.",
                    MessagePanel.MessageType.ERROR);
        }

        // Clear selection and update display
        clearSelection();
        updateDisplay();

        // Check for victory
        checkVictory();
    }

    /**
     * Clear card selection
     */
    private void clearSelection() {
        selectedCards.clear();
        formTrioButton.setEnabled(false);
        handPanel.clearSelection();
        lectureHallPanel.clearSelection();
    }

    /**
     * Update all displays
     */
    public void updateDisplay() {
        // Update player info
        removeAll();
        createComponents();

        // Update panels
        handPanel.updateDisplay(gameController.getCurrentPlayer().getHand());
        lectureHallPanel.updateDisplay(gameController.getGame().getLectureHall());
        scoreBoardPanel.updateDisplay();

        revalidate();
        repaint();
    }

    /**
     * Check for victory condition
     */
    private void checkVictory() {
        Student winner = gameController.getGame().checkVictoryConditions();

        if (winner != null) {
            showVictoryDialog(winner);
        }
    }

    /**
     * Show victory dialog
     * @param winner Winning student
     */
    private void showVictoryDialog(Student winner) {
        String message = "🎓 GRADUATION COMPLETE! 🎓\n\n";

        if (gameController.getGame().getGameMode().isTeamMode() && winner.getTeam() != null) {
            Team team = winner.getTeam();
            message += "Winner: " + team.getTeamName() + "\n";
            message += "Team ECTS: " + team.getTeamScore() + "\n\n";
            message += "Members:\n";
            for (Student member : team.getMembers()) {
                message += "• " + member.getName() + ": " + member.getEctsCredits() + " ECTS\n";
            }
        } else {
            message += "Winner: " + winner.getName() + "\n";
            message += "ECTS: " + winner.getEctsCredits() + "\n";
            message += "Trios: " + winner.getTrioCount();
        }

        message += "\n\nCongratulations!";

        JOptionPane.showMessageDialog(this,
                message,
                "Game Over",
                JOptionPane.INFORMATION_MESSAGE);

        mainWindow.endGame();
    }

    /**
     * Show detailed scores dialog
     */
    private void showDetailedScores() {
        StringBuilder scores = new StringBuilder("📊 DETAILED SCORES\n\n");

        if (gameController.getGame().getGameMode().isTeamMode()) {
            for (Team team : gameController.getGame().getTeams()) {
                scores.append(team.getTeamName()).append(": ")
                        .append(team.getTeamScore()).append(" ECTS\n");
                for (Student member : team.getMembers()) {
                    scores.append("  • ").append(member.getName()).append(": ")
                            .append(member.getEctsCredits()).append(" ECTS\n");
                }
                scores.append("\n");
            }
        } else {
            for (Student student : gameController.getGame().getStudents()) {
                scores.append(student.getName()).append(": ")
                        .append(student.getEctsCredits()).append("/6 ECTS (")
                        .append(student.getTrioCount()).append(" trios)\n");
            }
        }

        JOptionPane.showMessageDialog(this,
                scores.toString(),
                "Detailed Scores",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Get game controller
     * @return Game controller
     */
    public GameController getGameController() {
        return gameController;
    }
}
