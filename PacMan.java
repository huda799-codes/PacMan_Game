package PacmanGame;

public class PacMan {
    private int x;
    private int y;
    private int size;
    private String direction;

    public PacMan(int x,int y,int size) {
        this.x=x;
        this.y=y;
        this.size=size;
        direction="RIGHT";
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x=x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y=y;
    }

    public int getSize() {
        return size;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction=direction;
    }

    @Override
    public String toString() {
        return "PacMan x="+x+" y="+y+" size="+size+" direction="+direction;
    }
}