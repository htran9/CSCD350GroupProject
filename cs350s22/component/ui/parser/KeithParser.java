package cs350s22.component.ui.parser;

import cs350s22.component.actuator.A_Actuator;
import cs350s22.component.controller.A_Controller;
import cs350s22.component.controller.A_ControllerForwarding;
import cs350s22.component.sensor.A_Sensor;
import cs350s22.component.sensor.mapper.A_Mapper;
import cs350s22.component.sensor.reporter.A_Reporter;
import cs350s22.component.sensor.watchdog.A_Watchdog;
import cs350s22.support.Identifier;
import cs350s22.test.ActuatorPrototype;
import cs350s22.test.MySensor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KeithParser {
    private String commandText;
    private int currentToken;
    private String[] tokens;
    private ParserHelper parserHelper;
    public KeithParser( final ParserHelper parserHelper, final String commandText)
    {
        this.commandText = commandText;
        tokens = this.commandText.split(" ");
        this.parserHelper = parserHelper;
        this.currentToken = 1;

    }
    public void parse() //will parse and execute commands A1,F1, and H1.
    {
        if(tokens.length < 2)
            throw new RuntimeException("Malformed command:" + System.lineSeparator() + commandText);

        if(commandText.toUpperCase().matches("^BUILD NETWORK.+"))
        {
            buildNetwork();
        } else if (commandText.toUpperCase().matches("^CREATE ACTUATOR.+")) {
            createActuator();
        }
        else if (commandText.toUpperCase().matches("^CREATE SENSOR.+"))
        {
            createSensor();
        }
        else
            throw new RuntimeException("Malformed command:" + System.lineSeparator() + commandText);

    }
    private void createActuator()//unfinished
    {

        //System.out.println("CREATING ACTUATOR");

        ActuatorPrototype actuator;
        Identifier id = Identifier.make(tokens[3]);
        List<Identifier> groups;
        List<Identifier> sensorIds;
        List<A_Sensor> sensors;
        double leadIn;
        double leadOut;
        double relax;
        double velocityLimit;
        double min;
        double max;
        double initial;
        double jerkLimit;

        currentToken = 4;

        groups = parseIdentifierList("GROUPS?","(SENSORS?|ACCELERATION)");

        if(currentToken >= tokens.length)
            throw new RuntimeException("Malformed command:" + System.lineSeparator() + commandText);


        sensorIds = parseIdentifierList("SENSORS?", "ACCELERATION");
        SymbolTable<A_Sensor> allSensors = parserHelper.getSymbolTableSensor();
        sensors = allSensors.get(sensorIds);

        //Check if we have the correct number of tokens
        if(tokens.length <= currentToken + 19)
            throw new RuntimeException("Malformed command:" + System.lineSeparator() + commandText);

        //check that all the keywords are correct and in the correct order.
        if(         !tokens[currentToken].matches("(i?)ACCELERATION")
                ||  !tokens[currentToken+1].matches("(i?)LEADIN")
                ||  !tokens[currentToken+3].matches("(i?)LEADOUT")
                ||  !tokens[currentToken+5].matches("(i?)RELAX")
                ||  !tokens[currentToken+7].matches("(i?)VELOCITY")
                ||  !tokens[currentToken+8].matches("(i?)LIMIT")
                ||  !tokens[currentToken+10].matches("(i?)VALUE")
                ||  !tokens[currentToken+11].matches("(i?)MIN")
                ||  !tokens[currentToken+13].matches("(i?)MAX")
                ||  !tokens[currentToken+15].matches("(i?)INITIAL")
                ||  !tokens[currentToken+17].matches("(i?)JERK")
            ||  !tokens[currentToken+18].matches("(i?)LIMIT"))
            throw new RuntimeException("Malformed command:" + System.lineSeparator() + commandText);

        //Collect the values needed to create the actuator.
        currentToken += 2;//Move to the value of LeadIn

        leadIn = Double.parseDouble(tokens[currentToken]);
        currentToken += 2;//Move to the value of LeadOut

        leadOut = Double.parseDouble(tokens[currentToken]);
        currentToken += 2;//Move to the value of relax

        relax = Double.parseDouble(tokens[currentToken]);
        currentToken += 3;//Move to the value of velocityLimit

        velocityLimit = Double.parseDouble(tokens[currentToken]);
        currentToken += 3; //move to value of min

        min = Double.parseDouble(tokens[currentToken]);
        currentToken +=2;//move to value of max

        max = Double.parseDouble(tokens[currentToken]);
        currentToken +=2;//move to value of initial

        initial = Double.parseDouble(tokens[currentToken]);
        currentToken +=3;//move to value of jerkLimit

        jerkLimit = Double.parseDouble(tokens[currentToken]);

        //Create the actuator with the collected values
        actuator = new ActuatorPrototype(id, groups, leadIn, leadOut, relax, velocityLimit, initial, min, max, jerkLimit, sensors);

        //get the actuator symbol table
        SymbolTable<A_Actuator> allActuators = parserHelper.getSymbolTableActuator();
        //add the new actuator to the symbol table.
        allActuators.add(id,actuator);

    }
    private void createSensor()//unfinished
    {
        //System.out.println("CREATING SENSOR");
        Identifier id;

        List<Identifier> groups;

        List<Identifier> reporterIds;
        List<A_Reporter> reporters;

        List<Identifier> watchdogIds;
        List<A_Watchdog> watchdogs;

        A_Mapper mapper = null;
        Identifier mapperId;

        MySensor sensor;

        currentToken = 3;

        //parse values

        id = Identifier.make(tokens[currentToken++]);

        //parse groups
        groups = parseIdentifierList("GROUPS?","(REPORTERS?|WATCHDOGS?|MAPPER)");





        //parse reporters
        reporterIds = parseIdentifierList("REPORTERS?","(WATCHDOGS?|MAPPER)");
        SymbolTable<A_Reporter> allReporters = parserHelper.getSymbolTableReporter();//get the reporter table
        reporters = allReporters.get(reporterIds, false);//get the reporters

        //parse watchdogs
        watchdogIds = parseIdentifierList("WATCHDOGS?", "MAPPER");
        SymbolTable<A_Watchdog> allWatchDogs = parserHelper.getSymbolTableWatchdog();//get the symbol table.
        watchdogs = allWatchDogs.get(watchdogIds, false);//get the watchdogs

        //parse Mapper
        if(currentToken< tokens.length && tokens[currentToken].toUpperCase().matches("MAPPER"))
        {
            currentToken ++;
            if(currentToken == tokens.length)
                throw new RuntimeException("Malformed command:" + System.lineSeparator() + commandText);

            mapperId = Identifier.make(tokens[currentToken]);

            SymbolTable<A_Mapper> allMappers = parserHelper.getSymbolTableMapper();
            mapper = allMappers.get(mapperId);
        }

        //create the sensor
        if(mapper!=null)
            sensor = new MySensor(id,groups,reporters,watchdogs,mapper);
        else
            sensor = new MySensor(id,groups,reporters,watchdogs);

        //add sensor to the symbolTable
        SymbolTable<A_Sensor> allSensors = parserHelper.getSymbolTableSensor();
        allSensors.add(id, sensor);
    }
    private void buildNetwork()
    {
        if(!commandText.matches("(i?)BUILD NETWORK WITH COMPONENTS? .*"))
            throw new RuntimeException("Malformed command:" + System.lineSeparator() + commandText);


        //System.out.println("BUILDING NETWORK");
        if(tokens.length < 5)
            throw new RuntimeException("Malformed command:" + System.lineSeparator() + commandText);


        List<Identifier> componentIds;
        //getting necessary parts of the architecture from parserHelper
        A_ControllerForwarding controllerMaster = parserHelper.getControllerMaster();
        SymbolTable<A_Actuator> actuatorSymbolTable = parserHelper.getSymbolTableActuator();
        SymbolTable<A_Sensor> sensorSymbolTable = parserHelper.getSymbolTableSensor();
        SymbolTable<A_Controller> controllerSymbolTable = parserHelper.getSymbolTableController();


        //Making a list of Identifiers from the input string
        componentIds = parserHelper.getIdentifiers(tokens, 4,tokens.length);
        //getting the components associated with the given Identifiers
        List<A_Actuator> actuators = actuatorSymbolTable.get(componentIds,true);
        List<A_Sensor> sensors = sensorSymbolTable.get(componentIds,true);
        List<A_Controller> controllers = controllerSymbolTable.get(componentIds,true);
        //adding the components to the master controller
        controllerMaster.addComponents(actuators);
        controllerMaster.addComponents(controllers);
        controllerMaster.addComponents(sensors);
        //writing the network to output
        parserHelper.getNetwork().writeOutput();
    }
    //returns the list of identifiers starting at currentToken and ending either at
    //the first match of endRegex or the end of the array. Increments currentToken to the index of the first match or end of array.
    private List<Identifier> parseIdentifierList(String startRegex, String endRegex)
    {
        List<Identifier> out = new ArrayList<Identifier>(0);
        if(currentToken< tokens.length && tokens[currentToken].toUpperCase().matches("(i?)"+startRegex))
        {
            currentToken++;
            int startToken = currentToken;
            currentToken = parserHelper.getEndIndex(tokens, startToken, endRegex);

            if(currentToken == startToken)
                throw new RuntimeException("Malformed command:" + System.lineSeparator() + commandText);

            out = parserHelper.getIdentifiers(tokens, startToken, currentToken);
        }
        return out;
    }

}
