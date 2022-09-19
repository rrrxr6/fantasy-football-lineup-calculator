package roach.ryan.ff;

import static java.util.Comparator.comparing;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import roach.ryan.ff.data.DataParser;
import roach.ryan.ff.data.FreeAgentPool;
import roach.ryan.ff.model.Defense;
import roach.ryan.ff.model.FanDuelTeam;
import roach.ryan.ff.model.Flex;
import roach.ryan.ff.model.Player;
import roach.ryan.ff.model.Quarterback;
import roach.ryan.ff.model.RunningBack;
import roach.ryan.ff.model.Team;
import roach.ryan.ff.model.TightEnd;
import roach.ryan.ff.model.WideReceiver;

public class Application
{

    public static void main(String[] args)
    {
        DataParser parser = new DataParser(new File(args[0]));
        FreeAgentPool pool = new FreeAgentPool(parser.getQuarterbacks(), parser.getRunningBacks(),
                parser.getWideReceivers(), parser.getTightEnds(), parser.getDefenses(), parser.getFlexes());
        printTopTenPointsPerSalaryPerPosition(pool);

        parser.optimize();
        FreeAgentPool optimizedPool = new FreeAgentPool(parser.getQuarterbacks(), parser.getRunningBacks(),
                parser.getWideReceivers(), parser.getTightEnds(), parser.getDefenses(), parser.getFlexes());
        if (isPoolSizeTooLarge(optimizedPool))
        {
            System.out.println("Player pool is too large. Lineup calculator will take over five minutes to complete.");
            System.exit(0);
        }
        printTopFiveTeams(optimizedPool);
    }

    private static int sumSalary(Player... players)
    {
        int salary = 0;
        for (Player player : players)
        {
            salary += player.getSalary();
        }
        return salary;
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
        return combos.compareTo(new BigInteger("25000000000")) > 0;
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

    private static void printTopFiveTeams(FreeAgentPool pool)
    {
        List<Team> teams = new ArrayList<>(5);
        List<RunningBack> rbs = pool.getRunningBacks();
        List<WideReceiver> wrs = pool.getWideReceivers();
        for (Flex flex : pool.getFlexes())
        {
            for (int i = 0; i < rbs.size(); i++)
            {
                RunningBack rb1 = rbs.get(i);
                for (int j = i + 1; j < rbs.size(); j++)
                {
                    RunningBack rb2 = rbs.get(j);
                    for (int x = 0; x < wrs.size(); x++)
                    {
                        WideReceiver wr1 = wrs.get(x);
                        for (int y = x + 1; y < wrs.size(); y++)
                        {
                            WideReceiver wr2 = wrs.get(y);
                            for (int z = y + 1; z < wrs.size(); z++)
                            {
                                WideReceiver wr3 = wrs.get(z);
                                if (rb1.equals(flex) || rb2.equals(flex) || wr1.equals(flex) || wr2.equals(flex)
                                        || wr3.equals(flex))
                                {
                                    continue;
                                }
                                for (Quarterback qb : pool.getQuarterbacks())
                                {
                                    for (TightEnd te : pool.getTightEnds())
                                    {
                                        for (Defense dst : pool.getDefenses())
                                        {
                                            if (sumSalary(qb, rb1, rb2, wr1, wr2, wr3, te, flex, dst) > FanDuelTeam
                                                    .getSalaryCap())
                                            {
                                                continue;
                                            }
                                            Team team = new FanDuelTeam(qb, rb1, rb2, wr1, wr2, wr3, te, flex, dst);
                                            if (teams.size() < 5)
                                            {
                                                teams.add(team);
                                                teams.sort(comparing(Team::getProjectedPoints));
                                            }
                                            else if (teams.get(0).getProjectedPoints() < team.getProjectedPoints()
                                                    && !teams.contains(team))
                                            {
                                                teams.remove(0);
                                                teams.add(team);
                                                teams.sort(comparing(Team::getProjectedPoints));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (teams.isEmpty())
        {
            System.out.println("No valid teams can be made from the pool.");
        }

        int i = 5;
        Iterator<Team> it = teams.iterator();
        while (it.hasNext())
        {
            System.out.println("#" + i--);
            System.out.println(it.next());
        }
    }
}
