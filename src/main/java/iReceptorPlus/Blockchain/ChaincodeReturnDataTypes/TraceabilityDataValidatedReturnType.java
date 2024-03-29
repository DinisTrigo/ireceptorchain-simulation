package iReceptorPlus.Blockchain.ChaincodeReturnDataTypes;

import iReceptorPlus.Blockchain.ChainDataTypes.TraceabilityDataValidated;


public class TraceabilityDataValidatedReturnType extends TraceabilityDataReturnType
{
    /**
     * The uuid used to reference the traceability data.
     */

    protected String uuid;
    /**
     * The traceability data just as it is stored on the blockchain.
     */

    TraceabilityDataValidated traceabilityDataValidatedData;

    public String getUuid()
    {
        return uuid;
    }

    public TraceabilityDataValidated getTraceabilityDataValidatedData()
    {
        return traceabilityDataValidatedData;
    }

    public TraceabilityDataValidatedReturnType( final String uuid,
                                                final TraceabilityDataValidated traceabilityDataValidatedData)
    {
        this.uuid = uuid;
        this.traceabilityDataValidatedData = traceabilityDataValidatedData;
    }
}
