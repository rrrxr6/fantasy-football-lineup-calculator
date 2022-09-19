package roach.ryan.ff.model;

import static java.util.Collections.binarySearch;
import static java.util.Comparator.comparing;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TopTeams
{
    private final static Comparator<Team> COMPARATOR = comparing(Team::getProjectedPoints);
    private final int capacity;
    private final List<Team> teams;

    public TopTeams(int capacity)
    {
        this.capacity = capacity;
        this.teams = new ArrayList<>(capacity);
    }

    public void add(Team team)
    {
        if (notFull())
        {
            addInOrder(team);
        }
        else if (isTopTeam(team) && !teams.contains(team))
        {
            teams.remove(0);
            addInOrder(team);
        }
    }

    public List<Team> getTeams()
    {
        return teams;
    }

    private void addInOrder(Team team)
    {
        int index = binarySearch(teams, team, COMPARATOR);
        index = index >= 0 ? index : -1 * index - 1;
        teams.add(index, team);
    }

    private boolean isTopTeam(Team team)
    {
        return team.getProjectedPoints() > teams.get(0).getProjectedPoints();
    }

    private boolean notFull()
    {
        return teams.size() < capacity;
    }
}
