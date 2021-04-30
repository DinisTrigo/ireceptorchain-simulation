package iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPIs;

import iReceptorPlus.Blockchain.ChainDataTypes.iReceptorChainDataType;
import iReceptorPlus.Blockchain.RepastSimulation.MockBlockchainClasses.Database.BlockchainContext;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.TraceabilityDataInfo;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.iReceptorChainDataTypeInfo;

/**
 * This is a superclass of the HyperledgerFabricBlockhainRepositoryAPI
 */
public abstract class TraceabilityDataRepositoryAPI extends HyperledgerFabricBlockhainRepositoryAPI
{
    /**
     * Constructor for this class.
     *
     * @param ctx The blockchain context in which this class will perform the required tasks.
     * @param objectTypeIdentifier A String that uniquely identifies the object type that will be stored on the blockchain database by this repository class.
     */
    public TraceabilityDataRepositoryAPI(BlockchainContext ctx, String objectTypeIdentifier)
    {
        super(ctx, objectTypeIdentifier);
    }

    @Override
    protected iReceptorChainDataTypeInfo createiReceptorChainDataTypeInfo(String uuid, iReceptorChainDataType data)
    {
        return new TraceabilityDataInfo(uuid, data);
    }
}
