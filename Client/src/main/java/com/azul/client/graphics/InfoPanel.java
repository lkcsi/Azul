package com.azul.client.graphics;

import com.azul.client.controllers.GameController;

import javax.swing.*;
import java.awt.*;
import java.util.stream.Collectors;

public class InfoPanel extends JPanel implements Updatable {
    GameController game = GameController.getInstance();
    JTextField nameTextField = new JTextField();
    JTextField scoreTextField = new JTextField();
    JTextField currentNameTextField = new JTextField();
    JTextField scoresTextField = new JTextField();

    @Override
    public void update(){
        game.update();
        if(game.getCurrentPlayer() == null || game.getPlayer() == null){
            return;
        }

        currentNameTextField.setText(game.getCurrentPlayer().getName());
        scoreTextField.setText(String.valueOf(game.getCurrentPlayer().getScore()));
        scoresTextField.setText(String.join(" ", game.getPlayers().stream()
                .map(p -> p.getName() + "=" + p.getScore())
                .collect(Collectors.toList())));
    }
    public InfoPanel(){
        currentNameTextField.setEnabled(false);
        scoresTextField.setEnabled(false);
        scoreTextField.setEnabled(false);
        nameTextField.setEnabled(false);
        nameTextField.setText(game.getPlayer().getName());

        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0; c.gridx = 0;
        add(new JLabel("Your Name:"), c);

        c.gridy = 0; c.gridx = 1; c.weightx = 1.0;
        add(nameTextField, c);

        c.gridy = 1; c.gridx = 0; c.weightx = 0.0;
        add(new JLabel("Your Score:"), c);

        c.gridy = 1; c.gridx = 1; c.weightx = 1.0;
        add(scoreTextField, c);

        c.gridy = 2; c.gridx = 0; c.weightx = 0.0;
        add(new JLabel("Current player:"), c);

        c.gridy = 2; c.gridx = 1; c.weightx = 1.0;
        add(currentNameTextField, c);

        c.gridy = 3; c.gridx = 0; c.weightx = 0.0;
        add(new JLabel("Standing:"), c);

        c.gridy = 3; c.gridx = 1; c.weightx = 1.0;
        add(scoresTextField, c);
    }
}
