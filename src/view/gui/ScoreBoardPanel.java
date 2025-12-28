package view.gui;

import controller.GameController;
import model.*;
import javax.swing.*;
import java.awt.*;

/**
 * Panel displaying current scores and standings.
 * Shows ECTS credits and trio counts for all players.
 *
 * @author Acil HAMIEH
 * @version 1.0
 */
public class ScoreBoardPanel extends JPanel {
    private GameController gameController;
    private JTextArea scoreText;

    /**
     * Constructor for ScoreBoardPanel
     * @param gameController Game controller
     */
    public ScoreBoardPanel(GameController gameController) {
        this.gameController = gameController;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2),
                "Scoreboard",
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 14)
        ));

        createComponents();
        updateDisplay();
    }

    /**
     * Create components
     */
    private void createComponents() {
        scoreText = new JTextArea();
        scoreText.setEditable(false);
        scoreText.setFont(new Font("Monospaced", Font.PLAIN, 12));
        scoreText.setBackground(Color.WHITE);
        scoreText.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(scoreText);
        scrollPane.setPreferredSize(new Dimension(250, 300));

        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Update score display
     */
    public void updateDisplay() {
        StringBuilder scores = new StringBuilder();
        Game game = gameController.getGame();
        Student currentPlayer = gameController.getCurrentPlayer();

        if (game.getGameMode().isTeamMode()) {
            // Team scores
            scores.append("═════════════════════\n");
            scores.append("     TEAM SCORES\n");
            scores.append("═════════════════════\n\n");

            for (Team team : game.getTeams()) {
                scores.append(team.getTeamName()).append("\n");
                scores.append("  ECTS: ").append(team.getTeamScore()).append("/6\n");
                scores.append("  Trios: ").append(team.getTrioCount()).append("\n");
                scores.append("  Members:\n");

                for (Student member : team.getMembers()) {
                    String marker = member.equals(currentPlayer) ? "→ " : "  ";
                    scores.append(marker).append("• ").append(member.getName()).append("\n");
                    scores.append("    ").append(member.getEctsCredits()).append(" ECTS, ")
                            .append(member.getTrioCount()).append(" trios\n");
                }
                scores.append("\n");
            }
        } else {
            // Individual scores
            scores.append("═════════════════════\n");
            scores.append("   INDIVIDUAL SCORES\n");
            scores.append("═════════════════════\n\n");

            for (Student student : game.getStudents()) {
                String marker = student.equals(currentPlayer) ? "→ " : "  ";
                scores.append(marker).append(student.getName()).append("\n");
                scores.append("  ECTS: ").append(student.getEctsCredits()).append("/6\n");
                scores.append("  Trios: ").append(student.getTrioCount()).append("\n\n");
            }
        }

        // Deck info
        scores.append("═════════════════════\n");
        scores.append("Deck: ").append(game.getDeck().getRemainingCount()).append(" cards left\n");

        scoreText.setText(scores.toString());
    }
}
