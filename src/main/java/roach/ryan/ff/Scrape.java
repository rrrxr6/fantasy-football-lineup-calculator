package roach.ryan.ff;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scrape
{
    private static Map<Pair, String> PLAYER_DATA = new HashMap<>();
    private static String API_KEY;

    public static void main(String[] args) throws Exception
    {
        Document doc = Jsoup.connect("https://www.fantasypros.com/daily-fantasy/nfl/fanduel-salary-changes.php").get();
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
            String salary = cols.get(4).text().replaceAll("[^0-9]+", "");
            PLAYER_DATA.put(new Pair(name, position), String.join(",", gameTime, salary));
            // System.out.println(String.format("%s,%s,%s,%s", name, gameTime, salary, position));
        }

        API_KEY = args[0];
        String week = "7";
        populateData(week, "QB");
        populateData(week, "RB");
        populateData(week, "WR");
        populateData(week, "TE");
        populateData(week, "DST");

        Iterator<Entry<Pair, String>> it = PLAYER_DATA.entrySet().iterator();
        while (it.hasNext())
        {
            Entry<Pair, String> entry = it.next();
            if (entry.getValue().split(",").length == 2)
            {
                it.remove();
            }
        }

        for (Entry<Pair, String> entry : PLAYER_DATA.entrySet())
        {
            System.out.println(String.join(",", entry.getKey().name, entry.getKey().position, entry.getValue()));
        }
    }

    private static void populateData(String week, String position) throws Exception
    {
        String projectedPointBaseURL = "https://api.fantasypros.com/v2/json/nfl/2022/consensus-rankings?type=weekly&scoring=HALF&position=%s&week=%s&experts=available";
        JSONObject json = new JSONObject(getResponse(getRequest(projectedPointBaseURL, position, week)));
        JSONArray array = json.getJSONArray("players");
        for (int i = 0; i < array.length(); i++)
        {
            JSONObject player = array.getJSONObject(i);
            String name = player.getString("player_name");
            String rank = String.valueOf(player.getInt("rank_ecr"));
            String projectedPoints = String.valueOf(player.optDouble("r2p_pts", 0));
            PLAYER_DATA.computeIfPresent(new Pair(name, position),
                    (k, v) -> String.join(",", v, rank, projectedPoints));
            // System.out.println(String.format("%s,%s,%s", name, rank, projectedPoints));
        }
    }

    private static HttpRequest getRequest(String baseUrl, String position, String week) throws URISyntaxException
    {
        return HttpRequest.newBuilder().uri(new URI(String.format(baseUrl, position, week)))
                .header("accept", "application/json")
                .header("user-agent",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36")
                .header("x-api-key", API_KEY).GET().build();
    }

    private static String getResponse(HttpRequest request) throws IOException, InterruptedException
    {
        return HttpClient.newBuilder().build().send(request, BodyHandlers.ofString()).body();
    }

    private static class Pair
    {
        private final String name;
        private final String position;

        public Pair(String name, String position)
        {
            this.name = name;
            this.position = position;
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(name, position);
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
            {
                return true;
            }
            if (obj == null)
            {
                return false;
            }
            if (getClass() != obj.getClass())
            {
                return false;
            }
            Pair other = (Pair) obj;
            return Objects.equals(name, other.name) && Objects.equals(position, other.position);
        }
    }
}
