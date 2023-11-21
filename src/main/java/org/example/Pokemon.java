package org.example;

import java.util.HashMap;
import java.util.zip.ZipFile;

public class Pokemon {
    String level;
    String showdownId;
    String uuid;
    String name;

    public Pokemon(String level, String showdownId, String uuid, String name, String species, String gender, String item, EVS evs, String nature, IVS ivs, String[] moves, String ability) {
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
    }

    String species;
    String gender;
    String item;
    EVS evs;
    String nature;
    IVS ivs;
    String moves[] = new String[4];
    private String ability;


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
        return 30;
    }

    public String getStatus() {
        return "";
    }

    public String getAbility() {

        return ability;
    }
}
