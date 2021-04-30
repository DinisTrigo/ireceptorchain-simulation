package iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.Agents.CreatingBehavior;

import iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.APIs.BlockchainAPI;
import iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.Agents.Agent;
import iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.Agents.Behavior;
import iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.Agents.VotingBehaviors.Ethical;
import iReceptorPlus.Blockchain.ChainDataTypes.EntityID;
import iReceptorPlus.Blockchain.ChainDataTypes.ProcessingDetails;
import iReceptorPlus.Blockchain.ChainDataTypes.TraceabilityDataAwaitingValidation;
import iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.LoggingAndSettings.Logging;
import iReceptorPlus.Blockchain.RepastSimulation.Builders.NormalDistributionSamplingDescription;
import iReceptorPlus.Blockchain.RepastSimulation.Builders.PRNGNormalDistribution;
import iReceptorPlus.Blockchain.RepastSimulation.MockBlockchainClasses.Exceptions.ChaincodeException;
import iReceptorPlus.Blockchain.iReceptorChain.ChaincodeConfigs;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPIs.Exceptions.ObjectWithGivenKeyNotFoundOnBlockchainDB;

import java.util.concurrent.ThreadLocalRandom;

public abstract class CreatingBehavior extends Behavior
{
    public CreatingBehavior(Agent agent)
    {
        super(agent);
    }

    protected void createEntry(boolean validity)
    {
        Double baseValueOfTraceabilityData = BlockchainAPI.getBaseValueOfTraceabilityData();
        Double leastAmountOfReputationNecessary = BlockchainAPI.ReputationChangeCalculator.calculateStakeRatioForCreatingTraceabilityData(baseValueOfTraceabilityData);
        Double highestPossibleLooseWithLeastAmount = BlockchainAPI.ReputationChangeCalculator.calculatePenaltyRatioForCreatingIncorrectTraceabilityData(baseValueOfTraceabilityData);
        Double myReputation;
        try
        {
            myReputation = BlockchainAPI.getEntityReputation(agent.getEntityId());
        } catch (ObjectWithGivenKeyNotFoundOnBlockchainDB objectWithGivenKeyNotFoundOnBlockchainDB)
        {
            logErrorWithMessage("got ObjectWithGivenKeyNotFoundOnBlockchainDB exception when getting the list of traceability data to be validated");
            return;
        }
        Double amountIWantToRiskLoosing = myReputation * agent.getRisk();
        Double ratio = BlockchainAPI.ReputationChangeCalculator.calculatePenaltyRatioForCreatingIncorrectTraceabilityData(1.0);
        Double maxPossibleAdditionalValue = 0.0;
        if (highestPossibleLooseWithLeastAmount < amountIWantToRiskLoosing)
        {
            maxPossibleAdditionalValue = (amountIWantToRiskLoosing - highestPossibleLooseWithLeastAmount) / ratio;
        }
        else
        {
            Logging.Log("Agent " + agent.getEntityId() + " has stalled. Not enough reputation to keep creating traceability data. Reputation available is " + myReputation + ", amount willing to risk is " + amountIWantToRiskLoosing + " and maximum possible loose is " + highestPossibleLooseWithLeastAmount);
            return;
        }

        if (myReputation < leastAmountOfReputationNecessary)
        {
            Logging.Log("Agent " + agent.getEntityId() + " has stalled. Not enough reputation to keep creating traceability data. Reputation available is " + myReputation + " and required stake is " + leastAmountOfReputationNecessary);
            return;
        }

        Double workUnitsNecessary = generateWorkUnitsNecessary();
        Double additionalValue = getValueAccordingToWorkUnitsNecessary(workUnitsNecessary, ChaincodeConfigs.baseValueOfTraceabilityDataEntry, maxPossibleAdditionalValue);
        TraceabilityDataAwaitingValidation data = new TraceabilityDataAwaitingValidation("", "", new ProcessingDetails("", "", "", ""), new EntityID(agent.getEntityId()), ChaincodeConfigs.baseValueOfTraceabilityDataEntry, validity, workUnitsNecessary, 0);
        try
        {
            BlockchainAPI.createTraceabilityDataEntry(data);
        } catch (ChaincodeException e)
        {
            logErrorWithMessage("got error on trying to create traceability data entry");
        }
    }

    private Double generateWorkUnitsNecessary()
    {
        NormalDistributionSamplingDescription distributionSamplingDescription = new NormalDistributionSamplingDescription(ChaincodeConfigs.meanWorkNecessaryForCreatedTraceabilityDataEntries, ChaincodeConfigs.standardDeviationWorkNecessaryForCreatedTraceabilityDataEntries);
        PRNGNormalDistribution prngNormalDistribution = new PRNGNormalDistribution(distributionSamplingDescription.getMean(), distributionSamplingDescription.getStandardDeviation());

        return prngNormalDistribution.getNextDouble();
    }

    private Double getValueAccordingToWorkUnitsNecessary(Double workUnitsNecessary, Double mean, Double maxPossibleAdditionalValue)
    {
        Double diffFromMean = workUnitsNecessary - mean;

        if (diffFromMean < 0)
            return ChaincodeConfigs.baseValueOfTraceabilityDataEntry;


        Double rand = ThreadLocalRandom.current().nextDouble();
        Double willingnessToAdditionalRewardWhenCreating = agent.getBehaviorIncidence().getWillingnessToAdditionalRewardWhenCreating();
        Double amountWillingToAdd;
        if (rand < willingnessToAdditionalRewardWhenCreating)
        {
            amountWillingToAdd = (willingnessToAdditionalRewardWhenCreating - rand) * maxPossibleAdditionalValue;
        } else
        {
            amountWillingToAdd = 0.0;
        }

        return ChaincodeConfigs.baseValueOfTraceabilityDataEntry + amountWillingToAdd;
    }
}
