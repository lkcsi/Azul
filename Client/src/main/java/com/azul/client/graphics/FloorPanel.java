package com.azul.client.graphics;

import com.azul.client.controllers.GameController;
import com.azul.client.models.TileColor;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class FloorPanel extends JPanel implements Updatable{
    ArrayList<JButton> floorButtons = new ArrayList<>();
    GameController game = GameController.getInstance();
    public FloorPanel(Dimension tileSize){
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.WEST;
        int pad = tileSize.width / 20;
        c.insets = new Insets(0,pad,0,pad);
        for(int i = 0; i < 7 ; i++){
            c.gridx = i; c.weightx = 0.1;
            JButton button = new JButton();
            button.setBackground(Color.LIGHT_GRAY);
            button.setPreferredSize(tileSize);
            button.addActionListener(actionEvent -> floorClick());

            floorButtons.add(button);
            add(button, c);
        }
    }
    @Override
    public void update(){
        if(game.getPlayer() == null){
            return;
        }

        var floor = game.getPlayer().getFloor();
        int index = 0;
        for(var button : floorButtons){
            if(index >= floor.size()){
                if(button.getBackground() != Color.LIGHT_GRAY){
                    button.setBackground(Color.LIGHT_GRAY);
                }
            }
            else{
                var tile = floor.get(index);
                var color = TileColor.valueOf(tile).getColor();
                if(button.getBackground() != color){
                    button.setBackground(color);
                }
            }
            index += 1;
        }
    }

    private void floorClick() {
        try{
            game.sendPick(0, true);
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
