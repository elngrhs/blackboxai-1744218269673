import java.util.*;
import java.util.stream.Collectors;

class Card {
    String suit;
    String rank;
    int value;

    Card(String suit, String rank, int value) {
        this.suit = suit;
        this.rank = rank;
        this.value = value;
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}

class Deck {
    List<Card> cards;

    Deck() {
        cards = new ArrayList<>();
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};
        
        for (int i = 0; i < ranks.length; i++) {
            for (String suit : suits) {
                cards.add(new Card(suit, ranks[i], i + 2));
            }
        }
        Collections.shuffle(cards);
    }

    Card drawCard() {
        return cards.remove(cards.size() - 1);
    }
}

class HandEvaluator {
    public static int evaluateHand(List<Card> hand) {
        if (isRoyalFlush(hand)) return 10;
        if (isStraightFlush(hand)) return 9;
        if (isFourOfAKind(hand)) return 8;
        if (isFullHouse(hand)) return 7;
        if (isFlush(hand)) return 6;
        if (isStraight(hand)) return 5;
        if (isThreeOfAKind(hand)) return 4;
        if (isTwoPair(hand)) return 3;
        if (isPair(hand)) return 2;
        return 1; // High card
    }

    private static boolean isRoyalFlush(List<Card> hand) {
        return isStraightFlush(hand) && hand.stream().mapToInt(c -> c.value).sum() == 60;
    }

    private static boolean isStraightFlush(List<Card> hand) {
        return isFlush(hand) && isStraight(hand);
    }

    private static boolean isFourOfAKind(List<Card> hand) {
        return getRankCounts(hand).containsValue(4);
    }

    private static boolean isFullHouse(List<Card> hand) {
        Map<Integer, Integer> counts = getRankCounts(hand);
        return counts.containsValue(3) && counts.containsValue(2);
    }

    private static boolean isFlush(List<Card> hand) {
        return hand.stream().map(c -> c.suit).distinct().count() == 1;
    }

    private static boolean isStraight(List<Card> hand) {
        List<Integer> values = hand.stream().map(c -> c.value).sorted().collect(Collectors.toList());
        for (int i = 1; i < values.size(); i++) {
            if (values.get(i) - values.get(i - 1) != 1) {
                return false;
            }
        }
        return true;
    }

    private static boolean isThreeOfAKind(List<Card> hand) {
        return getRankCounts(hand).containsValue(3);
    }

    private static boolean isTwoPair(List<Card> hand) {
        return getRankCounts(hand).values().stream().filter(v -> v == 2).count() == 2;
    }

    private static boolean isPair(List<Card> hand) {
        return getRankCounts(hand).containsValue(2);
    }

    private static Map<Integer, Integer> getRankCounts(List<Card> hand) {
        Map<Integer, Integer> counts = new HashMap<>();
        for (Card card : hand) {
            counts.put(card.value, counts.getOrDefault(card.value, 0) + 1);
        }
        return counts;
    }
}

class Player {
    String name;
    List<Card> hand;
    int chips;
    boolean folded;
    int currentBet;

    Player(String name, int chips) {
        this.name = name;
        this.chips = chips;
        this.hand = new ArrayList<>();
        this.folded = false;
        this.currentBet = 0;
    }

    void drawHand(Deck deck) {
        hand.clear();
        for (int i = 0; i < 5; i++) {
            hand.add(deck.drawCard());
        }
    }

    void replaceCards(List<Integer> indices, Deck deck) {
        for (int index : indices) {
            if (index > 0 && index <= hand.size()) {
                hand.set(index - 1, deck.drawCard());
            }
        }
    }

    void showHand() {
        System.out.println("\n" + name + "'s hand:");
        for (int i = 0; i < hand.size(); i++) {
            System.out.println((i + 1) + ": " + hand.get(i));
        }
    }

    boolean hasEnoughChips(int amount) {
        return chips >= amount;
    }

    void bet(int amount) {
        chips -= amount;
        currentBet += amount;
    }

    void fold() {
        folded = true;
    }

    void win(int amount) {
        chips += amount;
    }

    void resetForNewRound() {
        folded = false;
        currentBet = 0;
        hand.clear();
    }

    int getHandStrength() {
        return HandEvaluator.evaluateHand(hand);
    }
}

public class PokerGame {
    private static final int STARTING_CHIPS = 100;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to Five Card Draw Poker!");

        List<Player> players = initializePlayers();
        Deck deck = new Deck();
        int pot = 0;

