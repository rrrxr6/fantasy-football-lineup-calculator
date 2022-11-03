package roach.ryan.ff.data.scrape;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import roach.ryan.ff.model.PlayerBuilder;

public class FanDuelProjectionDataRetriever
{
    private static final String PROJECTED_POINTS_URL = "https://api.fantasypros.com/v2/json/nfl/2022/consensus-rankings?type=weekly&scoring=HALF&position=%s&week=%d&experts=available";
    private final String apiKey;

    public FanDuelProjectionDataRetriever(String apiKey)
    {
        this.apiKey = apiKey;
    }

    public Map<PlayerKey, PlayerBuilder> retrieve(int week)
    {
        Map<PlayerKey, PlayerBuilder> playerData = new HashMap<>();
        try
        {

            Document doc = Jsoup.connect("https://www.fantasypros.com/daily-fantasy/nfl/fanduel-salary-changes.php")
                    .get();

            Elements rows = doc.select("table tr");
            for (Element row : rows)
            {
                Elements cols = row.select("td");
                if (cols.isEmpty())
                {
                    continue;
                }
                String namePlusPositionRaw = cols.get(1).text();
                String name = namePlusPositionRaw.substring(0, namePlusPositionRaw.indexOf('(') - 1);
                String position = namePlusPositionRaw
                        .substring(namePlusPositionRaw.length() - 4, namePlusPositionRaw.length() - 1).strip();

                String gameTime = cols.get(2).text();
                int salary = Integer.valueOf(cols.get(4).text().replaceAll("[^0-9]+", ""));
                playerData.put(new PlayerKey(name, position), new PlayerBuilder(name).withPositionDisplay(position)
                        .withGameTime(gameTime).withSalary(salary));
            }

            populateData(playerData, week, "QB");
            populateData(playerData, week, "RB");
            populateData(playerData, week, "WR");
            populateData(playerData, week, "TE");
            populateData(playerData, week, "DST");
            populateData(playerData, week, "K");
            return playerData;
        }
        catch (IOException e)
        {
            System.out.println("Error encountered while retrieving salary.");
            e.printStackTrace();
            System.exit(0);
        }
        return playerData;
    }

    public Map<PlayerKey, PlayerBuilder> retrieve(Map<PlayerKey, PlayerBuilder> playerData, int week)
    {
        populateData(playerData, week, "QB");
        populateData(playerData, week, "RB");
        populateData(playerData, week, "WR");
        populateData(playerData, week, "TE");
        populateData(playerData, week, "DST");
        populateData(playerData, week, "K");
        return playerData;
    }

    private void populateData(Map<PlayerKey, PlayerBuilder> playerData, int week, String position)
    {
        try
        {
            String responseBody = HttpClient.newBuilder().build()
                    .send(getRequest(PROJECTED_POINTS_URL, position, week), BodyHandlers.ofString()).body();
            JSONObject json = new JSONObject(responseBody);
            JSONArray array = json.getJSONArray("players");
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject player = array.getJSONObject(i);
                String name = player.getString("player_name");
                int rank = player.getInt("rank_ecr");
                double projectedPoints = player.optDouble("r2p_pts", 0);
                int rankMin = player.getInt("rank_min");
                int rankMax = player.getInt("rank_max");
                String team = player.getString("player_team_id");
                String rawOpponent = player.getString("player_opponent");
                String opponent = rawOpponent.isBlank() ? null : rawOpponent;
                playerData.computeIfPresent(new PlayerKey(name, position),
                        (key, builder) -> builder.withRank(rank).withProjectPoints(projectedPoints).withRankMin(rankMin)
                                .withRankMax(rankMax).withTeam(team).withOpponent(opponent));
            }
        }
        catch (IOException | InterruptedException | URISyntaxException e)
        {
            System.out.println(String.format(
                    "Error encountered while retrieving projected points for week [%d] and position [%s].", week,
                    position));
            e.printStackTrace();
            System.exit(0);
        }
    }

    private HttpRequest getRequest(String baseUrl, String position, int week) throws URISyntaxException
    {
        return HttpRequest.newBuilder().uri(new URI(String.format(baseUrl, position, week)))
                .header("accept", "application/json")
                .header("user-agent",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36")
                .header("x-api-key", apiKey).GET().build();
    }
}
