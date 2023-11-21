package org.example;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Array;
import java.util.ArrayList;
import java.util.UUID;

public class ShowdownService {
    //Nickname|species|uuid|currentHealth|Showdownstatus|Pokemoncondition|heldItem|ability|moves|moveInfo(pp/maxPP)|nature|evs|gender|ivs|shiny|level|happiness,pokeball,hiddenpowertype,gigantmax,dynamaxlevel,teratype
    public static void main(String[] args) {

        // simulating two battles simulatneously

        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();

        Implementation implementation = new Implementation();
        implementation.openConnection();

        BattleRegistry br1 = new BattleRegistry();
        BattleRegistry br2 = new BattleRegistry();

        Pokemon pokemon1 = new Pokemon("5", "articuno", "articunoUUID1", "Articuno", "Articuno", "M", "", new EVS(), "modest", new IVS(), new String[]{"icebeam", "hurricane", "substitute", "roost"}, "pressure");

        Pokemon pokemon2 = new Pokemon("5", "charmander", "charmanderUUID2", "charmander", "charmander", "M", "", new EVS(), "bashful", new IVS(), new String[]{"smokescreen", "ember", "scratch", "growl"}, "blaze");

        Pokemon pokemon3 = new Pokemon("5", "ponyta", "ponytaUUID3", "ponyta", "ponyta", "F", "", new EVS(), "rash", new IVS(), new String[]{"flamecharge", "ember", "tailwhip", "tackle"}, "flashfire");

        Pokemon pokemon4 = new Pokemon("5", "ludicolo", "ludicoloUUID4", "ludicolo", "ludicolo", "M", "", new EVS(), "modest", new IVS(), new String[]{"surf", "gigadrain", "icebeam", "raindance"}, "swiftswim");

        ArrayList player1Team = new ArrayList();
        player1Team.add(pokemon1);

        ArrayList player2Team = new ArrayList();
        player2Team.add(pokemon2);

        ArrayList player3Team = new ArrayList();
        player3Team.add(pokemon3);

        ArrayList player4Team = new ArrayList();
        player4Team.add(pokemon4);

        String firstBattle[] = new String[3];
        firstBattle[0] = ">start { \"format\": {  \"gameType\": \"singles\",    \"gen\": 9} }";
        firstBattle[1] = ">player p1 {\"name\":\"Alice\",\"team\":\"" + br1.packTeam(player1Team) + "\"}";
        firstBattle[2] = ">player p2 {\"name\":\"Bob\",\"team\":\"" + br1.packTeam(player2Team) + "\"}";

        String secondBattle[] = new String[3];
        secondBattle[0] = ">start { \"format\": {  \"gameType\": \"singles\", \"gen\": 9} }";
        secondBattle[1] = ">player p1 {\"name\":\"Cassie\",\"team\":\"" + br2.packTeam(player3Team) + "\"}";
        secondBattle[2] = ">player p2 {\"name\":\"Dave\",\"team\":\"" + br2.packTeam(player4Team) + "\"}";

        implementation.startBattle(uuid1, firstBattle);
        implementation.startBattle(uuid2, secondBattle);

        String battle[] = new String[1];

        System.out.println("-------Turn 1 --------");

        battle[0] = ">p1 move 1";
        implementation.send(uuid1, battle);

        battle[0] = ">p2 move 2";
        implementation.send(uuid1, battle);

        // Second battle

        battle[0] = ">p1 move 2";
        implementation.send(uuid2, battle);

        battle[0] = ">p2 move 3";
        implementation.send(uuid2, battle);

        System.out.println("------- Turn 2 --------");

        battle[0] = ">p1 move 4";
        implementation.send(uuid1, battle);

        battle[0] = ">p1 move 2";
        implementation.send(uuid2, battle);

        battle[0] = ">p2 move 3";
        implementation.send(uuid2, battle);

        battle[0] = ">p2 move 4";
        implementation.send(uuid1, battle);

        // Second battle


        System.out.println("------- Turn 3 --------");

        battle[0] = ">p1 move 1";
        implementation.send(uuid1, battle);

        battle[0] = ">p1 move 2";
        implementation.send(uuid2, battle);

        battle[0] = ">p2 move 1";
        implementation.send(uuid1, battle);

        battle[0] = ">p2 move 3";
        implementation.send(uuid2, battle);

        // Second battle


        System.out.println("------- Turn 4 --------");

        battle[0] = ">p1 move 2";
        implementation.send(uuid2, battle);

        battle[0] = ">p1 move 1";
        implementation.send(uuid1, battle);

        battle[0] = ">p2 move 3";
        implementation.send(uuid2, battle);

        battle[0] = ">p2 move 1";
        implementation.send(uuid1, battle);

        // Second battle


        System.out.println("------- Turn 5 --------");

        battle[0] = ">p1 move 2";
        implementation.send(uuid2, battle);

        battle[0] = ">p1 move 1";
        implementation.send(uuid1, battle);

        battle[0] = ">p2 move 2";
        implementation.send(uuid1, battle);

        battle[0] = ">p2 move 3";
        implementation.send(uuid2, battle);


        // Second battle


        System.out.println("------- Turn 6 --------");

        battle[0] = ">p1 move 2";
        implementation.send(uuid2, battle);

        battle[0] = ">p2 move 3";
        implementation.send(uuid2, battle);

        battle[0] = ">p1 move 1";
        implementation.send(uuid1, battle);

        battle[0] = ">p2 move 2";
        implementation.send(uuid1, battle);

        // Second battle


        System.out.println("------- Turn 7 --------");

        battle[0] = ">p1 move 1";
        implementation.send(uuid1, battle);

        battle[0] = ">p2 move 2";
        implementation.send(uuid1, battle);

        // Second battle

        battle[0] = ">p1 move 2";
        implementation.send(uuid2, battle);

        battle[0] = ">p2 move 3";
        implementation.send(uuid2, battle);


    }

