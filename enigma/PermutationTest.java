package enigma;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author John Schulz
 */
public class PermutationTest {
    String a = "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)";
    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void checkClass() {

        Permutation c = new Permutation(a, UPPER);
    }
    @Test
    public void testPermute() {
        Permutation c = new Permutation(a, UPPER);
        assertEquals(4, c.permute(0));
    }
    @Test
    public void testInverted() {
        Permutation c = new Permutation(a, UPPER);
        assertEquals(0, c.invert(4));
        assertEquals(65, c.invert('E'));
        assertEquals(22, c.invert(1));
    }
    @Test
    public void testDerangement() {
        Permutation c = new Permutation(a, UPPER);
        assertFalse(c.derangement());
        String empty = "";
        Permutation arranged = new Permutation(empty, UPPER);
        assertFalse(arranged.derangement());
        String derange = "(ABCDEFGHIJKLMNOPQRSTUVWXYZ)";
        Permutation deranged = new Permutation(derange, UPPER);
        assertTrue(deranged.derangement());


    }
}
