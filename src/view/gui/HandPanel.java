package view.gui;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel displaying the current player's hand.
 * Shows cards in a horizontal layout.
 *
 * @author Acil HAMIEH
 * @version 1.0
 */
public class HandPanel extends JPanel implements CardComponent.CardSelectionListener {
    private GamePanel gamePanel;
    private List<CardComponent> cardComponents;

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
                "Your Hand",
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 16)
        ));
        setPreferredSize(new Dimension(0, 160));
    }

    /**
     * Update display with current hand
     * @param hand Player's hand
     */
    public void updateDisplay(Hand hand) {
        // Clear existing cards
        removeAll();
        cardComponents.clear();

        // Add cards
        for (Card card : hand.getAllCards()) {
            CardComponent cardComp = new CardComponent(card, this);
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

    @Override
    public void onCardSelected(Card card) {
        gamePanel.onCardSelected(card, true);
    }

    @Override
    public void onCardDeselected(Card card) {
        gamePanel.onCardDeselected(card);
    }
}
