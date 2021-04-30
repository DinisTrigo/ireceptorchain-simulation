package iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.Agents.VotingBehaviors;

import iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.APIs.BlockchainAPI;
import iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.Agents.Agent;
import iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.LoggingAndSettings.Logging;
import iReceptorPlus.Blockchain.ChainDataTypes.TraceabilityDataAwaitingValidation;
import iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.Enums.VoteSide;
import iReceptorPlus.Blockchain.ChaincodeReturnDataTypes.TraceabilityDataAwaitingValidationReturnType;
import iReceptorPlus.Blockchain.RepastSimulation.MockBlockchainClasses.Exceptions.ChaincodeException;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPIs.Exceptions.ObjectWithGivenKeyNotFoundOnBlockchainDB;

import java.util.ArrayList;

/**
 * This class implements a behavior that always acts based on majority.
 * It calculates the value by weighting the majority and the reward of the traceability data.
 * The class can either follow or contradict the majority, accordingly to the boolean passed to the constructor.
 */
public class Majority extends UnEthical
{
    public class MajorityDescription
    {
        public VoteSide voteSide;
        public Double ratio;

        public MajorityDescription(VoteSide voteSide, Double ratio)
        {
            this.voteSide = voteSide;
            this.ratio = ratio;
        }
    }

    /**
     * The importance given to the majority when choosing the traceability data entry to vote for.
     * The significance of this value is relative to the importance of reward value, stored in variable rewardImportance).
     */
    protected Double majorityImportance;
    /**
     * The importance given to the reward when choosing the traceability data entry to vote for.
     * The significance of this value is relative to the importance of majority, stored in variable majorityImportance).
     */
    protected Double rewardImportance;

    /**
     * Tells whether the behavior will follow or contradict the majority.
     * If true, the behavior will follow majority.
     * If false, the behavior will contradict majority.
     */
    protected boolean followMajority;

    public Majority(Agent agent, Double majorityImportance, Double rewardImportance, boolean followMajority)
    {
        super(agent);
        this.majorityImportance = majorityImportance;
        this.rewardImportance = rewardImportance;
        this.followMajority = followMajority;
    }

    public MajorityDescription getMajorityDescription(TraceabilityDataAwaitingValidation traceabilityDataAwaitingValidation)
    {
        int amountOfApprovers = traceabilityDataAwaitingValidation.getApprovers().size();
        int amountOfRejecters = traceabilityDataAwaitingValidation.getRejecters().size();
        int total = amountOfApprovers + amountOfRejecters;

        VoteSide majoritySide = null;
        Double ratio = 0.0;
        if (amountOfApprovers > amountOfRejecters)
        {
            majoritySide = VoteSide.UPVOTE;
            ratio = (double) amountOfApprovers / total;
        }
        else if (amountOfApprovers < amountOfRejecters)
        {
            majoritySide = VoteSide.DOWNVOTE;
            ratio = (double) amountOfRejecters / total;
        }

        return new MajorityDescription(majoritySide, ratio);
    }

    @Override
    public void perform()
    {
        TraceabilityDataAwaitingValidationReturnType workPicked = pickWorkToDo();

        MajorityDescription majorityDescription = getMajorityDescription(workPicked.getTraceabilityDataAwaitingValidationData());
        VoteSide finalVote = majorityDescription.voteSide;
        if (!followMajority)
        {
            if (finalVote == VoteSide.UPVOTE)
                finalVote = VoteSide.DOWNVOTE;
            else
                finalVote = VoteSide.UPVOTE;
        }

        try
        {
            BlockchainAPI.submitVote(agent.getEntityId(), workPicked.getUuid(), finalVote);
        } catch (ChaincodeException e)
        {
            Logging.LogError("Agent " + agent.getEntityId() + " got exception on submitting vote.");
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
        Double bestReward = workList.get(0).getTraceabilityDataAwaitingValidationData().getValue();
        ArrayList<Double> weightedValue = new ArrayList<Double>();
        for (TraceabilityDataAwaitingValidationReturnType work : workList)
        {
            MajorityDescription majorityDescription = getMajorityDescription(work.getTraceabilityDataAwaitingValidationData());
            Double currentValue = (work.getTraceabilityDataAwaitingValidationData().getValue() / bestReward) * rewardImportance + majorityDescription.ratio * majorityImportance;
            weightedValue.add(currentValue);
        }

        Double maxWeightedValue = 0.0;
        int maxWeightedValueIdx = 0;
        for (int i = 0; i < weightedValue.size(); i++)
        {
            Double value = workList.get(i).getTraceabilityDataAwaitingValidationData().getValue();
            if (value > maxWeightedValue)
            {
                maxWeightedValue = value;
                maxWeightedValueIdx = i;
            }
        }

        return workList.get(maxWeightedValueIdx);
    }
}
