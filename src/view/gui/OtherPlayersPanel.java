package view.gui;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Panel displaying other players' hands.
 * REVEALING MODE: All cards face-down [?], only first/last clickable
 *
 * @author Dana SLEIMAN, Acil HAMIEH
 * @version 3.0 - Revealing system
 */
public class OtherPlayersPanel extends JPanel implements CardComponent.CardSelectionListener {
    private GamePanel gamePanel;
    private Map<String, List<CardComponent>> playerCardComponents;
    private List<JPanel> playerHandPanels;
    private boolean compactMode = false;

    public OtherPlayersPanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.playerCardComponents = new HashMap<>();
        this.playerHandPanels = new ArrayList<>();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(230, 240, 250));
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                "Other Players (Click First/Last)",
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 13)
        ));
    }

    public void setCompactMode(boolean compact) {
        this.compactMode = compact;
    }

    /**
     * Update display for REVEALING mode
     * All cards face-down, only first/last clickable
     */
    public void updateDisplay(List<Student> allPlayers, Student currentPlayer) {
        removeAll();
        playerCardComponents.clear();
        playerHandPanels.clear();

        for (Student player : allPlayers) {
            if (!player.equals(currentPlayer)) {
                JPanel playerPanel = createPlayerHandPanel(player);
                add(playerPanel);
                add(Box.createRigidArea(new Dimension(0, 5)));
                playerHandPanels.add(playerPanel);
            }
        }

        revalidate();
        repaint();
    }

    /**
     * Create panel for one player - REVEALING mode
     */
    private JPanel createPlayerHandPanel(Student player) {
        JPanel panel = new JPanel();
        int padding = compactMode ? 3 : 5;
        panel.setLayout(new BorderLayout(padding, padding));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(padding, padding, padding, padding)
        ));

        // Player name
        JLabel nameLabel = new JLabel(player.getName() + " (" + player.getHand().getSize() + " cards)");
        int fontSize = compactMode ? 11 : 12;
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, fontSize));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(nameLabel, BorderLayout.NORTH);

        // Cards panel
        int cardGap = compactMode ? 3 : 4;
        JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, cardGap, cardGap));
        cardsPanel.setBackground(Color.WHITE);

        Hand hand = player.getHand();
        List<Card> cards = hand.getAllCards();
        List<CardComponent> cardComps = new ArrayList<>();
        List<Integer> firstLast = HandPositionHelper.getFirstLastPositions(hand);

        // All cards face-down, only first/last clickable
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);

            // All face-down
            CardComponent cardComp = new CardComponent(card, this, false, i);

            // Only first/last clickable
            boolean isClickable = firstLast.contains(i);
            cardComp.setClickable(isClickable);

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
     * Clear all reveals
     */
    public void clearReveals() {
        for (List<CardComponent> cardComps : playerCardComponents.values()) {
            for (CardComponent cardComp : cardComps) {
                cardComp.setRevealed(false);
            }
        }
    }

    /**
     * Mark a card as revealed (flip face up and highlight)
     */
    public void markCardRevealed(String playerName, Card card, int position) {
        List<CardComponent> cardComps = playerCardComponents.get(playerName);
        if (cardComps != null && position >= 0 && position < cardComps.size()) {
            // ✅ FIX: Use position directly, don't compare cards!
            // Position is unique and reliable, card.equals() is not (duplicates!)
            CardComponent cardComp = cardComps.get(position);
            cardComp.setRevealed(true);
        }
    }

    /**
     * Flip a card back face down
     */
    public void flipCardBack(String playerName, int position) {
        List<CardComponent> cardComps = playerCardComponents.get(playerName);
        if (cardComps != null) {
            for (CardComponent cardComp : cardComps) {
                if (cardComp.getPositionIndex() == position) {
                    cardComp.flipFaceDown();
                    cardComp.setRevealed(false);
                    break;
                }
            }
        }
    }

    /**
     * Enable/disable all cards
     */
    public void setCardsEnabled(boolean enabled) {
        for (Map.Entry<String, List<CardComponent>> entry : playerCardComponents.entrySet()) {
            for (CardComponent cardComp : entry.getValue()) {
                // Re-check clickability based on position
                String playerName = (String) cardComp.getClientProperty("playerName");
                // Find player's hand
                // For now, just enable/disable based on first/last
                // This is simplified - in full implementation, check HandPositionHelper
                cardComp.setClickable(enabled);
            }
        }
    }

    public int getSelectedCount() {
        int count = 0;
        for (List<CardComponent> cardComps : playerCardComponents.values()) {
            for (CardComponent cardComp : cardComps) {
                if (cardComp.isSelected()) count++;
            }
        }
        return count;
    }

    @Override
    public void onCardSelected(Card card, int position) {
        // Get player name
        String playerName = null;
        for (Map.Entry<String, List<CardComponent>> entry : playerCardComponents.entrySet()) {
            for (CardComponent cardComp : entry.getValue()) {
                if (cardComp.getCard().equals(card) && cardComp.getPositionIndex() == position) {
                    playerName = (String) cardComp.getClientProperty("playerName");
                    break;
                }
            }
            if (playerName != null) break;
        }

        gamePanel.onOtherPlayerCardSelected(card, position, playerName);
    }

    @Override
    public void onCardDeselected(Card card, int position) {
        gamePanel.onCardDeselected(card, position);
    }
}
