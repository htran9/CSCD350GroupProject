package cs350s22.component.ui.parser;

import java.io.IOException;

public class Parser {
    private A_ParserHelper parserHelper;
    private String commandText;
    public Parser(final A_ParserHelper parserHelper, final String commandText) {
        this.commandText = commandText;
        this.parserHelper = parserHelper;
    }

    public void parse() throws IOException {
        System.out.println("PARSING: " + commandText);
        if(commandText.equalsIgnoreCase("@exit"))
            parserHelper.exit();
        if(commandText.matches("^CREATE ACTUATOR.+") || commandText.matches("^CREATE SENSOR.+") || commandText.matches("^BUILD NETWORK.+"))
        {
            KeithParser k = new KeithParser((ParserHelper) parserHelper, commandText);
            k.parse();
        }



    }
}
