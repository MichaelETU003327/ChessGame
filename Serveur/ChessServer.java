import java.awt.Point;
import java.io.*;
import java.net.*;
import java.util.Vector;

public  class ChessServer {
    private static final int PORT = 1234;
    private static boolean joueur1Tour = true; // Indique à qui appartient le tour
    private static Vector<Socket> players = new Vector<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Serveur d'échecs en attente de connexions sur le port " + PORT + "...");

            // Accepter plusieurs connexions
            while (true) {
                Socket player = serverSocket.accept();
                players.addElement(player);
            }

            // Envoyer une confirmation aux joueurs

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
