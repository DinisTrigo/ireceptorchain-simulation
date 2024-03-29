package iReceptorPlus.Blockchain.iReceptorChain.VotingRoundStateMachine;

import iReceptorPlus.Blockchain.ChainDataTypes.EntityData;
import iReceptorPlus.Blockchain.ChainDataTypes.EntityID;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPIs.EntityDataRepositoryAPI;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPIs.Exceptions.ObjectWithGivenKeyNotFoundOnBlockchainDB;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPIs.HyperledgerFabricBlockhainRepositoryAPI;
import iReceptorPlus.Blockchain.iReceptorChain.VotingRoundStateMachine.Exceptions.EntityDoesNotHaveEnoughReputationToPerformAction;
import iReceptorPlus.Blockchain.iReceptorChain.VotingRoundStateMachine.Exceptions.ReferenceToNonexistentEntity;

import java.util.ArrayList;

/**
 * This class is used by the voting state machine classes to manage the reputation of a peer.
 * The methods should be called perform the changes to the reputation of the peer.
 */
public class EntityReputationManager
{
    /**
     * An instance of class HyperledgerFabricBlockhainRepositoryAPI that represents the repository for querying the data.
     */
    private final HyperledgerFabricBlockhainRepositoryAPI api;

    /**
     * Boolean identifying if negative reputation should be allowed.
     * If negative reputation is allowed, when subtracting reputation to the peer, no restrictions will be applied.
     */
    private boolean allowNegativeReputation;

    public EntityReputationManager(HyperledgerFabricBlockhainRepositoryAPI api)
    {
        this.api = api;
        this.allowNegativeReputation = false;
    }

    public EntityReputationManager(HyperledgerFabricBlockhainRepositoryAPI api, boolean allowNegativeReputation)
    {
        this.api = api;
        this.allowNegativeReputation = allowNegativeReputation;
    }

    public void stakeEntityReputation(EntityID entityID, Double stakeNecessary) throws ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPerformAction
    {
        updateEntityReputation(entityID, -stakeNecessary, stakeNecessary);
    }

    public void unstakeEntityReputation(EntityID entityID, Double unstakeAmount) throws ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPerformAction
    {
        updateEntityReputation(entityID, +unstakeAmount, -unstakeAmount);
    }

    public void unstakeEntitiesReputation(ArrayList<EntityID> entityIDS, Double unstakeAmount) throws ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPerformAction
    {
        for (EntityID currVoterID : entityIDS)
        {
            unstakeEntityReputation(currVoterID, unstakeAmount);
        }
    }

    public void rewardEntity(EntityID entityID, Double reward) throws ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPerformAction
    {
        updateEntityReputation(entityID, reward, new Double(0));
    }

    public void rewardEntities(ArrayList<EntityID> entityIDS, Double reward) throws ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPerformAction
    {
        for (EntityID currVoterID : entityIDS)
        {
            rewardEntity(currVoterID, reward);
        }
    }

    public void rewardEntities(ArrayList<EntityID> entityIDS, ArrayList<Double> rewards) throws ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPerformAction
    {
        for (int i = 0; i < entityIDS.size(); i++)
        {
            rewardEntity(entityIDS.get(i), rewards.get(i));
        }
    }

    public void splitRewardBetweenEntities(ArrayList<EntityID> entityIDS, Double reward) throws ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPerformAction
    {
        Double rewardForEachEntity = reward / entityIDS.size();
        for (EntityID currEntityID : entityIDS)
        {
            rewardEntity(currEntityID, rewardForEachEntity);
        }
    }

    public void penalizeEntity(EntityID entityID, Double penalty) throws ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPerformAction
    {
        updateEntityReputation(entityID, -penalty, new Double(0));
    }

    public void penalizeEntities(ArrayList<EntityID> entityIDS, Double penalty) throws ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPerformAction
    {
        for (EntityID currVoterID : entityIDS)
        {
            penalizeEntity(currVoterID, penalty);
        }
    }

    public void penalizeEntities(ArrayList<EntityID> entityIDS, ArrayList<Double> penalties) throws ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPerformAction
    {
        for (int i = 0; i < entityIDS.size(); i++)
        {
            penalizeEntity(entityIDS.get(i), penalties.get(i));
        }
    }

    public void splitPenaltyBetweenEntities(ArrayList<EntityID> entityIDS, Double penalty) throws ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPerformAction
    {
        Double penaltyForEachEntity = penalty / entityIDS.size();
        for (EntityID currEntityID : entityIDS)
        {
            penalizeEntity(currEntityID, penaltyForEachEntity);
        }
    }

    private void updateEntityReputation(EntityID entityID, Double addToCurrentReputation, Double addToReputationAtStake) throws ReferenceToNonexistentEntity, EntityDoesNotHaveEnoughReputationToPerformAction
    {
        EntityDataRepositoryAPI entityRepository = new EntityDataRepositoryAPI(api);
        EntityData entityData;
        try
        {
            entityData = (EntityData) entityRepository.read(entityID.getId());
        } catch (ObjectWithGivenKeyNotFoundOnBlockchainDB objectWithGivenKeyNotFoundOnBlockchainDB)
        {
            throw new ReferenceToNonexistentEntity(entityID.getId());
        }
        Double currentReputation = entityData.getReputation();
        Double reputationAtStake = entityData.getReputationAtStake();
        if (currentReputation < -addToCurrentReputation && !allowNegativeReputation)
        {
            throw new EntityDoesNotHaveEnoughReputationToPerformAction("Entity does not have enough reputation", currentReputation, addToReputationAtStake);
        }
        currentReputation += addToCurrentReputation;
        reputationAtStake += addToReputationAtStake;
        entityData = new EntityData(entityID.getId(), currentReputation, reputationAtStake);
        try
        {
            entityRepository.update(entityID.getId(), entityData);
        } catch (ObjectWithGivenKeyNotFoundOnBlockchainDB objectWithGivenKeyNotFoundOnBlockchainDB)
        {
            throw new ReferenceToNonexistentEntity(entityID.getId());
        }
    }
}