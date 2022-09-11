package roach.ryan.ff.model;

import static java.util.Comparator.comparing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FreeAgentPool
{
    private List<Player> qbs = new ArrayList<>();
    private  List<Player> rbs = new ArrayList<>();
    private List<Player> dsts = new ArrayList<>();
    private List<Player> tes = new ArrayList<>();
    private List<Player> wrs = new ArrayList<>();
    private final List<Player> flexes = new ArrayList<>();

    public FreeAgentPool(Collection<Player> players)
    {
        players.forEach(p ->
        {
            switch (p.getPosition())
            {
                case QB:
                    qbs.add(p);
                    break;
                case RB:
                    rbs.add(p);
                    break;
                case ST:
                    dsts.add(p);
                    break;
                case TE:
                    tes.add(p);
                    break;
                case WR:
                    wrs.add(p);
                    break;
                default:
                    System.out.println(String.format("Unknown position [%s] for [%s]. Player skipped.", p.getPosition(),
                            p.getName()));
            }
        });
        flexes.addAll(rbs);
        flexes.addAll(wrs);

        qbs.sort(comparing(Player::getPoints).reversed());
        List<Player> trimmedQbs = new ArrayList<>(qbs.size());
        for (Player qb : qbs)
        {
            if (trimmedQbs.isEmpty())
            {
                trimmedQbs.add(qb);
            }
            else if (qb.getSalary() < trimmedQbs.get(trimmedQbs.size() - 1).getSalary())
            {
                trimmedQbs.add(qb);
            }
        }
        qbs = trimmedQbs;

        rbs.sort(comparing(Player::getPoints).reversed());
        List<Player> trimmedRbs = new ArrayList<>(rbs.size());
        for (Player rb : rbs)
        {
            if (trimmedRbs.isEmpty())
            {
                trimmedRbs.add(rb);
            }
            else if (rb.getSalary() < trimmedRbs.get(trimmedRbs.size() - 1).getSalary() /*|| rb.getPoints() > 15*/)
            {
                trimmedRbs.add(rb);
            }
        }
        rbs = trimmedRbs;

        dsts.sort(comparing(Player::getPoints).reversed());
        List<Player> trimmedDsts = new ArrayList<>(dsts.size());
        for (Player dst : dsts)
        {
            if (trimmedDsts.isEmpty())
            {
                trimmedDsts.add(dst);
            }
            else if (dst.getSalary() < trimmedDsts.get(trimmedDsts.size() - 1).getSalary())
            {
                trimmedDsts.add(dst);
            }
        }
        dsts = trimmedDsts;

        tes.sort(comparing(Player::getPoints).reversed());
        List<Player> trimmedTes = new ArrayList<>(tes.size());
        for (Player te : tes)
        {
            if (trimmedTes.isEmpty())
            {
                trimmedTes.add(te);
            }
            else if (te.getSalary() < trimmedTes.get(trimmedTes.size() - 1).getSalary())
            {
                trimmedTes.add(te);
            }
        }
        tes = trimmedTes;

        wrs.sort(comparing(Player::getPoints).reversed());
        List<Player> trimmedWrs = new ArrayList<>(wrs.size());
        for (Player wr : wrs)
        {
            if (trimmedWrs.isEmpty())
            {
                trimmedWrs.add(wr);
            }
            else if (wr.getSalary() < trimmedWrs.get(trimmedWrs.size() - 1).getSalary() /*|| wr.getPoints() > 15*/)
            {
                trimmedWrs.add(wr);
            }
        }
        wrs = trimmedWrs;
    }

    public List<Player> getQuarterbacks()
    {
        return qbs;
    }

    public List<Player> getRunningBacks()
    {
        return rbs;
    }

    public List<Player> getDefenses()
    {
        return dsts;
    }

    public List<Player> getTightEnds()
    {
        return tes;
    }

    public List<Player> getWideReceivers()
    {

        return wrs;
    }

    public List<Player> getFlexes()
    {
        return flexes;
    }
}
