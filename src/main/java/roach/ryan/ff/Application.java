package roach.ryan.ff;

import java.io.File;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import roach.ryan.ff.data.DataParser;
import roach.ryan.ff.data.FreeAgentPool;
import roach.ryan.ff.fanduel.FanDuel;
import roach.ryan.ff.model.Player;
import roach.ryan.ff.model.Team;
import roach.ryan.ff.model.TopTeams;

public class Application
{

    public static void main(String[] args)
    {
        DataParser parser = new DataParser(new File(args[0]));
        parser.optimize();
        FreeAgentPool optimizedPool = new FreeAgentPool(parser.getQuarterbacks(), parser.getRunningBacks(),
                parser.getWideReceivers(), parser.getTightEnds(), parser.getDefenses(), parser.getFlexes());

        if (isPoolSizeTooLarge(optimizedPool))
        {
            System.out.println("Player pool is too large. Lineup calculator will take over five minutes to complete.");
            System.exit(0);
        }

        TopTeams teams = FanDuel.getTopTeams(optimizedPool);
        if (teams.getTeams().isEmpty())
        {
            System.out.println("No valid teams can be made from the pool.");
        }

        int i = 5;
        Iterator<Team> it = teams.getTeams().iterator();
        while (it.hasNext())
        {
            System.out.println("#" + i--);
            System.out.println(it.next());
        }
    }

    private static boolean isPoolSizeTooLarge(FreeAgentPool pool)
    {
        BigInteger qbCount = BigInteger.valueOf(pool.getQuarterbacks().size());
        BigInteger rbCount = BigInteger.valueOf(pool.getRunningBacks().size());
        BigInteger wrCount = BigInteger.valueOf(pool.getWideReceivers().size());
        BigInteger teCount = BigInteger.valueOf(pool.getTightEnds().size());
        BigInteger defCount = BigInteger.valueOf(pool.getDefenses().size());
        BigInteger flexCount = BigInteger.valueOf(pool.getFlexes().size());
        BigInteger combos = qbCount.multiply(rbCount).multiply(rbCount).multiply(wrCount).multiply(wrCount)
                .multiply(wrCount).multiply(defCount).multiply(flexCount).multiply(teCount);
        return combos.compareTo(new BigInteger("300000000000")) > 0;
    }

    private static void printTopTenPointsPerSalaryPerPosition(FreeAgentPool pool)
    {
        printTopTen(pool.getQuarterbacks());
        printTopTen(pool.getRunningBacks());
        printTopTen(pool.getWideReceivers());
        printTopTen(pool.getTightEnds());
        printTopTen(pool.getDefenses());
    }

    private static void printTopTen(List<? extends Player> players)
    {
        Collections.sort(players, new Comparator<Player>()
        {

            @Override
            public int compare(Player p1, Player p2)
            {
                double comparison = p1.getProjectedPoints() / p1.getSalary() - p2.getProjectedPoints() / p2.getSalary();
                if (comparison > 0)
                {
                    return -1;
                }
                else if (comparison < 0)
                {
                    return 1;
                }
                else
                {
                    return 0;
                }
            }
        });
        System.out.println(players.get(0).getClass().getSimpleName());
        for (int i = 0; i < 10; i++)
        {
            System.out.println(i + 1 + ") " + players.get(i).getName());
        }
        System.out.println();
    }
}
