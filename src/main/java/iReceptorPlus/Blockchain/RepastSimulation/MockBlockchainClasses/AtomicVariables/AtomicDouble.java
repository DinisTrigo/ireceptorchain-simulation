package iReceptorPlus.Blockchain.RepastSimulation.MockBlockchainClasses.AtomicVariables;

public class AtomicDouble
{
    Double value;

    public AtomicDouble(Integer value)
    {
        this.value = (double) value;
    }

    public AtomicDouble(Double value)
    {
        this.value = value;
    }

    public AtomicDouble()
    {
        this.value = 0.0;
    }

    public Double get()
    {
        return value;
    }

    public void set(Double value)
    {
        this.value = value;
    }
}
