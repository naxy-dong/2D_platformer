
import processing.core.PApplet;
import processing.core.PImage;

import java.util.Arrays;
import java.util.ArrayList;

public class Platformer_2D extends PApplet {
    //Canvas width and height
    static final int canvasWidth = 16 * 64;
    static final int canvasHeight = 9 * 64;
    //Game settings
    static final int leftThreshold = 200;
    static final int rightThreshold = 500;
    //GAMES
    static final double GRAVITY = 0.7;
    int placeMode = 0;
    int animationFrame = 0;
    boolean debugMode = true;

    //Necessary objects
    Player p1;
    ArrayList<Platform> platforms = new ArrayList<>();
    Timer timer;
    Timer inGameTimer;
    /***************************  IMAGES AND ANIMATIONS  ***************************/
    PImage bgImage;
    PImage[] platformSprites = new PImage[9];
    PImage[] flagAnim = new PImage[11];
    int flagAnimFrame = 0;
    PImage[] playerIdleAnim = new PImage[11];
    PImage[] revPlayerIdleAnim = new PImage[11];
    PImage[] playerRunAnim = new PImage[12];
    PImage[] revPlayerRunAnim = new PImage[12];
    PImage[] playerDoubleJumpAnim = new PImage[6];
    PImage playerJumpSprite = new PImage();
    PImage revPlayerJumpSprite = new PImage();
    PImage playerFallSprite = new PImage();
    PImage revPlayerFallSprite = new PImage();
    int playerIdleAnimFrame = 0, playerRunAnimFrame = 0, playerDoubleJumpAnimFrame = 0;

//    private final int[][] map = {
//            // x: in range of 0-31
//            // y: in range of 0- 17
//            // format: x, y, type of platform
////            {0, 0, 1},
//            {1, 11, 0},
//            {2, 11, 1},
//            {3, 11, 1},
//            {4, 11, 1},
//            {5, 11, 1},
//            {6, 11, 2},
//            {1, 12, 6},
//            {2, 12, 7},
//            {3, 12, 7},
//            {4, 12, 7},
//            {5, 12, 7},
//            {6, 12, 8},
//
//            //Platform 1
//            {1, 11, 0}, {2, 11, 1}, {3, 11, 1}, {4, 11, 1}, {5, 11, 1}, {6, 11, 2}, {1, 12, 6}, {2, 12, 7}, {3, 12, 7}, {4, 12, 7}, {5, 12, 7}, {6, 12, 8},
//    };

    // x: in range of 0-31
    // y: in range of 0-17
    //platform data format: x, y, w, h
    private final int[][] platform_position = {
        {0,8,10,8}, {16,8,2,2}
    };
    private final int[][] map = new int[18][64];
    int offSet;
    /*
        // x: in range of 0-32  (32 excluded)
        // y: in range of 0-18  (18 excluded)
        // format: x, y, type of platform
        // type of platform : 0 is empty, otherwise it will be looking like the tile map
        // tilemap:
        1,2,3,
        4,5,6,
        7,8,9
    */
    // 2 x 2
    public void createPlatform(int x, int y, int width, int height){
        int i, j, endi = y + height -1, endj = x + width - 1;
        for(i = y; i <= endi; i++){
            for(j = x; j <= endj; j++){
                map[i][j] = 5;
                // left
                if(j == x){
                    map[i][j] = 4;
                }
                // right
                if(j == endj){
                    map[i][j] = 6;
                }
                // top
                if(i == y){
                    map[i][j] = 2;
                }
                // bottom
                if(i == endi){
                    map[i][j] = 8;
                }
            }
        }

        //CORNERS
        map[y][x] = 1;
        map[endi][x] = 7;
        map[y][endj] = 3;
        map[endi][endj] = 9;
    }

    public static void main(String[] args) {
        PApplet.main("Platformer_2D");
    }

    public void settings() {
        size(canvasWidth, canvasHeight);
    }

