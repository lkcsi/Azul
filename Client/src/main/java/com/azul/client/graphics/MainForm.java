package com.azul.client.graphics;

import com.azul.client.controllers.GameController;
import com.azul.client.dtos.Code;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.stream.Collectors;

public class MainForm extends JFrame{

    private static Dimension screen;
    private GameController game = GameController.getInstance();

    public MainForm(){

        screen = Toolkit.getDefaultToolkit().getScreenSize();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setTitle("Azul");
        setSize((int)(screen.width / 4), (int)(screen.height / 6));
        setLocation(screen.width / 2 - getWidth() / 2, screen.height / 2 - getHeight() / 2);

        JLabel statusLabel = new JLabel("Status: ");
        JTextField statusText = new JTextField("Unknown");
        statusText.setEnabled(false);

        JLabel playersLabel = new JLabel("Players: ");
        JTextField playersText = new JTextField("Unknown");
        playersText.setEnabled(false);

        JTextField name = new JTextField("Name");
        name.setHorizontalAlignment(SwingConstants.CENTER);
        name.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                name.setText("");
            }
        });

        Button join = new Button("Join Game");
        join.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(!game.isConnected()){
                    JOptionPane.showMessageDialog(null, "Not connected to server", "Error", JOptionPane.ERROR_MESSAGE);
                }

                var result = game.registerPlayer(name.getText());
                if(result.getCode() == Code.SUCCESS.getCode()){
                    var form = new WaitingForm(name.getText());
                    form.setVisible(true);
                    setVisible(false);
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


        JButton connect = new JButton("Connect");
        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                game.connect("http://localhost:8080");
                statusText.setText("Conneted: " + game.isConnected());

                if(!game.isConnected()) {
                    JOptionPane.showMessageDialog(null, "Cannot connect to server", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                game.update();
                var players = game.getPlayers();
                playersText.setText(String.join(", ", players.stream()
                        .map(t -> t.getName()).collect(Collectors.toList())));
            }
        });

        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0; c.gridy = 0;
        add(statusLabel, c);

        c.gridx = 1; c.gridy = 0;
        c.weightx = 0.5;
        add(statusText, c);

        c.gridx = 0; c.gridy = 1;
        add(playersLabel, c);

        c.gridx = 1; c.gridy = 1;
        c.weightx = 0.5;
        add(playersText, c);

        c.gridx = 0; c.gridy = 2;
        c.gridwidth = 2;
        add(connect, c);

        c.gridx = 0; c.gridy = 3;
        c.gridwidth = 2;
        add(new Label(""), c);

        c.gridx = 0; c.gridy = 4;
        c.gridwidth = 2;
        add(name, c);

        c.gridx = 0; c.gridy = 5;
        add(join, c);

        c.gridx = 0; c.gridy = 6;
        add(exit, c);

        setVisible(true);
    }
}
