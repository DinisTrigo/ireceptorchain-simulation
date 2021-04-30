package iReceptorPlus.Blockchain.RepastSimulation;

import iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.Agents.Agent;
import iReceptorPlus.Blockchain.ChainDataTypes.TraceabilityDataValidated;
import iReceptorPlus.Blockchain.RepastSimulation.MockBlockchainClasses.Database.BlockchainContext;
import iReceptorPlus.Blockchain.RepastSimulation.PerformanceMetrics.IterationStateMetrics;
import iReceptorPlus.Blockchain.RepastSimulation.PerformanceMetrics.SimulationPerformanceMetrics;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPIs.TraceabilityDataAwaitingValidationRepositoryAPI;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPIs.TraceabilityDataValidatedRepositoryAPI;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.iReceptorChainDataTypeInfo;

import java.util.ArrayList;

public class IReceptorChainModel
{
    private ArrayList<Agent> agents;
    private BlockchainContext ctx;

    public IReceptorChainModel(ArrayList<Agent> agents, BlockchainContext ctx)
    {
        this.agents = agents;
        this.ctx = ctx;
    }

    public BlockchainContext getContext()
    {
        return ctx;
    }

    public void preStep()
    {

    } // optional

    public void step()
    {
        for (Agent agent : agents)
        {
            agent.performBehavior();
        }
// action to execute at each tick, e.g.:
// - iterate through all agents and have them
// execute behavior
// - record data
// - update displays
    }

    public void postStep()
    {
        TraceabilityDataAwaitingValidationRepositoryAPI awaitingValidationAPI = new TraceabilityDataAwaitingValidationRepositoryAPI(ctx);

        ArrayList<iReceptorChainDataTypeInfo> awaitingValidationEntries = awaitingValidationAPI.getAllEntries();
        int numberOfAwaitingValidationEntries = awaitingValidationEntries.size();

        TraceabilityDataValidatedRepositoryAPI validatedAPI = new TraceabilityDataValidatedRepositoryAPI(ctx);
        ArrayList<iReceptorChainDataTypeInfo> validatedEntries = validatedAPI.getAllEntries();
        int numberOfValidatedEntries = validatedEntries.size();

        int numberOfIncorrectlyClassifiedEntries = 0;
        for (iReceptorChainDataTypeInfo iReceptorChainDataTypeInfo : validatedEntries)
        {
            TraceabilityDataValidated entry = (TraceabilityDataValidated) iReceptorChainDataTypeInfo.getData();
            if (entry.getValid() == false)
                numberOfIncorrectlyClassifiedEntries++;
        }

        IterationStateMetrics iterationStateMetrics =
                new IterationStateMetrics(numberOfAwaitingValidationEntries,
                        numberOfValidatedEntries, numberOfIncorrectlyClassifiedEntries);

        SimulationPerformanceMetrics.addIterationStateMetric(iterationStateMetrics);

        //Logging.LogSuccess(iterationStateMetrics.toString());

    } // optional
}
