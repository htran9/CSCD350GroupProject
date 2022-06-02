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

        /*if(commandText.equalsIgnoreCase("@exit"))
            parserHelper.exit();
        if(commandText.matches("^CREATE ACTUATOR.+") || commandText.matches("^CREATE SENSOR.+") || commandText.matches("^BUILD NETWORK.+"))
        {
            KeithParser k = new KeithParser((ParserHelper) parserHelper, commandText);
            k.parse();
        }*/
        String[] tokens = commandText.toUpperCase().split(" ");
        switch(tokens[0])
        {
            case "CREATE":
                if(tokens.length < 2)
                    throw new IllegalArgumentException("Malformed command:" + System.lineSeparator() + commandText);
                switch(tokens[1])
                {
                    case "ACTUATOR":
                    case "SENSOR":
                        KeithParser k = new KeithParser((ParserHelper) parserHelper, commandText);
                        k.parse();
                        break;
                        //Other CREATE Commands can go under here
                    case "REPORTER":
                    case "WATCHDOG":
                        break; //insert Hieu Tran's solution here.

                    case "MAPPER":
                        break; //insert Alex Cannon's solution here.
                    default:
                        throw new IllegalArgumentException("Malformed command:" + System.lineSeparator() + commandText);
                }
                break;
            case "BUILD":
                KeithParser k = new KeithParser((ParserHelper) parserHelper, commandText);
                k.parse();
                break;
            case "SEND": //TBD
                break;

            case "@CLOCK": //In progress
                break;
            case "@EXIT":
                parserHelper.exit();
                break;
            case "@RUN": //In progress
                break;
            case "@CONFIGURE"://TBD
                break;
            default:
                throw new IllegalArgumentException("Malformed command:" + System.lineSeparator() + commandText);
        }
    }
}
