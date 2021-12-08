package com.azul.client.graphics;

import com.azul.client.controllers.GameController;

import javax.swing.*;
import java.awt.*;
import java.util.stream.Collectors;

public class WaitingForm extends JFrame {

    JTextField statusText = new JTextField("...");
    JTextField namesText = new JTextField();
    JTextField gameIdText = new JTextField();
    private String playerName;
    GameController gameController = GameController.getInstance();

    public WaitingForm(String playerName) {
        this.playerName = playerName;
        var screen = Toolkit.getDefaultToolkit().getScreenSize();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setTitle("Azul");
        setSize((int) (screen.width / 4), (int) (screen.height / 6));
        setLocation(screen.width / 2 - getWidth() / 2, screen.height / 2 - getHeight() / 2);

        namesText.setEnabled(false);
        statusText.setEnabled(false);

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0;
        add(new JLabel("Share ID:"), c);

        c.gridx = 1; c.gridy = 0;
        c.weightx = 0.8;
        add(gameIdText, c);

        c.gridx = 0; c.gridy = 1;
        c.weightx = 1.0;
        c.gridwidth = 2;
        add(statusText, c);

        c.gridx = 0; c.gridy = 2;
        c.weightx = 0.0;
        c.gridwidth = 1;
        add(new JLabel("Players in game:"), c);

        c.gridx = 1; c.gridy = 2;
        c.weightx = 1.0;
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
                        Thread.sleep(1000);
                        gameController.update();

                        var game = gameController.getGame();
                        statusText.setText(game.getState());

                        if(gameIdText.getText().isEmpty())
                            gameIdText.setText(game.getId().toString());

                        var players = gameController.getPlayers();
                        namesText.setText(String.join(", ", players.stream()
                                .map(t -> t.getName()).collect(Collectors.toList())));

                        if (game.getState().equalsIgnoreCase("READY")){
                            startGame();
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }
    public void startGame(){
        var gameForm = new GameForm();
        gameForm.setVisible(true);
        setVisible(false);
    }
}
