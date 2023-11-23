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
    static  ArrayList player1Team,player2Team;
    static ArrayList playerTeams = new ArrayList();
    ShowdownService()
    {

    }

    //Nickname|species|uuid|currentHealth|Showdownstatus|Pokemoncondition|heldItem|ability|moves|moveInfo(pp/maxPP)|nature|evs|gender|ivs|shiny|level|happiness,pokeball,hiddenpowertype,gigantmax,dynamaxlevel,teratype
    public static void main(String[] args) {

        UUID uuid = UUID.randomUUID();

        Implementation implementation = new Implementation();
        implementation.openConnection();

        BattleRegistry br = new BattleRegistry();
        Pokemon pokemon1 = new Pokemon("5", "articuno", "articunoUUID1", "Articuno", "Articuno", "M", "", new EVS(), "modest", new IVS(), new Move[]{new Move("icebeam",10,10), new Move("hurricane",10,10),new Move("substitute",10,10),new Move("screech",10,10)}, "pressure", "");

        Pokemon pokemon2 = new Pokemon("5", "charmander", "charmanderUUID2", "charmander", "charmander", "M", "", new EVS(), "bashful", new IVS(),new Move[]{new Move("smokescreen",10,10), new Move("ember",10,10),new Move( "scratch",10,10), new Move("growl",10,10)}, "blaze", "");

        Pokemon pokemon3 = new Pokemon("5", "ponyta", "ponytaUUID3", "ponyta", "ponyta", "F", "", new EVS(), "rash", new IVS(), new Move[]{new Move("flamecharge",10,10), new Move("ember",10,10), new Move("tailwhip",10,10), new Move("uturn",10,10)}, "flashfire", "psn");

        Pokemon pokemon4 = new Pokemon("5", "ludicolo", "ludicoloUUID4", "ludicolo", "ludicolo", "M", "", new EVS(), "modest", new IVS(), new Move[]{new Move("surf",10,10), new Move("gigadrain",10,10), new Move("icebeam",10,10),new Move( "raindance",10,10)}, "swiftswim", "");

        player1Team = new ArrayList();
        player1Team.add(pokemon1);
//        player1Team.add(pokemon2);

        player2Team = new ArrayList();
        player2Team.add(pokemon3);
 //       player2Team.add(pokemon4);

        playerTeams.addAll(player1Team);
        playerTeams.addAll(player2Team);

        String firstBattle[] = new String[3];
        firstBattle[0] = ">start { \"format\": {  \"gameType\": \"singles\",    \"gen\": 9} }";
        firstBattle[1] = ">player p1 {\"name\":\"Alice\",\"team\":\"" + br.packTeam(player1Team) + "\"}";
        firstBattle[2] = ">player p2 {\"name\":\"Bob\",\"team\":\"" + br.packTeam(player2Team) + "\"}";

        implementation.startBattle(uuid, firstBattle);

        String battle[] = new String[1];

        displayPokemonHealth(player1Team);
        displayPokemonHealth(player2Team);
        displayPokemonPP(playerTeams);

        System.out.println("-------Turn 1 --------");

        battle[0] = ">p1 move 2";
        implementation.send(uuid, battle);

        battle[0] = ">p2 move 2";
        implementation.send(uuid, battle);


        displayPokemonHealth(player1Team);
        displayPokemonHealth(player2Team);
        displayPokemonPP(playerTeams);


        System.out.println("------- Turn 2 --------");

        battle[0] = ">p1 move 4";
        implementation.send(uuid, battle);

        battle[0] = ">p2 move 2";
        implementation.send(uuid, battle);

        displayPokemonHealth(player1Team);
        displayPokemonHealth(player2Team);
        displayPokemonPP(playerTeams);

        System.out.println("------- Turn 3 --------");

        battle[0] = ">p1 move 2";
        implementation.send(uuid, battle);


        battle[0] = ">p2 move 2";
        implementation.send(uuid, battle);

        displayPokemonHealth(player1Team);
        displayPokemonHealth(player2Team);
        displayPokemonPP(playerTeams);
    /*    System.out.println("------- Turn 4 --------");

        battle[0] = ">p1 move 2";
        implementation.send(uuid, battle);

        battle[0] = ">p1 move 1";
        implementation.send(uuid, battle);


        System.out.println("------- Turn 5 --------");

        battle[0] = ">p1 move 2";
        implementation.send(uuid, battle);

        battle[0] = ">p1 move 1";
        implementation.send(uuid, battle);


        System.out.println("------- Turn 6 --------");

        battle[0] = ">p1 move 2";
        implementation.send(uuid, battle);

        battle[0] = ">p2 move 3";
        implementation.send(uuid, battle);


        System.out.println("------- Turn 7 --------");

        battle[0] = ">p1 move 1";
        implementation.send(uuid, battle);

        battle[0] = ">p2 move 2";
        implementation.send(uuid, battle);
*/

    }
