package roach.ryan.ff.data.scrape;

import static roach.ryan.ff.data.scrape.NameConversionUtil.convert;

import java.io.IOException;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import roach.ryan.ff.model.PlayerBuilder;

public class FanDuelOwnershipRetriever
{
    private static final String OWNERSHIP_URL = "https://www.pff.com/dfs/ownership";

    public Map<PlayerKey, PlayerBuilder> retrieve(Map<PlayerKey, PlayerBuilder> playerData)
    {
        try
        {
            Document doc = Jsoup.connect(OWNERSHIP_URL).get();
            Elements tables = doc.select(".dfs-ownership table");
            Element fanDuelTable = tables.get(0);
            Elements rows = fanDuelTable.select("tbody tr");
            for (Element row : rows)
            {
                Elements cols = row.select("td");
                String name = cols.get(1).text();
                String position = cols.get(4).text();
                position = position.equals("D") ? "DST" : position;
                double ownership = Double.valueOf(cols.get(6).text().replace("%", ""));
                PlayerKey playerKey = new PlayerKey(convert(name), position);
                playerData.computeIfPresent(playerKey, (key, builder) -> builder.withOwnership(ownership));
            }

        }
        catch (IOException e)
        {
            System.out.println("An error occurred while retrieving ownership data.");
        }
        return playerData;
    }
}
