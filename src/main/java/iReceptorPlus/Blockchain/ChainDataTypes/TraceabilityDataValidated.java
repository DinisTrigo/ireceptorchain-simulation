package iReceptorPlus.Blockchain.ChainDataTypes;


import java.util.ArrayList;

/**
 * This is a subclass of TraceabilityData, representing traceability information in a specific state: after being validated and registered on the blockchain as valid traceability information.
 */

public class TraceabilityDataValidated extends TraceabilityData
{

    /**
     * An array of entities which have voted for the validity of the traceability information after its voting round had endeds.
     * (They have submitted a yes vote when the traceability information after it was already registered on the blockchain as valid.)
     * Each entry contains information about each entity that voted for the traceability information that corresponds to this class.
     */
    
    private final ArrayList<EntityID> corroborators;

    public TraceabilityDataValidated(final String inputDatasetHashValue,
                                     final String outputDatasetHashValue,
                                     final ProcessingDetails processingDetails,
                                     final EntityID creatorID,
                                     final ArrayList<EntityID> approvers,
                                     final ArrayList<EntityID> rejecters,
                                     final Double value,
                                     final Boolean valid,
                                     Double workUnitsNecessary,
                                     Long creationTime)
    {
        super(inputDatasetHashValue, outputDatasetHashValue, processingDetails, creatorID, approvers, rejecters, value, valid, workUnitsNecessary, creationTime);
        corroborators = new ArrayList<>();
    }

    @Override
    public void registerYesVoteForValidity(EntityID entityID)
    {
        corroborators.add(entityID);
    }

    @Override
    public void registerNoVoteForValidity(EntityID entityID)
    {
        //TODO

    }

    public ArrayList<EntityID> getCorroborators()
    {
        return corroborators;
    }
}
