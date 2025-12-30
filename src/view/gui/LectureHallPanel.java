package view.gui;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel displaying the Lecture Hall (grid of cards).
 * ALL CARDS VISIBLE with IDs
 *
 * @author Acil HAMIEH, Dana SLEIMAN
 * @version 2.0 - Memory game with IDs
 */
public class LectureHallPanel extends JPanel implements CardComponent.CardSelectionListener {
    private GamePanel gamePanel;
    private List<CardComponent> cardComponents;
    private int gridSize; // Dynamic grid size based on player count

    /**
     * Constructor for LectureHallPanel
     * @param gamePanel Parent game panel
     */
    public LectureHallPanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.cardComponents = new ArrayList<>();
        this.gridSize = 9; // Default 3x3

        setLayout(new GridLayout(3, 3, 15, 15));
        setBackground(new Color(240, 248, 255)); // Alice Blue
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.DARK_GRAY, 2),
                        "Lecture Hall (All Visible)",
                        javax.swing.border.TitledBorder.CENTER,
                        javax.swing.border.TitledBorder.TOP,
                        new Font("SansSerif", Font.BOLD, 18)
                ),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
    }

    /**
     * Update display with current lecture hall
     * All cards are VISIBLE with IDs
     * @param lectureHall The lecture hall
     */
    public void updateDisplay(LectureHall lectureHall) {
        // Clear existing cards
        removeAll();
        cardComponents.clear();

        List<Card> cards = lectureHall.getAllCards();
        gridSize = cards.size();

        // Update grid layout based on size
        if (gridSize == 9) {
            setLayout(new GridLayout(3, 3, 15, 15));
        } else if (gridSize == 8) {
            setLayout(new GridLayout(3, 3, 15, 15)); // Still 3x3, one empty
        } else if (gridSize == 6) {
            setLayout(new GridLayout(2, 3, 15, 15)); // 2x3
        }

        // Add cards - ALL VISIBLE (no hidden cards in lecture hall)
        for (int i = 0; i < gridSize; i++) {
            if (i < cards.size()) {
                Card card = cards.get(i);
                // Always visible in lecture hall
                CardComponent cardComp = new CardComponent(card, this, true, i);
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

        JLabel label = new JLabel("Empty", SwingConstants.CENTER);
        label.setForeground(Color.GRAY);
        panel.add(label);

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
        // Pass to GamePanel with hall flag
        gamePanel.onHallCardSelected(card, position);
    }

    @Override
    public void onCardDeselected(Card card, int position) {
        gamePanel.onCardDeselected(card, position);
    }
}
