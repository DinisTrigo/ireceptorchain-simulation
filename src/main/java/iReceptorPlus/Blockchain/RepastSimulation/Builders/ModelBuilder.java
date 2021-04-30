package iReceptorPlus.Blockchain.RepastSimulation.Builders;

import iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.Agents.Agent;
import iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.LoggingAndSettings.Logging;
import iReceptorPlus.Blockchain.ChainDataTypes.EntityData;
import iReceptorPlus.Blockchain.RepastSimulation.IReceptorChainModel;
import iReceptorPlus.Blockchain.RepastSimulation.MockBlockchainClasses.Database.BlockchainContext;
import iReceptorPlus.Blockchain.RepastSimulation.MockBlockchainClasses.Database.ChaincodeStub;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPIs.EntityDataRepositoryAPI;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPIs.Exceptions.GivenIdIsAlreadyAssignedToAnotherObject;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.EntityDataInfo;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class ModelBuilder
{
    AgentsDescription agentsDescription;
    ArrayList<Agent> agents;

    public ModelBuilder(AgentsDescription agentsDescription)
    {
        this.agentsDescription = agentsDescription;
    }

    public IReceptorChainModel build()
    {
        agents = new ArrayList<>();
        Double totalAgentsEthicness = agentsDescription.getIncidenceOfEthicalAgents() + agentsDescription.getIncidenceOfUnEthicalAgents();
        BlockchainContext ctx = new BlockchainContext(new ChaincodeStub());
        EntityDataRepositoryAPI entityDataRepositoryAPI = new EntityDataRepositoryAPI(ctx);

        NormalDistributionSamplingDescription workRateDistribution = agentsDescription.getWorkRateCapacityDistribution();
        NormalDistributionSamplingDescription riskDistribution = agentsDescription.getRiskDistribution();
        PRNGNormalDistribution workRatePRNG = new PRNGNormalDistribution(workRateDistribution.getMean(), workRateDistribution.getStandardDeviation());
        PRNGNormalDistribution riskDistributionPRNG = new PRNGNormalDistribution(riskDistribution.getMean(), riskDistribution.getStandardDeviation());

        for (Integer i = 0; i < agentsDescription.getNumberOfAgents(); i++)
        {
            try
            {
                entityDataRepositoryAPI.create(new EntityDataInfo(i.toString(),
                        new EntityData(i.toString(), agentsDescription.getInitialReputation(), 0.0)));
            } catch (GivenIdIsAlreadyAssignedToAnotherObject givenIdIsAlreadyAssignedToAnotherObject)
            {
                Logging.FatalErrorLog("Got exception from DB when creating entities on ModelBuilder");
                return null;
            }

            double workRateCapacity = workRatePRNG.getNextPositiveDouble();
            double risk = riskDistributionPRNG.getNextPositiveDouble();

            Double rand = ThreadLocalRandom.current().nextDouble() * totalAgentsEthicness;

            if (rand < agentsDescription.getIncidenceOfEthicalAgents())
            {
                agents.add(new Agent(i.toString(), workRateCapacity, risk, agentsDescription.getBehaviorIncidenceOfEthicalAgents()));
            } else
            {
                agents.add(new Agent(i.toString(), workRateCapacity, risk, agentsDescription.getBehaviorIncidenceOfUnEthicalAgents()));
            }

        }

        IReceptorChainModel model = new IReceptorChainModel(agents, ctx);
        return model;
    }
}
