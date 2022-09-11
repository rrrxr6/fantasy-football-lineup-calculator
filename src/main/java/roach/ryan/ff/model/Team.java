package roach.ryan.ff.model;

import java.util.HashSet;
import java.util.Set;

public class Team
{
    private final int salaryCap;
    private int salary = 0;
    private Player qb;
    private Player rb1;
    private Player rb2;
    private Player wr1;
    private Player wr2;
    private Player wr3;
    private Player te;
    private Player flex;
    private Player dst;

    public Team(int salayCap)
    {
        this.salaryCap = salayCap;
    }

    public boolean setQuarterback(Player qb)
    {
        if (!isValid(this.qb, qb, Position.QB))
        {
            return false;
        }

        updateSalary(this.qb, qb);
        this.qb = qb;
        return true;
    }

    public boolean setRunningBack1(Player rb1)
    {
        if (!isValid(this.rb1, rb1, Position.RB) || rb1.equals(rb2))
        {
            return false;
        }

        updateSalary(this.rb1, rb1);
        this.rb1 = rb1;
        return true;
    }

    public boolean setRunningBack2(Player rb2)
    {
        if (!isValid(this.rb2, rb2, Position.RB) || rb2.equals(rb1))
        {
            return false;
        }

        updateSalary(this.rb2, rb2);
        this.rb2 = rb2;
        return true;
    }

    public boolean setWideReceiver1(Player wr1)
    {
        if (!isValid(this.wr1, wr1, Position.WR) || wr1.equals(wr2) || wr1.equals(wr3))
        {
            return false;
        }

        updateSalary(this.wr1, wr1);
        this.wr1 = wr1;
        return true;
    }

    public boolean setWideReceiver2(Player wr2)
    {
        if (!isValid(this.wr2, wr2, Position.WR) || wr2.equals(wr1) || wr2.equals(wr3))
        {
            return false;
        }

        updateSalary(this.wr2, wr2);
        this.wr2 = wr2;
        return true;
    }

    public boolean setWideReceiver3(Player wr3)
    {
        if (!isValid(this.wr3, wr3, Position.WR) || wr3.equals(wr1) || wr3.equals(wr2))
        {
            return false;
        }

        updateSalary(this.wr3, wr3);
        this.wr3 = wr3;
        return true;
    }

    public boolean setTightEnd(Player te)
    {
        if (!isValid(this.te, te, Position.TE))
        {
            return false;
        }

        updateSalary(this.te, te);
        this.te = te;
        return true;
    }

    public boolean setDefense(Player dst)
    {
        if (!isValid(this.dst, dst, Position.ST))
        {
            return false;
        }

        updateSalary(this.dst, dst);
        this.dst = dst;
        return true;
    }

    public boolean setFlex(Player flex)
    {
        if ((flex.getPosition() != Position.RB && flex.getPosition() != Position.WR) || flex.equals(rb1)
                || flex.equals(rb2) || flex.equals(wr1) || flex.equals(wr2) || flex.equals(wr3))
        {
            return false;
        }
        int correction = this.flex == null ? 0 : this.flex.getSalary();
        if (salary - correction + flex.getSalary() > salaryCap)
        {
            return false;
        }

        updateSalary(this.flex, flex);
        this.flex = flex;
        return true;
    }

    public double getPoints()
    {
        return qb.getPoints() + rb1.getPoints() + rb2.getPoints() + wr1.getPoints() + wr2.getPoints() + wr3.getPoints()
                + te.getPoints() + flex.getPoints() + dst.getPoints();
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder("Points: ");
        sb.append(getPoints());
        sb.append(" - Salary: ");
        sb.append(salary);
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

        sb.append(dst);
        sb.append("\n");

        return sb.toString();
    }

    private boolean isValid(Player oldPlayer, Player newPlayer, Position position)
    {
        return newPlayer.getPosition() == position && !isOverCap(oldPlayer, newPlayer);
    }

    private boolean isOverCap(Player oldPlayer, Player newPlayer)
    {
        int correction = oldPlayer == null ? 0 : oldPlayer.getSalary();
        return salary - correction + newPlayer.getSalary() > salaryCap;
    }

    private void updateSalary(Player oldPlayer, Player newPlayer)
    {
        if (oldPlayer != null)
        {
            salary -= oldPlayer.getSalary();
        }
        salary += newPlayer.getSalary();
    }

    public Team copy()
    {
        Team team = new Team(salaryCap);
        team.setQuarterback(qb);
        team.setRunningBack1(rb1);
        team.setRunningBack2(rb2);
        team.setWideReceiver1(wr1);
        team.setWideReceiver2(wr2);
        team.setWideReceiver3(wr3);
        team.setTightEnd(te);
        team.setFlex(flex);
        team.setDefense(dst);
        return team;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dst == null) ? 0 : dst.hashCode());
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
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Team other = (Team) obj;
        Set<Player> otherPlayers = new HashSet<>(9);
        otherPlayers.add(other.qb);
        otherPlayers.add(other.rb1);
        otherPlayers.add(other.rb2);
        otherPlayers.add(other.wr1);
        otherPlayers.add(other.wr2);
        otherPlayers.add(other.wr3);
        otherPlayers.add(other.te);
        otherPlayers.add(other.flex);
        otherPlayers.add(other.dst);

        Set<Player> players = new HashSet<>(9);
        players.add(qb);
        players.add(rb1);
        players.add(rb2);
        players.add(wr1);
        players.add(wr2);
        players.add(wr3);
        players.add(te);
        players.add(flex);
        players.add(dst);
        return players.equals(otherPlayers);
    }
}
