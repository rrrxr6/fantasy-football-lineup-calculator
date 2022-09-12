package roach.ryan.ff.data;

import static java.util.Comparator.comparing;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import roach.ryan.ff.model.Defense;
import roach.ryan.ff.model.Player;
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
    private final List<Defense> dsts = new ArrayList<>();

    public DataParser(File file)
    {
        try (Scanner scanner = new Scanner(file))
        {
            while (scanner.hasNext())
            {
                //line format should be "QB,8700,Patrick Mahomes II,25.2"
                String[] parts = scanner.nextLine().split(",");
                String position = parts[0].replaceAll("[^A-Za-z]+", "");
                int salary = Integer.valueOf(parts[1]);
                String name = parts[2];
                double points = Double.valueOf(parts[3]);
                switch (position)
                {
                    case "QB":
                        qbs.add(new Quarterback(name, salary, points));
                        break;
                    case "RB":
                        rbs.add(new RunningBack(name, salary, points));
                        break;
                    case "WR":
                        wrs.add(new WideReceiver(name, salary, points));
                        break;
                    case "TE":
                        tes.add(new TightEnd(name, salary, points));
                        break;
                    case "ST":
                        dsts.add(new Defense(name, salary, points));
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
        optimize(qbs);
        optimize(rbs);
        optimize(wrs);
        optimize(tes);
        optimize(dsts);
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

    public List<Defense> getDefenses()
    {
        return dsts;
    }

    private void optimize(List<? extends Player> players)
    {
        players.sort(comparing(Player::getPoints).reversed());
        Iterator<? extends Player> iterator = players.iterator();
        Player current = iterator.next();
        while (iterator.hasNext())
        {
            Player last = current;
            current = iterator.next();
            if (current.getSalary() > last.getSalary())
            {
                iterator.remove();
            }
        }
    }
}
