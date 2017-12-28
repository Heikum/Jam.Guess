package main.Jam.Guess.Jam.Guess;

public class Round {
    public int roundCount;
    public Music currentSong;
    public User roundWinner;

    public Round(int roundCount, Music currentSong) {
        this.roundCount = roundCount;
        this.currentSong = currentSong;
    }

    public void endRound()
    {

    }
    public boolean checkWinner(String answer, String title)
    {
        return true;
    }
}
