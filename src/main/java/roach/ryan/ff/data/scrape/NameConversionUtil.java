package roach.ryan.ff.data.scrape;

import java.util.HashMap;
import java.util.Map;

public class NameConversionUtil
{
    private static Map<String, String> NAMES;
    static
    {
        NAMES = new HashMap<>();
        NAMES.put("Patrick Mahomes", "Patrick Mahomes II");
        NAMES.put("Ray-Ray McCloud III", "Ray-Ray McCloud");
        NAMES.put("KJ Hamler", "K.J. Hamler");
        NAMES.put("Brian Robinson", "Brian Robinson Jr.");
        NAMES.put("Phillip Dorsett", "Phillip Dorsett II");
        NAMES.put("Richie James", "Richie James Jr.");
        NAMES.put("D.J. Moore", "DJ Moore");
        NAMES.put("P.J. Walker", "PJ Walker");
    }

    private NameConversionUtil()
    {
    }

    public static String convert(String name)
    {
        return NAMES.getOrDefault(name, name);
    }

}
