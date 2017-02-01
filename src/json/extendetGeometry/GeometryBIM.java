package json.extendetGeometry;

/**
 * Created by boris on 01.02.17.
 */
public class GeometryBIM {
    private double minX;
    private double minY;
    private double maxX;
    private double maxY;
    private double width;
    private double length;

    public GeometryBIM() {
        this.minX = Double.MAX_VALUE;
        this.minY = Double.MAX_VALUE;
        this.maxX = -Double.MAX_VALUE;
        this.maxY = -Double.MAX_VALUE;
        this.width = 0;
        this.length = 0;
    }

    public double getMinX() {
        return minX;
    }

    public void setMinX(double minX) {
        this.minX = minX;
    }

    public double getMinY() {
        return minY;
    }

    public void setMinY(double minY) {
        this.minY = minY;
    }

    public double getMaxX() {
        return maxX;
    }

    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }

    public double getMaxY() {
        return maxY;
    }

    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }

    public double getWidth() {
        return maxY-minY;
    }

    public double getLength() {
        return maxX-minX;
    }

    @Override public String toString() {
        return "GeometryBIM {" + "minX=" + minX + ", minY=" + minY + ", maxX=" + maxX + ", maxY=" + maxY + ", width="
                + getWidth() + ", length=" + getLength() + '}';
    }
}
