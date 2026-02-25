// Card Game by Ryan Weinswig
// February 25

import java.util.ArrayList;
import java.util.Scanner;
public class Game {

    private static final int ROUNDS = 5;
    private static final int CARDS_PER_HAND = 3;
    private static final int STARTING_ENERGY = 3;
    private static final int MAX_CARD_VALUE = 13;
    private static final String[] SUITS = {"Spades", "Hearts", "Diamonds", "Clubs"};

    // Instance variables
    private boolean revealComputerCards = false;
    private boolean gameOver = false;

    private String roundWinner = "";

    private int playerEnergy;
    private int computerEnergy;

    private Scanner sc;
    private Deck deck;
    private Player player;
    private Player computer;
    private GameViewer window;


    // Initializes the game, creates players, deck, scanner, and GUI
    public Game() {
        window = new GameViewer(this);
        sc = new Scanner(System.in);

        System.out.print("Enter your name: ");
        String name = sc.nextLine();

        window.hideInstructions();

        // Create all of the arrays that store ranks of cards, and suits, as well as values
        String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

        int[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};

        deck = new Deck(ranks, SUITS, values, window);

        player = new Player(name);
        computer = new Player("Computer");

    }

    public boolean isRevealComputerCards() {
        return revealComputerCards;
    }

    public String getRoundWinner() {
        return roundWinner;
    }

    public void setRevealComputerCards(boolean reveal) {
        this.revealComputerCards = reveal;
    }