    public static void oldImplementation() {
        Implementation implementation = new Implementation();
        implementation.openConnection();
        UUID uuid = UUID.randomUUID();
        String[] string = new String[5];
        string[0] = ">start { \"format\": {    \"mod\": \"cobblemon\",    \"gameType\": \"singles\",    \"gen\": 9,    \"ruleset\": [\"Obtainable\", \"+Past\", \"+Unobtainable\"],    \"effectType\": \"Format\"} }";
        string[1] = ">player p1 {\"name\":\"73181018-6474-36f0-8722-c7fb6befd317\",\"team\":\"charmander|||||||leftovers|blaze|smokescreen,ember,scratch,growl|bashful|252,,,252,4,||,,,30,30,||||\"}";
        string[2] = ">player p2 {\"name\":\"122418f8-a2ab-4047-a335-d7d7b40cd1b4\",\"team\":\"ponyta||122418f8-a2ab-4047-a335-d7d7b40cd1b4|48||-1||flashfire|flamecharge,ember,tailwhip,tackle|20/20,25/25,30/30,35/35|rash|0,0,0,0,0,0|M|12,27,8,14,15,17||18|50,pokeball,,,0,fire,\"}";
        string[3] = ">p1 team 1";
        string[4] = ">p2 team 1";
        implementation.startBattle(uuid, string);

        System.out.println("/n/n/n------------------------------------------------/n/n/n/n");

        String string1[] = new String[2];
        string1[0] = ">p1 move 2 +1";
        string1[1] = "update\n" +
                "|\n" +
                "|t:|1699976341\n" +
                "|move|p2a: 122418f8-a2ab-4047-a335-d7d7b40cd1b4|Tail Whip|p1a: f85d7575-105b-402f-86f3-deae449c76ae\n" +
                "|-unboost|p1a: f85d7575-105b-402f-86f3-deae449c76ae|def|1\n" +
                "|move|p1a: f85d7575-105b-402f-86f3-deae449c76ae|Water Gun|p2a: 122418f8-a2ab-4047-a335-d7d7b40cd1b4\n" +
                "|-supereffective|p2a: 122418f8-a2ab-4047-a335-d7d7b40cd1b4\n" +
                "|split|p2\n" +
                "|-damage|p2a: 122418f8-a2ab-4047-a335-d7d7b40cd1b4|39/48\n" +
                "|-damage|p2a: 122418f8-a2ab-4047-a335-d7d7b40cd1b4|82/100\n" +
                "|\n" +
                "|upkeep\n" +
                "|turn|2\n" +
                "|pp_update|p1: f85d7575-105b-402f-86f3-deae449c76ae|rapidspin: 40, withdraw: 40, watergun: 24, tailwhip: 30\n" +
                "|pp_update|p2: 122418f8-a2ab-4047-a335-d7d7b40cd1b4|flamecharge: 20, ember: 25, tailwhip: 29, tackle: 35";
        implementation.startBattle(uuid, string);


    }
}

