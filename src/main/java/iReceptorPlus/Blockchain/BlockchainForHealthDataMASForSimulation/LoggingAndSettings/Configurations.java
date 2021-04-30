package iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.LoggingAndSettings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Configurations
{
    public static boolean LOG = true;
    public static boolean DEBUG = true;
    private String configFilePath = "config.ini";

    /**
     * Writhes the current configuration values to the config file
     */
    private void createConfigFile()
    {
        //create the configuration file
        FileOutputStream file = null;
        try
        {
            file = new FileOutputStream(this.configFilePath);
        } catch (FileNotFoundException e)
        {
            Logging.FatalErrorLog("Failed To Create Config File Exiting");
            System.exit(-1);
        }
        String configVal = "";

        //write the predefined values of the various system configurations
        configVal += "LOG=" + LOG + "\n";
        configVal += "DEBUG=" + DEBUG + "\n";
        //end of the predefined configurations values

        //write the values to the file
        try
        {
            file.write(configVal.getBytes());
        } catch (IOException e)
        {
            Logging.FatalErrorLog("Failed To Write Configuration To Config File");
            System.exit(-1);
        }

        //close the config file FD
        try
        {
            file.close();
        } catch (IOException e)
        {
            Logging.FatalErrorLog("Failed To Close Config File");
            System.exit(-1);
        }
    }


    /**
     * Checks if the configuration file exits and creates it if it does not
     * exist
     *
     * @return whether or not the configuration file exits
     */
    private boolean configFileInitializtion()
    {
        File configFile = new File(this.configFilePath);
        if (!configFile.exists())
        {
            this.createConfigFile();
            return false;
        }
        return true;
    }

}
