
import processing.core.PApplet;

import java.util.Arrays;
import java.util.ArrayList;

public class Platformer_2D extends PApplet {
    //Canvas width and height
    static final int canvasWidth = 800;
    static final int canvasHeight = 500;

    //Game settings
    static final int leftThreshold = 200;
    static final int rightThreshold = 500;
    //GAMES
    static final double GRAVITY = 0.7;
    int placeMode = 0;

    //Necessary objects
    Player p1;
    ArrayList<Platform> platforms = new ArrayList<>();
    Timer timer;
    Timer timer1;
    Timer inGameTimer;

    private final int[][] platform_positions = {
            {400, 400}, {100, 400},
            {300, 200}
    };

    public static void main(String[] args) {
        PApplet.main("Platformer_2D");
    }

    public void settings() {
        size(canvasWidth, canvasHeight);
    }

    public void setup() {
        //Player
        p1 = new Player();

        //Platforms
        for(int[] pos: platform_positions){
            platforms.add(new Platform(pos[0], pos[1]));
        }

        //Timer
        timer = new Timer();
        timer.setTimer(100);

        timer1 = new Timer();

        //invisible Timer
        inGameTimer = new Timer();
    }

    public void draw() {
        background(100);
//        platformCollision();
        isCollidingVertically();
        p1.update();

        p1.draw();
        for (Platform p : platforms) {
            p.draw();
        }
//        timer.countUp();
        timer.countDown();
        timer.draw(0,100);

        timer1.countUp();
        timer1.advancedDraw(300,100);

        inGameTimer.countUp();
    }

    /*********************** Placing and removing blocks! ****************************/
    float timeBetweenPlacingBlocks = 0.1f;
    float nextTime = 0;
    private void mouseEvent(){
        if(inGameTimer.time > nextTime && mouseButton == LEFT){
            addObstacles(placeMode);
            nextTime = inGameTimer.time + timeBetweenPlacingBlocks;
        }
        else if(mouseButton == RIGHT){
            // There is no simple way to do this. So you'll have to use the information given
            for (int i = 0; i < platforms.size(); i++) {
                Platform p = platforms.get(i);
                if (mouseX > p.x &&
                        mouseX < p.x + p.w &&
                        mouseY > p.y &&
                        mouseY < p.y + p.h) {
                    platforms.remove(p);
                }
            }
        }
    }

    public void addObstacles(int placeMode){
        switch (placeMode){
            case 1:
                platforms.add(new Platform(mouseX, mouseY, 30,30));
                break;
            case 2:
                platforms.add(new Platform(mouseX, mouseY));
                break;
            case 3:
                platforms.add(new Platform(mouseX, mouseY, 10,100));
                break;
            case 4:
                break;
            default:
        }
    }

    //    public void mouseDragged(){
    //        System.out.println(mouseX + " " + mouseY);
    //        platforms.add(new Platform(mouseX, mouseY, 30,30));
    //    }
    public void mouseDragged(){
        mouseEvent();
    }

    public void mousePressed(){
        mouseEvent();
    }
    /*********************** END of Placing and removing blocks! ****************************/


    public void keyPressed() {
//        System.out.println(key);
        if (key == 'w' || key == ' ' || keyCode == 38) {
            p1.jump();
        }
        if (key == 'a') {
            p1.moveLeft = true;
        }
        if (key == 'd') {
            p1.moveRight = true;
        }

        /* key manual
            0(default) : a normal 30 x 30 cube
            1. a long platform
            2. a wall that you can place
         */

        if(keyCode >= '0' && keyCode <= '9'){
            placeMode = keyCode - '0';
        }
    }

