import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.Timer;



public class Robber extends JPanel implements ActionListener, KeyListener {
    boolean gameWon=false;
    class Block {
        int x;
        int y;
        int width;
        int height;
        Image image;

        char nextDirection = ' ';

        int startX;
        int startY;
        char direction = 'U'; // U D L R
        int velocityX = 0;
        int velocityY = 0;

        Block(Image image, int x, int y, int width, int height) {  //Creates a block and stores its image, position and size.
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.startX = x;
            this.startY = y;
        }

        void updateDirection(char direction) {
            char prevDirection = this.direction;  //added this bcz when the theif is going down and suddenly i press up it stops this will save direction

            this.direction = direction;
            updateVelocity();
            this.x += this.velocityX;
            this.y += this.velocityY;

            //starting new code

//ending new code
            for (Block wall : walls) {
                if (collision(this, wall)) {
                    this.x -= this.velocityX;
                    this.y -= this.velocityY;
                    this.direction = prevDirection;
                    updateVelocity();
                }
            }
        }

        void updateVelocity() {
            if (this.direction == 'U') {  // "direction"  It tells us which way the thief is moving.
                this.velocityX = 0;
                this.velocityY = -tileSize/4;  //every frame it goes 8 pixels up
            }
            else if (this.direction == 'D') {
                this.velocityX = 0;
                this.velocityY = tileSize/4;
            }
            else if (this.direction == 'L') {
                this.velocityX = -tileSize/4;
                this.velocityY = 0;
            }
            else if (this.direction == 'R') {
                this.velocityX = tileSize/4;
                this.velocityY = 0;
            }
        }

        void reset() {
            this.x = this.startX;
            this.y = this.startY;
        }
    }

    private int rowCount = 21;
    private int columnCount = 19;
    private int tileSize = 32;
    private int boardWidth = columnCount * tileSize;
    private int boardHeight = rowCount * tileSize;

    private Image wallImage;
    private Image PolicemanImage;
    private Image PolicewomanImage;


    private Image thiefUpImage;
    private Image thiefDownImage;
    private Image thiefLeftImage;
    private Image thiefRightImage;
    private Image coinImage;
    private Image heartImage;


    private String[] tileMap = {
            "XXXXXXXXXXXXXXXXXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X                 X",
            "X XX X XXXXX X XX X",
            "X    X       X    X",
            "XXXX XXXX XXXX XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXrXX X XXXX",
            "X       bpo       X",
            "XXXX X XXXXX X XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXXXX X XXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X  X     P     X  X",
            "XX X X XXXXX X X XX",
            "X    X   X   X    X",
            "X XXXXXX X XXXXXX X",
            "X                 X",
            "XXXXXXXXXXXXXXXXXXX"
    };

    HashSet<Block> walls;
    HashSet<Block> coins;
    HashSet<Block> enemys;
    Block thief;

    Timer gameLoop;

    char[] directions = {'U', 'D', 'L', 'R'}; //up down left right
    Random random = new Random();
    int score = 0;
    int lives = 3;
    boolean gameOver = false;

    Robber() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        //load images
        wallImage = new ImageIcon(getClass().getResource("./wall.png")).getImage();
        PolicemanImage = new ImageIcon(getClass().getResource("./policeman.png")).getImage();
        PolicewomanImage = new ImageIcon(getClass().getResource("./Policewoman.png")).getImage();


        thiefUpImage = new ImageIcon(getClass().getResource("./thiefUp.png")).getImage();
        thiefDownImage = new ImageIcon(getClass().getResource("./thiefDown.png")).getImage();
        thiefLeftImage = new ImageIcon(getClass().getResource("./thiefLeft.png")).getImage();
        thiefRightImage = new ImageIcon(getClass().getResource("./thiefRight.png")).getImage();
        coinImage = new ImageIcon(getClass().getResource("./01coin.png")).getImage();
        heartImage = new ImageIcon(getClass().getResource("./heart.png")).getImage();

