package iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.Agents.VotingBehaviors;

import iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.APIs.BlockchainAPI;
import iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.Agents.Agent;
import iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.Agents.Behavior;
import iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.LoggingAndSettings.Logging;
import iReceptorPlus.Blockchain.ChaincodeReturnDataTypes.TraceabilityDataAwaitingValidationReturnType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public abstract class VotingBehavior extends Behavior
{

    public VotingBehavior(Agent agent)
    {
        super(agent);
    }

    protected abstract TraceabilityDataAwaitingValidationReturnType pickWorkToDo();

    protected ArrayList<TraceabilityDataAwaitingValidationReturnType> getAllWorkToDo()
    {
        ArrayList<TraceabilityDataAwaitingValidationReturnType> workList = BlockchainAPI.getTraceabilityDataAwaitingValidation();
        removeAllTraceabilityDataCreatedByThisAgent(workList);
        if (workList.isEmpty())
            Logging.Log("Agent " + agent.getEntityId() + " got empty work list from the blockchain");
        else if(workList.get(0).getTraceabilityDataAwaitingValidationData().getValue() <= 0)
            Logging.LogError("All traceability data returned from blockchain have rewards less or equal to zero. Please check blockchain information integrity.");

        return workList;
    }

    private void removeAllTraceabilityDataCreatedByThisAgent(ArrayList<TraceabilityDataAwaitingValidationReturnType> workList)
    {
        for (int i = 0; i < workList.size(); i++)
        {
            TraceabilityDataAwaitingValidationReturnType traceabilityDataAwaitingValidationReturnType = workList.get(i);
            if (traceabilityDataAwaitingValidationReturnType.getTraceabilityDataAwaitingValidationData().getCreatorID().getId().equals(agent.getEntityId()))
                workList.remove(i);
        }
    }

    protected ArrayList<TraceabilityDataAwaitingValidationReturnType> getWorkToDoSortedByBestRewardToWorkRatio()
    {
        ArrayList<TraceabilityDataAwaitingValidationReturnType> workList = getAllWorkToDo();
        workList = getSortedByBestRewardToWorkRatio(workList);

        return workList;
    }

    protected ArrayList<TraceabilityDataAwaitingValidationReturnType> getWorkToDoSortedByBestReward()
    {
        ArrayList<TraceabilityDataAwaitingValidationReturnType> workList = getAllWorkToDo();
        workList = getSortedByBestReward(workList);

        return workList;
    }

    protected ArrayList<TraceabilityDataAwaitingValidationReturnType> getWorkToDoSortedByBestRewardToWorkRatioThatReputationIsEnoughToVoteFor(Double reputation)
    {
        ArrayList<TraceabilityDataAwaitingValidationReturnType> workList = getAllWorkToDo();
        workList = getSortedByBestRewardToWorkRatio(workList);
        workList = getTraceabilityDataThatReputationIsEnoughToVoteFor(workList, reputation);

        return workList;
    }

    protected ArrayList<TraceabilityDataAwaitingValidationReturnType> getWorkToDoSortedByBestRewardThatReputationIsEnoughToVoteFor(Double reputation)
    {
        ArrayList<TraceabilityDataAwaitingValidationReturnType> workList = getAllWorkToDo();
        workList = getSortedByBestReward(workList);
        workList = getTraceabilityDataThatReputationIsEnoughToVoteFor(workList, reputation);

        return workList;
    }

    protected ArrayList<TraceabilityDataAwaitingValidationReturnType> getWorkToDoSortedByBestRewardToWorkRatioThatReputationIsEnoughToVoteForAfterFactoringRisk(Double reputation, Double risk)
    {
        ArrayList<TraceabilityDataAwaitingValidationReturnType> workList = getAllWorkToDo();
        workList = getSortedByBestRewardToWorkRatio(workList);
        workList = getTraceabilityDataThatReputationIsEnoughToVoteForAfterFactoringRisk(workList, reputation, risk);

        return workList;
    }

    protected ArrayList<TraceabilityDataAwaitingValidationReturnType> getWorkToDoSortedByBestRewardThatReputationIsEnoughToVoteForAfterFactoringRisk(Double reputation, Double risk)
    {
        ArrayList<TraceabilityDataAwaitingValidationReturnType> workList = getAllWorkToDo();
        workList = getSortedByBestReward(workList);
        workList = getTraceabilityDataThatReputationIsEnoughToVoteForAfterFactoringRisk(workList, reputation, risk);

        return workList;
    }

    protected ArrayList<TraceabilityDataAwaitingValidationReturnType> getSortedByBestRewardToWorkRatio(ArrayList<TraceabilityDataAwaitingValidationReturnType> traceabilityDataList)
    {
        ArrayList<TraceabilityDataAwaitingValidationReturnType> sorted = new ArrayList<TraceabilityDataAwaitingValidationReturnType>(traceabilityDataList);
        Collections.sort(sorted, new Comparator<TraceabilityDataAwaitingValidationReturnType>()
        {
            @Override
            public int compare(TraceabilityDataAwaitingValidationReturnType o1, TraceabilityDataAwaitingValidationReturnType o2)
            {
                double ratio1 = o1.getTraceabilityDataAwaitingValidationData().getValue() / o1.getTraceabilityDataAwaitingValidationData().getWorkUnitsNecessary();
                double ratio2 = o2.getTraceabilityDataAwaitingValidationData().getValue() / o2.getTraceabilityDataAwaitingValidationData().getWorkUnitsNecessary();
                if (ratio1 < ratio2)
                    return -1;
                else if (ratio1 == ratio2)
                    return 0;
                else
                    return 1;
            }
        });

        return sorted;
    }

    protected ArrayList<TraceabilityDataAwaitingValidationReturnType> getSortedByBestReward(ArrayList<TraceabilityDataAwaitingValidationReturnType> traceabilityDataList)
    {
        ArrayList<TraceabilityDataAwaitingValidationReturnType> sorted = new ArrayList<TraceabilityDataAwaitingValidationReturnType>(traceabilityDataList);
        Collections.sort(sorted, new Comparator<TraceabilityDataAwaitingValidationReturnType>()
        {
            @Override
            public int compare(TraceabilityDataAwaitingValidationReturnType o1, TraceabilityDataAwaitingValidationReturnType o2)
            {
                double value1 = o1.getTraceabilityDataAwaitingValidationData().getValue();
                double value2 = o2.getTraceabilityDataAwaitingValidationData().getValue();
                if (value1 < value2)
                    return -1;
                else if (value1 == value2)
                    return 0;
                else
                    return 1;
            }
        });

        return sorted;
    }

    protected ArrayList<TraceabilityDataAwaitingValidationReturnType> getTraceabilityDataThatReputationIsEnoughToVoteFor(ArrayList<TraceabilityDataAwaitingValidationReturnType> traceabilityDataList, Double reputationAvailable)
    {
        ArrayList<TraceabilityDataAwaitingValidationReturnType> isEnoughSet = new ArrayList<>();
        for (TraceabilityDataAwaitingValidationReturnType traceabilityDataAwaitingValidation : traceabilityDataList)
        {
            double min = Math.min(BlockchainAPI.ReputationChangeCalculator.calculateStakeRatioForUpVotingTraceabilityData(traceabilityDataAwaitingValidation.getTraceabilityDataAwaitingValidationData().getValue()),
                    BlockchainAPI.ReputationChangeCalculator.calculateStakeRatioForDownVotingTraceabilityData(traceabilityDataAwaitingValidation.getTraceabilityDataAwaitingValidationData().getValue()));
            if (reputationAvailable >= min)
            {
                isEnoughSet.add(traceabilityDataAwaitingValidation);
            }
        }

        return isEnoughSet;
    }

    protected ArrayList<TraceabilityDataAwaitingValidationReturnType> getTraceabilityDataThatReputationIsEnoughToVoteForAfterFactoringRisk(ArrayList<TraceabilityDataAwaitingValidationReturnType> traceabilityDataList,
                                                                                                                                 Double reputationAvailable,
                                                                                                                                 Double risk)
    {
        double reputationAvailableGivenRisk = reputationAvailable * ((risk - Agent.MIN_RISK) / (Agent.MAX_RISK - Agent.MIN_RISK));

        return getTraceabilityDataThatReputationIsEnoughToVoteFor(traceabilityDataList, reputationAvailableGivenRisk);
    }


    /**
     * This method simulates the agent working. It should be working for the seconds equal to the work units divided by its work rate capacity.
     * @param workUnits The amount of work units that the task needs to be completed.
     */
    public abstract void work(Double workUnits) throws InterruptedException;
}
