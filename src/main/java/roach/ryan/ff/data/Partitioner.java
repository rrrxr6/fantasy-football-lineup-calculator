package roach.ryan.ff.data;

import static java.util.stream.Collectors.groupingBy;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Partitioner
{
    private final int numberOfPartitions;

    public Partitioner(int numberOfPartitions)
    {
        this.numberOfPartitions = numberOfPartitions;
    }

    public <T> Collection<List<T>> getPartitions(List<T> list)
    {
        AtomicInteger counter = new AtomicInteger(0);
        return list.stream().collect(groupingBy(foo -> counter.getAndIncrement() / (list.size() / numberOfPartitions)))
                .values();
    }
}
