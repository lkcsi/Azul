package com.azul.client.graphics;

import com.azul.client.controllers.GameController;
import com.azul.client.dtos.*;
import com.azul.client.models.TileColor;
import com.azul.client.models.Wall;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class GameForm extends JFrame {

    private final Dimension tileSize;
    JTextField nameTextField = new JTextField();
    JTextField scoreTextField = new JTextField();
    JTextField currentNameTextField = new JTextField();
    JTextField scoresTextField = new JTextField();
    JTextField choiceTextField = new JTextField();
    ArrayList<ArrayList<JButton>> wallButtons = new ArrayList<>();
    ArrayList<ArrayList<JButton>> patternLineButtons = new ArrayList<>();
    ArrayList<ArrayList<JButton>> factoryButtons = new ArrayList<>();
    ArrayList<JButton> centerButtons = new ArrayList<>();
    ArrayList<JButton> floorButtons = new ArrayList<>();

    private ArrayList<PlayerDto> players;
    private ArrayList<FactoryDto> factories;
    private FactoryDto center;
    private GameDto game;

    private final String playerName;
    private final int numberOfFactories;
    private PlayerDto currentPlayer;
    private String selectedColorName;
    private Integer selectedFactoryId;
    private boolean centerSelected = false;
    private PlayerDto player;

    public GameForm(String playerName, int numberOfPlayers){

        this.playerName = playerName;
        numberOfFactories = numberOfPlayers * 2 + 1;
        currentNameTextField.setEnabled(false);
        scoresTextField.setEnabled(false);
        scoreTextField.setEnabled(false);
        nameTextField.setEnabled(false);
        choiceTextField.setEnabled(false);

        nameTextField.setText(playerName);

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
        add(getFactories(), c);

        c.fill = GridBagConstraints.CENTER;
        c.gridx = 0; c.gridy = 4; c.weightx = 1.0; c.weighty = 0.2;
        add(getCenter(), c);

        Thread t = new Thread(update());
        t.start();
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

        c.gridy = 3; c.gridx = 0; c.weightx = 0.0;
        infoPanel.add(new JLabel("Standing:"), c);

        c.gridy = 3; c.gridx = 1; c.weightx = 1.0;
        infoPanel.add(scoresTextField, c);

        return infoPanel;
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
            patternLineButtons.set(y, line);
        }

        return patternLine;
    }

    public JPanel getWall(){

        JPanel wallPanel = new JPanel();
        wallPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        wallPanel.setLayout(new GridBagLayout());

        var wall = Wall.getInstance();
        for(int i = 0 ; i < 5 ; i++){
            ArrayList<JButton> buttons = new ArrayList<>();
            for(int j = 0; j < 5 ; j++){
                buttons.add(new JButton());
            }
            wallButtons.add(buttons);
        }

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
                button.setBackground(wall.get(y).get(x).getColor());
                wallButtons.get(y).set(x, button);

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

            floorButtons.add(button);
            floor.add(button, c);
        }
        return floor;
    }

    public JPanel getCenter(){
        JPanel result = new JPanel();
        result.setLayout(new GridLayout(2,14));

        for(int i = 0; i < 28; i++){
            var button = new JButton();
            button.setPreferredSize(tileSize);
            button.setBackground(Color.LIGHT_GRAY);
            button.setEnabled(false);

            final int tileId = i;
            button.addActionListener(actionEvent -> {
                centerClick(tileId);
            });
            centerButtons.add(button);
            result.add(button);
        }
        return result;
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
                button.setEnabled(false);

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
                try {
                    Thread.sleep(2000);

                    game = GameController.getGame();
                    players = GameController.getPlayers();
                    factories = GameController.getFactories();
                    center = GameController.getCenter();
                    currentPlayer = players.stream()
                            .filter(p -> p.getName().equalsIgnoreCase(game.getCurrentPlayer()))
                            .findFirst()
                            .get();

                    player = players.stream()
                            .filter(p -> p.getName().equalsIgnoreCase(playerName))
                            .findFirst()
                            .get();

                    updateInfo();
                    updateWall();
                    updatePatternLine();
                    updateFloor();
                    updateFactories();
                    updateCenter();

                } catch (Exception e) {
                    System.out.println("ERROR: " + e.getMessage());
                }
            }
        };
    }

    public void updateInfo(){
        if(currentPlayer == null || players == null){
            return;
        }

        currentNameTextField.setText(currentPlayer.getName());
        scoreTextField.setText(String.valueOf(currentPlayer.getScore()));
        scoresTextField.setText(String.join(" ", players.stream()
                .map(p -> p.getName() + "=" + p.getScore())
                .collect(Collectors.toList())));
    }

    public void updateWall(){
        if(player == null){
            return;
        }

        var playerWall = player.getWall();
        var wall = Wall.getInstance();

        System.out.println("Size: " + playerWall.size());
        int lineIndex = 0;
        for(var line : playerWall) {
            int columnIndex = 0;
            for(var tile : line) {
                var button = wallButtons.get(lineIndex).get(columnIndex);
                if(tile.equalsIgnoreCase("Empty"))
                    button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                else
                    button.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
                columnIndex += 1;
            }
            lineIndex += 1;
        }
    }

    public void updatePatternLine(){
        if(player == null){
            return;
        }

        var playerPattern = player.getPatternLines();
        for(var line : playerPattern) {
            int lineIndex = playerPattern.indexOf(line);
            int columnIndex = line.size() - 1;
            for(var tile : line) {
                Color color = Color.LIGHT_GRAY;
                if(!tile.equalsIgnoreCase("empty")){
                    color = TileColor.valueOf(tile).getColor();
                }
                patternLineButtons.get(lineIndex).get(columnIndex).setBackground(color);
                columnIndex -= 1;
            }
        }
    }

    public void updateFloor(){
        if(player == null){
            return;
        }

        var floor = player.getFloor();
        int index = 0;
        for(var button : floorButtons){
            if(index >= floor.size()){
                if(button.isEnabled()){
                    button.setEnabled(false);
                    button.setBackground(Color.LIGHT_GRAY);
                }
            }
            else{
                var tile = floor.get(index);
                var color = TileColor.valueOf(tile).getColor();
                if(!button.isEnabled()){
                    button.setEnabled(true);
                }
                if(button.getBackground() != color){
                    button.setBackground(color);
                }
            }
            index += 1;
        }
    }

    public void updateCenter(){
        if(center == null){
            return;
        }
        int index = 0;
        var centerTiles = center.getTiles();
        for(var button : centerButtons){
            if(index >= centerTiles.size()){
                if(button.isEnabled()){
                    button.setEnabled(false);
                    button.setBackground(Color.LIGHT_GRAY);
                }
            }
            else{
                var tile = centerTiles.get(index);
                Color color = TileColor.valueOf(tile).getColor();
                if(color != button.getBackground()){
                    button.setBackground(color);
                }
                if(!button.isEnabled()){
                    button.setEnabled(true);
                }
            }
            index += 1;
        }
    }

    public void updateFactories(){
        if(factories == null){
            return;
        }

        for(var buttons : factoryButtons){
            int factoryIndex = this.factoryButtons.indexOf(buttons);
            var factory = factories.get(factoryIndex);
            for(var button : buttons){
                var tiles = factory.getTiles();
                int tileIndex = buttons.indexOf(button);
                if(tileIndex >= tiles.size()){
                    if(button.isEnabled()){
                        button.setEnabled(false);
                        button.setBackground(Color.LIGHT_GRAY);
                    }
                    continue;
                }
                var tile = tiles.get(tileIndex);
                if(!button.isEnabled()){
                    button.setEnabled(true);
                    button.setVisible(true);
                    Color color = TileColor.valueOf(tile).getColor();
                    button.setBackground(color);
                }
            }
        }
    }

    public void patternLineClick(int line){
        if(selectedColorName == null || selectedFactoryId == null){
            return;
        }
        var pickForm = new PickForm();
        pickForm.setColor(selectedColorName);
        pickForm.setLineNumber(line);
        pickForm.setPlayerName(playerName);
        pickForm.setFactoryId(selectedFactoryId);

        Code result;

        if(centerSelected){
            result = GameController.pickFromCenter(pickForm);
        }
        else {
            result = GameController.pickFromFactory(pickForm);
        }
        if(result.getCode() != Code.SUCCESS.getCode()){
            JOptionPane.showMessageDialog(null, result.getDescription(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        selectedColorName = null;
        selectedFactoryId = null;
        centerSelected = false;
    }

    public void factoryClick(int factoryId, int tileId){
        if(factories == null){
            return;
        }
        selectedFactoryId = factoryId;
        var color = factories.get(factoryId).getTiles().get(tileId);
        selectedColorName = color;
    }

    public void centerClick(int tileId){
        if(center == null){
            return;
        }
        centerSelected = true;
        selectedFactoryId = tileId;
        selectedColorName = center.getTiles().get(tileId);
    }
}