    public Player getComputer() {
        return computer;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    // Clears the hands of both the player and the computer
    // Then repaints, and neither player will have cards so nothing shows up
    private void hideHands() {
        player.clearHand();
        computer.clearHand();
        roundWinner = "";
        window.repaint();
    }

    public void playGame() {
        // Only play five rounds
        for (int round = 1; round <= ROUNDS; round++) {

            startRound(round);
            int playerBonus = playerTurn();

            revealComputerCards = true;
            window.repaint();
            waitForEnter();

            int computerBonus = computerTurn();
            scoreRound(playerBonus, computerBonus);
            window.repaint();
            System.out.println("\nWinner is highlighted for this round! Press Enter to see the scoreboard: ");
            waitForEnter();
            hideHands();
            window.drawScoreboard(window.getGraphics(), player.getPoints(), computer.getPoints(), round);
            System.out.println("\nHere is the scoreboard! Press enter to continue to the next round: ");
            waitForEnter();

        }
        endGame();

    }

    private void startRound(int round) {
        playerEnergy = STARTING_ENERGY;
        computerEnergy = STARTING_ENERGY;

        dealHands();
        window.repaint();
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
        // Show the cards
        setRevealComputerCards(false);

        // Adds three cards to both computer and player
        for (int i = 0; i < CARDS_PER_HAND; i++) {

            player.addCard(deck.deal());
            computer.addCard(deck.deal());
        }
    }



    private void waitForEnter() {
        System.out.println("Press Enter to continue...");
        sc.nextLine();
    }


    public int getPlayerEnergy() {
        return playerEnergy;
    }

    public int getComputerEnergy() {
        return computerEnergy;
    }

    // This is the method for the player's turn
    private int playerTurn() {
        // User can do three things per turn

        // User has 0 bonus points at beginning of round
        int bonus = 0;

        // Run their turn while they have some energy points remaining
        while (playerEnergy > 0) {
            // Print out the user's energy points remaining for the round
            System.out.println("Energy: " + playerEnergy);
            // User makes their selection of what they want to do with their energy point
            System.out.println("Make your selection. 1) Power Up 2) Change Suit 3) Pass");

            String choice = sc.nextLine();

            if (choice.equals("1")) {
                playerPowerUp();
            } else if (choice.equals("2")) {
                playerSuitChange();
            } else if (choice.equals("3")) {
                bonus++;
                playerEnergy--;
            }

            // Reprompt the user to insert new number
            else {
                System.out.println("Invalid Choice. Please pick 1, 2, or 3.");
            }
            window.repaint();

        }
        return bonus;
    }

    private void playerPowerUp() {
        System.out.println("Choose card index (1-3)");
        int choice = sc.nextInt() - 1;
        sc.nextLine();

        if (isValidIndex(choice)) {
            // Get the card index they want to power up
            Card card = player.getCard(choice);

            int maxAdd = MAX_CARD_VALUE - card.getValue();
            if (maxAdd > 0) {
                int add = (int) (Math.random() * maxAdd) + 1;
                card.powerUp(add);
            }
            playerEnergy--;


        }
    }

        private void playerSuitChange() {
            System.out.println("Choose card index (1-3");
            int choice = sc.nextInt() - 1;
            sc.nextLine();

            if (isValidIndex(choice)) {
                // Get the card index they want to power up
                Card card = player.getCard(choice);
                // Set the suit to a random suit, suit might not change
                card.setSuit(SUITS[(int) (Math.random() * SUITS.length)]);
                playerEnergy--;
            }
        }

        private boolean isValidIndex(int index) {
            return index >= 0 && index < CARDS_PER_HAND;
        }

    private int computerTurn() {
            // Computer starts at 3 energy points, and zero bonus points

            int bonus = 0;


        // While the computer still has energy points
            while (computerEnergy > 0) {
                // If the computer currently has a score above 3, the computer will pass
                if (shouldPass()) {
                    // Gets one bonus point, loses one energy one, and prints out hand
                    System.out.println("Computer passes. ");
                    bonus++;
                    computerEnergy--;
                    printComputerHands();
                } else if (tryImproveThreeOfAKind()) {
                    computerEnergy--;
                } else if (tryCompleteFlush()) {
                    computerEnergy--;
                } else {
                    randomComputerMove();
                    computerEnergy--;
                }

                window.repaint();
                waitForEnter();
            }
                return bonus;
    }

    private boolean shouldPass() {
        return HandRanking.evaluate(computer.getHand()) > 3;
    }

    private boolean tryImproveThreeOfAKind() {
        if (HandRanking.evaluate(computer.getHand()) != 3) {
            return false;
        }

        Card c1 = computer.getCard(0);
        Card c2 = computer.getCard(1);
        Card c3 = computer.getCard(2);

        if (c1.getValue() != c2.getValue() && c1.getValue() != c3.getValue()) {
            powerUp(c1);
            return true;
        }
        else if (c2.getValue() != c1.getValue() && c2.getValue() != c3.getValue()) {
            powerUp(c2);
            return true;
        }
        else if (c3.getValue() != c1.getValue() && c3.getValue() != c2.getValue()) {
            powerUp(c3);
            return true;
        }

        return false;
    }

    private boolean tryCompleteFlush() {
        if (!oneAwayFromFlush(computer.getHand())) {
            return false;
        }

        Card c1 = computer.getCard(0);
        Card c2 = computer.getCard(1);
        Card c3 = computer.getCard(2);

        if (!c1.getSuit().equals(c2.getSuit()) && !c1.getSuit().equals(c3.getSuit())){
            c1.setSuit(randomSuit());
            return true;
        }

        else if (!c2.getSuit().equals(c1.getSuit()) && !c2.getSuit().equals(c3.getSuit())){
            c2.setSuit(randomSuit());
            return true;
        }

        else if (!c3.getSuit().equals(c1.getSuit()) && !c3.getSuit().equals(c2.getSuit())){
            c3.setSuit(randomSuit());
            return true;
        }
        return false;
    }

    private void randomComputerMove() {
        int choice = (int) (Math.random() * 3) + 1;
        int index = (int) (Math.random() * CARDS_PER_HAND);

        Card card = computer.getCard(index);

        if (choice == 1) {
            powerUp(card);
        } else if (choice == 2) {
            card.setSuit(randomSuit());
        }

    }

    private void powerUp(Card card) {
        int maxAdd = MAX_CARD_VALUE - card.getValue();
        if (maxAdd > 0) {
            card.powerUp((int)(Math.random() * maxAdd) + 1);
        }
    }

    private String randomSuit() {
        return SUITS[(int)(Math.random() * SUITS.length)];
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
            roundWinner = "";
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
                    System.out.println(player.getName() + " has the high card! You get a point!");
                    roundWinner = "PLAYER";
                    playerScore ++;
                }
                // Otherwise if computer had higher card add one to their score
                else if (Math.max(p1, Math.max(p2, p3)) < Math.max(c1, Math.max(c2, c3))) {
                    System.out.println("The computer  has the high card! It gets a point!");
                    roundWinner = "COMPUTER";
                    computerScore ++;
                }
                // Otherwise nobody gets a point
                else{
                    System.out.println("It's a tie! Nobody gets a point!");
                }
                // If computer wins round, they get bonus point
            } else if (playerScore < computerScore) {
                roundWinner = "COMPUTER";
                System.out.println("The computer wins this round. They get a point!");
                computerScore ++;
            }
            // Otherwise player wins round and gets bonus point
            else{
                roundWinner = "PLAYER";
                System.out.println(player.getName() + " wins this round. You get a point!");
                playerScore ++;
            }

            // Add points of round to each player's score
            player.addPoints(playerScore);
            computer.addPoints(computerScore);

    }

    private void endGame() {
        gameOver = true;
        window.repaint();
    }

    public Player getPlayer() {
        return player;
    }

    // Create and play the game
    public static void main(String[] args) {
        Game game = new Game();
        game.playGame();

    }




}
