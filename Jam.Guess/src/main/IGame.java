package main;

public interface IGame {
    int getId();

    int getRounds();

    void setRounds(int rounds);

    void startGame();

    void endGame();
}
