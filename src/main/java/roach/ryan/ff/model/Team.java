package roach.ryan.ff.model;

public interface Team
{
    int getSalary();

    double getAverageRank();

    double getProjectedPoints();

    double getActualPoints();

    String toStringCsv();
}
