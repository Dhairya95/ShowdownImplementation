package org.example;

import java.util.List;

public class Trainer {
    String name;
    String avatar;
    int rating;
    List<Pokemon> pokemonList;
    Trainer(String name)
    {
        this.name = name;
    }
    public void addPokemon(Pokemon pokemon)
    {
        this.pokemonList.add(pokemon);
    }

    public Pokemon getPokemon(String name)
    {
        return pokemonList.get(0);
    }
}
