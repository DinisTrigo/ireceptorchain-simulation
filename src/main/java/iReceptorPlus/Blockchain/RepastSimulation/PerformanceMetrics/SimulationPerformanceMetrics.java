package iReceptorPlus.Blockchain.RepastSimulation.PerformanceMetrics;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

public class SimulationPerformanceMetrics
{
    /**
     * This array list stores the variables that describe the state in each simulation.
     * Each element of the array corresponds to an iteration.
     */
    static ArrayList<IterationStateMetrics> iterationStateMetricsArrayList = new ArrayList<>();

    static ArrayBlockingQueue<IterationStateMetrics> iterationStateMetricsArrayBlockingQueue = new ArrayBlockingQueue<IterationStateMetrics>(10);

    public static void addIterationStateMetric(IterationStateMetrics iterationStateMetrics)
    {
        iterationStateMetricsArrayList.add(iterationStateMetrics);
        //iterationStateMetricsArrayBlockingQueue.add(iterationStateMetrics);
    }

    public static IterationStateMetrics getLastIterationStateMetrics() throws InterruptedException
    {
        return iterationStateMetricsArrayBlockingQueue.take();
    }
}
