package roach.ryan.ff;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import java.io.File;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import roach.ryan.ff.data.DataParser;
import roach.ryan.ff.data.FreeAgentPool;
import roach.ryan.ff.data.Partitioner;
import roach.ryan.ff.fanduel.FanDuel;
import roach.ryan.ff.model.Flex;
import roach.ryan.ff.model.Player;
import roach.ryan.ff.model.Team;
import roach.ryan.ff.model.TopTeams;

public class Application
{

    public static void main(String[] args)
    {
        DataParser parser = new DataParser(new File(args[0]));
        parser.optimize();
        FreeAgentPool optimizedPool = new FreeAgentPool(parser.getQuarterbacks(), parser.getRunningBacks(),
                parser.getWideReceivers(), parser.getTightEnds(), parser.getDefenses(), parser.getFlexes());

        if (optimizedPool.size().compareTo(new BigInteger("850000000000")) > 0)
        {
            System.out.println("Player pool is too large. Lineup calculator will take over five minutes to complete.");
            System.exit(0);
        }

        Collection<List<Flex>> flexPartitions = new Partitioner(4).getPartitions(optimizedPool.getFlexes());
        List<CompletableFuture<List<Team>>> futures = flexPartitions.stream()
                .map(flexes -> CompletableFuture.supplyAsync(() -> FanDuel.getTopTeams(optimizedPool, flexes)))
                .map(future -> future.thenApply(TopTeams::getTeams)).collect(toList());
        CompletableFuture<List<List<Team>>> combinedFutures = CompletableFuture
                .allOf(futures.toArray(new CompletableFuture[futures.size()]))
                .thenApply(v -> futures.stream().map(CompletableFuture::join).collect(toList()));
        List<Team> teams = combinedFutures.join().stream().flatMap(List::stream)
                .sorted(comparing(Team::getProjectedPoints)).collect(toList());

        if (teams.isEmpty())
        {
            System.out.println("No valid teams can be made from the pool.");
        }

        int i = teams.size();
        Iterator<Team> it = teams.iterator();
        while (it.hasNext())
        {
            System.out.println("#" + i--);
            System.out.println(it.next());
        }
    }

    private static void printTopTenPointsPerSalaryPerPosition(FreeAgentPool pool)
    {
        printTopTen(pool.getQuarterbacks());
        printTopTen(pool.getRunningBacks());
        printTopTen(pool.getWideReceivers());
        printTopTen(pool.getTightEnds());
        printTopTen(pool.getDefenses());
    }

    private static void printTopTen(List<? extends Player> players)
    {
        Collections.sort(players, new Comparator<Player>()
        {

            @Override
            public int compare(Player p1, Player p2)
            {
                double comparison = p1.getProjectedPoints() / p1.getSalary() - p2.getProjectedPoints() / p2.getSalary();
                if (comparison > 0)
                {
                    return -1;
                }
                else if (comparison < 0)
                {
                    return 1;
                }
                else
                {
                    return 0;
                }
            }
        });
        System.out.println(players.get(0).getClass().getSimpleName());
        for (int i = 0; i < 10; i++)
        {
            System.out.println(i + 1 + ") " + players.get(i).getName());
        }
        System.out.println();
    }
}
