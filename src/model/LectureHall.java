package model;

import java.util.ArrayList;
import java.util.List;


public class LectureHall {
    private List<Card> visibleCards;
    private static final int MAX_CAPACITY = 9;

    //Constructor for Lecture Hall

    public LectureHall() {
        this.visibleCards = new ArrayList<>();
    }

    //Add a card to the lecture hall

    public boolean addCard(Card card) {
        if (card != null && visibleCards.size() < MAX_CAPACITY) {
            visibleCards.add(card);
            return true;
        }
        return false;
    }

    //Remove a card from the lecture hall
    public boolean removeCard(Card card) {
        return visibleCards.remove(card);
    }

    //Remove a card at a specific index

    public Card removeCard(int index) {
        if (index >= 0 && index < visibleCards.size()) {
            return visibleCards.remove(index);
        }
        return null;
    }

    //Get a card at a specific index without removing it

    public Card getCard(int index) {
        if (index >= 0 && index < visibleCards.size()) {
            return visibleCards.get(index);
        }
        return null;
    }

    //Check if lecture hall contains a specific card

    public boolean contains(Card card) {
        return visibleCards.contains(card);
    }

    //Get all visible cards (defensive copy)

    public List<Card> getAllCards() {
        return new ArrayList<>(visibleCards);
    }

    //Get the number of cards currently visible

    public int getCardCount() {
        return visibleCards.size();
    }

    //Check if the lecture hall is full

    public boolean isFull() {
        return visibleCards.size() >= MAX_CAPACITY;
    }

    //Check if the lecture hall is empty

    public boolean isEmpty() {
        return visibleCards.isEmpty();
    }

    //Get the maximum capacity

    public int getMaxCapacity() {
        return MAX_CAPACITY;
    }

    //Clear all cards from lecture hall

    public void clear() {
        visibleCards.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Lecture Hall (").append(visibleCards.size()).append("/").append(MAX_CAPACITY).append("):\n");
        for (int i = 0; i < visibleCards.size(); i++) {
            sb.append("[").append(i).append("] ").append(visibleCards.get(i).getCourseCode());
            if ((i + 1) % 3 == 0) {
                sb.append("\n");
            } else {
                sb.append("  ");
            }
        }
        return sb.toString();
    }
}
