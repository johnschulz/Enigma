package enigma;

/**
 * Class that represents a rotating rotor in the enigma machine.
 *
 * @author John Schulz
 */
class MovingRotor extends Rotor {
    /** The letters which represent the rotors notches.*/
    private String _notches;
    /** Returns the letters which corespond to the notches.*/
    public String getNotches() {
        return _notches;
    }
    /** The set for this moving rotor.*/
    private int _set = 0;
    /**
     * A rotor named NAME whose permutation in its default setting is
     * PERM, and whose notches are at the positions indicated in NOTCHES.
     * The Rotor is initally in its 0 setting (first character of its
     * alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
    }

    /** Set setting() to POSN.  */
    void set(int posn) {
        _set = posn;
    }

    /** Set setting() to character CPOSN. */
    void set(char cposn) {
        _set = permutation().alphabet().toInt(cposn);
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    int convertForward(int p) {
        int orig = getPermutation().wrap(p + _set);
        int input = getPermutation().permute(orig);
        int output = getPermutation().wrap(input - _set);
        return output;
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        int orig = getPermutation().wrap(e + _set);
        int input = getPermutation().invert(orig);
        int output = getPermutation().wrap(input - _set);
        return output;
    }
    @Override
    void advance() {
        _set = (_set + 1) % getPermutation().size();
    }

    @Override()
    boolean rotates() {
        return true;
    }

    @Override()
    boolean atNotch() {
        Alphabet a = getPermutation().alphabet();
        for (int x = 0; x < _notches.length(); x++) {
            if (_set == a.toInt(_notches.charAt(x))) {
                return true;
            }
        }
        return false;
    }
}
