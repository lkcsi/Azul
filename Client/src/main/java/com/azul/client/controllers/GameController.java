package com.azul.client.controllers;

import com.azul.client.dtos.*;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class GameController {

    private static String baseUrl = "http://localhost:8080/api";

    public static Code registerPlayer(String playerName) {
        String result = request("players/new/" + playerName,
                "",
                "POST");

        System.out.println("Result: " + result);
        return new Gson().fromJson(result, Code.class);
    }

    public static ArrayList<PlayerDto> getPlayers(){
        String result = request("players",
                "",
                "GET");

        return new ArrayList<PlayerDto>(Arrays.asList(new Gson().fromJson(result, PlayerDto[].class)));
    }

    public static ArrayList<FactoryDto> getFactories(){
        String result = request("factories",
                "",
                "GET");

        return new ArrayList<FactoryDto>(Arrays.asList(new Gson().fromJson(result, FactoryDto[].class)));
    }
    public static FactoryDto getCenter(){
        String result = request("center",
                "",
                "GET");

        return new Gson().fromJson(result, FactoryDto.class);
    }

    public static GameDto getGame(){
        String result = request("game",
                "",
                "GET");

        return new Gson().fromJson(result, GameDto.class);
    }

    public static Code pickFromFactory(PickForm form){
        String result = request("factories/pick",
                new Gson().toJson(form),
                "POST");

        return new Gson().fromJson(result, Code.class);
    }

    public static Code pickFromCenter(PickForm form){
        String result = request("center/pick",
                new Gson().toJson(form),
                "POST");

        return new Gson().fromJson(result, Code.class);
    }

    private static String request(String urlString, String jsonInputString, String requestMethod){
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
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }
}
