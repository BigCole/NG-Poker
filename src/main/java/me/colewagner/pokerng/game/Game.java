package me.colewagner.pokerng.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Game {
    private static final int STARTING_BALANCE = 500;
    private static final int MIN_BET = 10;
    private static final int SMALL_BLIND = 5;
    private static final int BIG_BLIND = 15;
    private Deck deck;
    private List<Player> players;
    private String username;
    private int pot;

    public HashMap<Integer, String> positionNames = new HashMap<>();

    public Game() {
        deck = new Deck();
        players = new ArrayList<>();
        pot = 0;
    }

    private void definePositions() {
        positionNames.put(0, "Button");
        positionNames.put(1, "Small Blind");
        positionNames.put(2, "Big Blind");
        positionNames.put(3, "Hijack");
        positionNames.put(4, "Cutoff");
    }

    private void addPlayers() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("What is your name?\n");
        username = scanner.nextLine();
        players.add(new Player(username, STARTING_BALANCE, 0));
        for(int i = 1; i < 5; i++) {
            players.add(new Player("Player " + i, STARTING_BALANCE, i));
        }
    }

    public void start() {
        definePositions();
        addPlayers();
        while (players.size() > 1) {
            playHand();
        }
        System.out.println("Game over!");
        System.out.println(players.get(0).getName() + " is the winner!");
    }

    private void playHand() {
        deck.shuffle();
        dealCards();
        int bet = getMinBet();
        doBettingRound(bet);
        if (players.size() > 1) {
            exchangeCards();
            bet = getMinBet();
            doBettingRound(bet);
        }
        if (players.size() > 1) {
//            determineWinner();
        }
        collectBets();
        removeBankruptPlayers();
    }

    private void dealCards() {
        for (Player player : players) {
            player.getHand().getCards().clear();
            for (int i = 0; i < 2; i++) {
                player.addCard(deck.deal());
            }
        }
    }

    private int getMinBet() {
        int maxBalance = 0;
        for (Player player : players) {
            if (player.getBalance() > maxBalance) {
                maxBalance = player.getBalance();
            }
        }
        return Math.min(MIN_BET, maxBalance);
    }

    private void doBettingRound(int bet) {
        for (Player player : players) {
            if (player.getBalance() == 0) {
                continue;
            }
            int amount = 0;
            if(player.getName().equals(username)) {
                amount = getBetFromPlayer(player, bet);
            } else {
                //TODO AI determines move
            }
            pot += amount;
            player.setBalance(player.getBalance() - amount);
        }
    }

    private int getBetFromPlayer(Player player, int minBet) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(player.getName() + ", your current balance is $" + player.getBalance());
        System.out.println(String.format("Your hand is a %s of %s and a %s of %s.",
                player.getHand().getCards().get(0).getRank(),
                player.getHand().getCards().get(0).getSuit(),
                player.getHand().getCards().get(1).getRank(),
                player.getHand().getCards().get(1).getSuit()));
        System.out.println(String.format("You are in %s position and you can either:", positionNames.get(player.getPosition())));
        while (true) {
            System.out.print("Enter your bet: ");
            int bet = scanner.nextInt();
            if (bet < minBet) {
                System.out.println("Your bet is too low.");
            } else if (bet > player.getBalance()) {
                System.out.println("You cannot bet more than your balance.");
            } else {
                return bet;
            }
        }
    }

    private void exchangeCards() {
        for (Player player : players) {
            if (player.getBalance() == 0) {
                continue;
            }
            Scanner scanner = new Scanner(System.in);
            System.out.println(player.getName() + ", you can exchange up to 3 cards from your hand.");
            List<Card> cardsToExchange = new ArrayList<>();
            while (cardsToExchange.size() < 3) {
                System.out.print("Enter the index of a card to exchange (or -1 to stop): ");
                int index = scanner.nextInt();
                if (index == -1) {
                    break;
                } else if (index < 0 || index > 4) {
                    System.out.println("Invalid index.");
                } else {
                    cardsToExchange.add(player.getHand().getCards().get(index));
                }
            }

            for (Card card : cardsToExchange) {
                player.removeCard(card);
                deck.returnCardToDeck(card);
            }
            while (cardsToExchange.size() < 3) {
                cardsToExchange.add(deck.deal());
            }
            player.getHand().getCards().addAll(cardsToExchange);
            System.out.println(player.getName() + ", your new hand is:");
            System.out.println(player.getHand());
        }
    }

//    private void determineWinner() {
//        Hand bestHand = null;
//        List<Player> winners = new ArrayList<>();
//        for (Player player : players) {
//            if (player.getBalance() == 0) {
//                continue;
//            }
//            Hand hand = player.getHand();
//            if (bestHand == null || hand.compareTo(bestHand) > 0) {
//                bestHand = hand;
//                winners.clear();
//                winners.add(player);
//            } else if (hand.compareTo(bestHand) == 0) {
//                winners.add(player);
//            }
//        }
//        if (winners.size() == 1) {
//            //TODO getDescription() method
//            System.out.println(winners.get(0).getName() + " wins with " + bestHand.getDescription() + "!");
//            winners.get(0).setBalance(winners.get(0).getBalance() + pot);
//        } else {
//            System.out.println("Tie between:");
//            for (Player winner : winners) {
//                System.out.println("- " + winner.getName());
//                winner.setBalance(winner.getBalance() + pot / winners.size());
//            }
//        }
//    }

    private void collectBets() {
        for (Player player : players) {
            if (player.getBalance() == 0) {
                continue;
            }
            pot += Math.min(MIN_BET, player.getBalance());
            player.setBalance(player.getBalance() - Math.min(MIN_BET, player.getBalance()));
        }
    }

    private void removeBankruptPlayers() {
        List<Player> bankruptPlayers = new ArrayList<>();
        for (Player player : players) {
            if (player.getBalance() == 0) {
                bankruptPlayers.add(player);
            }
        }
        players.removeAll(bankruptPlayers);
        System.out.println("The following players are bankrupt and have been removed from the game:");
        for (Player player : bankruptPlayers) {
            System.out.println("- " + player.getName());
        }
    }
}
