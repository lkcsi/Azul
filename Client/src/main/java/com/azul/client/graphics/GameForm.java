package com.azul.client.graphics;

import com.azul.client.controllers.GameController;
import com.azul.client.dtos.PlayerDto;
import com.azul.client.models.Wall;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class GameForm extends JFrame {

    JTextField nameTextField = new JTextField();
    JTextField scoreTextField = new JTextField();
    JTextField currentNameTextField = new JTextField();
    JTextField scoresTextField = new JTextField();
    ArrayList<ArrayList<JButton>> wallButtons = new ArrayList<ArrayList<JButton>>();
    ArrayList<ArrayList<JButton>> patternLineButtons = new ArrayList<ArrayList<JButton>>();

    String playerName;
    int playerScore;

    PlayerDto currentPlayer;

    public GameForm(String playerName){

        this.playerName = playerName;
        currentNameTextField.setEnabled(false);
        scoresTextField.setEnabled(false);
        scoreTextField.setEnabled(false);
        nameTextField.setEnabled(false);


        var screen = Toolkit.getDefaultToolkit().getScreenSize();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setTitle("Azul");
        setSize((int)(screen.width / 2), (int)(screen.height / 2));
        setLocation(screen.width / 2 - getWidth() / 2, screen.height / 2 - getHeight() / 2);

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0; c.gridy = 0;
        c.gridwidth = 2;
        add(infoPanel(), c);

        var dimension = new Dimension((int)(getHeight() / 1.5),(int)(getHeight() / 1.5));

        c.fill = GridBagConstraints.CENTER;
        c.gridx = 0; c.gridy = 1; c.weightx = 0.5; c.weighty = 0.5;
        c.gridwidth = 1;
        var patternLine = getPatternLine();
        patternLine.setPreferredSize(dimension);
        add(patternLine, c);

        c.fill = GridBagConstraints.CENTER;
        c.gridx = 1; c.gridy = 1; c.weightx = 0.5; c.weighty = 0.5;
        var wall = getWall();
        wall.setPreferredSize(dimension);
        add(wall, c);

        Thread t = new Thread(update());
        //t.start();
    }

    public JPanel infoPanel(){
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0; c.gridx = 0;
        infoPanel.add(new JLabel("Your Name:"), c);

        c.gridy = 0; c.gridx = 1; c.weightx = 1.0;
        infoPanel.add(nameTextField, c);

        c.gridy = 1; c.gridx = 0; c.weightx = 0.0;
        infoPanel.add(new JLabel("Your Score:"), c);

        c.gridy = 1; c.gridx = 1; c.weightx = 1.0;
        infoPanel.add(scoreTextField, c);

        c.gridy = 2; c.gridx = 0; c.weightx = 0.0;
        infoPanel.add(new JLabel("Current player:"), c);

        c.gridy = 2; c.gridx = 1; c.weightx = 1.0;
        infoPanel.add(currentNameTextField, c);

        c.gridy = 3; c.gridx = 0; c.gridwidth = 2;
        infoPanel.add(scoresTextField, c);

        return infoPanel;
    }


    public JPanel getPatternLine(){
        JPanel patternLine = new JPanel();

        patternLine.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;

        for(int i = 0; i < 5; i++) {
            patternLineButtons.add(new ArrayList<JButton>());
        }

        for(int y = 4; y >= 0; y--){
            ArrayList<JButton> line = new ArrayList<JButton>();
            int min = (4 - y);
            for(int x = 4; x >= min; x--){
                c.gridy = y; c.gridx = x;
                c.weightx = 0.2; c.weighty = 0.2;
                var button = new JButton();
                button.setBackground(Color.LIGHT_GRAY);
                final int lineIndex = y;
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        patternLineClick(lineIndex);
                    }
                });
                line.add(button);
                patternLine.add(button, c);
            }
        }

        return patternLine;
    }

    public JPanel getWall(){

        JPanel wallPanel = new JPanel();
        wallPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        wallPanel.setLayout(new GridBagLayout());

        var wall = Wall.getInstance();
        wallButtons.addAll(Collections.nCopies(5, new ArrayList<JButton>(Collections.nCopies(5, null))));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;

        for(int y = 4; y >= 0; y--){
            for(int x = 4; x >= 0; x--){
                c.gridy = y; c.gridx = x;
                c.weightx = 0.2; c.weighty = 0.2;

                var button = new JButton();
                button.setBackground(wall.get(y).get(x).darker());
                wallButtons.get(y).set(x, button);

                final int line = y;
                final int row = x;
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        wallClick(line,row);
                    }
                });

                wallPanel.add(button, c);
            }
        }

        return wallPanel;
    }

    public Runnable update(){
        return new Runnable() {
            @Override
            public void run() {
                while(true){
                    var game = GameController.getGame();
                    var players = GameController.getPlayers();

                    currentPlayer = players.stream()
                            .filter(p -> p.getName().equalsIgnoreCase(game.getCurrentPlayer()))
                            .findFirst()
                            .get();

                    currentNameTextField.setText(currentPlayer.getName());
                    scoreTextField.setText(String.valueOf(currentPlayer.getScore()));
                    scoresTextField.setText(String.join(" ", players.stream()
                            .map(p -> p.getName() + "=" + p.getScore())
                            .collect(Collectors.toList())));

                    updateWall(currentPlayer);
                    updatePatternLine(currentPlayer);

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    public void updateWall(PlayerDto playerDto){
        var playerWall = playerDto.getWall();
        var wall = Wall.getInstance();

        playerWall.forEach((line) -> {
            int lineIndex = playerWall.indexOf(line);
            line.forEach( (t) -> {
                int columnIndex = line.indexOf(t);

                Color color = wall.get(lineIndex).get(columnIndex);
                if(t.equalsIgnoreCase("Empty")) {
                    color = color.darker();
                }
                wallButtons.get(lineIndex).get(columnIndex).setBackground(color);
            });
        });
    }

    public void updatePatternLine(PlayerDto playerDto){
        var playerPattern = playerDto.getPatternLines();

        playerPattern.forEach((line) -> {
            int lineIndex = playerPattern.indexOf(line);
            line.forEach( (t) -> {
                Color color = Color.GRAY;
                if(!playerPattern.get(lineIndex).get(0).equalsIgnoreCase("Empty")){
                    color = Color.getColor(playerPattern.get(lineIndex).get(0));
                }
                int columnIndex = line.indexOf(t);
                patternLineButtons.get(lineIndex).get(columnIndex).setBackground(color);
            });
        });
    }

    public void wallClick(int line, int row){
        System.out.println(line +";"+ row);
    }

    public void patternLineClick(int line){

    }
}
