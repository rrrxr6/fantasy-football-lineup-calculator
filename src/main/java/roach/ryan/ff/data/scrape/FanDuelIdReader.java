package roach.ryan.ff.data.scrape;

import static roach.ryan.ff.data.scrape.NameConversionUtil.convert;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

import roach.ryan.ff.model.PlayerBuilder;

public class FanDuelIdReader
{

    public FanDuelIdReader()
    {
    }

    public Map<PlayerKey, PlayerBuilder> read(Map<PlayerKey, PlayerBuilder> playerData, String fileName)
    {
        try
        {
            Scanner scanner = new Scanner(new File(fileName));
            while (scanner.hasNext())
            {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                String id = parts[0].replaceAll("[^0-9A-Za-z- :.']+", "");
                String name = parts[1];
                String position = parts[2];
                PlayerKey playerKey = new PlayerKey(convert(name), position);
                playerData.computeIfPresent(playerKey, (key, builder) -> builder.withId(id));
                if (!playerData.containsKey(playerKey))
                {
                    System.out.println(name + ":" + position);
                }
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Error encountered while reading the file.");
            e.printStackTrace();
        }
        return playerData;
    }
}
