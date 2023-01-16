let canvas = document.getElementById("game");
let c = canvas.getContext("2d");
let width = canvas.width;
let height = canvas.height;
const gravity = 0.3
const leftThreshold = 150
const rightThreshold = 400
canvas.style.marginTop = innerHeight / 2 - canvas.height / 2 + "px";

let platformImage = new Image()
platformImage.onload = function() {
    alert(this.width + 'x' + this.height);
  }
platformImage.src = "download.jpg"
/*
    https://www.toptal.com/developers/keycode
    up: 38
    down: 40
    left: 37
    right : 39
*/

keys = []
window.addEventListener('keydown', (e) => {
    keys[e.key] = true
})
window.addEventListener('keyup', (e) => {
    keys[e.key] = false
    // switch (e.key) {
    //     case "ArrowLeft":
    //         player.velocity.x = 0
    //         break
    //     case "ArrowRight":
    //         player.velocity.x = 0
    //         break
    //     case "ArrowUp":
    //         break
    //     case "ArrowDown":
    //         break
    // }
})
function checkInput(){
    if(keys["ArrowLeft"]){
        player.velocity.x = -player.moveSpeed
        if (player.position.x <= leftThreshold) {
            platforms.forEach(function (platform) {
                platform.position.x += player.moveSpeed
            })
            player.velocity.x = 0
        }
    }
    else if(keys["ArrowRight"]){
        player.velocity.x = player.moveSpeed
        if (player.position.x >= rightThreshold) {
            platforms.forEach(function (platform) {
                platform.position.x -= player.moveSpeed
            })
            player.velocity.x = 0
        }
    }
    else{
        player.velocity.x = 0
    }
    //should not be an else if statement because we want player to be able
    //to register both horizonal direction and vertical direction instead of one direction
    if(keys["ArrowUp"]){
        player.velocity.y = -player.jumpSpeed
    }
    else if(keys['ArrowDown']){
        player.velocity.y += player.moveSpeed
    }
}

class Player {
    //constructor initializes the values of the variables inside
    constructor() {
        this.position = {
            x: 100,
            y: 100
        }

        this.velocity = {
            x: 0,
            y: 0
        }

        this.width = 30
        this.height = 30
        this.moveSpeed = 7
        this.jumpSpeed = 10
    }

    //this is a function inside our class
    draw() {
        c.fillStyle = 'red'
        c.fillRect(this.position.x, this.position.y, this.width, this.height);
    }
    //update variables
    update() {
        this.draw()
        this.position.y += this.velocity.y
        this.position.x += this.velocity.x
        this.velocity.y += gravity;
        /*
        if(this.position.y + this.height + this.velocity.y <= canvas.height){
            this.velocity.y += gravity;
        }
        else{
            this.velocity.y = 0;
        }
        */
        //The if function can be written in this following statement
        this.velocity.y = this.position.y + this.height + this.velocity.y <= canvas.height ? this.velocity.y + gravity : 0;
    }
}

class Platform{
    constructor(my_X, my_Y){
        this.position = {
            x: my_X,
            y: my_Y
        }

        this.width =300; 
        this.height =168

    }

    draw(){
        // c.fillStyle = 'blue'
        // c.fillRect(this.position.x, this.position.y, this.width, this.height)
        c.drawImage(platformImage, this.position.x, this.position.y, this.width, this.height)
    }
}

let player = new Player()
let platforms = [new Platform(400,300),new Platform(200,200)]
function animate() {
    //if other object such as obstacle needs to be drawn
    c.clearRect(0, 0, width, height)
    checkInput()
    player.update()
    platforms.forEach(function(platform){
        platform.draw() 
    })
    //rectangle collision
    platforms.forEach(function(platform){
        if(player.position.y + player.height <= platform.position.y && 
            player.position.y + player.height + player.velocity.y >= platform.position.y  &&
            player.position.x + player.width >= platform.position.x &&
            player.position.x <= platform.width + platform.position.x){
            player.velocity.y = 0
        }
    })

    requestAnimationFrame(animate)
}

animate()