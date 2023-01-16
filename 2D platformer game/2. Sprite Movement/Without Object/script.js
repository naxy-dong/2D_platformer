let canvas = document.getElementById("game");
let context = canvas.getContext("2d");
let width = canvas.width;
let height = canvas.height;
let playerX, playerY;
let rectangleSize = 25;
let keys = [];
let platformX, platformY
let platform1X, platform1Y

canvas.style.marginTop = window.innerHeight / 2 - height / 2 + "px";

window.addEventListener("keydown", function(e) {
    keys[e.key] = true;
});

window.addEventListener("keyup", function(e) {
    keys[e.key] = false;
});

function init(){
    playerX = 200;
    playerY = 200;
}

function loop(){
    update();
    render();
}

//used for changing variables like play position
function update(){
    //start here
    if(keys["ArrowLeft"] == true){
        playerX = playerX - 1;
    }

    if(keys["ArrowUp"] == true){
        playerY = playerY - 1;
    }
}

//this is for displaying the game
function render(){
    context.clearRect(0,0,width,height);
    context.fillStyle = "red";
    context.fillRect(playerX,playerY,rectangleSize,rectangleSize);
}
//two ways to do it
//requestAnimationFrame(loop);
window.setInterval(loop, 1000/60);
init();

//use to initialize variables
function init(){
}

//repeat this function each frame
function loop(){
    update();
    render();
}

//used for changing variables
function update(){
}

//displaying the game
function render(){
}

window.setInterval(loop, 1000/60);
init();