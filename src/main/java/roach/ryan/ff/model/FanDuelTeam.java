package roach.ryan.ff.model;

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
        if (getSalary() > getSalaryCap())
        {
            throw new IllegalArgumentException("salary exceeds cap");
        }
    }

    @Override
    public double getPoints()
    {
        return qb.getPoints() + rb1.getPoints() + rb2.getPoints() + wr1.getPoints() + wr2.getPoints() + wr3.getPoints()
                + te.getPoints() + flex.getPoints() + def.getPoints();
    }

    @Override
    public int getSalary()
    {
        return qb.getSalary() + rb1.getSalary() + rb2.getSalary() + wr1.getSalary() + wr2.getSalary() + wr3.getSalary()
                + te.getSalary() + flex.getSalary() + def.getSalary();
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder("Points: ");
        sb.append(getPoints());
        sb.append(" - Salary: ");
        sb.append(getSalary());
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

}
