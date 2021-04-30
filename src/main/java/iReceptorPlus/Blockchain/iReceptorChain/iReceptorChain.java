/*
 * SPDX-License-Identifier: Apache-2.0
 */

package iReceptorPlus.Blockchain.iReceptorChain;

import iReceptorPlus.Blockchain.RepastSimulation.MockBlockchainClasses.Database.BlockchainContext;
import iReceptorPlus.Blockchain.RepastSimulation.MockBlockchainClasses.Database.ChaincodeStub;
import iReceptorPlus.Blockchain.RepastSimulation.MockBlockchainClasses.Exceptions.ChaincodeException;
import iReceptorPlus.Blockchain.ChainDataTypes.*;
import iReceptorPlus.Blockchain.ChaincodeReturnDataTypes.EntityDataReturnType;
import iReceptorPlus.Blockchain.ChaincodeReturnDataTypes.TraceabilityDataAwaitingValidationReturnType;
import iReceptorPlus.Blockchain.ChaincodeReturnDataTypes.TraceabilityDataReturnType;
import iReceptorPlus.Blockchain.ChaincodeReturnDataTypes.TraceabilityDataValidatedReturnType;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPIs.EntityDataRepositoryAPI;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPIs.Exceptions.GivenIdIsAlreadyAssignedToAnotherObject;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPIs.Exceptions.ObjectWithGivenKeyNotFoundOnBlockchainDB;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPIs.HyperledgerFabricBlockhainRepositoryAPI;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPIs.TraceabilityDataAwaitingValidationRepositoryAPI;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPIs.TraceabilityDataValidatedRepositoryAPI;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.EntityDataInfo;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.TraceabilityDataInfo;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.iReceptorChainDataTypeInfo;
import iReceptorPlus.Blockchain.iReceptorChain.VotingRoundStateMachine.Exceptions.*;
import iReceptorPlus.Blockchain.iReceptorChain.VotingRoundStateMachine.Returns.VotingStateMachineReturn;
import iReceptorPlus.Blockchain.iReceptorChain.VotingRoundStateMachine.TraceabilityDataStateMachine;

import java.util.ArrayList;

/**
 * Java implementation of the Fabric Car Contract described in the Writing Your
 * First Application tutorial
 */

public final class iReceptorChain {


    private enum FabCarErrors {
        CAR_NOT_FOUND,
        CAR_ALREADY_EXISTS
    }

    private EntityID getEntityIdFromBlockchainContext(BlockchainContext ctx)
    {
        return new EntityID(ctx.getClientIdentity().getId());
    }



    public String readMockTraceabilityData(final BlockchainContext ctx, final String uuid) throws ChaincodeException
    {
        TraceabilityDataAwaitingValidationRepositoryAPI api = new TraceabilityDataAwaitingValidationRepositoryAPI(ctx);
        try
        {
            api.read(uuid);
        } catch (ObjectWithGivenKeyNotFoundOnBlockchainDB objectWithGivenKeyNotFoundOnBlockchainDB)
        {
            throw new ChaincodeException("read failed");
        }

        return "success";
    }


    public String testVote(final BlockchainContext ctx, final String uuid) throws ChaincodeException
    {
        logDebugMsg("registerYesVoteForTraceabilityEntryInVotingRound");

        TraceabilityDataStateMachine traceabilityDataStateMachine = getTraceabilityDataFromDBAndBuildVotingStateMachine(ctx, uuid);
        VotingStateMachineReturn votingStateMachineReturn;
        try
        {
            votingStateMachineReturn = traceabilityDataStateMachine.voteYesForTheVeracityOfTraceabilityInfo(getEntityIdFromBlockchainContext(ctx));
        } catch (IncosistentInfoFoundOnDB incosistentInfoFoundOnDB)
        {
            throw new ChaincodeException(incosistentInfoFoundOnDB.getMessage());
        } catch (EntityDoesNotHaveEnoughReputationToPlaceVote entityDoesNotHaveEnoughReputationToPlaceVote)
        {
            throw new ChaincodeException(entityDoesNotHaveEnoughReputationToPlaceVote.getMessage());
        } catch (ReferenceToNonexistentEntity referenceToNonexistentEntity)
        {
            throw new ChaincodeException(referenceToNonexistentEntity.getMessage());
        }

        logDebugMsg("registerYesVoteForTraceabilityEntryInVotingRound END");

        return votingStateMachineReturn.getMessage();
    }

