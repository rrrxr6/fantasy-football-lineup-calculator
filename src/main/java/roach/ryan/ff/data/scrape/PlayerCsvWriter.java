package roach.ryan.ff.data.scrape;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collection;

import roach.ryan.ff.model.Player;
import roach.ryan.ff.model.PlayerBuilder;

public class PlayerCsvWriter
{
    public void write(String fileName, Collection<PlayerBuilder> builders)
    {
        try (PrintWriter pw = new PrintWriter(fileName))
        {
            builders.stream().map(PlayerBuilder::createPlayer).map(Player::toCsv).forEach(pw::println);
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Error encountered while creating CSV file.");
            e.printStackTrace();
        }
    }
}
