public class Index2D implements Pixel2D {
    /** X coordinate of the pixel */
    private int _x;

    /** Y coordinate of the pixel */
    private int _y;

    /**
     * Constructs a new Index2D with the given x and y coordinates.
     *
     * @param w the x coordinate
     * @param h the y coordinate
     */
    public Index2D(int w, int h) {
        this._x = w;
        this._y = h;
    }

    /**
     * Copy constructor.
     * Creates a new Index2D from another Pixel2D object.
     *
     * @param other the Pixel2D object to copy from
     * @throws IllegalArgumentException if other is null
     */
    public Index2D(Pixel2D other) {
        if (other == null) {
            throw new IllegalArgumentException("other is null");
        }
        this._x = other.getX();
        this._y = other.getY();
    }
    /**
     * Returns the x coordinate of this pixel.
     *
     * @return the x coordinate
     */
    @Override
    public int getX() {

        return this._x;
    }
    /**
     * Returns the y coordinate of this pixel.
     *
     * @return the y coordinate
     */
    @Override
    public int getY() {

        return this._y;
    }

    /**
     * Computes the Euclidean distance between this pixel
     * and another Pixel2D.
     *
     * @param p2 the other pixel
     * @return the distance between the two pixels
     * @throws RuntimeException if p2 is null
     */
    @Override
    public double distance2D(Pixel2D p2) {
        if (p2 == null) {
            throw new RuntimeException("p2 is null");
        }
        double dx = this._x - p2.getX();
        double dy = this._y - p2.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }
    /**
     * Returns a string representation of this Index2D
     * in the format "(x,y)".
     *
     * @return string representation of the pixel
     */
    @Override
    public String toString() {

        return "(" + _x + "," + _y + ")";
    }

    /**
     * Compares this Index2D with another object for equality.
     * Two Index2D objects are equal if they have the same
     * x and y coordinates.
     *
     * @param p the object to compare with
     * @return true if the objects represent the same coordinates,
     *         false otherwise
     */
    @Override
    public boolean equals(Object p) {
        if (this == p) {
            return true;
        }// same object in memory
        if (p == null || !(p instanceof Index2D)) {
            return false;
        }

        Index2D other = (Index2D) p;// safe cast
        return this._x == other.getX() && this._y == other.getY();
    }
}
