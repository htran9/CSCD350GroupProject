package cs350s22.component.ui.parser;

import cs350s22.test.ActuatorPrototype;

public class KeithParser {
    private String commandText;
    private String[] tokens;
    private ParserHelper parserHelper;
    public KeithParser( final ParserHelper parserHelper, final String commandText)
    {
        this.commandText = commandText.toUpperCase();
        tokens = this.commandText.split(" ");
        this.parserHelper = parserHelper;
    }
    public void parse() //will parse and execute commands A1,F1, and H1.
    {
        if(tokens.length < 2)
            throw new IllegalArgumentException("Malformed command:" + System.lineSeparator() + commandText);
        if(commandText.toUpperCase().matches("^BUILD NETWORK.+"))
        {
            buildNetwork();
        } else if (commandText.matches("^CREATE ACTUATOR.+")) {
            createActuator();
        }
        else if (commandText.matches("^CREATE SENSOR.+"))
        {
            createSensor();
        }
        else
            throw new IllegalArgumentException("Malformed command:" + System.lineSeparator() + commandText);

    }
    private void createActuator()//unfinished
    {

        System.out.println("CREATING ACTUATOR");
        /*if(tokens.length < 25)
            throw new IllegalArgumentException("Malformed command:" + System.lineSeparator() + commandText);*/

        ActuatorPrototype actuator;
        if(tokens[2].matches("ROTARY"))
        {
            System.out.println("CREATING ROTARY ACTUATOR");
        }
        else if(tokens[2].matches("LINEAR"))
        {
            System.out.println("CREATING LINEAR ACTUATOR");
        }
        else
            throw new IllegalArgumentException("Malformed command:" + System.lineSeparator() + commandText);



    }
    private void createSensor()//unfinished
    {
        System.out.println("CREATING SENSOR");

    }
    private void buildNetwork()//unfinished
    {
        System.out.println("BUILDING NETWORK");

    }
}
