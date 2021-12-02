package com.azul.client.graphics;

import com.azul.client.controllers.GameController;
import com.azul.client.dtos.Code;
import com.azul.client.models.TileColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PatternLinePanel extends JPanel implements Updatable {
    ArrayList<ArrayList<JButton>> patternLineButtons = new ArrayList<>();
    GameController game = GameController.getInstance();

    public PatternLinePanel(Dimension tileSize){
        setLayout(new GridBagLayout());
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
                add(button, c);
            }
            patternLineButtons.set(y, line);
        }
    }

    @Override
    public void update(){
        if(game.getPlayer() == null){
            return;
        }

        var playerPattern = game.getPlayer().getPatternLines();
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

    public void patternLineClick(int line){
        Code result = game.sendPick(line, false);
        if(result.getCode() != Code.SUCCESS.getCode()){
            JOptionPane.showMessageDialog(null, result.getDescription(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