    /**
     * Creates a new entity on the ledger.
     *
     * @param ctx the transaction context
     * @param entityID A string representing the id of the entity.
     * @return the EntityData entry just created on the blockchain.
     */

    public EntityDataInfo createEntityById(final BlockchainContext ctx, final String entityID) throws ChaincodeException
    {
        logDebugMsg("createTraceabilityDataEntry");

        ChaincodeStub stub = ctx.getStub();

        EntityData entityData = new EntityData(entityID);
        EntityDataInfo entityDataInfo = new EntityDataInfo(entityData.getId(), entityData);
        EntityDataRepositoryAPI api = new EntityDataRepositoryAPI(ctx);
        try
        {
            api.create(entityDataInfo);
        } catch (GivenIdIsAlreadyAssignedToAnotherObject givenIdIsAlreadyAssignedToAnotherObject)
        {
            throw new ChaincodeException("Entity with the same id already exists on the blockchain database");
        }

        return entityDataInfo;
    }

    /**
     * Creates a new traceability data entry on the ledger.
     * The entry is placed on the pool of traceability data's waiting to be validated by peers.
     * @param ctx the transaction context
     * @param newUUID the new UUID of the object to be created. This is generated at client-side in order to avoid different blockchain nodes reaching different ids for the same transaction for a creation of an object.
     * @param traceabilityData An instance of class TraceabilityDataAwaitingValidation containing the traceability data to be inserted in the blockchain.
     */

    /**
     * Creates a new traceability data entry on the ledger.
     * The entry is placed on the pool of traceability data's waiting to be validated by peers.
     * @param ctx the transaction context
     * @param newUUID the new UUID of the object to be created. This is generated at client-side in order to avoid different blockchain nodes reaching different ids for the same transaction for a creation of an object.
     * @param traceabilityData An instance of class TraceabilityDataAwaitingValidation containing the traceability data to be inserted in the blockchain.
     */
    public TraceabilityDataAwaitingValidationReturnType createTraceabilityDataEntryByObject(final BlockchainContext ctx, final String newUUID, final TraceabilityDataAwaitingValidation traceabilityData) throws ChaincodeException
    {
        ProcessingDetails processingDetails = traceabilityData.getProcessingDetails();
        return createTraceabilityDataEntry(ctx, newUUID, traceabilityData.getInputDatasetHashValue(), traceabilityData.getOutputDatasetHashValue(),
                processingDetails.getSoftwareId(), processingDetails.getSoftwareVersion(), processingDetails.getSoftwareBinaryExecutableHashValue(),
                processingDetails.getSoftwareConfigParams(), traceabilityData.getValid(), traceabilityData.getValue(), traceabilityData.getWorkUnitsNecessary(),traceabilityData.getCreationTime());
    }

