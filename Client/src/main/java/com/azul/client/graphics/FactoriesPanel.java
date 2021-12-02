package com.azul.client.graphics;

import com.azul.client.controllers.GameController;
import com.azul.client.models.TileColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class FactoriesPanel extends JPanel implements Updatable {
    ArrayList<ArrayList<JButton>> factoryButtons = new ArrayList<>();
    GameController game = GameController.getInstance();
    public FactoriesPanel(Dimension tileSize){
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.CENTER;

        for(int i = 0; i < game.getFactories().stream().count(); i++){
            c.gridx = i; c.weighty = 0.0; c.weightx = 0.1;
            add(getFactory(i, tileSize), c);
        }
    }

    public JPanel getFactory(int factoryId, Dimension tileSize){
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
    public void update(){
        if(game.getFactories() == null){
            return;
        }

        for(var buttons : factoryButtons){
            int factoryIndex = this.factoryButtons.indexOf(buttons);
            var factory = game.getFactories().get(factoryIndex);
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
    public void factoryClick(int factoryId, int tileId){
        if(game.getFactories() == null){
            return;
        }
        game.setSelectedFactoryId(factoryId);
        var color = game.getFactories().get(factoryId).getTiles().get(tileId);
        game.setSelectedColorName(color);
    }
}