    public void keyReleased() {
        if (key == 'a') {
            p1.moveLeft = false;
        }
        if (key == 'd') {
            p1.moveRight = false;
        }
    }
    // In this case, the player can jump underneath the platform
    public void platformCollision() {
        for (Platform p : platforms) {
            if (p1.y + p1.h <= p.y &&
                    p1.y + p1.h + p1.vely >= p.y &&
                    p1.x + p1.w >= p.x &&
                    p1.x <= p.w + p.x) {
                p1.vely = 0;
                p1.isOnGround = true;
                p1.jCount = 0;
            }
        }
    }
    // Performs rectangle detection to each platforms
    public boolean isCollidingVertically() {
        for (Platform p : platforms) {
            // If I keep moving in my y direction, will it collide with the rectangle?
            if (p1.x + p1.w >= p.x && p1.x <= p.w + p.x) {
                //if the player lands on the platform
                if(p1.y + p1.h + p1.vely >= p.y && p1.y + p1.h <= p.y){
                    p1.vely = 0;
                    p1.isOnGround = true;
                    p1.jCount = 0;
                    return true;
                }
                //if the player hit the bottom of the platform
                if(p1.y + p1.vely <= p.y + p.h && p1.y > p.y + p.h){
                    p1.vely = 0;
                    return true;
                }
            }
        }
        return false;
    }

    // Performs rectangle detection to each platforms
    public boolean isCollidingHorizontally() {
        for (Platform p : platforms) {
            if (p1.y + p1.h > p.y && p1.y < p.y + p.h) {
                //if it's moving to the right
                if(p1.x + p1.w + p1.speed > p.x && p1.moveRight && p1.x + p1.w <= p.x){
                    return true;
                }
                //if it's moving to the left
                if(p1.x - p1.speed < p.x + p.w && p1.moveLeft && p1.x >= p.x + p.w){
                    return true;
                }
            }
        }
        return false;
    }

    public class Player {
        public int x = 50, y = 100, w = 100, h = 100, jCount = 0, jMax = 2, speed = 10;
        public double velx = 0, vely = 0, jumpForce = 15;
        public boolean isOnGround = true, moveLeft = false, moveRight = false;

        public void update() {
            // DEALING WITH THE Y-AXIS
            if (y + h + vely < canvasHeight) {
                vely += GRAVITY;
                y += vely;
            } else {
                vely = 0;
                isOnGround = true;
                jCount = 0;
            }
            // DEALING WITH THE X-AXIS
            if (moveLeft) {
                moveHorizontal(-1);
            } else if (moveRight) {
                moveHorizontal(1);
            }
        }

        //negative stands for -x direction, positive stands for +x direction
        public void moveHorizontal(int direction) {
            // If I keep moving in my x direction, will it collide with the rectangle?
            if(isCollidingHorizontally()){
                return;
            }

            if (x < leftThreshold && direction == -1 || x > rightThreshold && direction == 1) {
                for (Platform p : platforms) {
                    p.x -= speed * direction;
                }
            } else {
                x += speed * direction;
            }
        }

        public void jump() {
            isOnGround = false;
            if (jCount < jMax) {
                vely = -jumpForce;
                jCount++;
            }
        }

        public void draw() {
            fill(255);
            rect(x, y, w, h);
        }
    }

    //Platforms that you can jump underneath the platform
    public class Platform {
        public int x, y, w, h;
        public boolean canJumpUnderneath;
        public Platform(int x, int y, int w, int h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.canJumpUnderneath = canJumpUnderneath;
        }

        public Platform(int x, int y) {
            this.x = x;
            this.y = y;
            this.w = 200;
            this.h = 10;
        }

        public void draw() {
            rect(x, y, w, h);
        }
    }

    public class Timer {
        public float time;
        public Timer() {
            time = 0;
        }

        void resetTimer(){
            time = 0;
        }

        void setTimer(int time){
            this.time = time;
        }
        void countUp(){
            time += 1 / frameRate;
        }

        void countDown(){
//            if(time > 0){
//                time -= 1 / frameRate;
//            }
//            else{
//                time = 0;
//            }
            time = time <= 0 ? 0 : time - (1 / frameRate);
        }

        public void draw(int x, int y) {
            fill(50);
            textSize(80);
            text(time, x, y);
        }

        /*** Add ons extensions ***/
        public int getSeconds(){
            return (int) (time % 60);
        }

        public int getMinutes(){
            return (int) ((time/60) % 60);
        }

        public int getHours(){
            return (int) ((time/(60*60)) % 60);
        }

        public void advancedDraw(int x, int y){
            fill(50);
            textSize(80);
            //nf formats number into strings
            text(String.format("%s:%s:%s", nf(getHours(),2), nf(getMinutes(),2),nf(getSeconds(),2)), x, y);
        }
    }
}
