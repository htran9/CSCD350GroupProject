package cs350s22.component.ui.parser;

import cs350s22.support.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class ParserHelper extends A_ParserHelper
{
   public ParserHelper()
   {
      System.out.println("Welcome to your ParserHelper");
   }

   //scans through the array of tokens and returns the index of the first match of endRegex(case-insensitive).
   public int getEndIndex(final String [] tokens, int start, String endRegex)
   {
      int cur = start;
      while(cur< tokens.length && !tokens[cur].matches("(?i)"+endRegex)) cur++;
      return cur;
   }
   //makes an Identifier List out of the tokens from start to end(exclusive). If start >= end the list will be empty.
   public List<Identifier> getIdentifiers(String [] tokens, int start, int end)
   {
      List<Identifier> out;
      if(start >= end)
         out = new ArrayList<Identifier>(0);
      else
         out = Identifier.makeList(Arrays.copyOfRange(tokens, start,end));
      return out;
   }
}
