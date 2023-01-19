//DEFINE CANVAS
var canvasWidth = 600;
var canvasHeight = 720;

//GROUND and PLATFORM
var groundX = 80;
var groundY = 700;

var wall1X = 450;
var wall1Y = 560;

var wall2X = 260;
var wall2Y = 590;

//DEFINE SPRITE SIZE
var sprWidth = 64
var sprHeight = 64

//PLAYER SPRITES
var idleFrames = 1;
var runFrames = 4;
var attackWidth = 70;
var attackFrames = 8;
var player = 0;
var playerX = 50;
var playerY = 623;

//MONSTER SPRITES;
var monster = 0;
var monsterX = 350;
var monsterY = 520;
var direction = 90;

//GRAVITY AND JUMPING
var jump = -15;
var jumpMax = 1;
var jumpCount = 0;
const GRAVITY = 1;

//STATES
var jumpActive = false;
var editorMode = false; 
var moving = false; 
var meleeAttack = false;
var playerPower = false;
var gamePlay = false;
var stopInvincible; 
var invincible = false; 

//MELEE ATTACK
var hitboxes;
var attacks;
var hitbox;
var hitboxX = 50;
var hitboxColliderX = 40;
var hitboxColliderY = 40;

//KEYCODES
var jumpKey = 32;  //spacebar key
var jumpKey2 = 87; //W key
var rightKey = 68; //D key
var leftKey = 65;  //A key
var attackKey = 81;//Q key 
var reloadKey = 82;//R key 
var returnKey = 66; 

//PLAYER MOVEMENTS
var borderLeft = -63
var borderRight = 1362
var directionFacing = "right"
var playerSpeed = 5;

//GOAL
var goalX = 1200;
var goalY = 500;

//SCREENS
var scene = "title"
var youWinBackground = "hotpink"
var level1Background = "hotpink"
var titleBackground = "brown";
var camY;

//WIN SCREEN TEXT
var colorWin = "white"; 
var textWin = "YOU WIN!"; 
var outro = "Thank you for playing"; 
var replay = "Press R to Replay"; 
var myScore = "YOUR SCORE: "; 

//Start Button & TITLE SCREEN TEXT
var startButtonX = 40;
var startButtonY = 450;
var startWidth = 450;
var startHeight = 100;
var textTitle = "Start Game"; 
var colorTitle = "white"; 
var gameTitle = "Donut Canyon"; 

//HEALTH BAR
var playerHp = 4;
var hpX = 110;
var hpY = 30;
var hpWidth = 208;
var hpHeight = 43;
var hpPosX = 180; 
var hpPosY = 320; 

var health; 
var hpImg; 
var hpBorder; 
var hpBg; 

//SCORE
var score = 0; 
var enemyScore = 100; 

//TEXTBOX
var textBoxX = 40; 
var textBoxY = 535; 
var boxWidth = 50; 
var boxHeight = 50;
var mainDialogue = "Oh No! Donut Land has been invaded! 😱" + "\n"; 
var missionText = mainDialogue + "Let's save all the Donuts 🍩 (Click)"; 

//MENU
var menuX; 
var menuY; 
var menuX2; 
var menuY2 ; 

var menuPosX = 245; 
var menuPosY = 255; 
var menuPosX2 = 60; 
var menuPosY2 = 10; 

var menuWidth = 50; 
var menuHeight = 50; 

//Menu Text
var menuTitle = "Menu"; 
var controlsText = "CONTROLS"; 
var moveLeft = "Move the Player Left: " + "\t" + "Press A"; 
var moveRight = "Move the Player Right: " + "\t" + "Press D"; 
var makeJumps = "Jump: " + "\t" + "Press Spacebar or W"; 
var makeAttacks = "Melee Attack: " + "\t" + "Press Q"; 
var statsText = "PLAYER STATS"; 
var exitMenu = "[Press B to Exit Menu]"; 

//COLLECTIBLES
var coinX = 750; 
var coinY = 500; 
var coinColliderX = 60; 
var coinColliderY = 60; 

var coinWidth = 50; 
var coinHeight = 50; 
var coinFrames = 16; 

