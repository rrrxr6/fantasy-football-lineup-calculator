package roach.ryan.ff.data.scrape;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import roach.ryan.ff.model.PlayerBuilder;

public class PlayerCsvReader
{

    public Map<PlayerKey, PlayerBuilder> read(String fileName)
    {
        Map<PlayerKey, PlayerBuilder> playerData = new HashMap<>();
        try
        {
            Scanner scanner = new Scanner(new File(fileName));
            while (scanner.hasNext())
            {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                int rank = Integer.valueOf(parts[0].replaceAll("[^0-9]+", ""));
                String position = parts[1];
                int salary = Integer.valueOf(parts[2]);
                String name = parts[3];
                double projectedPoints = Double.valueOf(parts[4]);
                double actualPoints = Double.valueOf(parts[5]);
                double ownership = Double.valueOf(parts[6]);
                String gameTime = parts[7];
//                int rankMin = Integer.valueOf(parts[8]);
//                int rankMax = Integer.valueOf(parts[9]);
//                String team = parts[10];
//                String opponent = parts[11];
                playerData.put(new PlayerKey(name, position),
                        new PlayerBuilder(name).withRank(rank).withPositionDisplay(position).withSalary(salary)
                                .withProjectPoints(projectedPoints).withActualPoints(actualPoints)
                                .withOwnership(ownership).withGameTime(gameTime)/*.withRankMin(rankMin).withRankMax(rankMax).withTeam(team).withOpponent(opponent)*/);
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
