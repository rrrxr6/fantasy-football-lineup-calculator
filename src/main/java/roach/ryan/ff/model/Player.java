package roach.ryan.ff.model;

public interface Player
{
    String getName();

    String getPositionDisplay();

    int getSalary();

    int getRank();

    double getProjectedPoints();

    double getActualPoints();

    double getOwnership();

    String getGameTime();

    String getId();

    String toCsv();
}