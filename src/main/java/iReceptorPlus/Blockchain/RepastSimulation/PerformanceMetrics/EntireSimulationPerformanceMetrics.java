package iReceptorPlus.Blockchain.RepastSimulation.PerformanceMetrics;

import iReceptorPlus.Blockchain.ChainDataTypes.TraceabilityData;

import java.util.concurrent.atomic.AtomicInteger;

public class EntireSimulationPerformanceMetrics
{
    static AtomicInteger numberOfValidatedEntries = new AtomicInteger();
    static AtomicInteger numberOfInvalidatedEntries = new AtomicInteger();
    static AtomicInteger incorrectlyClassifiedEntries = new AtomicInteger();

    public static void reset()
    {
        numberOfValidatedEntries.set(0);
        numberOfInvalidatedEntries.set(0);
        incorrectlyClassifiedEntries.set(0);
    }

    public static void registerValidatedEntry(TraceabilityData traceabilityData)
    {
        numberOfValidatedEntries.incrementAndGet();
        incrementIncorrectlyClassifiedEntriesIfShould(traceabilityData, true);
    }

    public static void registerInvalidatedEntry(TraceabilityData traceabilityData)
    {
        numberOfInvalidatedEntries.incrementAndGet();
        incrementIncorrectlyClassifiedEntriesIfShould(traceabilityData, false);
    }

    public static void incrementIncorrectlyClassifiedEntriesIfShould(TraceabilityData traceabilityData, boolean consideredValid)
    {
        if (traceabilityData.getValid() != consideredValid)
            incorrectlyClassifiedEntries.incrementAndGet();
    }

    public static Integer getNumberOfValidatedEntries()
    {
        return numberOfValidatedEntries.get();
    }

    public static Integer getNumberOfInvalidatedEntries()
    {
        return numberOfInvalidatedEntries.get();
    }

    public static Integer getIncorrectlyClassifiedEntries()
    {
        return incorrectlyClassifiedEntries.get();
    }
}
