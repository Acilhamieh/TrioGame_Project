package view.gui;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Panel displaying ALL other players' hands.
 * Shows first & last cards visible, middle hidden.
 * Player can select from ANY other player.
 * COMPACT: Reduced spacing
 *
 * @author Dana SLEIMAN, Acil HAMIEH
 * @version 1.1 - Compact spacing
 */
public class OtherPlayersPanel extends JPanel implements CardComponent.CardSelectionListener {
    private GamePanel gamePanel;
    private Map<String, List<CardComponent>> playerCardComponents;
    private List<JPanel> playerHandPanels;

    /**
     * Constructor for OtherPlayersPanel
     * @param gamePanel Parent game panel
     */
    public OtherPlayersPanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.playerCardComponents = new HashMap<>();
        this.playerHandPanels = new ArrayList<>();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(230, 240, 250)); // Light blue
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                "Other Players' Hands (First & Last Visible)",
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 14)  // REDUCED from 16
        ));
    }

    /**
     * Update display with all other players' hands
     * @param allPlayers All players in game
     * @param currentPlayer The current player (don't show their hand here)
     */
    public void updateDisplay(List<Student> allPlayers, Student currentPlayer) {
        // Clear existing
        removeAll();
        playerCardComponents.clear();
        playerHandPanels.clear();

        // Add each other player's hand
        for (Student player : allPlayers) {
            if (!player.equals(currentPlayer)) {
                JPanel playerPanel = createPlayerHandPanel(player);
                add(playerPanel);
                add(Box.createRigidArea(new Dimension(0, 5)));  // REDUCED from 10
                playerHandPanels.add(playerPanel);
            }
        }

        revalidate();
        repaint();
    }

    /**
     * Create a panel for one player's hand - COMPACT
     * @param player The player
     * @return Panel showing their hand
     */
    private JPanel createPlayerHandPanel(Student player) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(3, 3));  // REDUCED from 5
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)  // REDUCED from 10
        ));

        // Player name label
        JLabel nameLabel = new JLabel(player.getName() + " (" + player.getHand().getSize() + " cards)");
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 12));  // REDUCED from 14
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(nameLabel, BorderLayout.NORTH);

        // Cards panel
        JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 4));  // REDUCED from 5
        cardsPanel.setBackground(Color.WHITE);

        Hand hand = player.getHand();
        List<Card> cards = hand.getAllCards();
        List<CardComponent> cardComps = new ArrayList<>();

        // Add cards with visibility rules (first & last only)
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            boolean visible = hand.isCardVisible(i); // First & last only

            CardComponent cardComp = new CardComponent(card, this, visible, i);
            // Store player name in component for later identification
            cardComp.putClientProperty("playerName", player.getName());
            cardComps.add(cardComp);
            cardsPanel.add(cardComp);
        }

        playerCardComponents.put(player.getName(), cardComps);
        panel.add(cardsPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Clear all selections
     */
    public void clearSelection() {
        for (List<CardComponent> cardComps : playerCardComponents.values()) {
            for (CardComponent cardComp : cardComps) {
                cardComp.setSelected(false);
            }
        }
    }

    /**
     * Get selected card count across all players
     * @return Number of selected cards
     */
    public int getSelectedCount() {
        int count = 0;
        for (List<CardComponent> cardComps : playerCardComponents.values()) {
            for (CardComponent cardComp : cardComps) {
                if (cardComp.isSelected()) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Get selected card and player name
     * @return Array [Card, PlayerName] or null if none selected
     */
    public Object[] getSelectedCardAndPlayer() {
        for (Map.Entry<String, List<CardComponent>> entry : playerCardComponents.entrySet()) {
            String playerName = entry.getKey();
            for (CardComponent cardComp : entry.getValue()) {
                if (cardComp.isSelected()) {
                    return new Object[]{cardComp.getCard(), playerName};
                }
            }
        }
        return null;
    }

    @Override
    public void onCardSelected(Card card, int position) {
        // Clear other selections in this panel first (only 1 card from other players)
        clearSelection();

        // Re-select this card
        for (List<CardComponent> cardComps : playerCardComponents.values()) {
            for (CardComponent cardComp : cardComps) {
                if (cardComp.getCard().equals(card)) {
                    cardComp.setSelected(true);
                }
            }
        }

        // Get player name from card component
        String playerName = null;
        for (Map.Entry<String, List<CardComponent>> entry : playerCardComponents.entrySet()) {
            for (CardComponent cardComp : entry.getValue()) {
                if (cardComp.getCard().equals(card) && cardComp.isSelected()) {
                    playerName = (String) cardComp.getClientProperty("playerName");
                    break;
                }
            }
        }

        gamePanel.onOtherPlayerCardSelected(card, position, playerName);
    }

    @Override
    public void onCardDeselected(Card card, int position) {
        gamePanel.onCardDeselected(card, position);
    }
}
