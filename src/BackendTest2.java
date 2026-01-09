import model.*;
import enums.Branch;


public class BackendTest2 {

    public static void main(String[] args) {

        RevealState state = new RevealState();

        Card c1 = new Card("AP4B", Branch.COMPUTER_SCIENCE);
        Card c2 = new Card("AP4B", Branch.COMPUTER_SCIENCE);
        Card c3 = new Card("MQ41", Branch.MECHANICAL_ENGINEERING);


        System.out.println("Add first card");
        state.addReveal(c1, "hand", null, 0);
        System.out.println("Reveal count = " + state.getRevealCount());

        System.out.println("Add second SAME card");
        state.addReveal(c2, "hand", null, 1);
        System.out.println("Reveal count = " + state.getRevealCount());
        System.out.println("Mismatch? " + state.hasMismatch());

        System.out.println("Add third DIFFERENT card");
        state.addReveal(c3, "hand", null, 2);
        System.out.println("Reveal count = " + state.getRevealCount());
        System.out.println("Mismatch? " + state.hasMismatch());
    }
}
