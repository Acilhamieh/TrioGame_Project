import model.*;
import enums.*;
import java.util.*;

public class BackendTest3 {

    public static void main(String[] args) {

        // --- Setup game ---
        Game game = new Game();

        List<String> names = Arrays.asList("Acil", "Dana", "Israa");

        game.configure(3, GameMode.INDIVIDUAL_SIMPLE, Difficulty.SIMPLE, names);
        game.initialize();
        game.startGame();

        Student current = game.getTurnManager().getCurrentStudent();

        System.out.println("\nCurrent player: " + current.getName());

        // --- Take cards from CURRENT PLAYER hand ---
        Card c1 = current.getHand().getCard(0);
        Card c2 = current.getHand().getCard(1);

        // --- Take card from LECTURE HALL ---
        Card hallCard = game.getLectureHall().getCard(0);

        System.out.println("\nReveal 1st card (hand)");
        System.out.println(game.revealCard(current, c1, "hand", null, 0));

        System.out.println("\nReveal 2nd card (hand)");
        System.out.println(game.revealCard(current, c2, "hand", null, 1));

        System.out.println("\nReveal 3rd card (hall)");
        System.out.println(game.revealCard(current, hallCard, "hall", null, 0));

        System.out.println("\nReveal count: " + game.getRevealState().getRevealCount());
        System.out.println("Valid trio? " + game.getRevealState().isValidTrio());

        System.out.println("\nCurrent player AFTER reveals: "
                + game.getTurnManager().getCurrentStudent().getName());
    }
}
