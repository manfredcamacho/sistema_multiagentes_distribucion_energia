// Internal action code for project JCM_energy_distribution_planning

package jia;

import java.util.jar.JarException;

import jason.*;
import jason.asSemantics.*;
import jason.asSyntax.*;
import utils.JasonUtils;

public class getDoubleItemFromArrayAtIxdex extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        // execute the internal action
        ts.getAg().getLogger().info("executing internal action 'jia.getArrayDoubleItem'");
        
        if (!(args.length == 3)) {
       	 		
        	throw new JasonException("Needs Three arguments: index, numeric array"
        			+ " and a Term to unifies result");
        }
        if (!args[0].isNumeric()) {
        	throw new JasonException("Index is not a number");
        }
        
        int index = (int) JasonUtils.getDoubleFromTerm(args[0]);
        
        if (!args[1].isList()){
        	throw new JasonException("second arg is not an array");
        }

        double [] values = JasonUtils.getDoubleArrayfrom(args[1]);
        
     
        
        if (index >= values.length || index < 0) {
        	throw new JarException("Index out of bounds");
        }
        double arrayItem = values[index]; 
        
        NumberTerm numTerm = new NumberTermImpl(arrayItem); 
        return un.unifies(numTerm,args[2]);
        
    }
}
