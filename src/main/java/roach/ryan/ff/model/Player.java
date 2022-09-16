package roach.ryan.ff.model;

import java.util.Objects;

public abstract class Player
{
    private final String name;
    private final int salary;
    private final double points;
    private final int rank;

    public Player(String name, int salary, double points, int rank)
    {
        this.name = name;
        this.salary = salary;
        this.points = points;
        this.rank = rank;
    }

    public String getName()
    {
        return name;
    }

    public int getSalary()
    {
        return salary;
    }

    public double getPoints()
    {
        return points;
    }

    @Override
    public String toString()
    {
        return String.format("[%25s, %13s, $%,6d, %4.1f, %3d]", name, getClass().getSimpleName(), salary, points, rank);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name, points, salary);
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
                && Double.doubleToLongBits(points) == Double.doubleToLongBits(other.points) && salary == other.salary;
    }

    public int getRank()
    {
        return rank;
    }
}
