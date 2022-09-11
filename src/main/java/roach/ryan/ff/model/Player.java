package roach.ryan.ff.model;

import java.util.Objects;

public class Player
{
    private final String name;
    private final Position position;
    private final int salary;
    private final double points;

    public Player(String name, Position position, int salary, double points)
    {
        this.name = name;
        this.position = position;
        this.salary = salary;
        this.points = points;
    }

    public String getName()
    {
        return name;
    }

    public Position getPosition()
    {
        return position;
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
    public String toString() {
        return String.format("[%25s, %4s, $%,6d, %4.1f]", name, position.getDisplay(), salary, points);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name, points, position, salary);
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
                && Double.doubleToLongBits(points) == Double.doubleToLongBits(other.points)
                && position == other.position && salary == other.salary;
    }
}
