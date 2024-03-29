package iReceptorPlus.Blockchain.ChainDataTypes;



import java.util.ArrayList;
import java.util.Objects;

/**
 * This class represents an entry of traceability information.
 * This is the base class for traceability information.
 * Sub classes are used where there are necessary additional attributes, depending on the state of validation of the traceability information.
 * Please check the VotingRoundStateMachine package for more information.
 */

public abstract class TraceabilityData implements iReceptorChainDataType
{
    /**
     * The hash value of the input dataset used to perform the data transformation.
     * This is used to validate the integrity of the input dataset, in order to be able to verify the traceability information.
     */
    
    protected final String inputDatasetHashValue;

    /**
     * The hash value of the output dataset used to perform the data transformation.
     * This is used to validate the integrity of the input dataset, in order to be able to verify the traceability information.
     */
    
    protected final String outputDatasetHashValue;

    /**
     * This is an instance of the class ProcessingDetails which contains information regarding the steps taken to perform the data transformation.
     * These steps are necessary in order to check the veracity of the traceability information.
     */
    
    protected final ProcessingDetails processingDetails;

    /**
     * An instance of class EntityID containing information about the id of the entity that created the traceability data entry.
     */
    
    protected final EntityID creatorID;

    /**
     * An array of entities who have submitted a YES vote for the validity of the traceability information.
     * Each entry contains information about each entity that voted for the traceability information that corresponds to this class.
     */
    
    protected ArrayList<EntityID> approvers;

    /**
     * An array of entities who have submitted a NO vote for the validity of the traceability information.
     * Each entry contains information about each entity that voted for the traceability information that corresponds to this class.
     */
    
    protected ArrayList<EntityID> rejecters;

    /**
     * The value of this traceability data that will be used to calculate rewards and penalties for the voters.
     * Optionally, the creator may decide to include an additional reward that will be split among the traceability data validators.
     * The double representing the reward will be available to be consulted even after the traceability data is registered as validated.
     */
    
    protected Double value;

    protected Boolean valid;

    protected Double workUnitsNecessary;

    protected long creationTime;

    public TraceabilityData(String inputDatasetHashValue,
                            String outputDatasetHashValue,
                            ProcessingDetails processingDetails,
                            EntityID creatorID)
    {
        this.inputDatasetHashValue = inputDatasetHashValue;
        this.outputDatasetHashValue = outputDatasetHashValue;
        this.processingDetails = processingDetails;
        this.creatorID = creatorID;
    }

    public TraceabilityData(String inputDatasetHashValue,
                            String outputDatasetHashValue,
                            ProcessingDetails processingDetails,
                            EntityID creatorID,
                            Double value,
                            Boolean valid,
                            Double workUnitsNecessary,
                            Long creationTime)
    {
        this.inputDatasetHashValue = inputDatasetHashValue;
        this.outputDatasetHashValue = outputDatasetHashValue;
        this.processingDetails = processingDetails;
        this.creatorID = creatorID;
        this.value = value;
        this.valid = valid;
        this.workUnitsNecessary = workUnitsNecessary;
        this.creationTime = creationTime;
    }

    public TraceabilityData(String inputDatasetHashValue,
                            String outputDatasetHashValue,
                            ProcessingDetails processingDetails,
                            EntityID creatorID,
                            ArrayList<EntityID> approvers,
                            ArrayList<EntityID> rejecters,
                            Double value,
                            Boolean valid,
                            Double workUnitsNecessary,
                            Long creationTime)
    {
        this.inputDatasetHashValue = inputDatasetHashValue;
        this.outputDatasetHashValue = outputDatasetHashValue;
        this.processingDetails = processingDetails;
        this.creatorID = creatorID;
        this.approvers = approvers;
        this.rejecters = rejecters;
        this.value = value;
        this.valid = valid;
        this.workUnitsNecessary = workUnitsNecessary;
    }

    public String getInputDatasetHashValue()
    {
        return inputDatasetHashValue;
    }

    public String getOutputDatasetHashValue()
    {
        return outputDatasetHashValue;
    }

    public ProcessingDetails getProcessingDetails()
    {
        return processingDetails;
    }

    public EntityID getCreatorID()
    {
        return creatorID;
    }

    public ArrayList<EntityID> getApprovers()
    {
        return approvers;
    }

    public ArrayList<EntityID> getRejecters()
    {
        return rejecters;
    }

    public Boolean getValid()
    {
        return valid;
    }

    public Double getValue()
    {
        return value;
    }

    public Double getWorkUnitsNecessary()
    {
        return workUnitsNecessary;
    }

    /**
     * This method is called whenever a user votes yes for the validity of the traceability information.
     * Depending on the state of the traceability information, different actions are necessary, so the methods must be overridden by the derived classes that implement specific behavior necessary for each state.
     * @param entityID An instance of class EntityID containing information about the entityID that has voted yes for the traceability information. For more information, please read the documentation for the EntityID class.
     */
    public abstract void registerYesVoteForValidity(EntityID entityID);

    /**
     * This method is called whenever a user votes no for the validity of the traceability information.
     * Depending on the state of the traceability information, different actions are necessary, so the methods must be overridden by the derived classes that implement specific behavior necessary for each state.
     * @param entityID An instance of class EntityID containing information about the entityID that has voted yes for the traceability information. For more information, please read the documentation for the EntityID class.
     */
    public abstract void registerNoVoteForValidity(EntityID entityID);

    /**
     * Returns the number of approvers of the traceability information represented by this class.
     * @return the number of approvers of the traceability information represented by this class.
     */
    public long getNumberOfApprovers()
    {
        return approvers.size();
    }

    /**
     * Returns the number of rejecters of the traceability information represented by this class.
     * @return the number of rejecters of the traceability information represented by this class.
     */
    public long getNumberOfRejecters()
    {
        return rejecters.size();
    }

    public long getCreationTime()
    {
        return creationTime;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TraceabilityData that = (TraceabilityData) o;
        return inputDatasetHashValue.equals(that.inputDatasetHashValue) &&
                outputDatasetHashValue.equals(that.outputDatasetHashValue) &&
                processingDetails.equals(that.processingDetails) &&
                creatorID.equals(that.creatorID) &&
                approvers.equals(that.approvers) &&
                rejecters.equals(that.rejecters);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(inputDatasetHashValue, outputDatasetHashValue, processingDetails, creatorID, approvers, rejecters);
    }
}
