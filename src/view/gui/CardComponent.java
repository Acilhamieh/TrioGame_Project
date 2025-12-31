package view.gui;

import model.Card;
import enums.Branch;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Card component with FLIP ANIMATION for revealing system.
 * Supports both face-up (visible) and face-down (hidden) states.
 *
 * @author Acil HAMIEH, Dana SLEIMAN
 * @version 3.0 - Flip animation for revealing
 */
public class CardComponent extends JPanel {
    private Card card;
    private boolean selected;
    private boolean hovered;
    private CardSelectionListener listener;
    private boolean faceUp;  // TRUE = face up (visible), FALSE = face down (hidden)
    private boolean revealed;  // TRUE = currently revealed during turn
    private int positionIndex;
    private boolean clickable;  // Can this card be clicked?

    // Colors
    private static final Color FACE_UP_BG = new Color(255, 255, 255);
    private static final Color FACE_DOWN_BG = new Color(180, 180, 180);
    private static final Color REVEALED_BG = new Color(255, 250, 205);  // Light yellow
    private static final Color SELECTED_BG = new Color(144, 238, 144);  // Light green
    private static final Color HOVER_BG = new Color(230, 230, 250);  // Lavender
    private static final Color NON_CLICKABLE_BG = new Color(200, 200, 200);  // Gray
    private static final Color BORDER_COLOR = Color.DARK_GRAY;

    /**
     * Interface for card selection listeners
     */
    public interface CardSelectionListener {
        void onCardSelected(Card card, int position);
        void onCardDeselected(Card card, int position);
    }

    /**
     * Constructor
     * @param card The card
     * @param listener Selection listener
     * @param faceUp Is card face up?
     * @param positionIndex Position in hand/hall
     */
    public CardComponent(Card card, CardSelectionListener listener, boolean faceUp, int positionIndex) {
        this.card = card;
        this.listener = listener;
        this.selected = false;
        this.hovered = false;
        this.faceUp = faceUp;
        this.revealed = false;
        this.positionIndex = positionIndex;
        this.clickable = true;  // Default clickable

        setupComponent();
        setupMouseListeners();
    }

    /**
     * Setup component properties
     */
    private void setupComponent() {
        setPreferredSize(new Dimension(90, 110));
        updateBackground();
        setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 2));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        createContent();
    }

    /**
     * Create card content based on face up/down
     */
    private void createContent() {
        removeAll();

        if (faceUp) {
            createFaceUpContent();
        } else {
            createFaceDownContent();
        }

        revalidate();
        repaint();
    }

    /**
     * Create face-up card content
     */
    private void createFaceUpContent() {
        add(Box.createVerticalGlue());

        // ID badge
        JLabel idLabel = new JLabel("ID:" + card.getId());
        idLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        idLabel.setForeground(new Color(100, 100, 100));
        idLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(idLabel);

        add(Box.createRigidArea(new Dimension(0, 8)));

        // Course code (BIG)
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

        add(Box.createVerticalGlue());

        // Special styling for PFE
        if (card.isPFE()) {
            codeLabel.setForeground(new Color(255, 215, 0)); // Gold
            branchLabel.setForeground(new Color(218, 165, 32));
        }
    }

    /**
     * Create face-down card content [?]
     */
    private void createFaceDownContent() {
        add(Box.createVerticalGlue());

        // Big question mark
        JLabel questionLabel = new JLabel("?");
        questionLabel.setFont(new Font("SansSerif", Font.BOLD, 48));
        questionLabel.setForeground(new Color(120, 120, 120));
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(questionLabel);

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
     * Setup mouse listeners
     */
    private void setupMouseListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (clickable) {
                    toggleSelection();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (clickable && !selected && !revealed) {
                    hovered = true;
                    updateBackground();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                updateBackground();
            }
        });
    }

    /**
     * Toggle selection
     */
    private void toggleSelection() {
        selected = !selected;
        updateBackground();

        if (listener != null) {
            if (selected) {
                listener.onCardSelected(card, positionIndex);
            } else {
                listener.onCardDeselected(card, positionIndex);
            }
        }
    }

    /**
     * Update background color based on state
     */
    private void updateBackground() {
        Color bg;

        if (!clickable) {
            bg = NON_CLICKABLE_BG;
        } else if (revealed) {
            bg = REVEALED_BG;  // Yellow when revealed
        } else if (selected) {
            bg = SELECTED_BG;  // Green when selected
        } else if (hovered) {
            bg = HOVER_BG;  // Lavender on hover
        } else if (faceUp) {
            bg = FACE_UP_BG;  // White when face up
        } else {
            bg = FACE_DOWN_BG;  // Gray when face down
        }

        setBackground(bg);

        // Update border
        if (selected || revealed) {
            setBorder(BorderFactory.createLineBorder(selected ? Color.GREEN : Color.ORANGE, 3));
        } else {
            setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 2));
        }

        repaint();
    }

    /**
     * Flip card face up (reveal)
     */
    public void flipFaceUp() {
        if (!faceUp) {
            faceUp = true;
            createContent();
            updateBackground();
        }
    }

    /**
     * Flip card face down (hide)
     */
    public void flipFaceDown() {
        if (faceUp) {
            faceUp = false;
            createContent();
            updateBackground();
        }
    }

    /**
     * Set revealed state (highlighted during turn)
     */
    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
        if (revealed) {
            flipFaceUp();  // Auto flip face up when revealed
        }
        updateBackground();
    }

    /**
     * Set clickable state
     */
    public void setClickable(boolean clickable) {
        this.clickable = clickable;
        setCursor(clickable ?
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) :
                Cursor.getDefaultCursor());
        updateBackground();
    }

    // Standard getters/setters
    public void setSelected(boolean selected) {
        this.selected = selected;
        updateBackground();
    }

    public boolean isSelected() {
        return selected;
    }

    public Card getCard() {
        return card;
    }

    public boolean isFaceUp() {
        return faceUp;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public int getPositionIndex() {
        return positionIndex;
    }

    public boolean isClickable() {
        return clickable;
    }
}
