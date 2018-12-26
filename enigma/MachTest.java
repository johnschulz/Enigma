package enigma;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class MachTest {
    @Test
    public void testDoubleStep() {
        Alphabet ac = new CharacterRange('A', 'C');
        Rotor one = new Reflector("R1", new Permutation("(AC)", ac));
        Rotor two = new MovingRotor("R2", new Permutation("(ABC)", ac), "C");
        Rotor three = new MovingRotor("R3", new Permutation("(ABC)", ac), "C");
        Rotor four = new MovingRotor("R4", new Permutation("(ABC)", ac), "C");
        String setting = "AAA";
        Rotor[] machineRotors = {one, two, three, four};
        String[] rotors = {"R1", "R2", "R3", "R4"};
        ArrayList<Rotor> arra = new ArrayList<>(Arrays.asList(machineRotors));
        Machine mach = new Machine(ac, 4, 3, arra);
        mach.setPlugboard(new Permutation("", ac));
        mach.insertRotors(rotors);
        mach.setRotors(setting);

        assertEquals("AAAA", getSetting(ac, machineRotors));
        mach.convert('A');
        assertEquals("AAAB", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AAAC", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AABA", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AABB", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AABC", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AACA", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("ABAB", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("ABAC", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("ABBA", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("ABBB", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("ABBC", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("ABCA", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("ACAB", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("ACAC", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("ACBA", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("ACBB", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("ACBC", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("ACCA", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AAAB", getSetting(ac, machineRotors));

    }

    private String getSetting(Alphabet alph, Rotor[] machineRotors) {
        String currSetting = "";
        for (Rotor r : machineRotors) {
            currSetting += alph.toChar(r.setting());
        }
        return currSetting;
    }
}
