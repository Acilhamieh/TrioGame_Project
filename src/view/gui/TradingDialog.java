package view.gui;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;


public class TradingDialog extends JDialog {
    private Student player1;
    private Student player2;
    private Card selectedCard1;
    private Card selectedCard2;
    private JPanel cardsPanel1;
    private JPanel cardsPanel2;
    private JButton confirmButton;
    private JButton skipButton;
    private boolean tradeMade = false;

    public TradingDialog(Frame parent, Student player1, Student player2) {
        super(parent, "Card Trading - " + player1.getName() + " ↔ " + player2.getName(), true);
        this.player1 = player1;
        this.player2 = player2;

        initializeUI();
        setSize(800, 400);
        setLocationRelativeTo(parent);
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));

        // Title
        JLabel titleLabel = new JLabel("Select cards to trade (face-down exchange)", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(titleLabel, BorderLayout.NORTH);

        // Main panel with both players
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Player 1 panel
        JPanel player1Panel = createPlayerPanel(player1, true);
        mainPanel.add(player1Panel);

        // Player 2 panel
        JPanel player2Panel = createPlayerPanel(player2, false);
        mainPanel.add(player2Panel);

        add(mainPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        confirmButton = new JButton("Confirm Trade");
        confirmButton.setEnabled(false);
        confirmButton.addActionListener(e -> confirmTrade());

        skipButton = new JButton("Skip Trade");
        skipButton.addActionListener(e -> skipTrade());

        buttonPanel.add(confirmButton);
        buttonPanel.add(skipButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createPlayerPanel(Student player, boolean isPlayer1) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(player.getName()));

        // Instructions
        JLabel instLabel = new JLabel("Click a card to select (even from middle)", SwingConstants.CENTER);
        instLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        panel.add(instLabel, BorderLayout.NORTH);

        // Cards
        JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        List<Card> cards = player.getHand().getAllCards();

        for (int i = 0; i < cards.size(); i++) {
            final int index = i;
            Card card = cards.get(i);

            JButton cardButton = new JButton(card.getCourseCode());
            cardButton.setPreferredSize(new Dimension(70, 100));
            cardButton.setFont(new Font("SansSerif", Font.BOLD, 12));

            cardButton.addActionListener(e -> {
                if (isPlayer1) {
                    selectCard1(card, index, cardButton, cardsPanel);
                } else {
                    selectCard2(card, index, cardButton, cardsPanel);
                }
            });

            cardsPanel.add(cardButton);
        }

        if (isPlayer1) {
            cardsPanel1 = cardsPanel;
        } else {
            cardsPanel2 = cardsPanel;
        }

        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void selectCard1(Card card, int index, JButton button, JPanel panel) {
        selectedCard1 = card;

        // Clear previous selection
        for (Component c : panel.getComponents()) {
            c.setBackground(null);
        }

        // Highlight selected
        button.setBackground(Color.YELLOW);

        checkBothSelected();
    }

    private void selectCard2(Card card, int index, JButton button, JPanel panel) {
        selectedCard2 = card;

        // Clear previous selection
        for (Component c : panel.getComponents()) {
            c.setBackground(null);
        }

        // Highlight selected
        button.setBackground(Color.YELLOW);

        checkBothSelected();
    }

    private void checkBothSelected() {
        confirmButton.setEnabled(selectedCard1 != null && selectedCard2 != null);
    }

    private void confirmTrade() {
        if (selectedCard1 != null && selectedCard2 != null) {
            // Remove cards from hands
            player1.getHand().removeCard(selectedCard1);
            player2.getHand().removeCard(selectedCard2);

            // Add to other player (will be sorted automatically by Hand.addCard())
            player1.getHand().addCard(selectedCard2);
            player2.getHand().addCard(selectedCard1);

            tradeMade = true;

            JOptionPane.showMessageDialog(this,
                    "Cards exchanged!\n" +
                            player1.getName() + " → " + player2.getName() + "\n" +
                            player2.getName() + " → " + player1.getName(),
                    "Trade Complete",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();
        }
    }

    private void skipTrade() {
        tradeMade = false;
        dispose();
    }

    public boolean wasTradeMade() {
        return tradeMade;
    }
}
