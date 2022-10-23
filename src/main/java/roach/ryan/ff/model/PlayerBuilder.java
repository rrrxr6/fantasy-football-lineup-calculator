package roach.ryan.ff.model;

import java.util.Objects;

public class PlayerBuilder
{
    private final String name;
    private int salary;
    private int rank;
    private double projectedPoints;
    private double actualPoints;

    public PlayerBuilder(String name)
    {
        this.name = name;
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

    public Quarterback createQuarterback()
    {
        return new PlayerImpl(name, salary, rank, projectedPoints, actualPoints);
    }

    public RunningBack createRunningBack()
    {
        return new PlayerImpl(name, salary, rank, projectedPoints, actualPoints);
    }

    public WideReceiver createWideReceiver()
    {
        return new PlayerImpl(name, salary, rank, projectedPoints, actualPoints);
    }

    public TightEnd createTightEnd()
    {
        return new PlayerImpl(name, salary, rank, projectedPoints, actualPoints);
    }

    public Kicker createKicker()
    {
        return new PlayerImpl(name, salary, rank, projectedPoints, actualPoints);
    }

    public Defense createDefense()
    {
        return new PlayerImpl(name, salary, rank, projectedPoints, actualPoints);
    }

    public PlayerBuilder withActualPoints(double actualPoints)
    {
        this.actualPoints = actualPoints;
        return this;
    }

    private static class PlayerImpl implements Quarterback, RunningBack, WideReceiver, TightEnd, Kicker, Defense
    {
        private final String name_;
        private final int salary_;
        private final int rank_;
        private final double projectedPoints_;
        private final double actualPoints_;

        public PlayerImpl(String name, int salary, int rank, double projectedPoints, double actualPoints)
        {
            this.name_ = name;
            this.salary_ = salary;
            this.rank_ = rank;
            this.projectedPoints_ = projectedPoints;
            this.actualPoints_ = actualPoints;
        }

        @Override
        public String getName()
        {
            return name_;
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
        public String toString()
        {
            return String.format("[%25s, %13s, $%,6d, %4.1f, %3d]", name_, getClass().getSimpleName(), salary_,
                    projectedPoints_, rank_);
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
