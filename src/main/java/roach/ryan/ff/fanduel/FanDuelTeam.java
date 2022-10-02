package roach.ryan.ff.fanduel;

import java.util.Objects;
import java.util.Set;
import java.util.stream.IntStream;

import roach.ryan.ff.model.Defense;
import roach.ryan.ff.model.Flex;
import roach.ryan.ff.model.Quarterback;
import roach.ryan.ff.model.RunningBack;
import roach.ryan.ff.model.Team;
import roach.ryan.ff.model.TightEnd;
import roach.ryan.ff.model.WideReceiver;

public class FanDuelTeam implements Team
{
    private final Quarterback qb;
    private final RunningBack rb1;
    private final RunningBack rb2;
    private final WideReceiver wr1;
    private final WideReceiver wr2;
    private final WideReceiver wr3;
    private final TightEnd te;
    private final Flex flex;
    private final Defense def;
    private int salary = 0;
    private double averageRank = 0;
    private double projectedPoints = 0;
    private double actualPoints = 0;
    private double metric = 0;

    public FanDuelTeam(Quarterback qb, RunningBack rb1, RunningBack rb2, WideReceiver wr1, WideReceiver wr2,
            WideReceiver wr3, TightEnd te, Flex flex, Defense def)
    {
        this.qb = qb;
        this.rb1 = rb1;
        this.rb2 = rb2;
        this.wr1 = wr1;
        this.wr2 = wr2;
        this.wr3 = wr3;
        this.te = te;
        this.flex = flex;
        this.def = def;
    }

    @Override
    public int getSalary()
    {
        if (salary == 0)
        {
            salary = qb.getSalary() + rb1.getSalary() + rb2.getSalary() + wr1.getSalary() + wr2.getSalary()
                    + wr3.getSalary() + te.getSalary() + flex.getSalary() + def.getSalary();
        }
        return salary;
    }

    @Override
    public double getAverageRank()
    {
        if (averageRank == 0)
        {
            averageRank = IntStream.of(qb.getRank(), rb1.getRank(), rb2.getRank(), wr1.getRank(), wr2.getRank(),
                    wr3.getRank(), te.getRank(), flex.getRank(), def.getRank()).average().getAsDouble();
        }
        return averageRank;
    }

    @Override
    public double getProjectedPoints()
    {
        if (projectedPoints == 0)
        {
            projectedPoints = qb.getProjectedPoints() + rb1.getProjectedPoints() + rb2.getProjectedPoints()
                    + wr1.getProjectedPoints() + wr2.getProjectedPoints() + wr3.getProjectedPoints()
                    + te.getProjectedPoints() + flex.getProjectedPoints() + def.getProjectedPoints();
        }
        return projectedPoints;
    }

    @Override
    public double getActualPoints()
    {
        if (actualPoints == 0)
        {
            actualPoints = qb.getActualPoints() + rb1.getActualPoints() + rb2.getActualPoints() + wr1.getActualPoints()
                    + wr2.getActualPoints() + wr3.getActualPoints() + te.getActualPoints() + flex.getActualPoints()
                    + def.getActualPoints();
        }
        return actualPoints;
    }

    @Override
    public double getMetric()
    {
        if (metric == 0)
        {
            metric = 4 * getProjectedPoints() - getAverageRank();
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
        sb.append(qb.getRank());
        sb.append(",");
        sb.append(rb1.getRank());
        sb.append(",");
        sb.append(rb2.getRank());
        sb.append(",");
        sb.append(wr1.getRank());
        sb.append(",");
        sb.append(wr2.getRank());
        sb.append(",");
        sb.append(wr3.getRank());
        sb.append(",");
        sb.append(te.getRank());
        sb.append(",");
        sb.append(flex.getRank());
        sb.append(",");
        sb.append(def.getRank());
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

        sb.append(qb);
        sb.append("\n");

        sb.append(rb1);
        sb.append("\n");
        sb.append(rb2);
        sb.append("\n");

        sb.append(wr1);
        sb.append("\n");
        sb.append(wr2);
        sb.append("\n");
        sb.append(wr3);
        sb.append("\n");

        sb.append(te);
        sb.append("\n");

        sb.append(flex);
        sb.append("\n");

        sb.append(def);
        sb.append("\n");

        return sb.toString();
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(Set.of(qb, rb1, rb2, wr1, wr2, wr3, te, flex, def));
    }

    @Override
    public boolean equals(Object obj)
    {
        FanDuelTeam other = (FanDuelTeam) obj;
        return Set.of(qb, rb1, rb2, wr1, wr2, wr3, te, flex, def).equals(Set.of(other.qb, other.rb1, other.rb2,
                other.wr1, other.wr2, other.wr3, other.te, other.flex, other.def));

    }

    public static int getSalaryCap()
    {
        return 60000;
    }
}
