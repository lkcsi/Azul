package com.azul.client.graphics;

import com.azul.client.controllers.GameController;
import com.azul.client.dtos.FactoryDto;
import com.azul.client.dtos.GameDto;
import com.azul.client.dtos.PlayerDto;
import com.azul.client.models.Wall;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class GameForm extends JFrame {

    private final int numberOfFactories;
    private final Dimension tileSize;
    JTextField nameTextField = new JTextField();
    JTextField scoreTextField = new JTextField();
    JTextField currentNameTextField = new JTextField();
    JTextField scoresTextField = new JTextField();
    JTextField choiceTextField = new JTextField();
    ArrayList<ArrayList<JButton>> wallButtons = new ArrayList<>();
    ArrayList<ArrayList<JButton>> patternLineButtons = new ArrayList<>();
    ArrayList<ArrayList<JButton>> factoryButtons = new ArrayList<>();

    ArrayList<PlayerDto> players;
    ArrayList<FactoryDto> factories;
    GameDto game;


    String playerName;
    PlayerDto currentPlayer;

    public GameForm(String playerName, int numberOfPlayers){

        this.playerName = playerName;
        numberOfFactories = numberOfPlayers * 2 + 1;
        currentNameTextField.setEnabled(false);
        scoresTextField.setEnabled(false);
        scoreTextField.setEnabled(false);
        nameTextField.setEnabled(false);
        choiceTextField.setEnabled(false);


        var screen = Toolkit.getDefaultToolkit().getScreenSize();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setTitle("Azul");
        setSize((int)(screen.width / 1.5), (int)(screen.height / 1.5));
        setLocation(screen.width / 2 - getWidth() / 2, screen.height / 2 - getHeight() / 2);

        tileSize = new Dimension(getWidth() /30, getWidth()/ 30);

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0; c.gridy = 0;
        c.gridwidth = 2;
        add(infoPanel(), c);

        c.fill = GridBagConstraints.CENTER;
        c.gridx = 0; c.gridy = 1; c.weightx = 0.5; c.weighty = 0.5;
        c.gridwidth = 1;
        add(getPatternLine(), c);

        c.fill = GridBagConstraints.CENTER;
        c.gridx = 1; c.gridy = 1; c.weightx = 0.5; c.weighty = 0.5;
        add(getWall(), c);

        c.fill = GridBagConstraints.WEST;
        c.gridx = 0; c.gridy = 2; c.weightx = 0.5; c.weighty = 0.2;
        add(getFloor(), c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0; c.gridy = 3; c.weightx = 1.0; c.weighty = 0.2;
        c.gridwidth = 2;
        var factories = getFactories();
        add(factories, c);

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

        c.gridy = 0; c.gridx = 1; c.weightx = 1.0; c.gridwidth = 2;
        infoPanel.add(nameTextField, c);

        c.gridy = 1; c.gridx = 0; c.weightx = 0.0; c.gridwidth = 1;
        infoPanel.add(new JLabel("Your Score:"), c);

        c.gridy = 1; c.gridx = 1; c.weightx = 1.0; c.gridwidth = 2;
        infoPanel.add(scoreTextField, c);

        c.gridy = 2; c.gridx = 0; c.weightx = 0.0; c.gridwidth = 1;
        infoPanel.add(new JLabel("Current player:"), c);

        c.gridy = 2; c.gridx = 1; c.weightx = 1.0; c.gridwidth = 2;
        infoPanel.add(currentNameTextField, c);

        c.gridy = 3; c.gridx = 0; c.weightx = 0.0; c.gridwidth = 1;
        infoPanel.add(new JLabel("Standing:"), c);

        c.gridy = 3; c.gridx = 1; c.weightx = 1.0; c.gridwidth = 2;
        infoPanel.add(scoresTextField, c);

        c.gridy = 4; c.gridx = 0; c.weightx = 0.2; c.gridwidth = 1;
        infoPanel.add(new JLabel("Your choice:"), c);

        c.gridy = 4; c.gridx = 1; c.weightx = 1.0;
        infoPanel.add(choiceTextField, c);

        var submit = new JButton();
        submit.addActionListener(actionEvent -> {
            choice();
        });
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 4; c.gridx = 2; c.weightx = 0.2; c.weighty = 0.1;
        infoPanel.add(submit, c);

        return infoPanel;
    }

    private void choice() {
    }


    public JPanel getPatternLine(){
        JPanel patternLine = new JPanel();

        patternLine.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;

        for(int i = 0; i < 5; i++) {
            patternLineButtons.add(new ArrayList<>());
        }

        for(int y = 4; y >= 0; y--){
            ArrayList<JButton> line = new ArrayList<>();
            int min = (4 - y);
            for(int x = 4; x >= min; x--){
                c.gridy = y; c.gridx = x;
                c.weightx = 0.2; c.weighty = 0.2;
                int pad = tileSize.width / 20;
                c.insets = new Insets(pad,pad,pad,pad);

                var button = new JButton();
                button.setBackground(Color.LIGHT_GRAY);
                button.setPreferredSize(tileSize);
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
                int pad = tileSize.width / 20;
                c.insets = new Insets(pad,pad,pad,pad);

                var button = new JButton();
                button.setPreferredSize(tileSize);
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

    public JPanel getFloor(){
        JPanel floor = new JPanel();

        floor.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.WEST;
        int pad = tileSize.width / 20;
        c.insets = new Insets(0,pad,0,pad);
        for(int i = 0; i < 7 ; i++){
            c.gridx = i; c.weightx = 0.1;
            JButton button = new JButton();
            button.setBackground(Color.LIGHT_GRAY);
            button.setPreferredSize(tileSize);

            floor.add(button, c);
        }
        return floor;
    }

    public JPanel getFactories(){
        JPanel factories = new JPanel();

        factories.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.CENTER;

        for(int i = 0; i < numberOfFactories; i++){
            c.gridx = i; c.weighty = 0.0; c.weightx = 0.1;
            factories.add(getFactory(i), c);
        }

        return factories;
    }

    public JPanel getFactory(int factoryId){
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        ArrayList<JButton> factory = new ArrayList<>();

        for(int i = 0; i < 2; i++){
            for(int j = 0; j < 2; j++){
                c.gridy = i; c.gridx = j;
                int pad = tileSize.width / 20;
                c.insets = new Insets(pad,pad,pad,pad);

                var button = new JButton();
                button.setBackground(Color.LIGHT_GRAY);
                button.setPreferredSize(tileSize);

                int tileId = factory.size();

                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        factoryClick(factoryId, tileId);
                    }
                });
                factory.add(button);
                panel.add(button, c);
                panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            }
        }

        factoryButtons.add(factory);

        return panel;
    }

    public Runnable update(){
        return () -> {
            while(true){
                System.out.println("Update");

                try {
                    game = GameController.getGame();
                    players = GameController.getPlayers();
                    factories = GameController.getFactories();
                    currentPlayer = players.stream()
                            .filter(p -> p.getName().equalsIgnoreCase(game.getCurrentPlayer()))
                            .findFirst()
                            .get();

                    updateInfo();
                    updateWall();
                    updatePatternLine();
                    updateFactories();

                    Thread.sleep(2000);
                } catch (Exception e) {
                    System.out.println("ERROR: " + e.getMessage());
                }
            }
        };
    }

    public void updateInfo(){

        currentNameTextField.setText(currentPlayer.getName());
        scoreTextField.setText(String.valueOf(currentPlayer.getScore()));
        scoresTextField.setText(String.join(" ", players.stream()
                .map(p -> p.getName() + "=" + p.getScore())
                .collect(Collectors.toList())));
    }

    public void updateWall(){
        var playerWall = currentPlayer.getWall();
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

    public void updatePatternLine(){
        var playerPattern = currentPlayer.getPatternLines();

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

    public void updateFactories(){
        for(var factory : factories){
            var buttons = factoryButtons.get(factories.indexOf(factory));
            buttons.forEach((b) -> {
                b.setVisible(false);
                b.setEnabled(false);
            });

            var tiles = factory.getTiles();
            for(var tile : tiles){
                int index = tiles.indexOf(tile);
                var button = buttons.get(index);
                Color color = Color.getColor(tile);
                button.setEnabled(true);
                button.setVisible(true);
            }
        }
    }

    public void wallClick(int line, int row){
        System.out.println("Wall: " + line +";"+ row);
    }

    public void patternLineClick(int line){
        System.out.println("Pattern: " + line);
    }

    public void factoryClick(int factoryId, int tileId){
        System.out.println("Factory: " + factoryId +";"+ tileId);
    }
}
