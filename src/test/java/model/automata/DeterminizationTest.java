package model.automata;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import exception.automata.DuplicatedStateException;
import exception.automata.DuplicatedTransitionException;
import exception.automata.InvalidStateException;
import model.io.FileUtils;

public class DeterminizationTest {

	@Test
	public void determinizationTest() throws IOException, InvalidStateException, DuplicatedStateException, DuplicatedTransitionException {
		File file = null;
		Automata automata = null;
		for (int i = 1; i < 6; i++) {
			file = new File(getClass().getClassLoader().getResource(i+".json").getPath());
			automata = FileUtils.loadFromFile(file.getAbsolutePath(), Automata.class);
			automata.determinize();
		}
	}
}
