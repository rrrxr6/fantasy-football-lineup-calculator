package roach.ryan.ff.data;

import static java.util.Comparator.comparing;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
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
                switch (parts.length)
                {
                    case 9:
                        builder.withId(parts[8]);
                    case 8:
                        builder.withGameTime(parts[7]);
                    case 7:
                        builder.withOwnership(Double.valueOf(parts[6]));
                        break;
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
        optimize(qbs);
        optimizeSkill(rbs);
        optimizeSkill(wrs);
        optimize(tes);
        optimizeSkill(flexes);
        optimize(defs);
        optimize(ks);
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

    private static void optimize(List<? extends Player> players)
    {
        if (players.isEmpty())
        {
            return;
        }
        players.sort(comparing(Player::getProjectedPoints).reversed());
        double avgPoints = players.stream().mapToDouble(Player::getProjectedPoints).average().getAsDouble();
        Iterator<? extends Player> iterator = players.iterator();
        Player current = iterator.next();
        while (iterator.hasNext())
        {
            Player last = current;
            current = iterator.next();
            if (current.getSalary() >= last.getSalary() || current.getProjectedPoints() < avgPoints)
            {
                iterator.remove();
                current = last;
            }
        }
    }

    private static void optimizeSkill(List<? extends Player> players)
    {
        players.removeIf(p -> p.getRank() > 30);
    }
}