var goldScore = 50; 

//Health Pack
var healthPackX = 450; 
var healthPackY = 620; 
var packColliderX = 48; 
var packColliderY = 50; 

var packWidth = 50; 
var packHeight = 50; 
var packFrames = 4; 

var hpScore = 50

//Inventory
var inventoryX; 
var inventoryY; 
var inventoryPosX = 185; 
var inventoryPosY = 265; 

var inventoryPressed = true; 

//Power Cake
var powerX = -100; 
var powerY = 620; 
var powerupColliderX = 45; 
var powerupColliderY = 50; 

var powerWidth = 50; 
var powerHeight = 50; 
var powerFrames = 5; 

function preload() {
  // Load in Background
  bgImg = loadImage("images/firstLevel.png");
  
  // Load in Platforms
  platformImg = loadImage("images/platform.png");
  groundImg = loadImage("images/ground.png");
  
  // Load in Player (Idle State)
  playerImage = "images/player.png";
  playerImg = loadSpriteSheet(playerImage, sprWidth, sprHeight, idleFrames); 
  playerIdle = loadAnimation(playerImg); 
  
  // Load in Walking Player (Running state)
  playerRunImage = "images/player_run.png";
  playerRunImg = loadSpriteSheet(playerRunImage, sprWidth, sprHeight, runFrames);
  playerRun = loadAnimation(playerRunImg);
  
  // Load in Attacking Player (Attacking state)
  playerAttackImage = "images/player_attack.png";
  playerAttackImg = loadSpriteSheet(playerAttackImage, attackWidth, sprHeight, attackFrames);
  playerAttack = loadAnimation(playerAttackImg);
  
  // Load in Monster 
  monsterImg = loadImage("images/monster.png");  
  
  // Load in Hitbox
  hitboxImg = loadImage("images/empty.png");
  
  //GOAL 
  goalImg = loadImage("images/goal.png");
  
  //START BUTTON
  startImg = loadImage("images/start_button.png"); 
  
  //HP Image
  hpBgImg = loadImage("images/hp_background.png"); 
  hp4Img = loadImage("images/hp_green.png"); 
  hp3Img = loadImage("images/hp_yellow.png"); 
  hp2Img = loadImage("images/hp_orange.png"); 
  hp1Img = loadImage("images/hp_red.png"); 
  hpBorderImg = loadImage("images/hp_border.png"); 
  
  //Textbox Image
   textBoxImg = loadImage("images/textBox.png");
  
  //Menu
   menuImg = loadImage("images/menu.png"); 
   menuIconImg = loadImage("images/menuIcon.png"); 
  
  //Coin
  coinImage = "images/coin.png"; 
  coinImg = loadSpriteSheet(coinImage, coinWidth, coinHeight, coinFrames); 
  coinSpin = loadAnimation(coinImg); 
  
  //Health pack
  healthPackImage = "images/health_pack.png"; 
  healthPackImg = loadSpriteSheet(healthPackImage, packWidth, packHeight, packFrames); 
  healthPackAnim = loadAnimation(healthPackImg);
  
  //Cake
  powerupImage = "images/cake.png"; 
  powerupImg = loadSpriteSheet(powerupImage, powerWidth, powerHeight, powerFrames); 
  powerUp = loadAnimation(powerupImg); 
  
  //Player Power
  powerPlayerIdleImage = "images/player_power.png"; 
 playerIdlePowerImg = loadSpriteSheet(powerPlayerIdleImage, sprWidth, sprHeight, idleFrames); 
  playerIdlePower = loadAnimation(playerIdlePowerImg); 
  
  powerPlayerRunImage = "images/player_run_power.png"; 
 playerRunPowerImg = loadSpriteSheet(powerPlayerRunImage, sprWidth, sprHeight, runFrames); 
  playerRunPower = loadAnimation(playerRunPowerImg);
  
  powerPlayerAttackImage = "images/player_attack_power.png"; 
 playerAttackPowerImg = loadSpriteSheet(powerPlayerAttackImage, attackWidth, sprHeight, attackFrames); 
  playerAttackPower = loadAnimation(playerAttackPowerImg);
}

