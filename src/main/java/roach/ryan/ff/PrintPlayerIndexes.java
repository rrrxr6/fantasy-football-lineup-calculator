package roach.ryan.ff;

import java.io.File;
import java.util.List;

import roach.ryan.ff.data.DataParser;
import roach.ryan.ff.data.FreeAgentPool;
import roach.ryan.ff.model.Player;

public class PrintPlayerIndexes
{
    public static void main(String[] args)
    {
        DataParser parser = new DataParser(new File(args[0]));
        parser.optimize();
        FreeAgentPool optimizedPool = new FreeAgentPool(parser.getQuarterbacks(), parser.getRunningBacks(),
                parser.getWideReceivers(), parser.getTightEnds(), parser.getFlexes(), parser.getDefenses());

        printPlayers(optimizedPool);
    }

    private static void printPlayers(FreeAgentPool pool)
    {
        printPosition(pool.getQuarterbacks());
        printPosition(pool.getRunningBacks());
        printPosition(pool.getWideReceivers());
        printPosition(pool.getTightEnds());
        printPosition(pool.getDefenses());
    }

    private static void printPosition(List<? extends Player> players)
    {
        System.out.println(players.get(0).getClass().getSimpleName());
        for (int i = 0; i < players.size(); i++)
        {
            System.out.println("[" + i + "] " + players.get(i).getName());
        }
        System.out.println();
    }
}
