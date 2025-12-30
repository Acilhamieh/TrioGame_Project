package view.gui;

import controller.GameController;
import model.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Main game panel for Trio_UTBM memory game.
 * NEW RULES: Select 1 from hand + 1 from ANY other player + 1 from hall
 *
 * @author Acil HAMIEH, Dana SLEIMAN
 * @version 3.1 - Fixed layout for visible lecture hall
 */
public class GamePanel extends JPanel {
    private MainWindow mainWindow;
    private GameController controller;

    // UI Components
    private JPanel topPanel;
    private HandPanel handPanel;
    private OtherPlayersPanel otherPlayersPanel;  // NEW! Shows ALL other players
    private LectureHallPanel lectureHallPanel;
    private JPanel controlPanel;

    // Control buttons
    private JButton formTrioButton;
    private JButton clearSelectionButton;
    private JButton quitButton;

    // Labels
    private JLabel playerLabel;
    private JLabel scoreLabel;
    private JLabel instructionLabel;

    // Selection tracking - NEW SYSTEM
    private Card selectedFromHand = null;
    private int selectedHandPosition = -1;
    private Card selectedFromOtherPlayer = null;
    private int selectedOtherPlayerPosition = -1;
    private String selectedOtherPlayerName = null;  // Track which player
    private Card selectedFromHall = null;
    private int selectedHallPosition = -1;

    /**
     * Constructor for GamePanel (compatible with MainWindow)
     * @param mainWindow Main window reference
     * @param controller Game controller
     */
    public GamePanel(MainWindow mainWindow, GameController controller) {
        this.mainWindow = mainWindow;
        this.controller = controller;

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initializeComponents();
        updateDisplay();
    }

    /**
     * Initialize all UI components
     * FIXED: Use GridLayout for equal space distribution
     */
    private void initializeComponents() {
        // Top panel (player info)
        createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // Center panel - FIXED: Use GridLayout(3 rows) for equal space
        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        centerPanel.setOpaque(false);

        // Other players' hands (top) - with scroll if needed
        otherPlayersPanel = new OtherPlayersPanel(this);
        JScrollPane otherPlayersScroll = new JScrollPane(otherPlayersPanel);
        otherPlayersScroll.setPreferredSize(new Dimension(0, 220));
        otherPlayersScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        otherPlayersScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        otherPlayersScroll.setBorder(null);
        centerPanel.add(otherPlayersScroll);

        // Lecture hall (middle) - NOW HAS EQUAL SPACE!
        lectureHallPanel = new LectureHallPanel(this);
        centerPanel.add(lectureHallPanel);

        // Your hand (bottom)
        handPanel = new HandPanel(this);
        centerPanel.add(handPanel);

        add(centerPanel, BorderLayout.CENTER);

        // Control panel
        createControlPanel();
        add(controlPanel, BorderLayout.SOUTH);
    }

    /**
     * Create top panel with player info
     */
    private void createTopPanel() {
        topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(70, 130, 180));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Left: Player name
        playerLabel = new JLabel("Player: Loading...");
        playerLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        playerLabel.setForeground(Color.WHITE);
        topPanel.add(playerLabel, BorderLayout.WEST);

        // Center: Instructions
        instructionLabel = new JLabel("Select: 1 from YOUR hand + 1 from ANY other player + 1 from HALL");
        instructionLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        instructionLabel.setForeground(Color.WHITE);
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(instructionLabel, BorderLayout.CENTER);

