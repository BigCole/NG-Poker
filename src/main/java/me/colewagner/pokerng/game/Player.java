package me.colewagner.pokerng.game;

public class Player {

    private String name;
    private Hand hand;
    private int balance;
    private int position;

    public Player(String name, int balance, int position) {
        this.name = name;
        this.balance = balance;
        this.position = position;
        this.hand = new Hand();
    }

    public String getName() {
        return name;
    }

    public Hand getHand() {
        return hand;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void addCard(Card card) {
        hand.addCard(card);
    }

    public void removeCard(Card card) {
        hand.removeCard(card);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}

