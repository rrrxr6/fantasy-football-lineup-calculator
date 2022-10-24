package roach.ryan.ff.fanduel;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import roach.ryan.ff.data.FreeAgentPool;
import roach.ryan.ff.model.Defense;
import roach.ryan.ff.model.Flex;
import roach.ryan.ff.model.Player;
import roach.ryan.ff.model.Quarterback;
import roach.ryan.ff.model.RunningBack;
import roach.ryan.ff.model.Team;
import roach.ryan.ff.model.TightEnd;
import roach.ryan.ff.model.TopTeams;
import roach.ryan.ff.model.WideReceiver;

public class FanDuelFullRosterOptimizer
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
    private final int progress;

    private FanDuelFullRosterOptimizer(FreeAgentPool pool, List<Quarterback> overrideQb, List<RunningBack> overrideRb1,
            List<RunningBack> overrideRb2, List<WideReceiver> overrideWr1, List<WideReceiver> overrideWr2,
            List<WideReceiver> overrideWr3, List<TightEnd> overrideTe, List<Defense> overrideDef)
    {
        qbs = Optional.ofNullable(overrideQb).orElse(pool.getQuarterbacks());
        rb1s = Optional.ofNullable(overrideRb1).orElse(pool.getRunningBacks());
        rb2s = Optional.ofNullable(overrideRb2).orElse(pool.getRunningBacks());
        wr1s = Optional.ofNullable(overrideWr1).orElse(pool.getWideReceivers());
        wr2s = Optional.ofNullable(overrideWr2).orElse(pool.getWideReceivers());
        wr3s = Optional.ofNullable(overrideWr3).orElse(pool.getWideReceivers());
        tes = Optional.ofNullable(overrideTe).orElse(pool.getTightEnds());
        defs = Optional.ofNullable(overrideDef).orElse(pool.getDefenses());
        progress = pool.getFlexes().size();
    }

    public TopTeams getTopTeams(List<Flex> flexes, Comparator<Team> comparator)
    {
        if (size(flexes).compareTo(new BigInteger("500000000000")) > 0)
        {
            System.out.println("Player pool is too large. Lineup calculator will take over five minutes to complete.");
            System.exit(0);
        }

        TopTeams teams = new TopTeams(50, comparator);
        for (Flex flex : flexes)
        {
            System.out.print("(" + COUNT.getAndIncrement() + "/" + progress + ")\r");
            for (int i = 0; i < rb1s.size(); i++)
            {
                RunningBack rb1 = rb1s.get(i);
                for (int j = rb2s.size() == 1 ? 0 : i + 1; j < rb2s.size(); j++)
                {
                    RunningBack rb2 = rb2s.get(j);
                    for (int x = 0; x < wr1s.size(); x++)
                    {
                        WideReceiver wr1 = wr1s.get(x);
                        for (int y = wr2s.size() == 1 ? 0 : x + 1; y < wr2s.size(); y++)
                        {
                            WideReceiver wr2 = wr2s.get(y);
                            for (int z = wr3s.size() == 1 ? 0 : y + 1; z < wr3s.size(); z++)
                            {
                                WideReceiver wr3 = wr3s.get(z);
                                if (rb1.equals(flex) || rb2.equals(flex) || wr1.equals(flex) || wr2.equals(flex)
                                        || wr3.equals(flex) || rb1.equals(rb2) || wr1.equals(wr2) || wr1.equals(wr3)
                                        || wr2.equals(wr3))
                                {
                                    continue;
                                }
                                for (Quarterback qb : qbs)
                                {
                                    for (TightEnd te : tes)
                                    {
                                        for (Defense def : defs)
                                        {
                                            if (sumSalary(qb, rb1, rb2, wr1, wr2, wr3, te, flex,
                                                    def) > FanDuelFullRosterTeam.getSalaryCap())
                                            {
                                                continue;
                                            }
                                            teams.add(new FanDuelFullRosterTeam(qb, rb1, rb2, wr1, wr2, wr3, te, flex,
                                                    def));

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

    private BigInteger size(List<Flex> flexes)
    {
        BigInteger qbCount = BigInteger.valueOf(qbs.size());
        BigInteger rb1Count = BigInteger.valueOf(rb1s.size());
        BigInteger rb2Count = BigInteger.valueOf(rb2s.size());
        BigInteger wr1Count = BigInteger.valueOf(wr1s.size());
        BigInteger wr2Count = BigInteger.valueOf(wr2s.size());
        BigInteger wr3Count = BigInteger.valueOf(wr3s.size());
        BigInteger teCount = BigInteger.valueOf(tes.size());
        BigInteger flexCount = BigInteger.valueOf(flexes.size());
        BigInteger defCount = BigInteger.valueOf(defs.size());
        return qbCount.multiply(rb1Count).multiply(rb2Count).multiply(wr1Count).multiply(wr2Count).multiply(wr3Count)
                .multiply(teCount).multiply(flexCount).multiply(defCount);
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

        public Builder withQuarterback(int index)
        {
            return withQuarterback(pool.getQuarterbacks().get(index));
        }

        public Builder withRunningBack1(RunningBack rb1)
        {
            this.rb1 = Collections.singletonList(rb1);
            return this;
        }

        public Builder withRunningBack1(int index)
        {
            return withRunningBack1(pool.getRunningBacks().get(index));
        }

        public Builder withRunningBack2(RunningBack rb2)
        {
            this.rb2 = Collections.singletonList(rb2);
            return this;
        }

        public Builder withRunningBack2(int index)
        {
            return withRunningBack2(pool.getRunningBacks().get(index));
        }

        public Builder withWideReceiver1(WideReceiver wr1)
        {
            this.wr1 = Collections.singletonList(wr1);
            return this;
        }

        public Builder withWideReceiver1(int index)
        {
            return withWideReceiver1(pool.getWideReceivers().get(index));
        }

        public Builder withWideReceiver2(WideReceiver wr2)
        {
            this.wr2 = Collections.singletonList(wr2);
            return this;
        }

        public Builder withWideReceiver2(int index)
        {
            return withWideReceiver2(pool.getWideReceivers().get(index));
        }

        public Builder withWideReceiver3(WideReceiver wr3)
        {
            this.wr3 = Collections.singletonList(wr3);
            return this;
        }

        public Builder withWideReceiver3(int index)
        {
            return withWideReceiver3(pool.getWideReceivers().get(index));
        }

        public Builder withTightEnd(TightEnd te)
        {
            this.te = Collections.singletonList(te);
            return this;
        }

        public Builder withTightEnd(int index)
        {
            return withTightEnd(pool.getTightEnds().get(index));
        }

        public Builder withDefense(Defense def)
        {
            this.def = Collections.singletonList(def);
            return this;
        }

        public Builder withDefense(int index)
        {
            return withDefense(pool.getDefenses().get(index));
        }

        public FanDuelFullRosterOptimizer build()
        {
            return new FanDuelFullRosterOptimizer(pool, qb, rb1, rb2, wr1, wr2, wr3, te, def);
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
