package iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPIs;

import iReceptorPlus.Blockchain.ChainDataTypes.iReceptorChainDataType;
import iReceptorPlus.Blockchain.RepastSimulation.MockBlockchainClasses.Database.BlockchainContext;
import iReceptorPlus.Blockchain.iReceptorChain.ChaincodeConfigs;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.iReceptorChainDataTypeInfo;

public class TraceabilityDataAwaitingValidationRepositoryAPI extends TraceabilityDataRepositoryAPI
{

    /**
     * Constructor for this class.
     *
     * @param ctx The blockchain context in which this class will perform the required tasks.
     */
    public TraceabilityDataAwaitingValidationRepositoryAPI(BlockchainContext ctx)
    {
        super(ctx, ChaincodeConfigs.getTraceabilityAwaitingValidationKeyPrefix());
    }

    /**
     * Constructor that creates a new instance of this class from another class of the same type.
     * Uses the same blockchain context but not the same objectTypeIdentifier. Is is used when another repository is meant to be created when another repository is needed and the same context should be used.
     * @param api The instance of HyperledgerFabricBlockhainRepositoryAPI from which to copy the context from.
     */
    public TraceabilityDataAwaitingValidationRepositoryAPI(HyperledgerFabricBlockhainRepositoryAPI api)
    {
        super(api.ctx, ChaincodeConfigs.getTraceabilityAwaitingValidationKeyPrefix());
    }
}
