package view.gui;

import model.Card;
import enums.Branch;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Custom component for displaying a card in the GUI.
 * MEMORY GAME: Shows/hides cards, displays ID
 * SIMPLIFIED: No icon, more space for text
 *
 * @author Acil HAMIEH, Dana SLEIMAN
 * @version 2.2 - Removed icon for better visibility
 */
public class CardComponent extends JPanel {
    private Card card;
    private boolean selected;
    private boolean hovered;
    private CardSelectionListener listener;
    private boolean visible;  // NEW: Is this card visible or hidden?
    private int positionIndex;  // NEW: Position in hand

    // Colors
    private static final Color NORMAL_BG = new Color(255, 255, 255);
    private static final Color HIDDEN_BG = new Color(200, 200, 200);
    private static final Color SELECTED_BG = new Color(144, 238, 144);  // Light green
    private static final Color HOVER_BG = new Color(230, 230, 250);     // Lavender
    private static final Color BORDER_COLOR = Color.DARK_GRAY;

    /**
     * Interface for card selection listeners
     */
    public interface CardSelectionListener {
        void onCardSelected(Card card, int position);
        void onCardDeselected(Card card, int position);
    }

    /**
     * Constructor for always-visible cards (lecture hall)
     * @param card The card to display
     * @param listener Selection listener
     */
    public CardComponent(Card card, CardSelectionListener listener) {
        this(card, listener, true, -1);
    }

    /**
     * Constructor with visibility control (for hands)
     * @param card The card to display
     * @param listener Selection listener
     * @param visible Is this card visible?
     * @param positionIndex Position in hand (0-8)
     */
    public CardComponent(Card card, CardSelectionListener listener, boolean visible, int positionIndex) {
        this.card = card;
        this.listener = listener;
        this.selected = false;
        this.hovered = false;
        this.visible = visible;
        this.positionIndex = positionIndex;

        setupComponent();
        setupMouseListeners();
    }

    /**
     * Setup component properties - SIMPLIFIED without icon
     */
    private void setupComponent() {
        setPreferredSize(new Dimension(90, 110));  // Slightly smaller now
        setBackground(visible ? NORMAL_BG : HIDDEN_BG);
        setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 2));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Add content
        createContent();
    }

    /**
     * Create card content based on visibility
     */
    private void createContent() {
        if (visible) {
            createVisibleCard();
        } else {
            createHiddenCard();
        }
    }

    /**
     * Create visible card - SIMPLIFIED without icon
     */
    private void createVisibleCard() {
        // Add vertical glue for centering
        add(Box.createVerticalGlue());

        // ID badge (top)
        JLabel idLabel = new JLabel("ID:" + card.getId());
        idLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        idLabel.setForeground(new Color(100, 100, 100));
        idLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(idLabel);

        add(Box.createRigidArea(new Dimension(0, 8)));

        // Course code (MAIN - BIG AND CLEAR!)
        JLabel codeLabel = new JLabel(card.getCourseCode());
        codeLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        codeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(codeLabel);

        add(Box.createRigidArea(new Dimension(0, 8)));

        // Branch abbreviation
        JLabel branchLabel = new JLabel(getBranchAbbr(card.getBranch()));
        branchLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        branchLabel.setForeground(Color.GRAY);
        branchLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(branchLabel);

        // Add vertical glue for centering
        add(Box.createVerticalGlue());

        // Special styling for PFE cards
        if (card.isPFE()) {
            codeLabel.setForeground(new Color(255, 215, 0)); // Gold
            branchLabel.setForeground(new Color(218, 165, 32)); // Goldenrod
        }
    }

    /**
     * Create hidden card [?]
     */
    private void createHiddenCard() {
        // Add vertical glue for centering
        add(Box.createVerticalGlue());

        // Big question mark
        JLabel questionLabel = new JLabel("?");
        questionLabel.setFont(new Font("SansSerif", Font.BOLD, 48));
        questionLabel.setForeground(new Color(150, 150, 150));
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(questionLabel);

        // Show position number
        if (positionIndex >= 0) {
            add(Box.createRigidArea(new Dimension(0, 5)));
            JLabel posLabel = new JLabel("Pos: " + positionIndex);
            posLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
            posLabel.setForeground(new Color(100, 100, 100));
            posLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(posLabel);
        }

        // Add vertical glue for centering
        add(Box.createVerticalGlue());
    }

    /**
     * Get branch abbreviation
     */
    private String getBranchAbbr(Branch branch) {
        switch (branch) {
            case COMPUTER_SCIENCE: return "CS";
            case INDUSTRIAL_ENGINEERING: return "IE";
            case MECHANICAL_ENGINEERING: return "ME";
            case ENERGY_ENGINEERING: return "EE";
            case SPECIAL: return "PFE";
            default: return "";
        }
    }

    /**
     * Setup mouse listeners for selection
     */
    private void setupMouseListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toggleSelection();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!selected) {
                    hovered = true;
                    updateAppearance();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                updateAppearance();
            }
        });
    }

    /**
     * Toggle selection state
     */
    private void toggleSelection() {
        selected = !selected;
        updateAppearance();

        if (listener != null) {
            if (selected) {
                listener.onCardSelected(card, positionIndex);
            } else {
                listener.onCardDeselected(card, positionIndex);
            }
        }
    }

    /**
     * Update visual appearance based on state
     */
    private void updateAppearance() {
        if (selected) {
            setBackground(SELECTED_BG);
            setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
        } else if (hovered) {
            setBackground(HOVER_BG);
            setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 2));
        } else {
            setBackground(visible ? NORMAL_BG : HIDDEN_BG);
            setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 2));
        }
        repaint();
    }

    /**
     * Set selection state programmatically
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
        updateAppearance();
    }

    /**
     * Check if card is selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Get the card
     */
    public Card getCard() {
        return card;
    }

    /**
     * Set card visibility (show/hide)
     */
    public void setVisible(boolean visible) {
        if (this.visible != visible) {
            this.visible = visible;
            // Rebuild content
            removeAll();
            createContent();
            revalidate();
            repaint();
        }
    }

    /**
     * Check if card is visible
     */
    public boolean isCardVisible() {
        return visible;
    }

    /**
     * Get position index
     */
    public int getPositionIndex() {
        return positionIndex;
    }
}
