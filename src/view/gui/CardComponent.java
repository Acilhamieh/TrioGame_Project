package view.gui;

import model.Card;
import enums.Branch;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Visual component representing a single card.
 * Supports MEMORY GAME: Can be hidden or visible, displays ID
 *
 * @author Acil HAMIEH, Dana SLEIMAN
 * @version 2.0 - Memory game support
 */
public class CardComponent extends JPanel {
    private Card card;
    private boolean selected;
    private boolean hovered;
    private boolean visible;  // NEW: Can be hidden [?]
    private int positionIndex; // NEW: Position in hand for selection
    private CardSelectionListener listener;

    // Colors
    private static final Color NORMAL_BG = Color.WHITE;
    private static final Color SELECTED_BG = new Color(144, 238, 144); // Light Green
    private static final Color HOVER_BG = new Color(230, 230, 250); // Lavender
    private static final Color BORDER_COLOR = new Color(100, 100, 100);
    private static final Color SELECTED_BORDER = new Color(34, 139, 34); // Forest Green
    private static final Color HIDDEN_BG = new Color(200, 200, 200); // Gray for hidden cards

    /**
     * Constructor for CardComponent (always visible - for lecture hall)
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
     * @param visible Whether card face is visible
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
     * Setup component properties
     */
    private void setupComponent() {
        setPreferredSize(new Dimension(80, 110));
        setBackground(visible ? NORMAL_BG : HIDDEN_BG);
        setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 2));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setLayout(new BorderLayout(5, 5));

        // Add content
        createContent();
    }

    /**
     * Create card content (visible or hidden)
     */
    private void createContent() {
        removeAll(); // Clear previous content

        if (visible && card != null) {
            // VISIBLE CARD: Show all details
            createVisibleCard();
        } else {
            // HIDDEN CARD: Show [?]
            createHiddenCard();
        }
    }

    /**
     * Create visible card with ID
     */
    private void createVisibleCard() {
        // Top: Branch icon + ID
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel iconLabel = new JLabel(getBranchIcon(card.getBranch()), SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        topPanel.add(iconLabel, BorderLayout.CENTER);

        // ID badge
        JLabel idLabel = new JLabel("ID:" + card.getId());
        idLabel.setFont(new Font("SansSerif", Font.BOLD, 9));
        idLabel.setForeground(new Color(100, 100, 100));
        idLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(idLabel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // Center: Course code
        JLabel codeLabel = new JLabel(card.getCourseCode(), SwingConstants.CENTER);
        codeLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(codeLabel, BorderLayout.CENTER);

        // Bottom: Branch abbreviation
        JLabel branchLabel = new JLabel(getBranchAbbr(card.getBranch()), SwingConstants.CENTER);
        branchLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
        branchLabel.setForeground(Color.GRAY);
        add(branchLabel, BorderLayout.SOUTH);

        // Special styling for PFE cards
        if (card.isPFE()) {
            codeLabel.setForeground(new Color(255, 215, 0)); // Gold
            iconLabel.setForeground(new Color(255, 215, 0));
        }
    }

    /**
     * Create hidden card [?]
     */
    private void createHiddenCard() {
        // Show [?] and position
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);

        JLabel questionLabel = new JLabel("?", SwingConstants.CENTER);
        questionLabel.setFont(new Font("SansSerif", Font.BOLD, 48));
        questionLabel.setForeground(new Color(150, 150, 150));
        centerPanel.add(questionLabel, BorderLayout.CENTER);

        // Show position number
        if (positionIndex >= 0) {
            JLabel posLabel = new JLabel("Pos: " + positionIndex, SwingConstants.CENTER);
            posLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
            posLabel.setForeground(new Color(100, 100, 100));
            centerPanel.add(posLabel, BorderLayout.SOUTH);
        }

        add(centerPanel, BorderLayout.CENTER);

        setBackground(HIDDEN_BG);
    }

    /**
     * Setup mouse listeners for hover and click
     */
    private void setupMouseListeners() {
        addMouseListener(new MouseAdapter() {
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

            @Override
            public void mouseClicked(MouseEvent e) {
                toggleSelection();
            }
        });
    }

    /**
     * Toggle card selection
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
     * Set selection state programmatically
     * @param selected Selection state
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
        updateAppearance();
    }

    /**
     * Check if card is selected
     * @return True if selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Set visibility of card face
     * @param visible True to show card, false to show [?]
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
        createContent();
        revalidate();
        repaint();
    }

    /**
     * Check if card face is visible
     * @return True if visible
     */
    public boolean isCardVisible() {
        return visible;
    }

    /**
     * Get position index
     * @return Position in hand
     */
    public int getPositionIndex() {
        return positionIndex;
    }

    /**
     * Update visual appearance based on state
     */
    private void updateAppearance() {
        if (selected) {
            setBackground(SELECTED_BG);
            setBorder(BorderFactory.createLineBorder(SELECTED_BORDER, 3));
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
     * Get card instance
     * @return The card
     */
    public Card getCard() {
        return card;
    }

    /**
     * Get branch icon emoji
     * @param branch Card branch
     * @return Icon string
     */
    private String getBranchIcon(Branch branch) {
        switch (branch) {
            case COMPUTER_SCIENCE:
                return "💻";
            case INDUSTRIAL_ENGINEERING:
                return "🏭";
            case MECHANICAL_ENGINEERING:
                return "⚙️";
            case ENERGY_ENGINEERING:
                return "⚡";
            case SPECIAL:
                return "⭐";
            default:
                return "📚";
        }
    }

    /**
     * Get branch abbreviation
     * @param branch Card branch
     * @return Abbreviation
     */
    private String getBranchAbbr(Branch branch) {
        switch (branch) {
            case COMPUTER_SCIENCE:
                return "CS";
            case INDUSTRIAL_ENGINEERING:
                return "IE";
            case MECHANICAL_ENGINEERING:
                return "ME";
            case ENERGY_ENGINEERING:
                return "EE";
            case SPECIAL:
                return "PFE";
            default:
                return "???";
        }
    }

    /**
     * Interface for card selection events
     */
    public interface CardSelectionListener {
        void onCardSelected(Card card, int position);
        void onCardDeselected(Card card, int position);
    }
}
