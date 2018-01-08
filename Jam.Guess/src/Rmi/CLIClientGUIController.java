package Rmi;


import main.Jam.Guess.IGUIController;
import main.Jam.Guess.Jam.Guess.rmi.ClientManager;

public class CLIClientGUIController implements IGUIController {
    private ClientManager manager;

    public CLIClientGUIController(ClientManager manager) {
        this.manager = manager;
    }

    @Override
    public void update() {
        if (manager.getUser() != null){

        }
    }




}
