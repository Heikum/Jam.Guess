package main.Jam.Guess.Jam.Guess.audioplayer;

import aud.io.IMedia;
import aud.io.Votable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import main.Jam.Guess.IMedia;
import org.jongo.marshall.jackson.oid.MongoObjectId;

import java.io.Serializable;

public class Track extends Votable implements Serializable{

    //MongoDB van Track
    private static final String MONGO_ARTIST = "Artist";
    private static final String MONGO_ALBUM = "Album";

    //MongoDB van Votable
    private static final String MONGO_MEDIA = "Media";
    private static final String MONGO_NAME = "Name";
    private static final String MONGO_LENGTH = "Length";

    private String artist;
    private String album;

    @MongoObjectId
    private String _id;

    @JsonCreator
    public Track(@JsonProperty(MONGO_MEDIA) IMedia media,
                 @JsonProperty(MONGO_NAME) String name,
                 @JsonProperty(MONGO_LENGTH) float length,
                 @JsonProperty(MONGO_ARTIST) String artist,
                 @JsonProperty(MONGO_ALBUM) String album) {
        super(media, name, length);
        this.artist = artist;
        this.album = album;
    }

    /**
     * Create new Track using Media
     * @param media Media where the Track is at
     */
    public Track(IMedia media) {
        super(media);
    }

//    /**
//     * /**
//     * Create new Track
//     * @param media Media where the Track is at
//     * @param name Name of the Track
//     * @param length Length of the Track
//     * @param artist Artist of the Track
//     * @param album Album of the Track
//     */
//    public Track(IMedia media, String name, float length, String artist, String album) {
//        super(media, name, length);
//        this.artist = artist;
//        this.album = album;
//    }

    /**
     * Get Artist
     * @return artist
     */
    public String getArtist() {
        return artist;
    }

    /**
     * Get Album
     * @return album
     */
    public String getAlbum() {
        return album;
    }

    /**
     * Set Album
     * @param value new Album value
     */
    public void setAlbum(String value) {
        album = value;
    }
}