        loadMap();
        for (Block enemy : enemys) {
            char newDirection = directions[random.nextInt(4)];
            enemy.updateDirection(newDirection);
        }
        //how long it takes to start timer, milliseconds gone between frames
        gameLoop = new Timer(50, this); //20fps (1000/50)
        gameLoop.start();

    }

    public void loadMap() {

        walls = new HashSet<Block>();
        coins = new HashSet<Block>();
        enemys = new HashSet<Block>();

        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                String row = tileMap[r];
                char tileMapChar = row.charAt(c);

                int x = c*tileSize;
                int y = r*tileSize;

                if (tileMapChar == 'X') { //block wall
                    Block wall = new Block(wallImage, x, y, tileSize, tileSize);
                    walls.add(wall);
                }
                else if (tileMapChar == 'b') {
                    Block enemy = new Block(PolicemanImage, x, y, tileSize, tileSize);
                    enemys.add(enemy);
                }
                else if (tileMapChar == 'o') {
                    Block enemy = new Block(PolicewomanImage, x, y, tileSize, tileSize);
                    enemys.add(enemy);
                }
                else if (tileMapChar == 'p') {
                    Block enemy = new Block(PolicemanImage, x, y, tileSize, tileSize);
                    enemys.add(enemy);
                }
                else if (tileMapChar == 'r') {
                    Block enemy = new Block(PolicewomanImage, x, y, tileSize, tileSize);
                    enemys.add(enemy);
                }
                else if (tileMapChar == 'P') {
                    thief = new Block(thiefRightImage, x, y, tileSize, tileSize);
                }
                else if (tileMapChar == ' ') {
                    Block coin = new Block(coinImage, x + 14, y + 14, 10, 10);
                    coins.add(coin);
                }
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    //this method draws the images on the window (in short it loads the images on the screen)
    public void draw(Graphics g) {
        g.drawImage(thief.image, thief.x, thief.y, thief.width, thief.height, null);

        for (Block enemy : enemys) {
            g.drawImage(enemy.image, enemy.x, enemy.y, enemy.width, enemy.height, null);
        }

        for (Block wall : walls) {
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }

        g.setColor(Color.WHITE);
        for (Block coin : coins) {
            g.drawImage(coin.image , coin.x, coin.y, coin.width, coin.height , null);
        }
        //score
        for (int i=0;i<lives;i++){
            g.drawImage(heartImage, tileSize/2+(i*25), tileSize/2-5, 20,20,null);
        }
        g.setFont(new Font("Arial",Font.BOLD,18));
        String scoreText="Score: " +score;
        int scoretextWidth=g.getFontMetrics().stringWidth(scoreText);

        int scoreboxX=tileSize/2+(lives*25)+5;
        int scoreboxY=tileSize/2_8;
        int scoreboxWidth=scoretextWidth+20;
        int scoreboxHeight=25;

        g.setColor(Color.WHITE);
        g.fillRoundRect(scoreboxX, scoreboxY, scoreboxWidth, scoreboxHeight, 8, 8);

        g.setColor(new Color(200,200,200));
        g.drawRoundRect(scoreboxX, scoreboxY, scoreboxWidth, scoreboxHeight, 8, 8);

        g.setColor(Color.BLACK);
        g.drawString(scoreText,scoreboxX+(scoreboxWidth-scoretextWidth)/2,scoreboxY+scoreboxHeight/2+6);

        if(gameOver){
            g.setColor(new Color(0,0,0,150));
            g.fillRect(0,0,boardWidth,boardHeight);
            g.setFont(new Font("Arial", Font.BOLD,36));
            String gameOverText="GAME OVER";
            int gameOverWidth=g.getFontMetrics().stringWidth(scoreText);

            g.setFont(new Font("Arial",Font.PLAIN,24));
            String scoreText2="Final Score: " +score;
            int scoreWidth=g.getFontMetrics().stringWidth(scoreText2);

            String restartText="Press any key to restart";
            int restartWidth=g.getFontMetrics().stringWidth(restartText);

            int boxWidth=Math.max(Math.max(gameOverWidth,scoreWidth),restartWidth)+60;
            int boxHeight=150;
            int boxX=boardWidth/2-boxWidth/2;
            int boxY=boardHeight/2-boxHeight/2;

            g.setColor(Color.WHITE);
            g.fillRoundRect(boxX,boxY,boxWidth,boxHeight,20,20);
            g.setColor(new Color(180,180,180,180));
            g.drawRoundRect(boxX,boxY,boxWidth,boxHeight,20,20);

            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD,36));
            g.drawString(gameOverText,boardWidth/2-gameOverWidth/2,boxY+50);

            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.PLAIN,24));
            g.drawString(scoreText2,boardWidth/2-scoreWidth/2,boxY+90);

            g.setColor(new Color(70,70,70));
            g.setFont(new Font("Arial",Font.ITALIC,18));
            g.drawString(restartText,boardWidth/2-restartWidth/2,boxY+120);
        }
        else if(gameWon){
            g.setColor(new Color(0,0,0,150));
            g.fillRect(0,0,boardWidth,boardHeight);
            g.setFont(new Font("Arial",Font.BOLD,36));
            String winText="YOU WON THE GAME";
            int winWidth=g.getFontMetrics().stringWidth(winText);

            g.setFont(new Font("Arial",Font.PLAIN,24));
            String scoreText2="Final Score: "+score;
            int scoreWidth=g.getFontMetrics().stringWidth(scoreText2);

            String restartText="Press any key to restart";
            int restartWidth=g.getFontMetrics().stringWidth(restartText);

            int boxWidth=Math.max(Math.max(winWidth,scoreWidth),restartWidth)+60;
            int boxHeight=150;
            int boxX=boardWidth/2-boxWidth/2;
            int boxY=boardHeight/2-boxHeight/2;

            g.setColor(new Color(200,255,200));
            g.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 20, 20);
            g.setColor(new Color(100,180,100));
            g.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 20, 20);

            g.setColor(new Color(0,100,0));
            g.setFont(new Font("Arial",Font.BOLD, 36));
            g.drawString(winText,boardWidth/2-winWidth/2,boxY+50);

            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial",Font.PLAIN,24));
            g.drawString(scoreText2,boardWidth/2-scoreWidth/2,boxY+90);

            g.setColor(new Color(70,70,70));
            g.setFont(new Font("Arial",Font.ITALIC,18));
            g.drawString(restartText,boardWidth/2-scoreWidth/2,boxY+120);
        }
    }

    public void move() {
        //new code
        if(gameOver||gameWon){
            return;
        }

        if (thief.nextDirection != thief.direction) {
            int testX = thief.x;
            int testY = thief.y;

            int tempVX = 0;
            int tempVY = 0;

            if (thief.nextDirection == 'U') {
                tempVY = -tileSize/4;
            } else if (thief.nextDirection == 'D') {
                tempVY = tileSize/4;
            } else if (thief.nextDirection == 'L') {
                tempVX = -tileSize/4;
            } else if (thief.nextDirection == 'R') {
                tempVX = tileSize/4;
            }

            testX += tempVX;
            testY += tempVY;

            Block testBlock = new Block(null, testX, testY, thief.width, thief.height);

            boolean canMove = true;
            for (Block wall : walls) {
                if (collision(testBlock, wall)) {
                    canMove = false;
                    break;
                }
            }

            if (canMove) {
                thief.updateDirection(thief.nextDirection);
            }
        }
//closed
        thief.x += thief.velocityX;
        thief.y += thief.velocityY;

        //check wall collisions
        for (Block wall : walls) {
            if (collision(thief, wall)) {  //if collision between theif and current wall
                thief.x -= thief.velocityX;    //this will cancel the velocity and it will appear as the theif isnt moving
                thief.y -= thief.velocityY;
                break;
            }
        }

        //check thief collisions
        for (Block enemy : enemys) {
            if (collision(enemy, thief)) {
                lives -= 1;
                if (lives == 0) {
                    gameOver = true;
                    return;
                }
                resetPositions();
            }

            if (enemy.y == tileSize*9 && enemy.direction != 'U' && enemy.direction != 'D') {
                enemy.updateDirection('U');
            }
            enemy.x += enemy.velocityX;
            enemy.y += enemy.velocityY;
            for (Block wall : walls) {
                if (collision(enemy, wall) || enemy.x <= 0 || enemy.x + enemy.width >= boardWidth) {
                    enemy.x -= enemy.velocityX;
                    enemy.y -= enemy.velocityY;
                    char newDirection = directions[random.nextInt(4)];
                    enemy.updateDirection(newDirection);
                }
            }
        }


        Block coinEaten = null;
        for (Block coin : coins) {
            if (collision(thief, coin)) {
                coinEaten = coin;
                score += 10;
            }
        }
        coins.remove(coinEaten);

        if (coins.isEmpty()) {
            gameWon=true;
            return;
        }
    }

    public boolean collision(Block a, Block b) {
        return  a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }

    public void resetPositions() {
        thief.reset();
        thief.velocityX = 0;
        thief.velocityY = 0;
        for (Block enemy : enemys) {
            enemy.reset();
            char newDirection = directions[random.nextInt(4)];
            enemy.updateDirection(newDirection);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver||gameWon) {
            gameLoop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        if (gameOver||gameWon) {
            loadMap();
            resetPositions();
            lives = 3;
            score = 0;
            gameOver = false;
            gameWon=false;
            gameLoop.start();
        }
        // System.out.println("KeyEvent: " + e.getKeyCode());


        if (e.getKeyCode() == KeyEvent.VK_UP) {
            thief.nextDirection = 'U';
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            thief.nextDirection = 'D';
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            thief.nextDirection = 'L';
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            thief.nextDirection = 'R';
        }

        //closed

        if (thief.direction == 'U') {
            thief.image = thiefUpImage;
        }
        else if (thief.direction == 'D') {
            thief.image = thiefDownImage;
        }
        else if (thief.direction == 'L') {
            thief.image = thiefLeftImage;
        }
        else if (thief.direction == 'R') {
            thief.image = thiefRightImage;
        }
    }
}