    /**
     * Creates a new traceability data entry on the ledger.
     * The entry is placed on the pool of traceability data's waiting to be validated by peers.
     *
     * @param ctx the transaction context
     * @param newUUID the new UUID of the object to be created. This is generated at client-side in order to avoid different blockchain nodes reaching different ids for the same transaction for a creation of an object.
     * @param inputDatasetHashValue the hash value of the input dataset used to perform the data transformation.
     * @param outputDatasetHashValue the hash value of the output dataset used to perform the data transformation.
     * @param softwareId an unique identifier of the software used to perform the data transformation.
     * @param softwareVersion the version of the software used to perform the data transformation.
     * @param softwareBinaryExecutableHashValue the hash value of the binary executable used to perform the data transformation.
     * @param softwareConfigParams the configuration parameters of the software used to perform the data transformation.
     * @return the traceability entry and the UUID used to reference the traceability information.
     */
    public TraceabilityDataAwaitingValidationReturnType createTraceabilityDataEntry(final BlockchainContext ctx, final String newUUID, final String inputDatasetHashValue,
                                                                                    final String outputDatasetHashValue, final String softwareId,
                                                                                    final String softwareVersion, final String softwareBinaryExecutableHashValue,
                                                                                    final String softwareConfigParams, Boolean valid,
                                                                                    final Double additionalValue, final Double workUnitsNecessary,
                                                                                    final Long creationTime) throws ChaincodeException
    {
        logDebugMsg("createTraceabilityDataEntry");
        long actualCreationTime = System.nanoTime();

        //System.err.println("************** entity that is creating the entry id: |" + ctx.getClientIdentity().getId() + "|");

        ChaincodeStub stub = ctx.getStub();

        TraceabilityDataAwaitingValidation traceabilityData = new TraceabilityDataAwaitingValidation(inputDatasetHashValue, outputDatasetHashValue,
                new ProcessingDetails(softwareId, softwareVersion, softwareBinaryExecutableHashValue, softwareConfigParams), new EntityID(ctx.getClientIdentity().getId()), ChaincodeConfigs.baseValueOfTraceabilityDataEntry + additionalValue, valid, 1.0, actualCreationTime);

        TraceabilityDataInfo dataInfo = new TraceabilityDataInfo(newUUID, traceabilityData);

        HyperledgerFabricBlockhainRepositoryAPI api = new TraceabilityDataAwaitingValidationRepositoryAPI(ctx);

        try
        {
            TraceabilityDataStateMachine stateMachine = new TraceabilityDataStateMachine(dataInfo, api);
            stateMachine.initVotingRound(new EntityID(ctx.getClientIdentity().getId()));
        } catch (UnsupportedTypeOfTraceabilityInfo unsupportedTypeOfTraceabilityInfo)
        {
            throw new ChaincodeException(unsupportedTypeOfTraceabilityInfo.getMessage());
        } catch (ReferenceToNonexistentEntity referenceToNonexistentEntity)
        {
            throw new ChaincodeException(referenceToNonexistentEntity.getMessage());
        } catch (EntityDoesNotHaveEnoughReputationToCreateTraceabilityDataEntry entityDoesNotHaveEnoughReputationToCreateTraceabilityDataEntry)
        {
            throw new ChaincodeException(entityDoesNotHaveEnoughReputationToCreateTraceabilityDataEntry.getMessage());
        } catch (GivenIdIsAlreadyAssignedToAnotherObject givenIdIsAlreadyAssignedToAnotherObject)
        {
            throw new ChaincodeException(givenIdIsAlreadyAssignedToAnotherObject.getMessage());
        }

        TraceabilityDataAwaitingValidationReturnType traceabilityDataInfo = new TraceabilityDataAwaitingValidationReturnType(newUUID, traceabilityData);

        logDebugMsg("createTraceabilityDataEntry END");

        return traceabilityDataInfo;
    }

    /**
     * Allows a node to vote yes for the veracity of a traceability entry on the ledger.
     *
     * @param ctx the transaction context
     * @param uuid the UUID of the traceability data entry to vote yes for.
     * @return a string identifying the success of the operation.
     */

    public String registerYesVoteForTraceabilityEntryInVotingRound(final BlockchainContext ctx, final String uuid) throws ChaincodeException
    {

        logDebugMsg("registerYesVoteForTraceabilityEntryInVotingRound");

        TraceabilityDataStateMachine traceabilityDataStateMachine = getTraceabilityDataFromDBAndBuildVotingStateMachine(ctx, uuid);
        VotingStateMachineReturn votingStateMachineReturn;
        try
        {
            votingStateMachineReturn = traceabilityDataStateMachine.voteYesForTheVeracityOfTraceabilityInfo(getEntityIdFromBlockchainContext(ctx));
        } catch (IncosistentInfoFoundOnDB incosistentInfoFoundOnDB)
        {
            throw new ChaincodeException(incosistentInfoFoundOnDB.getMessage());
        } catch (EntityDoesNotHaveEnoughReputationToPlaceVote entityDoesNotHaveEnoughReputationToPlaceVote)
        {
            throw new ChaincodeException(entityDoesNotHaveEnoughReputationToPlaceVote.getMessage());
        } catch (ReferenceToNonexistentEntity referenceToNonexistentEntity)
        {
            throw new ChaincodeException(referenceToNonexistentEntity.getMessage());
        }

        logDebugMsg("registerYesVoteForTraceabilityEntryInVotingRound END");

        return votingStateMachineReturn.getMessage();
    }

    /**
     * Allows a node to vote no for the veracity of a traceability entry on the ledger.
     *
     * @param ctx the transaction context
     * @param uuid the UUID of the traceability data entry to vote no for.
     * @return a string identifying the success of the operation.
     */

