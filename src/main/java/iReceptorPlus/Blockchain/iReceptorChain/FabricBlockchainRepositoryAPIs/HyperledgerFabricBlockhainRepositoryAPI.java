package iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPIs;

import iReceptorPlus.Blockchain.ChainDataTypes.TraceabilityData;
import iReceptorPlus.Blockchain.RepastSimulation.MockBlockchainClasses.Database.BlockchainContext;
import iReceptorPlus.Blockchain.RepastSimulation.MockBlockchainClasses.Database.CompositeKey;
import iReceptorPlus.Blockchain.ChainDataTypes.iReceptorChainDataType;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPIs.Exceptions.GivenIdIsAlreadyAssignedToAnotherObject;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPIs.Exceptions.ObjectWithGivenKeyNotFoundOnBlockchainDB;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.iReceptorChainDataTypeInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class is an API that handles all calls to the hyperledger functions that are necessary for CRUD operations.
 * It is used to abstract database related calls to the hyperledger API from the application logic.
 * The class receives the blockchain context on which it will perform the operations and can then be called to perform CRUD operations on that same context without the need for interacting directly with hyperledger fabric's methods.
 * The class is abstract and defines the methods for CRUD operations.
 * Subclasses implement the specific logic necessary to handle these operations for each one of the data types but generalization is maintained whenever possible.
 */
public abstract class HyperledgerFabricBlockhainRepositoryAPI
{
    /**
     * The blockchain context in which this class will perform the required tasks.
     */
    protected BlockchainContext ctx;

    /**
     * A String that uniquely identifies the object type that will be stored on the blockchain database by this repository class.
     * It will be used to build the UUID for storing the object on the blockchain.
     */
    String objectTypeIdentifier;

    /**
     * Constructor for this class.
     *
     * @param ctx        The blockchain context in which this class will perform the required tasks.
     * @param objectTypeIdentifier A String that uniquely identifies the object type that will be stored on the blockchain database by this repository class.
     */
    public HyperledgerFabricBlockhainRepositoryAPI(BlockchainContext ctx, String objectTypeIdentifier)
    {
        this.ctx = ctx;
        this.objectTypeIdentifier = objectTypeIdentifier;
    }

    /**
     * Constructor that creates a new instance of this class from another class of the same type.
     * Uses the same blockchain context but not the same objectTypeIdentifier. Is is used when another repository is meant to be created when another repository is needed and the same context should be used.
     * @param api The instance of HyperledgerFabricBlockhainRepositoryAPI from which to copy the context from.
     */
    public HyperledgerFabricBlockhainRepositoryAPI(HyperledgerFabricBlockhainRepositoryAPI api, String objectTypeIdentifier)
    {
        this(api.ctx, objectTypeIdentifier);
    }

    protected String uuidToKey(String uuid)
    {
        CompositeKey compositeKey = new CompositeKey(objectTypeIdentifier, uuid);
        String compositeKeyString = compositeKey.toString();
        return compositeKeyString;
    }

    /**
     * Implements create operations for the data type.
     * @param data An instance of a subclass of iReceptorChainDataType containing the data to be saved on the blockchain.
     * @return The UUID of the newly created entry on the blockchain.
     */
    public String create(String newUUID, iReceptorChainDataType data) throws GivenIdIsAlreadyAssignedToAnotherObject
    {
        try
        {
            iReceptorChainDataType stringState = getDataTypeFromDB(newUUID);
            throw new GivenIdIsAlreadyAssignedToAnotherObject("The id you have provided is not unique: it is already assigned to another object of the same type. Please try with a different id.", newUUID);
        } catch (ObjectWithGivenKeyNotFoundOnBlockchainDB objectWithGivenKeyNotFoundOnBlockchainDB)
        {
            putEntryToDB(newUUID, data);
            return newUUID;
        }
    }

