package test;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.TextArea;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import manager.NetworkManager;

public class Test extends JFrame {
    TextArea textArea;
    JButton submit;
    NetworkManager nm;
    String rep;
    int click=0;

    public String getRep() {
        return rep;
    }

    public void setRep(String rep) {
        this.rep = rep;
    }

    public JButton getSubmit() {
        return submit;
    }

    public void setSubmit(JButton submit) {
        this.submit = submit;
    }

    public TextArea getTextArea() {
        return textArea;
    }

    public void setTextArea(TextArea textArea) {
        this.textArea = textArea;
    }

    public NetworkManager getNm() {
        return nm;
    }

    public void setNm(NetworkManager nm) {
        this.nm = nm;
    }

    public Test() {

        JPanel panel = new JPanel(new FlowLayout());
        textArea = new TextArea();
        submit = new JButton("Submit");
        panel.add(textArea);
        panel.add(submit);
        this.add(panel);

        nm = new NetworkManager(this);
        nm.initialize("localhost", 1234);
        submit.addActionListener(e -> {
            nm.sendMessage(textArea.getText());
            if (click<1) {
                setTitle(textArea.getText());
            }
            click++;
        });

        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if(rep!=null)
        g.drawString(rep,250, 250);
        
    }

}
