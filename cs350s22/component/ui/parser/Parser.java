package cs350s22.component.ui.parser;

import cs350s22.component.logger.LoggerMessage;
import cs350s22.component.logger.LoggerMessageSequencing;
import cs350s22.support.Filespec;

import java.io.IOException;

public class Parser {
    private A_ParserHelper parserHelper;
    private String commandText;
    public Parser(final A_ParserHelper parserHelper, final String commandText) {
        this.commandText = commandText;
        this.parserHelper = parserHelper;
    }

    public void parse() throws IOException {
        String[] tokens = commandText.split(" ");
        switch(tokens[0].toUpperCase())
        {
            case "CREATE":
                if(tokens.length < 2)
                    throw new IllegalArgumentException("Malformed command:" + System.lineSeparator() + commandText);
                switch(tokens[1].toUpperCase())
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
            case "@CONFIGURE"://In progress
            {

                if (tokens.length < 7)
                    throw new IllegalArgumentException("Malformed command:" + System.lineSeparator() + commandText);

                String log = tokens[2].replaceAll("\"","");
                String dot = tokens[5].replaceAll("\"","");
                String network = tokens[7].replaceAll("\"","");

                LoggerMessage.initialize(Filespec.make(log));
                LoggerMessageSequencing.initialize(Filespec.make(dot), Filespec.make(network));


            }
            break;
            default:
                throw new IllegalArgumentException("Malformed command:" + System.lineSeparator() + commandText);
        }
    }
}
