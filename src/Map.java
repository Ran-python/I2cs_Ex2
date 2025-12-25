import java.io.Serializable;
/**
 * This class represents a 2D map (int[w][h]) as a "screen" or a raster matrix or maze over integers.
 * This is the main class needed to be implemented.
 *
 * @author boaz.benmoshe
 *
 */
public class Map implements Map2D, Serializable{
private int [][] _map ;
    // edit this class below
	/**
	 * Constructs a w*h 2D raster map with an init value v.
	 * @param w
	 * @param h
	 * @param v
	 */
	public Map(int w, int h, int v) {init(w, h, v);}

    /**
	 * Constructs a square map (size*size).
	 * @param size
	 */
	public Map(int size) {this(size,size, 0);}
	
	/**
	 * Constructs a map from a given 2D array.
	 * @param data
	 */
	public Map(int[][] data) {
		init(data);
	}
	@Override
	public void init(int w, int h, int v) {
        if (w <= 0 || h <= 0) {
            throw new RuntimeException("Invalid map size");
        }

        this._map = new int[w][h];

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                this._map[i][j] = v;
            }
        }
	}
	@Override
	public void init(int[][] arr) {
        // check null
        if (arr == null) {
            throw new RuntimeException("Array is null");
        }

        // check empty array
        if (arr.length == 0 || arr[0] == null || arr[0].length == 0) {
            throw new RuntimeException("Array is empty");
        }

        int w = arr.length;
        int h = arr[0].length;

        // check ragged array
        for (int i = 0; i < w; i++) {
            if (arr[i] == null || arr[i].length != h) {
                throw new RuntimeException("Ragged 2D array");
            }
        }

        // deep copy
        this._map = new int[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                this._map[i][j] = arr[i][j];
            }
        }
    }
	@Override
	public int[][] getMap() {
        if (this._map == null) {
            return null;
        }

        int w = this.getWidth();
        int h = this.getHeight();

        int [][] deepCopy = new int[w][h];
    for(int i=0; i<w;i++){
        for(int j=0;j<h;j++){
    deepCopy[i][j]= this._map[i][j];

        }
    }

		return deepCopy;
	}
	@Override
	public int getWidth() {
        return this._map.length;
    }

	@Override
	public int getHeight() {
        return this._map[0].length;
    }
   // x are rows and y are columns
	@Override
	public int getPixel(int x, int y) {
        if(x<0 || y<0 || x>=this.getWidth() || y>= this.getHeight()){
            throw new RuntimeException("out of bounds");
        }
        return this._map[x][y];
    }
	@Override
	public int getPixel(Pixel2D p) {
        if (p==null) {
            throw new RuntimeException("p is null");
        }
        if (p.getX() < 0 || p.getY() < 0 ||
                p.getX() >= this.getWidth() || p.getY() >= this.getHeight()) {
            throw new RuntimeException("Pixel out of bounds: " + p);
        }

        // Index2D other = (Index2D) p;
        return this._map[p.getX()][p.getY()];
    }

	@Override
	public void setPixel(int x, int y, int v) {

        if (_map == null) {
            throw new RuntimeException("Map is not initialized");
        }

        if (x < 0 || y < 0 || x >= this.getWidth() || y >= this.getHeight()) {
            throw new RuntimeException(
                    "Pixel out of bounds: (" + x + "," + y + ")"
            );
        }

        this._map[x][y] = v;
    }

	@Override
	public void setPixel(Pixel2D p, int v) {
        if (p==null) {
            throw new RuntimeException("p is null");
        }
        if (p.getX() < 0 || p.getY() < 0 ||
                p.getX() >= this.getWidth() || p.getY() >= this.getHeight()) {
            throw new RuntimeException("Pixel out of bounds: " + p);
        }
        this._map[p.getX()][p.getY()] = v;
	}

    @Override
    public boolean isInside(Pixel2D p) {
        if (p == null) {
            throw new RuntimeException("p is null");
        }
        if (p.getX() < 0 || p.getY() < 0 ||
                p.getX() >= getWidth() || p.getY() >= getHeight()) {
            return false;
        }
        return true;
    }
    @Override
    public boolean sameDimensions(Map2D p) {
    if (p==null){
        throw new RuntimeException("p is null");
    }
    int ph = p.getHeight();
    int th = this.getHeight();
    int pw = p.getWidth();
    int tw = this.getWidth();

        return ph == th && pw == tw;
    }

    @Override
    public void addMap2D(Map2D p) {
    if (!this.sameDimensions(p)){
        return;
    }
        for(int x =0; x<this.getWidth();x++){
            for(int y = 0;y<this.getHeight();y++){
                this._map[x][y]+= p.getPixel(x,y);
        }
    }
    }

    @Override
    public void mul(double scalar) {
        for(int x =0; x<this.getWidth();x++) {
            for (int y = 0; y < this.getHeight(); y++) {
                this._map[x][y] =(int)(this._map[x][y] * scalar);
            }
        }
    }

    @Override
    public void rescale(double sx, double sy) {
        // 1) validate
        if (sx <= 0 || sy <= 0) {
            throw new IllegalArgumentException("Scale factors must be positive: sx=" + sx + ", sy=" + sy);
        }
        if (this._map == null) {
            // אין מה לשנות
            return;
        }

        int oldW = getWidth();
        int oldH = getHeight();

        // 2) compute new size (use round to match "100*1.2 = 120" nicely)
        int newW = (int) Math.round(oldW * sx);
        int newH = (int) Math.round(oldH * sy);

        // avoid zero-sized maps
        if (newW <= 0) newW = 1;
        if (newH <= 0) newH = 1;

        // 3) keep old data
        int[][] oldMap = this._map; // אפשר גם getMap(), אבל זה יוצר העתקה מיותרת

        // 4) allocate new map
        int[][] newMap = new int[newW][newH];

        // 5) nearest-neighbor mapping:
        // new[x][y] comes from old[ floor(x/sx) ][ floor(y/sy) ]
        // clamp to avoid out-of-bounds due to rounding edge cases
        for (int x = 0; x < newW; x++) {
            int ox = (int) Math.floor(x / sx);
            if (ox < 0) ox = 0;
            if (ox >= oldW) ox = oldW - 1;

            for (int y = 0; y < newH; y++) {
                int oy = (int) Math.floor(y / sy);
                if (oy < 0) oy = 0;
                if (oy >= oldH) oy = oldH - 1;

                newMap[x][y] = oldMap[ox][oy];
            }
        }

        // 6) commit
        this._map = newMap;
    }

    @Override
    public void drawCircle(Pixel2D center, double rad, int color) {
        if (center == null) {
            throw new RuntimeException("center is null");
        }
        if (rad < 0) {
            throw new RuntimeException("rad is negative: " + rad);
        }
        if (this._map == null) {
            throw new RuntimeException("map is null");
        }

    int cx = center.getX();
    int cy = center.getY();
    double r2 = rad*rad;

    for(int x=0;x<this.getWidth();x++) {
        for (int y = 0; y<this.getHeight(); y++){
            long dx = (long) x-cx;
            long dy = (long) y-cy;
            long dist = dx*dx+dy*dy;
            if(dist<=r2){
                setPixel(x,y,color);
            }
            }
        }
    }

    @Override
    public void drawLine(Pixel2D p1, Pixel2D p2, int color) {
        //   if (this.isInside(p1)&&this.isInside(p2)) {
            int x1 = p1.getX();
            int x2 = p2.getX();
            int y1 = p1.getY();
            int y2 = p2.getY();

            if (p1.equals(p2)){
                setPixel(x1,y1,color);
            return;
            }

        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);

        if(dx>=dy){
                if (x1>x2){
                    int tempX = x1;
                    int tempY = y1;
                    x1=x2;
                    y1=y2;
                    x2=tempX;
                    y2=tempY;
                }
                double m = (double) (y2 - y1) /(double) (x2 - x1);
                for (int x=x1;x<=x2;x++){
                    double yReal = y1 + m * (x - x1);
                    int y = (int) Math.round(yReal);
                    setPixel(x,y,color);
            }
            } else{
            if(y1>y2){
                int tempX = x1;
                int tempY = y1;
                x1=x2;
                y1=y2;
                x2=tempX;
                y2=tempY;
                dx = Math.abs(x2 - x1);
                dy = Math.abs(y2 - y1);
            }
                double k = (double) (x2 - x1) /(double) (y2 - y1);
                for (int y=y1;y<=y2;y++) {
                    double xReal = x1 + k * (y - y1);
                    int x = (int) Math.round(xReal);
                    setPixel(x, y, color);
                }
        }
    }


    @Override
    public void drawRect(Pixel2D p1, Pixel2D p2, int color) {
        if (p1 == null || p2 == null) {
            throw new RuntimeException("p1 or p2 is null");
        }
        if (!this.isInside(p1)||!this.isInside(p2)) {
            throw new RuntimeException("p1 or p2 is out of bounds");
        }

        int x1 = p1.getX();
        int x2 = p2.getX();
        int y1 = p1.getY();
        int y2 = p2.getY();

        if(x1<x2){
            int tempX=x1;
            x1=x2;
            x2=tempX;
        }
        if(y1<y2){
            int tempY=y1;
            y1=y2;
            y2=tempY;
        }
        for (int x=x2;x<=x1;x++){
            for (int y=y2;y<=y1;y++){
                setPixel(x,y,color);
            }
        }
    }

    @Override
    public boolean equals(Object ob) {
        if(this == ob){
            return true;
        }
        if (!(ob instanceof Map)){
            return false;
        }
        Map other = (Map) ob;
        if(!this.sameDimensions(other)){
            return false;
        }
        for(int x =0; x<this.getWidth();x++) {
            for (int y = 0; y < this.getHeight(); y++) {
            if (this._map[x][y] != other._map[x][y]){
                return false;
                    }
                }
            }

        return true;
    }
	@Override
	/** 
	 * Fills this map with the new color (new_v) starting from p.
	 * https://en.wikipedia.org/wiki/Flood_fill
	 */
	public int fill(Pixel2D xy, int new_v,  boolean cyclic) {
		int ans = -1;

		return ans;
	}

	@Override
	/**
	 * BFS like shortest the computation based on iterative raster implementation of BFS, see:
	 * https://en.wikipedia.org/wiki/Breadth-first_search
	 */
	public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor, boolean cyclic) {
		Pixel2D[] ans = null;  // the result.

		return ans;
	}
    @Override
    public Map2D allDistance(Pixel2D start, int obsColor, boolean cyclic) {
        Map2D ans = null;  // the result.

        return ans;
    }
	////////////////////// Private Methods ///////////////////////

}
