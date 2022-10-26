package roach.ryan.ff.data.scrape;

import java.util.Objects;

public class PlayerKey
{
    private final String name;
    private final String position;

    public PlayerKey(String name, String position)
    {
        this.name = name;
        this.position = position;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name, position);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        PlayerKey other = (PlayerKey) obj;
        return Objects.equals(name, other.name) && Objects.equals(position, other.position);
    }
}
