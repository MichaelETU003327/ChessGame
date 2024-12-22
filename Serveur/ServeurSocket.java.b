import java.awt.Point;
import java.io.*;
import java.net.*;

public class ServeurSocket {
    private static final int PORT = 1234;
    private static boolean joueur1Tour = true; // Indique à qui appartient le tour

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Serveur d'échecs en attente de connexions sur le port " + PORT + "...");

            // Accepter deux connexions
            Socket joueur1 = serverSocket.accept();
            System.out.println("Joueur 1 connecté : " + joueur1.getInetAddress());
            ObjectOutputStream out1 = new ObjectOutputStream(joueur1.getOutputStream());
            ObjectInputStream in1 = new ObjectInputStream(joueur1.getInputStream());

            Socket joueur2 = serverSocket.accept();
            System.out.println("Joueur 2 connecté : " + joueur2.getInetAddress());
            ObjectOutputStream out2 = new ObjectOutputStream(joueur2.getOutputStream());
            ObjectInputStream in2 = new ObjectInputStream(joueur2.getInputStream());

            System.out.println("Les deux joueurs sont connectés. Début de la partie.");

            // Envoyer une confirmation aux joueurs
            out1.writeObject("Vous êtes le joueur 1. Vous commencez !");
            out2.writeObject("Vous êtes le joueur 2. Attendez votre tour.");
            
            // Boucle principale du jeu
            while (true) {
                try {
                    if (joueur1Tour) {
                        // Informer J1 qu'il peut jouer
                        out1.writeObject("Votre tour");
                        Point move1 = (Point) in1.readObject(); // Recevoir le premier mouvement de J1
                        Point move2 = (Point) in1.readObject(); // Recevoir le deuxième mouvement de J1
                        System.out.println("Joueur 1 a joué : " + move1 + " et " + move2);

                        // Envoyer les mouvements à J2
                        out2.writeObject(move1);
                        out2.writeObject(move2);
                        out2.writeObject("Adversaire a joué : " + move1 + " et " + move2);

                        joueur1Tour = false; // Passer au tour du joueur 2
                    } else {
                        // Informer J2 qu'il peut jouer
                        out2.writeObject("Votre tour");
                        Point move1 = (Point) in2.readObject(); // Recevoir le premier mouvement de J2
                        Point move2 = (Point) in2.readObject(); // Recevoir le deuxième mouvement de J2
                        System.out.println("Joueur 2 a joué : " + move1 + " et " + move2);

                        // Envoyer les mouvements à J1
                        out1.writeObject(move1);
                        out1.writeObject(move2);
                        out1.writeObject("Adversaire a joué : " + move1 + " et " + move2);

                        joueur1Tour = true; // Passer au tour du joueur 1
                    }
                } catch (SocketException e) {
                    System.out.println("Un joueur s'est déconnecté : " + e.getMessage());
                    handleDisconnection(joueur1, joueur2, out1, out2, in1, in2);
                    break; // Sortir de la boucle principale
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    break; // Sortir en cas d'autre erreur
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleDisconnection(Socket joueur1, Socket joueur2, ObjectOutputStream out1,
            ObjectOutputStream out2, ObjectInputStream in1, ObjectInputStream in2) {
        try {
            if (!joueur1.isClosed()) {
                out1.writeObject("L'adversaire s'est déconnecté. Fin de la partie.");
                joueur1.close();
            }
            if (!joueur2.isClosed()) {
                out2.writeObject("L'adversaire s'est déconnecté. Fin de la partie.");
                joueur2.close();
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la fermeture des connexions : " + e.getMessage());
        }
    }
}
