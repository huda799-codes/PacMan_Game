package PacmanGame;

public class Ghost {
    private int x;
    private int y;
    private int size;
    private String type;

    public Ghost(int x,int y,int size,String type) {
        this.x=x;
        this.y=y;
        this.size=size;
        this.type=type;
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

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Ghost x="+x+" y="+y+" type="+type;
    }
}