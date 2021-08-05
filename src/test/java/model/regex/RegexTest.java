package model.regex;

import java.util.List;

import javax.swing.UIManager;

import org.junit.Test;

import com.formdev.flatlaf.FlatLightLaf;

import exception.automata.DuplicatedStateException;
import exception.automata.DuplicatedTransitionException;
import exception.automata.InvalidStateException;
import exception.regex.BracketMismatchException;
import exception.regex.InvalidInputException;
import exception.regex.OperatorMismatchException;
import model.automata.Automata;
import model.automata.Transition;

public class RegexTest {

    @Test
    public void regexTest() {
	try {
	    Regex re = new Regex("(a|b)*abb");	
		re.printTree(re.tree());
		
		System.out.println("---------------------");
		
		re.prints(re.tree());
		
		Automata auto = re.convert();
		
		System.out.println("ESTADOS:");
	    List<String> states2 = auto.getStates();
	    for (int i = 0; i < states2.size(); i++)
		System.out.println(states2.get(i));
	    System.out.println("ESTADO INICIAL");
	    System.out.println(auto.getInitialState());
	    System.out.println("ESTADO FINAIS:");
	    List<String> finalStates2 = auto.getFinalStates();
	    for (int i = 0; i < finalStates2.size(); i++)
		System.out.println(finalStates2.get(i));
	    System.out.println("TRANSITIONS:");
		for (Transition transition : auto.getTransitions()) {
			for (String targetState : transition.getTargetStates()) {
				System.out.println(transition.getSourceState() + ">>" +transition.getTransitionCharacter()+">>"+ targetState);
			}
		}

	} catch (BracketMismatchException e) {
	    e.printStackTrace();
	} catch (OperatorMismatchException e) {
	    e.printStackTrace();
	} catch (InvalidInputException e) {
	    e.printStackTrace();
	} catch (DuplicatedStateException e) {
		e.printStackTrace();
	} catch (InvalidStateException e) {
		e.printStackTrace();
	} catch (DuplicatedTransitionException e) {
		e.printStackTrace();
	}

	try {
	    UIManager.setLookAndFeel(new FlatLightLaf());
	} catch (Exception ex) {
	    System.err.println("Failed to initialize LaF");
	}
    }

}
