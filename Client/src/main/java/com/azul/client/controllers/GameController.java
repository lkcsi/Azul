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
import java.util.UUID;

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
    private UUID gameId;

    public void createGame(String url, String playerName, int players){
        try{
            baseUrl = url + "/api";
            game = newGame(players);
            gameId = game.getId();
            player = registerPlayer(playerName);
            this.playerName = playerName;
            connected = true;
        }catch (Exception e){
            connected = false;
            throw e;
        }
    }

    public void joinGame(String url, String playerName, UUID gameId){
        try{
            baseUrl = url + "/api";
            this.gameId = gameId;
            this.playerName = playerName;
            player = registerPlayer(playerName);
            connected = true;
        }catch (Exception e){
            connected = false;
            throw e;
        }
    }

    public GameDto newGame(int players) {
        String result = request("game/?players=" + players,
                "",
                "POST");

        return new Gson().fromJson(result, GameDto.class);
    }

    public PlayerDto registerPlayer(String playerName) {
        this.playerName = playerName;
        String result = request("player/?gameId=" + gameId + "&name="+ playerName,
                "",
                "POST");

        return new Gson().fromJson(result, PlayerDto.class);
    }

    private ArrayList<PlayerDto> updatePlayers(){
        String result = request("players/?gameId=" + gameId,
                "",
                "GET");

        return new ArrayList<PlayerDto>(Arrays.asList(new Gson().fromJson(result, PlayerDto[].class)));
    }

    private ArrayList<FactoryDto> updateFactories(){
        String result = request("factories/?gameId=" + gameId,
                "",
                "GET");

        return new ArrayList<FactoryDto>(Arrays.asList(new Gson().fromJson(result, FactoryDto[].class)));
    }
    private FactoryDto updateCenter(){
        String result = request("center/?gameId=" + gameId, "", "GET");
        return new Gson().fromJson(result, FactoryDto.class);
    }
    private TileCollectionDto updateDrop(){
        String result = request("drop/?gameId=" + gameId, "", "GET");
        return new Gson().fromJson(result, TileCollectionDto.class);
    }
    private TileCollectionDto updateBag(){
        String result = request("bag/?gameId=" + gameId, "", "GET");
        return new Gson().fromJson(result, TileCollectionDto.class);
    }
    private GameDto updateGame(){
        String result = request("game/?gameId=" + gameId, "", "GET");
        return new Gson().fromJson(result, GameDto.class);
    }

    public String pickFromFactory(PickForm form){
        String result = request("factory-pick/?gameId=" + gameId, new Gson().toJson(form), "POST");
        System.out.println(result);
        return result;
    }

    public String pickFromCenter(PickForm form){
        String result = request("center-pick/?gameId=" + gameId, new Gson().toJson(form), "POST");
        return result;
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

                InputStreamReader reader;
                if(con.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST)
                    reader = new InputStreamReader(con.getInputStream());
                else
                    reader = new InputStreamReader(con.getErrorStream());

                //Read
                try(BufferedReader br = new BufferedReader(reader)){
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while((responseLine = br.readLine()) != null){
                        response.append(responseLine.trim());
                    }
                   return response.toString();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return e.getMessage();
            }
    }

    public void update(){
        try{
            game = updateGame();
            players = updatePlayers();
            factories = updateFactories();
            center = updateCenter();
            drop = updateDrop();
            bag = updateBag();

            currentPlayer = players.stream()
                    .filter(p -> p.getName().equalsIgnoreCase(game.getCurrentPlayer()))
                    .findFirst()
                    .get();

            if(playerName!=null){
                player = players.stream()
                        .filter(p -> p.getName().equalsIgnoreCase(playerName))
                        .findFirst()
                        .get();
            }
        } catch (Exception e) {
            System.out.println("Update error: " + e.getMessage());
        }
    }

    public ErrorMessage sendPick(int line, boolean toFloor){
        if(selectedColorName == null ){
            throw new RuntimeException("No tile selected");
        }if(selectedFactoryId == null){
            throw new RuntimeException("No factory selected");
        }

        var pickForm = new PickForm();
        pickForm.setColor(selectedColorName);
        pickForm.setLineNumber(line);
        pickForm.setPlayerName(playerName);
        pickForm.setFactoryId(selectedFactoryId);
        pickForm.setToFloor(toFloor);

        String result;
        if(centerSelected){ result = pickFromCenter(pickForm); }
        else { result = pickFromFactory(pickForm); }

        selectedColorName = null;
        selectedFactoryId = null;
        centerSelected = false;

        if(result.isEmpty())
            return null;

        return new Gson().fromJson(result, ErrorMessage.class);
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

    public GameDto getGame(){
        return game;
    }

    public TileCollectionDto getBag() {
        return bag;
    }

    public TileCollectionDto getDrop() {
        return drop;
    }
}
