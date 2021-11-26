package com.project.azul.dto;

import com.project.azul.models.Factory;

import java.util.List;
import java.util.stream.Collectors;

public class FactoryDto {
    private final int id;
    private final List<String> tiles;

    public FactoryDto(Factory factory){
        this.id = factory.getId();
        this.tiles = factory.getTiles().stream().map(t -> t.getColor().toString()).collect(Collectors.toList());
    }

    public int getId() {
        return id;
    }

    public List<String> getTiles() {
        return tiles;
    }
}
