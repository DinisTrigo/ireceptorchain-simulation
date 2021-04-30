package iReceptorPlus.Blockchain.ChaincodeReturnDataTypes;

import iReceptorPlus.Blockchain.ChainDataTypes.EntityData;

public class EntityDataReturnType extends ChaincodeReturnDataType
{
    /**
     * The uuid used to reference the traceability data.
     */

    protected String uuid;
    EntityData entityData;

    public EntityDataReturnType( final String uuid,
                                 final EntityData entityData)
    {
        this.uuid = uuid;
        this.entityData = entityData;
    }

    public EntityData getEntityData()
    {
        return entityData;
    }

    public String getUuid()
    {
        return uuid;
    }
}
