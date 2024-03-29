package iReceptorPlus.Blockchain.iReceptorChain.VotingRoundStateMachine.ReputationDistributer;

import iReceptorPlus.Blockchain.ChainDataTypes.EntityID;

import java.util.ArrayList;

public interface ReputationDistributor
{
    ArrayList<Double> distributeReputation(Double reputation, ArrayList<EntityID> entities);
}
