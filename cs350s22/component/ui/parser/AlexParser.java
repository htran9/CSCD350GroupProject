package cs350s22.component.ui.parser;

import java.io.IOException;

import cs350s22.component.sensor.mapper.*;
import cs350s22.component.sensor.mapper.function.equation.*;
import cs350s22.component.sensor.mapper.function.interpolator.*;
import cs350s22.component.sensor.mapper.function.interpolator.loader.MapLoader;
import cs350s22.component.ui.CommandLineInterface;
import cs350s22.message.A_Message;
import cs350s22.message.actuator.MessageActuatorRequestPosition;
import cs350s22.message.ping.MessagePing;
import cs350s22.support.*;

public class AlexParser {
    private String commandText;
    private String[] tokens;
    private ParserHelper parserHelper;
    public AlexParser( final ParserHelper parserHelper, final String commandText)
    {
        this.commandText = commandText.toUpperCase();
        tokens = this.commandText.split(" ");
        this.parserHelper = parserHelper;
    }
    public void parse() throws IOException //will parse and execute commands C1 and D1.
    {
        if(tokens.length < 2)
            throw new IllegalArgumentException("Malformed command:" + System.lineSeparator() + commandText);
        if(commandText.toUpperCase().matches("^CREATE MAPPER.+"))
        {
            createMapper();
        } 
        else if(commandText.toUpperCase().matches("^SEND MESSAGE.+")){
        	sendMessage();
        }
        else
            throw new IllegalArgumentException("Malformed command:" + System.lineSeparator() + commandText);

    }
    private void createMapper() throws IOException//unfinished
    {

        System.out.println("CREATING MAPPER");
        SymbolTable<A_Mapper> mapperTable = parserHelper.getSymbolTableMapper();
        Identifier mapID = Identifier.make(tokens[2]);
        
        if(tokens[3].matches("EQUATION"))
        {
            System.out.println("CREATING EQUATION MAPPER");
            
            if(tokens[4].matches("PASSTHROUGH")){//creates equationPassthrough object, passes it into MapperEquation object then adds to list
            	
            	System.out.println("CREATING EQUATION PASSTHROUGH MAPPER");
            	
            	EquationPassthrough passMapper = new EquationPassthrough();
            	MapperEquation eqMapper = new MapperEquation(passMapper);
            	mapperTable.add(mapID, eqMapper);
            	
            }
            else if(tokens[4].matches("SCALE")) {
            	
            	System.out.println("CREATING EQUATION SCALE MAPPER");
            	
            	double value = Double.parseDouble(tokens[5]);
            	EquationScaled scaleMapper = new EquationScaled(value);
            	MapperEquation eqMapper = new MapperEquation(scaleMapper);
            	mapperTable.add(mapID, eqMapper);
            	
            }
            else if(tokens[4].matches("NORMALIZE")) {
            	
            	System.out.println("CREATING EQUATION NORMALIZE MAPPER");
            	
            	double value1 = Double.parseDouble(tokens[5]), value2 = Double.parseDouble(tokens[6]);
            	EquationNormalized normMapper = new EquationNormalized(value1, value2);
            	MapperEquation eqMapper = new MapperEquation(normMapper);
            	mapperTable.add(mapID, eqMapper);
            	
            }
            else {throw new IllegalArgumentException("Malformed command:" + System.lineSeparator() + commandText);}
        }
        else if(tokens[3].matches("INTERPOLATION"))
        {
            System.out.println("CREATING INTERPOLATION MAPPER");
            
            Filespec file = new Filespec(tokens[6]);            
            MapLoader mpLd = new MapLoader(file);
            InterpolationMap iMapper = mpLd.load();
            
            if(tokens[4].matches("LINEAR")) {
            	
            	System.out.println("CREATING LINEAR INTERPOLATION MAPPER");
            	
            	InterpolatorLinear iplMap = new InterpolatorLinear(iMapper);
            	MapperInterpolation mapInt = new MapperInterpolation(iplMap);
            	mapperTable.add(mapID, mapInt);
            	
            }
            
            else if(tokens[4].matches("SPLINE")) {
            	
            	System.out.println("CREATING SPLINE INTERPOLATION MAPPER");
            	
            	InterpolatorSpline ipsMap = new InterpolatorSpline(iMapper);
            	MapperInterpolation mapInt = new MapperInterpolation(ipsMap);
            	mapperTable.add(mapID, mapInt);
            	
            }
            else {throw new IllegalArgumentException("Malformed command:" + System.lineSeparator() + commandText);}
            
            
        }
        else
            throw new IllegalArgumentException("Malformed command:" + System.lineSeparator() + commandText);



    }
    private void sendMessage()//unfinished
    {
        System.out.println("SENDING MESSAGE...");
        
        CommandLineInterface cLI = parserHelper.getCommandLineInterface();
        
        if(tokens[2].matches("PING")) {
        	
        	System.out.println("SENDING PING");
        	
        	MessagePing ping = new MessagePing();
        	cLI.issueMessage(ping);
        	
        }
        else if(tokens[tokens.length-2].matches("REQUEST")) {
        	
        	double value = Double.parseDouble(tokens[tokens.length-1]);
        	
        	
        	
        }
        else if(tokens[tokens.length-1].matches("REPORT")) {
        	
        	
        	
        }

    }
}