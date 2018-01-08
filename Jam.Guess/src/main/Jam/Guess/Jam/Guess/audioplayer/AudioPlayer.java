package main.Jam.Guess.Jam.Guess.audioplayer;

import main.Jam.Guess.IMedia;
import main.Jam.Guess.IPlayer;
import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AudioPlayer implements IPlayer {
    private final ExecutorService pool;
    private AudioMediaPlayerComponent VLCPlayer;
    private PlayerRunnable currentSong;
    private Logger logger;

    public AudioPlayer(ExecutorService pool, AudioMediaPlayerComponent VLCPlayer) {
        this.pool = pool;
        //TODO: Insert actual player
        this.VLCPlayer = VLCPlayer;
    }




    @Override
    public void stop() {
        currentSong.stop();
    }

    @Override
    public void pause() {
        currentSong.pause();
    }

    @Override
    public void play() {
        //load new song and start playing
        if (currentSong != null){
            currentSong.exit();
        }
        //TODO: Revise with thread handling in mind

    }

    private class PlayerRunnable implements Runnable{

        private Future mediaFileContainer;
        private File mediaFile;
        private IMedia trackMedia;
        private ExecutorService pool;
        private AudioMediaPlayerComponent VLCPlayer;
        private AtomicBoolean play, pause, stop, exit;
        private boolean loadedSong = false;

        PlayerRunnable(IMedia trackMedia, ExecutorService pool, AudioPlayer player, AudioMediaPlayerComponent VLCPlayer) {
            this.trackMedia = trackMedia;
            this.pool = pool;
            this.VLCPlayer = VLCPlayer;
            play = new AtomicBoolean(false);
            pause = new AtomicBoolean(false);
            stop = new AtomicBoolean(false);
            exit = new AtomicBoolean(false);
        }

        @Override
        public void run() {
            System.out.println("loading...");

            VLCPlayer.getMediaPlayer().stop();
            //VLCPlayer.getMediaPlayer().release();
            //VLCPlayer.release();
            System.out.println("new player...");
            VLCPlayer = new AudioMediaPlayerComponent();

            mediaFileContainer = pool.submit(trackMedia.getFile());
            try {
                mediaFile = (File)mediaFileContainer.get();
            } catch (InterruptedException | ExecutionException e) {
                logger.log(Level.SEVERE, e.toString());
            }


            System.out.println("loaded: " + mediaFile.getAbsolutePath());

            while (!exit.get()){
                if (play.get()){
                    play.set(false);
                    if (!loadedSong){
                        //TODO: load song
                        System.out.println(mediaFile.getAbsolutePath());
                        System.out.println("playing");
                        VLCPlayer.getMediaPlayer().playMedia(mediaFile.getAbsolutePath());
                        loadedSong = true;
                    } else{
                        System.out.println("playing");
                        VLCPlayer.getMediaPlayer().play();
                    }
                    //TODO: play
                }
                if (pause.get()){
                    pause.set(false);
                    System.out.println("paused");
                    VLCPlayer.getMediaPlayer().pause();
                    //TODO: pause
                }
                if (stop.get()){
                    stop.set(false);
                    System.out.println("stopped");
                    VLCPlayer.getMediaPlayer().stop();
                    //VLCPlayer.getMediaPlayer().release();
                    loadedSong = false;
                    //TODO: stop
                }
            }

            //TODO: stop song, handle thread finishing

            VLCPlayer.getMediaPlayer().stop();
            //VLCPlayer.getMediaPlayer().release();

        }

        public void play(){
            play.set(true);
        }

        public void pause(){
            pause.set(true);
        }

        public void stop(){
            stop.set(true);
        }

        public void exit(){
            exit.set(true);
        }
    }
}
