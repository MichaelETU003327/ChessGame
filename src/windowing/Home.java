package windowing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Home extends JPanel {
    // Fields
    Window window;
    JButton[] buttons = new JButton[3];

    // Constructors
    public Home(Window window) {
        this.window = window;
        init();
    }

    // Getters and Setters
    public Window getWindow() {
        return window;
    }

    public void setWindow(Window window) {
        this.window = window;
    }

    // Methods
    public void init() {
        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        buttons[0] = new JButton("SplitScreen");
        buttons[1] = new JButton("LAN");
        buttons[2] = new JButton("Quit");

        int y = 0;
        for (JButton jButton : buttons) {
            gbc.gridx = 0;
            gbc.gridy = y;
            this.add(jButton, gbc);
            y++;
        }
        for (JButton jButton : buttons) {
            String text = jButton.getText();
            jButton.addActionListener(e -> {
                if (text.equals("Quit")) {
                    System.exit(0);
                } else {
                    window.setGameMode(text);
                    if (text.equals("LAN")) {
                        window.getLogin().show();
                    } else {
                        window.add(window.getDraw(), BorderLayout.CENTER); // Plateau de jeu au centre
                    }
                }
            });
        }

    }

}