        // Right: Score
        scoreLabel = new JLabel("ECTS: 0 | Trios: 0");
        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        scoreLabel.setForeground(Color.WHITE);
        topPanel.add(scoreLabel, BorderLayout.EAST);
    }

    /**
     * Create control panel with buttons
     */
    private void createControlPanel() {
        controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controlPanel.setBackground(new Color(220, 220, 220));

        // Form Trio button
        formTrioButton = new JButton("Form Trio");
        formTrioButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        formTrioButton.setPreferredSize(new Dimension(150, 40));
        formTrioButton.setEnabled(false);
        formTrioButton.addActionListener(e -> formTrio());
        controlPanel.add(formTrioButton);

        // Clear Selection button
        clearSelectionButton = new JButton("Clear Selection");
        clearSelectionButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        clearSelectionButton.setPreferredSize(new Dimension(150, 40));
        clearSelectionButton.addActionListener(e -> clearAllSelections());
        controlPanel.add(clearSelectionButton);

        // Quit button
        quitButton = new JButton("Quit Game");
        quitButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        quitButton.setPreferredSize(new Dimension(120, 40));
        quitButton.setBackground(new Color(220, 20, 60)); // Crimson
        quitButton.setForeground(Color.WHITE);
        quitButton.addActionListener(e -> {
            if (mainWindow != null) {
                mainWindow.endGame();
            }
        });
        controlPanel.add(quitButton);
    }

    /**
     * Update entire display
     * FIXED: Show ALL cards in own hand, show all other players
     */
    public void updateDisplay() {
        Student currentPlayer = controller.getCurrentPlayer();
        if (currentPlayer == null) return;

        // Update player info
        playerLabel.setText("Player: " + currentPlayer.getName());
        scoreLabel.setText("ECTS: " + currentPlayer.getEctsCredits() +
                " | Trios: " + currentPlayer.getTrioCount());

        // Update own hand - SHOW ALL CARDS
        handPanel.updateDisplayAllVisible(currentPlayer.getHand());

        // Update other players' hands - NEW! Show ALL other players
        List<Student> allPlayers = controller.getGame().getStudents();
        otherPlayersPanel.updateDisplay(allPlayers, currentPlayer);

        // Update lecture hall
        lectureHallPanel.updateDisplay(controller.getGame().getLectureHall());

        // Clear selections
        clearAllSelections();
    }

    /**
     * Handle card selected from YOUR hand
     */
    public void onHandCardSelected(Card card, int position) {
        // Only allow 1 card from hand
        if (selectedFromHand != null && !selectedFromHand.equals(card)) {
            clearHandSelection();
        }

        selectedFromHand = card;
        selectedHandPosition = position;
        updateSelectionStatus();
    }

    /**
     * Handle card selected from ANY other player's hand
     * NEW METHOD for showing all players
     */
    public void onOtherPlayerCardSelected(Card card, int position, String playerName) {
        // Only allow 1 card from other players
        if (selectedFromOtherPlayer != null && !selectedFromOtherPlayer.equals(card)) {
            clearOtherPlayersSelection();
        }

        selectedFromOtherPlayer = card;
        selectedOtherPlayerPosition = position;
        selectedOtherPlayerName = playerName;
        updateSelectionStatus();
    }

    /**
     * Handle card selected from LECTURE HALL
     */
    public void onHallCardSelected(Card card, int position) {
        // Only allow 1 card from hall
        if (selectedFromHall != null && !selectedFromHall.equals(card)) {
            clearHallSelection();
        }

        selectedFromHall = card;
        selectedHallPosition = position;
        updateSelectionStatus();
    }

    /**
     * Handle card deselected
     */
    public void onCardDeselected(Card card, int position) {
        if (card.equals(selectedFromHand)) {
            selectedFromHand = null;
            selectedHandPosition = -1;
        } else if (card.equals(selectedFromOtherPlayer)) {
            selectedFromOtherPlayer = null;
            selectedOtherPlayerPosition = -1;
            selectedOtherPlayerName = null;
        } else if (card.equals(selectedFromHall)) {
            selectedFromHall = null;
            selectedHallPosition = -1;
        }
        updateSelectionStatus();
    }

    /**
     * Update selection status and button state
     */
    private void updateSelectionStatus() {
        int selectedCount = 0;
        if (selectedFromHand != null) selectedCount++;
        if (selectedFromOtherPlayer != null) selectedCount++;
        if (selectedFromHall != null) selectedCount++;

        // Update instruction label
        StringBuilder instruction = new StringBuilder("Selected: ");
        if (selectedFromHand != null) {
            instruction.append("✅ Hand(").append(selectedFromHand.getCourseCode()).append(") ");
        } else {
            instruction.append("❌ Hand ");
        }
        if (selectedFromOtherPlayer != null) {
            instruction.append("✅ ").append(selectedOtherPlayerName).append("(").append(selectedFromOtherPlayer.getCourseCode()).append(") ");
        } else {
            instruction.append("❌ Other Player ");
        }
        if (selectedFromHall != null) {
            instruction.append("✅ Hall(").append(selectedFromHall.getCourseCode()).append(")");
        } else {
            instruction.append("❌ Hall");
        }

        instructionLabel.setText(instruction.toString());

        // Enable form trio button only when all 3 selected
        formTrioButton.setEnabled(selectedCount == 3);
    }

    /**
     * Form trio with selected cards
     * NEW: Uses 1 from hand + 1 from ANY other player + 1 from hall
     */
    private void formTrio() {
        if (selectedFromHand == null || selectedFromOtherPlayer == null || selectedFromHall == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select:\n" +
                            "- 1 card from YOUR hand\n" +
                            "- 1 card from ANY other player\n" +
                            "- 1 card from HALL",
                    "Incomplete Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Find the selected other player
        Student currentPlayer = controller.getCurrentPlayer();
        Student selectedPlayer = null;
        for (Student player : controller.getGame().getStudents()) {
            if (player.getName().equals(selectedOtherPlayerName)) {
                selectedPlayer = player;
                break;
            }
        }

        if (selectedPlayer == null) {
            JOptionPane.showMessageDialog(this,
                    "Error: Could not find player " + selectedOtherPlayerName,
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create list: [from hand, from other player, from hall]
        List<Card> selectedCards = Arrays.asList(
                selectedFromHand,
                selectedFromOtherPlayer,
                selectedFromHall
        );

        // Execute turn with validation
        boolean success = controller.getGame().playTurnWithPlayer(
                currentPlayer,
                selectedPlayer,
                selectedCards
        );

        if (success) {
            // Valid trio!
            JOptionPane.showMessageDialog(this,
                    "Valid Trio! " + selectedFromHand.getCourseCode() + " × 3\n" +
                            "+2 ECTS! You get a BONUS TURN!",
                    "Success!", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Invalid trio
            JOptionPane.showMessageDialog(this,
                    "Invalid Trio!\n" +
                            "Cards: " + selectedFromHand.getCourseCode() + ", " +
                            selectedFromOtherPlayer.getCourseCode() + ", " +
                            selectedFromHall.getCourseCode() + "\n" +
                            "Turn passes to next player.",
                    "Invalid", JOptionPane.ERROR_MESSAGE);
        }

        // Update display
        updateDisplay();

        // Check victory
        checkVictory();
    }

    /**
     * Clear all selections
     */
    private void clearAllSelections() {
        selectedFromHand = null;
        selectedHandPosition = -1;
        selectedFromOtherPlayer = null;
        selectedOtherPlayerPosition = -1;
        selectedOtherPlayerName = null;
        selectedFromHall = null;
        selectedHallPosition = -1;

        handPanel.clearSelection();
        otherPlayersPanel.clearSelection();
        lectureHallPanel.clearSelection();

        updateSelectionStatus();
    }

    /**
     * Clear hand selection only
     */
    private void clearHandSelection() {
        handPanel.clearSelection();
        selectedFromHand = null;
        selectedHandPosition = -1;
    }

    /**
     * Clear other players selection only
     */
    private void clearOtherPlayersSelection() {
        otherPlayersPanel.clearSelection();
        selectedFromOtherPlayer = null;
        selectedOtherPlayerPosition = -1;
        selectedOtherPlayerName = null;
    }

    /**
     * Clear hall selection only
     */
    private void clearHallSelection() {
        lectureHallPanel.clearSelection();
        selectedFromHall = null;
        selectedHallPosition = -1;
    }

    /**
     * Check for victory condition
     */
    private void checkVictory() {
        Student winner = controller.getGame().checkVictoryConditions();
        if (winner != null) {
            JOptionPane.showMessageDialog(this,
                    "🎉 CONGRATULATIONS! 🎉\n\n" +
                            winner.getName() + " has graduated!\n" +
                            "Total ECTS: " + winner.getEctsCredits() + "\n" +
                            "Trios completed: " + winner.getTrioCount(),
                    "Game Over - Victory!",
                    JOptionPane.INFORMATION_MESSAGE);

            // Disable buttons
            formTrioButton.setEnabled(false);
            clearSelectionButton.setEnabled(false);
        }
    }
}
