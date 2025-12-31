package view.gui;

import controller.*;
import enums.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Main window for the Trio_UTBM GUI - REVEALING GAME VERSION.
 * Contains all GUI panels and manages the overall interface.
 *
 * @author Acil HAMIEH, Dana SLEIMAN
 * @version 3.0 - Fixed: Creates new GameController for each game
 */
public class MainWindow extends JFrame {
    private GameController gameController;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Panels
    private MenuPanel menuPanel;
    private GamePanel gamePanel;

    // Constants
    private static final String TITLE = "Trio_UTBM - Revealing Game Edition!";

    /**
     * Constructor for MainWindow
     */
    public MainWindow() {
        this.gameController = new GameController();

        initializeWindow();
        createPanels();
        setupLayout();

        setVisible(true);
    }

    /**
     * Initialize window properties - RESPONSIVE
     */
    private void initializeWindow() {
        setTitle(TITLE);

        // Get screen size for responsive sizing
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = Math.min(1400, (int)(screenSize.width * 0.9));
        int height = Math.min(900, (int)(screenSize.height * 0.9));

        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
        setResizable(true);
        setMinimumSize(new Dimension(1000, 650)); // Minimum size for playability
    }

    /**
     * Create all panels
     */
    private void createPanels() {
        // Create menu panel
        menuPanel = new MenuPanel(this);

        // Game panel will be created when game starts
        gamePanel = null;
    }

    /**
     * Setup layout with CardLayout for switching between panels
     */
    private void setupLayout() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Add menu panel
        mainPanel.add(menuPanel, "MENU");

        add(mainPanel);

        // Show menu initially
        showMenu();
    }

    /**
     * Show the menu panel
     */
    public void showMenu() {
        cardLayout.show(mainPanel, "MENU");
    }

    /**
     * Start a new game with configuration
     * ✅ FIXED: Creates NEW GameController for each game
     * @param numPlayers Number of players
     * @param mode Game mode
     * @param difficulty Difficulty level
     * @param playerNames List of player names
     */
    public void startNewGame(int numPlayers, GameMode mode, Difficulty difficulty, List<String> playerNames) {
        // ✅ CREATE NEW GAMECONTROLLER FOR EACH GAME
        // This ensures we start with a fresh Game object (0 cards, no old state)
        this.gameController = new GameController();

        // Initialize game
        boolean success = gameController.initializeGame(numPlayers, mode, difficulty, playerNames);

        if (success) {
            gameController.startGame();

            // Remove old game panel if it exists
            if (gamePanel != null) {
                mainPanel.remove(gamePanel);
            }

            // Create new game panel
            gamePanel = new GamePanel(this, gameController);

            // Add game panel to card layout
            mainPanel.add(gamePanel, "GAME");

            // Switch to game view
            cardLayout.show(mainPanel, "GAME");

            // Update display
            gamePanel.updateDisplay();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to initialize game!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * End current game and return to menu
     */
    public void endGame() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to quit this game?",
                "Quit Game",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            gameController.endGame();

            // Remove game panel
            if (gamePanel != null) {
                mainPanel.remove(gamePanel);
                gamePanel = null;
            }

            // Return to menu
            showMenu();
        }
    }

    /**
     * Get the game controller
     * @return Game controller instance
     */
    public GameController getGameController() {
        return gameController;
    }

    /**
     * Show rules dialog - UPDATED FOR REVEALING GAME
     */
    public void showRules() {
        String rules = "🎓 TRIO_UTBM - REVEALING GAME RULES\n\n" +
                "🎯 OBJECTIVE:\n" +
                "Be the first to win by forming trios!\n\n" +
                "🎴 WHAT IS A TRIO?\n" +
                "Three cards with the SAME course code\n\n" +
                "🔍 REVEALING MECHANICS:\n" +
                "• Your hand: All cards face-UP (you see them)\n" +
                "• Other players: All cards face-DOWN [?]\n" +
                "• Lecture hall: All cards face-DOWN [?]\n" +
                "• Click cards ONE BY ONE to reveal them!\n\n" +
                "👆 CLICKING RULES:\n" +
                "• YOUR hand: Can click first/last cards + duplicates\n" +
                "• OTHER players: Can only click first/last cards\n" +
                "• LECTURE HALL: Can click any card\n\n" +
                "🎮 HOW TO PLAY:\n" +
                "1. Click first card → Reveals face-up\n" +
                "2. Click second card → If matches: continue!\n" +
                "3. If mismatch → Cards flip back after 2 seconds, turn ends\n" +
                "4. Click third card → If all match: TRIO!\n" +
                "5. Trio complete → Cards removed, +2 ECTS, bonus turn!\n\n" +
                "⚙️  DIFFICULTY:\n" +
                "• Simple: Any 3 matching cards = 2 ECTS\n" +
                "• Advanced: 3 matching from same branch = 3 ECTS\n\n" +
                "🏆 VICTORY:\n" +
                "• Simple: Win 3 trios OR 1 trio of PFE (ID:7)\n" +
                "• Advanced: Win 2 linked trios OR 1 trio of PFE\n\n" +
                "🎴 CARD IDs:\n" +
                "SY41=12, IA41=11, SY48=10, AP4B=9\n" +
                "GI21=8, PFE=7, GI41=6\n" +
                "MQ18=5, MQ41=4, MQ51=3\n" +
                "EN21=2, GI28=1\n\n" +
                "🧠 STRATEGY:\n" +
                "• Remember revealed cards\n" +
                "• Watch which cards others reveal\n" +
                "• Plan your trio before revealing!\n\n" +
                "Good luck! 🎓";

        JTextArea textArea = new JTextArea(rules);
        textArea.setEditable(false);
        textArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 600));

        JOptionPane.showMessageDialog(this,
                scrollPane,
                "Revealing Game Rules",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Show about dialog
     */
    public void showAbout() {
        String about = "🎓 TRIO_UTBM - REVEALING GAME EDITION\n\n" +
                "A revealing card game about graduating from UTBM!\n\n" +
                "🎮 FEATURES:\n" +
                "• Reveal cards one by one\n" +
                "• Face-down mystery cards\n" +
                "• First/last + duplicates clicking\n" +
                "• Flip animations\n" +
                "• Mismatch detection\n\n" +
                "👨‍💻 Developed by:\n" +
                "• Dana SLEIMAN\n" +
                "• Acil HAMIEH\n\n" +
                "📚 Course: AP4B\n" +
                "🏫 UTBM - Université de Technologie\n" +
                "    de Belfort-Montbéliard\n\n" +
                "Version 3.0 - Revealing Game - 2024/2025";

        JOptionPane.showMessageDialog(this,
                about,
                "About Trio_UTBM",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Main entry point for GUI version
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Use default look and feel
        }

        // Create and show main window
        SwingUtilities.invokeLater(() -> new MainWindow());
    }
}
