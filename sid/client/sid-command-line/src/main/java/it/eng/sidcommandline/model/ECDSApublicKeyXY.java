package it.eng.sidcommandline.model;

public class ECDSApublicKeyXY {

    private String x;
    private String y;

    public ECDSApublicKeyXY(String x, String y) {
        this.x = x;
        this.y = y;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }
}
