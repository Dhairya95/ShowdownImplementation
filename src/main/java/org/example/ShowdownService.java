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

    public static void main(String[] args) {
        UUID uuid = UUID.randomUUID();
        Implementation implementation = new Implementation();
        implementation.openConnection();
   /*     String[] string = new String[5];
        string[0] = ">start {\"formatid\":\"gen7randombattle\"}";
        String msg12 = "|player|p1|Anonycat|60|1200\n" +
                "|player|p2|Anonybird|113|1300\n" +
                "|teamsize|p1|4\n" +
                "|teamsize|p2|5\n" +
                "|gametype|doubles\n" +
                "|gen|7\n" +
                "|tier|[Gen 7] Doubles Ubers\n" +
                "|rule|Species Clause: Limit one of each Pokémon\n" +
                "|rule|OHKO Clause: OHKO moves are banned\n" +
                "|rule|Moody Clause: Moody is banned\n" +
                "|rule|Evasion Abilities Clause: Evasion abilities are banned\n" +
                "|rule|Evasion Moves Clause: Evasion moves are banned\n" +
                "|rule|Endless Battle Clause: Forcing endless battles is banned\n" +
                "|rule|HP Percentage Mod: HP is shown in percentages\n" +
                "|clearpoke\n" +
                "|poke|p1|Pikachu, L59, F|item\n" +
                "|poke|p1|Kecleon, M|item\n" +
                "|poke|p1|Jynx, F|item\n" +
                "|poke|p1|Mewtwo|item\n" +
                "|poke|p2|Hoopa-Unbound|\n" +
                "|poke|p2|Smeargle, L1, F|item\n" +
                "|poke|p2|Forretress, L31, F|\n" +
                "|poke|p2|Groudon, L60|item\n" +
                "|poke|p2|Feebas, L1, M|\n" +
                "|teampreview\n" +
                "|\n" +
                "|start";*/
        String msg[] = new String[3];
        msg[0] = ">start { \"format\": {  \"gameType\": \"singles\",    \"gen\": 9} }";
        msg[1] = ">player p1 {\"name\":\"Alice\",\"team\":\"Articuno||||||leftovers|pressure|icebeam,hurricane,substitute,roost|Modest|252,,,252,4,||,,,30,30,||||\"}";
        msg[2] = ">player p2 {\"name\":\"Bob\",\"team\":\"Ludicolo|||||||lifeorb|swiftswim|surf,gigadrain,icebeam,raindance|Modest|4,,,252,,252|||||\"}";

      implementation.startBattle(uuid, msg);

      String battle[] = new String[1];
        battle[0] = ">p1 move 3 +1";

        implementation.sendToShowdown(uuid,battle);


//oldImplementation();
    }

    public static void oldImplementation()
    {
        Implementation implementation = new Implementation();
        implementation.openConnection();
        UUID uuid = UUID.randomUUID();
        String[] string = new String[5];
        string[0] = ">start { \"format\": {    \"mod\": \"cobblemon\",    \"gameType\": \"singles\",    \"gen\": 9,    \"ruleset\": [\"Obtainable\", \"+Past\", \"+Unobtainable\"],    \"effectType\": \"Format\"} }";
        string[1] = ">player p1 {\"name\":\"73181018-6474-36f0-8722-c7fb6befd317\",\"team\":\"charmander|||||||leftovers|blaze|smokescreen,ember,scratch,growl|bashful|252,,,252,4,||,,,30,30,||||\"}";
        string[2] = ">player p2 {\"name\":\"122418f8-a2ab-4047-a335-d7d7b40cd1b4\",\"team\":\"ponyta||122418f8-a2ab-4047-a335-d7d7b40cd1b4|48||-1||flashfire|flamecharge,ember,tailwhip,tackle|20/20,25/25,30/30,35/35|rash|0,0,0,0,0,0|M|12,27,8,14,15,17||18|50,pokeball,,,0,fire,\"}";
        string[3] = ">p1 team 1";
        string[4] = ">p2 team 1";
        // implementation.startBattle(uuid,string);

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

