package view.console;

import model.*;
import enums.Branch;
import java.util.List;

/**
 * Utility class for displaying cards in the console.
 * Provides formatted card visualization.
 *
 * @author Dana SLEIMAN
 * @version 1.0
 */
public class CardDisplayer {

    /**
     * Display a hand of cards with indices
     * @param hand The hand to display
     */
    public void displayHand(Hand hand) {
        List<Card> cards = hand.getAllCards();

        if (cards.isEmpty()) {
            System.out.println("  (Empty hand)");
            return;
        }

        // Display in rows of 3
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            System.out.print("  [" + i + "] ");
            displayCardInline(card);

            if ((i + 1) % 3 == 0) {
                System.out.println();
            }
        }

        // New line if last row incomplete
        if (cards.size() % 3 != 0) {
            System.out.println();
        }
    }

    /**
     * Display lecture hall in 3x3 grid
     * @param hall The lecture hall to display
     */
    public void displayLectureHall(LectureHall hall) {
        List<Card> cards = hall.getAllCards();

        if (cards.isEmpty()) {
            System.out.println("  (Empty lecture hall)");
            return;
        }

        // Display in 3x3 grid
        int index = 0;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (index < cards.size()) {
                    Card card = cards.get(index);
                    System.out.print("  [" + index + "] ");
                    displayCardInline(card);
                    System.out.print("  ");
                    index++;
                } else {
                    System.out.print("  [ ] -----        ");
                }
            }
            System.out.println();
        }
    }

    /**
     * Display a single card inline with branch icon
     * @param card The card to display
     */
    private void displayCardInline(Card card) {
        String icon = getBranchIcon(card.getBranch());
        String code = String.format("%-4s", card.getCourseCode());

        if (card.isPFE()) {
            System.out.print("⭐ " + code + " ");
        } else {
            System.out.print(icon + " " + code + " ");
        }
    }

    /**
     * Display a card in detailed box format
     * @param card The card to display
     */
    public void displayCardBox(Card card) {
        String code = card.getCourseCode();
        String branch = getBranchAbbreviation(card.getBranch());
        String icon = getBranchIcon(card.getBranch());

        System.out.println("┌─────────┐");
        System.out.println("│ " + icon + " " + String.format("%-4s", code) + " │");
        System.out.println("│  " + String.format("%-6s", branch) + " │");
        System.out.println("└─────────┘");
    }

    /**
     * Display multiple cards in a row
     * @param cards List of cards to display
     */
    public void displayCardRow(List<Card> cards) {
        for (int i = 0; i < cards.size(); i++) {
            displayCardInline(cards.get(i));
            if (i < cards.size() - 1) {
                System.out.print(" + ");
            }
        }
        System.out.println();
    }

    /**
     * Display a trio with validation result
     * @param card1 First card
     * @param card2 Second card
     * @param card3 Third card
     * @param isValid Whether trio is valid
     * @param ects ECTS credits awarded
     */
    public void displayTrio(Card card1, Card card2, Card card3, boolean isValid, int ects) {
        System.out.println("\n🎯 Trio:");
        System.out.print("   ");
        displayCardInline(card1);
        System.out.print(" + ");
        displayCardInline(card2);
        System.out.print(" + ");
        displayCardInline(card3);
        System.out.println();

        if (isValid) {
            System.out.println("   ✅ Valid! +" + ects + " ECTS");
        } else {
            System.out.println("   ❌ Invalid trio");
        }
    }

    /**
     * Get branch icon for display
     * @param branch The branch
     * @return Icon string
     */
    private String getBranchIcon(Branch branch) {
        switch (branch) {
            case COMPUTER_SCIENCE:
                return "💻";
            case INDUSTRIAL_ENGINEERING:
                return "🏭";
            case MECHANICAL_ENGINEERING:
                return "⚙️";
            case ENERGY_ENGINEERING:
                return "⚡";
            case SPECIAL:
                return "⭐";
            default:
                return "📚";
        }
    }

    /**
     * Get branch abbreviation
     * @param branch The branch
     * @return Abbreviation string
     */
    private String getBranchAbbreviation(Branch branch) {
        switch (branch) {
            case COMPUTER_SCIENCE:
                return "CS";
            case INDUSTRIAL_ENGINEERING:
                return "IE";
            case MECHANICAL_ENGINEERING:
                return "ME";
            case ENERGY_ENGINEERING:
                return "EE";
            case SPECIAL:
                return "PFE";
            default:
                return "???";
        }
    }

    /**
     * Display deck information
     * @param deck The deck
     */
    public void displayDeckInfo(Deck deck) {
        System.out.println("🎲 Deck: " + deck.getRemainingCount() + " cards remaining");
    }

    /**
     * Display a separator line
     */
    public void displaySeparator() {
        System.out.println("─".repeat(50));
    }

    /**
     * Display a header
     * @param title Header title
     */
    public void displayHeader(String title) {
        System.out.println("\n" + "═".repeat(50));
        System.out.println("  " + title);
        System.out.println("═".repeat(50));
    }
}
