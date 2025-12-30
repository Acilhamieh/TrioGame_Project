package view.gui;

import enums.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Menu panel for game setup and main menu.
 * MEMORY GAME: 3-6 players (need neighbors for memory mechanic)
 *
 * @author Acil HAMIEH, Dana SLEIMAN
 * @version 2.0 - Memory game (3-6 players)
 */
public class MenuPanel extends JPanel {
    private MainWindow mainWindow;

    // UI Components
    private JComboBox<String> playerCountCombo;
    private JComboBox<String> gameModeCombo;
    private JComboBox<String> difficultyCombo;
    private JTextField[] playerNameFields;
    private JButton startButton;
    private JButton rulesButton;
    private JButton aboutButton;
    private JButton exitButton;

    /**
     * Constructor for MenuPanel
     * @param mainWindow Reference to main window
     */
    public MenuPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;

        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255)); // Alice Blue

        createComponents();
    }

    /**
     * Create all UI components
     */
    private void createComponents() {
        // Title panel
        JPanel titlePanel = createTitlePanel();
        add(titlePanel, BorderLayout.NORTH);

        // Configuration panel
        JPanel configPanel = createConfigPanel();
        add(configPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Create title panel with game logo
     * @return Title panel
     */
    private JPanel createTitlePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(70, 130, 180)); // Steel Blue
        panel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        // Title
        JLabel titleLabel = new JLabel("TRIO_UTBM");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Memory Game Edition - Graduate by Remembering!");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        subtitleLabel.setForeground(Color.WHITE);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(subtitleLabel);

        return panel;
    }

    /**
     * Create configuration panel for game setup
     * @return Configuration panel
     */
    private JPanel createConfigPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));
        panel.setBackground(new Color(240, 248, 255));

        // Section title
        JLabel setupLabel = new JLabel("Game Setup");
        setupLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        setupLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(setupLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Player count (3-6 players for memory game!)
        panel.add(createLabeledCombo("Number of Players:",
                new String[]{"3 Players", "4 Players", "5 Players", "6 Players"},
                combo -> playerCountCombo = combo));
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Game mode (Individual or Teams)
        panel.add(createLabeledCombo("Game Mode:",
                new String[]{
                        "Individual",
                        "Teams (Teams of 2)"
                },
                combo -> gameModeCombo = combo));
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Difficulty (Simple or Advanced)
        panel.add(createLabeledCombo("Difficulty:",
                new String[]{"Simple (3 matching = 2 ECTS)", "Advanced (3 matching same branch = 3 ECTS)"},
                combo -> difficultyCombo = combo));
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Player names section
        JLabel namesLabel = new JLabel("Player Names");
        namesLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        namesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(namesLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Player name fields (create 6, show/hide based on player count)
        playerNameFields = new JTextField[6];
        for (int i = 0; i < 6; i++) {
            JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            namePanel.setBackground(new Color(240, 248, 255));

            JLabel label = new JLabel("Player " + (i + 1) + ":");
            label.setPreferredSize(new Dimension(80, 25));

            playerNameFields[i] = new JTextField(20);
            playerNameFields[i].setFont(new Font("SansSerif", Font.PLAIN, 14));

            namePanel.add(label);
            namePanel.add(playerNameFields[i]);
            panel.add(namePanel);

            // Hide players 4-6 initially (start with 3)
            if (i >= 3) {
                namePanel.setVisible(false);
            }
        }

        // Add listener to show/hide player fields based on count
        playerCountCombo.addActionListener(e -> {
            int count = playerCountCombo.getSelectedIndex() + 3; // 3-6 (index 0=3, 1=4, 2=5, 3=6)
            for (int i = 0; i < 6; i++) {
                Component parent = playerNameFields[i].getParent();
                parent.setVisible(i < count);
            }
        });

        return panel;
    }

    /**
     * Create labeled combo box
     * @param labelText Label text
     * @param items Combo box items
     * @param comboSetter Consumer to set combo reference
     * @return Panel with label and combo
     */
    private JPanel createLabeledCombo(String labelText, String[] items,
                                      java.util.function.Consumer<JComboBox<String>> comboSetter) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(new Color(240, 248, 255));

        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(150, 25));
        label.setFont(new Font("SansSerif", Font.BOLD, 14));

        JComboBox<String> combo = new JComboBox<>(items);
        combo.setPreferredSize(new Dimension(400, 30));
        combo.setFont(new Font("SansSerif", Font.PLAIN, 14));

        comboSetter.accept(combo);

        panel.add(label);
        panel.add(combo);

        return panel;
    }

    /**
     * Create button panel
     * @return Button panel
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panel.setBackground(new Color(240, 248, 255));

        // Start button
        startButton = new JButton("Start Game");
        startButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        startButton.setPreferredSize(new Dimension(150, 40));
        startButton.setBackground(new Color(34, 139, 34)); // Forest Green
        startButton.setForeground(Color.WHITE);
        startButton.addActionListener(e -> startGame());

        // Rules button
        rulesButton = new JButton("Rules");
        rulesButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        rulesButton.setPreferredSize(new Dimension(120, 40));
        rulesButton.addActionListener(e -> mainWindow.showRules());

        // About button
        aboutButton = new JButton("About");
        aboutButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        aboutButton.setPreferredSize(new Dimension(120, 40));
        aboutButton.addActionListener(e -> mainWindow.showAbout());

        // Exit button
        exitButton = new JButton("Exit");
        exitButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        exitButton.setPreferredSize(new Dimension(120, 40));
        exitButton.setBackground(new Color(220, 20, 60)); // Crimson
        exitButton.setForeground(Color.WHITE);
        exitButton.addActionListener(e -> System.exit(0));

        panel.add(startButton);
        panel.add(rulesButton);
        panel.add(aboutButton);
        panel.add(exitButton);

        return panel;
    }

    /**
     * Validate and start game
     */
    private void startGame() {
        // Get configuration - UPDATED for 3-6 players
        int numPlayers = playerCountCombo.getSelectedIndex() + 3; // 3-6 (index 0=3, 1=4, 2=5, 3=6)

        GameMode mode = getSelectedGameMode();
        Difficulty difficulty = getSelectedDifficulty();

        // Validate player names
        List<String> playerNames = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            String name = playerNameFields[i].getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Player " + (i + 1) + " name cannot be empty!",
                        "Invalid Name",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (name.length() > 20) {
                JOptionPane.showMessageDialog(this,
                        "Player " + (i + 1) + " name is too long (max 20 characters)!",
                        "Invalid Name",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            playerNames.add(name);
        }

        // Validate team mode - must have even number of players
        if (mode.isTeamMode() && numPlayers % 2 != 0) {
            JOptionPane.showMessageDialog(this,
                    "Team mode requires an even number of players!\n" +
                            "Current: " + numPlayers + " players\n" +
                            "Please select 4 or 6 players for team mode.",
                    "Invalid Configuration",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // For team mode, let players choose partners
        if (mode.isTeamMode()) {
            boolean partnersConfirmed = confirmTeamPartners(playerNames);
            if (!partnersConfirmed) {
                return; // User cancelled
            }
        }

        // Start game
        mainWindow.startNewGame(numPlayers, mode, difficulty, playerNames);
    }

    /**
     * Let players confirm team partnerships
     * @param playerNames List of player names
     * @return true if confirmed, false if cancelled
     */
    private boolean confirmTeamPartners(List<String> playerNames) {
        StringBuilder teamInfo = new StringBuilder();
        teamInfo.append("Team Formation:\n\n");

        int teamNumber = 1;
        for (int i = 0; i < playerNames.size(); i += 2) {
            teamInfo.append("Team ").append(teamNumber).append(": ");
            teamInfo.append(playerNames.get(i));
            teamInfo.append(" & ");
            teamInfo.append(playerNames.get(i + 1));
            teamInfo.append("\n");
            teamNumber++;
        }

        teamInfo.append("\nPlayers are paired in order entered.\n");
        teamInfo.append("Continue with these teams?");

        int choice = JOptionPane.showConfirmDialog(this,
                teamInfo.toString(),
                "Confirm Teams",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        return choice == JOptionPane.YES_OPTION;
    }

    /**
     * Get selected game mode
     * @return Selected GameMode enum
     */
    private GameMode getSelectedGameMode() {
        int modeIndex = gameModeCombo.getSelectedIndex();
        int difficultyIndex = difficultyCombo.getSelectedIndex();

        // Combine mode and difficulty
        // Mode: 0 = Individual, 1 = Teams
        // Difficulty: 0 = Simple, 1 = Advanced

        if (modeIndex == 0) { // Individual
            if (difficultyIndex == 0) {
                return GameMode.INDIVIDUAL_SIMPLE;
            } else {
                return GameMode.INDIVIDUAL_ADVANCED;
            }
        } else { // Teams
            if (difficultyIndex == 0) {
                return GameMode.TEAM_SIMPLE;
            } else {
                return GameMode.TEAM_ADVANCED;
            }
        }
    }

    /**
     * Get selected difficulty
     * @return Selected Difficulty enum
     */
    private Difficulty getSelectedDifficulty() {
        // For compatibility with existing system, return NORMAL
        // The actual difficulty (Simple/Advanced) is handled by GameMode
        return Difficulty.NORMAL;
    }
}
