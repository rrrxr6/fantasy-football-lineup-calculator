package roach.ryan.ff.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;

import roach.ryan.ff.model.Player;
import roach.ryan.ff.model.Position;

public class DataParser
{
    public static Collection<Player> read(File file) {
        Collection<Player> players = new HashSet<>();
        try (Scanner scanner = new Scanner(file))
        {
            while (scanner.hasNext())
            {
                String[] parts = scanner.nextLine().split(",");
                Position position = Position.valueOf(parts[0].replaceAll("[^A-Za-z]+", ""));
                int salary = Integer.valueOf(parts[1]);
                String name = parts[2];
                double points = Double.valueOf(parts[3]);
                players.add(new Player(name, position, salary, points));
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not found.");
            System.exit(0);
        }
        return players;
    }
}
