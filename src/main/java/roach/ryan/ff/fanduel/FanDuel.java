package roach.ryan.ff.fanduel;

import java.util.List;

import roach.ryan.ff.data.FreeAgentPool;
import roach.ryan.ff.model.Defense;
import roach.ryan.ff.model.Flex;
import roach.ryan.ff.model.Player;
import roach.ryan.ff.model.Quarterback;
import roach.ryan.ff.model.RunningBack;
import roach.ryan.ff.model.TightEnd;
import roach.ryan.ff.model.TopTeams;
import roach.ryan.ff.model.WideReceiver;

public class FanDuel
{
    public static TopTeams getTopTeams(FreeAgentPool pool, List<Flex> flexes)
    {
        TopTeams teams = new TopTeams(100);
        List<RunningBack> rbs = pool.getRunningBacks();
        List<WideReceiver> wrs = pool.getWideReceivers();
        for (Flex flex : flexes)
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
                                            teams.add(new FanDuelTeam(qb, rb1, rb2, wr1, wr2, wr3, te, flex, dst));

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return teams;
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
}