function setup() {
  createCanvas(canvasWidth, canvasHeight);
  
  // Create Player Sprite
  player = createSprite(playerX, playerY, sprWidth, sprHeight); 
  player.addAnimation("idle", playerIdle);
  player.addAnimation("run", playerRun);
  player.addAnimation("attack", playerAttack);
  player.setCollider("rectangle", 0, 0, 58, 58);
  
  // Create Monster Sprite
  monster = createSprite(monsterX, monsterY, sprWidth, sprHeight);
  monster.addImage(monsterImg);
  monster.setCollider("rectangle", 0, 0, 40, 40);
  
  enemies = new Group(); 
  enemies.add(monster);
  
  // Create Platform Sprites
  ground = createSprite(groundX, groundY);
  ground.addImage(groundImg);
  ground.setCollider("rectangle");
    
  wall1 = createSprite(wall1X, wall1Y);
  wall1.addImage(platformImg);
  wall1.setCollider("rectangle");
  
  wall2 = createSprite(wall2X, wall2Y);
  wall2.addImage(platformImg);
  wall2.setCollider("rectangle");
  
  walls = new Group();
  walls.add(ground);
  walls.add(wall1);
  walls.add(wall2);
  
  // Create Hitbox Groups
  attacks = new Group();
  hitboxes = new Group();
  
  //Goal image
  goal = createSprite(goalX, goalY); 
  goal.addImage(goalImg); 
  
  //Player Group
  players = new Group(); 
  players.add(player);

  startButton = createSprite(startButtonX, startButtonY);  
  startButton.addImage(startImg); 
  startButton.setCollider("rectangle", 0, 0, startWidth, startHeight); 
  startButton.visible = false;  
  
  // Create Health Bar Sprites
  hpBorder = createSprite(hpX, hpY, hpWidth, hpHeight);
  hpBorder.addImage(hpBorderImg, "images/hp_border.png");
  
  hpBg = createSprite(hpX, hpY, hpWidth, hpHeight);
  hpBg.addImage(hpBgImg, "images/hp_background.png"); 
  
  hpImg = createSprite(hpX, hpY,hpWidth, hpHeight);
  hpImg.addImage("green", hp4Img);
  hpImg.addImage("yellow", hp3Img);
  hpImg.addImage("orange", hp2Img);
  hpImg.addImage("red", hp1Img);
  
  //TEXTBOX 
   textBox = createSprite(textBoxX, textBoxY); 
 textBox.addImage(textBoxImg); 
   textBox.setCollider("rectangle", 0, 0, boxWidth, boxHeight); 
  
  //MENU page
  menu = createSprite(menuX2, menuY2); 
  menu.addImage(menuImg); 
  menu.setCollider("rectangle", 0, 0, menuWidth, menuHeight); 
  menu.visible = false; 
  
  //Menu Icon
  menuIcon = createSprite(menuX, menuY); 
  menuIcon.addImage(menuIconImg);
  menuIcon.setCollider("rectangle", 0, 0, menuWidth, menuHeight); 
  
  //Coin
  newCoin = createSprite(coinX, coinY); 
  newCoin.addAnimation("spin", coinSpin); 
  newCoin.setCollider("rectangle", 0, 0, coinColliderX, coinColliderY); 
  
  collectibles = new Group(); 
  collectibles.add(newCoin); 
  
  //Health Pack
  newHealthPack = createSprite(healthPackX, healthPackY); 
  newHealthPack.addAnimation("heal", healthPackAnim); 
  newHealthPack.setCollider("rectangle", 0, 0, packColliderX, packColliderY);
  
  healths = new Group(); 
  healths.add(newHealthPack); 
  
  //Inventory
  newInventory = createSprite(inventoryX, inventoryY); 
  newInventory.addAnimation("heal", healthPackImg); 
  newInventory.setCollider("rectangle", 0, 0, packColliderX, packColliderY); 
  newInventory.visible = false; 
  healths.add(newInventory); 
  
  //Cake powerup
  newPowerup = createSprite(powerX, powerY); 
  newPowerup.addAnimation("glow", powerUp); 
  newPowerup.setCollider("rectangle", 0, 0, powerupColliderX, powerupColliderY); 
  
  powerups = new Group(); 
  powerups.add(newPowerup); 
  
  //Powerups animation
  player.addAnimation("idlePower", playerIdlePower); 
  player.addAnimation("runPower", playerRunPower); 
  player.addAnimation("attackPower", playerAttackPower); 
}


