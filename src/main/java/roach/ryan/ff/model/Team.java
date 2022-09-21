package roach.ryan.ff.model;

public interface Team
{
    double getProjectedPoints();

    double getActualPoints();

    int getSalary();

    double getAverageRank();

    String toStringCsv();
}
