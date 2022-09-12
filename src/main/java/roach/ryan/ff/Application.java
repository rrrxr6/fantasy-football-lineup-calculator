package roach.ryan.ff;

import java.io.File;
import java.util.ArrayList;
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
                parser.getWideReceivers(), parser.getTightEnds(), parser.getDefenses());
        List<Team> teams = new ArrayList<>();
        for (Quarterback qb : pool.getQuarterbacks())
        {
            for (RunningBack rb1 : pool.getRunningBacks())
            {
                for (RunningBack rb2 : pool.getRunningBacks())
                {
                    for (WideReceiver wr1 : pool.getWideReceivers())
                    {
                        if(sumSalary(qb, rb1, rb2, wr1) > FanDuelTeam.getSalaryCap()) {
                            continue;
                        }
                        for (WideReceiver wr2 : pool.getWideReceivers())
                        {
                            if(sumSalary(qb, rb1, rb2, wr1,wr2) > FanDuelTeam.getSalaryCap()) {
                                continue;
                            }
                            for (WideReceiver wr3 : pool.getWideReceivers())
                            {
                                if(sumSalary(qb, rb1, rb2, wr1,wr2, wr3) > FanDuelTeam.getSalaryCap()) {
                                    continue;
                                }
                                for (TightEnd te : pool.getTightEnds())
                                {
                                    if(sumSalary(qb, rb1, rb2, wr1,wr2, wr3, te) > FanDuelTeam.getSalaryCap()) {
                                        continue;
                                    }
                                    for (Flex flex : pool.getFlexes())
                                    {
                                        if(sumSalary(qb, rb1, rb2, wr1,wr2, wr3, te, flex) > FanDuelTeam.getSalaryCap()) {
                                            continue;
                                        }
                                        if(rb1.equals(rb2) || wr1.equals(wr2) || wr1.equals(wr3) || wr2.equals(wr3) || rb1.equals(flex) || rb2.equals(flex) || wr1.equals(flex) || wr2.equals(flex) || wr3.equals(flex)) {
                                            continue;
                                        }
                                        for (Defense dst : pool.getDefenses())
                                        {
                                            if(sumSalary(qb, rb1, rb2, wr1,wr2, wr3, te, flex, dst) > FanDuelTeam.getSalaryCap()) {
                                                continue;
                                            }
                                            Team team = new FanDuelTeam(qb, rb1, rb2, wr1,wr2, wr3, te, flex, dst);
                                            if ((teams.isEmpty()
                                                    || team.getPoints() >= teams.get(teams.size() - 1).getPoints())
                                                    && !teams.contains(team))
                                            {
                                                teams.add(team);
                                                System.out.println(team);
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
    }

    private static int sumSalary(Player... players)
    {
        int salary = 0;
        for(Player player: players) {
            salary += player.getSalary();
        }
        return salary;
    }

}
