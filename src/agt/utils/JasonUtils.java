package utils;

import jason.NoValueException;
import jason.asSyntax.ListTerm;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;

public class JasonUtils {

//Precondition Term must be  a "Number"[] (not arraylist object) 
//where "Number" is int, float, double 
	
public static double[] getDoubleArrayfrom(Term term) throws NoValueException {
    	
    	if (!term.isList()) {
    		throw new NoValueException("Term is not list");
    	}
        
    	ListTerm termList = (ListTerm) term;
        
     	double[] arrayNumber = new double [termList.size()];
     	
     	for (int i = 0; i < termList.size(); i++) {
     		
     		 Term termItem = termList.get(i);
     		
     		 if (!termItem.isNumeric()) {
     			throw new NoValueException("Value is not a number (int,float or double)");
     		 }
     		 
  			 NumberTerm numberTerm = (NumberTerm)termItem;
				 double finalNumber = numberTerm.solve(); 
				 arrayNumber[i] = finalNumber;
		}
     	return arrayNumber;
    }
    
//Precondition Term must be a "Number"
//where "Number" is int, float, double 
 public  static double getDoubleFromTerm(Term term) throws NoValueException{
    	 
    	if (!term.isNumeric()) {
    		throw new NoValueException("Value is not a number");
    	}
    	
    	NumberTerm numberTerm = (NumberTerm)term;
	    double finalNumber = numberTerm.solve();
	    
		return finalNumber; 
    }
}
