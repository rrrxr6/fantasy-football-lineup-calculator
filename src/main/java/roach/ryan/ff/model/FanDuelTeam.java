package roach.ryan.ff.model;

import java.util.Set;
import java.util.stream.IntStream;

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
    private double points = 0;
    private int salary = 0;
    private double averageRank = 0;

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
    public double getPoints()
    {
        if (points == 0)
        {
            points = qb.getPoints() + rb1.getPoints() + rb2.getPoints() + wr1.getPoints() + wr2.getPoints()
                    + wr3.getPoints() + te.getPoints() + flex.getPoints() + def.getPoints();
        }
        return points;
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
    public String toString()
    {
        StringBuilder sb = new StringBuilder("Points: ");
        sb.append(getPoints());
        sb.append(" - Salary: ");
        sb.append(getSalary());
        sb.append(" - Avg Rank: ");
        sb.append(getAverageRank());
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

    public static int getSalaryCap()
    {
        return 60000;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((def == null) ? 0 : def.hashCode());
        result = prime * result + ((flex == null) ? 0 : flex.hashCode());
        result = prime * result + ((qb == null) ? 0 : qb.hashCode());
        result = prime * result + ((rb1 == null) ? 0 : rb1.hashCode());
        result = prime * result + ((rb2 == null) ? 0 : rb2.hashCode());
        result = prime * result + ((te == null) ? 0 : te.hashCode());
        result = prime * result + ((wr1 == null) ? 0 : wr1.hashCode());
        result = prime * result + ((wr2 == null) ? 0 : wr2.hashCode());
        result = prime * result + ((wr3 == null) ? 0 : wr3.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        FanDuelTeam other = (FanDuelTeam) obj;
        return Set.of(qb, rb1, rb2, wr1, wr2, wr3, te, def, flex).equals(Set.of(other.qb, other.rb1, other.rb2,
                other.wr1, other.wr2, other.wr3, other.te, other.def, other.flex));

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

}
