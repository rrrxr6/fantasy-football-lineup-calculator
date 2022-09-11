package roach.ryan.ff;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import roach.ryan.ff.data.DataParser;
import roach.ryan.ff.model.FreeAgentPool;
import roach.ryan.ff.model.Player;
import roach.ryan.ff.model.Team;

public class Application
{

    public static void main(String[] args) throws FileNotFoundException
    {
        FreeAgentPool pool = new FreeAgentPool(DataParser.read(new File(args[0])));
        List<Team> teams = new ArrayList<>();
        for (Player qb : pool.getQuarterbacks())
        {
            Team team = new Team(60000);
            if (!team.setQuarterback(qb))
            {
                continue;
            }
            for (Player rb1 : pool.getRunningBacks())
            {
                if (!team.setRunningBack1(rb1))
                {
                    continue;
                }
                for (Player rb2 : pool.getRunningBacks())
                {
                    if (!team.setRunningBack2(rb2))
                    {
                        continue;
                    }
                    for (Player wr1 : pool.getWideReceivers())
                    {
                        if (!team.setWideReceiver1(wr1))
                        {
                            continue;
                        }
                        for (Player wr2 : pool.getWideReceivers())
                        {
                            if (!team.setWideReceiver2(wr2))
                            {
                                continue;
                            }
                            for (Player wr3 : pool.getWideReceivers())
                            {
                                if (!team.setWideReceiver3(wr3))
                                {
                                    continue;
                                }
                                for (Player te : pool.getTightEnds())
                                {
                                    if (!team.setTightEnd(te))
                                    {
                                        continue;
                                    }
                                    for (Player flex : pool.getFlexes())
                                    {
                                        if (!team.setFlex(flex))
                                        {
                                            continue;
                                        }
                                        for (Player dst : pool.getDefenses())
                                        {
                                            if (!team.setDefense(dst))
                                            {
                                                continue;
                                            }

                                            if ((teams.isEmpty()
                                                    || team.getPoints() >= teams.get(teams.size() - 1).getPoints())
                                                    && !teams.contains(team))
                                            {
                                                teams.add(team.copy());
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

}
