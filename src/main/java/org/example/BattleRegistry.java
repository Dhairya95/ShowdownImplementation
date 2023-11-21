package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BattleRegistry {
    public String packTeam(List<Pokemon> pokemonList) {
        List<String> team = new ArrayList<>();
        for (Pokemon pk : pokemonList) {
            StringBuilder packedTeamBuilder = new StringBuilder();
            // If no nickname, write species first and leave next blank
            packedTeamBuilder.append(pk.showdownId()).append("|");
            // Species, left empty if no nickname
            packedTeamBuilder.append("|");
            // REQUIRES OUR SHOWDOWN
            packedTeamBuilder.append(pk.getUuid()).append("|");
            packedTeamBuilder.append(pk.getCurrentHealth()).append("|");
            String showdownStatus = "";
            packedTeamBuilder.append(showdownStatus).append("|");
            // If a temporary status is on the Pokémon, provide a duration.
            packedTeamBuilder.append("-1|");
            // Held item, empty if none
            String heldItemID = pk.item;
            packedTeamBuilder.append(heldItemID).append("|");
            // Ability, our showdown has edits here to trust whatever we tell it, this was needed to support more than 4 abilities.
            packedTeamBuilder.append(pk.getAbility().replace("_", "")).append("|");
            // Moves
            packedTeamBuilder.append(
                    Arrays.stream(pk.moves).map(move -> move.replace("_", "")).collect(Collectors.joining(","))
            ).append("|");
            // Additional move info
            packedTeamBuilder.append(
                    Arrays.stream(pk.moves).map(move -> "10/10").collect(Collectors.joining(","))
            ).append("|");
            // Nature
            packedTeamBuilder.append(pk.nature).append("|");
            // EVs
            String evsInOrder = pk.evs.getShowdownEvs();
            packedTeamBuilder.append(evsInOrder).append("|");
            // Gender
            packedTeamBuilder.append(pk.gender).append("|");
            // IVs
            String ivsInOrder = pk.ivs.getShowdownIvs();
            packedTeamBuilder.append(ivsInOrder).append("|");
            // Shiny
            packedTeamBuilder.append("").append("|");
            // Level
            packedTeamBuilder.append(pk.level).append("|");
            // Misc
            // Happiness
            packedTeamBuilder.append("255").append(",");
            // Caught Ball
            // This is safe to do as all our pokeballs that have showdown item equivalents are the same IDs they use for the pokeball attribute
            String pokeball = "";
            packedTeamBuilder.append(pokeball).append(",");
            // Hidden Power Type
            packedTeamBuilder.append(",");
            // Gigantamax
            packedTeamBuilder.append("").append(",");
            // DynamaxLevel
            // 0 - 9, empty == 10
            packedTeamBuilder.append("").append(",");
            // Teratype
            packedTeamBuilder.append("").append(",");
            team.add(packedTeamBuilder.toString());
        }
        return String.join("]", team);
    }


}

