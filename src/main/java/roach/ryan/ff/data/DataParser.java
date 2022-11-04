package roach.ryan.ff.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import roach.ryan.ff.model.Defense;
import roach.ryan.ff.model.Flex;
import roach.ryan.ff.model.Kicker;
import roach.ryan.ff.model.Player;
import roach.ryan.ff.model.PlayerBuilder;
import roach.ryan.ff.model.Quarterback;
import roach.ryan.ff.model.RunningBack;
import roach.ryan.ff.model.TightEnd;
import roach.ryan.ff.model.WideReceiver;

public class DataParser
{
    private final List<Quarterback> qbs = new ArrayList<>();
    private final List<RunningBack> rbs = new ArrayList<>();
    private final List<WideReceiver> wrs = new ArrayList<>();
    private final List<TightEnd> tes = new ArrayList<>();
    private final List<Flex> flexes = new ArrayList<>();
    private final List<Defense> defs = new ArrayList<>();
    private final List<Kicker> ks = new ArrayList<>();

    public DataParser(File file)
    {
        try (Scanner scanner = new Scanner(file))
        {
            while (scanner.hasNext())
            {
                // line format should be "1,QB,8700,Patrick Mahomes II,25.2,37.7,7.8,Sun 8:20PM"
                String[] parts = scanner.nextLine().split(",");
                int rank = Integer.valueOf(parts[0].replaceAll("[^0-9]+", ""));
                if (rank == 0)
                {
                    continue;
                }
                String position = parts[1].replaceAll("[^A-Za-z]+", "");
                int salary = Integer.valueOf(parts[2]);
                String name = parts[3];
                double projectedPoints = Double.valueOf(parts[4]);
                double actualPoints = Double.valueOf(parts[5]);
                PlayerBuilder builder = new PlayerBuilder(name).withPositionDisplay(position).withSalary(salary)
                        .withRank(rank).withProjectPoints(projectedPoints).withActualPoints(actualPoints);
                if (parts.length == 7)
                {
                    builder.withOwnership(Double.valueOf(parts[6]));
                }
                if (parts.length == 8)
                {
                    builder.withOwnership(Double.valueOf(parts[6]));
                    builder.withGameTime(parts[7]);
                }
                switch (position)
                {
                    case "QB":
                        qbs.add(builder.createQuarterback());
                        break;
                    case "RB":
                        rbs.add(builder.createRunningBack());
                        break;
                    case "WR":
                        wrs.add(builder.createWideReceiver());
                        break;
                    case "TE":
                        tes.add(builder.createTightEnd());
                        break;
                    case "DST":
                        defs.add(builder.createDefense());
                        break;
                    case "K":
                        ks.add(builder.createKicker());
                        break;
                    default:
                        throw new RuntimeException("unrecognized position");
                }

            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not found.");
            System.exit(0);
        }
        flexes.addAll(rbs);
        flexes.addAll(wrs);
    }

    public void optimize()
    {
        optimize(qbs, 10);
        optimize(rbs, 20);
        optimize(wrs, 40);
        optimize(tes, 10);

        flexes.clear();
        flexes.addAll(rbs);
        flexes.addAll(wrs);

        optimize(defs, 10);
        optimize(ks, 10);
    }

    public List<Quarterback> getQuarterbacks()
    {
        return qbs;
    }

    public List<RunningBack> getRunningBacks()
    {
        return rbs;
    }

    public List<WideReceiver> getWideReceivers()
    {
        return wrs;
    }

    public List<TightEnd> getTightEnds()
    {
        return tes;
    }

    public List<Flex> getFlexes()
    {
        return flexes;
    }

    public List<Defense> getDefenses()
    {
        return defs;
    }

    public List<Kicker> getKickers()
    {
        return ks;
    }

    private static void optimize(List<? extends Player> players, int toKeep)
    {
        if (players.isEmpty())
        {
            return;
        }
        players.sort(new Comparator<Player>()
        {

            @Override
            public int compare(Player o1, Player o2)
            {
                Double d1 = o1.getProjectedPoints() / o1.getSalary();
                Double d2 = o2.getProjectedPoints() / o2.getSalary();
                return d1.compareTo(d2);
            }
        }.reversed());
        players.subList(toKeep, players.size()).clear();
    }
}
