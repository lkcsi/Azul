package com.azul.client.graphics;

import com.azul.client.controllers.GameController;
import com.azul.client.models.Wall;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;

public class WallPanel extends JPanel implements Updatable {
    GameController game = GameController.getInstance();
    ArrayList<ArrayList<JButton>> wallButtons = new ArrayList<>();
    Border noBorder = BorderFactory.createLineBorder(Color.BLACK, 1);
    Border greenBorder = BorderFactory.createLineBorder(Color.GREEN, 2);

    public WallPanel(Dimension tileSize){
        setBorder(BorderFactory.createLineBorder(Color.black));
        setLayout(new GridBagLayout());

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

                add(button, c);
            }
        }
    }

    @Override
    public void update(){
        if(game.getPlayer() == null){
            return;
        }

        var playerWall = game.getPlayer().getWall();
        var wall = Wall.getInstance();

        int lineIndex = 0;
        for(var line : playerWall) {
            int columnIndex = 0;
            for(var tile : line) {
                var button = wallButtons.get(lineIndex).get(columnIndex);
                if(tile.equalsIgnoreCase("Empty")){
                    if(button.getBorder() != noBorder)
                        button.setBorder(noBorder);
                }
                else if(button.getBorder() != greenBorder){
                    button.setBorder(greenBorder);
                }
                columnIndex += 1;
            }
            lineIndex += 1;
        }
    }
}
