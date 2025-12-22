import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class Index2DTest {


    @Test
    void getX() {
        Index2D p1 = new Index2D(3, 7);
        Index2D p2 = new Index2D(-5, 0);

        assertEquals(3, p1.getX());
        assertEquals(-5, p2.getX());
        assertNotEquals(7, p1.getX());   // make sure Y is not returned by mistake
    }

    @Test
    void getY() {
        Index2D p1 = new Index2D(3, 7);
        Index2D p2 = new Index2D(0, -4);

        assertEquals(7, p1.getY());
        assertEquals(-4, p2.getY());
        assertNotEquals(3, p1.getY());   // make sure X is not returned
    }

    @Test
    void distance2D() {
        Index2D a = new Index2D(0, 0);
        Index2D b = new Index2D(3, 4);
        Index2D c = new Index2D(0, 5);

        assertEquals(5.0, a.distance2D(b), 0.0001); // classic 3-4-5
        assertEquals(5.0, a.distance2D(c), 0.0001); // vertical distance
        assertEquals(0.0, a.distance2D(a), 0.0001); // distance to itself
    }

    @Test
    void testToString() {
        Index2D p1 = new Index2D(1, 2);
        Index2D p2 = new Index2D(0, 0);
        Index2D p3 = new Index2D(-3, 4);

        assertEquals("(1,2)", p1.toString());
        assertEquals("(0,0)", p2.toString());
        assertEquals("(-3,4)", p3.toString());
    }

    @Test
    void testEquals() {
        Index2D a = new Index2D(1, 2);
        Index2D b = new Index2D(1, 2);
        Index2D c = new Index2D(2, 1);

        assertEquals(a, b);              // same values
        assertNotEquals(a, c);           // different values
        assertNotEquals(a, null);        // null safety
        assertNotEquals(a, "Index2D");   // type safety
        assertEquals(a, a);              // reflexive
    }
}