package roach.ryan.ff.model;

public enum Position
{
    QB("QB"),
    RB("RB"),
    ST("D/ST"),
    TE("TE"),
    WR("WR");

    private final String display;

    Position(String display)
    {
        this.display = display;
    }

    public String getDisplay()
    {
        return display;
    }
}
