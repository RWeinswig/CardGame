import java.util.ArrayList;
import java.util.Scanner;
public class Game {


        private static Scanner sc;
        private static Deck deck;
        private static Player player;
        private static Player computer;

        private static final String[] SUITS = {"Hearts","Clubs","Diamonds","Spades"};

        // Create and play the game
    public static void main(String[] args) {
        Game game = new Game();
        game.playGame();
    }


        public Game() {
            // Create the scanner
            sc = new Scanner(System.in);
            // Print out the manual to how to play the game
            printInstructions();

            // Have user input name
            System.out.print("Enter your name: ");
            String name = sc.nextLine();

            // Create all of the arrays that store ranks of cards, and suits, as well as values
            String[] ranks = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};
            String[] suits = {"Hearts","Clubs","Diamonds","Spades"};
            int[] values = {1,2,3,4,5,6,7,8,9,10,11,12,13};

            // Create the deck with all of the ranks, suits, and values, and create computer player and user
            deck = new Deck(ranks, suits, values);
            player = new Player(name);
            computer = new Player("Computer");
        }

    public void printInstructions() {
            // Print out the instructions
        System.out.println("=== TRIAD TACTICS ===");
        System.out.println("A 3-card battle game. Each round:");
        System.out.println("• You and computer draw 3 cards.");
        System.out.println("• You have 3 energy to spend:");
        System.out.println("  1. Power Up (+ random value to any card of your choice, you cannot subtract from a cards value)");
        System.out.println("  2. Change Suit");
        System.out.println("  3. Pass (+1 bonus point)");
        System.out.println("• Hands are scored. Higher score wins the round.");
        System.out.println("• After 5 rounds, highest total wins.\n");
    }

    public void playGame() {
            // Only play five rounds
        for (int round = 1; round <= 5; round++) {
            System.out.println("\n              ROUND: " + round);
            // Print out the standings including user's and computer's points
            System.out.println("The current standings .............");
            System.out.println(player.name + ": " + player.points);
            System.out.println("Computer: " + computer.points);
            // Deal the hands, and print them
            dealHands();
            printHands();
            System.out.println("!!!!!!");
            // This is the score of the number of times the user passed
            // Call the player's turn
            int bonus = playerTurn();

            // Call the computer's turn and score the bonus
            int bonus2 = computerTurn();
            // Call the function which scores the round to see who won and to get the scores
            scoreRound(bonus, bonus2);
        }

        System.out.println(" ");
        System.out.println(" ");

        // Checks to see who won, and prints out message depending on who won
        if (player.points > computer.points) {
            System.out.println(player.name + " won!!!!!! Congratulations!!!");
        }

        else if (player.points < computer.points) {
            System.out.println("The computer won. Try again later. ");
        }

        else{
            System.out.println("BOOOOO. It was a tie.");
        }

        // Prints out final standings and final points
        System.out.println("The final standings .............");
        System.out.println(player.name + ": " + player.points);
        System.out.println("Computer: " + computer.points);

    }

    // Prints out users hand
    private void printHands() {
        System.out.println("Your hand: " + player.getHand());
    }

    // Prints out computer's hand
    private void printComputerHands() {
        System.out.println("Computer's hand: " + computer.getHand());
    }

    // This method deals the hands to both the computer and user
        private void dealHands() {
            // Resets their hand so they are empty
            player.clearHand();
            computer.clearHand();

            // Shuffles the deck
            deck.shuffle();

            // Adds three cards to both computer and player
            for (int i = 0; i < 3; i++) {
                player.addCard(deck.deal());
                computer.addCard(deck.deal());
            }
        }

        // This is the method for the player's turn
        private int playerTurn() {
            // User can do three things per turn
            int energy = 3;
            // User has 0 bonus points at beginning of round
            int bonus = 0;

            // Run their turn while they have some energy points remaining
            while (energy > 0) {
                // Print out the user's energy points remaining for the round
                System.out.println("Energy: " + energy);
                // User makes their selection of what they want to do with their energy point
                System.out.println("Make your selection. 1) Power Up 2) Change Suit 3) Pass");
                String choice = sc.nextLine();

                // If the user wanted to power up
                if (choice.equals("1")) {
                    // Get the card index they want to power up
                    System.out.println("Which card index (1-3) do you want to power up: ");
                    int index = sc.nextInt() - 1;
                    sc.nextLine();
                    // Get the card the player wanted to power up
                    Card card1 = player.getCard(index);

                    // 13 is the highest value a card can have
                    // We want to add a random number to it, but not go over 13
                    // That is what howMuchToAdd does, finds value to add
                    int howMuchToAdd = 13 - card1.getValue();
                    int add = (int)(Math.random() * howMuchToAdd) + 1;
                    // Power up the card
                    card1.powerUp(add);

                    // Print out new value of card, and of hand
                    // Subtract from energy now that turn is over
                    System.out.println("New Value: " + card1.getValue());
                    energy --;
                    printHands();
                }

                // If the user chose to change the suit
                else if (choice.equals("2")) {
                    // Ask the user what suit they would like to change
                    System.out.println("Which card index (1-3) do you want to change: ");
                    int index = sc.nextInt() - 1;
                    sc.nextLine();

                    // Get the card that they want to change
                    Card card = player.getCard(index);


                    // Set the suit to a random suit, suit might not change
                    card.setSuit(SUITS[(int)(Math.random() * SUITS.length)]);
                    // Print out what the suit was changed to
                    System.out.println("Suit changed to " + card.getSuit());
                    energy --;
                    // Print the hand and subtract one from energy
                    printHands();
                }

                // if they chose to pass, give the user one bonus point and take one energy point away
                else if (choice.equals("3")) {
                    System.out.println("You passed. +1 bonus point");
                    bonus ++;
                    energy --;
                    printHands();
                }

                // Reprompt the user to insert new number
                else {
                    System.out.println("Invalid Choice. Please pick 1, 2, or 3.");
                }


            }
            // Returns the amount of bonus points the user accumulated
            return bonus;
    }

    private int computerTurn() {
            // Computer starts at 3 energy points, and zero bonus points
            int energy = 3;
            int bonus = 0;
            // Print the computer's hand
            printComputerHands();

            // Get all three cards in the computer's hand
        Card c1 = computer.getCard(0);
        Card c2 = computer.getCard(1);
        Card c3 = computer.getCard(2);

        // While the computer still has energy points
            while (energy > 0) {
                // Find the score of the computer's current hand
                int handScore = HandRanking.evaluate(computer.getHand());

                // If the computer currently has a score above 3, the computer will pass
                if (handScore > 3) {
                    // Gets one bonus point, loses one energy one, and prints out hand
                    System.out.println("Computer passes. ");
                    bonus ++;
                    energy--;
                    printComputerHands();
                }

                else if(handScore == 3) {
                    // If hand score is 3, which means it has a pair

                    // Computer will try to get a 3 of a kind. This means changing value of only one not the same
                    if (c1.getValue() != c2.getValue() && c1.getValue() != c3.getValue()) {
                        c1.setSuit(SUITS[(int)(Math.random() * SUITS.length)]);
                        System.out.println("Computer changed a card to improve their pair.");
                        printComputerHands();
                    }
                    else if (c2.getValue() != c1.getValue() && c2.getValue() != c3.getValue()) {
                        c2.setSuit(SUITS[(int)(Math.random() * SUITS.length)]);
                        System.out.println("Computer changed a card to improve their pair.");
                        printComputerHands();
                    } else if (c3.getValue() != c1.getValue() && c3.getValue() != c2.getValue()) {
                        c3.setSuit(SUITS[(int)(Math.random() * SUITS.length)]);
                        System.out.println("Computer changed a card to improve their pair.");
                        printComputerHands();
                    }
                    // Compute4r loses an energy point
                    energy--;
                }

                // If the computer is one away from a flush
                else if (oneAwayFromFlush(computer.getHand())) {


                    // Check to see which card needs to be changed to get the flush
                    // Then try to change it in order to get that flush
                    if (!c1.getSuit().equals(c2.getSuit()) && !c1.getSuit().equals(c3.getSuit())) {
                        c1.setSuit(SUITS[(int)(Math.random() * SUITS.length)]);
                        System.out.println("Computer changed a card's suit to try for a flush.");
                        printComputerHands();
                    } else if (!c2.getSuit().equals(c1.getSuit()) && !c2.getSuit().equals(c3.getSuit())) {
                        c2.setSuit(SUITS[(int)(Math.random() * SUITS.length)]);
                        System.out.println("Computer changed a card's suit to try for a flush.");
                        printComputerHands();
                    } else if (!c3.getSuit().equals(c1.getSuit()) && !c3.getSuit().equals(c2.getSuit())) {
                        c3.setSuit(SUITS[(int)(Math.random() * SUITS.length)]);
                        System.out.println("Computer changed a card's suit to try for a flush.");
                        printComputerHands();
                    }

                    // Uses an energy point
                    energy--;
                }

                else {
                    // Otherwise, the computer does a random move
                    int choice = (int)(Math.random() * 3) +1;

                    // If random move is 1
                    // Powers up a card by adding to its value
                    if (choice == 1) {
                        int index = (int)(Math.random() * 3);
                        Card card = computer.getCard(index);
                        int add = (int)(Math.random() * (13 - card.getValue()));
                        card.powerUp(add);
                        System.out.println("The computer powered up a card. ");
                        // Loses one energy point and prints out hand
                        energy -- ;
                        printComputerHands();
                    }

                    // Otherwise if choice is two
                    // Change a random suit
                    else if (choice == 2) {
                        int index = (int)(Math.random() * 3);
                        Card card = computer.getCard(index);
                        card.setSuit(SUITS[(int)(Math.random() * SUITS.length)]);
                        System.out.println("The computer changed one of its suits");
                        // Subtract an energy point
                        // print out the computer hands
                        energy --;
                        printComputerHands();
                    }
                    else{
                        // Otherwise the computer passed
                        // Gets one bonus point and loses energy
                        // Print out hand
                        System.out.println("The computer passed");
                        bonus ++;
                        energy --;
                        printComputerHands();
                    }
                }

            }
            // Returns the bonus the computer has accumulated
        return bonus;
    }

    private boolean oneAwayFromFlush(ArrayList<Card> hand) {
            // Get the suit of each of the cards
            String suit1 = hand.get(0).getSuit();
            String suit2 = hand.get(1).getSuit();
            String suit3 = hand.get(2).getSuit();

            // Check if two of the suits are the same, and thus one away from a flush
            if ((suit1.equals(suit2) && !suit3.equals(suit1)) ||
                    (suit1.equals(suit3) && !suit2.equals(suit1)) ||
                    (suit2.equals(suit3) && !suit1.equals(suit2))) {
                // If yes, return true
                return true;
            } else {
                // Return false otherwise
                return false;
            }
    }

    // This is the method that scores each player's hand, and the round
    private void scoreRound(int bonus, int computerBonus) {
            // We create two variables that store the score of each user
            // Call on the handranking to evaluate hands
            int playerScore = HandRanking.evaluate(player.getHand());
            int computerScore = HandRanking.evaluate(computer.getHand());

            // We add the bonus to each of the scores
            playerScore += bonus;
            computerScore += computerBonus;

            // Print out the scores of both players
            System.out.println("\n   ROUND RESULTS   ");
            System.out.println("Your hand: " + player.getHand() + "  Score: " + playerScore);
            System.out.println("Computer hand: " + computer.getHand() + "  Score: " + computerScore);

            // If the scores are equivalent
            if (playerScore == computerScore){
                System.out.println("The round is a tie. Let's see who wins with the High Card!");
                int p1 = player.getCard(0).getValue();
                int p2 = player.getCard(1).getValue();
                int p3 = player.getCard(2).getValue();

                int c1 = computer.getCard(0).getValue();
                int c2 = computer.getCard(1).getValue();
                int c3 = computer.getCard(2).getValue();

                // Get all of the values and see who had the max score
                // if the player had the highest card, add one to their score
                if (Math.max(p1, Math.max(p2, p3)) > Math.max(c1, Math.max(c2, c3))) {
                    System.out.println(player.name + " has the high card! You get a point!");
                    playerScore ++;
                }
                // Otherwise if computer had higher card add one to their score
                else if (Math.max(p1, Math.max(p2, p3)) < Math.max(c1, Math.max(c2, c3))) {
                    System.out.println("The computer  has the high card! It gets a point!");
                    computerScore ++;
                }
                // Otherwise nobody gets a point
                else{
                    System.out.println("It's a tie! Nobody gets a point!");
                }
                // If computer wins round, they get bonus point
            } else if (playerScore < computerScore) {
                System.out.println("The computer wins this round. They get a point!");
                computerScore ++;
            }
            // Otherwise player wins round and gets bonus point
            else{
                System.out.println(player.name + " wins this round. You get a point!");
                playerScore ++;
            }

            // Add points of round to each player's score
            player.addPoints(playerScore);
            computer.addPoints(computerScore);

    }




}
