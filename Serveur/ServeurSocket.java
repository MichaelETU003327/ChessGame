import java.awt.Color;
import java.awt.Point;
import java.io.*;
import java.net.*;
import java.security.KeyStore.Entry;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ServeurSocket {
    private static final int PORT = 1234;
    private static final Map<String, Joueur> joueursConnectes = new ConcurrentHashMap<>();
    private static final Map<String, Partie> parties = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        System.out.println("Serveur multi-joueurs en attente de connexions sur le port " + PORT + "...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new GestionJoueur(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class GestionJoueur implements Runnable {
        private Socket socket;
        private String joueurID;
        private Joueur j;

        public GestionJoueur(Socket socket) {
            this.socket = socket;
        }

        public Point getPoint(String coup) {
            String[] s = coup.split(";");
            Point p = new Point(Integer.parseInt(s[0]),
                    Integer.parseInt(s[1]));
            return p;
        }

        @Override
        public void run() {
            try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                // Enregistrer le joueur
                String message = (String) in.readObject();
                joueurID = message;
                Joueur joueur = new Joueur(joueurID, socket);
                joueur.out = out;
                joueur.in = in;
                joueursConnectes.put(joueurID, joueur);

                while (true) {
                    String commande = (String) in.readObject();
                    if (commande.startsWith("LIST")) {
                        // Lister les joueurs disponibles
                        out.writeObject(new ArrayList<>(joueursConnectes.keySet()));
                    } else if (commande.startsWith("HOST")) {
                        String[] splited = commande.split(" ");
                        String partieID = splited[1].trim();
                        Partie partie = new Partie(joueurID, null);
                        parties.put(partieID, partie);
                        out.writeObject("PID " + partieID);
                        out.flush();
                    } else if (commande.startsWith("JOIN")) {
                        String[] splited = commande.split(" ");
                        String partieID = splited[1].trim();
                        Partie partie = parties.get(partieID);

                        if (partie.joueur2 != null) {
                            out.writeObject("Partie déjà pleine.");
                            out.flush();
                        } else {
                            partie.joueur2 = joueurID;
                            joueursConnectes.get(partie.joueur1).out.writeObject("white");
                            joueursConnectes.get(partie.joueur1).out.flush();

                            joueursConnectes.get(partie.joueur2).out.writeObject("PID " + partieID);
                            joueursConnectes.get(partie.joueur2).out.writeObject("black");
                            joueursConnectes.get(partie.joueur2).out.flush();
                        }
                    } else if (commande.startsWith("PLAY")) {
                        String[] splited = commande.split(" ");
                        String partieID = splited[1].trim();
                        String coup1 = splited[2].trim();
                        String coup2 = splited[3].trim();
                        Partie partie = parties.get(partieID);
                        String vs = partie.joueur1;
                        if (vs.equals(joueurID)) {
                            vs = partie.joueur2;
                        }
                        Joueur opp = joueursConnectes.get(vs);
                        opp.out.writeObject(getPoint(coup1));
                        opp.out.writeObject(getPoint(coup2));
                        opp.out.flush();
                    } else if (commande.startsWith("PARTY")) {
                        // Liste des parties disponibles
                        out.writeObject(new ArrayList<>(parties.keySet()));
                        out.flush();
                    } else if (commande.equals("QUIT")) {
                        out.writeObject("Déconnexion...");
                        break;
                    } else {
                        out.writeObject("Commande invalide.");
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Joueur " + joueurID + " déconnecté.");
            } finally {
                try {
                    joueursConnectes.remove(joueurID);
                    Set<Map.Entry<String, Partie>> entries = parties.entrySet();
                    for (Map.Entry<String, Partie> entry : entries) {
                        Partie partie = entry.getValue();
                        if (partie.joueur1 != null) {
                            if (partie.joueur1.equals(joueurID)) {
                                parties.remove(entry.getKey());
                            }
                        }
                        if (partie.joueur2 != null) {
                            if (partie.joueur2.equals(joueurID)) {
                                parties.remove(entry.getKey());
                            }
                        }

                    }
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Partie {
        String joueur1;
        String joueur2;

        public Partie(String joueur1, String joueur2) {
            this.joueur1 = joueur1;
            this.joueur2 = joueur2;
        }
    }

    static class Joueur {
        String joueurID;
        Socket socket;
        ObjectOutputStream out;
        ObjectInputStream in;

        public Joueur(String ID, Socket socket) {
            this.joueurID = ID;
            this.socket = socket;
        }

    }

}
