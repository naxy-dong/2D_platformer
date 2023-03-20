
import processing.core.PApplet;
import processing.core.PImage;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;

// collision finally fixed
// Animations++ need to be fixed as well
/*
* Dashing: dashing in all directions
* fix the problem when you can't dash when not crashed
* */
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
    ArrayList<GenericObject> genericObj = new ArrayList<>();
    ArrayList<Background> backgrounds = new ArrayList<>();
    Timer timer;
    Timer inGameTimer;
    /***************************  IMAGES AND ANIMATIONS  ***************************/
    PImage bgImage;
    PImage[] platformSprites = new PImage[9];
    PImage[] flagAnim = new PImage[11];
    PImage[] playerIdleAnim = new PImage[11];
    PImage[] revPlayerIdleAnim = new PImage[11];
    PImage[] playerRunAnim = new PImage[12];
    PImage[] revPlayerRunAnim = new PImage[12];
    PImage[] playerDoubleJumpAnim = new PImage[6];
    PImage playerJumpSprite = new PImage();
    PImage revPlayerJumpSprite = new PImage();
    PImage playerFallSprite = new PImage();
    PImage revPlayerFallSprite = new PImage();

//    private final int[][] map = {
//            // x: in range of 0-31
//            // y: in range of 0- 17
//            // format: x, y, type of platform
////          {0, 0, 1},
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
            {0, 8, 10, 8}, {16, 8, 2, 2}, {10, 15, 5, 2}, {0, 17, 255, 1},
            {50, 5, 2, 10}
    };
    private final int[][] map = new int[18][256];
