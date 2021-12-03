package com.azul.client.graphics;

import com.azul.client.controllers.GameController;
import com.azul.client.dtos.TileCollectionDto;
import com.azul.client.models.TileColor;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BagForm extends JFrame implements Updatable {
    GameController game = GameController.getInstance();
    ArrayList<JButton> bagButtons = new ArrayList<>();
    private TileCollectionDto tilesDto;

    public BagForm(Dimension tileSize, TileCollectionDto tilesDto){
        this.tilesDto = tilesDto;

        tileSize = new Dimension((int)tileSize.getWidth() / 10, (int)tileSize.getWidth() / 10);
        var screen = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(tileSize.width * 20, tileSize.width * 20);
        setLocation(screen.width / 2 - getWidth() / 2, screen.height / 2 - getHeight() / 2);

        setLayout(new GridLayout(10,10));
        for(int i = 0; i < 100; i++){
            var button = new JButton();
            button.setPreferredSize(tileSize);
            button.setBackground(Color.LIGHT_GRAY);
            button.setEnabled(false);
            bagButtons.add(button);
            add(button);
        }
    }
    @Override
    public void update(){
        if(tilesDto == null){
            return;
        }
        var bagTiles = tilesDto.getTiles();
        for(int i = 0; i < 100; i++){
            var button = bagButtons.get(i);
            if(i < bagTiles.size()) {
                var tile = bagTiles.get(i);
                var color = TileColor.valueOf(tile).getColor();
                if(button.getBackground() != color){
                    button.setBackground(color);
                }
                continue;
            }
            if(button.getBackground() != Color.LIGHT_GRAY){
                button.setBackground(Color.LIGHT_GRAY);
            }
        }
    }
}
