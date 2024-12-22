package windowing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import manager.NetworkManager;

public class Login extends JPanel {
    Window window;
    JTextField usernameField;

    public Login(Window window) {
        this.window = window;
        this.setLayout(new GridBagLayout());
        usernameField = new JTextField(15);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(new JLabel("Nom d'utilisateur :"), gbc);

        gbc.gridx = 1;
        this.add(usernameField, gbc);
    }

    public Window getWindow() {
        return window;
    }

    public void setWindow(Window window) {
        this.window = window;
    }

    public void show() {
        int result = JOptionPane.showConfirmDialog(
                window, this, "Connexion", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            window.getHome().setVisible(false);
            window.setUsername(usernameField.getText().trim());
            window.add(window.getLanHome());
            window.getLanHome().show();
            window.revalidate();
            window.setNetManager(new NetworkManager(window));
            window.getNetManager().initialize("localhost", 1234);
            window.getNetManager().sendMessage(window.getUsername());
        }

    }
}
