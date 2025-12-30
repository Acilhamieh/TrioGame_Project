package view.gui;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel displaying the current player's hand.
 * MEMORY GAME: Shows first & last cards visible, middle hidden
 *
 * @author Acil HAMIEH, Dana SLEIMAN
 * @version 2.0 - Memory game support
 */
public class HandPanel extends JPanel implements CardComponent.CardSelectionListener {
    private GamePanel gamePanel;
    private List<CardComponent> cardComponents;
    private Hand currentHand;

    /**
     * Constructor for HandPanel
     * @param gamePanel Parent game panel
     */
    public HandPanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.cardComponents = new ArrayList<>();

        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        setBackground(new Color(220, 220, 220));
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2),
                "Your Hand (All Visible to You)",
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 16)
        ));
        setPreferredSize(new Dimension(0, 160));
    }

    /**
     * Update display with current hand
     * Shows ONLY first and last cards, hides middle
     * @param hand Player's hand
     */
    public void updateDisplay(Hand hand) {
        this.currentHand = hand;

        // Clear existing cards
        removeAll();
        cardComponents.clear();

        List<Card> cards = hand.getAllCards();

        // Add cards with visibility rules
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            boolean visible = hand.isCardVisible(i); // First & last only

            CardComponent cardComp = new CardComponent(card, this, visible, i);
            cardComponents.add(cardComp);
            add(cardComp);
        }

        revalidate();
        repaint();
    }

    /**
     * Update display with ALL cards visible (for current player's own hand)
     * MEMORY GAME FIX: You can see ALL your own cards!
     * @param hand Player's hand
     */
    public void updateDisplayAllVisible(Hand hand) {
        this.currentHand = hand;

        // Clear existing cards
        removeAll();
        cardComponents.clear();

        List<Card> cards = hand.getAllCards();

        // Add cards - ALL VISIBLE
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);

            CardComponent cardComp = new CardComponent(card, this, true, i); // ALL VISIBLE!
            cardComponents.add(cardComp);
            add(cardComp);
        }

        revalidate();
        repaint();
    }

    /**
     * Clear selection on all cards
     */
    public void clearSelection() {
        for (CardComponent cardComp : cardComponents) {
            cardComp.setSelected(false);
        }
    }

    /**
     * Get selected card count
     * @return Number of selected cards
     */
    public int getSelectedCount() {
        int count = 0;
        for (CardComponent cardComp : cardComponents) {
            if (cardComp.isSelected()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Get all card components
     * @return List of card components
     */
    public List<CardComponent> getCardComponents() {
        return new ArrayList<>(cardComponents);
    }

    @Override
    public void onCardSelected(Card card, int position) {
        // NEW: Pass position to GamePanel
        gamePanel.onHandCardSelected(card, position);
    }

    @Override
    public void onCardDeselected(Card card, int position) {
        gamePanel.onCardDeselected(card, position);
    }
}
