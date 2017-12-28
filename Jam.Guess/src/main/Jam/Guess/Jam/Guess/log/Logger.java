package main.Jam.Guess.Jam.Guess.log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;

public class Logger {

    private java.util.logging.Logger logger;

    public Logger(String name, Level fileLogging, Level commandLogging) {
        setupLogger(name, fileLogging, commandLogging);
    }

    public void setupLogger(String name, Level fileLogging, Level commandLogging) {
        try {
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(Calendar.getInstance().getTime());
            FileHandler fh = new FileHandler(String.format("logs/%s-%s.log", name, timeStamp));
            fh.setLevel(fileLogging);
            ConsoleHandler ch = new ConsoleHandler();
            ch.setLevel(commandLogging);
            logger = java.util.logging.Logger.getLogger(name);
            logger.addHandler(fh);
            logger.setLevel(Level.ALL);
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    public void log(Level level, String message){
        logger.log(level, message);
    }
}
