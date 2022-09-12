package roach.ryan.ff;

import static java.util.Comparator.comparing;

import java.io.File;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;

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
        if (isPoolSizeTooLarge(pool))
        {
            System.out.println("Player pool is too large. Program will take over five minutes to complete.");
            System.exit(0);
        }
        printTopTenPointsPerSalaryPerPosition();
        printTopFiveTeams(pool);
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

    private static void printTopTenPointsPerSalaryPerPosition()
    {

    }

    private static void printTopFiveTeams(FreeAgentPool pool)
    {
        Queue<Team> teams = new PriorityQueue<>(comparing(Team::getPoints));
        for (Quarterback qb : pool.getQuarterbacks())
        {
            for (RunningBack rb1 : pool.getRunningBacks())
            {
                for (RunningBack rb2 : pool.getRunningBacks())
                {
                    for (WideReceiver wr1 : pool.getWideReceivers())
                    {
                        for (WideReceiver wr2 : pool.getWideReceivers())
                        {
                            for (WideReceiver wr3 : pool.getWideReceivers())
                            {
                                for (TightEnd te : pool.getTightEnds())
                                {
                                    for (Flex flex : pool.getFlexes())
                                    {
                                        if (rb1.equals(rb2) || wr1.equals(wr2) || wr1.equals(wr3) || wr2.equals(wr3)
                                                || rb1.equals(flex) || rb2.equals(flex) || wr1.equals(flex)
                                                || wr2.equals(flex) || wr3.equals(flex))
                                        {
                                            continue;
                                        }
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
                                            }
                                            else if (teams.peek().getPoints() < team.getPoints()
                                                    && !teams.contains(team))
                                            {

                                                teams.poll();
                                                teams.add(team);
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