function collisions(){
  player.collide(walls);
  player.collide(enemies, damagePlayer); 
  enemies.overlap(hitboxes, destroyEnemies); 
  goal.overlap(player, endGame);
  collectibles.overlap(player, increaseGold)
  healths.overlap(player, healthPackItem);
  powerups.overlap(player, powerupItem);
}

function jumping(){
  jumpCount += 1;
  jumpActive = true;
  player.velocity.y = jump;
}

function gravity() {
  var grounded = false;
  for (wall of walls.toArray()) {
        if (wall.overlapPixel(player.position.x, player.position.y + 28)) {
            player.velocity.y = 0;
            jumpActive = false;
            grounded = true;
            jumpCount = 0;
            break;
        }
    }
    if (grounded == false) {
        player.velocity.y += GRAVITY;
    }
}

function playerControls(){
  if(editorMode == false){
     if (keyIsDown(jumpKey) || keyIsDown(jumpKey2)) {
      if (jumpCount < jumpMax) {
        jumping();
      }
    }
    if (keyIsDown(leftKey)) { // A key, left
      if (player.position.x < borderLeft) {
        player.velocity.x = 0;
        moving = false;
      } else {
        player.velocity.x = -playerSpeed;
        player.mirrorX(-1); 
        directionFacing = "left";
        moving = true;
      }
    } else if (keyIsDown(rightKey)) { // D key, right
      if (player.position.x > borderRight) {
        player.velocity.x = 0;
        moving = false;
      } else {
        player.velocity.x = playerSpeed;
        player.mirrorX(1);
        directionFacing = "right";
        moving = true;
      }
    } else {
      player.velocity.x = 0;
      moving = false;
    }
    if (keyIsDown(attackKey)) { // Q key
      hitboxes.removeSprites();
      meleeAttack = true;
      attack();
    }
  } 
}

function monsterMovements(){
  if(editorMode == false){
    direction += 15;
    monster.setSpeed(1, direction);
  }else{
    monster.setSpeed(0, direction);
  }
}

function attack(){
  if (meleeAttack == true) {
    if(directionFacing == "right"){
      hitbox = createSprite(player.position.x + hitboxX, player.position.y);
    }
    else if (directionFacing == "left") { 
       hitbox = createSprite(player.position.x - hitboxX, player.position.y); 
    }
    hitbox.addImage(hitboxImg); 
    hitbox.setCollider("rectangle", 0, 0, hitboxColliderX, hitboxColliderY); 
    
    hitboxes.add(hitbox); 
    attacks.add(hitbox); 
    setTimeout(attackOff, 400);
  }
}

function attackOff(){
  hitboxes.removeSprites(); 
  meleeAttack = false; 
}

function destroyEnemies(destroyed){
  destroyed.remove()
  hitboxes.removeSprites(); 
  score += enemyScore;
}

function states() {
  if (moving == true) {
    if (playerPower == false) {
      player.changeAnimation("run");
    } 
    else if(playerPower == true){
      player.changeAnimation("runPower");
    }
  } 
  else if (meleeAttack == true) {
    if (playerPower == false) {
      player.changeAnimation("attack");
    }
    else if(playerPower == true){
      player.changeAnimation("attackPower");
    }
  }
  else if(playerPower == true){
    player.changeAnimation("idlePower");
  }
  else {
    player.changeAnimation("idle");
  }
}

function cam(){
  if(editorMode == false){
    camera.position.x = player.position.x;
  }
}

function winText(){
  //Setup
  fill(colorWin); 
  stroke("black"); 
  strokeWeight(4); 
  textAlign(CENTER);
  
  //Win text
  textSize(41);
  text(textWin, goalX - 380, 130, canvasWidth, canvasHeight); 
  textSize(30);
  text(outro, goalX - 380, 210, canvasWidth, canvasHeight); 
  textSize(30);
  text(replay, goalX - 380, 260, canvasWidth, canvasHeight); 
  textSize(40);
  text(myScore + score, goalX - 380, 350, canvasWidth, canvasHeight); 
}

