package view.gui;

import controller.GameController;
import model.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Main game panel for Trio_UTBM memory game.
 * FLEXIBLE RULES: Select ANY 3 cards from anywhere!
 * RESPONSIVE: Adapts to different screen sizes
 *
 * @author Acil HAMIEH, Dana SLEIMAN
 * @version 4.0 - Flexible selection + Responsive design
 */
public class GamePanel extends JPanel {
    private MainWindow mainWindow;
    private GameController controller;

    // UI Components
    private JPanel topPanel;
    private HandPanel handPanel;
    private OtherPlayersPanel otherPlayersPanel;
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

    // NEW: Flexible selection - store all selected cards
    private List<SelectedCard> selectedCards = new ArrayList<>();

    /**
     * Inner class to track where cards came from
     */
    private static class SelectedCard {
        Card card;
        String source; // "hand", "other_player", "hall"
        String playerName; // For other players
        int position;

        SelectedCard(Card card, String source, String playerName, int position) {
            this.card = card;
            this.source = source;
            this.playerName = playerName;
            this.position = position;
        }
    }

    /**
     * Constructor for GamePanel (compatible with MainWindow)
     * @param mainWindow Main window reference
     * @param controller Game controller
     */
    public GamePanel(MainWindow mainWindow, GameController controller) {
        this.mainWindow = mainWindow;
        this.controller = controller;

        setLayout(new BorderLayout(5, 5));
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        initializeComponents();
        updateDisplay();
    }

