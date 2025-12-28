package view.gui;

import enums.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Menu panel for game setup and main menu.
 * Handles game configuration before starting.
 *
 * @author Acil HAMIEH
 * @version 1.0
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
        JLabel subtitleLabel = new JLabel("Graduate by Forming Course Trios!");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
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

        // Player count
        panel.add(createLabeledCombo("Number of Players:",
                new String[]{"3 Players", "4 Players"},
                combo -> playerCountCombo = combo));
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Game mode
        panel.add(createLabeledCombo("Game Mode:",
                new String[]{
                        "Individual - Simple Mode (3 matching = 2 ECTS)",
                        "Individual - Advanced Mode (3 matching same branch = 3 ECTS)",
                        "Team - Simple Mode (Teams of 2, 3 matching = 2 ECTS)",
                        "Team - Advanced Mode (Teams of 2, 3 matching same branch = 3 ECTS)"
                },
                combo -> gameModeCombo = combo));
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Difficulty
        panel.add(createLabeledCombo("Difficulty:",
                new String[]{"Easy", "Normal", "Hard"},
                combo -> difficultyCombo = combo));
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Player names section
        JLabel namesLabel = new JLabel("Player Names");
        namesLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        namesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(namesLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Player name fields (create 4, show/hide based on player count)
        playerNameFields = new JTextField[4];
        for (int i = 0; i < 4; i++) {
            JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            namePanel.setBackground(new Color(240, 248, 255));

            JLabel label = new JLabel("Player " + (i + 1) + ":");
            label.setPreferredSize(new Dimension(80, 25));

            playerNameFields[i] = new JTextField(20);
            playerNameFields[i].setFont(new Font("SansSerif", Font.PLAIN, 14));

            namePanel.add(label);
            namePanel.add(playerNameFields[i]);
            panel.add(namePanel);

            // Hide 4th player initially
            if (i == 3) {
                namePanel.setVisible(false);
            }
        }

        // Add listener to show/hide 4th player field
        playerCountCombo.addActionListener(e -> {
            int count = playerCountCombo.getSelectedIndex() + 3; // 3 or 4
            Component parent = playerNameFields[3].getParent();
            parent.setVisible(count == 4);
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
        // Get configuration
        int numPlayers = playerCountCombo.getSelectedIndex() + 3; // 3 or 4

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

        // Validate team mode
        if (mode.isTeamMode() && numPlayers % 2 != 0) {
            JOptionPane.showMessageDialog(this,
                    "Team mode requires an even number of players!",
                    "Invalid Configuration",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Start game
        mainWindow.startNewGame(numPlayers, mode, difficulty, playerNames);
    }

    /**
     * Get selected game mode
     * @return Selected GameMode enum
     */
    private GameMode getSelectedGameMode() {
        int index = gameModeCombo.getSelectedIndex();
        switch (index) {
            case 0: return GameMode.INDIVIDUAL_SIMPLE;
            case 1: return GameMode.INDIVIDUAL_ADVANCED;
            case 2: return GameMode.TEAM_SIMPLE;
            case 3: return GameMode.TEAM_ADVANCED;
            default: return GameMode.INDIVIDUAL_SIMPLE;
        }
    }

    /**
     * Get selected difficulty
     * @return Selected Difficulty enum
     */
    private Difficulty getSelectedDifficulty() {
        int index = difficultyCombo.getSelectedIndex();
        switch (index) {
            case 0: return Difficulty.EASY;
            case 1: return Difficulty.NORMAL;
            case 2: return Difficulty.HARD;
            default: return Difficulty.NORMAL;
        }
    }
}