    public String registerNoVoteForTraceabilityEntryInVotingRound(final BlockchainContext ctx, final String uuid) throws ChaincodeException
    {
        logDebugMsg("registerNoVoteForTraceabilityEntryInVotingRound");

        TraceabilityDataStateMachine traceabilityDataStateMachine = getTraceabilityDataFromDBAndBuildVotingStateMachine(ctx, uuid);
        VotingStateMachineReturn votingStateMachineReturn;
        try
        {
            votingStateMachineReturn = traceabilityDataStateMachine.voteNoForTheVeracityOfTraceabilityInfo(getEntityIdFromBlockchainContext(ctx));
        } catch (IncosistentInfoFoundOnDB incosistentInfoFoundOnDB)
        {
            throw new ChaincodeException(incosistentInfoFoundOnDB.getMessage());
        } catch (EntityDoesNotHaveEnoughReputationToPlaceVote entityDoesNotHaveEnoughReputationToPlaceVote)
        {
            throw new ChaincodeException(entityDoesNotHaveEnoughReputationToPlaceVote.getMessage());
        } catch (ReferenceToNonexistentEntity referenceToNonexistentEntity)
        {
            throw new ChaincodeException(referenceToNonexistentEntity.getMessage());
        }


        logDebugMsg("registerNoVoteForTraceabilityEntryInVotingRound END");

        return votingStateMachineReturn.getMessage();
    }

    /**
     * Auxiliary method for the voting methods.
     * Validates the info on the database and builds the state machine that implements the voting rounds logic.
     * @param ctx the transaction context
     * @param uuid the UUID of the traceability data entry to support voting for.
     * @return a string identifying the success of the operation.
     */
    private TraceabilityDataStateMachine getTraceabilityDataFromDBAndBuildVotingStateMachine(BlockchainContext ctx, String uuid) throws ChaincodeException
    {
        logDebugMsg("getTraceabilityDataFromDBAndBuildVotingStateMachine");

        ChaincodeStub stub = ctx.getStub();

        TraceabilityData traceabilityData;

        TraceabilityDataAwaitingValidationRepositoryAPI api = new TraceabilityDataAwaitingValidationRepositoryAPI(ctx);
        try
        {
            traceabilityData = (TraceabilityData) api.read(uuid);
        } catch (ObjectWithGivenKeyNotFoundOnBlockchainDB objectWithGivenKeyNotFoundOnBlockchainDB)
        {
            throw new ChaincodeException(objectWithGivenKeyNotFoundOnBlockchainDB.getMessage());
        }

        String voterID = ctx.getClientIdentity().getId();
        String creatorID = traceabilityData.getCreatorID().getId();
        if (voterID.equals(creatorID))
            throw new ChaincodeException("Creator of traceability data cannot vote for it.");

        TraceabilityDataInfo traceabilityDataInfo = new TraceabilityDataInfo(uuid, traceabilityData);
        TraceabilityDataStateMachine traceabilityDataStateMachine;
        try
        {
            traceabilityDataStateMachine = new TraceabilityDataStateMachine(traceabilityDataInfo, api);
        } catch (UnsupportedTypeOfTraceabilityInfo unsupportedTypeOfTraceabilityInfo)
        {
            throw new ChaincodeException("Voting on information with this type is not supported");
        }
        logDebugMsg("getTraceabilityDataFromDBAndBuildVotingStateMachine END");

        return traceabilityDataStateMachine;
    }

    /**
     * Retrieves all traceability data that is in state awaiting validation.
     *
     * @param ctx the transaction context
     * @return array of traceability data that is in state awaiting validation.
     */

    public EntityDataReturnType[] getMyReputation(final BlockchainContext ctx) {
        ChaincodeStub stub = ctx.getStub();


        HyperledgerFabricBlockhainRepositoryAPI api = new EntityDataRepositoryAPI(ctx);
        ArrayList<iReceptorChainDataTypeInfo> results = api.getAllEntries();

        ArrayList<EntityDataReturnType> resultsToReturn = new ArrayList<>();

        for (iReceptorChainDataTypeInfo result: results)
        {
            EntityDataInfo entityDataInfo = (EntityDataInfo) result;
            EntityDataReturnType dataReturnType = new EntityDataReturnType(entityDataInfo.getUUID(), (EntityData) entityDataInfo.getData());
            resultsToReturn.add(dataReturnType);
        }
        EntityDataReturnType[] response = resultsToReturn.toArray(new EntityDataReturnType[resultsToReturn.size()]);

        return response;
    }

