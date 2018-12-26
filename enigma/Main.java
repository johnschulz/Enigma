package enigma;


import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.error;

/**
 * Enigma simulator.
 *
 * @author John Schulz
 */
public final class Main {

    /**
     * Process a sequence of encryptions and decryptions, as
     * specified by ARGS, where 1 <= ARGS.length <= 3.
     * ARGS[0] is the name of a configuration file.
     * ARGS[1] is optional; when present, it names an input file
     * containing messages.  Otherwise, input comes from the standard
     * input.  ARGS[2] is optional; when present, it names an output
     * file for processed messages.  Otherwise, output goes to the
     * standard output. Exits normally if there are no errors in the input;
     * otherwise with code 1.
     */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /**
     * Check ARGS and open the necessary files (see comment on main).
     */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /**
     * Return a Scanner reading from the file named NAME.
     */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /**
     * Return a PrintStream writing to the file named NAME.
     */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /**
     * Configure an Enigma machine from the contents of configuration
     * file _config and apply it to the messages in _input, sending the
     * results to _output.
     */
    private void process() {
        Machine m = readConfig();
        String settings = "";
        boolean setup = false;
        while (_input.hasNextLine()) {
            settings = _input.nextLine();
            if (settings.equals("")) {
                _output.println();
            } else if (settings.charAt(0) == '*') {
                setup = true;
                setUp(m, settings);
                int c = 1;
            } else {
                if (!setup) {
                    throw new EnigmaException("No setting declared.");
                }
                String ret = "";
                settings = settings.toUpperCase();
                for (int x = 0; x < settings.length(); x += 1) {
                    char next = settings.charAt(x);
                    if (_alphabet.contains(next)) {
                        int z = m.convert(_alphabet.toInt(next));
                        char c = _alphabet.toChar(z);
                        ret += Character.toString(c);
                    }
                }
                printMessageLine(ret);
            }
        }
    }

    /**
     * Return an Enigma machine configured from the contents of configuration
     * file _config.
     */
    private Machine readConfig() {
        try {
            String c = _config.next();
            char c1 = c.charAt(0);
            char c2 = c.charAt(c.length() - 1);
            String s = _config.next();
            String p = _config.next();
            if (s.charAt(0) < '0' || s.charAt(0) > '9'
                    || p.charAt(0) < '0' || p.charAt(0) > '9') {
                String e = "Bad rotor and pawl configuration";
                throw new EnigmaException(e);
            }
            int s1 = Integer.parseInt(String.valueOf(s));
            int p1 = Integer.parseInt(String.valueOf(p));
            Collection<Rotor> allRotors = new ArrayList<>();
            if (c.length() == 3) {
                if (c1 > c2) {
                    throw new EnigmaException("Incorrect alphabet format");
                }
                _alphabet = new CharacterRange(c1, c2);
            } else {
                _alphabet = new Characters(c);
            }
            while (_config.hasNext()) {
                String name = _config.next();
                String typ = _config.next();
                String perm = "";
                while (_config.hasNext("[(][^-]*[)]")) {
                    perm += _config.next();
                }
                if (!typ.contains("M") && !typ.contains("R")
                        && !typ.contains("N")) {
                    throw new EnigmaException("Incorrect rotor set up");
                }
                Rotor ad;
                if (typ.charAt(0) == 'M') {
                    Permutation permu = new Permutation(perm, _alphabet);
                    ad = new MovingRotor(name, permu, typ.substring(1));
                } else if (typ.charAt(0) == 'R') {
                    ad = new Reflector(name, new Permutation(perm, _alphabet));
                } else {
                    ad = new Rotor(name, new Permutation(perm, _alphabet));
                }
                allRotors.add(ad);
            }
            return new Machine(_alphabet, s1, p1, allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }
    /**
     * Return a rotor, reading its description from _config.
     */
    private Rotor readRotor() {
        try {
            return null;
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /**
     * Set M according to the specification given on SETTINGS,
     * which must have the format specified in the assignment.
     */
    private void setUp(Machine M, String settings) {
        int count = 0;
        int s = M.numRotors();
        Scanner settin = new Scanner(settings);
        String[] insertStruct = new String[s];
        settin.next();
        while (settin.hasNext() && s > count) {
            insertStruct[count] = settin.next();
            count += 1;
        }
        M.insertRotors(insertStruct);
        String spot = settin.next();
        M.setRotors(spot);
        String perm = "";
        while (settin.hasNext()) {
            perm += settin.next();
        }
        M.setPlugboard(new Permutation(perm, _alphabet));
    }

    /**
     * Print MSG in groups of five (except that the last group may
     * have fewer letters).
     */
    private void printMessageLine(String msg) {
        while (!msg.equals("")) {
            for (int x = 0; x < 5; x += 1) {
                if (!msg.equals("")) {
                    _output.print(msg.charAt(0));
                    msg = msg.substring(1);
                }
            }
            _output.print(" ");
        }
        _output.println();
    }

    /**
     * Alphabet used in this machine.
     */
    private Alphabet _alphabet;

    /**
     * Source of input messages.
     */
    private Scanner _input;

    /**
     * Source of machine configuration.
     */
    private Scanner _config;

    /**
     * File for encoded/decoded messages.
     */
    private PrintStream _output;
}
