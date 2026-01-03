package view.gui;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel displaying lecture hall.
 * REVEALING MODE: All cards face-down [?], all clickable
 *
 * @author Acil HAMIEH, Dana SLEIMAN
 * @version 3.0 - Revealing system
 */
public class LectureHallPanel extends JPanel implements CardComponent.CardSelectionListener {
    private GamePanel gamePanel;
    private List<CardComponent> cardComponents;
    private int gridSize;

    public LectureHallPanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.cardComponents = new ArrayList<>();
        this.gridSize = 9;

        setLayout(new GridLayout(3, 3, 8, 8));
        setBackground(new Color(240, 248, 255));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.DARK_GRAY, 2),
                        "Lecture Hall (Click Any Card)",
                        javax.swing.border.TitledBorder.CENTER,
                        javax.swing.border.TitledBorder.TOP,
                        new Font("SansSerif", Font.BOLD, 16)
                ),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
    }

    /**
     * Update display for REVEALING mode
     * All cards face-down, all clickable
     */
    public void updateDisplay(LectureHall lectureHall) {
        removeAll();
        cardComponents.clear();

        List<Card> cards = lectureHall.getAllCards();
        gridSize = cards.size();

        // Update grid layout
        if (gridSize == 9) {
            setLayout(new GridLayout(3, 3, 8, 8));
        } else if (gridSize == 8) {
            setLayout(new GridLayout(3, 3, 8, 8));
        } else if (gridSize == 6) {
            setLayout(new GridLayout(2, 3, 8, 8));
        }

        // All cards face-down, all clickable
        for (int i = 0; i < gridSize; i++) {
            if (i < cards.size()) {
                Card card = cards.get(i);
                CardComponent cardComp = new CardComponent(card, this, false, i);  // Face-down
                cardComp.setClickable(true);  // All clickable
                cardComponents.add(cardComp);
                add(cardComp);
            } else {
                add(createEmptySlot());
            }
        }

        revalidate();
        repaint();
    }

    private JPanel createEmptySlot() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(220, 220, 220));
        panel.setBorder(BorderFactory.createDashedBorder(Color.GRAY, 1, 3, 3, true));

        JLabel label = new JLabel("Empty", SwingConstants.CENTER);
        label.setForeground(Color.GRAY);
        label.setFont(new Font("SansSerif", Font.PLAIN, 10));
        panel.add(label);

        return panel;
    }

    /**
     * Clear all selections
     */
    public void clearSelection() {
        for (CardComponent cardComp : cardComponents) {
            cardComp.setSelected(false);
        }
    }

    /**
     * Clear all reveals
     */
    public void clearReveals() {
        for (CardComponent cardComp : cardComponents) {
            cardComp.setRevealed(false);
        }
    }

    /**
     * Mark a card as revealed (flip face up and highlight)
     */
    public void markCardRevealed(Card card, int position) {
        //  FIX: Use position directly
        if (position >= 0 && position < cardComponents.size()) {
            CardComponent cardComp = cardComponents.get(position);
            cardComp.setRevealed(true);
        }
    }
    /**
     *  NEW: Disable a card after it's been clicked to prevent double-clicks
     */
    public void disableCard(int position) {
        if (position >= 0 && position < cardComponents.size()) {
            CardComponent cardComp = cardComponents.get(position);
            cardComp.setClickable(false);
        }
    }
    /**
     * Flip a card back face down
     */
    public void flipCardBack(int position) {
        for (CardComponent cardComp : cardComponents) {
            if (cardComp.getPositionIndex() == position) {
                cardComp.flipFaceDown();
                cardComp.setRevealed(false);
                break;
            }
        }
    }

    /**
     * Enable/disable all cards
     */
    public void setCardsEnabled(boolean enabled) {
        for (CardComponent cardComp : cardComponents) {
            cardComp.setClickable(enabled);
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
        gamePanel.onHallCardSelected(card, position);
    }

    @Override
    public void onCardDeselected(Card card, int position) {
        gamePanel.onCardDeselected(card, position);
    }
}
