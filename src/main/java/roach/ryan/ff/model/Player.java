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

    int getRankMin();

    int getRankMax();

    String getTeam();

    String getOpponent();

    String toCsv();
}