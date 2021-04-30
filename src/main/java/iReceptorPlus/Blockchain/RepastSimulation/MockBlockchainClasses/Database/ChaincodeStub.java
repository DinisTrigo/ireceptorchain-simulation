package iReceptorPlus.Blockchain.RepastSimulation.MockBlockchainClasses.Database;



import iReceptorPlus.Blockchain.ChainDataTypes.iReceptorChainDataType;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChaincodeStub
{
    HashMap<String, iReceptorChainDataType> database;

    public ChaincodeStub()
    {
        database = new HashMap<>();
    }

    public void putStringState(String key, iReceptorChainDataType value)
    {
        database.put(key, value);
    }

    public iReceptorChainDataType getStringState(String key)
    {
        return database.get(key);
    }

    public void delState(String key)
    {
        database.remove(key);
    }

    public HashMap<String, iReceptorChainDataType> getEntriesByPartialCompositeKey(String partialKey)
    {
        HashMap<String, iReceptorChainDataType> results = new HashMap<>();
        Iterator it = database.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry pair = (Map.Entry)it.next();
            String key = (String) pair.getKey();

            if (key.contains(partialKey))
                results.put(key, (iReceptorChainDataType) pair.getValue());
        }

        return results;
    }
}
