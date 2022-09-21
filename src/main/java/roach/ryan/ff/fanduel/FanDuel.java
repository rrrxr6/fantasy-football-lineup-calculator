package roach.ryan.ff.fanduel;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

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
    private static final AtomicInteger COUNT = new AtomicInteger(1);
    private final List<Quarterback> qbs;
    private final List<RunningBack> rb1s;
    private final List<RunningBack> rb2s;
    private final List<WideReceiver> wr1s;
    private final List<WideReceiver> wr2s;
    private final List<WideReceiver> wr3s;
    private final List<TightEnd> tes;
    private final List<Defense> defs;

    private FanDuel(FreeAgentPool pool, List<Quarterback> forcedQb, List<RunningBack> forcedRb1,
            List<RunningBack> forcedRb2, List<WideReceiver> forcedWr1, List<WideReceiver> forcedWr2,
            List<WideReceiver> forcedWr3, List<TightEnd> forcedTe, List<Defense> forcedDef)
    {
        this.qbs = Optional.ofNullable(forcedQb).orElse(pool.getQuarterbacks());
        this.rb1s = Optional.ofNullable(forcedRb1).orElse(pool.getRunningBacks());
        this.rb2s = Optional.ofNullable(forcedRb2).orElse(pool.getRunningBacks());
        this.wr1s = Optional.ofNullable(forcedWr1).orElse(pool.getWideReceivers());
        this.wr2s = Optional.ofNullable(forcedWr2).orElse(pool.getWideReceivers());
        this.wr3s = Optional.ofNullable(forcedWr3).orElse(pool.getWideReceivers());
        this.tes = Optional.ofNullable(forcedTe).orElse(pool.getTightEnds());
        this.defs = Optional.ofNullable(forcedDef).orElse(pool.getDefenses());
    }

    public TopTeams getTopTeams(List<Flex> flexes)
    {
        TopTeams teams = new TopTeams(100);
        for (Flex flex : flexes)
        {
            System.out.println(COUNT.getAndIncrement());
            for (int i = 0; i < rb1s.size(); i++)
            {
                RunningBack rb1 = rb1s.get(i);
                for (int j = i + 1; j < rb2s.size(); j++)
                {
                    RunningBack rb2 = rb2s.get(j);
                    for (int x = 0; x < wr1s.size(); x++)
                    {
                        WideReceiver wr1 = wr1s.get(x);
                        for (int y = x + 1; y < wr2s.size(); y++)
                        {
                            WideReceiver wr2 = wr2s.get(y);
                            for (int z = y + 1; z < wr3s.size(); z++)
                            {
                                WideReceiver wr3 = wr3s.get(z);
                                if (rb1.equals(flex) || rb2.equals(flex) || wr1.equals(flex) || wr2.equals(flex)
                                        || wr3.equals(flex))
                                {
                                    continue;
                                }
                                for (Quarterback qb : qbs)
                                {
                                    for (TightEnd te : tes)
                                    {
                                        for (Defense def : defs)
                                        {
                                            if (sumSalary(qb, rb1, rb2, wr1, wr2, wr3, te, flex, def) > FanDuelTeam
                                                    .getSalaryCap())
                                            {
                                                continue;
                                            }
                                            teams.add(new FanDuelTeam(qb, rb1, rb2, wr1, wr2, wr3, te, flex, def));

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

    public static class Builder
    {
        private final FreeAgentPool pool;
        private List<Quarterback> qb;
        private List<RunningBack> rb1;
        private List<RunningBack> rb2;
        private List<WideReceiver> wr1;
        private List<WideReceiver> wr2;
        private List<WideReceiver> wr3;
        private List<TightEnd> te;
        private List<Defense> def;

        public Builder(FreeAgentPool pool)
        {
            this.pool = pool;
        }

        public Builder withQuarterback(Quarterback qb)
        {
            this.qb = Collections.singletonList(qb);
            return this;
        }

        public Builder withRunningBack1(RunningBack rb1)
        {
            this.rb1 = Collections.singletonList(rb1);
            return this;
        }

        public Builder withRunningBack2(RunningBack rb2)
        {
            this.rb2 = Collections.singletonList(rb2);
            return this;
        }

        public Builder withWideReceiver1(WideReceiver wr1)
        {
            this.wr1 = Collections.singletonList(wr1);
            return this;
        }

        public Builder withWideReceiver2(WideReceiver wr2)
        {
            this.wr2 = Collections.singletonList(wr2);
            return this;
        }

        public Builder withWideReceiver3(WideReceiver wr3)
        {
            this.wr3 = Collections.singletonList(wr3);
            return this;
        }

        public Builder withTightEnd(TightEnd te)
        {
            this.te = Collections.singletonList(te);
            return this;
        }

        public Builder withDefense(Defense def)
        {
            this.def = Collections.singletonList(def);
            return this;
        }

        public FanDuel build()
        {
            return new FanDuel(pool, qb, rb1, rb2, wr1, wr2, wr3, te, def);
        }
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