    /**
     * Implements create operations for the data type.
     * @param dataInfo An instance of a subclass of iReceptorChainDataTypeInfo containing the UUID of the data to be added and the data to be saved on the blockchain.
     * @return The UUID of the newly created entry on the blockchain.
     */
    public String create(iReceptorChainDataTypeInfo dataInfo) throws GivenIdIsAlreadyAssignedToAnotherObject
    {
        try
        {
            iReceptorChainDataType stringState = getDataTypeFromDB(dataInfo.getUUID());
            throw new GivenIdIsAlreadyAssignedToAnotherObject("The id you have provided is not unique: it is already assigned to another object of the same type. Please try with a different id.", dataInfo.getUUID());
        } catch (ObjectWithGivenKeyNotFoundOnBlockchainDB objectWithGivenKeyNotFoundOnBlockchainDB)
        {
            putEntryToDB(dataInfo.getUUID(), dataInfo.getData());
            return dataInfo.getUUID();
        }
    }

    /**
     * Auxiliary method used on the create and update operations. This methods puts (create or update) an entry on the blockchain, represented by the parameter data.
     * @param uuid The UUID of the object to be put. In case it is a create operation, it is the new UUID (id) of the entry. In case it is an update operation, it is the id that the entry being updated currently has on the blockchain.
     * @param data An instance of a subclass of iReceptorChainDataType containing the data to be created or updated on the blockchain.
     */
    private void putEntryToDB(String uuid, iReceptorChainDataType data)
    {
        String key = uuidToKey(uuid);
        ctx.getStub().putStringState(key, data);
    }

    private iReceptorChainDataType getDataTypeFromDB(String uuid) throws ObjectWithGivenKeyNotFoundOnBlockchainDB
    {
        String key = uuidToKey(uuid);
        iReceptorChainDataType data = ctx.getStub().getStringState(key);
        if (data == null)
            throw new ObjectWithGivenKeyNotFoundOnBlockchainDB("The object referenced does not exist on the blockchain database", uuid);

        return data;
    }

    public iReceptorChainDataType read(String uuid) throws ObjectWithGivenKeyNotFoundOnBlockchainDB
    {
        iReceptorChainDataType object = getDataTypeFromDB(uuid);

        return object;
    }


    public iReceptorChainDataType update(String uuid, iReceptorChainDataType traceabilityDataInfo) throws ObjectWithGivenKeyNotFoundOnBlockchainDB
    {
        read(uuid); //check if exists
        putEntryToDB(uuid, traceabilityDataInfo);

        return traceabilityDataInfo;
    }

    public iReceptorChainDataTypeInfo update(iReceptorChainDataTypeInfo traceabilityDataInfo) throws ObjectWithGivenKeyNotFoundOnBlockchainDB
    {
        read(traceabilityDataInfo.getUUID()); //check if exists
        putEntryToDB(traceabilityDataInfo.getUUID(), traceabilityDataInfo.getData());

        return traceabilityDataInfo;
    }

    public void remove(iReceptorChainDataTypeInfo traceabilityDataInfo) throws ObjectWithGivenKeyNotFoundOnBlockchainDB
    {
        read(traceabilityDataInfo.getUUID()); //check if exists

        deleteDataFromDB(traceabilityDataInfo.getUUID());
    }

    public void remove(String uuid) throws ObjectWithGivenKeyNotFoundOnBlockchainDB
    {
        read(uuid); //check if exists

        deleteDataFromDB(uuid);
    }

    public void deleteDataFromDB(String uuid)
    {
        String key = uuidToKey(uuid);
        ctx.getStub().delState(key);
    }

    public ArrayList<iReceptorChainDataTypeInfo> getAllEntries()
    {
        HashMap<String, iReceptorChainDataType> database = ctx.getStub().getEntriesByPartialCompositeKey(objectTypeIdentifier);
        ArrayList<iReceptorChainDataTypeInfo> results = new ArrayList<>();
        Iterator it = database.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry pair = (Map.Entry)it.next();
            String key = (String) pair.getKey();
            String uuid = key.substring((objectTypeIdentifier + "-").length());
            results.add(createiReceptorChainDataTypeInfo(uuid, (iReceptorChainDataType) pair.getValue()));
        }

        return results;
    }

    protected abstract iReceptorChainDataTypeInfo createiReceptorChainDataTypeInfo(String uuid, iReceptorChainDataType data);

}
