package view.console;

import controller.GameController;
import model.*;
import enums.*;


public class GameView {
    private CardDisplayer cardDisplayer;

    /**
     * Constructor for GameView
     */
    public GameView() {
        this.cardDisplayer = new CardDisplayer();
    }

    /**
     * Display the complete game state
     * @param controller The game controller
     */
    public void displayGameState(GameController controller) {
        Game game = controller.getGame();
        Student currentPlayer = controller.getCurrentPlayer();

        // Header
        displayHeader(game);

        // Current player info
        displayCurrentPlayerInfo(currentPlayer, game.getGameMode());

        // Player's hand
        System.out.println("\n🤚 YOUR HAND:");
        cardDisplayer.displayHand(currentPlayer.getHand());

        // Lecture Hall
        System.out.println("\n🏛️  LECTURE HALL:");
        cardDisplayer.displayLectureHall(game.getLectureHall());

        // Quick scores
        displayQuickScores(game);
    }

    /**
     * Display game header with round info
     * @param game The game instance
     */
    private void displayHeader(Game game) {
        System.out.println("\n╔════════════════════════════════════════════════════╗");
        System.out.println("║                  TRIO_UTBM                         ║");
        System.out.println("╚════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("  Mode: " + game.getGameMode().getDisplayName());
        System.out.println("  Round: " + game.getTurnManager().getRoundNumber());
        System.out.println("  Deck: " + game.getDeck().getRemainingCount() + " cards remaining");
        System.out.println();
    }

    /**
     * Display current player information
     * @param player Current player
     * @param mode Game mode
     */
    private void displayCurrentPlayerInfo(Student player, GameMode mode) {
        System.out.println("═".repeat(50));
        System.out.println("  👤 CURRENT PLAYER: " + player.getName());
        System.out.println("  📊 ECTS Credits: " + player.getEctsCredits() + "/6");
        System.out.println("  🎯 Trios Completed: " + player.getTrioCount());

        if (mode.isTeamMode() && player.getTeam() != null) {
            Team team = player.getTeam();
            System.out.println("  👥 Team: " + team.getTeamName() +
                    " (" + team.getTeamScore() + " ECTS)");
        }

        System.out.println("═".repeat(50));
    }

    /**
     * Display quick score summary
     * @param game The game instance
     */
    private void displayQuickScores(Game game) {
        System.out.println("\n📊 CURRENT STANDINGS:");

        if (game.getGameMode().isTeamMode()) {
            // Team scores
            for (Team team : game.getTeams()) {
                System.out.println("  " + team.getTeamName() + ": " +
                        team.getTeamScore() + " ECTS");
            }
        } else {
            // Individual scores
            for (Student student : game.getStudents()) {
                String marker = student.equals(game.getTurnManager().getCurrentStudent())
                        ? "→ " : "  ";
                System.out.println(marker + student.getName() + ": " +
                        student.getEctsCredits() + " ECTS");
            }
        }
    }

    /**
     * Display detailed scores
     * @param controller The game controller
     */
    public void displayDetailedScores(GameController controller) {
        Game game = controller.getGame();

        System.out.println("\n╔════════════════════════════════════════════════════╗");
        System.out.println("║              📊 DETAILED SCORES                    ║");
        System.out.println("╚════════════════════════════════════════════════════╝\n");

        if (game.getGameMode().isTeamMode()) {
            displayTeamScores(game);
        } else {
            displayIndividualScores(game);
        }
    }

    /**
     * Display individual player scores
     * @param game The game instance
     */
    private void displayIndividualScores(Game game) {
        System.out.println("Individual Standings:\n");

        int rank = 1;
        for (Student student : game.getStudents()) {
            System.out.println("  " + rank + ". " + student.getName());
            System.out.println("     ECTS: " + student.getEctsCredits() + "/6");
            System.out.println("     Trios: " + student.getTrioCount());
            System.out.println("     Hand Size: " + student.getHand().getSize());

            // Show completed trios
            if (student.getTrioCount() > 0) {
                System.out.print("     Completed: ");
                for (Trio trio : student.getCompletedTrios()) {
                    System.out.print(trio.getCard1().getCourseCode() + " ");
                }
                System.out.println();
            }

            System.out.println();
            rank++;
        }
    }

    /**
     * Display team scores
     * @param game The game instance
     */
    private void displayTeamScores(Game game) {
        System.out.println("Team Standings:\n");

        int rank = 1;
        for (Team team : game.getTeams()) {
            System.out.println("  " + rank + ". " + team.getTeamName());
            System.out.println("     Team ECTS: " + team.getTeamScore() + "/6");
            System.out.println("     Trios: " + team.getTrioCount());
            System.out.println("     Members:");

            for (Student member : team.getMembers()) {
                System.out.println("       - " + member.getName() + ": " +
                        member.getEctsCredits() + " ECTS (" +
                        member.getTrioCount() + " trios)");
            }

            System.out.println();
            rank++;
        }
    }

    /**
     * Display victory message
     * @param winner The winning student
     * @param game The game instance
     */
    public void displayVictory(Student winner, Game game) {
        System.out.println("\n\n");
        System.out.println("╔════════════════════════════════════════════════════╗");
        System.out.println("║                                                    ║");
        System.out.println("║            🎓 GRADUATION COMPLETE! 🎓             ║");
        System.out.println("║                                                    ║");
        System.out.println("╚════════════════════════════════════════════════════╝\n");

        if (game.getGameMode().isTeamMode() && winner.getTeam() != null) {
            Team team = winner.getTeam();
            System.out.println("  🏆 WINNING TEAM: " + team.getTeamName());
            System.out.println("  📊 Final Score: " + team.getTeamScore() + " ECTS");
            System.out.println("\n  Team Members:");
            for (Student member : team.getMembers()) {
                System.out.println("    • " + member.getName() + " - " +
                        member.getEctsCredits() + " ECTS");
            }
        } else {
            System.out.println("  🏆 WINNER: " + winner.getName());
            System.out.println("  📊 Final Score: " + winner.getEctsCredits() + " ECTS");
            System.out.println("  🎯 Trios Completed: " + winner.getTrioCount());
        }

        System.out.println("\n  🎉 Congratulations! 🎉\n");
    }

    /**
     * Display game over screen (no winner - deck empty)
     * @param game The game instance
     */
    public void displayGameOver(Game game) {
        System.out.println("\n\n");
        System.out.println("╔════════════════════════════════════════════════════╗");
        System.out.println("║                  GAME OVER                         ║");
        System.out.println("╚════════════════════════════════════════════════════╝\n");

        System.out.println("  The deck is empty!\n");

        displayDetailedScores(null);

        System.out.println("  Thanks for playing!\n");
    }
}
