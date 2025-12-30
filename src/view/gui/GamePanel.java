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
 * RESPONSIVE: Adapts to different screen sizes
 *
 * @author Acil HAMIEH, Dana SLEIMAN
 * @version 3.2 - Responsive design
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

    // Selection tracking
    private Card selectedFromHand = null;
    private int selectedHandPosition = -1;
    private Card selectedFromOtherPlayer = null;
    private int selectedOtherPlayerPosition = -1;
    private String selectedOtherPlayerName = null;
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
        instructionLabel = new JLabel("Select: 1 from hand + 1 from other + 1 from hall");
        instructionLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
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

    public void onHandCardSelected(Card card, int position) {
        if (selectedFromHand != null && !selectedFromHand.equals(card)) {
            clearHandSelection();
        }
        selectedFromHand = card;
        selectedHandPosition = position;
        updateSelectionStatus();
    }

    public void onOtherPlayerCardSelected(Card card, int position, String playerName) {
        if (selectedFromOtherPlayer != null && !selectedFromOtherPlayer.equals(card)) {
            clearOtherPlayersSelection();
        }
        selectedFromOtherPlayer = card;
        selectedOtherPlayerPosition = position;
        selectedOtherPlayerName = playerName;
        updateSelectionStatus();
    }

    public void onHallCardSelected(Card card, int position) {
        if (selectedFromHall != null && !selectedFromHall.equals(card)) {
            clearHallSelection();
        }
        selectedFromHall = card;
        selectedHallPosition = position;
        updateSelectionStatus();
    }

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

    private void updateSelectionStatus() {
        int selectedCount = 0;
        if (selectedFromHand != null) selectedCount++;
        if (selectedFromOtherPlayer != null) selectedCount++;
        if (selectedFromHall != null) selectedCount++;

        StringBuilder instruction = new StringBuilder();
        if (selectedFromHand != null) {
            instruction.append("✅ Hand(").append(selectedFromHand.getCourseCode()).append(") ");
        } else {
            instruction.append("❌ Hand ");
        }
        if (selectedFromOtherPlayer != null) {
            instruction.append("✅ ").append(selectedOtherPlayerName).append("(").append(selectedFromOtherPlayer.getCourseCode()).append(") ");
        } else {
            instruction.append("❌ Other ");
        }
        if (selectedFromHall != null) {
            instruction.append("✅ Hall(").append(selectedFromHall.getCourseCode()).append(")");
        } else {
            instruction.append("❌ Hall");
        }

        instructionLabel.setText(instruction.toString());
        formTrioButton.setEnabled(selectedCount == 3);
    }

    private void formTrio() {
        if (selectedFromHand == null || selectedFromOtherPlayer == null || selectedFromHall == null) {
            JOptionPane.showMessageDialog(this,
                    "Select: 1 from hand + 1 from other + 1 from hall",
                    "Incomplete", JOptionPane.WARNING_MESSAGE);
            return;
        }

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
                    "Error: Player not found",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Card> selectedCards = Arrays.asList(
                selectedFromHand,
                selectedFromOtherPlayer,
                selectedFromHall
        );

        boolean success = controller.getGame().playTurnWithPlayer(
                currentPlayer,
                selectedPlayer,
                selectedCards
        );

        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Valid Trio! " + selectedFromHand.getCourseCode() + " × 3\n+2 ECTS! Bonus turn!",
                    "Success!", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid Trio! Turn passes.",
                    "Invalid", JOptionPane.ERROR_MESSAGE);
        }

        updateDisplay();
        checkVictory();
    }

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

    private void clearHandSelection() {
        handPanel.clearSelection();
        selectedFromHand = null;
        selectedHandPosition = -1;
    }

    private void clearOtherPlayersSelection() {
        otherPlayersPanel.clearSelection();
        selectedFromOtherPlayer = null;
        selectedOtherPlayerPosition = -1;
        selectedOtherPlayerName = null;
    }

    private void clearHallSelection() {
        lectureHallPanel.clearSelection();
        selectedFromHall = null;
        selectedHallPosition = -1;
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

            formTrioButton.setEnabled(false);
            clearSelectionButton.setEnabled(false);
        }
    }
}
