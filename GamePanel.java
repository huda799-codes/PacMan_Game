package PacmanGame;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements KeyListener,ActionListener {

    private char[][] map;
    private PacMan pacman;
    private ArrayList<Ghost> ghosts;

    private int score;
    private int lives;

    private boolean gameStarted;
    private boolean paused;
    private boolean gameOver;

    private boolean frightenedMode;
    private long frightenedEndTime;

    private Timer timer;
    private Random random;

    private Image pacmanLeft;
    private Image pacmanRight;
    private Image pacmanUp;
    private Image pacmanDown;

    private Image blueGhost;
    private Image pinkGhost;
    private Image orangeGhost;
    private Image scaredGhost;

    private Image cherry;
    private Image cherry2;
    private Image wallImage;

    private final int frightenedTime=8000;

    public GamePanel() {
        MapData data=new MapData();
        map=copyMap(data.getMap());

        pacman=new PacMan(13*MapData.TILE_SIZE,9*MapData.TILE_SIZE,24);

        ghosts=new ArrayList<>();
        ghosts.add(new Ghost(26*MapData.TILE_SIZE,7*MapData.TILE_SIZE,24,"BLUE"));
        ghosts.add(new Ghost(24*MapData.TILE_SIZE,7*MapData.TILE_SIZE,24,"PINK"));
        ghosts.add(new Ghost(22*MapData.TILE_SIZE,7*MapData.TILE_SIZE,24,"ORANGE"));

        score=0;
        lives=3;

        gameStarted=false;
        paused=false;
        gameOver=false;
        frightenedMode=false;
        frightenedEndTime=0;

        random=new Random();

        loadImages();

        setPreferredSize(new Dimension(map[0].length*MapData.TILE_SIZE,map.length*MapData.TILE_SIZE+80));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        timer=new Timer(250,this);
    }

    private char[][] copyMap(char[][] oldMap) {
        char[][] newMap=new char[oldMap.length][];
        for(int i=0;i<oldMap.length;i++) {
            newMap[i]=oldMap[i].clone();
        }
        return newMap;
    }

    private void loadImages() {
        try {
            pacmanLeft=ImageIO.read(new File("pics/pacmanLeft.png"));
            pacmanRight=ImageIO.read(new File("pics/pacmanRight.png"));
            pacmanUp=ImageIO.read(new File("pics/pacmanUp.png"));
            pacmanDown=ImageIO.read(new File("pics/pacmanDown.png"));

            blueGhost=ImageIO.read(new File("pics/blueGhost.png"));
            pinkGhost=ImageIO.read(new File("pics/pinkGhost.png"));
            orangeGhost=ImageIO.read(new File("pics/orangeGhost.png"));
            scaredGhost=ImageIO.read(new File("pics/scaredGhost.png"));

            cherry=ImageIO.read(new File("pics/cherry.png"));
            cherry2=ImageIO.read(new File("pics/cherry2.png"));

            wallImage=ImageIO.read(new File("pics/wall.png"));

            System.out.println("images loaded");
        } catch(Exception e) {
            System.out.println("problem in loading images");
            e.printStackTrace();
        }
    }

    private boolean isWall(int x,int y) {
        int col=x/MapData.TILE_SIZE;
        int row=y/MapData.TILE_SIZE;

        if(row<0||row>=map.length||col<0||col>=map[row].length) {
            return true;
        }

        return map[row][col]=='W';
    }

    private void startFrightenedMode() {
        frightenedMode=true;
        frightenedEndTime=System.currentTimeMillis()+frightenedTime;
    }

    private void updateFrightenedMode() {
        if(frightenedMode&&System.currentTimeMillis()>frightenedEndTime) {
            frightenedMode=false;
        }
    }

    private void eatFood() {
        int col=pacman.getX()/MapData.TILE_SIZE;
        int row=pacman.getY()/MapData.TILE_SIZE;

        if(map[row][col]=='.') {
            map[row][col]=' ';
            score+=10;
        } else if(map[row][col]=='C') {
            map[row][col]=' ';
            score+=50;
            startFrightenedMode();
        }
    }

    private void moveGhosts() {
        for(Ghost ghost:ghosts) {
            int[][] dirs={
                    {-MapData.TILE_SIZE,0},
                    {MapData.TILE_SIZE,0},
                    {0,-MapData.TILE_SIZE},
                    {0,MapData.TILE_SIZE}
            };

            int nextX=ghost.getX();
            int nextY=ghost.getY();

            if(frightenedMode) {
                int farX=ghost.getX();
                int farY=ghost.getY();
                int farDist=-1;

                for(int[] d:dirs) {
                    int testX=ghost.getX()+d[0];
                    int testY=ghost.getY()+d[1];

                    if(!isWall(testX,testY)) {
                        int dist=Math.abs(testX-pacman.getX())+Math.abs(testY-pacman.getY());
                        if(dist>farDist) {
                            farDist=dist;
                            farX=testX;
                            farY=testY;
                        }
                    }
                }

                nextX=farX;
                nextY=farY;
            } else {
                int[] d=dirs[random.nextInt(dirs.length)];
                int testX=ghost.getX()+d[0];
                int testY=ghost.getY()+d[1];

                if(!isWall(testX,testY)) {
                    nextX=testX;
                    nextY=testY;
                }
            }

            ghost.setX(nextX);
            ghost.setY(nextY);
        }
    }

    private void resetGhostPositions() {
        ghosts.get(0).setX(26*MapData.TILE_SIZE);
        ghosts.get(0).setY(7*MapData.TILE_SIZE);

        ghosts.get(1).setX(24*MapData.TILE_SIZE);
        ghosts.get(1).setY(7*MapData.TILE_SIZE);

        ghosts.get(2).setX(22*MapData.TILE_SIZE);
        ghosts.get(2).setY(7*MapData.TILE_SIZE);
    }

    private void sendGhostHome(Ghost ghost) {
        if(ghost.getType().equals("BLUE")) {
            ghost.setX(26*MapData.TILE_SIZE);
            ghost.setY(7*MapData.TILE_SIZE);
        } else if(ghost.getType().equals("PINK")) {
            ghost.setX(24*MapData.TILE_SIZE);
            ghost.setY(7*MapData.TILE_SIZE);
        } else {
            ghost.setX(22*MapData.TILE_SIZE);
            ghost.setY(7*MapData.TILE_SIZE);
        }
    }

    private void checkCollision() {
        for(Ghost ghost:ghosts) {
            if(pacman.getX()==ghost.getX()&&pacman.getY()==ghost.getY()) {
                if(frightenedMode) {
                    score+=200;
                    sendGhostHome(ghost);
                } else {
                    lives--;

                    if(lives<=0) {
                        gameOver=true;
                        timer.stop();
                    } else {
                        pacman.setX(13*MapData.TILE_SIZE);
                        pacman.setY(9*MapData.TILE_SIZE);
                        pacman.setDirection("RIGHT");
                        resetGhostPositions();
                    }
                }
                break;
            }
        }
    }

    private void resetGame() {
        MapData data=new MapData();
        map=copyMap(data.getMap());

        pacman.setX(13*MapData.TILE_SIZE);
        pacman.setY(9*MapData.TILE_SIZE);
        pacman.setDirection("RIGHT");

        ghosts.clear();
        ghosts.add(new Ghost(26*MapData.TILE_SIZE,7*MapData.TILE_SIZE,24,"BLUE"));
        ghosts.add(new Ghost(24*MapData.TILE_SIZE,7*MapData.TILE_SIZE,24,"PINK"));
        ghosts.add(new Ghost(22*MapData.TILE_SIZE,7*MapData.TILE_SIZE,24,"ORANGE"));

        score=0;
        lives=3;
        gameStarted=false;
        paused=false;
        gameOver=false;
        frightenedMode=false;
        frightenedEndTime=0;

        timer.stop();
        repaint();
    }

    private Image getPacmanImage() {
        if(pacman.getDirection().equals("LEFT")) {
            return pacmanLeft;
        } else if(pacman.getDirection().equals("RIGHT")) {
            return pacmanRight;
        } else if(pacman.getDirection().equals("UP")) {
            return pacmanUp;
        } else {
            return pacmanDown;
        }
    }

    private Image getGhostImage(String type) {
        if(frightenedMode&&scaredGhost!=null) {
            return scaredGhost;
        }

        if(type.equals("BLUE")) {
            return blueGhost;
        } else if(type.equals("PINK")) {
            return pinkGhost;
        } else {
            return orangeGhost;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(gameStarted&&!paused&&!gameOver) {
            updateFrightenedMode();
            moveGhosts();
            checkCollision();
            repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key=e.getKeyCode();

        if(key==KeyEvent.VK_S) {
            gameStarted=true;
            paused=false;
            if(!timer.isRunning()) {
                timer.start();
            }
            repaint();
            return;
        }

        if(key==KeyEvent.VK_P) {
            if(gameStarted&&!gameOver) {
                paused=!paused;
            }
            repaint();
            return;
        }

        if(key==KeyEvent.VK_R) {
            resetGame();
            return;
        }

        if(!gameStarted||paused||gameOver) {
            return;
        }

        int newX=pacman.getX();
        int newY=pacman.getY();

        if(key==KeyEvent.VK_LEFT) {
            newX-=MapData.TILE_SIZE;
            pacman.setDirection("LEFT");
        } else if(key==KeyEvent.VK_RIGHT) {
            newX+=MapData.TILE_SIZE;
            pacman.setDirection("RIGHT");
        } else if(key==KeyEvent.VK_UP) {
            newY-=MapData.TILE_SIZE;
            pacman.setDirection("UP");
        } else if(key==KeyEvent.VK_DOWN) {
            newY+=MapData.TILE_SIZE;
            pacman.setDirection("DOWN");
        }

        if(!isWall(newX,newY)) {
            pacman.setX(newX);
            pacman.setY(newY);
            eatFood();
            checkCollision();
        }

        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for(int row=0;row<map.length;row++) {
            for(int col=0;col<map[row].length;col++) {
                char tile=map[row][col];
                int x=col*MapData.TILE_SIZE;
                int y=row*MapData.TILE_SIZE;

                if(tile=='W') {
                    if(wallImage!=null) {
                        g.drawImage(wallImage,x,y,MapData.TILE_SIZE,MapData.TILE_SIZE,this);
                    } else {
                        g.setColor(Color.BLUE);
                        g.fillRect(x,y,MapData.TILE_SIZE,MapData.TILE_SIZE);
                    }
                } else if(tile=='.') {
                    g.setColor(Color.WHITE);
                    g.fillOval(x+12,y+12,6,6);
                } else if(tile=='C') {
                    if((row+col)%2==0&&cherry!=null) {
                        g.drawImage(cherry,x+4,y+4,MapData.TILE_SIZE-8,MapData.TILE_SIZE-8,this);
                    } else if(cherry2!=null) {
                        g.drawImage(cherry2,x+4,y+4,MapData.TILE_SIZE-8,MapData.TILE_SIZE-8,this);
                    } else {
                        g.setColor(Color.RED);
                        g.fillOval(x+8,y+8,14,14);
                    }
                }
            }
        }

        Image pacImg=getPacmanImage();
        if(pacImg!=null) {
            g.drawImage(pacImg,pacman.getX()+2,pacman.getY()+2,MapData.TILE_SIZE-4,MapData.TILE_SIZE-4,this);
        } else {
            g.setColor(Color.YELLOW);
            g.fillArc(pacman.getX()+3,pacman.getY()+3,pacman.getSize(),pacman.getSize(),30,300);
        }

        for(Ghost ghost:ghosts) {
            Image ghostImg=getGhostImage(ghost.getType());

            if(ghostImg!=null) {
                g.drawImage(ghostImg,ghost.getX()+2,ghost.getY()+2,MapData.TILE_SIZE-4,MapData.TILE_SIZE-4,this);
            } else {
                if(frightenedMode) {
                    g.setColor(new Color(50,100,255));
                } else if(ghost.getType().equals("BLUE")) {
                    g.setColor(Color.CYAN);
                } else if(ghost.getType().equals("PINK")) {
                    g.setColor(Color.PINK);
                } else {
                    g.setColor(Color.ORANGE);
                }

                g.fillOval(ghost.getX()+3,ghost.getY()+3,ghost.getSize(),ghost.getSize());
            }
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial",Font.BOLD,18));
        g.drawString("Score: "+score,20,map.length*MapData.TILE_SIZE+25);
        g.drawString("Lives: "+lives,150,map.length*MapData.TILE_SIZE+25);
        g.drawString("S = Start",260,map.length*MapData.TILE_SIZE+25);
        g.drawString("P = Pause",360,map.length*MapData.TILE_SIZE+25);
        g.drawString("R = Reset",480,map.length*MapData.TILE_SIZE+25);

        if(frightenedMode&&!gameOver) {
            g.setColor(Color.CYAN);
            g.drawString("Ghosts are scared!",620,map.length*MapData.TILE_SIZE+25);
        }

        g.setFont(new Font("Arial",Font.BOLD,20));

        if(!gameStarted&&!gameOver) {
            g.setColor(Color.YELLOW);
            g.drawString("Press S to Start",280,map.length*MapData.TILE_SIZE+55);
        }

        if(paused&&!gameOver) {
            g.setColor(Color.ORANGE);
            g.drawString("PAUSED",320,map.length*MapData.TILE_SIZE+55);
        }

        if(gameOver) {
            g.setColor(Color.RED);
            g.drawString("GAME OVER",300,map.length*MapData.TILE_SIZE+55);
        }
    }
}