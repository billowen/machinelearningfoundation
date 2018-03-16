package quiz1;

public class Record {
    private double[] xs;
    private int y;

    public Record(double[] xs, int y) {
        this.xs = xs;
        this.y = y;
    }

    public double[] getXs() {
        return xs;
    }

    public void setXs(double[] xs) {
        this.xs = xs;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public String toString() {
        String msg = "";
        for (double x : xs) {
            msg += (x + " ");
        }
        msg += y;
        return msg;
    }
}
