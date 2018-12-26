package enigma;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Class that represents a complete enigma machine.
 *
 * @author John Schulz
 */
class Machine {
    /** The plugboard for the machine.*/
    private Rotor _plugboarded;
    /** Returns the plugboard for the machine.*/
    public Rotor getPlugboarded() {
        return _plugboarded;
    }
    /** The number of Rotors for this machine.*/
    private int _numRotors;
    /** The number of Pawls for this machine.*/
    private int _numPawls;
    /** A list of rotors for this machine.*/
    private Rotor[] rotorr;
    /** Returns the rotors for this machine.*/
    public Rotor[] getRotors() {
        return rotorr;
    }
    /** The total amount of Rotors possible for this class.*/
    private ArrayList<Rotor> aRotors;
    /** Returns all possible rotors.*/
    public ArrayList<Rotor> getAllRotors() {
        return aRotors;
    }

    /**
     * A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     * and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     * available rotors.
     */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _numPawls = pawls;
        rotorr = new Rotor[_numRotors];
        ArrayList<Rotor> a = new ArrayList<>(allRotors);
        aRotors = a;
    }

    /**
     * Return the number of rotor slots I have.
     */
    int numRotors() {
        return _numRotors;
    }

    /**
     * Return the number pawls (and thus rotating rotors) I have.
     */
    int numPawls() {
        return _numPawls;
    }

    /**
     * Set my rotor slots to the rotors named ROTORS from my set of
     * available rotors (ROTORS[0] names the reflector).
     * Initially, all rotors are set at their 0 setting.
     */
    void insertRotors(String[] rotors) {
        ArrayList<Rotor> added = new ArrayList<>();
        for (int x = 0; x < rotors.length; x += 1) {
            String y = rotors[x].toUpperCase();
            Iterator<Rotor> rot = aRotors.iterator();
            while (rot.hasNext()) {
                Rotor next = rot.next();
                String upper = next.name().toUpperCase();
                if (upper.equals(y)) {
                    rotorr[x] = next;
                    added.add(rotorr[x]);
                }
            }
        }
        int actualPawls = 0;
        for (int x = 0; x < rotorr.length; x += 1) {
            if (MovingRotor.class.isInstance(rotorr[x])) {
                actualPawls += 1;
            }
        }
        if (actualPawls != numPawls()) {
            String e = "Amount of pawls does not match + "
                    + "the number of moving Rotors";
            throw new EnigmaException(e);
        }
        if (!Reflector.class.isInstance(rotorr[0])) {
            throw new EnigmaException("Reflector is not in the first position");
        }
        if (added.size() != rotors.length) {
            String e = "Rotor is not initialized in configuration file";
            throw new EnigmaException(e);
        }
    }

    /**
     * Set my rotors according to SETTING, which must be a string of
     * numRotors()-1 upper-case letters. The first letter refers to the
     * leftmost rotor setting (not counting the reflector).
     */
    void setRotors(String setting) {
        if (setting.length() != rotorr.length - 1) {
            String e = "Rotor Length doesn't match settings length";
            throw new EnigmaException(e);
        }
        for (int x = 0; x < setting.length(); x += 1) {
            rotorr[x + 1].set(setting.charAt(x));
        }
    }

    /**
     * Set the plugboard to PLUGBOARD.
     */
    void setPlugboard(Permutation plugboard) {
        _plugboarded = new Rotor("PB", plugboard);
    }

    /**
     * Returns the result of converting the input character C (as an
     * index in the range 0..alphabet size - 1), after first advancing
     * <p>
     * the machine.
     */
    int convert(int c) {
        int track = c;
        boolean rot1Rotated = false;
        ArrayList<Rotor> moved = new ArrayList<>();
        track = _plugboarded.convertForward(c);
        for (int x = 1; x <= numRotors() - 1; x += 1) {
            if (rotorr[x].atNotch()) {
                if (!moved.contains(rotorr[x - 1])) {
                    rotorr[x - 1].advance();
                    moved.add(rotorr[x - 1]);
                }
                if (rotorr[x].rotates() && rotorr[x - 1].rotates()) {
                    if (!moved.contains(rotorr[x])) {
                        rotorr[x].advance();
                        moved.add(rotorr[x]);
                    }
                    if (x == numRotors() - 1) {
                        rot1Rotated = true;
                    }
                }
            }
        }
        if (!rot1Rotated) {
            rotorr[numRotors() - 1].advance();
        }
        for (int x = numRotors() - 1; x >= 0; x--) {
            track = rotorr[x].convertForward(track);
        }
        for (int y = 1; y < numRotors(); y++) {
            track = rotorr[y].convertBackward(track);
        }
        track = _plugboarded.convertBackward(track);
        return track;
    }

    /**
     * Returns the encoding/decoding of MSG, updating the state of
     * the rotors accordingly.
     */
    String convert(String msg) {
        return "";
    }

    /**
     * Common alphabet of my rotors.
     */
    private final Alphabet _alphabet;
}
