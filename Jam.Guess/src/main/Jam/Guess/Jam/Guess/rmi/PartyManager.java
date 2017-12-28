package main.Jam.Guess.Jam.Guess.rmi;


import aud.io.*;
import aud.io.fontyspublisher.RemotePublisher;
import aud.io.log.Logger;
import aud.io.mongo.MongoDatabase;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class PartyManager extends UnicastRemoteObject implements IPartyManager {
    private ArrayList<RegisteredUser> registeredUsers;
    private List<Party> activeParties;
    private IDatabase database;

    private RemotePublisher publisher;
    private Logger logger;

    public List<RegisteredUser> getRegisteredUsers() {
        return registeredUsers;
    }

    public List<Party> getActiveParties() {
        return activeParties;
    }

    public IDatabase getDatabase() {
        return database;
    }


    /**
     * Create a new PartyManager which will handle all Parties
     */
    public PartyManager(RemotePublisher publisher) throws RemoteException {
        this.publisher = publisher;
        database = new MongoDatabase();//new MemoryDatabase();
        activeParties = new ArrayList<>();
        logger = new Logger("PartyManager", Level.ALL, Level.SEVERE);
    }

    /**
     * Not implemented
     */
    public void sendKeepAlive() {
        //TODO: Implement
    }

    /**
     * Update a thing
     *
     * @param o
     * @param arg
     */


    /**
     * Update the playlist
     *
     * @param users    Users where the playlist will be updated
     * @param playlist list which will be updated
     * @deprecated
     */
    private void playListUpdate(ArrayList<User> users, ArrayList<Votable> playlist) {
        //TODO: Implement
    }

    /**
     * Join Party
     *
     * @param user     User which will join the Party
     * @param partyKey Key of the Party the User wants to join
     * @return true whether or not joining the party succeeded
     */
    @Override
    public synchronized IParty joinParty(String partyKey, User user) throws RemoteException {
        Party party = getPartyByKey(partyKey);
        if (party != null) {
            party.join(user);

            party.setPartyMessage(String.format("%s has joined the party.", user.getNickname()));
            logger.log(Level.INFO, String.format("%s has joined %s", user.getNickname(), party.getName()));
            publisher.inform(party.getPartyKey(), null, party);

            return party;
        }
        return null;
    }

    /**
     * Create a new Party
     *
     * @param user      User which will host the Party
     * @param partyName Name the party will use
     * @return Key which Users can use to join Party
     */
    @Override
    public synchronized IParty createParty(RegisteredUser user, String partyName) throws RemoteException {
        Party party = new Party(user, partyName);
        activeParties.add(party);
        logger.log(Level.INFO, String.format("%s has created a new party (%s) ", user.getNickname(), party.getName()));
        publisher.registerProperty(party.getPartyKey());

        return party;
    }

    /**
     * Add Votable to Party
     *
     * @param media    Votable to be added
     * @param partyKey Key of the Party the Votable should be added to
     * @return
     */
    @Override
    public synchronized void addMedia(Votable media, String partyKey, User user) throws RemoteException {
        Party party = getPartyByKey(partyKey);
        if (party != null) {
            party.addToVotables(media);
            logger.log(Level.INFO, String.format("%s added %s to %s", user.getNickname(), media.getName(), party.getName()));
            party.setPartyMessage(String.format("%s added %s", user.getNickname(), media.getName()));
            publisher.inform(party.getPartyKey(), null, party);

            //TODO: User votes on votable?
        }
    }

    @Override
    public List<Votable> getAllVotables() throws RemoteException {
        return database.getAllSongs();
    }

    @Override
    public List<Votable> searchVotablesWithSearchTerm(String searchTerm) throws RemoteException {
        return database.getSongsWithSearchterm(searchTerm);
    }

    /**
     * Login for existing users
     *
     * @param name     Email of the user
     * @param password Password of the user
     * @return User that has just logged in
     */

    @Override
    public synchronized User login(String name, String password) throws RemoteException {
        User user = database.loginUser(name, password);
        if (user != null) {
            if (user.getNickname().equals(name)) {
                logger.log(Level.INFO, String.format("%s logged in", user.getNickname()));
                return user;
            }
            return database.loginUser(name, password);
        }
        return null;
    }

    /**
     * Create a new user
     *
     * @param name     Email of the new user
     * @param password Password of the new user
     * @param nickname Nickname the user would like to use
     * @return whether or not adding user succeeded
     */
    @Override
    public synchronized Boolean createUser(String name, String password, String nickname) throws RemoteException {
        Boolean success = database.createUser(name, nickname, password);
        if (success) {
            logger.log(Level.INFO, String.format("Created new user: name:%s nick:%s", name, nickname));
            return database.createUser(name, nickname, password);
        }
        return success;
    }

    @Override
    public synchronized Boolean logout(User user, String partyKey) throws RemoteException {
        if (!partyKey.equals("")) {
            leaveParty(user, partyKey);
            logger.log(Level.INFO, String.format("%s logged out", user.getNickname()));
            return true;
        }
        return false;
    }

    @Override
    public synchronized void vote(Votable votable, Vote vote, User user, String partyKey) throws RemoteException {
        //Not sure how to implement this, should do the following:
        //User votes on a votable with a vote, vote isn't defined though.
        Party party = getPartyByKey(partyKey);
        if (party != null) {
            party.voteOnVotable(user, votable, vote);
            logger.log(Level.INFO, String.format("%s has voted with %s on %s", user.getNickname(), vote.toString(), votable.getName()));
            party.setPartyMessage(String.format("%s has voted on %s.", user.getNickname(), votable.getName()));
            if (party.belowRemovalThreshold(votable)) {
                removeVotable(user, partyKey, votable);
            } else {
                publisher.inform(party.getPartyKey(), null, party);
            }

            //logger.log(Level.INFO, String.format("%s has left party %s", user.getNickname(), party.getName()));
        }
    }

    @Override
    public synchronized boolean mediaIsPlayed(Votable media, String partyKey, User host) throws RemoteException {
        Party party = getPartyByKey(partyKey);
        if (party != null) {
            if (party.mediaIsPlayed(media, host)) {
                party.setPartyMessage(String.format("%s has started playing.", media.getName()));
                logger.log(Level.INFO, String.format("%s has started playing", media.getName()));
                publisher.inform(party.getPartyKey(), null, party);
                return true;
            }
        }

        return false;
    }

    @Override
    public synchronized User getTemporaryUser(String nickname) throws RemoteException {
        return new TemporaryUser(nickname);
    }

    @Override
    public synchronized void leaveParty(User user, String partyKey) throws RemoteException {
        Party party = getPartyByKey(partyKey);
        if (party != null) {
            party.removeUser(user);
            logger.log(Level.INFO, String.format("%s has left party %s", user.getNickname(), party.getName()));
            party.setPartyMessage(String.format("%s has left the party.", user.getNickname()));
            //logger.log(Level.INFO, String.format("%s has left party %s", user.getNickname(), party.getName()));
            publisher.inform(party.getPartyKey(), null, party);
        }
    }

    @Override
    public void removeVotable(User user, String partyKey, Votable votable) throws RemoteException {
        Party party = getPartyByKey(partyKey);
        if (party != null) {
            party.removeVotable(votable);
            logger.log(Level.INFO, String.format("%s has removed %s in party %s", user.getNickname(), votable.getName(), party.getName()));
            party.setPartyMessage(String.format("%s has removed %s in party %s", user.getNickname(), votable.getName(), party.getName()));
            //logger.log(Level.INFO, String.format("%s has left party %s", user.getNickname(), party.getName()));
            publisher.inform(party.getPartyKey(), null, party);
        }
    }

    public synchronized Party getPartyByKey(String partyKey) {
        for (Party party : activeParties) {
            if (party.getPartyKey().equals(partyKey)) return party;
        }

        return null;
    }
}
