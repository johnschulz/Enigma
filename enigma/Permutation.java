package enigma;

/**
 * Represents a permutation of a range of integers starting at 0 corresponding
 * to the characters of an alphabet.
 *
 * @author John Schulz
 */
class Permutation {
    /**
     * Alphabet of this permutation.
     */
    private Alphabet _alphabet;
    /**
     * Instance variable for cycles.
     */
    private String _cycles;
    /**
     * Array list to hold circle.
     */
    private Cycle[] _cycling;

    /**
     * Set this Permutation to that specified by CYCLES, a string in the
     * form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     * is interpreted as a permutation in cycle notation.  Characters in the
     * alphabet that are not included in any cycle map to themselves.
     * Whitespace is ignored.
     */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = cycles;
        _cycling = new Cycle[size()];
        addCycle(_cycles);


    }

    /**
     * Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     * c0c1...cm.
     */
    private void addCycle(String cycle) {
        for (int x = 0; x < size(); x += 1) {
            Cycle tail = new Cycle(_alphabet.toChar(x));
            _cycling[x] = new Cycle(_alphabet.toChar(x), tail);
            Cycle a = _cycling[x]._next;
            a._next = _cycling[x];
            if (cycle.contains(String.valueOf(_alphabet.toChar(x)))) {
                int i = cycle.indexOf(_alphabet.toChar(x));
                char d = '(';
                char b = ')';
                char c = ' ';
                if (cycle.charAt(i + 1) == b) {
                    while (cycle.charAt(i) != d) {
                        i -= 1;
                    }
                }
                a._value = cycle.charAt(i + 1);
            }

        }

    }

    /**
     * Return the value of P modulo the size of this permutation.
     */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /**
     * Returns the size of the alphabet I permute.
     */
    int size() {
        return _alphabet.size();
    }

    /**
     * Return the result of applying this permutation to P modulo the
     * alphabet size.
     */
    int permute(int p) {
        Cycle perm = _cycling[p]._next;
        int alpha = _alphabet.toInt(perm._value);
        return alpha;
    }

    /**
     * Return the result of applying the inverse of this permutation
     * to  C modulo the alphabet size.
     */
    int invert(int c) {
        char find = _alphabet.toChar(c);
        Cycle toInv = new Cycle('A');
        for (int x = 0; x < _alphabet.size(); x += 1) {
            Cycle pos = _cycling[x]._next;
            if (pos._value == find) {
                toInv = pos;
            }
        }
        Cycle inverted = toInv._next;
        return _alphabet.toInt(inverted._value);
    }

    /**
     * Return the result of applying this permutation to the index of P
     * in ALPHABET, and converting the result to a character of ALPHABET.
     */
    char permute(char p) {

        return _alphabet.toChar(permute(_alphabet.toInt(p)));
    }

    /**
     * Return the result of applying the inverse of this permutation to C.
     */
    int invert(char c) {
        return _alphabet.toChar(invert(_alphabet.toInt(c)));
    }

    /**
     * Return the alphabet used to initialize this Permutation.
     */
    Alphabet alphabet() {
        return _alphabet;
    }

    /**
     * Return true iff this permutation is a derangement (i.e., a
     * permutation for which no value maps to itself).
     */
    boolean derangement() {
        boolean deranged = true;
        for (int x = 0; x < _alphabet.size() - 1; x += 1) {
            char a = _cycling[x]._value;
            Cycle next = _cycling[x]._next;
            char b = next._value;
            if (a == b) {
                deranged = false;
            }
        }
        return deranged;
    }

    /**
     * Class to initialize permutations and rotor elements.
     */
    public static class Cycle {
        /**
         * the Value represented as a char.
         */
        private char _value;
        /**
         * the next and previous day.
         */
        private Cycle _next;

        /**
         * Gets the value of the instance. @return the value
         */
        public char getValue() {
            return _value;
        }

        /**
         * Gets the next cycle of the instance. @return the next cycle
         */
        public Cycle getNext() {
            return _next;
        }

        /**
         * The initializer for Cycle with VALUE.
         */
        Cycle(char value) {
            _value = value;
            _next = null;
        }

        /**
         * Another initializer for Cycle with VALUE and NEXT.
         */
        Cycle(char value, Cycle next) {
            _value = value;
            _next = next;
        }
    }

}