    public void setup() {
        int i, j;
        // Background Sprite
        bgImage = loadImage("resources/Art/Pixel Adventure 1/Background/Blue.png");

        // Platform Sprites
        for(i = 0; i <= 8; i++){
            platformSprites[i] = loadImage("resources/Art/Pixel Adventure 1/Terrain/sprite_0/sprite_" + i + ".png");
            platformSprites[i].resize(32,32);
        }

        // PLAYER SPRITES
        playerJumpSprite = loadImage("resources/Art/Pixel Adventure 1/Main Characters/Mask Dude/Jump (32x32).png");
        playerJumpSprite.resize(64,64);
        revPlayerJumpSprite = getReversePImage(playerJumpSprite);

        playerFallSprite = loadImage("resources/Art/Pixel Adventure 1/Main Characters/Mask Dude/Fall (32x32).png");
        playerFallSprite.resize(64,64);
        revPlayerFallSprite = getReversePImage(playerFallSprite);

        // PLAYER ANIMATIONS
        for(i = 0; i <= 5; i++){
            playerDoubleJumpAnim[i] = loadImage("resources/Art/Pixel Adventure 1/Main Characters/Mask Dude/Double Jump/sprite_" + i + ".png");
            playerDoubleJumpAnim[i].resize(64,64);
        }
        for (i = 0; i <= 10; i++) {
            playerIdleAnim[i] = loadImage("resources/Art/Pixel Adventure 1/Main Characters/Mask Dude/Idle/sprite_" + nf(i, 2) + ".png");
            playerIdleAnim[i].resize(64,64);
            revPlayerIdleAnim[i] = getReversePImage(playerIdleAnim[i]);

            playerRunAnim[i] = loadImage("resources/Art/Pixel Adventure 1/Main Characters/Mask Dude/Run/sprite_" + nf(i, 2) + ".png");
            playerRunAnim[i].resize(64,64);
            revPlayerRunAnim[i] = getReversePImage(playerRunAnim[i]);

            //FLAG ANIMATIONS
            flagAnim[i]=loadImage("resources/Art/Pixel Adventure 1/Items/Checkpoints/Checkpoint/Checkpoint/sprite_" + nf(i, 2) + ".png");
            flagAnim[i].resize(128,128);
        }
        for(i = 11; i <= 11; i++){
            playerRunAnim[i] = loadImage("resources/Art/Pixel Adventure 1/Main Characters/Mask Dude/Run/sprite_" + nf(i, 2) + ".png");
            playerRunAnim[i].resize(64,64);
            revPlayerRunAnim[i] = getReversePImage(playerRunAnim[i]);
        }
        //Player
        p1 = new Player();

        //Timer
        timer = new Timer();
        timer.setTimer(100);

        //invisible Timer
        inGameTimer = new Timer();

        for(int[] pos: platform_position){
            createPlatform(pos[0],pos[1],pos[2], pos[3]);

        }
        // Adding Platforms
        for( i = 0; i < map.length; i++){
            for( j = 0; j < map[0].length; j++) {
                if (map[i][j] != 0) {
                    // j is the column * hence the X directions
                    // i is the row    * hence the Y direction
                    platforms.add(new Platform(j * 32, i * 32, map[i][j] - 1));
                }
            }
        }
    }
    public PImage getReversePImage( PImage image ) {
        PImage reverse;
        reverse = createImage(image.width, image.height,ARGB );

        for(int i=0; i < image.width; i++ ){
            for(int j=0; j < image.height; j++){
                int xPixel, yPixel;
                xPixel = image.width - 1 - i;
                yPixel = j;
                reverse.pixels[yPixel*image.width+xPixel]=image.pixels[j*image.width+i] ;
            }
        }
        return reverse;
    }

