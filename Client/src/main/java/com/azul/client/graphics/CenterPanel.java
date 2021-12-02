package com.azul.client.graphics;

import com.azul.client.controllers.GameController;
import com.azul.client.models.TileColor;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CenterPanel extends JPanel implements Updatable {
    GameController game = GameController.getInstance();
    ArrayList<JButton> centerButtons = new ArrayList<>();

    public CenterPanel(Dimension tileSize){
        setLayout(new GridLayout(2,14));

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
            add(button);
        }
    }
    @Override
    public void update(){
        if(game.getCenter() == null){
            return;
        }
        int index = 0;
        var centerTiles = game.getCenter().getTiles();
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

    public void centerClick(int tileId){
        if(game.getCenter() == null){
            return;
        }
        game.setCenterSelected(true);
        game.setSelectedFactoryId(0);
        game.setSelectedColorName (game.getCenter().getTiles().get(tileId));
    }
}
