package com.azul.client;

import com.azul.client.graphics.GameForm;
import com.azul.client.graphics.MainForm;

public class Main {
    public static void main(String[] args) {
        //var frame = new MainForm();
        var test = new GameForm("", 4);
        test.setVisible(true);
    }
}
