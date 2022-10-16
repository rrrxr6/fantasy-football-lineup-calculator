package roach.ryan.ff;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import java.io.File;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import roach.ryan.ff.data.DataParser;
import roach.ryan.ff.data.FreeAgentPool;
import roach.ryan.ff.fanduel.FanDuelSingleGameOptimizer;
import roach.ryan.ff.model.Team;

public class OptimizeSingleGameLineup
{
    public static void main(String[] args)
    {
        DataParser parser = new DataParser(new File(args[0]));
        parser.optimize();
        FreeAgentPool optimizedPool = new FreeAgentPool(parser.getQuarterbacks(), parser.getRunningBacks(),
                parser.getWideReceivers(), parser.getTightEnds(), parser.getFlexes(), parser.getDefenses(),
                parser.getKickers());

        Comparator<Team> comparator = comparing(Team::getMetric);
        FanDuelSingleGameOptimizer fanDuel = new FanDuelSingleGameOptimizer.Builder(optimizedPool).build();
        List<Team> teams = fanDuel.getTopTeams(comparator).getTeams().stream().distinct().sorted(comparator)
                .collect(toList());

        if (teams.isEmpty())
        {
            System.out.println("No valid teams can be made from the pool.");
        }

        printTeams(teams);
    }

    private static void printTeams(List<Team> teams)
    {
        int i = teams.size();
        Iterator<Team> it = teams.iterator();
        while (it.hasNext())
        {
            System.out.println("#" + i--);
            System.out.println(it.next());
        }
    }
}
