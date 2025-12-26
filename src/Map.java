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
	 *
     *  Fills (paints) the connected component of a given pixel with a new color.
     *
     *  The connected component of a pixel p consists of all pixels that:
     *  - have the same original color as p
     *  - are connected to p using 4-neighbors (up, down, left, right)
     *
     *  If cyclic is true, the map is treated as cyclic (crossing one border enters
     *  from the opposite side). If cyclic is false, pixels outside the map are ignored
     *  and the starting pixel must be inside the map.
     *
     *  If the color of the starting pixel is already new_v, no filling is performed
     *  and the method returns 0.
     *
     *  @param xy the starting pixel for the fill operation
     *  @param new_v the new color to apply
     *  @param cyclic true if the map is cyclic, false otherwise
     *  @return the number of pixels that were filled
     */

	public int fill(Pixel2D xy, int new_v,  boolean cyclic) {
        if (xy == null) {
            throw new RuntimeException("pixel is null");
        }
        if (this._map == null) {
            throw new RuntimeException("map is null");
        }

        int w = getWidth();
        int h = getHeight();

        int x = xy.getX();
        int y = xy.getY();

        // אם לא מחזורי - חייב להיות בתוך המפה
        if (!cyclic) {
            if (!this.isInside(xy)) {
                throw new RuntimeException("out of bounds");
            }
        } else {
            // מחזורי: עיטוף נקודת התחלה (מספיק step אחד כי השכנים הם ±1)
            if (x < 0) x = w - 1;
            if (x >= w) x = 0;
            if (y < 0) y = h - 1;
            if (y >= h) y = 0;
        }

        int oldColor = this._map[x][y];
        if (oldColor == new_v) {
            return 0;
        }

        return fillDfs(x, y, oldColor, new_v, cyclic);
    }

    @Override
	/**
	 * BFS like shortest the computation based on iterative raster implementation of BFS, see:
	 * https://en.wikipedia.org/wiki/Breadth-first_search
	 */
	public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor, boolean cyclic) {
        if (p1 == null||p2==null){
            throw new RuntimeException("p1 or p2 is null");
        }
        int w = getWidth();
        int h = getHeight();

        int x1 = p1.getX();
        int y1 = p1.getY();
        int x2 = p2.getX();
        int y2 = p2.getY();

        if (!cyclic) {
            if (!this.isInside(p1) || !this.isInside(p2)) {
                throw new RuntimeException("out of bounds");
            }
        }else{
            x1 = wrap(x1, w);
            y1 = wrap(y1, h);
            x2 = wrap(x2, w);
            y2 = wrap(y2, h);
        }

        if (_map[x1][y1] == obsColor || _map[x2][y2] == obsColor) {
            return null;
        }

        Map2D distMap = allDistance(new Index2D(x1, y1), obsColor, cyclic);
        int endDist = distMap.getPixel(x2, y2);
        if (endDist < 0) {
            return null;
        }

        Pixel2D[] path = new Pixel2D[endDist + 1];
        int cx = x2;
        int cy = y2;
        int currDist = endDist;

        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};

        while (currDist >= 0) {
            path[currDist] = new Index2D(cx, cy);
            if (currDist == 0) {
                break;
            }

            boolean foundPrev = false;
            for (int k = 0; k < 4 && !foundPrev; k++) {
                int nx = cx + dx[k];
                int ny = cy + dy[k];

                if (cyclic) {
                    nx = wrap(nx, w);
                    ny = wrap(ny, h);
                } else {
                    if (nx < 0 || nx >= w || ny < 0 || ny >= h) {
                        continue;
                    }
                }

                if (distMap.getPixel(nx, ny) == currDist - 1) {
                    cx = nx;
                    cy = ny;
                    currDist--;
                    foundPrev = true;
                }
            }

            if (!foundPrev) {
                // Should not happen if distances are consistent
                return null;
            }
        }

        return path;
    }

    @Override
    public Map2D allDistance(Pixel2D start, int obsColor, boolean cyclic) {
        if (start == null) {
            throw new RuntimeException("start is null");
        }

        int w = getWidth();
        int h = getHeight();
        int sx = start.getX();
        int sy = start.getY();

        if (!cyclic) {
            if (!isInside(start)) {
                throw new RuntimeException("start out of bounds");
            }
        } else {
            sx = wrap(sx, w);
            sy = wrap(sy, h);
        }

        Map result = new Map(w, h, -1);
        if (_map[sx][sy] == obsColor) {
            return result;
        }

        int[][] dist = result._map;

        int maxSize = w * h;
        int[] qx = new int[maxSize];
        int[] qy = new int[maxSize];
        int head = 0;
        int tail = 0;

        dist[sx][sy] = 0;
        qx[tail] = sx;
        qy[tail] = sy;
        tail++;

        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};

        while (head < tail) {
            int cx = qx[head];
            int cy = qy[head];
            head++;

            for (int k = 0; k < 4; k++) {
                int nx = cx + dx[k];
                int ny = cy + dy[k];

                if (cyclic) {
                    nx = wrap(nx, w);
                    ny = wrap(ny, h);
                } else {
                    if (nx < 0 || nx >= w || ny < 0 || ny >= h) {
                        continue;
                    }
                }

                if (_map[nx][ny] == obsColor) {
                    continue;
                }
                if (dist[nx][ny] != -1) {
                    continue;
                }

                dist[nx][ny] = dist[cx][cy] + 1;
                qx[tail] = nx;
                qy[tail] = ny;
                tail++;
            }
        }

        return result;
    }

	////////////////////// Private Methods ///////////////////////

    /**
     * Recursively fills the connected component using a depth-first search (DFS).
     *
     * Starting from the pixel at coordinates (x, y), this method:
     * - checks boundary conditions (or wraps coordinates if cyclic is true)
     * - verifies that the pixel has the original color oldColor
     * - paints the pixel with the new color new_v
     * - continues the fill process on the four neighboring pixels
     *
     * The coloring itself is used as a "visited" marker to prevent infinite recursion.
     *
     * @param x the x-coordinate of the current pixel
     * @param y the y-coordinate of the current pixel
     * @param oldColor the original color of the connected component
     * @param new_v the new color to apply
     * @param cyclic true if the map is cyclic, false otherwise
     * @return the number of pixels filled by this call and its recursive calls
     */
    private int fillDfs(int x, int y, int oldColor, int new_v, boolean cyclic) {
        int w = getWidth();
        int h = getHeight();

        // עטיפה / בדיקת גבולות
        if (cyclic) {
            if (x < 0) x = w - 1;
            if (x >= w) x = 0;
            if (y < 0) y = h - 1;
            if (y >= h) y = 0;
        } else {
            if (x < 0 || x >= w || y < 0 || y >= h) {
                return 0;
            }
        }

        // אם זה לא הצבע המקורי - לא שייך לרכיב המחובר
        if (this._map[x][y] != oldColor) {
            return 0;
        }

        // צביעה = סימון visited
        this._map[x][y] = new_v;
        int count = 1;

        // 4 שכנים
        count += fillDfs(x + 1, y, oldColor, new_v, cyclic);
        count += fillDfs(x - 1, y, oldColor, new_v, cyclic);
        count += fillDfs(x, y + 1, oldColor, new_v, cyclic);
        count += fillDfs(x, y - 1, oldColor, new_v, cyclic);

        return count;
    }

/**
 * Wraps a coordinate into the range [0, limit-1].
 */
private int wrap(int value, int limit) {
    if (value < 0) {
        return limit - 1;
    }
    if (value >= limit) {
        return 0;
    }
    return value;
}

}
