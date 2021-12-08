package com.azul.client.graphics;

import com.azul.client.controllers.GameController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.UUID;
import java.util.stream.Collectors;

public class MainForm extends JFrame{

    private static Dimension screen;
    private GameController gameController = GameController.getInstance();

    public MainForm(){

        screen = Toolkit.getDefaultToolkit().getScreenSize();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setTitle("Azul");
        setSize((int)(screen.width / 4), (int)(screen.height / 6));
        setLocation(screen.width / 2 - getWidth() / 2, screen.height / 2 - getHeight() / 2);

        JTextField nameText = new JTextField("Vazul");

        JTextField urlText = new JTextField("http://localhost:8080");

        JTextField joinIdText = new JTextField("");

        JButton joinButton = new JButton("Join Game");
        joinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                try{
                    gameController.joinGame(urlText.getText(), nameText.getText(), UUID.fromString(joinIdText.getText()));
                    var waitingRoom = new WaitingForm(nameText.getText());
                    waitingRoom.setVisible(true);
                    setVisible(false);
                }
                catch (Exception e){
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton exit = new JButton("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });

        JTextField numberOfPlayerText = new JTextField("2");

        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try{
                    gameController.createGame(urlText.getText(), nameText.getText(), Integer.valueOf(numberOfPlayerText.getText()));
                    var waitingRoom = new WaitingForm(nameText.getText());
                    waitingRoom.setVisible(true);
                }catch (Exception e){
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        });

        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0; c.gridy = 0;
        add(new JLabel("URL:"), c);

        c.gridx = 1; c.gridy = 0;
        c.weightx = 1.0;
        c.gridwidth = 2;
        add(urlText, c);

        c.gridx = 0; c.gridy = 1;
        c.weightx = 0.0;
        c.gridwidth = 1;
        add(new JLabel("Name:"), c);

        c.gridx = 1; c.gridy = 1;
        c.weightx = 1.0;
        c.gridwidth = 2;
        add(nameText, c);

        c.gridx = 0; c.gridy = 2;
        c.weightx = 0.0;
        c.gridwidth = 1;
        add(new JLabel("Number of players:"), c);

        c.gridx = 1; c.gridy = 2;
        c.weightx = 0.5;
        add(numberOfPlayerText, c);

        c.gridx = 2; c.gridy = 2;
        add(newGameButton, c);

        c.gridx = 0; c.gridy = 3;
        c.weightx = 0.0;
        c.gridwidth = 1;
        add(new JLabel("Game ID:"), c);

        c.gridx = 1; c.gridy = 3;
        c.weightx = 0.5;
        add(joinIdText, c);

        c.gridx = 2; c.gridy = 3;
        c.gridwidth = 2;
        add(joinButton, c);

        c.gridx = 0; c.gridy = 4;
        c.gridwidth = 3;
        add(exit, c);

        setVisible(true);
    }
}
