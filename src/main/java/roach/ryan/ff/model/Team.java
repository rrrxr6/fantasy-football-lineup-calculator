package roach.ryan.ff.model;

import java.util.Collection;

public interface Team
{
    int getSalary();

    double getAverageRank();

    double getProjectedPoints();

    double getActualPoints();

    double getMetric();

    double getOwnership();

    String toStringCsv();

    String toLineupIdsCsv();

    Collection<Player> getPlayers();
}
