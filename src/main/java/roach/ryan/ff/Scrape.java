package roach.ryan.ff;

import java.io.File;
import java.util.Map;

import roach.ryan.ff.data.scrape.FanDuelActualPointsRetriever;
import roach.ryan.ff.data.scrape.FanDuelProjectionDataRetriever;
import roach.ryan.ff.data.scrape.PlayerCsvReader;
import roach.ryan.ff.data.scrape.PlayerCsvWriter;
import roach.ryan.ff.data.scrape.PlayerKey;
import roach.ryan.ff.model.PlayerBuilder;

public class Scrape
{
    public static void main(String[] args) throws Exception
    {
        if (args.length < 3)
        {
            System.out.println("Specify api key, csv file location, and week in args.");
            System.exit(0);
        }

        String apiKey = args[0];
        String fileName = args[1];
        int week = Integer.valueOf(args[2]);

        if (!new File(fileName).exists())
        {
            Map<PlayerKey, PlayerBuilder> dataFromWeb = new FanDuelProjectionDataRetriever(apiKey).retrieve(week);
            new PlayerCsvWriter().write(fileName, dataFromWeb.values());
        }

        Map<PlayerKey, PlayerBuilder> dataFromFile = new PlayerCsvReader().read(fileName);
        Map<PlayerKey, PlayerBuilder> dataWithActualPoints = new FanDuelActualPointsRetriever().retrieve(dataFromFile,
                week);

        new PlayerCsvWriter().write(fileName, dataWithActualPoints.values());
        // for (PlayerBuilder builder : dataWithActualPoints.values())
        // {
        // System.out.println(builder.createPlayer().toCsv());
        // }
    }
}
