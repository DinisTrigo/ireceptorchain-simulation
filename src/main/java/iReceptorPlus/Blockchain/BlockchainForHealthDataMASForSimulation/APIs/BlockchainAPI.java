package iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.APIs;

import iReceptorPlus.Blockchain.ChainDataTypes.EntityData;
import iReceptorPlus.Blockchain.ChainDataTypes.TraceabilityDataAwaitingValidation;
import iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.Enums;
import iReceptorPlus.Blockchain.RepastSimulation.MockBlockchainClasses.Database.BlockchainContext;
import iReceptorPlus.Blockchain.RepastSimulation.MockBlockchainClasses.Exceptions.ChaincodeException;
import iReceptorPlus.Blockchain.iReceptorChain.ChaincodeConfigs;
import iReceptorPlus.Blockchain.ChaincodeReturnDataTypes.TraceabilityDataAwaitingValidationReturnType;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPIs.EntityDataRepositoryAPI;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPIs.Exceptions.ObjectWithGivenKeyNotFoundOnBlockchainDB;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.EntityDataInfo;
import iReceptorPlus.Blockchain.iReceptorChain.iReceptorChain;

import java.util.ArrayList;
import java.util.UUID;

public class BlockchainAPI
{
    private static BlockchainContext context;
    private static iReceptorChain iReceptorChain = new iReceptorChain();

    public static void initBlockchainContext(BlockchainContext context)
    {
        BlockchainAPI.context = context;
    }

    public static ArrayList<TraceabilityDataAwaitingValidationReturnType> getTraceabilityDataAwaitingValidation()
    {
        TraceabilityDataAwaitingValidationReturnType[] data = iReceptorChain.getAllAwaitingValidationTraceabilityDataEntries(context);
        ArrayList<TraceabilityDataAwaitingValidationReturnType> dataArrayList = new ArrayList<>();
        for (TraceabilityDataAwaitingValidationReturnType currData : data)
        {
            dataArrayList.add(currData);
        }

        return dataArrayList;
    }

    public static Double getEntityReputation(String entityId) throws ObjectWithGivenKeyNotFoundOnBlockchainDB
    {
context.setClientId(entityId);
        EntityDataRepositoryAPI api = new EntityDataRepositoryAPI(context);
            EntityData entityData = (EntityData) api.read(entityId);

        return entityData.getReputation();
    }

    public static void submitVote(String entityId, String traceabilityDataUUID, Enums.VoteSide voteSide) throws ChaincodeException
    {
        context.setClientId(entityId);
        if (voteSide == Enums.VoteSide.UPVOTE)
        {
            iReceptorChain.registerYesVoteForTraceabilityEntryInVotingRound(context, traceabilityDataUUID);
        }
        else
        {
            iReceptorChain.registerNoVoteForTraceabilityEntryInVotingRound(context, traceabilityDataUUID);
        }
    }

    public static Double getBaseValueOfTraceabilityData()
    {
        return ChaincodeConfigs.baseValueOfTraceabilityDataEntry;
    }

    public static TraceabilityDataAwaitingValidationReturnType createTraceabilityDataEntry(TraceabilityDataAwaitingValidation data) throws ChaincodeException
    {
        return iReceptorChain.createTraceabilityDataEntryByObject(context, UUID.randomUUID().toString(), data);
    }

    public static class ReputationChangeCalculator
    {
        public static Double calculateStakeRatioForCreatingTraceabilityData(Double value)
        {
            return ChaincodeConfigs.reputationChangeCalculator.calculateStakeRatioForCreatingTraceabilityData(value);
        }

        public static Double calculateStakeRatioForUpVotingTraceabilityData(Double value)
        {
            return ChaincodeConfigs.reputationChangeCalculator.calculateStakeRatioForUpVotingTraceabilityData(value);
        }

        public static Double calculateStakeRatioForDownVotingTraceabilityData(Double value)
        {
            return ChaincodeConfigs.reputationChangeCalculator.calculateStakeRatioForDownVotingTraceabilityData(value);
        }

        public static Double calculatePenaltyRatioForCreatingIncorrectTraceabilityData(Double value)
        {
            return ChaincodeConfigs.reputationChangeCalculator.calculatePenaltyRatioForCreatingIncorrectTraceabilityData(value);
        }

        public static Double calculatePenaltyRatioForUpVotingIncorrectTraceabilityData(Double value)
        {
            return ChaincodeConfigs.reputationChangeCalculator.calculatePenaltyRatioForUpVotingIncorrectTraceabilityData(value);
        }

        public static Double calculatePenaltyRatioForDownVotingCorrectTraceabilityData(Double value)
        {
            return ChaincodeConfigs.reputationChangeCalculator.calculatePenaltyRatioForDownVotingCorrectTraceabilityData(value);
        }

        public static Double calculateRewardRatioForCreatingCorrectTraceabilityData(Double value)
        {
            return ChaincodeConfigs.reputationChangeCalculator.calculateRewardRatioForCreatingCorrectTraceabilityData(value);
        }

        public static Double calculateRewardRatioForUpVotingCorrectTraceabilityData(Double value)
        {
            return ChaincodeConfigs.reputationChangeCalculator.calculateRewardRatioForUpVotingCorrectTraceabilityData(value);
        }

        public static Double calculateRewardRatioForDownVotingIncorrectTraceabilityData(Double value)
        {
            return ChaincodeConfigs.reputationChangeCalculator.calculateRewardRatioForDownVotingIncorrectTraceabilityData(value);
        }
    }
}
