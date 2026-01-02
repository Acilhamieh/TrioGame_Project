package model;

/**
 * Helper class to store a card with its position in hand.
 * Used for auto-revealing duplicates.
 *
 * @author Dana SLEIMAN, Acil HAMIEH
 * @version 1.0
 */
public class CardWithPosition {
    public final Card card;
    public final int position;

    public CardWithPosition(Card card, int position) {
        this.card = card;
        this.position = position;
    }
}
