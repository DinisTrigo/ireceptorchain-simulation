package iReceptorPlus.Blockchain.RepastSimulation;

import iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.APIs.BlockchainAPI;
import iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.LoggingAndSettings.Logging;
import iReceptorPlus.Blockchain.RepastSimulation.PerformanceMetrics.EntireSimulationPerformanceMetrics;

public class SimulationRunner
{
    private IReceptorChainModel model;
    private int numIterations;

    public SimulationRunner(IReceptorChainModel model, int numIterations)
    {
        this.model = model;
        this.numIterations = numIterations;
    }

    public void runSimulation()
    {
        BlockchainAPI.initBlockchainContext(model.getContext());
        for(int i = 1; i <= numIterations; i++)
        {
            Logging.LogSimulationProgress("Initiating iteration " + i);
            runModel();
            Logging.LogSimulationProgress("Finished iteration " + i);
            Logging.LogSimulationProgress("Metrics: ");
            Integer numberOfValidatedEntries = EntireSimulationPerformanceMetrics.getNumberOfValidatedEntries();
            Integer numberOfInvalidatedEntries = EntireSimulationPerformanceMetrics.getNumberOfInvalidatedEntries();
            Integer incorrectlyClassifiedEntries = EntireSimulationPerformanceMetrics.getIncorrectlyClassifiedEntries();
            Logging.LogSimulationProgress("numberOfValidatedEntries: " + numberOfValidatedEntries);
            Logging.LogSimulationProgress("numberOfInvalidatedEntries: " + numberOfInvalidatedEntries);
            Logging.LogSimulationProgress("incorrectlyClassifiedEntries: " + incorrectlyClassifiedEntries);

        }
    }

    private void runModel()
    {
        model.preStep();
        model.step();
        model.postStep();
    }
}
