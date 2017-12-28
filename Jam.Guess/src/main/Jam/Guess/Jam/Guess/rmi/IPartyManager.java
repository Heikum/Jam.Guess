package main.Jam.Guess.Jam.Guess.rmi;

import aud.io.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IPartyManager  extends Remote {

    IParty joinParty(String partyKey, User user) throws RemoteException;

    IParty createParty(RegisteredUser user, String partyName) throws RemoteException;

    void addMedia(Votable media, String partyKey, User user) throws RemoteException;

    List<Votable> getAllVotables() throws RemoteException;

    List<Votable> searchVotablesWithSearchTerm(String searchTerm) throws RemoteException;

    User login(String name, String password) throws RemoteException;

    Boolean createUser(String name, String password, String nickname) throws RemoteException;

    Boolean logout(User user, String partyKey) throws RemoteException;

    void vote(Votable votable, Vote vote, User user, String partyKey) throws RemoteException;

    boolean mediaIsPlayed(Votable media, String partyKey, User host) throws RemoteException;

    User getTemporaryUser(String nickname) throws RemoteException;

    void leaveParty(User user, String partyKey) throws RemoteException;

    void removeVotable(User user, String partyKey, Votable votable) throws RemoteException;
}
