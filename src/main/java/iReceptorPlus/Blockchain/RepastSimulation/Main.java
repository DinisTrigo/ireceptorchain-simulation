package iReceptorPlus.Blockchain.RepastSimulation;

import org.apache.commons.cli.*;

import java.io.IOException;

public class Main
{
    private static final boolean BATCH_MODE = true;

    public static void main(String[] args)
    {
        Options options = new Options();

        {
            Option initialIncidenceOfEthicalAgents = new Option("ii",
                    "initialIncidenceOfIntendedAgents", true,
                    "The initial incidence of intended-behaving agents");
            initialIncidenceOfEthicalAgents.setRequired(true);
            options.addOption(initialIncidenceOfEthicalAgents);
        }
        {
            Option initialIncidenceOfUnEthicalAgents = new Option("iu",
                    "initialIncidenceOfUnintendedAgents", true,
                    "The initial incidence of unintended-behaving agents");
            initialIncidenceOfUnEthicalAgents.setRequired(true);
            options.addOption(initialIncidenceOfUnEthicalAgents);
        }
        {
            Option initialIncidenceOfCreatorAgents = new Option("ic",
                    "initialIncidenceOfCreatorAgents", true,
                    "The initial incidence of creator agents");
            initialIncidenceOfCreatorAgents.setRequired(true);
            options.addOption(initialIncidenceOfCreatorAgents);
        }
        {
            Option initialIncidenceOfVotingAgents = new Option("iv",
                    "initialIncidenceOfVotingAgents", true,
                    "The initial incidence of voting agents");
            initialIncidenceOfVotingAgents.setRequired(true);
            options.addOption(initialIncidenceOfVotingAgents);
        }


        {
            Option finalIncidenceOfUnEthicalAgents = new Option("fu",
                    "finalIncidenceOfUnintendedAgents", true,
                    "The final incidence of unintended-behaving agents");
            finalIncidenceOfUnEthicalAgents.setRequired(true);
            options.addOption(finalIncidenceOfUnEthicalAgents);
        }
        {
            Option finalIncidenceOfCreatorAgents = new Option("fc",
                    "finalIncidenceOfCreatorAgents", true,
                    "The final incidence of creator agents");
            finalIncidenceOfCreatorAgents.setRequired(true);
            options.addOption(finalIncidenceOfCreatorAgents);
        }


        {
            Option unintendedAgentsIncrement = new Option("uinc",
                    "unintendedAgentsIncrement", true,
                    "The increment made to the incidence of unintended-behaving agents on each simulation trial");
            unintendedAgentsIncrement.setRequired(true);
            options.addOption(unintendedAgentsIncrement);
        }
        {
            Option creatorAgentsIncrement = new Option("iinc",
                    "creatorAgentsIncrement", true,
                    "The increment made to the incidence of creator agents on each simulation trial");
            creatorAgentsIncrement.setRequired(true);
            options.addOption(creatorAgentsIncrement);
        }

        {
            Option numIterations = new Option("i",
                    "numIterations", true,
                    "The number of iterations of each simulation trial.");
            numIterations.setRequired(true);
            options.addOption(numIterations);
        }

        HelpFormatter formatter = new HelpFormatter();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("iReceptorChain simulation configuration", options);
            System.exit(1);
            return;
        }

        boolean runMode = !BATCH_MODE; // BATCH_MODE or !BATCH_MODE
// create a simulation
        //SimInit init = new SimInit();


            int initialIncidenceOfEthicalAgents = Integer.parseInt(cmd.getOptionValue("initialIncidenceOfIntendedAgents"));
            int initialIncidenceOfUnEthicalAgents = Integer.parseInt(cmd.getOptionValue("initialIncidenceOfUnintendedAgents"));
            int initialIncidenceOfCreatorAgents = Integer.parseInt(cmd.getOptionValue("initialIncidenceOfCreatorAgents"));
            int initialIncidenceOfVotingAgents = Integer.parseInt(cmd.getOptionValue("initialIncidenceOfVotingAgents"));

            int finalIncidenceOfUnEthicalAgents = Integer.parseInt(cmd.getOptionValue("finalIncidenceOfUnintendedAgents"));
            int finalIncidenceOfCreatorAgents = Integer.parseInt(cmd.getOptionValue("finalIncidenceOfCreatorAgents"));

            int unintendedAgentsIncrement = Integer.parseInt(cmd.getOptionValue("unintendedAgentsIncrement"));
            int creatorAgentsIncrement = Integer.parseInt(cmd.getOptionValue("creatorAgentsIncrement"));

            int numIterations = Integer.parseInt(cmd.getOptionValue("numIterations"));

        SimulationController controller = new SimulationController(
                initialIncidenceOfEthicalAgents, initialIncidenceOfUnEthicalAgents, finalIncidenceOfUnEthicalAgents,
                initialIncidenceOfCreatorAgents, initialIncidenceOfVotingAgents, finalIncidenceOfCreatorAgents,
                unintendedAgentsIncrement, creatorAgentsIncrement, numIterations
        );
        controller.runSimulationsTrials();
        try
        {
            controller.saveResultsToFile("results.csv");
        } catch (IOException e)
        {
            e.printStackTrace();
        }


// load model into simulation:
        //init.loadModel(model, null, runMode);
    }
}
