package windowing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import manager.*;
import drawing.*;
import listening.Lsnr;

public class Window extends JFrame {
    // Fields
    Draw draw;
    Home home;
    Login login;
    LanHome lanHome;
    Chat chat;

    String username;
    String color;
    String gameMode;
    String party;

    NetworkManager netManager;

    // Getters and Setters
    public Draw getDraw() {
        return draw;
    }

    public void setDraw(Draw draw) {
        this.draw = draw;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public NetworkManager getNetManager() {
        return netManager;
    }

    public void setNetManager(NetworkManager netManager) {
        this.netManager = netManager;
    }

    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public LanHome getLanHome() {
        return lanHome;
    }

    public void setLanHome(LanHome lanHome) {
        this.lanHome = lanHome;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }




    // Constructor
    public Window() {
        this.home = new Home(this);
        this.draw = new Draw(this);
        this.draw.addMouseListener(new Lsnr(this));
        this.chat = new Chat(this);
        this.login = new Login(this);
        this.lanHome = new LanHome(this);
        this.add(home);
        this.setSize(1000, 750);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        
    }

}
