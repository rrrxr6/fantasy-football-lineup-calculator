package roach.ryan.ff.analysis;

import static java.util.Comparator.comparing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import roach.ryan.ff.model.Player;
import roach.ryan.ff.model.Team;

public class PlayerDistribution
{
    private final int teamCount;
    private final Map<Player, Integer> playerOccurrences = new HashMap<>();

    public PlayerDistribution(Collection<Team> teams)
    {
        teamCount = teams.size();
        for (Team team : teams)
        {
            for (Player player : team.getPlayers())
            {
                playerOccurrences.compute(player, (p, o) -> o == null ? 1 : ++o);
            }
        }
    }

    public void print()
    {
        List<Entry<Player, Integer>> players = new ArrayList<>(playerOccurrences.entrySet());
        Collections.sort(players, comparing(Entry<Player, Integer>::getValue).reversed());
        System.out.println(String.format("%25s | %7s | %7s | (count/total)", "Player", "Field", "Me"));
        System.out.println(String.format("%25s | %7s | %7s | ------------", "--------------", "------", "------"));
        for(Entry<Player, Integer> player: players) {
            double p = 100.0 * player.getValue() / teamCount;
            String formatted = String.format("%25s | %6.1f%% | %6.1f%% | (%d/%d)", player.getKey().getName(), player.getKey().getOwnership(), p, player.getValue(), teamCount);
            System.out.println(formatted);
        }
    }
}
