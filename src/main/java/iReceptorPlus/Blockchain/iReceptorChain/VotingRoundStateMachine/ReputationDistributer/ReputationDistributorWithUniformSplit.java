package iReceptorPlus.Blockchain.iReceptorChain.VotingRoundStateMachine.ReputationDistributer;

import iReceptorPlus.Blockchain.ChainDataTypes.EntityID;

import java.util.ArrayList;

public class ReputationDistributorWithUniformSplit implements ReputationDistributor
{
    @Override
    public ArrayList<Double> distributeReputation(Double reputation, ArrayList<EntityID> entities)
    {
        ArrayList<Double> distribution = new ArrayList<>(entities.size());
        Double reputationForEachEntity = reputation / entities.size();
        for (EntityID entityDataInfo : entities)
        {
            distribution.add(reputationForEachEntity);
        }

        return distribution;
    }
}
