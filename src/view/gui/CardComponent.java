package view.gui;

import model.Card;
import enums.Branch;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Visual component representing a single card.
 * Clickable, selectable, with hover effects.
 *
 * @author Acil HAMIEH
 * @version 1.0
 */
public class CardComponent extends JPanel {
    private Card card;
    private boolean selected;
    private boolean hovered;
    private CardSelectionListener listener;

    // Colors
    private static final Color NORMAL_BG = Color.WHITE;
    private static final Color SELECTED_BG = new Color(144, 238, 144); // Light Green
    private static final Color HOVER_BG = new Color(230, 230, 250); // Lavender
    private static final Color BORDER_COLOR = new Color(100, 100, 100);
    private static final Color SELECTED_BORDER = new Color(34, 139, 34); // Forest Green

    /**
     * Constructor for CardComponent
     * @param card The card to display
     * @param listener Selection listener
     */
    public CardComponent(Card card, CardSelectionListener listener) {
        this.card = card;
        this.listener = listener;
        this.selected = false;
        this.hovered = false;

        setupComponent();
        setupMouseListeners();
    }

    /**
     * Setup component properties
     */
    private void setupComponent() {
        setPreferredSize(new Dimension(80, 110));
        setBackground(NORMAL_BG);
        setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 2));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setLayout(new BorderLayout(5, 5));

        // Add content
        createContent();
    }

    /**
     * Create card content
     */
    private void createContent() {
        // Top: Branch icon
        JLabel iconLabel = new JLabel(getBranchIcon(card.getBranch()), SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        add(iconLabel, BorderLayout.NORTH);

        // Center: Course code
        JLabel codeLabel = new JLabel(card.getCourseCode(), SwingConstants.CENTER);
        codeLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
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
                listener.onCardSelected(card);
            } else {
                listener.onCardDeselected(card);
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
            setBackground(NORMAL_BG);
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
        void onCardSelected(Card card);
        void onCardDeselected(Card card);
    }
}
