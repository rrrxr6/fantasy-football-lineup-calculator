package roach.ryan.ff.model;

import java.util.Collection;
import java.util.Collections;

public interface Team
{
    int getSalary();

    double getAverageRank();

    double getProjectedPoints();

    double getActualPoints();

    double getMetric();

    double getOwnership();

    String toStringCsv();

    default Collection<Player> getPlayers() {
        return Collections.emptyList();
    }
}
