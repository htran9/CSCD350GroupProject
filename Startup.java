import cs350s22.component.ui.parser.A_ParserHelper;
import cs350s22.component.ui.parser.Parser;
import cs350s22.component.ui.parser.ParserHelper;

//=================================================================================================================================================================================
public class Startup
{
   private final A_ParserHelper _parserHelper = new ParserHelper();
   
   // -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
   public Startup()
   {
      System.out.println("Welcome to your Startup class");
   }

   // -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
   public static void main(final String[] arguments) throws Exception
   {
      Startup startup = new Startup();
      
      // this command must come first. The filenames do not matter here
      startup.parse("@CONFIGURE LOG \"a.txt\" DOT SEQUENCE \"b.txt\" NETWORK \"c.txt\" XML \"d.txt\"");
      startup.parse("@CLOCK PAUSE");
      startup.parse("@CLOCK ONESTEP 20");
      startup.parse("CREATE SENSOR POSITION mySensor");
      startup.parse("CREATE ACTUATOR ROTARY myac SENSOR mySensor ACCELERATION LEADIN 10 LEADOUT -5 RELAX 20 VELOCITY LIMIT 100 VALUE MIN -1000 MAX 1000 INITIAL 500 JERK LIMIT 20"); //test actuator
      startup.parse("BUILD NETWORK WITH COMPONENT myac");
      startup.parse("@CLOCK ONESTEP");


      // run your tests like this
      startup.parse("@exit");
   }
   
   // -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
   private void parse(final String parse) throws Exception
   {
      System.out.println("PARSE> "+ parse);
      
         Parser parser = new Parser(_parserHelper, parse);
      
      parser.parse();
   }
}
