package enigma;

import static enigma.EnigmaException.error;
/**
 * An alphabet created through a String
 * of characters instead of two chars
 * first and last in the CharacterRange class.
 *
 * @author John Schulz
 */
public class Characters extends Alphabet {
    /** All the characters in the alphabet.*/
    private String _all;
    /** Returns all the characters in the alphabet.*/
    public String getAll() {
        return _all;
    }
    /** The characters and their respective integer values stored in an array.*/
    private Alpha[] _cycling;
    /** Returns the array cycling. */
    public Alpha[] getCycling() {
        return _cycling;
    }
    /**
     * An alphabet consisting of all characters in CHARS
     * inclusive.
     */
    Characters(String chars) {
        _all = chars;
        _cycling = new Alpha[chars.length()];
        for (int x = 0; x < chars.length(); x += 1) {
            _cycling[x] = new Alpha(chars.charAt(x), x);
        }
    }


    @Override
    int size() {
        return _all.length();
    }

    @Override
    boolean contains(char ch) {
        for (int x = 0; x < size(); x += 1) {
            if (_cycling[x]._letter == ch) {
                return true;
            }
        }
        return false;
    }

    @Override
    char toChar(int index) {
        if (index < 0 || index >= size()) {
            throw error("character index out of range");
        }
        return _cycling[index]._letter;
    }

    @Override
    int toInt(char ch) {
        if (!contains(ch)) {
            throw error("character out of range");
        }
        int ret = 0;
        for (int x = 0; x < size(); x += 1) {
            if (_cycling[x]._letter == ch) {
                ret = x;
            }
        }
        return ret;
    }

    /**
     * Range of characters in this Alphabet.
     */
    private char _first, _last;
    /**
     * Class to initialize permutations and rotor elements.
     */
    public static class Alpha {
        /**
         * the Value represented as a char.
         */
        private int _value;
        /**
         * the next and previous day.
         */
        private char _letter;

        /**
         * Gets the value of the instance. @return the value
         */
        public int getValue() {
            return _value;
        }

        /**
         * Gets the next cycle of the instance. @return the next cycle
         */
        public char getNext() {
            return _letter;
        }


        /**
         * Another initializer for Cycle with VALUE and LETTER.
         */
        Alpha(char letter, int value) {
            _value = value;
            _letter = letter;
        }
    }

}

