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
        NAMES.put("Duke Johnson", "Duke Johnson Jr.");
        NAMES.put("Stanley Berryhill", "Stanley Berryhill III");
        NAMES.put("Mark Vital", "Mark Vital Jr.");
        NAMES.put("Demetric Felton", "Demetric Felton Jr.");
        NAMES.put("Troy Hairston", "Troy Hairston II");
        NAMES.put("Terry Godwin II", "Terry Godwin");
        NAMES.put("Chris Herndon", "Chris Herndon IV");
        NAMES.put("LuJuan Winningham", "Lujuan Winningham");
        NAMES.put("Master Teague III", "Master Teague");
        NAMES.put("Chig Okonkwo", "Chigoziem Okonkwo");
        NAMES.put("D'vonte Price", "D'Vonte Price");
        NAMES.put("Keelan Cole", "Keelan Cole Sr.");
        NAMES.put("TJ Pledger", "T.J. Pledger");
        NAMES.put("Matt Orzech", "Matthew Orzech");
        NAMES.put("Patrick Taylor", "Patrick Taylor Jr.");
    }

    private NameConversionUtil()
    {
    }

    public static String convert(String name)
    {
        return NAMES.getOrDefault(name, name);
    }

}
