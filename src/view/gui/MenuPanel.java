package view.gui;

import enums.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Menu panel for game setup and main menu.
 * REVEALING GAME: 3-6 players
 *
 * @author Acil HAMIEH, Dana SLEIMAN
 * @version 3.1 - UI fix: Player names panel is now scrollable
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

    // NEW: panel for names (scrollable)
    private JPanel playerNamesPanel;

    public MenuPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;

        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255)); // Alice Blue

        createComponents();
    }

    private void createComponents() {
        add(createTitlePanel(), BorderLayout.NORTH);
        add(createConfigPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createTitlePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(70, 130, 180));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        JLabel titleLabel = new JLabel("TRIO_UTBM");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Revealing Game - Graduate by Remembering!");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        subtitleLabel.setForeground(Color.WHITE);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(subtitleLabel);

        return panel;
    }

    private JPanel createConfigPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));
        panel.setBackground(new Color(240, 248, 255));

        JLabel setupLabel = new JLabel("Game Setup");
        setupLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        setupLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(setupLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        panel.add(createLabeledCombo("Number of Players:",
                new String[]{"3 Players", "4 Players", "5 Players", "6 Players"},
                combo -> playerCountCombo = combo));
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        panel.add(createLabeledCombo("Game Mode:",
                new String[]{"Individual", "Teams (Teams of 2)"},
                combo -> gameModeCombo = combo));
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        panel.add(createLabeledCombo("Difficulty:",
                new String[]{"Simple (3 matching = 2 ECTS)", "Advanced (3 matching same branch = 3 ECTS)"},
                combo -> difficultyCombo = combo));
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        JLabel namesLabel = new JLabel("Player Names");
        namesLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        namesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(namesLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // ===== PLAYER NAMES PANEL (SCROLLABLE) =====
        playerNamesPanel = new JPanel();
        playerNamesPanel.setLayout(new BoxLayout(playerNamesPanel, BoxLayout.Y_AXIS));
        playerNamesPanel.setBackground(new Color(240, 248, 255));

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
            playerNamesPanel.add(namePanel);

            if (i >= 3) {
                namePanel.setVisible(false);
            }
        }

        JScrollPane scrollPane = new JScrollPane(playerNamesPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(600, 200));
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);

        panel.add(scrollPane);

        playerCountCombo.addActionListener(e -> {
            int count = playerCountCombo.getSelectedIndex() + 3;
            for (int i = 0; i < 6; i++) {
                Component parent = playerNameFields[i].getParent();
                parent.setVisible(i < count);
            }
            playerNamesPanel.revalidate();
            playerNamesPanel.repaint();
        });

        return panel;
    }

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

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panel.setBackground(new Color(240, 248, 255));

        startButton = new JButton("Start Game");
        startButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        startButton.setPreferredSize(new Dimension(150, 40));
        startButton.setBackground(new Color(34, 139, 34));
        startButton.setForeground(Color.WHITE);
        startButton.addActionListener(e -> startGame());

        rulesButton = new JButton("Rules");
        rulesButton.setPreferredSize(new Dimension(120, 40));
        rulesButton.addActionListener(e -> mainWindow.showRules());

        aboutButton = new JButton("About");
        aboutButton.setPreferredSize(new Dimension(120, 40));
        aboutButton.addActionListener(e -> mainWindow.showAbout());

        exitButton = new JButton("Exit");
        exitButton.setPreferredSize(new Dimension(120, 40));
        exitButton.setBackground(new Color(220, 20, 60));
        exitButton.setForeground(Color.WHITE);
        exitButton.addActionListener(e -> System.exit(0));

        panel.add(startButton);
        panel.add(rulesButton);
        panel.add(aboutButton);
        panel.add(exitButton);

        return panel;
    }

    // ===== GAME START LOGIC (UNCHANGED) =====

    private void startGame() {
        int numPlayers = playerCountCombo.getSelectedIndex() + 3;

        GameMode mode = getSelectedGameMode();
        Difficulty difficulty = getSelectedDifficulty();

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
            playerNames.add(name);
        }

        if (mode.isTeamMode() && numPlayers % 2 != 0) {
            JOptionPane.showMessageDialog(this,
                    "Team mode requires an even number of players!",
                    "Invalid Configuration",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (mode.isTeamMode() && !confirmTeamPartners(playerNames)) {
            return;
        }

        mainWindow.startNewGame(numPlayers, mode, difficulty, playerNames);
    }

    private boolean confirmTeamPartners(List<String> playerNames) {
        StringBuilder teamInfo = new StringBuilder("Team Formation:\n\n");
        for (int i = 0, t = 1; i < playerNames.size(); i += 2, t++) {
            teamInfo.append("Team ").append(t).append(": ")
                    .append(playerNames.get(i)).append(" & ")
                    .append(playerNames.get(i + 1)).append("\n");
        }
        teamInfo.append("\nContinue with these teams?");
        return JOptionPane.showConfirmDialog(this, teamInfo.toString(),
                "Confirm Teams", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    private GameMode getSelectedGameMode() {
        int m = gameModeCombo.getSelectedIndex();
        int d = difficultyCombo.getSelectedIndex();
        return (m == 0)
                ? (d == 0 ? GameMode.INDIVIDUAL_SIMPLE : GameMode.INDIVIDUAL_ADVANCED)
                : (d == 0 ? GameMode.TEAM_SIMPLE : GameMode.TEAM_ADVANCED);
    }

    private Difficulty getSelectedDifficulty() {
        return difficultyCombo.getSelectedIndex() == 0
                ? Difficulty.SIMPLE
                : Difficulty.ADVANCED;
    }
}
