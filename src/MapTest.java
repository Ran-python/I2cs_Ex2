import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;
/**
 * Intro2CS, 2026A, this is a very
 */
class MapTest {
    /**
     */
    private int[][] _map_3_3 = {{0,1,0}, {1,0,1}, {0,1,0}};
    private Map2D _m0, _m1, _m3_3;
    @BeforeEach
    public void setuo() {
        _m3_3 = new Map(_map_3_3);
    }
    @Test
    @Timeout(value = 1, unit = SECONDS)
    void init() {
        int[][] bigarr = new int [500][500];
        _m1.init(bigarr);
        assertEquals(bigarr.length, _m1.getWidth());
        assertEquals(bigarr[0].length, _m1.getHeight());
        Pixel2D p1 = new Index2D(3,2);
        _m1.fill(p1,1, true);
    }

    @Test
    void testInit() {
        _m0.init(_map_3_3);
        _m1.init(_map_3_3);
        assertEquals(_m0, _m1);
    }
    @Test
    void testEquals() {
        assertEquals(_m0,_m1);
        _m0.init(_map_3_3);
        _m1.init(_map_3_3);
        assertEquals(_m0,_m1);
    }
    /// my tests
    @Test
    void addMap2D_addsElementWise_correctly() {
        // this map
        Map a = new Map(new int[][]{
                {1, 2, 3},
                {4, 5, 6}
        });

        // map to add
        Map b = new Map(new int[][]{
                {10, 20, 30},
                {40, 50, 60}
        });

        a.addMap2D(b);

        assertArrayEquals(new int[][]{
                {11, 22, 33},
                {44, 55, 66}
        }, a.getMap());
    }

    @Test
    void addMap2D_supportsNegativeNumbers() {
        Map a = new Map(new int[][]{
                {5, -2},
                {0, 7}
        });

        Map b = new Map(new int[][]{
                {-1, 2},
                {-3, -10}
        });

        a.addMap2D(b);

        assertArrayEquals(new int[][]{
                {4, 0},
                {-3, -3}
        }, a.getMap());
    }

    @Test
    void addMap2D_doesNotModifyInputMap() {
        Map a = new Map(new int[][]{
                {1, 1},
                {1, 1}
        });

        Map b = new Map(new int[][]{
                {2, 3},
                {4, 5}
        });

        int[][] before = b.getMap();   // deep copy snapshot
        a.addMap2D(b);
        int[][] after = b.getMap();    // should be identical

        assertArrayEquals(before, after);
    }

    @Test
    void addMap2D_differentDimensions_doesNothing() {
        Map a = new Map(new int[][]{
                {1, 2, 3},
                {4, 5, 6}
        });

        Map b = new Map(new int[][]{
                {100, 200},
                {300, 400}
        });

        int[][] before = a.getMap();
        a.addMap2D(b);
        int[][] after = a.getMap();

        assertArrayEquals(before, after);
    }

    @Test
    void addMap2D_null_throwsRuntimeException() {
        Map a = new Map(2, 2, 0);
        assertThrows(RuntimeException.class, () -> a.addMap2D(null));
    }
}