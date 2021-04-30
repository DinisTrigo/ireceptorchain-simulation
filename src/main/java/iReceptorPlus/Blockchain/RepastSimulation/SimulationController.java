package iReceptorPlus.Blockchain.RepastSimulation;

import iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.APIs.BlockchainAPI;
import iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.Agents.BehaviorIncidence;
import iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.LoggingAndSettings.Logging;
import iReceptorPlus.Blockchain.RepastSimulation.Builders.AgentsDescription;
import iReceptorPlus.Blockchain.RepastSimulation.Builders.ModelBuilder;
import iReceptorPlus.Blockchain.RepastSimulation.Builders.NormalDistributionSamplingDescription;
import iReceptorPlus.Blockchain.RepastSimulation.PerformanceMetrics.EntireSimulationPerformanceMetrics;
import iReceptorPlus.Blockchain.iReceptorChain.FabricBlockchainRepositoryAPIs.TraceabilityDataAwaitingValidationRepositoryAPI;

import java.io.*;
import java.util.ArrayList;

public class SimulationController
{
    private static final String fileSeparator = ";";

    public class TrialResults
    {
        int incidenceOfEthicalAgents;
        int incidenceOfUnEthicalAgents;
        int incidenceOfCreatorAgents;
        int incidenceOfVotingAgents;
        Integer numberOfValidatedEntries;
        Integer numberOfInvalidatedEntries;
        Integer incorrectlyClassifiedEntries;
        Integer numberOfEntriesAwaitingValidation;

        public TrialResults(int incidenceOfEthicalAgents, int incidenceOfUnEthicalAgents, int incidenceOfCreatorAgents, int incidenceOfVotingAgents, Integer numberOfValidatedEntries, Integer numberOfInvalidatedEntries, Integer incorrectlyClassifiedEntries, Integer numberOfEntriesAwaitingValidation)
        {
            this.incidenceOfEthicalAgents = incidenceOfEthicalAgents;
            this.incidenceOfUnEthicalAgents = incidenceOfUnEthicalAgents;
            this.incidenceOfCreatorAgents = incidenceOfCreatorAgents;
            this.incidenceOfVotingAgents = incidenceOfVotingAgents;
            this.numberOfValidatedEntries = numberOfValidatedEntries;
            this.numberOfInvalidatedEntries = numberOfInvalidatedEntries;
            this.incorrectlyClassifiedEntries = incorrectlyClassifiedEntries;
            this.numberOfEntriesAwaitingValidation = numberOfEntriesAwaitingValidation;
        }

        public int getIncidenceOfEthicalAgents()
        {
            return incidenceOfEthicalAgents;
        }

        public int getIncidenceOfUnEthicalAgents()
        {
            return incidenceOfUnEthicalAgents;
        }

        public int getIncidenceOfCreatorAgents()
        {
            return incidenceOfCreatorAgents;
        }

        public int getIncidenceOfVotingAgents()
        {
            return incidenceOfVotingAgents;
        }

        public Integer getNumberOfValidatedEntries()
        {
            return numberOfValidatedEntries;
        }

        public Integer getNumberOfInvalidatedEntries()
        {
            return numberOfInvalidatedEntries;
        }

        public Integer getIncorrectlyClassifiedEntries()
        {
            return incorrectlyClassifiedEntries;
        }

        public Integer getNumberOfEntriesAwaitingValidation()
        {
            return numberOfEntriesAwaitingValidation;
        }
    }

    private int initialIncidenceOfEthicalAgents;
    private int initialIncidenceOfUnEthicalAgents;
    private int finalIncidenceOfUnEthicalAgents;

    private int initialIncidenceOfCreatorAgents;
    private int initialIncidenceOfVoterAgents;
    private int finalIncidenceOfCreatorAgents;

    private int eachTrialStepUnEthicalAgents;
    private int eachTrialStepCreatorAgents;

    private int numIterationsOfEachTrial;

    ArrayList<TrialResults> results;

    public SimulationController(int initialIncidenceOfEthicalAgents, int initialIncidenceOfUnEthicalAgents, int finalIncidenceOfUnEthicalAgents, int initialIncidenceOfCreatorAgents, int initialIncidenceOfVoterAgents, int finalIncidenceOfCreatorAgents, int eachTrialStepUnEthicalAgents, int eachTrialStepCreatorAgents, int numIterationsOfEachTrial)
    {
        this.initialIncidenceOfEthicalAgents = initialIncidenceOfEthicalAgents;
        this.initialIncidenceOfUnEthicalAgents = initialIncidenceOfUnEthicalAgents;
        this.finalIncidenceOfUnEthicalAgents = finalIncidenceOfUnEthicalAgents;
        this.initialIncidenceOfCreatorAgents = initialIncidenceOfCreatorAgents;
        this.initialIncidenceOfVoterAgents = initialIncidenceOfVoterAgents;
        this.finalIncidenceOfCreatorAgents = finalIncidenceOfCreatorAgents;
        this.eachTrialStepUnEthicalAgents = eachTrialStepUnEthicalAgents;
        this.eachTrialStepCreatorAgents = eachTrialStepCreatorAgents;
        this.numIterationsOfEachTrial = numIterationsOfEachTrial;
        results = new ArrayList<>();
    }

