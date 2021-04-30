package iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.Agents.VotingBehaviors;

import iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.APIs.BlockchainAPI;
import iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.Agents.Agent;
import iReceptorPlus.Blockchain.ChainDataTypes.TraceabilityDataAwaitingValidation;
import iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.Enums.VoteSide;
import iReceptorPlus.Blockchain.ChaincodeReturnDataTypes.TraceabilityDataAwaitingValidationReturnType;
import iReceptorPlus.Blockchain.RepastSimulation.MockBlockchainClasses.Exceptions.ChaincodeException;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPIs.Exceptions.ObjectWithGivenKeyNotFoundOnBlockchainDB;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Random extends UnEthical
{
    public Random(Agent agent)
    {
        super(agent);
    }

    @Override
    public void perform()
    {
        TraceabilityDataAwaitingValidationReturnType traceabilityDataAwaitingValidation = pickWorkToDo();

        int voteNum = ThreadLocalRandom.current().nextInt() % 2;
        VoteSide voteSide;
        if (voteNum == 0)
            voteSide = VoteSide.UPVOTE;
        else
            voteSide = VoteSide.DOWNVOTE;

        try
        {
            BlockchainAPI.submitVote(agent.getEntityId(), traceabilityDataAwaitingValidation.getUuid(), voteSide);
        } catch (ChaincodeException e)
        {
            logErrorWithMessage("got error on trying to submit vote for traceability data with id " + traceabilityDataAwaitingValidation.getUuid());
        }
    }

    @Override
    protected TraceabilityDataAwaitingValidationReturnType pickWorkToDo()
    {
        ArrayList<TraceabilityDataAwaitingValidationReturnType> workList = null;
        try
        {
            workList = getWorkToDoSortedByBestRewardThatReputationIsEnoughToVoteForAfterFactoringRisk(BlockchainAPI.getEntityReputation(agent.getEntityId()), agent.getRisk());
        } catch (ObjectWithGivenKeyNotFoundOnBlockchainDB objectWithGivenKeyNotFoundOnBlockchainDB)
        {
            logErrorOnTryingToGetMyReputation("on trying to pick work to do");
            return null;
        }

        return workList.get(0);
    }
}
