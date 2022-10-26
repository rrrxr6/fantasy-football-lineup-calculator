package roach.ryan.ff.data.scrape;

import static roach.ryan.ff.data.scrape.NameConversionUtil.convert;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import roach.ryan.ff.model.PlayerBuilder;

public class FanDuelActualPointsRetriever
{
    private static final String ACTUAL_POINTS_URL = "https://fantasydata.com/NFL_FantasyStats/FantasyStats_Read";
    private static final Set<String> VALID_POSITIONS = Set.of("QB", "RB", "WR", "TE", "DST", "K");

    public Map<PlayerKey, PlayerBuilder> retrieve(Map<PlayerKey, PlayerBuilder> playerData, int week)
    {
        retrieve(playerData, week, 1);// offense
        retrieve(playerData, week, 6);// kicker
        retrieve(playerData, week, 7);// dst
        return playerData;
    }

    private void retrieve(Map<PlayerKey, PlayerBuilder> playerData, int week, int positionFilter)
    {
        try
        {
            HttpRequest request = HttpRequest.newBuilder().uri(new URI(ACTUAL_POINTS_URL))
                    .header("accept", "application/json, text/javascript, */*; q=0.01")
                    .header("x-request-with", "XMLHttpRequest")
                    .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                    .header("user-agent",
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36")
                    .POST(HttpRequest.BodyPublishers.ofString(getPostBody(week, positionFilter))).build();
            String responseBody = HttpClient.newBuilder().build().send(request, BodyHandlers.ofString()).body();
            JSONObject json = new JSONObject(responseBody);
            JSONArray array = json.getJSONArray("Data");
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject player = array.getJSONObject(i);
                String name = player.getString("Name");
                String position = player.getString("Position");
                if (!VALID_POSITIONS.contains(position))
                {
                    continue;
                }
                double actualPoints = player.getDouble("FantasyPointsFanDuel");
                PlayerKey playerKey = new PlayerKey(convert(name), position);
                if (playerData.containsKey(playerKey))
                {
                    playerData.compute(playerKey, (key, builder) -> builder.withActualPoints(actualPoints));
                }
                else
                {
                    // System.out.println(
                    // name + "(" + position + ") not found in map. Should this be added to conversion util?");
                }

            }
        }
        catch (IOException | InterruptedException | URISyntaxException e)
        {
            System.out.println("An error occurred while retrieving actual points scored data.");
            e.printStackTrace();
        }
    }

    private String getPostBody(int week, int positionFilter)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("sort=FantasyPointsFanDuel-desc");
        sb.append("&pageSize=300");
        sb.append("&group=");
        sb.append("&filter=");
        sb.append("&filters.position=");
        sb.append(positionFilter);
        sb.append("&filters.team=");
        sb.append("&filters.teamkey=");
        sb.append("&filters.season=2022");
        sb.append("&filters.seasontype=1");
        sb.append("&filters.cheatsheettype=");
        sb.append("&filters.scope=2");
        sb.append("&filters.subscope=1");
        sb.append("&filters.redzonescope=");
        sb.append("&filters.scoringsystem=3");
        sb.append("&filters.leaguetype=");
        sb.append("&filters.searchtext=");
        sb.append("&filters.week=");
        sb.append("&filters.startweek=");
        sb.append(week);
        sb.append("&filters.endweek=");
        sb.append(week);
        sb.append("&filters.minimumsnaps=");
        sb.append("&filters.teamaspect=");
        sb.append("&filters.stattype=");
        sb.append("&filters.exportType=");
        sb.append("&filters.desktop=");
        sb.append("&filters.dfsoperator=");
        sb.append("&filters.dfsslateid=");
        sb.append("&filters.dfsslategameid=");
        sb.append("&filters.dfsrosterslot=");
        sb.append("&filters.page=");
        sb.append("&filters.showfavs=");
        sb.append("&filters.posgroup=");
        sb.append("&filters.oddsstate=");
        sb.append("&filters.showall=");
        sb.append("&filters.aggregatescope=1");
        sb.append("&filters.rangescope=");
        sb.append("&filters.range=1");
        sb.append("&filters.type=");
        sb.append("&filters.letter=");
        sb.append("&filters.league=");
        return sb.toString();
    }
}
