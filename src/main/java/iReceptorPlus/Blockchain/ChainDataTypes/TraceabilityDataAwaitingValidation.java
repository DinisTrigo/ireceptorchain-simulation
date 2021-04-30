package iReceptorPlus.Blockchain.ChainDataTypes;


import java.util.ArrayList;

/**
 * This is a subclass of TraceabilityData, representing traceability information in a specific state: after the entry was first created and submitted to the blockchain.
 */

public class TraceabilityDataAwaitingValidation extends TraceabilityData
{

    public TraceabilityDataAwaitingValidation(String inputDatasetHashValue,
                                              String outputDatasetHashValue,
                                              ProcessingDetails processingDetails,
                                              EntityID creatorID,
                                              Double value,
                                              Boolean valid,
                                              Double workUnitsNecessary,
                                              long creationTime)
    {
        super(inputDatasetHashValue, outputDatasetHashValue, processingDetails, creatorID, value, valid, workUnitsNecessary, creationTime);
        approvers = new ArrayList<>();
        rejecters = new ArrayList<>();
        this.value = value;
    }

    public TraceabilityDataAwaitingValidation(String inputDatasetHashValue,
                                              String outputDatasetHashValue,
                                              ProcessingDetails processingDetails,
                                              EntityID creatorID,
                                              ArrayList<EntityID> approvers,
                                              ArrayList<EntityID> rejecters,
                                              Double value,
                                              Boolean valid,
                                              Double workUnitsNecessary,
                                              long creationTime)
    {
        super(inputDatasetHashValue, outputDatasetHashValue, processingDetails, creatorID, approvers, rejecters, value, valid, workUnitsNecessary, creationTime);
    }

    /**
     * Registers the entityID passed as parameter as an approver of the traceability information.
     * @param entityID An instance of class EntityID containing information about the entityID that has voted yes for the traceability information. For more information, please read the documentation for the EntityID class.
     */
    @Override
    public void registerYesVoteForValidity(EntityID entityID)
    {
        approvers.add(entityID);
    }

    /**
     * Registers the entityID passed as parameter as a rejecter of the traceability information.
     * @param entityID An instance of class EntityID containing information about the entityID that has voted yes for the traceability information. For more information, please read the documentation for the EntityID class.
     */
    @Override
    public void registerNoVoteForValidity(EntityID entityID)
    {
        rejecters.add(entityID);
    }


}