    public void draw() {
        // Background
        drawBackground();
        drawFlag();
        //Players
        p1.update();
        checkLosing();
        p1.draw();

        //Platforms
        for (Platform p : platforms) {
            p.draw();
        }

        //Timers
        timer.countDown();
        timer.draw(0,100);
        inGameTimer.countUp();

        // Animation Frames
        if (frameCount % 5 == 0) {
            animationFrame++;
            flagAnimFrame = animationFrame % 11;
            playerIdleAnimFrame = animationFrame % 11;
            playerRunAnimFrame = animationFrame % 12;
            playerDoubleJumpAnimFrame = animationFrame % 6;
        }
    }
    public void checkLosing(){
        if(p1.y > canvasHeight){
            reset();
        }
    }
      /***************************************/
     /*            Drawing Stuff           */
    /*************************************/
    public void drawBackground(){
        background(100);
        int i, j;
        for(i = 0; i < canvasWidth/64; i++){
            for(j = 0; j < canvasHeight/64; j++) {
                image(bgImage, bgImage.width * i, bgImage.height * j);
            }
        }
    }
    public void drawFlag(){
        image(flagAnim[flagAnimFrame], 400,400);
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
                platforms.add(new Platform(mouseX, mouseY, 0));
                break;
            case 2:
                platforms.add(new Platform(mouseX, mouseY, 1));
                break;
            case 3:
                platforms.add(new Platform(mouseX, mouseY, 2));
                break;
            case 4:
                break;
            default:
        }
    }
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
        if (key == 'a' || keyCode == 37) {
            p1.moveLeft = true;
            p1.facingLeft = true;
        }
        if (key == 'd' || keyCode == 39) {
            p1.moveRight = true;
            p1.facingLeft = false;
        }

        /* key manual
            1 (default) : a normal 30 x 30 cube
            2. a long platform
            3. a wall that you can place
         */
        if(keyCode >= '0' && keyCode <= '9'){
            placeMode = keyCode - '0';
        }
    }
    public void keyReleased() {
        if (key == 'a' || keyCode == 37) {
            p1.moveLeft = false;
        }
        if (key == 'd' || keyCode == 39) {
            p1.moveRight = false;
        }
        if (key == 'r') {
            reset();
        }
    }
    public void reset(){
        platforms.clear();
        setup();
    }
    /*********************    COLLISION CODE    *******************/
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
            if (p1.x + p1.w > p.x && p1.x < p.w + p.x) {
                //if the player lands on the platform
                if(p1.y + p1.h + p1.vely >= p.y && p1.y + p1.h <= p.y){
                    p1.vely = 0;
                    p1.isOnGround = true;
                    p1.jCount = 0;
                    return true;
                }
                //if the player hit the bottom of the platform
                if(p1.y + p1.vely < p.y + p.h && p1.y >= p.y + p.h){
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
    /*********************  END of COLLISION CODE    *******************/

    /*********************  CLASSES  *******************/
    public class Player {
        public int x = 50, y = 100, w = 64, h = 64, jCount = 0, jMax = 2, speed = 7;
        public double velx = 0, vely = 0, jumpForce = 15;
        public boolean isOnGround = true, moveLeft = false, moveRight = false, facingLeft = false;

        public void update() {
            // DEALING WITH THE Y-AXIS
            isCollidingVertically();
            vely += GRAVITY;
            y += vely;
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
            if(debugMode){
                fill(0,255,0);
                rect(x,y,w,h);
            }

            // reverse the image if the player is moving left
            if(facingLeft){
                if(isOnGround && moveLeft){
                    image(revPlayerRunAnim[playerRunAnimFrame], x, y);
                    return;
                }
                if(isOnGround){
                    image(revPlayerIdleAnim[playerIdleAnimFrame], x, y);
                    return;
                }
                // IF THE PLAYER IS JUMPING AND in the air
                if(vely <= 0 && jCount == 2){
                    image(playerDoubleJumpAnim[playerDoubleJumpAnimFrame], x, y);
                    return;
                }
                if(vely <= 0){
                    image(revPlayerJumpSprite, x, y);
                    return;
                }
                // IF the player is not jumping, then it's falling
                image(revPlayerFallSprite, x, y);
            }
            else{
                if(isOnGround && moveRight){
                    image(playerRunAnim[playerRunAnimFrame], x, y);
                    return;
                }
                if(isOnGround) {
                    image(playerIdleAnim[playerIdleAnimFrame], x, y);
                    return;
                }
                // IF THE PLAYER IS JUMPING
                if(vely <= 0 && jCount == 2){
                    image(playerDoubleJumpAnim[playerDoubleJumpAnimFrame], x, y);
                    return;
                }
                if(vely <= 0){
                    image(playerJumpSprite, x, y);
                    return;
                }
                // IF the player is not jumping, then it's falling
                image(playerFallSprite, x, y);
                return;
            }
        }
    }

    //Platforms that you can jump underneath the platform
    public class Platform {
        public int x, y, w, h, imgID;
        public PImage img;
//        public boolean canJumpUnderneath;
        public Platform(int x, int y, int imgID) {
            this.x = x;
            this.y = y;
            this.imgID = imgID;
            this.img = platformSprites[imgID];
            this.w = img.width;
            this.h = img.height;
        }
         void draw() {
            image(img, x, y);
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
/************* UN-USED CODE ****************/
//    Timer timer1;
//        timer1 = new Timer();
//        timer1.countUp();
//        timer1.advancedDraw(300,100);