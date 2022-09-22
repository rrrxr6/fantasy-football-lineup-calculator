package roach.ryan.ff;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import java.io.File;
import java.math.BigInteger;
import java.util.Collection;
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
    private static final boolean PRINT_PLAYER_INDEXES = false;

    public static void main(String[] args)
    {
        DataParser parser = new DataParser(new File(args[0]));
        parser.optimize();
        FreeAgentPool optimizedPool = new FreeAgentPool(parser.getQuarterbacks(), parser.getRunningBacks(),
                parser.getWideReceivers(), parser.getTightEnds(), parser.getFlexes(), parser.getDefenses());

        if (optimizedPool.size().compareTo(new BigInteger("850000000000")) > 0)
        {
            System.out.println("Player pool is too large. Lineup calculator will take over five minutes to complete.");
            System.exit(0);
        }

        if (PRINT_PLAYER_INDEXES)
        {
            printPlayers(optimizedPool);
            System.exit(0);
        }

        FanDuel fanDuel = new FanDuel.Builder(optimizedPool).build();
        Collection<List<Flex>> flexPartitions = new Partitioner(4).getPartitions(optimizedPool.getFlexes());
        List<CompletableFuture<List<Team>>> futures = flexPartitions.stream()
                .map(flexes -> CompletableFuture.supplyAsync(() -> fanDuel.getTopTeams(flexes)))
                .map(future -> future.thenApply(TopTeams::getTeams)).collect(toList());
        CompletableFuture<List<List<Team>>> combinedFutures = CompletableFuture
                .allOf(futures.toArray(new CompletableFuture[futures.size()]))
                .thenApply(v -> futures.stream().map(CompletableFuture::join).collect(toList()));
        List<Team> teams = combinedFutures.join().stream().flatMap(List::stream)
                .sorted(comparing(Team::getAverageRank).reversed()).collect(toList());

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

    private static void printPlayers(FreeAgentPool pool)
    {
        printPosition(pool.getQuarterbacks());
        printPosition(pool.getRunningBacks());
        printPosition(pool.getWideReceivers());
        printPosition(pool.getTightEnds());
        printPosition(pool.getDefenses());
    }

    private static void printPosition(List<? extends Player> players)
    {
        System.out.println(players.get(0).getClass().getSimpleName());
        for (int i = 0; i < players.size(); i++)
        {
            System.out.println("[" + i + "] " + players.get(i).getName());
        }
        System.out.println();
    }
}
