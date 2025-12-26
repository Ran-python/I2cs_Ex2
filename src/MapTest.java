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
    public void setUp() {
        _m0 = new Map(3, 3, 0);
        _m1 = new Map(3, 3, 0);
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
        assertEquals(0, _m1.fill(p1,0, true));
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

    @Test
    void init_withNullArray_throwsRuntimeException() {
        assertThrows(RuntimeException.class, () -> _m0.init((int[][]) null));
    }

    @Test
    void getMap_returnsDeepCopy() {
        int[][] original = {{1, 2}, {3, 4}};
        Map map = new Map(original);

        int[][] copy = map.getMap();
        copy[0][0] = 99;

        assertEquals(1, map.getPixel(0, 0));
    }

    @Test
    void setPixel_updatesByCoordinatesAndObject() {
        Map map = new Map(2, 2, 0);
        map.setPixel(1, 1, 7);
        map.setPixel(new Index2D(0, 0), 5);

        assertEquals(7, map.getPixel(1, 1));
        assertEquals(5, map.getPixel(new Index2D(0, 0)));
    }

    @Test
    void isInside_correctlyDetectsBounds() {
        Map map = new Map(3, 3, 0);
        assertTrue(map.isInside(new Index2D(0, 0)));
        assertTrue(map.isInside(new Index2D(2, 2)));
        assertFalse(map.isInside(new Index2D(-1, 0)));
        assertFalse(map.isInside(new Index2D(3, 1)));
    }

    @Test
    void sameDimensions_detectsMismatch() {
        Map mapA = new Map(3, 3, 0);
        Map mapB = new Map(2, 3, 0);

        assertTrue(mapA.sameDimensions(new Map(3, 3, 0)));
        assertFalse(mapA.sameDimensions(mapB));
        assertThrows(RuntimeException.class, () -> mapA.sameDimensions(null));
    }

    @Test
    void mul_scalesAllEntries() {
        Map map = new Map(new int[][]{
                {1, -1},
                {2, 3}
        });

        map.mul(2.5);

        assertArrayEquals(new int[][]{
                {2, -2},
                {5, 7}
        }, map.getMap());
    }

    @Test
    void rescale_nearestNeighborCopiesExistingValues() {
        Map map = new Map(new int[][]{
                {1, 2},
                {3, 4}
        });

        map.rescale(2, 1);

        assertArrayEquals(new int[][]{
                {1, 2},
                {1, 2},
                {3, 4},
                {3, 4}
        }, map.getMap());
    }

    @Test
    void drawCircle_colorsPixelsWithinRadius() {
        Map map = new Map(5, 5, 0);
        map.drawCircle(new Index2D(2, 2), 1, 7);

        assertEquals(7, map.getPixel(2, 2));
        assertEquals(7, map.getPixel(1, 2));
        assertEquals(7, map.getPixel(3, 2));
        assertEquals(7, map.getPixel(2, 1));
        assertEquals(7, map.getPixel(2, 3));
        assertEquals(0, map.getPixel(0, 0));
    }

    @Test
    void drawLine_drawsDiagonalPixels() {
        Map map = new Map(4, 4, 0);
        map.drawLine(new Index2D(0, 0), new Index2D(3, 3), 9);

        assertEquals(9, map.getPixel(0, 0));
        assertEquals(9, map.getPixel(1, 1));
        assertEquals(9, map.getPixel(2, 2));
        assertEquals(9, map.getPixel(3, 3));
        assertEquals(0, map.getPixel(0, 1));
    }

    @Test
    void drawRect_fillsInclusiveArea() {
        Map map = new Map(3, 3, 0);
        map.drawRect(new Index2D(0, 0), new Index2D(1, 1), 5);

        assertEquals(5, map.getPixel(0, 0));
        assertEquals(5, map.getPixel(1, 1));
        assertEquals(0, map.getPixel(2, 2));
    }

    @Test
    void fill_nonCyclic_respectsBoundaries() {
        Map map = new Map(new int[][]{
                {1, 1, 1},
                {1, 0, 1},
                {1, 1, 1}
        });

        int filled = map.fill(new Index2D(1, 1), 2, false);

        assertEquals(1, filled);
        assertEquals(2, map.getPixel(1, 1));
        assertEquals(1, map.getPixel(0, 0));
    }

    @Test
    void fill_cyclic_wrapsAcrossEdges() {
        Map map = new Map(new int[][]{
                {0, 1},
                {0, 0}
        });

        int filled = map.fill(new Index2D(-1, 0), 3, true);

        assertEquals(3, filled);
        assertEquals(3, map.getPixel(1, 0));
        assertEquals(3, map.getPixel(1, 1));
        assertEquals(3, map.getPixel(0, 0));
        assertEquals(1, map.getPixel(0, 1));
    }

    @Test
    void shortestPath_avoidsObstacles() {
        Map map = new Map(new int[][]{
                {0, 1, 0},
                {0, 1, 0},
                {0, 0, 0}
        });

        Pixel2D start = new Index2D(0, 0);
        Pixel2D end = new Index2D(0, 2);

        Pixel2D[] path = map.shortestPath(start, end, 1, false);

        assertNotNull(path);
        assertEquals(7, path.length);
        assertEquals(start, path[0]);
        assertEquals(end, path[path.length - 1]);
        for (Pixel2D step : path) {
            assertNotEquals(1, map.getPixel(step));
        }
    }

    @Test
    void allDistance_marksUnreachableCells() {
        Map map = new Map(new int[][]{
                {0, 0, 0},
                {1, 1, 1},
                {0, 0, 0}
        });

        Map2D distances = map.allDistance(new Index2D(0, 0), 1, false);

        assertEquals(0, distances.getPixel(0, 0));
        assertEquals(1, distances.getPixel(0, 1));
        assertEquals(2, distances.getPixel(0, 2));
        assertEquals(-1, distances.getPixel(2, 0));
        assertEquals(-1, distances.getPixel(2, 2));
    }
}