    /**
     * Initialize all UI components - RESPONSIVE
     */
    private void initializeComponents() {
        // Top panel (player info)
        createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // Center panel - RESPONSIVE with proportional sizing
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        // Other players' hands (top) - scrollable, min height
        otherPlayersPanel = new OtherPlayersPanel(this);
        JScrollPane otherPlayersScroll = new JScrollPane(otherPlayersPanel);
        otherPlayersScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        otherPlayersScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        otherPlayersScroll.setBorder(null);
        // Set min/max heights for flexibility
        otherPlayersScroll.setMinimumSize(new Dimension(0, 150));
        otherPlayersScroll.setPreferredSize(new Dimension(0, 180));
        otherPlayersScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
        centerPanel.add(otherPlayersScroll);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        // Lecture hall (middle) - FLEXIBLE, takes available space
        lectureHallPanel = new LectureHallPanel(this);
        lectureHallPanel.setMinimumSize(new Dimension(0, 200));
        lectureHallPanel.setPreferredSize(new Dimension(0, 350));
        // Add with flexible sizing
        JPanel hallWrapper = new JPanel(new BorderLayout());
        hallWrapper.setOpaque(false);
        hallWrapper.add(lectureHallPanel, BorderLayout.CENTER);
        centerPanel.add(hallWrapper);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        // Your hand (bottom)
        handPanel = new HandPanel(this);
        handPanel.setMinimumSize(new Dimension(0, 120));
        handPanel.setPreferredSize(new Dimension(0, 135));
        handPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        centerPanel.add(handPanel);

        add(centerPanel, BorderLayout.CENTER);

        // Control panel
        createControlPanel();
        add(controlPanel, BorderLayout.SOUTH);

        // Add component listener to handle resizing
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                adjustLayoutForSize();
            }
        });
    }

    /**
     * Adjust layout based on window size - RESPONSIVE
     */
    private void adjustLayoutForSize() {
        int height = getHeight();

        // Adjust component sizes based on available height
        if (height < 700) {
            // Small screen - compact mode
            otherPlayersPanel.setCompactMode(true);
            handPanel.setPreferredSize(new Dimension(0, 120));
        } else if (height < 900) {
            // Medium screen - normal mode
            otherPlayersPanel.setCompactMode(false);
            handPanel.setPreferredSize(new Dimension(0, 135));
        } else {
            // Large screen - spacious mode
            otherPlayersPanel.setCompactMode(false);
            handPanel.setPreferredSize(new Dimension(0, 145));
        }

        revalidate();
        repaint();
    }

    /**
     * Create top panel with player info - RESPONSIVE
     */
    private void createTopPanel() {
        topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(70, 130, 180));
        topPanel.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        // Left: Player name
        playerLabel = new JLabel("Player: Loading...");
        playerLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        playerLabel.setForeground(Color.WHITE);
        topPanel.add(playerLabel, BorderLayout.WEST);

        // Center: Instructions
        instructionLabel = new JLabel("Select ANY 3 cards to form a trio!");
        instructionLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        instructionLabel.setForeground(Color.WHITE);
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(instructionLabel, BorderLayout.CENTER);

        // Right: Score
        scoreLabel = new JLabel("ECTS: 0 | Trios: 0");
        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        scoreLabel.setForeground(Color.WHITE);
        topPanel.add(scoreLabel, BorderLayout.EAST);
    }

    /**
     * Create control panel with buttons - RESPONSIVE
     */
    private void createControlPanel() {
        controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 8));
        controlPanel.setBackground(new Color(220, 220, 220));

        // Form Trio button
        formTrioButton = new JButton("Form Trio");
        formTrioButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        formTrioButton.setPreferredSize(new Dimension(130, 35));
        formTrioButton.setEnabled(false);
        formTrioButton.addActionListener(e -> formTrio());
        controlPanel.add(formTrioButton);

        // Clear Selection button
        clearSelectionButton = new JButton("Clear Selection");
        clearSelectionButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        clearSelectionButton.setPreferredSize(new Dimension(130, 35));
        clearSelectionButton.addActionListener(e -> clearAllSelections());
        controlPanel.add(clearSelectionButton);

        // Quit button
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

    /**
     * Update entire display
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

        // Update other players' hands
        List<Student> allPlayers = controller.getGame().getStudents();
        otherPlayersPanel.updateDisplay(allPlayers, currentPlayer);

        // Update lecture hall
        lectureHallPanel.updateDisplay(controller.getGame().getLectureHall());

        // Clear selections
        clearAllSelections();
    }

    /**
     * NEW: Flexible card selection - from your hand
     */
    public void onHandCardSelected(Card card, int position) {
        addSelectedCard(card, "hand", null, position);
    }

    /**
     * NEW: Flexible card selection - from other player
     */
    public void onOtherPlayerCardSelected(Card card, int position, String playerName) {
        addSelectedCard(card, "other_player", playerName, position);
    }

    /**
     * NEW: Flexible card selection - from lecture hall
     */
    public void onHallCardSelected(Card card, int position) {
        addSelectedCard(card, "hall", null, position);
    }

    /**
     * Add a card to selection (max 3)
     */
    private void addSelectedCard(Card card, String source, String playerName, int position) {
        // Check if THIS EXACT card at THIS position is already selected
        for (SelectedCard sc : selectedCards) {
            if (sc.card.equals(card) && sc.position == position && sc.source.equals(source)) {
                return; // This exact card at this position is already selected
            }
        }

        // Max 3 cards
        if (selectedCards.size() >= 3) {
            JOptionPane.showMessageDialog(this,
                    "Maximum 3 cards! Clear selection to choose different cards.",
                    "Max Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        selectedCards.add(new SelectedCard(card, source, playerName, position));
        updateSelectionStatus();
    }

    /**
     * Handle card deselected
     */
    public void onCardDeselected(Card card, int position) {
        // Remove the card at this specific position
        selectedCards.removeIf(sc -> sc.card.equals(card) && sc.position == position);
        updateSelectionStatus();
    }

    /**
     * Update selection status display
     */
    private void updateSelectionStatus() {
        int count = selectedCards.size();

        if (count == 0) {
            instructionLabel.setText("Select ANY 3 cards to form a trio!");
        } else if (count < 3) {
            StringBuilder sb = new StringBuilder("Selected " + count + "/3: ");
            for (int i = 0; i < selectedCards.size(); i++) {
                SelectedCard sc = selectedCards.get(i);
                sb.append(sc.card.getCourseCode());
                if (i < selectedCards.size() - 1) {
                    sb.append(", ");
                }
            }
            instructionLabel.setText(sb.toString());
        } else {
            StringBuilder sb = new StringBuilder("Ready! Cards: ");
            for (int i = 0; i < selectedCards.size(); i++) {
                sb.append(selectedCards.get(i).card.getCourseCode());
                if (i < selectedCards.size() - 1) {
                    sb.append(", ");
                }
            }
            instructionLabel.setText(sb.toString());
        }

        formTrioButton.setEnabled(count == 3);
    }

    /**
     * Form trio with selected cards - FLEXIBLE!
     */
    private void formTrio() {
        if (selectedCards.size() != 3) {
            JOptionPane.showMessageDialog(this,
                    "Select exactly 3 cards!",
                    "Incomplete", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Student currentPlayer = controller.getCurrentPlayer();
        List<Card> cards = new ArrayList<>();
        String[] sources = new String[3];
        String[] playerNames = new String[3];

        // Collect cards and sources
        for (int i = 0; i < 3; i++) {
            SelectedCard sc = selectedCards.get(i);
            cards.add(sc.card);
            sources[i] = sc.source;
            playerNames[i] = sc.playerName;
        }

        // Validate and execute
        boolean success = controller.getGame().playFlexibleTurn(
                currentPlayer,
                cards,
                sources,
                playerNames
        );

        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Valid Trio! " + cards.get(0).getCourseCode() + " × 3\n+2 ECTS! Bonus turn!",
                    "Success!", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid Trio! Cards don't match.\nTurn passes.",
                    "Invalid", JOptionPane.ERROR_MESSAGE);
        }

        updateDisplay();
        checkVictory();
    }

    /**
     * Clear all selections
     */
    private void clearAllSelections() {
        selectedCards.clear();
        handPanel.clearSelection();
        otherPlayersPanel.clearSelection();
        lectureHallPanel.clearSelection();
        updateSelectionStatus();
    }

    /**
     * Check for victory condition
     */
    private void checkVictory() {
        Student winner = controller.getGame().checkVictoryConditions();
        if (winner != null) {
            JOptionPane.showMessageDialog(this,
                    "🎉 CONGRATULATIONS! 🎉\n\n" +
                            winner.getName() + " graduated!\n" +
                            "ECTS: " + winner.getEctsCredits() + " | Trios: " + winner.getTrioCount(),
                    "Victory!",
                    JOptionPane.INFORMATION_MESSAGE);

            formTrioButton.setEnabled(false);
            clearSelectionButton.setEnabled(false);
        }
    }
}
