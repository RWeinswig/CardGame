// Card Game by Ryan Weinswig
// February 25

import java.util.ArrayList;
import java.util.Scanner;
public class Game {

    // Constants
    private static final int ROUNDS = 5;
    private static final int CARDS_PER_HAND = 3;
    private static final int STARTING_ENERGY = 3;
    private static final int MAX_CARD_VALUE = 13;
    private static final int SCORE_FOR_PAIR = 3;
    private static final String[] SUITS = {"Spades", "Hearts", "Diamonds", "Clubs"};
    private static final String[] RANKS = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
    private static final int[] VALUES = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};

    // Instance variables
    private boolean revealComputerCards = false;
    private boolean gameOver = false;

    // Stores who won the round
    private String roundWinner = "";

    // Storing energy points remaining for both player and computer
    private int playerEnergy;
    private int computerEnergy;

    private Scanner sc;
    // Gives access to the deck
    private Deck deck;
    // Gives access to the player
    private Player player;
    // Gives access to the computer
    private Player computer;
    // Gives backend access to frontend
    private GameViewer window;


    // Initializes the game, creates players, deck, scanner, and GUI
    public Game() {
        window = new GameViewer(this);
        sc = new Scanner(System.in);

        System.out.print("Enter your name: ");
        String name = sc.nextLine();

        window.hideInstructions();

        // Create all of the arrays that store ranks of cards, and suits, as well as values


        // Deck made up of the
        deck = new Deck(RANKS, SUITS, VALUES, window);

        player = new Player(name);
        computer = new Player("Computer");

    }

    // Getters and setters
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

    public int getPlayerEnergy() {
        return playerEnergy;
    }

    public int getComputerEnergy() {
        return computerEnergy;
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

            // Start the round and tell what round it is
            startRound(round);
            // Player bonus equals the amount of passes the user did during their turn
            int playerBonus = playerTurn();

            // Show the computer's cards once the user's turn is over
            revealComputerCards = true;
            window.repaint();
            // Wait for user to hit enter before going to the next step
            waitForEnter();

            // Stores the amount of passes the user did during their turn for their bonus
            int computerBonus = computerTurn();

            // Score the round
            scoreRound(playerBonus, computerBonus);
            // Repaint to show who won the round
            window.repaint();

            // Print out what the golden box means
            System.out.println("\nWinner is highlighted for this round! Press Enter to see the scoreboard: ");
            // Wait for user to hit enter
            waitForEnter();
            // Hide all of the hands
            hideHands();

            // Draw the scoreboard and give instructions for user to hit enter before going to next turn
            window.drawScoreboard(window.getGraphics(), player.getPoints(), computer.getPoints(), round);
            System.out.println("\nHere is the scoreboard! Press enter to continue to the next round: ");
            waitForEnter();

        }
        // Once the rounds are over, end the game
        endGame();

    }

    // This starts the round
    private void startRound(int round) {
        // Player and computer start with three energy points
        playerEnergy = STARTING_ENERGY;
        computerEnergy = STARTING_ENERGY;

        // Deal the hands to both players
        dealHands();
        // Draw the hands and the board
        window.repaint();
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



    // This cleans up code to make it less repetitive
    // Once the user hits enter, move to the next line
    private void waitForEnter() {
        System.out.println("Press Enter to continue...");
        sc.nextLine();
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
            // Store the user's choice
            String choice = sc.nextLine();

            // If they chose to power up
            if (choice.equals("1")) {
                playerPowerUp();
            }
            // If they chose to change suit
            else if (choice.equals("2")) {
                playerSuitChange();
            }
            // If player chose to pass
            else if (choice.equals("3")) {
                // Give them a bonus point and take away an energy point
                bonus++;
                playerEnergy--;
            }

            // Reprompt the user to insert new number
            else {
                System.out.println("Invalid Choice. Please pick 1, 2, or 3.");
            }
            // Repaint once they made their selection
            window.repaint();

        }
        // Return the bonus once their turn is over
        return bonus;
    }

    // Method used when player chooses to power up
    private void playerPowerUp() {
        // Prompt the user to choose what card index they wish to power up
        System.out.println("Choose card index (1-3)");
        // Store choice
        int choice = sc.nextInt() - 1;
        sc.nextLine();

        // If the user chose a correct index
        if (isValidIndex(choice)) {
            // Get the card index they want to power up
            Card card = player.getCard(choice);

            // This checks the most they can add to their score
            int maxAdd = MAX_CARD_VALUE - card.getValue();
            // If the user can actually add to the card because it isn't a king
            if (maxAdd > 0) {
                // Add a random number between 1 and the max they can add
                int add = (int) (Math.random() * maxAdd) + 1;
                // Power up the card by adding this value to it
                card.powerUp(add);
            }
            else{
                System.out.println("You can't power up a king!!! You just wasted a turn. ");
            }
            // Take away a user's energy points
            // If they tried to power up a king, they still use up an energy point (be smart with your decisions)
            playerEnergy--;
        }
        // Reprompt user if they chose an incorrect index
        else{
            System.out.println("Please choose a valid index");
        }
    }


    private void playerSuitChange() {
        // Tell user to choose which suit they want to change and store their choice
        System.out.println("Choose card index (1-3)");
        int choice = sc.nextInt() - 1;
        sc.nextLine();

        // Check if they chose a valid index
        if (isValidIndex(choice)) {
            // Get the card index they want to power up
            Card card = player.getCard(choice);
            // Set the suit to a random suit, suit might not change
            card.setSuit(SUITS[(int) (Math.random() * SUITS.length)]);
            playerEnergy--;
            }
        }

        // This checks if they chose an index that is possible
        // Note; we already subtracted one from their index
    private boolean isValidIndex(int index) {
        return index >= 0 && index < CARDS_PER_HAND;
    }

    // Method for the computer's turn
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
                }
                // If the user can improve their three of a kind
                else if (tryImproveThreeOfAKind()) {
                    // They lose an energy and make the turn
                    computerEnergy--;
                }
                // If the user can get a flush and are just one card away
                else if (tryCompleteFlush()) {
                    // They lose an energy and make the turn
                    computerEnergy--;
                }
                // Otherwise the computer just makes a random move
                else {
                    randomComputerMove();
                    computerEnergy--;
                }

                // Repaint the window once their turn is over
                // Wait for the user to hit enter to make their next move
                window.repaint();
                waitForEnter();
            }
            // Once their turn is over, return the amount of bonus points they accumulated
                return bonus;
    }

    private boolean shouldPass() {
        // If the computer's hand is already better than a pair
        return HandRanking.evaluate(computer.getHand()) > SCORE_FOR_PAIR;
    }

    private boolean tryImproveThreeOfAKind() {
        // If the user does not have a pair, don't make this move
        if (HandRanking.evaluate(computer.getHand()) != SCORE_FOR_PAIR) {
            return false;
        }

        // Otherwise, get each card in the hand
        Card c1 = computer.getCard(0);
        Card c2 = computer.getCard(1);
        Card c3 = computer.getCard(2);

        // Check to see which card is the odd one out
        // Checks the first one
        if (c1.getValue() != c2.getValue() && c1.getValue() != c3.getValue()) {
            // Powers up the first card and hopes for the best
            // Returns true because they tried to improve their pair
            powerUp(c1);
            return true;
        }
        // Checks the second
        else if (c2.getValue() != c1.getValue() && c2.getValue() != c3.getValue()) {
            // Powers up the first card and hopes for the best
            // Returns true because they tried to improve their pair
            powerUp(c2);
            return true;
        }
        // Checks the third
        else if (c3.getValue() != c1.getValue() && c3.getValue() != c2.getValue()) {
            // Powers up the first card and hopes for the best
            // Returns true because they tried to improve their pair
            powerUp(c3);
            return true;
        }
        // If computer glitched and said they had a pair, but they didn't then jut return false at the end
        return false;
    }

    // Makes the mood if the computer is one away from a flush
    private boolean tryCompleteFlush() {
        // Check if they need to adjust one card for a flush
        if (!oneAwayFromFlush(computer.getHand())) {
            // If not, return false
            return false;
        }

        // Store each card
        Card c1 = computer.getCard(0);
        Card c2 = computer.getCard(1);
        Card c3 = computer.getCard(2);

        // Check to see what is the odd card out
        // If it is the first one
        if (!c1.getSuit().equals(c2.getSuit()) && !c1.getSuit().equals(c3.getSuit())){
            // Set a random suit (note the suit might not change)
            // Computer made move
            c1.setSuit(randomSuit());
            return true;
        }

        // If it is the second one
        else if (!c2.getSuit().equals(c1.getSuit()) && !c2.getSuit().equals(c3.getSuit())){
            // Set a random suit (note the suit might not change)
            // Computer made move
            c2.setSuit(randomSuit());
            return true;
        }

        // If it is the third one
        else if (!c3.getSuit().equals(c1.getSuit()) && !c3.getSuit().equals(c2.getSuit())){
            // Set a random suit (note the suit might not change)
            // Computer made move
            c3.setSuit(randomSuit());
            return true;
        }
        // If computer made mistake when evaluating (in case of a glitch)
        // Return false
        return false;
    }

    // Make a random computer move
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