//    int offSet;

    public static void main(String[] args) {
        PApplet.main("Platformer_2D");
    }

    public void settings() {
        size(canvasWidth, canvasHeight);
    }

    public void setup() {
        int i, j;
        // Background Sprite
//        bgImage = loadImage("resources/Art/Pixel Adventure 1/Background/Blue.png");
        backgrounds.addAll(Arrays.asList(
                new Background("resources/background.jpg", 1, true)
        ));

        // Platform Sprites
        for (i = 0; i <= 8; i++) {
            platformSprites[i] = loadImage("resources/Art/Pixel Adventure 1/Terrain/sprite_0/sprite_" + i + ".png");
            platformSprites[i].resize(32, 32);
        }

        // PLAYER SPRITES
        playerJumpSprite = loadImage("resources/Art/Pixel Adventure 1/Main Characters/Mask Dude/Jump (32x32).png");
        playerJumpSprite.resize(64, 64);
        revPlayerJumpSprite = getReversePImage(playerJumpSprite);

        playerFallSprite = loadImage("resources/Art/Pixel Adventure 1/Main Characters/Mask Dude/Fall (32x32).png");
        playerFallSprite.resize(64, 64);
        revPlayerFallSprite = getReversePImage(playerFallSprite);

        // PLAYER ANIMATIONS
        for (i = 0; i <= 5; i++) {
            playerDoubleJumpAnim[i] = loadImage("resources/Art/Pixel Adventure 1/Main Characters/Mask Dude/Double Jump/sprite_" + i + ".png");
            playerDoubleJumpAnim[i].resize(64, 64);
        }
        for (i = 0; i <= 10; i++) {
            playerIdleAnim[i] = loadImage("resources/Art/Pixel Adventure 1/Main Characters/Mask Dude/Idle/sprite_" + nf(i, 2) + ".png");
            playerIdleAnim[i].resize(64, 64);
            revPlayerIdleAnim[i] = getReversePImage(playerIdleAnim[i]);

            playerRunAnim[i] = loadImage("resources/Art/Pixel Adventure 1/Main Characters/Mask Dude/Run/sprite_" + nf(i, 2) + ".png");
            playerRunAnim[i].resize(64, 64);
            revPlayerRunAnim[i] = getReversePImage(playerRunAnim[i]);

            //FLAG ANIMATIONS
            flagAnim[i] = loadImage("resources/Art/Pixel Adventure 1/Items/Checkpoints/Checkpoint/Checkpoint/sprite_" + nf(i, 2) + ".png");
            flagAnim[i].resize(128, 128);
        }
        for (i = 11; i <= 11; i++) {
            playerRunAnim[i] = loadImage("resources/Art/Pixel Adventure 1/Main Characters/Mask Dude/Run/sprite_" + nf(i, 2) + ".png");
            playerRunAnim[i].resize(64, 64);
            revPlayerRunAnim[i] = getReversePImage(playerRunAnim[i]);
        }
        //Player
        p1 = new Player();

        //Timer
        timer = new Timer();
        timer.setTimer(100);
        genericObj.addAll(Arrays.asList(
                new GenericObject(2000,400, flagAnim)
        ));
        //invisible Timer
        inGameTimer = new Timer();

        for (int[] pos : platform_position) {
            createPlatform(pos[0], pos[1], pos[2], pos[3]);
        }
        // Adding Platforms
        for (i = 0; i < map.length; i++) {
            for (j = 0; j < map[0].length; j++) {
                if (map[i][j] != 0) {
                    // j is the column * hence the X directions
                    // i is the row    * hence the Y direction
                    platforms.add(new Platform(j * 32, i * 32, map[i][j] - 1));
                }
            }
        }
    }

    public void draw() {
        // Background
//        drawBackground();
        background(200);
        for (Background g : backgrounds) {
            g.draw();
        }
        for (GenericObject g : genericObj) {
            g.draw();
        }
        //Players
        p1.update();
        p1.draw();
        checkLosing();

        //Platforms
        for (Platform p : platforms) {
            p.draw();
        }

        //Timers
        timer.countDown();
        timer.draw(0, 100);
        inGameTimer.countUp();

        // Animation Frames
        if (frameCount % 5 == 0) {
            animationFrame++;
            p1.incrementAnimFrame();
            for (GenericObject g : genericObj) {
                g.update();
            }
        }
    }

    public void checkLosing() {
        if (p1.y > canvasHeight) {
            reset();
        }
    }


    /*********************** Placing and removing blocks! ****************************/
    float timeBetweenPlacingBlocks = 0.1f;
    float nextTime = 0;

    private void mouseEvent() {
        if (inGameTimer.time > nextTime && mouseButton == LEFT) {
            addObstacles(placeMode);
            nextTime = inGameTimer.time + timeBetweenPlacingBlocks;
        } else if (mouseButton == RIGHT) {
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

    public void addObstacles(int placeMode) {
        switch (placeMode) {
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

    public void mouseDragged() {
        mouseEvent();
    }

    public void mousePressed() {
        mouseEvent();
    }

    /*********************** END of Placing and removing blocks! ****************************/
    public void keyPressed() {
//        System.out.println(key);
        if (key == 'w' || key == ' ' || keyCode == 38) {
            if(p1.isOnWall && !p1.isOnGround){
                p1.wallJump();
            }
            else{
                p1.jump();
            }
        }
        if (key == 'a' || keyCode == 37) {
            p1.moveLeft = true;
            p1.facingLeft = true;
        }
        if (key == 'd' || keyCode == 39) {
            p1.moveRight = true;
            p1.facingLeft = false;
        }
        if (key == 's' || keyCode == 40) {
            p1.pressDown = true;
        }
        if (key == 'w' || keyCode == 38) {
            p1.pressUp = true;
        }
        if (key == 'x') {
            p1.dash();
        }

        /* key manual
            1 (default) : a normal 30 x 30 cube
            2. a long platform
            3. a wall that you can place
         */
        if (keyCode >= '0' && keyCode <= '9') {
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
        if (key == 's' || keyCode == 40) {
            p1.pressDown = false;
        }
        if (key == 'w' || keyCode == 38) {
            p1.pressUp = false;
        }
        if (key == 'r') {
            reset();
        }
    }

    public void reset() {
        platforms.clear();
        backgrounds.clear();
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
            if (p1.colx + p1.colw > p.x && p1.colx < p.w + p.x) {
                //if the player lands on the platform
                if (p1.coly + p1.colh + p1.vely > p.y && p1.coly + p1.colh <= p.y) {
                    p1.isOnGround = true;
                    p1.jCount = 0;
                    p1.vely = 0;
                    return true;
                }
                //if the player hit the bottom of the platform
                if (p1.coly + p1.vely < p.y + p.h && p1.coly >= p.y + p.h) {
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
            if (p1.coly + p1.colh > p.y && p1.coly < p.y + p.h) {
                //if it's moving to the right
                if (p1.colx + p1.colw + p1.velx > p.x && p1.moveRight && p1.colx + p1.colw <= p.x) {
                    p1.velx = 0;
                    return true;
                }
                //if it's moving to the left
                if (p1.colx + p1.velx < p.x + p.w && p1.moveLeft && p1.colx >= p.x + p.w) {
                    p1.velx = 0;
                    return true;
                }
            }
        }
        return false;
    }

    /***********************************************
     ****   Rectangle Overlapping demonstration ****
     ************************************************/
    // Returns true if two rectangles (l1, r1) and (l2, r2) overlap
    // We only have 2 points to represent a rectangle
    /*  Visual representation:
          (x,y)======= w ========   basically r1.x = x + w
    *       l1-------------------
    *       |                   |
    *       |                   |
    *       |                   |
    *       |                   |
    *       -------------------r1
    * */
    //    static  boolean doOverlap() {
//        // If one rectangle is on left side of other
//        if (l1.x > r2.x || l2.x > r1.x) {
//            return false;
//        }
//
//        // If one rectangle is above other
//        if (r1.y < l2.y || r2.y < l1.y) {
//            return false;
//        }
//        return true;
//    }
    boolean doOverlap(float x1, float y1, float w1, float h1, float x2, float y2, float w2, float h2) {
        // If one rectangle is on left side of other
        if (x1 > x2 + w2 || x2 > x1 + w1) {
            return false;
        }
        // If one rectangle is above other
        if (y1 + h1 < y2 || y2 + h2 < y1) {
            return false;
        }
        return true;
    }

    boolean isLeftRayColliding(){
        for (Platform p : platforms) {
            if(doOverlap(p.x, p.y, p.w, p.h, p1.left_ray_x, p1.left_ray_y, p1.ray_offset, p1.ray_offset)){
                return true;
            }
        }
        return false;
    }

    boolean isRightRayColliding(){
        for (Platform p : platforms) {
            if(doOverlap(p.x, p.y, p.w, p.h, p1.right_ray_x, p1.right_ray_y, p1.ray_offset, p1.ray_offset)){
                return true;
            }
        }
        return false;
    }




    /*********************  END of COLLISION CODE    *******************/

    /*********************  CLASSES  *******************/
    public class Player {
        //movements + physics
        float x = 50, y = 100, w = 64, h = 64, jCount = 0, jMax = 2, speed = 4;
        float colx, coly, colw = 40, colh = 50, coly_offset = 7;
        float velx = 0, vely = 0, jumpForce = 15;
        //States
        public boolean isOnGround = true, moveLeft = false, moveRight = false, facingLeft = false;
        //Animations
        boolean oneshot = false;
        int anim_frame = 0;
        //Raycasts
        float ray_offset = 5, left_ray_x, left_ray_y, right_ray_x, right_ray_y;
        boolean isOnWall, isOnLeftWall, isOnRightWall;
        float slideForce = 3, wallJumpRepelForce = 40;
        // Dash
        boolean pressDown = false, pressUp = false;
        int dashForce_x = 50, dashForce_y = 10;

        HashMap<String, Integer> frames = new HashMap<>(){{
            put("idle", 11);
            put("run", 12);
            put("double_jump", 6);
        }};
        String prev_state = "", current_state = "";
        public void update() {
            // DEALING WITH THE Y-AXIS
            vely += GRAVITY;
            if(!isCollidingVertically()){
                y += vely;
            }

            // DEALING WITH THE X-AXIS
            int leftVal = moveLeft ? 1: 0;
            int rightVal = moveRight ? 1: 0;
            velx += speed * (rightVal - leftVal);

            moveHorizontal();

            velx = lerp(velx, 0, 0.3f);
            // collisions
            colx = x + (w/2 - colw/2);
            coly = y + (h/2 - colh/2) + coly_offset;
            // raycasts
            left_ray_x = colx - ray_offset;
            left_ray_y = right_ray_y = coly + (colh/2);
            right_ray_x = colx + colw;
            isOnLeftWall = isLeftRayColliding();
            isOnRightWall = isRightRayColliding();
            isOnWall = isOnLeftWall || isOnRightWall;
            if(isOnWall){
                wallSlide();
            }
            // states
            prev_state = current_state;
            update_current_state();
            if(prev_state != current_state){
                anim_frame = 0;
            }
        }
        //negative stands for -x direction, positive stands for +x direction
        public void moveHorizontal() {
            if(isCollidingHorizontally()){
                return;
            }
            if (x < leftThreshold && velx < -0.5 || x > rightThreshold && velx > 0.5) {
                for (Platform p : platforms) {
                    p.x -= velx;
                }
                for (GenericObject g : genericObj) {
                    g.x -= velx;
                }
                for (Background g : backgrounds) {
                    g.currentX -= g.speed * (velx < 0 ? -1 : 1);
//                    g.currentX -= 0.3 * velx;  // it introduce lag for some reason
//                    g.currentX -= velx;
                    if (g.currentX > 0) {
                        g.currentX = -canvasWidth;
                    } else if (g.currentX < -canvasWidth) {
                        g.currentX = 0;
                    }
                }
            } else {
                x += velx;
            }
        }
        public void jump() {
            isOnGround = false;
            if (jCount < jMax) {
                vely = -jumpForce;
                jCount++;
            }
        }
        public void wallJump() {
            int direction = isOnLeftWall ? 1 : -1;
            velx = wallJumpRepelForce * direction;
            vely = -jumpForce;
        }
        public void wallSlide() {
            if(isOnWall && !isOnGround && vely > 0){
                vely = slideForce;
            }
        }
        public void dash(){
            int rightVal = moveRight ? 1 : 0;
            int leftVal = moveLeft ? 1 : 0;
            int downVal = pressDown ? 1 : 0;
            int upVal = pressUp ? 1 : 0;

            int xDirection = 0, yDirection = 0;
            xDirection = rightVal - leftVal;
            yDirection = downVal - upVal;
//            System.out.println("x:" + xDirection + ", y:" + yDirection);
            // if nothing is pressed, or... everything is pressed
            if(xDirection == 0 && yDirection == 0){
                xDirection = facingLeft ? -1 : 1;
            }
            System.out.println("x:" + xDirection + ", y:" + yDirection);

            // implement dashing force!!!
            velx += xDirection * dashForce_x;
            vely += yDirection * dashForce_y;
        }
        public void incrementAnimFrame(){
            Integer num = frames.get(current_state);
            if(num != null){
                if(oneshot){
                    anim_frame = Math.min(num - 1,++anim_frame);
                }
                else{
                    anim_frame = (++anim_frame)%num;
                }
            }
        }
        public void update_current_state(){
            if (isOnGround && (moveLeft || moveRight)) {
                current_state = "run";
                oneshot = false;
                return;
            }
            if (isOnGround) {
                current_state = "idle";
                oneshot = false;
                return;
            }
            // IF THE PLAYER IS JUMPING AND in the air
            if (vely <= 0 && jCount == 2) {
                current_state = "double_jump";
                oneshot = true;
                return;
            }
            if (vely <= 0) {
                current_state = "jump";
                oneshot = true;
                return;
            }
            current_state = "fall";
            oneshot = true;
        }
        public void draw() {
            if (debugMode) {
                fill(0, 255, 0);
                rect(x, y, w, h);

                fill(100,100,200);
                rect(colx, coly, colw, colh);

                fill(0,0, 200);
                square(left_ray_x, left_ray_y, ray_offset);
                square(right_ray_x, right_ray_y, ray_offset);
            }

            // reverse the image if the player is moving left
            if (facingLeft) {
                switch(current_state){
                    case "run":
                        image(revPlayerRunAnim[anim_frame], x, y);
                        break;
                    case "idle":
                        image(revPlayerIdleAnim[anim_frame], x, y);
                        break;
                    case "double_jump":
                        image(playerDoubleJumpAnim[anim_frame], x, y);
                        break;
                    case "jump":
                        image(revPlayerJumpSprite, x, y);
                        break;
                    case "fall":
                        image(revPlayerFallSprite, x, y);
                        break;
                }
            } else {
                switch(current_state){
                    case "run":
                        image(playerRunAnim[anim_frame], x, y);
                        break;
                    case "idle":
                        image(playerIdleAnim[anim_frame], x, y);
                        break;
                    case "double_jump":
                        image(playerDoubleJumpAnim[anim_frame], x, y);
                        break;
                    case "jump":
                        image(playerJumpSprite, x, y);
                        break;
                    case "fall":
                        image(playerFallSprite, x, y);
                        break;
                }
            }
        }
    }

    //Platforms that you can jump underneath the platform
    public class Platform {
        public float x, y, w, h;
        int imgID;
        public PImage img;

        //        public boolean canJumpUnderneath;
        public Platform(float x, float y, int imgID) {
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

    public class Background {
        public float speed, currentX = 0;
        public String imgSrc;
        public PImage img;
        public boolean repeatable;

        public Background(String imgSrc, float speed, boolean repeatable) {
            this.imgSrc = imgSrc;
            this.img = loadImage(imgSrc);
            this.img.resize(canvasWidth, canvasHeight);
            this.speed = speed;
            this.repeatable = repeatable;
        }

        void draw() {
            if (repeatable) {
                image(img, currentX, 0);
                image(img, currentX + canvasWidth, 0);
            } else {
                image(img, currentX, 0);
            }
        }
    }

    public class GenericObject {
        public int x, y, w, h, speed, anim_frame;
        public PImage[] anim;
        public PImage sprite;
        boolean has_anim;

        //        public boolean canJumpUnderneath;
        public GenericObject(int x, int y, PImage[] anim) {
            this.x = x;
            this.y = y;
            this.anim = anim;
            this.speed = 0;
            has_anim = true;
            anim_frame = 0;
        }

        public GenericObject(int x, int y, PImage sprite) {
            this.x = x;
            this.y = y;
            this.sprite = sprite;
            this.speed = 0;
            has_anim = false;
        }

        void update(){
            anim_frame = (++anim_frame) % anim.length;
        }
        void draw() {
            if(has_anim){
                image(anim[anim_frame], x, y);
            }
            else{
                image(sprite, x, y);
            }
        }
    }

    public class Timer {
        public float time;

        public Timer() {
            time = 0;
        }

        void resetTimer() {
            time = 0;
        }

        void setTimer(int time) {
            this.time = time;
        }

        void countUp() {
            time += 1 / frameRate;
        }

        void countDown() {
            time = time <= 0 ? 0 : time - (1 / frameRate);
        }

        public void draw(int x, int y) {
            fill(50);
            textSize(80);
            text(time, x, y);
        }

        /*** Add ons extensions ***/
        public int getSeconds() {
            return (int) (time % 60);
        }

        public int getMinutes() {
            return (int) ((time / 60) % 60);
        }

        public int getHours() {
            return (int) ((time / (60 * 60)) % 60);
        }

        public void advancedDraw(int x, int y) {
            fill(50);
            textSize(80);
            //nf formats number into strings
            text(String.format("%s:%s:%s", nf(getHours(), 2), nf(getMinutes(), 2), nf(getSeconds(), 2)), x, y);
        }
    }


    public PImage getReversePImage(PImage image) {
        PImage reverse;
        reverse = createImage(image.width, image.height, ARGB);

        for (int i = 0; i < image.width; i++) {
            for (int j = 0; j < image.height; j++) {
                int xPixel, yPixel;
                xPixel = image.width - 1 - i;
                yPixel = j;
                reverse.pixels[yPixel * image.width + xPixel] = image.pixels[j * image.width + i];
            }
        }
        return reverse;
    }

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
    public void createPlatform(int x, int y, int width, int height) {
        int i, j, endi = y + height - 1, endj = x + width - 1;
        for (i = y; i <= endi; i++) {
            for (j = x; j <= endj; j++) {
                map[i][j] = 5;
                // left
                if (j == x) {
                    map[i][j] = 4;
                }
                // right
                if (j == endj) {
                    map[i][j] = 6;
                }
                // top
                if (i == y) {
                    map[i][j] = 2;
                }
                // bottom
                if (i == endi) {
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
}
/************* UN-USED CODE ****************/
//    Timer timer1;         // in setup
//        timer1 = new Timer(); // in draw
//        timer1.countUp();
//        timer1.advancedDraw(300,100);
      /***************************************/
     /*            Drawing Stuff           */
    /*************************************/
//    public void drawBackground(){
//        background(100);
//        int i, j;
//        for(i = 0; i < canvasWidth/64; i++){
//            for(j = 0; j < canvasHeight/64; j++) {
//                image(bgImage, bgImage.width * i, bgImage.height * j);
//            }
//        }
//    }
//    public void drawFlag() {
//        image(flagAnim[flagAnimFrame], 400, 400);
//    }

                        /***********************************
                         ****   Background demonstration ****
                          ************************************/

//                            (0,0)                     (canvasWidth,0)
//                                /*************************************************/
//                                //                        *                      //
//                                //                        *                      //
//                                // \ O /                  *                      //
//                                //  -+-                   *                      //
//                                //   |                    *                      //
//                                //  / \                   *                      //
//                                //                        *                      //
//                                //                        *                      //
//                                /*************************************************/
//
//(-canvaswidth, 0)            (0,0)
///*************************************************/
////                        *                      //
////                        *                      //
////                        *  \ O /               //
////                        *   -+-                //
////                        *    |                 //
////                        *   / \                //
////                        *                      //
////                        *                      //
///*************************************************/