    public void runSimulationsTrials()
    {



        NormalDistributionSamplingDescription workRateCapacityDistribution = new NormalDistributionSamplingDescription(20.0, 10.0);
        NormalDistributionSamplingDescription riskDistribution = new NormalDistributionSamplingDescription(0.1, 0.02);

        int trial = 1;
    for (int incidenceOfCreatorAgents = initialIncidenceOfCreatorAgents;
    incidenceOfCreatorAgents <= finalIncidenceOfCreatorAgents;
    incidenceOfCreatorAgents += eachTrialStepCreatorAgents)
            for (int incidenceOfUnEthicalAgents = initialIncidenceOfUnEthicalAgents;
                 incidenceOfUnEthicalAgents <= finalIncidenceOfUnEthicalAgents;
                 incidenceOfUnEthicalAgents += eachTrialStepUnEthicalAgents)
            {
                Logging.Log("Initiating trial " + trial);

                runTrial(initialIncidenceOfEthicalAgents, incidenceOfUnEthicalAgents,
                        incidenceOfCreatorAgents, initialIncidenceOfVoterAgents,
                        workRateCapacityDistribution, riskDistribution);

                trial++;
            }



    }

    public void saveResultsToFile(String filename) throws IOException
    {
        File fout = new File(filename);
        FileOutputStream fos = new FileOutputStream(fout);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        String firstLine = "incidenceOfEthicalAgents" + fileSeparator +"incidenceOfUnEthicalAgents" + fileSeparator +
                "incidenceOfCreatorAgents" + fileSeparator +"incidenceOfVotingAgents" + fileSeparator +
                "numberOfValidatedEntries" + fileSeparator +"numberOfInvalidatedEntries" + fileSeparator +
                "incorrectlyClassifiedEntries" + fileSeparator +"numberOfEntriesAwaitingValidation";

        bw.write(firstLine);
        bw.newLine();

        for (TrialResults trialResults : results)
        {
            String currLine = trialResults.getIncidenceOfEthicalAgents() + fileSeparator +
                    trialResults.getIncidenceOfUnEthicalAgents() + fileSeparator +
                    trialResults.getIncidenceOfCreatorAgents() + fileSeparator +
                    trialResults.getIncidenceOfVotingAgents() + fileSeparator +
                    trialResults.getNumberOfValidatedEntries() + fileSeparator +
                    trialResults.getNumberOfInvalidatedEntries() + fileSeparator +
                    trialResults.getIncorrectlyClassifiedEntries() + fileSeparator +
                    trialResults.getNumberOfEntriesAwaitingValidation();

            bw.write(currLine);
            bw.newLine();
        }

        bw.close();
    }

    private void runTrial(int incidenceOfEthicalAgents, int incidenceOfUnEthicalAgents,
                          int creatorAgentsIncidence, int voterAgentsIncidence,
                          NormalDistributionSamplingDescription workRateCapacityDistribution,
                          NormalDistributionSamplingDescription riskDistribution)
    {
        BehaviorIncidence ethicalAgentsBehavior = new BehaviorIncidence(Double.valueOf(creatorAgentsIncidence), 0.1, Double.valueOf(voterAgentsIncidence),
                new BehaviorIncidence.EthicnessBehaviorIncidence(100.0, 0.0, null),
                new BehaviorIncidence.EthicnessBehaviorIncidence(100.0, 0.0, null));

        BehaviorIncidence unEthicalAgentsBehavior = new BehaviorIncidence(Double.valueOf(creatorAgentsIncidence), 0.1, Double.valueOf(voterAgentsIncidence),
                new BehaviorIncidence.EthicnessBehaviorIncidence(10.0, 90.0, null),
                new BehaviorIncidence.EthicnessBehaviorIncidence(10.0, 90.0,
                        new BehaviorIncidence.EthicnessBehaviorIncidence.UnEthicalVotingBehaviorIncidence(10.0, 90.0,
                                new BehaviorIncidence.EthicnessBehaviorIncidence.UnEthicalVotingBehaviorIncidence.MajorityVotingBehaviorIncidence(10.0, 80.0, 80.0, 20.0))));

        AgentsDescription agentsDescription = new AgentsDescription(new Long(100), Double.valueOf(incidenceOfEthicalAgents), Double.valueOf(incidenceOfUnEthicalAgents),
                ethicalAgentsBehavior, unEthicalAgentsBehavior,
                100.0, workRateCapacityDistribution, riskDistribution);

        ModelBuilder builder = new ModelBuilder(agentsDescription);
        IReceptorChainModel model = builder.build();
        BlockchainAPI.initBlockchainContext(model.getContext());
        EntireSimulationPerformanceMetrics.reset();

        SimulationRunner runner = new SimulationRunner(model, numIterationsOfEachTrial);
        runner.runSimulation();

        Integer numberOfValidatedEntries = EntireSimulationPerformanceMetrics.getNumberOfValidatedEntries();
        Integer numberOfInvalidatedEntries = EntireSimulationPerformanceMetrics.getNumberOfInvalidatedEntries();
        Integer incorrectlyClassifiedEntries = EntireSimulationPerformanceMetrics.getIncorrectlyClassifiedEntries();

        TraceabilityDataAwaitingValidationRepositoryAPI api = new TraceabilityDataAwaitingValidationRepositoryAPI(model.getContext());
        Integer numberOfEntriesAwaitingValidation = api.getAllEntries().size();

        TrialResults metrics = new TrialResults(incidenceOfEthicalAgents, incidenceOfUnEthicalAgents,
                creatorAgentsIncidence, voterAgentsIncidence,
                numberOfValidatedEntries, numberOfInvalidatedEntries,
                incorrectlyClassifiedEntries, numberOfEntriesAwaitingValidation);

        results.add(metrics);
    }
}
