package roach.ryan.ff;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import java.io.File;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import roach.ryan.ff.data.DataParser;
import roach.ryan.ff.data.FreeAgentPool;
import roach.ryan.ff.data.Partitioner;
import roach.ryan.ff.fanduel.FanDuelFullRosterOptimizer;
import roach.ryan.ff.model.Flex;
import roach.ryan.ff.model.Team;
import roach.ryan.ff.model.TopTeams;

public class CsvResults
{
    public static void main(String[] args)
    {
        DataParser parser = new DataParser(new File(args[0]));
        parser.optimize();
        FreeAgentPool optimizedPool = new FreeAgentPool(parser.getQuarterbacks(), parser.getRunningBacks(),
                parser.getWideReceivers(), parser.getTightEnds(), parser.getFlexes(), parser.getDefenses());

        Comparator<Team> comparator = comparing(Team::getMetric);
        FanDuelFullRosterOptimizer fanDuel = new FanDuelFullRosterOptimizer.Builder(optimizedPool).build();
        Collection<List<Flex>> flexPartitions = new Partitioner(4).getPartitions(optimizedPool.getFlexes());
        List<CompletableFuture<List<Team>>> futures = flexPartitions.stream()
                .map(flexes -> CompletableFuture.supplyAsync(() -> fanDuel.getTopTeams(flexes, comparator)))
                .map(future -> future.thenApply(TopTeams::getTeams)).collect(toList());
        CompletableFuture<List<List<Team>>> combinedFutures = CompletableFuture
                .allOf(futures.toArray(new CompletableFuture[futures.size()]))
                .thenApply(v -> futures.stream().map(CompletableFuture::join).collect(toList()));
        List<Team> teams = combinedFutures.join().stream().flatMap(List::stream).distinct().sorted(comparator)
                .collect(toList());

        if (teams.isEmpty())
        {
            System.out.println("No valid teams can be made from the pool.");
        }

        printTeams(teams);
        // printAverageOfTopTen(teams);
    }

    private static void printAverageOfTopTen(List<Team> teams)
    {
        teams.subList(0, teams.size() - 10).clear();
        System.out.println(teams.stream().mapToDouble(Team::getActualPoints).average().getAsDouble());
    }

    private static void printTeams(List<Team> teams)
    {
        Iterator<Team> it = teams.iterator();
        while (it.hasNext())
        {
            System.out.println(it.next().toStringCsv());
        }
    }
}
