package windowing;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.*;

public class LanHome extends JPanel {
    // Fiels[
    Window window;
    DefaultListModel<String> connectedPlayersModel;
    DefaultListModel<String> partiesModel;
    List<String> players = new ArrayList<>();

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    List<String> parties = new ArrayList<>();

    // ]

    public List<String> getParties() {
        return parties;
    }

    public void setParties(List<String> parties) {
        this.parties = parties;
    }

    // Constructors[
    public LanHome(Window window) {
        this.window = window;
    }
    // ]

    // Getters and Setters[
    public Window getWindow() {
        return window;
    }

    public void setWindow(Window window) {
        this.window = window;
    }

    // ]

    // Methods[
    // Méthode pour afficher l'écran d'accueil
    public void show() {

        this.setLayout(new BorderLayout());

        // Titre
        JLabel welcomeLabel = new JLabel("Bienvenue, " + window.getUsername() + " !");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        this.add(welcomeLabel, BorderLayout.NORTH);

        // Liste des joueurs connectés
        connectedPlayersModel = new DefaultListModel<>();
        JList<String> connectedPlayersList = new JList<>(connectedPlayersModel);
        connectedPlayersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(connectedPlayersList);
        this.add(scrollPane, BorderLayout.CENTER);

        // Liste des parties
        partiesModel = new DefaultListModel<>();
        JList<String> partiesList = new JList<>(partiesModel);
        partiesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPanel = new JScrollPane(partiesList);
        this.add(scrollPanel, BorderLayout.CENTER);

        // Boutons
        JPanel buttonsPanel = new JPanel();
        //JButton inviteButton = new JButton("Inviter");
        JButton refreshButton = new JButton("Rafraîchir");
        JButton joinButton = new JButton("Rejoindre");
        JButton hostButton = new JButton("Heberger");

        //buttonsPanel.add(inviteButton);
        buttonsPanel.add(refreshButton);
        buttonsPanel.add(joinButton);
        buttonsPanel.add(hostButton);
        this.add(buttonsPanel, BorderLayout.SOUTH);

        // Actions
        refreshButton.addActionListener(e -> window.getNetManager().sendMessage("PARTY"));
        // inviteButton.addActionListener(e -> {
        //     String selectedPlayer = connectedPlayersList.getSelectedValue();
        //     if (selectedPlayer != null) {
        //         window.getNetManager().sendMessage("INVITE : " + selectedPlayer);

        //     } else {
        //         JOptionPane.showMessageDialog(this, "Veuillez sélectionner un joueur.", "Erreur",
        //                 JOptionPane.ERROR_MESSAGE);
        //     }
        // });

        joinButton.addActionListener(e -> {
            String selectedParty = partiesList.getSelectedValue();
            if (selectedParty != null) {
                window.getNetManager().sendMessage("JOIN " + selectedParty);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une partie.", "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        hostButton.addActionListener(e -> {
        JPanel p=new JPanel(new GridBagLayout());    
        JTextField usernameField = new JTextField(15);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        p.add(new JLabel("Nom de la partie :"), gbc);

        gbc.gridx = 1;
        p.add(usernameField, gbc);

        int result = JOptionPane.showConfirmDialog(
                window,p, "Creation d'une partie", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            window.getNetManager().sendMessage("HOST "+usernameField.getText().trim());
        }

        });

    }

    // Rafraîchir la liste des joueurs
    private void refreshPlayerList() {
        window.getNetManager().sendMessage("LIST");
        if (players != null) {
            connectedPlayersModel.clear();
            for (String player : players) {
                if (!player.equals(window.getUsername())) { // Ne pas afficher soi-même
                    connectedPlayersModel.addElement(player);
                }
            }
        }
    }

    // Rafraîchir la liste des parties
    public void refreshPartyList() {
        if (parties != null) {
            partiesModel.clear();
            for (String party : parties) {
                if (!party.equals(window.getParty())) {
                    partiesModel.addElement(party);
                }

            }
        }
    }

    // ]
}
