package org.example;

public class Move {
    String name;
    int pp;
    int maxPP;

    public Move(String name, int pp, int maxPP) {
        this.name = name;
        this.pp = pp;
        this.maxPP = maxPP;
    }

    public String getName() {
        return name;
    }

    public void setPp(int pp) {
        this.pp = pp;
    }

    public int getPp() {
        return pp;
    }

    public int getMaxPP() {
        return maxPP;
    }
}
