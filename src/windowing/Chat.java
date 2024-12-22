package windowing;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.*;

public class Chat extends JPanel {
    // Fields
    private Window window;
    private JTextField textField;
    private JTextArea textArea;
    private JButton sendButton;

    // Getters and Setters
    public Window getWindow() {
        return window;
    }

    public void setWindow(Window window) {
        this.window = window;
    }

    public JTextField getTextField() {
        return textField;
    }

    public void setTextField(JTextField textField) {
        this.textField = textField;
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public void setTextArea(JTextArea textArea) {
        this.textArea = textArea;
    }

    public JButton getSendButton() {
        return sendButton;
    }

    public void setSendButton(JButton sendButton) {
        this.sendButton = sendButton;
    }

    // Constructors
    public Chat(Window window) {
        this.window = window;
    }

    public void init() {
        this.setLayout(new BorderLayout(5, 5)); // Espacement de 5 pixels entre les composants

        // Zone de texte pour afficher les messages
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true); // Retour automatique à la ligne
        textArea.setWrapStyleWord(true); // Couper les mots proprement
        JScrollPane scrollPane = new JScrollPane(textArea); // Ajout d'un scroll si nécessaire

        // Zone de saisie en bas
        textField = new JTextField();

        // Bouton d'envoi
        sendButton = new JButton("Send");
        sendButton.setPreferredSize(new Dimension(80, 30)); // Taille fixe pour le bouton

        sendButton.addActionListener(e -> {
            String message = textField.getText().trim();
            if (!message.isEmpty()) {
                window.getNetManager()
                        .sendMessage("CHAT " + window.getParty() + " " + window.getUsername() + " " + message);
                textField.setText(""); // Réinitialiser le champ de texte après l'envoi
            }
        });

        // Panneau en bas pour champ de saisie et bouton
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(textField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        // Ajout des composants au panneau principal
        this.add(scrollPane, BorderLayout.CENTER); // Zone de texte au centre
        this.add(bottomPanel, BorderLayout.SOUTH); // Panneau en bas
    }

    // Methods
    public void show() {
        init();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300, getHeight()); // Largeur fixe de 300 pixels
    }
}
