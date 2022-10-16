package roach.ryan.ff.fanduel;

import java.util.Objects;
import java.util.Set;

import roach.ryan.ff.model.Player;
import roach.ryan.ff.model.Team;

public class FanDuelSingleGameTeam implements Team
{
    private final Player p1;
    private final Player p2;
    private final Player p3;
    private final Player p4;
    private final Player p5;
    private int salary = 0;
    private double averageRank = 0;
    private double projectedPoints = 0;
    private double actualPoints = 0;
    private double metric = 0;

    public FanDuelSingleGameTeam(Player p1, Player p2, Player p3, Player p4, Player p5)
    {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
        this.p5 = p5;
    }

    @Override
    public int getSalary()
    {
        if (salary == 0)
        {
            salary = p1.getSalary() + p2.getSalary() + p3.getSalary() + p4.getSalary() + p5.getSalary();
        }
        return salary;
    }

    @Override
    public double getAverageRank()
    {
        if (averageRank == 0)
        {
            averageRank = (p1.getRank() + p2.getRank() + p3.getRank() + p4.getRank() + p5.getRank()) / 9.0;
        }
        return averageRank;
    }

    @Override
    public double getProjectedPoints()
    {
        if (projectedPoints == 0)
        {
            projectedPoints = p1.getProjectedPoints() + p2.getProjectedPoints() + p3.getProjectedPoints()
                    + p4.getProjectedPoints() + p5.getProjectedPoints();
        }
        return projectedPoints;
    }

    @Override
    public double getActualPoints()
    {
        if (actualPoints == 0)
        {
            actualPoints = p1.getActualPoints() + p2.getActualPoints() + p3.getActualPoints() + p4.getActualPoints()
                    + p5.getActualPoints();
        }
        return actualPoints;
    }

    @Override
    public double getMetric()
    {
        if (metric == 0)
        {
            metric = 1.5 * getProjectedPoints() - getAverageRank();
        }
        return metric;
    }

    @Override
    public String toStringCsv()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getAverageRank());
        sb.append(",");
        sb.append(getProjectedPoints());
        sb.append(",");
        sb.append(getActualPoints());
        sb.append(",");
        sb.append(p1.getRank());
        sb.append(",");
        sb.append(p2.getRank());
        sb.append(",");
        sb.append(p3.getRank());
        sb.append(",");
        sb.append(p4.getRank());
        sb.append(",");
        sb.append(p5.getRank());
        return sb.toString();
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder("Projected Points: ");
        sb.append(String.format("%.2f", getProjectedPoints()));
        if (getActualPoints() > 0)
        {
            sb.append(" - Actual Points: ");
            sb.append(String.format("%.2f", getActualPoints()));
        }
        sb.append("\n");
        sb.append("Salary: ");
        sb.append(getSalary());
        sb.append(" - Avg Rank: ");
        sb.append(String.format("%.2f", getAverageRank()));
        sb.append("\n");

        sb.append(p1);
        sb.append("\n");
        sb.append(p2);
        sb.append("\n");
        sb.append(p3);
        sb.append("\n");
        sb.append(p4);
        sb.append("\n");
        sb.append(p5);
        sb.append("\n");

        return sb.toString();
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(Set.of(p1, p2, p3, p4, p5));
    }

    @Override
    public boolean equals(Object obj)
    {
        FanDuelSingleGameTeam other = (FanDuelSingleGameTeam) obj;
        return Set.of(p1, p2, p3, p4, p5).equals(Set.of(other.p1, other.p2, other.p3, other.p4, other.p5));

    }

    public static int getSalaryCap()
    {
        return 60000;
    }
}
