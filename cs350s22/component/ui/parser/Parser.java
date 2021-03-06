package cs350s22.component.ui.parser;

import cs350s22.component.logger.LoggerActuator;
import cs350s22.component.logger.LoggerMessage;
import cs350s22.component.logger.LoggerMessageSequencing;
import cs350s22.support.Clock;
import cs350s22.support.Filespec;

import java.io.IOException;

public class Parser {
    private A_ParserHelper parserHelper;
    private String commandText;
    private String [] tokens;
    public Parser(final A_ParserHelper parserHelper, final String commandText) {
        this.commandText = commandText;
        this.parserHelper = parserHelper;
        tokens = commandText.split(" ");
    }
    public void parse() throws IOException, ParseException {

        switch(tokens[0].toUpperCase())
        {
            case "CREATE":
                if(tokens.length < 2)
                    throw new RuntimeException("Malformed command:" + System.lineSeparator() + commandText);
                switch(tokens[1].toUpperCase())
                {
                    case "ACTUATOR":
                    case "SENSOR":
                        KeithParser k = new KeithParser((ParserHelper) parserHelper, commandText);
                        k.parse();
                        break;
                    case "REPORTER":
                    case "WATCHDOG":
                        HieuParser r = new HieuParser(parserHelper, commandText);
                        r.parse();
                        break;

                    case "MAPPER":
                        AlexParser a = new AlexParser((ParserHelper)parserHelper, commandText);
                        a.parse();
                        break;
                    default:
                        throw new RuntimeException("Malformed command:" + System.lineSeparator() + commandText);
                }
                break;
            case "BUILD":
                KeithParser k = new KeithParser((ParserHelper) parserHelper, commandText);
                k.parse();
                break;
            case "SEND":
                AlexParser a = new AlexParser((ParserHelper)parserHelper, commandText);
                a.parse();
                break;
            case "@CLOCK":
                parseClock();
                break;
            case "@EXIT":
                parserHelper.exit();
                break;
            case "@RUN":
                String filename = tokens[2].replaceAll("\"","");
                parserHelper.run(filename);
                break;
            case "@CONFIGURE":
            {

                if (tokens.length < 7)
                    throw new RuntimeException("Malformed command:" + System.lineSeparator() + commandText);

                String log = tokens[2].replaceAll("\"","");
                String dot = tokens[5].replaceAll("\"","");
                String network = tokens[7].replaceAll("\"","");
                String xml = tokens[9].replaceAll("\"","");
                LoggerMessage.initialize(Filespec.make(log));
                LoggerMessageSequencing.initialize(Filespec.make(dot), Filespec.make(network));
            }
            break;
            default:
                throw new RuntimeException("Malformed command:" + System.lineSeparator() + commandText);
        }
    }
    private void parseClock()
    {
        if(tokens.length < 2)
            throw new RuntimeException("Malformed command:" + System.lineSeparator() + commandText);
        Clock clock = Clock.getInstance();
        switch(tokens[1].toUpperCase())
        {
            case "PAUSE":
                clock.isActive(false);
                break;
            case "RESUME":
                clock.isActive(true);
                break;
            case "ONESTEP":
                if(tokens.length > 2)
                {
                    int count = Integer.parseInt(tokens[2]);
                    clock.onestep(count);
                }
                else clock.onestep();
                break;
            case "SET":
                if(tokens.length < 4||!tokens[2].equalsIgnoreCase("RATE"))
                    throw new RuntimeException("Malformed command:" + System.lineSeparator() + commandText);

                int multiplier = Integer.parseInt(tokens[3]);
                clock.setRate(multiplier);
                break;
            default:
                throw new RuntimeException("Malformed command:" + System.lineSeparator() + commandText);
        }
    }
}