public static void displayPokemonHealth(ArrayList team)
{
    for(Object pok : team)
    {
        Pokemon pp = (Pokemon) pok;
        System.out.println("Health of "+ pp.name+" is "+pp.health);
    }
}
public static void displayPokemonPP(ArrayList playerTeams)
{
    for(Object pok : playerTeams)
    {
        Pokemon pop = (Pokemon) pok;
        System.out.println("Pokemon "+pop.name+" has these moves,");
        for(Move move: pop.moves)
        {
            System.out.print(move.getName()+" pp is "+move.getPp()+" ");
        }
        System.out.println("");
    }
}


public static void updatePokemonPP(String battleId,String message)
{

    String msg[] = message.split("\\|");
    for (int i = 0; i < msg.length; i++)
    {

        if (msg[i].equals("turn"))
        {

            int j = 0;
            try {
                while (msg[i + 2 + j] != null && msg[i + 2 + j].equals("pp_update")) {


                    String pokemon[] = msg[i + 2 + j + 1].split(":");
                    String pokemonID = pokemon[1].replaceAll(" ", "");
                    String movesList[] = msg[i + 2 + j + 2].split(",");


                    for (Object pok : playerTeams) {

                        Pokemon pokemon1 = (Pokemon) pok;
                        if (pokemon1.getUuid().equals(pokemonID)) {

                            for (Move move : pokemon1.moves) {

                                for (String movesSet : movesList) {


                                    String moveData[] = movesSet.split(":");
                                    String moveName = moveData[0].replaceAll(" ", "");
                                    String movePP = moveData[1].replaceAll(" ", "");


                                    if (move.getName().equals(moveName)) {
                                        if (movePP.contains("\n")) {
                                            String mm = movePP.substring(0,movePP.length()-1);
                                             movePP = mm;
                                        }
                                        move.setPp(Integer.parseInt(movePP));
                                    }
                                }

                            }
                        }
                    }

                    j += 3;
                }
            }
            catch (Exception e)
            {

            }

        }
    }
}
    public static void updatePokemonHealth(String battleId,String message)
    {

        String msg[] = message.split("\\|");

        for(int i=0;i<msg.length;i++)
        {

            if(msg[i].equals("split") )//&& msg[i+2].equals("-damage"))
            {

                String player = msg[i+1];
                String pokemon[] = msg[i+3].split(":");
                String pokemonID = pokemon[1];
                String pokemonHealth[] = msg[i+4].split("/");
                String pokmeonHP = pokemonHealth[0];

                if(player.equals("p1\n"))
                {

                    for(Object pok : player1Team)
                    {

                        Pokemon pp = (Pokemon) pok;

                        if(pp.getUuid().equals(pokemonID.replaceAll(" ","")))
                        {
                           System.out.println("Pokemon  health = "+pokmeonHP );
                            pp.health = Integer.parseInt(pokmeonHP);
                            if(msg[i+4].equals("0 fnt"))
                            {
                             pp.health = 0;
                            }
                        }
                    }
                }
                if(player.equals("p2\n"))
                {
                    for(Object pok : player2Team)
                    {
                        Pokemon pp = (Pokemon) pok;
                        if(pp.getUuid().equals(pokemonID.replaceAll(" ","")))
                        {
                            System.out.println("Pokemon  health = "+pokmeonHP );
                            pp.health = Integer.parseInt(pokmeonHP);
                            if(msg[i+4].replaceAll(" ","").equals("0fnt"))
                            {
                                pp.health = 0;
                            }
                        }
                    }
                }

            }
        }
    }


   public static void simultaneousBattle() {
        // simulating two battles simulatneously

        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();

        Implementation implementation = new Implementation();
        implementation.openConnection();

        BattleRegistry br1 = new BattleRegistry();
        BattleRegistry br2 = new BattleRegistry();

       Pokemon pokemon1 = new Pokemon("5", "articuno", "articunoUUID1", "Articuno", "Articuno", "M", "", new EVS(), "modest", new IVS(), new Move[]{new Move("icebeam",10,10), new Move("hurricane",10,10),new Move("substitute",10,10),new Move("roar",10,10)}, "pressure", "");

       Pokemon pokemon2 = new Pokemon("5", "charmander", "charmanderUUID2", "charmander", "charmander", "M", "", new EVS(), "bashful", new IVS(),new Move[]{new Move("smokescreen",10,10), new Move("ember",10,10),new Move( "scratch",10,10), new Move("growl",10,10)}, "blaze", "");

       Pokemon pokemon3 = new Pokemon("5", "ponyta", "ponytaUUID3", "ponyta", "ponyta", "F", "", new EVS(), "rash", new IVS(), new Move[]{new Move("flamecharge",10,10), new Move("ember",10,10), new Move("tailwhip",10,10), new Move("uturn",10,10)}, "flashfire", "psn");

       Pokemon pokemon4 = new Pokemon("5", "ludicolo", "ludicoloUUID4", "ludicolo", "ludicolo", "M", "", new EVS(), "modest", new IVS(), new Move[]{new Move("surf",10,10), new Move("gigadrain",10,10), new Move("icebeam",10,10),new Move( "raindance",10,10)}, "swiftswim", "");
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

}

