package iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.Agents.VotingBehaviors;

import iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.APIs.BlockchainAPI;
import iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.Agents.Agent;
import iReceptorPlus.Blockchain.ChainDataTypes.TraceabilityDataAwaitingValidation;
import iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.Enums;
import iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.LoggingAndSettings.Logging;
import iReceptorPlus.Blockchain.ChaincodeReturnDataTypes.TraceabilityDataAwaitingValidationReturnType;
import iReceptorPlus.Blockchain.RepastSimulation.MockBlockchainClasses.Exceptions.ChaincodeException;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPIs.Exceptions.ObjectWithGivenKeyNotFoundOnBlockchainDB;

import java.util.ArrayList;

public class Ethical extends VotingBehavior
{
    public Ethical(Agent agent)
    {
        super(agent);
    }

    @Override
    public void perform()
    {
        TraceabilityDataAwaitingValidationReturnType work = pickWorkToDo();
        Double workUnits = work.getTraceabilityDataAwaitingValidationData().getWorkUnitsNecessary();
        try
        {
            work(workUnits);
        } catch (InterruptedException e)
        {
            Logging.Log("Agent " + agent.getEntityId() + " got interrupted while waiting for work to get done.");
        }
        boolean valid = work.getTraceabilityDataAwaitingValidationData().getValid();
        Enums.VoteSide voteSide;
        if (valid)
            voteSide = Enums.VoteSide.UPVOTE;
        else
            voteSide = Enums.VoteSide.DOWNVOTE;

        try
        {
            BlockchainAPI.submitVote(agent.getEntityId(), work.getUuid(), voteSide);
        } catch (ChaincodeException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected TraceabilityDataAwaitingValidationReturnType pickWorkToDo()
    {
        Double currentReputation = null;
        try
        {
            currentReputation = BlockchainAPI.getEntityReputation(agent.getEntityId());
        } catch (ObjectWithGivenKeyNotFoundOnBlockchainDB objectWithGivenKeyNotFoundOnBlockchainDB)
        {
            logErrorOnTryingToGetMyReputation("on trying to pick work to do");
            return null;
        }
        ArrayList<TraceabilityDataAwaitingValidationReturnType> workList = getWorkToDoSortedByBestRewardToWorkRatioThatReputationIsEnoughToVoteForAfterFactoringRisk(currentReputation, agent.getRisk());
        TraceabilityDataAwaitingValidationReturnType workToPerform = workList.get(0);

        return workToPerform;
    }

    @Override
    public void work(Double workUnits) throws InterruptedException
    {
        long milliSecondsToCompleteTask = (long) ((workUnits / agent.getWorkRateCapacity()) * 1000);
        Thread.sleep(milliSecondsToCompleteTask);
    }
}