    /**
     * Retrieves all traceability data that is in state awaiting validation.
     *
     * @param ctx the transaction context
     * @return array of traceability data that is in state awaiting validation.
     */

    public EntityDataReturnType[] getAllEntities(final BlockchainContext ctx) {
        ChaincodeStub stub = ctx.getStub();

        HyperledgerFabricBlockhainRepositoryAPI api = new EntityDataRepositoryAPI(ctx);
        ArrayList<iReceptorChainDataTypeInfo> results = api.getAllEntries();

        ArrayList<EntityDataReturnType> resultsToReturn = new ArrayList<>();

        for (iReceptorChainDataTypeInfo result: results)
        {
            EntityDataInfo entityDataInfo = (EntityDataInfo) result;
            EntityDataReturnType dataReturnType = new EntityDataReturnType(entityDataInfo.getUUID(), (EntityData) entityDataInfo.getData());
            resultsToReturn.add(dataReturnType);
        }
        EntityDataReturnType[] response = resultsToReturn.toArray(new EntityDataReturnType[resultsToReturn.size()]);

        return response;
    }
    
    /**
     * Retrieves all traceability data that is in state awaiting validation.
     *
     * @param ctx the transaction context
     * @return array of traceability data that is in state awaiting validation.
     */

    public TraceabilityDataAwaitingValidationReturnType[] getAllAwaitingValidationTraceabilityDataEntries(final BlockchainContext ctx) {
        logDebugMsg("getAllAwaitingValidationTraceabilityDataEntries");
        ChaincodeStub stub = ctx.getStub();

        HyperledgerFabricBlockhainRepositoryAPI api = new TraceabilityDataAwaitingValidationRepositoryAPI(ctx);
        ArrayList<iReceptorChainDataTypeInfo> results = api.getAllEntries();

        ArrayList<TraceabilityDataAwaitingValidationReturnType> resultsToReturn = new ArrayList<>();

        for (iReceptorChainDataTypeInfo result: results)
        {
            TraceabilityDataInfo traceabilityDataInfo = (TraceabilityDataInfo) result;
            logDebugMsg("uuid: " + traceabilityDataInfo.getUUID());
            TraceabilityDataAwaitingValidationReturnType dataReturnType = new TraceabilityDataAwaitingValidationReturnType(traceabilityDataInfo.getUUID(), (TraceabilityDataAwaitingValidation) traceabilityDataInfo.getTraceabilityData());
            resultsToReturn.add(dataReturnType);
        }

        TraceabilityDataAwaitingValidationReturnType[] response = resultsToReturn.toArray(new TraceabilityDataAwaitingValidationReturnType[resultsToReturn.size()]);


        logDebugMsg("getAllAwaitingValidationTraceabilityDataEntries END");

        return response;
    }

    /**
     * Retrieves all traceability data that is in state validated.
     *
     * @param ctx the transaction context
     * @return array of traceability data that is in state awaiting validation.
     */

    public TraceabilityDataValidatedReturnType[] getAllValidatedTraceabilityDataEntries(final BlockchainContext ctx) {
        logDebugMsg("getAllValidatedTraceabilityDataEntries");

        ChaincodeStub stub = ctx.getStub();

        HyperledgerFabricBlockhainRepositoryAPI api = new TraceabilityDataValidatedRepositoryAPI(ctx);
        ArrayList<iReceptorChainDataTypeInfo> results = api.getAllEntries();

        ArrayList<TraceabilityDataReturnType> resultsToReturn = new ArrayList<>();

        for (iReceptorChainDataTypeInfo result: results)
        {
            TraceabilityDataInfo traceabilityDataInfo = (TraceabilityDataInfo) result;
            TraceabilityDataValidatedReturnType dataReturnType = new TraceabilityDataValidatedReturnType(traceabilityDataInfo.getUUID(), (TraceabilityDataValidated) traceabilityDataInfo.getTraceabilityData());
            resultsToReturn.add(dataReturnType);
    }

        TraceabilityDataValidatedReturnType[] response = resultsToReturn.toArray(new TraceabilityDataValidatedReturnType[resultsToReturn.size()]);
        logDebugMsg("getAllValidatedTraceabilityDataEntries END");

        return response;
    }

    private void logDebugMsg(String msg)
    {
        //System.err.println("************************** " + msg + " **************************");
    }
}
