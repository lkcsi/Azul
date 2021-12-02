package com.azul.client.graphics;

import com.azul.client.controllers.GameController;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GameForm extends JFrame {

    GameController game = GameController.getInstance();
    private ArrayList<Updatable> updatables = new ArrayList<>();

    private final Dimension tileSize;

    public GameForm(){
        setTitle("Azul");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        var screen = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int)(screen.width / 1.5), (int)(screen.height / 1.5));
        setLocation(screen.width / 2 - getWidth() / 2, screen.height / 2 - getHeight() / 2);

        tileSize = new Dimension(getWidth() /30, getWidth()/ 30);

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0; c.gridy = 0;
        c.gridwidth = 2;
        var info = new InfoPanel();
        updatables.add(info);
        add(info, c);

        c.fill = GridBagConstraints.CENTER;
        c.gridx = 0; c.gridy = 1; c.weightx = 0.5; c.weighty = 0.5;
        c.gridwidth = 1;

        var patternLines = new PatternLinePanel(tileSize);
        updatables.add(patternLines);
        add(patternLines, c);

        c.fill = GridBagConstraints.CENTER;
        c.gridx = 1; c.gridy = 1; c.weightx = 0.5; c.weighty = 0.5;

        var wall = new WallPanel(tileSize);
        updatables.add(wall);
        add(wall, c);

        c.fill = GridBagConstraints.WEST;
        c.gridx = 0; c.gridy = 2; c.weightx = 0.5; c.weighty = 0.2;

        var floor = new FloorPanel(tileSize);
        updatables.add(floor);
        add(floor, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0; c.gridy = 3; c.weightx = 1.0; c.weighty = 0.2;
        c.gridwidth = 2;

        var factories = new FactoriesPanel(tileSize);
        updatables.add(factories);
        add(factories, c);

        c.fill = GridBagConstraints.CENTER;
        c.gridx = 0; c.gridy = 4; c.weightx = 1.0; c.weighty = 0.2;
        var center = new CenterPanel(tileSize);
        updatables.add(center);
        add(center, c);

        BagForm bag = new BagForm(tileSize, game.getBag());
        bag.setVisible(true);
        updatables.add(bag);

        BagForm drop = new BagForm(tileSize, game.getDrop());
        drop.setVisible(true);
        updatables.add(drop);

        Thread t = new Thread(update());
        t.start();
    }

    private Runnable update(){
        return new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(1000);
                        game.update();
                        updatables.forEach(panel -> panel.update());
                    } catch (InterruptedException e) {
                        System.out.println("Could not update");
                    }
                }
            }
        };
    }

}
