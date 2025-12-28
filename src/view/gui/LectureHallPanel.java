package view.gui;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel displaying the Lecture Hall (3x3 grid of cards).
 * Shows available cards that can be selected.
 *
 * @author Acil HAMIEH
 * @version 1.0
 */
public class LectureHallPanel extends JPanel implements CardComponent.CardSelectionListener {
    private GamePanel gamePanel;
    private List<CardComponent> cardComponents;

    /**
     * Constructor for LectureHallPanel
     * @param gamePanel Parent game panel
     */
    public LectureHallPanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.cardComponents = new ArrayList<>();

        setLayout(new GridLayout(3, 3, 15, 15));
        setBackground(new Color(240, 248, 255)); // Alice Blue
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.DARK_GRAY, 2),
                        "Lecture Hall",
                        javax.swing.border.TitledBorder.CENTER,
                        javax.swing.border.TitledBorder.TOP,
                        new Font("SansSerif", Font.BOLD, 18)
                ),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
    }

    /**
     * Update display with current lecture hall
     * @param lectureHall The lecture hall
     */
    public void updateDisplay(LectureHall lectureHall) {
        // Clear existing cards
        removeAll();
        cardComponents.clear();

        // Add cards in 3x3 grid
        List<Card> cards = lectureHall.getAllCards();

        for (int i = 0; i < 9; i++) {
            if (i < cards.size()) {
                Card card = cards.get(i);
                CardComponent cardComp = new CardComponent(card, this);
                cardComponents.add(cardComp);
                add(cardComp);
            } else {
                // Empty slot
                JPanel emptySlot = createEmptySlot();
                add(emptySlot);
            }
        }

        revalidate();
        repaint();
    }

    /**
     * Create empty slot panel
     * @return Empty slot panel
     */
    private JPanel createEmptySlot() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(200, 200, 200));
        panel.setBorder(BorderFactory.createDashedBorder(Color.GRAY, 2, 5, 5, true));
        return panel;
    }

    /**
     * Clear selection on all cards
     */
    public void clearSelection() {
        for (CardComponent cardComp : cardComponents) {
            cardComp.setSelected(false);
        }
    }

    @Override
    public void onCardSelected(Card card) {
        gamePanel.onCardSelected(card, false);
    }

    @Override
    public void onCardDeselected(Card card) {
        gamePanel.onCardDeselected(card);
    }
}