function endGame(){
  gamePlay = false;  
  scene = "youWin"
  console.log("You win")
}

function spriteHide(){
  player.visible = false; 
  monster.visible = false; 
  ground.visible = false; 
  wall1.visible = false; 
  wall2.visible = false; 
  goal.visible = false;
  hpBorder.visible = false;
  hpImg.visible = false;
  hpBg.visible = false;
  textBox.visible = false;
  menuIcon.visible = false;
  newCoin.visible = false;
  newHealthPack.visible = false;
  newPowerup.visible = false;
}

function spriteDisplay(){
  player.visible = true; 
  monster.visible = true; 
  ground.visible = true; 
  wall1.visible = true; 
  wall2.visible = true; 
  goal.visible = true;
  hpBorder.visible = true;
  hpImg.visible = true;
  hpBg.visible = true;
  textBox.visible = true;
  menuIcon.visible = true;
  newCoin.visible = true;
  newHealthPack.visible = true;
  newPowerup.visible = true;
}

function titleText(){
  fill(colorTitle); 
  stroke("black"); 
  strokeWeight(4); 
  
   textSize(30); 
 textAlign(CENTER); 
  text(textTitle, -250, 425, canvasWidth, canvasHeight); 
  
  textSize(41); 
 textAlign(CENTER); 
   text(gameTitle, -250, 200, canvasWidth, canvasHeight);
}

function titleButton(){
  spriteHide()
  startButton.mouseUpdate(); 
  if (startButton.mouseIsPressed == true) { 
     gamePlay = true; 
     scene = "level1"; 
     spriteDisplay(); 
  }
}

function title(){
   startButton.visible = true; 
   drawSprites(); 
   titleButton(); 
   titleText(); 
}

function removeTitle(){
   startButton.visible = false; 
}

function healthBar(){
  if (playerHp >= 4) { 
    hpImg.changeImage("green"); 
  }
  else if(playerHp == 3){
    hpImg.changeImage("yellow")
  }
  else if(playerHp == 2){
    hpImg.changeImage("orange")
  }
  else if(playerHp == 1){
    hpImg.changeImage("red")
  }
  else{
    hpImg.remove();
  }
}

function gui(){
  hpX = camera.position.x - hpPosX; 
  hpY = camera.position.y - hpPosY; 
  
  hpBg.position.x = hpX; 
  hpBg.position.y = hpY; 
  
  hpImg.position.x = hpX
  hpImg.position.y = hpY
  
  hpBorder.position.x = hpX
  hpBorder.position.y = hpY
  
  menuX = camera.position.x - menuPosX; 
  menuY = camera.position.y - menuPosY; 
  menuX2 = camera.position.x - menuPosX2; 
  menuY2 = camera.position.y - menuPosY2; 
  
  menuIcon.position.x = menuX; 
  menuIcon.position.y = menuY; 
  
  menu.position.x = menuX2; 
  menu.position.y = menuY2; 
  
   inventoryX = camera.position.x - inventoryPosX; 
 inventoryY = camera.position.y - inventoryPosY; 
   newInventory.position.x = inventoryX; 
 newInventory.position.y = inventoryY; 
}

function damagePlayer(){
   if (playerHp <= 0) { 
     players.removeSprites();
   }
   if (invincible == false) { 
      invincible = true; 
      playerHp = playerHp - 1; 
      stopInvincible = frameCount + 60; 
   }
}

function invincibleTimer(){
  if (frameCount > stopInvincible && invincible == true) { 
     playerPower = false; 
     invincible = false; 
  }
}

function removePlayer(){
  if (player.position.y >= 850) { 
     playerHp = 0; 
     editorMode = true; 
  }
}

function reload(){
   if (keyIsDown(reloadKey)) { 
      window.location.reload(); 
   }
}

