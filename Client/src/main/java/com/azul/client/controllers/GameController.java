package com.azul.client.controllers;

import com.azul.client.dtos.*;
import com.google.gson.Gson;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class GameController {

    private static GameController instance;
    public static GameController getInstance(){
        if(instance == null)
            instance = new GameController();
        return instance;
    }

    private ArrayList<PlayerDto> players;
    private ArrayList<FactoryDto> factories;
    private FactoryDto center;
    private PlayerDto currentPlayer;
    private PlayerDto player;
    private GameDto game;
    private TileCollectionDto bag;
    private TileCollectionDto drop;
    private String playerName;

    private boolean centerSelected;
    private Integer selectedFactoryId;
    private String selectedColorName;

    private String baseUrl;
    private boolean connected;

    public boolean connect(String url){
        baseUrl = url + "/api";
        connected = true;
        if(updateGame() == null) {
            connected = false;
        }
        return connected;
    }

    public Code registerPlayer(String playerName) {
        this.playerName = playerName;
        String result = request("players/new/" + playerName,
                "",
                "POST");

        System.out.println("Result: " + result);
        return new Gson().fromJson(result, Code.class);
    }

    private ArrayList<PlayerDto> updatePlayers(){
        String result = request("players",
                "",
                "GET");

        return new ArrayList<PlayerDto>(Arrays.asList(new Gson().fromJson(result, PlayerDto[].class)));
    }

    private ArrayList<FactoryDto> updateFactories(){
        String result = request("factories",
                "",
                "GET");

        return new ArrayList<FactoryDto>(Arrays.asList(new Gson().fromJson(result, FactoryDto[].class)));
    }
    private FactoryDto updateCenter(){
        try{
            String result = request("center", "", "GET");
            return new Gson().fromJson(result, FactoryDto.class);
        }
        catch (Exception ex){
            return null;
        }
    }
    private TileCollectionDto updateDrop(){
        try{
            String result = request("drop", "", "GET");
            return new Gson().fromJson(result, TileCollectionDto.class);
        }
        catch (Exception ex){
            return null;
        }
    }
    private TileCollectionDto updateBag(){
        try{
            String result = request("bag", "", "GET");
            return new Gson().fromJson(result, TileCollectionDto.class);
        }
        catch (Exception ex){
            return null;
        }
    }
    private GameDto updateGame(){
        try {
            String result = request("game", "", "GET");
            return new Gson().fromJson(result, GameDto.class);
        }
        catch (Exception e){
            return null;
        }
    }

    public Code pickFromFactory(PickForm form){
        try{
            String result = request("factories/pick", new Gson().toJson(form), "POST");
            return new Gson().fromJson(result, Code.class);
        }
        catch (Exception ex){
            return Code.UNKNOWN;
        }
    }

    public Code pickFromCenter(PickForm form){
        try {
            String result = request("center/pick", new Gson().toJson(form), "POST");
            return new Gson().fromJson(result, Code.class);
        }
        catch (Exception ex){
            return Code.UNKNOWN;
        }
    }

    private String request(String urlString, String jsonInputString, String requestMethod){
            try {
                URL url = new URL(baseUrl + "/" + urlString);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestProperty("Content-Type", "application/json; utf-8");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod(requestMethod);

                //Send
                if(!jsonInputString.isEmpty()){
                    con.setDoOutput(true);
                    try(OutputStream os = con.getOutputStream()) {
                        byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                        os.write(input, 0, input.length);
                    }
                }

                //Read
                try(BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))){
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while((responseLine = br.readLine()) != null){
                        response.append(responseLine.trim());
                    }
                   return response.toString();
                }
            } catch (Exception e) {
                return "";
            }
    }

    public void update(){
        if(!connected)
            return;

        try{
            game = updateGame();
            players = updatePlayers();
            factories = updateFactories();
            center = updateCenter();
            drop = updateDrop();
            bag = updateBag();
            currentPlayer = players.stream()
                    .filter(p -> p.getName().equalsIgnoreCase(game.getCurrentPlayer())) .findFirst() .get();

            if(playerName!=null){
                player = players.stream()
                        .filter(p -> p.getName().equalsIgnoreCase(playerName)) .findFirst() .get();
            }

            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
    }

    public Code sendPick(int line, boolean toFloor){
        if(selectedColorName == null ){
            return Code.NO_TILE_SELECTED;
        }if(selectedFactoryId == null){
            return Code.NO_FACTORY_SELECTED;
        }

        var pickForm = new PickForm();
        pickForm.setColor(selectedColorName);
        pickForm.setLineNumber(line);
        pickForm.setPlayerName(playerName);
        pickForm.setFactoryId(selectedFactoryId);
        pickForm.setToFloor(toFloor);

        Code result;

        if(centerSelected){
            result = pickFromCenter(pickForm);
        }
        else {
            result = pickFromFactory(pickForm);
        }

        selectedColorName = null;
        selectedFactoryId = null;
        centerSelected = false;

        return result;
    }

    public ArrayList<FactoryDto> getFactories() {
        return factories;
    }

    public FactoryDto getCenter() {
        return center;
    }

    public PlayerDto getCurrentPlayer() {
        return currentPlayer;
    }

    public PlayerDto getPlayer() {
        return player;
    }
    public ArrayList<PlayerDto> getPlayers(){
        return players;
    }

    public boolean isCenterSelected() {
        return centerSelected;
    }

    public void setCenterSelected(boolean centerSelected) {
        this.centerSelected = centerSelected;
    }

    public Integer getSelectedFactoryId() {
        return selectedFactoryId;
    }

    public void setSelectedFactoryId(Integer selectedFactoryId) {
        this.selectedFactoryId = selectedFactoryId;
    }

    public String getSelectedColorName() {
        return selectedColorName;
    }

    public void setSelectedColorName(String selectedColorName) {
        this.selectedColorName = selectedColorName;
    }

    public boolean isConnected(){
        return connected;
    }

    public String getStatus(){
        return game.getState();
    }

    public TileCollectionDto getBag() {
        return bag;
    }

    public TileCollectionDto getDrop() {
        return drop;
    }
}
