package roach.ryan.ff.fanduel;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import roach.ryan.ff.data.FreeAgentPool;
import roach.ryan.ff.model.Flex;
import roach.ryan.ff.model.Player;
import roach.ryan.ff.model.Team;
import roach.ryan.ff.model.TopTeams;

public class FanDuelSingleGameOptimizer
{
    private static final AtomicInteger COUNT = new AtomicInteger(1);
    private final List<Player> p1s;
    private final List<Player> p2s;
    private final List<Player> p3s;
    private final List<Player> p4s;
    private final List<Player> p5s;

    private FanDuelSingleGameOptimizer(FreeAgentPool pool, List<Player> overrideP1, List<Player> overrideP2,
            List<Player> overrideP3, List<Player> overrideP4, List<Player> overrideP5)
    {
        p1s = Optional.ofNullable(overrideP1).orElse(pool.getPlayers());
        p2s = Optional.ofNullable(overrideP2).orElse(pool.getPlayers());
        p3s = Optional.ofNullable(overrideP3).orElse(pool.getPlayers());
        p4s = Optional.ofNullable(overrideP4).orElse(pool.getPlayers());
        p5s = Optional.ofNullable(overrideP5).orElse(pool.getPlayers());
    }

    public TopTeams getTopTeams(List<Flex> flexes, Comparator<Team> comparator)
    {
        TopTeams teams = new TopTeams(50, comparator);
        for (int index1 = 0; index1 < p1s.size(); index1++)
        {
            Player p1 = p1s.get(index1);
            for (int index2 = p2s.size() == 1 ? 0 : index1 + 1; index2 < p2s.size(); index2++)
            {
                Player p2 = p2s.get(index2);
                for (int index3 = p3s.size() == 1 ? 0 : index2 + 1; index3 < p3s.size(); index3++)
                {
                    Player p3 = p3s.get(index3);
                    for (int index4 = p4s.size() == 1 ? 0 : index3 + 1; index4 < p4s.size(); index4++)
                    {
                        Player p4 = p4s.get(index4);
                        for (int index5 = p5s.size() == 1 ? 0 : index4 + 1; index5 < p5s.size(); index5++)
                        {
                            Player p5 = p5s.get(index5);
                            List<Player> players = Arrays.asList(p1, p2, p3, p4, p5);
                            if (players.size() != new HashSet<>(players).size())
                            {
                                continue;
                            }
                            if (sumSalary(p1, p2, p3, p4, p5) > FanDuelSingleGameTeam.getSalaryCap())
                            {
                                continue;
                            }
                            teams.add(new FanDuelSingleGameTeam(p1, p2, p3, p4, p5));

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
        private List<Player> _p1;
        private List<Player> _p2;
        private List<Player> _p3;
        private List<Player> _p4;
        private List<Player> _p5;

        public Builder(FreeAgentPool pool)
        {
            this.pool = pool;
        }

        public Builder withPlayer1(Player p1)
        {
            this._p1 = Collections.singletonList(p1);
            return this;
        }

        public Builder withPlayer1(int index)
        {
            return withPlayer1(pool.getPlayers().get(index));
        }

        public Builder withPlayer2(Player rb2)
        {
            this._p2 = Collections.singletonList(rb2);
            return this;
        }

        public Builder withPlayer2(int index)
        {
            return withPlayer2(pool.getPlayers().get(index));
        }

        public Builder withPlayer3(Player p3)
        {
            this._p3 = Collections.singletonList(p3);
            return this;
        }

        public Builder withPlayer3(int index)
        {
            return withPlayer3(pool.getPlayers().get(index));
        }

        public Builder withPlayer4(Player p4)
        {
            this._p4 = Collections.singletonList(p4);
            return this;
        }

        public Builder withPlayer4(int index)
        {
            return withPlayer4(pool.getPlayers().get(index));
        }

        public Builder withPlayer5(Player p5)
        {
            this._p5 = Collections.singletonList(p5);
            return this;
        }

        public Builder withPlayer5(int index)
        {
            return withPlayer5(pool.getPlayers().get(index));
        }

        public FanDuelSingleGameOptimizer build()
        {
            return new FanDuelSingleGameOptimizer(pool, _p1, _p2, _p3, _p4, _p5);
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
