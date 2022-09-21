package roach.ryan.ff.data;

import java.math.BigInteger;
import java.util.List;

import roach.ryan.ff.model.Defense;
import roach.ryan.ff.model.Flex;
import roach.ryan.ff.model.Quarterback;
import roach.ryan.ff.model.RunningBack;
import roach.ryan.ff.model.TightEnd;
import roach.ryan.ff.model.WideReceiver;

public class FreeAgentPool
{
    private final List<Quarterback> qbs;
    private final List<RunningBack> rbs;
    private final List<WideReceiver> wrs;
    private final List<TightEnd> tes;
    private final List<Flex> flexes;
    private final List<Defense> defs;

    public FreeAgentPool(List<Quarterback> qbs, List<RunningBack> rbs, List<WideReceiver> wrs, List<TightEnd> tes,
            List<Flex> flexes, List<Defense> defs)
    {
        this.qbs = qbs;
        this.rbs = rbs;
        this.wrs = wrs;
        this.tes = tes;
        this.flexes = flexes;
        this.defs = defs;
    }

    public List<Quarterback> getQuarterbacks()
    {
        return qbs;
    }

    public List<RunningBack> getRunningBacks()
    {
        return rbs;
    }

    public List<WideReceiver> getWideReceivers()
    {

        return wrs;
    }

    public List<TightEnd> getTightEnds()
    {
        return tes;
    }

    public List<Flex> getFlexes()
    {
        return flexes;
    }

    public List<Defense> getDefenses()
    {
        return defs;
    }

    public BigInteger size()
    {
        BigInteger qbCount = BigInteger.valueOf(qbs.size());
        BigInteger rbCount = BigInteger.valueOf(rbs.size());
        BigInteger wrCount = BigInteger.valueOf(wrs.size());
        BigInteger teCount = BigInteger.valueOf(tes.size());
        BigInteger flexCount = BigInteger.valueOf(flexes.size());
        BigInteger defCount = BigInteger.valueOf(defs.size());
        return qbCount.multiply(rbCount).multiply(rbCount).multiply(wrCount).multiply(wrCount).multiply(wrCount)
                .multiply(teCount).multiply(flexCount).multiply(defCount);
    }
}
