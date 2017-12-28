package main.Jam.Guess.Jam.Guess;

public class Game implements IGame {

    public int getId() {
        return id;
    }

    public int id;


    public int getRounds() {
        return rounds;
    }


    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public int rounds;

    public MusicPlayer musicplayer;


    public void startGame(){}

    public void endGame(){}

}
