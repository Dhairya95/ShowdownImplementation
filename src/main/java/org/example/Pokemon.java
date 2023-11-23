package org.example;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipFile;

public class Pokemon {
    String level;
    String showdownId;
    String uuid;
    String name;
    String status;
int health;
    public Pokemon(String level, String showdownId, String uuid, String name, String species, String gender, String item, EVS evs, String nature, IVS ivs, Move[] moves, String ability,String status) {
        this.level = level;
        this.showdownId = showdownId;
        this.uuid = uuid;
        this.name = name;
        this.species = species;
        this.gender = gender;
        this.item = item;
        this.evs = evs;
        this.nature = nature;
        this.ivs = ivs;
        this.moves = moves;
        this.ability = ability;
        this.status = status;
        health = 30;
    }

    String species;
    String gender;
    String item;
    EVS evs;
    String nature;
    IVS ivs;
    Move moves[] = new Move[4];
    private String ability;

    Map <String,String>movesPP = new HashMap<String,String>();

    public void setMovesPP(HashMap<String,String> movesList)
    {
        for(Map.Entry<String,String> map: movesList.entrySet())
        {
            movesPP.put(map.getKey(),map.getValue());
        }
    }


    public String showdownId() {
        return showdownId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getCurrentHealth() {
        return health;
    }

    public String getStatus() {
        return status;
    }

    public String getAbility() {

        return ability;
    }
}
