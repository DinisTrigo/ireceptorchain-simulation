package iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPIs;

import iReceptorPlus.Blockchain.ChainDataTypes.iReceptorChainDataType;
import iReceptorPlus.Blockchain.RepastSimulation.MockBlockchainClasses.Database.BlockchainContext;
import iReceptorPlus.Blockchain.iReceptorChain.ChaincodeConfigs;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.EntityDataInfo;
import iReceptorPlus.Blockchain.iReceptorChain.LogicDataTypes.iReceptorChainDataTypeInfo;

public class EntityDataRepositoryAPI extends HyperledgerFabricBlockhainRepositoryAPI
{
    public EntityDataRepositoryAPI(BlockchainContext ctx)
    {
        super(ctx, ChaincodeConfigs.getEntityDataKeyPrefix());
    }

    public EntityDataRepositoryAPI(HyperledgerFabricBlockhainRepositoryAPI api)
    {
        super(api, ChaincodeConfigs.getEntityDataKeyPrefix());
    }

    @Override
    protected iReceptorChainDataTypeInfo createiReceptorChainDataTypeInfo(String uuid, iReceptorChainDataType data)
    {
        return new EntityDataInfo(uuid, data);
    }
}
