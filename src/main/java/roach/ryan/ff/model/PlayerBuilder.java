package roach.ryan.ff.model;

import java.util.Objects;

public class PlayerBuilder
{
    private final String name;
    private String positionDisplay;
    private int salary;
    private int rank;
    private double projectedPoints;
    private double actualPoints;
    private double ownership;
    private String gameTime;
    private int rankMin;
    private int rankMax;
    private String team;
    private String opponent;

    public PlayerBuilder(String name)
    {
        this.name = name;
    }

    public PlayerBuilder withPositionDisplay(String positionDisplay)
    {
        this.positionDisplay = positionDisplay;
        return this;
    }

    public PlayerBuilder withSalary(int salary)
    {
        this.salary = salary;
        return this;
    }

    public PlayerBuilder withRank(int rank)
    {
        this.rank = rank;
        return this;
    }

    public PlayerBuilder withProjectPoints(double projectedPoints)
    {
        this.projectedPoints = projectedPoints;
        return this;
    }

    public PlayerBuilder withActualPoints(double actualPoints)
    {
        this.actualPoints = actualPoints;
        return this;
    }

    public PlayerBuilder withOwnership(double ownership)
    {
        this.ownership = ownership;
        return this;
    }

    public PlayerBuilder withGameTime(String gameTime)
    {
        this.gameTime = gameTime;
        return this;
    }

    public PlayerBuilder withRankMin(int rankMin)
    {
        this.rankMin = rankMin;
        return this;
    }

    public PlayerBuilder withRankMax(int rankMax)
    {
        this.rankMax = rankMax;
        return this;
    }

    public PlayerBuilder withTeam(String team)
    {
        this.team = team;
        return this;
    }

    public PlayerBuilder withOpponent(String opponent)
    {
        this.opponent = opponent;
        return this;
    }

    public Quarterback createQuarterback()
    {
        return newPlayer();
    }

    public RunningBack createRunningBack()
    {
        return newPlayer();
    }

    public WideReceiver createWideReceiver()
    {
        return newPlayer();
    }

    public TightEnd createTightEnd()
    {
        return newPlayer();
    }

    public Kicker createKicker()
    {
        return newPlayer();
    }

    public Defense createDefense()
    {
        return newPlayer();
    }

    public Player createPlayer()
    {
        return newPlayer();
    }

    private PlayerImpl newPlayer()
    {

        return new PlayerImpl(name, positionDisplay, salary, rank, projectedPoints, actualPoints, ownership, gameTime,
                rankMin, rankMax, team, opponent);
    }

    private static class PlayerImpl implements Quarterback, RunningBack, WideReceiver, TightEnd, Kicker, Defense
    {
        private final String name_;
        private final String positionDisplay_;
        private final int salary_;
        private final int rank_;
        private final double projectedPoints_;
        private final double actualPoints_;
        private final double ownership_;
        private final String gameTime_;
        private final int rankMin_;
        private final int rankMax_;
        private final String team_;
        private final String opponent_;

        public PlayerImpl(String name, String positionDisplay, int salary, int rank, double projectedPoints,
                double actualPoints, double ownership, String gameTime, int rankMin, int rankMax, String team,
                String opponent)
        {
            this.name_ = name;
            this.positionDisplay_ = positionDisplay;
            this.salary_ = salary;
            this.rank_ = rank;
            this.projectedPoints_ = projectedPoints;
            this.actualPoints_ = actualPoints;
            this.ownership_ = ownership;
            this.gameTime_ = gameTime;
            this.rankMin_ = rankMin;
            this.rankMax_ = rankMax;
            this.team_ = team;
            this.opponent_ = opponent;
        }

        @Override
        public String getName()
        {
            return name_;
        }

        @Override
        public String getPositionDisplay()
        {
            return positionDisplay_;
        }

        @Override
        public int getSalary()
        {
            return salary_;
        }

        @Override
        public int getRank()
        {
            return rank_;
        }

        @Override
        public double getProjectedPoints()
        {
            return projectedPoints_;
        }

        @Override
        public double getActualPoints()
        {
            return actualPoints_;
        }

        @Override
        public double getOwnership()
        {
            return ownership_;
        }

        @Override
        public String getGameTime()
        {
            return gameTime_;
        }

        @Override
        public int getRankMin()
        {
            return rankMin_;
        }

        @Override
        public int getRankMax()
        {
            return rankMax_;
        }

        @Override
        public String getTeam()
        {
            return team_;
        }

        @Override
        public String getOpponent()
        {
            return opponent_;
        }

        @Override
        public String toCsv()
        {
            // line format should be "1,QB,8700,Patrick Mahomes II,25.2,37.7,7.8,Sun 8:20PM"
            return String.format("%d,%s,%d,%s,%.1f,%.2f,%.1f,%s,%d,%d,%s,%s", rank_, positionDisplay_, salary_, name_,
                    projectedPoints_, actualPoints_, ownership_, gameTime_, rankMin_, rankMax_, team_, opponent_);
        }

        @Override
        public String toString()
        {
            return String.format("[%25s, %4s, $%,6d, %4.1f, %3d, %4.1f%%, %12s]", name_, positionDisplay_, salary_,
                    projectedPoints_, rank_, ownership_, gameTime_);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(name_, projectedPoints_, salary_);
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            PlayerImpl other = (PlayerImpl) obj;
            return Objects.equals(name_, other.name_)
                    && Double.doubleToLongBits(projectedPoints_) == Double.doubleToLongBits(other.projectedPoints_)
                    && salary_ == other.salary_;
        }
    }
}
