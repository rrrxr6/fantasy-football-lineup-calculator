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

    public Quarterback createQuarterback()
    {
        return new PlayerImpl(name, positionDisplay, salary, rank, projectedPoints, actualPoints, ownership, gameTime);
    }

    public RunningBack createRunningBack()
    {
        return new PlayerImpl(name, positionDisplay, salary, rank, projectedPoints, actualPoints, ownership, gameTime);
    }

    public WideReceiver createWideReceiver()
    {
        return new PlayerImpl(name, positionDisplay, salary, rank, projectedPoints, actualPoints, ownership, gameTime);
    }

    public TightEnd createTightEnd()
    {
        return new PlayerImpl(name, positionDisplay, salary, rank, projectedPoints, actualPoints, ownership, gameTime);
    }

    public Kicker createKicker()
    {
        return new PlayerImpl(name, positionDisplay, salary, rank, projectedPoints, actualPoints, ownership, gameTime);
    }

    public Defense createDefense()
    {
        return new PlayerImpl(name, positionDisplay, salary, rank, projectedPoints, actualPoints, ownership, gameTime);
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

        public PlayerImpl(String name, String positionDisplay, int salary, int rank, double projectedPoints,
                double actualPoints, double ownership, String gameTime)
        {
            this.name_ = name;
            this.positionDisplay_ = positionDisplay;
            this.salary_ = salary;
            this.rank_ = rank;
            this.projectedPoints_ = projectedPoints;
            this.actualPoints_ = actualPoints;
            this.ownership_ = ownership;
            this.gameTime_ = gameTime;
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
