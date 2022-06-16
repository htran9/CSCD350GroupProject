package cs350s22.component.ui.parser;

import java.io.IOException;
import java.util.*;

import cs350s22.component.sensor.mapper.*;
import cs350s22.component.sensor.mapper.function.equation.*;
import cs350s22.component.sensor.mapper.function.interpolator.*;
import cs350s22.component.sensor.mapper.function.interpolator.loader.A_MapLoader;
import cs350s22.component.sensor.mapper.function.interpolator.loader.MapLoader;
import cs350s22.component.ui.CommandLineInterface;
import cs350s22.message.A_Message;
import cs350s22.message.actuator.MessageActuatorReportPosition;
import cs350s22.message.actuator.MessageActuatorRequestPosition;
import cs350s22.message.ping.MessagePing;
import cs350s22.support.*;

public class AlexParser {
    private String commandText;
    private String[] tokens;
    private ParserHelper parserHelper;
    public AlexParser( final ParserHelper parserHelper, final String commandText)
    {
        this.commandText = commandText;
        tokens = this.commandText.split(" ");
        this.parserHelper = parserHelper;
    }
    public void parse() throws IOException //will parse and execute commands C1 and D1.
    {
        if(tokens.length < 2)
            throw new RuntimeException("Malformed command:" + System.lineSeparator() + commandText);
        if(commandText.toUpperCase().matches("^CREATE MAPPER.+"))
        {
            createMapper();//executes commands C1 C2 C3 C4
        } 
        else if(commandText.toUpperCase().matches("^SEND MESSAGE.+")){
        	sendMessage();//Executes commands D1 D2 D3
        }
        else
            throw new RuntimeException("Malformed command:" + System.lineSeparator() + commandText);

    }
    private void createMapper() throws IOException
    {

        
        SymbolTable<A_Mapper> mapperTable = parserHelper.getSymbolTableMapper();
        Identifier mapID = Identifier.make(tokens[2]);
        
        if(tokens[3].equalsIgnoreCase("EQUATION"))
        {
            
            
            if(tokens[4].equalsIgnoreCase("PASSTHROUGH")){//creates equationPassthrough object, passes it into MapperEquation object then adds to list
            	
            	System.out.println("CREATING EQUATION PASSTHROUGH MAPPER");
            	
            	EquationPassthrough passMapper = new EquationPassthrough();
            	MapperEquation eqMapper = new MapperEquation(passMapper);
            	mapperTable.add(mapID, eqMapper);
            	
            }
            else if(tokens[4].equalsIgnoreCase("SCALE")) {
            	
            	System.out.println("CREATING EQUATION SCALE MAPPER");
            	
            	double value = Double.parseDouble(tokens[5]);
            	EquationScaled scaleMapper = new EquationScaled(value);
            	MapperEquation eqMapper = new MapperEquation(scaleMapper);
            	mapperTable.add(mapID, eqMapper);
            	
            }
            else if(tokens[4].equalsIgnoreCase("NORMALIZE")) {
            	
            	System.out.println("CREATING EQUATION NORMALIZE MAPPER");
            	
            	double value1 = Double.parseDouble(tokens[5]), value2 = Double.parseDouble(tokens[6]);
            	EquationNormalized normMapper = new EquationNormalized(value1, value2);
            	MapperEquation eqMapper = new MapperEquation(normMapper);
            	mapperTable.add(mapID, eqMapper);
            	
            }
            else {throw new RuntimeException("Malformed command:" + System.lineSeparator() + commandText);}
        }
        else if(tokens[3].equalsIgnoreCase("INTERPOLATION"))
        {
            System.out.println("CREATING INTERPOLATION MAPPER");
            
            System.out.println("CREATING EQUATION SCALE MAPPER");
        	String fileName = tokens[5].replaceAll("^\"|\"$", "");
            
            Filespec file = Filespec.make(fileName);
            A_MapLoader mpLd = new MapLoader(file);
            InterpolationMap iMapper = mpLd.load();
            
            if(tokens[4].equalsIgnoreCase("LINEAR")) {
            	
            	System.out.println("CREATING LINEAR INTERPOLATION MAPPER");
            	
            	InterpolatorLinear iplMap = new InterpolatorLinear(iMapper);
            	MapperInterpolation mapInt = new MapperInterpolation(iplMap);
            	mapperTable.add(mapID, mapInt);
            	
            }
            
            else if(tokens[4].equalsIgnoreCase("SPLINE")) {
            	
            	System.out.println("CREATING SPLINE INTERPOLATION MAPPER");
            	
            	InterpolatorSpline ipsMap = new InterpolatorSpline(iMapper);
            	MapperInterpolation mapInt = new MapperInterpolation(ipsMap);
            	mapperTable.add(mapID, mapInt);
            	
            }
            else {throw new RuntimeException("Malformed command:" + System.lineSeparator() + commandText);}
            
            
        }
        else
            throw new RuntimeException("Malformed command:" + System.lineSeparator() + commandText);



    }
    private void sendMessage()
    {
        System.out.println("SENDING MESSAGE...");
        
        CommandLineInterface cLI = parserHelper.getCommandLineInterface();
        
        if(tokens[2].equalsIgnoreCase("PING")) {
        	
        	System.out.println("SENDING PING");
        	
        	MessagePing ping = new MessagePing();
        	cLI.issueMessage(ping);
        	
        }
        else if(tokens[tokens.length-2].equalsIgnoreCase("REQUEST")) {
        	
        	double value = Double.parseDouble(tokens[tokens.length-1]);
        	
        	A_Message message;
        	
        	if(tokens[2].equalsIgnoreCase("IDS?")) {
        		
        		if(commandText.contains("(?i)GROUPS?")) {
        			
        			int gIndex;
        			
        			if(commandText.contains("(?i)GROUPS")) 
        				gIndex = parserHelper.getEndIndex(tokens, 2, "GROUPS");
        			else 
        				gIndex = parserHelper.getEndIndex(tokens, 2, "GROUP");
        			
        			message = createRequestMessage(3, gIndex, "ID", value);
        			cLI.issueMessage(message);
        			
        			message = createRequestMessage(gIndex, tokens.length-2, "GROUPS", value);
        			cLI.issueMessage(message);
        			
        		}
        		else {	
        			
        			message = createRequestMessage(3, tokens.length-2, "ID", value);
        			cLI.issueMessage(message);
        			
        		}
        		
        	}
        	
        	else if(tokens[2].equalsIgnoreCase("GROUPS?")) {
        		
        		
        		message = createRequestMessage(3, tokens.length-2, "GROUPS", value);
    			cLI.issueMessage(message);
        		
        	}
        	else {throw new RuntimeException("Malformed command:" + System.lineSeparator() + commandText);}
        	
        }
        else if(tokens[tokens.length-1].equalsIgnoreCase("REPORT")) {
        	
        	
        	A_Message message;
        	
        	if(tokens[2].equalsIgnoreCase("ID") || tokens[2].equalsIgnoreCase("IDS")) {
        		
        		if(commandText.contains("GROUPS")) {
        			
        			int gIndex;
        			
        			if(commandText.contains("(?i)GROUPS")) 
        				gIndex = parserHelper.getEndIndex(tokens, 2, "GROUPS");
        			else 
        				gIndex = parserHelper.getEndIndex(tokens, 2, "GROUP");
        			
        			message = createReportMessage(3, gIndex, "ID");
        			cLI.issueMessage(message);
        			
        			message = createReportMessage(gIndex, tokens.length-1, "GROUPS");
        			cLI.issueMessage(message);
        			
        		}
        		else {	
        			
        			message = createReportMessage(3, tokens.length-1, "ID");
        			cLI.issueMessage(message);
        			
        		}
        		
        	}
        	
        	else if(tokens[2].equalsIgnoreCase("GROUPS")) {
        		
        		message = createReportMessage(3, tokens.length-1, "GROUPS");
    			cLI.issueMessage(message);
        		
        	}
        	else {throw new RuntimeException("Malformed command:" + System.lineSeparator() + commandText);}
        }
        else {throw new RuntimeException("Malformed command:" + System.lineSeparator() + commandText);}

    }
    
