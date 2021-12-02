package com.azul.client.graphics;

import com.azul.client.controllers.GameController;

import javax.swing.*;
import java.awt.*;
import java.util.stream.Collectors;

public class WaitingForm extends JFrame {

    JLabel statusLabel = new JLabel("Waiting for players...");
    JLabel playersLabel = new JLabel("Players");
    JTextField namesText = new JTextField();
    private String playerName;
    GameController game = GameController.getInstance();

    public WaitingForm(String playerName) {
        this.playerName = playerName;
        var screen = Toolkit.getDefaultToolkit().getScreenSize();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setTitle("Azul");
        setSize((int) (screen.width / 4), (int) (screen.height / 6));
        setLocation(screen.width / 2 - getWidth() / 2, screen.height / 2 - getHeight() / 2);

        namesText.setEnabled(false);

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0; c.gridy = 0;
        c.gridwidth = 2;
        add(statusLabel, c);

        c.gridx = 0; c.gridy = 1;
        c.gridwidth = 1;
        add(playersLabel, c);

        c.gridx = 1; c.gridy = 1;
        c.weightx = 0.5;
        add(namesText, c);

        Thread thread = new Thread(startWaiting());
        thread.start();
    }

    public Runnable startWaiting(){
        return new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(2000);
                        game.update();

                        var players = game.getPlayers();
                        namesText.setText(String.join(", ", players.stream()
                                .map(t -> t.getName()).collect(Collectors.toList())));

                        if (game.getStatus().equalsIgnoreCase("READY")){
                            var gameForm = new GameForm();
                            gameForm.setVisible(true);

                            setVisible(false);
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }
}
