package iReceptorPlus.Blockchain.iReceptorChain.VotingRoundStateMachine.ReputationDistributer;

import iReceptorPlus.Blockchain.ChainDataTypes.EntityID;

import java.util.ArrayList;

public class ReputationDistributorWithoutSplit implements ReputationDistributor
{
    @Override
    public ArrayList<Double> distributeReputation(Double reputation, ArrayList<EntityID> entities)
    {
        ArrayList<Double> distribution = new ArrayList<>(entities.size());
        for (EntityID entityDataInfo : entities)
        {
            distribution.add(reputation);
        }

        return distribution;
    }
}