function dialogueText(){
  //Set up
   fill("white"); 
   stroke("black"); 
   strokeWeight(4); 
   textAlign(CENTER); 
   textSize(18); 
  
  text(missionText, -250, 500, canvasWidth, canvasHeight); 
}

function removeTextBox(){
  textBox.visible = false; 
}

function displayDialogue(){
  if(textBox.visible){
    dialogueText()
    textBox.mouseUpdate();
    if(textBox.mouseIsPressed){
      removeTextBox();
    }
  }
}

function menuText(){ 
  // Text Setup
  fill("black");
  stroke("black"); 
  strokeWeight(1);
  
  // Menu Title
  textSize(35);
  textAlign(CENTER);
  text(menuTitle, menuIcon.position.x - 50, 80, canvasWidth, canvasHeight);

  // Player Controls
  textSize(24);
  textAlign(LEFT);
  text(controlsText, menuIcon.position.x, 130, canvasWidth, canvasHeight);
  
  // Player Keys
  textSize(20);
  textAlign(LEFT);
  text(moveLeft, menuIcon.position.x, 170, canvasWidth, canvasHeight);
  text(moveRight, menuIcon.position.x, 200, canvasWidth, canvasHeight);
  text(makeJumps, menuIcon.position.x, 230, canvasWidth, canvasHeight);
  text(makeAttacks, menuIcon.position.x, 260, canvasWidth, canvasHeight);
  
  // Player Stats Title
  textSize(24);
  textAlign(LEFT);
  text(statsText, menuIcon.position.x, 320, canvasWidth, canvasHeight);
  
  // Track Player HP
  textSize(20);
  textAlign(LEFT);
  var healthStat = "Player HP:    " + playerHp;
  text(healthStat, menuIcon.position.x, 360, canvasWidth, canvasHeight);
  
  // Exit Menu
  textSize(24);
  textAlign(CENTER);
  text(exitMenu, menuIcon.position.x - 50, 600, canvasWidth, canvasHeight);
}

function removeMenu(){
   menu.visible = false; 
}

function displayMenu(){
  if(menuIcon.visible){
     menuIcon.mouseUpdate(); 
     if(menuIcon.mouseIsPressed == true) { 
       menu.visible = true; 
       gamePlay = false; 
       editorMode = true; 
       menuIcon.visible = false; 
       textBox.visible = false;
       newCoin.visible = false;
       newHealthPack.visible = false;
       newInventory.visible = false;
       newPowerup.visible = false;
     }
  }
   if (menu.visible == true) { 
     menuText()
   }
  
   if (keyIsDown(returnKey)) { 
     gamePlay = true;
     editorMode = false; 
     menuIcon.visible = true; 
     newCoin.visible = true;
     newHealthPack.visible = true;
     newInventory.visible = true;
     newPowerup.visible = true;
     removeMenu()
   }
}

function increaseGold(coinObject){
  score += goldScore;
  coinObject.remove(); 
}

function healthPackItem(healthObject){
  score+= hpScore;
  healthObject.remove(); 
  newInventory.visible = true; 
}

function store(){
  newInventory.mouseUpdate();
   if(newInventory.mouseIsPressed == true && inventoryPressed == true) { 
     if (playerHp <= 3) { 
        playerHp += 1; 
        healths.removeSprites(); 
        inventoryPressed = false; 
     }
   }
}

function powerupItem(powerupObject){
  powerupObject.remove();
  playerPower = true;
  stopInvincible = frameCount + 200;
}

function transitions(){
  //Title
  if(scene == "title"){
    background(titleBackground);
    camY = camera.position.y; 
    title()
  }
  else{
    removeTitle();
  }
  if (scene == "level1") { 
    background(level1Background);
    background(bgImg);
    drawSprites();
    gravity();
    collisions();
    playerControls();
    monsterMovements();
    states();
    healthBar();
    gui();
    invincibleTimer() 
    removePlayer()
    displayDialogue();
    displayMenu();
    store();
    
    camera.on();
  }
  if(scene == "youWin"){
    players.removeSprites();
    background(youWinBackground); 
    winText();
  }
}


function draw() {
  cam();
  transitions();
  reload();
}