        while (players.size() > 1) {
            // Deal hands
            deck = new Deck();
            for (Player player : players) {
                player.drawHand(deck);
            }

            // Betting round 1
            pot += bettingRound(players);

            // Card replacement
            cardReplacementRound(players, deck);

            // Betting round 2
            pot += bettingRound(players);

            // Showdown
            determineWinner(players, pot);

            // Prepare for next round
            pot = 0;
            for (Player player : players) {
                player.resetForNewRound();
                if (player.chips <= 0) {
                    System.out.println(player.name + " is out of chips!");
                }
            }
            players.removeIf(p -> p.chips <= 0);

            if (players.size() > 1) {
                System.out.print("\nPlay another round? (y/n): ");
                if (!scanner.next().equalsIgnoreCase("y")) {
                    break;
                }
            }
        }

        System.out.println("\nGame over! Final results:");
        players.forEach(p -> System.out.println(p.name + ": " + p.chips + " chips"));
    }

    private static List<Player> initializePlayers() {
        List<Player> players = new ArrayList<>();
        System.out.print("Enter number of players: ");
        int numPlayers = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < numPlayers; i++) {
            System.out.print("Enter name for player " + (i + 1) + ": ");
            String name = scanner.nextLine();
            players.add(new Player(name, STARTING_CHIPS));
        }
        return players;
    }

    private static int bettingRound(List<Player> players) {
        int pot = 0;
        int currentBet = 0;
        int playersInRound = players.size();

        for (int i = 0; i < players.size() && playersInRound > 1; i = (i + 1) % players.size()) {
            Player player = players.get(i);
            if (player.folded || player.chips == 0) continue;

            player.showHand();
            System.out.println(player.name + ", you have " + player.chips + " chips");
            System.out.println("Current bet: " + currentBet + ", Your current bet: " + player.currentBet);
            System.out.print("Enter bet (0 to check/fold, -1 to call): ");

            int bet = scanner.nextInt();
            if (bet == -1) {
                bet = currentBet - player.currentBet;
            }

            if (bet > 0) {
                if (player.hasEnoughChips(bet)) {
                    player.bet(bet);
                    pot += bet;
                    currentBet = player.currentBet;
                } else {
                    System.out.println("Not enough chips!");
                    i--; // Retry same player
                }
            } else if (bet == 0) {
                if (currentBet > player.currentBet) {
                    System.out.println("You must at least call the current bet to stay in the round");
                    i--; // Retry same player
                } else {
                    System.out.println(player.name + " checks");
                }
            }
        }
        return pot;
    }

    private static void cardReplacementRound(List<Player> players, Deck deck) {
        for (Player player : players) {
            if (player.folded) continue;

            player.showHand();
            System.out.print(player.name + ", enter card numbers to replace (space separated, 0 to keep all): ");
            scanner.nextLine();
            String input = scanner.nextLine();

            if (!input.equals("0")) {
                List<Integer> indices = Arrays.stream(input.split(" "))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
                player.replaceCards(indices, deck);
                player.showHand();
            }
        }
    }

    private static void determineWinner(List<Player> players, int pot) {
        List<Player> activePlayers = players.stream()
            .filter(p -> !p.folded)
            .collect(Collectors.toList());

        if (activePlayers.isEmpty()) {
            System.out.println("All players folded! No winner this round.");
            return;
        }

        if (activePlayers.size() == 1) {
            Player winner = activePlayers.get(0);
            winner.win(pot);
            System.out.println(winner.name + " wins the pot of " + pot + " chips by default!");
            return;
        }

        // Evaluate all hands and find winner(s)
        int maxStrength = -1;
        List<Player> winners = new ArrayList<>();

        for (Player player : activePlayers) {
            int strength = player.getHandStrength();
            if (strength > maxStrength) {
                maxStrength = strength;
                winners.clear();
                winners.add(player);
            } else if (strength == maxStrength) {
                winners.add(player);
            }
        }

        // Distribute pot
        int winnings = pot / winners.size();
        for (Player winner : winners) {
            winner.win(winnings);
        }

        // Announce results
        System.out.println("\nShowdown results:");
        for (Player player : activePlayers) {
            System.out.println(player.name + ": " + getHandName(player.getHandStrength()));
        }
        if (winners.size() == 1) {
            System.out.println(winners.get(0).name + " wins the pot of " + pot + " chips!");
        } else {
            System.out.println("Split pot! Winners each get " + winnings + " chips");
        }
    }

    private static String getHandName(int strength) {
        switch (strength) {
            case 10: return "Royal Flush";
            case 9: return "Straight Flush";
            case 8: return "Four of a Kind";
            case 7: return "Full House";
            case 6: return "Flush";
            case 5: return "Straight";
            case 4: return "Three of a Kind";
            case 3: return "Two Pair";
            case 2: return "Pair";
            default: return "High Card";
        }
    }
}