    private A_Message createRequestMessage(int startIndex, int endIndex, String matcher, double value) {
    	
    	List<Identifier> idList = parserHelper.getIdentifiers(tokens, startIndex, endIndex-1);
    	
    	if(matcher.equalsIgnoreCase("ID") || matcher.equalsIgnoreCase("IDS")) {
    		
    		A_Message message = new MessageActuatorRequestPosition(idList,value);
    		return message;
    	}
    	else if(matcher.equalsIgnoreCase("GROUPS")) {
    	
    		A_Message message = new MessageActuatorRequestPosition(idList,value,0);
    		return message;
    		
    	}
    	else
    		return null;
    	
    }
    
    private A_Message createReportMessage(int startIndex, int endIndex, String matcher) {
    	
List<Identifier> idList = parserHelper.getIdentifiers(tokens, startIndex, endIndex-1);
    	
    	if(matcher.equalsIgnoreCase("ID") || matcher.equalsIgnoreCase("IDS")) {
    		
    		A_Message message = new MessageActuatorReportPosition(idList);
    		return message;
    		
    	}
    	else if(matcher.equalsIgnoreCase("GROUPS")) {
    	
    		A_Message message = new MessageActuatorReportPosition(idList,0);
    		return message;
    		
    	}
    	else
    		return null;
    	
    }
    
}