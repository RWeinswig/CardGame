import java.util.Scanner;
public class Game {


        private static Scanner sc;
        private static Deck deck;
        private static Player player;
        private static Player computer;


        public Game() {
            sc = new Scanner(System.in);
            printInstructions();

            System.out.print("Enter your name: ");
            String name = sc.nextLine();

            String[] ranks = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};
            String[] suits = {"Hearts","Clubs","Diamonds","Spades"};
            int[] values = {1,2,3,4,5,6,7,8,9,10,10,10,10};

            deck = new Deck(ranks, suits, values);
            player = new Player(name);
            computer = new Player("Computer");
        }

    public void printInstructions() {
        System.out.println("=== TRIAD TACTICS ===");
        System.out.println("A 3-card battle game. Each round:");
        System.out.println("• You and computer draw 3 cards.");
        System.out.println("• You have 3 energy to spend:");
        System.out.println("  1. Power Up (+2 value)");
        System.out.println("  2. Change Suit");
        System.out.println("  3. Pass (+1 bonus point)");
        System.out.println("• Hands are scored. Higher score wins the round.");
        System.out.println("• After 5 rounds, highest total wins.\n");
    }

    public  void playGame() {
        for (int round = 1; round <= 5; round++) {
            System.out.println("\n              ROUND: " + round);

            dealHands();
            printHands();

            int bonus = playerTurn();

            computerTurn();
            scoreRound(bonus);
        }
    }

    private void printHands() {
        System.out.println("Your hand: " + player.getHand());
    }

        private void dealHands() {
            player.clearHand();
            computer.clearHand();

            deck.shuffle();

            for (int i = 0; i < 3; i++) {
                player.addCard(deck.deal());
                computer.addCard(deck.deal());
            }
        }

        private int playerTurn() {
            int energy = 3;
            int bonus = 0;

            while (energy > 3) {
                System.out.println("Energy: " + energy);
                System.out.println("Make your selection. 1) Power Up 2) Change Suit 3) Pass");
            }
    }

    public void main(String[] args) {
        playGame();
    }


}
