package view.gui;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class HandPanel extends JPanel implements CardComponent.CardSelectionListener {
    private GamePanel gamePanel;
    private List<CardComponent> cardComponents;
    private Hand currentHand;

    public HandPanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.cardComponents = new ArrayList<>();

        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        setBackground(new Color(220, 220, 220));
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2),
                "Your Hand (Click First/Last Cards)",
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 14)
        ));
        setPreferredSize(new Dimension(0, 140));
    }

    /*Update display for REVEALING mode
      All cards face-up, only revealable positions clickable
     */
    public void updateDisplay(Hand hand) {
        this.currentHand = hand;
        removeAll();
        cardComponents.clear();

        List<Card> cards = hand.getAllCards();
        List<Integer> revealablePositions = hand.getRevealablePositions();

        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);

            // All cards face-up (you can see your own cards)
            CardComponent cardComp = new CardComponent(card, this, true, i);

            // Only revealable positions are clickable
            boolean isClickable = revealablePositions.contains(i);
            cardComp.setClickable(isClickable);

            cardComponents.add(cardComp);
            add(cardComp);
        }

        revalidate();
        repaint();
    }

    // OLD METHOD: For flexible mode compatibility

    public void updateDisplayAllVisible(Hand hand) {
        updateDisplay(hand);  // Same behavior in revealing mode
    }

    //Clear all selections

    public void clearSelection() {
        for (CardComponent cardComp : cardComponents) {
            cardComp.setSelected(false);
        }
    }

    //Clear all reveals (flip back face down not needed - stays face up)

    public void clearReveals() {
        for (CardComponent cardComp : cardComponents) {
            cardComp.setRevealed(false);
        }
    }

    //Mark a card as revealed (highlight it)

    public void markCardRevealed(Card card, int position) {
        //  FIX: Use position directly
        if (position >= 0 && position < cardComponents.size()) {
            CardComponent cardComp = cardComponents.get(position);
            cardComp.setRevealed(true);
        }
    }
    public void disableCard(int position) {
        if (position >= 0 && position < cardComponents.size()) {
            CardComponent cardComp = cardComponents.get(position);
            cardComp.setClickable(false);
        }
    }
    //Enable/disable all cards

    public void setCardsEnabled(boolean enabled) {
        if (currentHand != null) {
            List<Integer> revealablePositions = currentHand.getRevealablePositions();
            for (CardComponent cardComp : cardComponents) {
                boolean clickable = enabled && revealablePositions.contains(cardComp.getPositionIndex());
                cardComp.setClickable(clickable);
            }
        }
    }

    public int getSelectedCount() {
        int count = 0;
        for (CardComponent cardComp : cardComponents) {
            if (cardComp.isSelected()) count++;
        }
        return count;
    }

    public List<CardComponent> getCardComponents() {
        return new ArrayList<>(cardComponents);
    }

    @Override
    public void onCardSelected(Card card, int position) {
        gamePanel.onHandCardSelected(card, position);
    }

    @Override
    public void onCardDeselected(Card card, int position) {
        gamePanel.onCardDeselected(card, position);
    }
}
