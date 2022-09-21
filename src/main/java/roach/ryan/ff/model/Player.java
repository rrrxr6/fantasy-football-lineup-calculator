package roach.ryan.ff.model;

import java.util.Objects;

public abstract class Player
{
    private final String name;
    private final int salary;
    private final int rank;
    private final double projectedPoints;
    private final double actualPoints;

    public Player(String name, int salary, int rank, double projectedPoints, double actualPoints)
    {
        this.name = name;
        this.salary = salary;
        this.rank = rank;
        this.projectedPoints = projectedPoints;
        this.actualPoints = actualPoints;
    }

    public String getName()
    {
        return name;
    }

    public int getSalary()
    {
        return salary;
    }

    public int getRank()
    {
        return rank;
    }

    public double getProjectedPoints()
    {
        return projectedPoints;
    }

    public double getActualPoints()
    {
        return actualPoints;
    }

    @Override
    public String toString()
    {
        return String.format("[%25s, %13s, $%,6d, %4.1f, %3d]", name, getClass().getSimpleName(), salary,
                projectedPoints, rank);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name, projectedPoints, salary);
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
        Player other = (Player) obj;
        return Objects.equals(name, other.name)
                && Double.doubleToLongBits(projectedPoints) == Double.doubleToLongBits(other.projectedPoints)
                && salary == other.salary;
    }
}
