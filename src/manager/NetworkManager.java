package manager;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.channels.Pipe;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;

import test.Test;
import windowing.Window;

public class NetworkManager {

    // Fields
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean isTurn = false; // Indique si c'est le tour du joueur
    private Window window;
    private Test jframe;

    // Constructors
    public NetworkManager(Window window) {
        this.window = window;

    }

    public NetworkManager(Test jFrame) {
        this.jframe = jFrame;

    }

    // Methods
    public void initialize(String hostname, int port) {
        try {
            socket = new Socket(hostname, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            // // // // Lancer le thread de réception
            new Thread(this::listen).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // public void listen() {
    // while (!socket.isClosed() && !Thread.currentThread().isInterrupted()) {
    // try {
    // Object obj = in.readObject();
    // System.out.println("Objet reçu : " + obj);
    // if (obj instanceof ArrayList) {
    // jframe.setRep(((ArrayList) obj).toString());
    // jframe.repaint();
    // } else {
    // jframe.setRep((String) obj);
    // jframe.repaint();
    // }
    // // Traitez l'objet ici (String, Point, etc.)
    // } catch (EOFException e) {
    // e.printStackTrace();
    // System.err.println("Connexion fermée par le serveur.");
    // break;
    // } catch (IOException | ClassNotFoundException e) {
    // e.printStackTrace();
    // break;
    // }
    // }

    // }

    public void listen() {
        while (!socket.isClosed() && !Thread.currentThread().isInterrupted()) {
            try {
                Object obj = in.readObject();
                System.out.println("Objet reçu : " + obj);
                if (obj instanceof ArrayList) {
                    window.getLanHome().setParties((ArrayList) obj);
                    window.getLanHome().refreshPartyList();
                } else if (obj instanceof String) {
                    String message = (String) obj;
                    String[] splited = message.split(" ");
                    if (message.startsWith("PID")) {
                        window.setParty(splited[1]);
                    }
                    if (message.equals("white") || message.equals("black")) {
                        window.setColor(message);
                        window.setTitle("ChessGame: " + window.getUsername() + ";" + window.getColor());
                        window.getLanHome().setVisible(false);
                        // Ajouter les composants
                        window.add(window.getDraw(), BorderLayout.CENTER); // Plateau de jeu au centre
                        window.add(window.getChat(), BorderLayout.EAST); // Chat à droite
                        window.getChat().show();
                        window.revalidate();
                    }
                    if (message.startsWith("MESSAGE")) {
                        String tmp = message.replace("MESSAGE", "");
                        tmp = tmp.trim();
                        tmp += "\n";
                        String curr = window.getChat().getTextArea().getText();
                        if (curr == null) {
                            curr = "";
                        }
                        curr += tmp;
                        window.getChat().getTextArea().setText(curr);
                    }
                } else if (obj instanceof Point) {
                    Point move1 = (Point) obj;
                    Point move2 = (Point) obj;
                    window.getDraw().getManager().getInterpreter().execute1(move1.x, move1.y);
                    window.getDraw().getManager().getInterpreter().execute2(move2.x, move2.y);
                    window.getDraw().repaint();
                }

            } catch (EOFException e) {
                e.printStackTrace();
                System.err.println("Connexion fermée par le serveur.");
                break;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                break;
            }
        }

    }

    public void startGame() {
        try {

            // Attendre la notification du serveur pour commencer
            while (true) {
                Object obj = in.readObject();
                if (obj instanceof String && ((String) obj).contains("La partie commence")) {
                    System.out.println((String) obj);
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setMove(Point move1, Point move2) {
        String coup1 = move1.x + ";" + move1.y;
        String coup2 = move2.x + ";" + move2.y;
        String coup = coup1 + " " + coup2;
        sendMessage("PLAY " + window.getParty() + " " + coup);
    }

    // Méthode pour envoyer un message simple au serveur
    public void sendMessage(String message) {
        try {
            out.writeObject(message);
            out.flush();
            System.out.println("Message envoyé : " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour obtenir une réponse
    public Object response() {
        Object response = null;
        try {
            // Attendre une réponse du serveur
            response = in.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();

        }
        return response;
    }

    // Méthode pour envoyer une requête et obtenir une réponse
    public Object request(String request) {
        try {
            out.writeObject(request);
            out.flush();
            System.out.println("Requête envoyée : " + request);

            // Attendre une réponse du serveur
            Object response = in.readObject();
            System.out.println("Réponse reçue : " + response);

            return response;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
