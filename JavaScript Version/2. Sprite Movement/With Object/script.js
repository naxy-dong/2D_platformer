let canvas = document.getElementById("game");
let c = canvas.getContext("2d");
let width = canvas.width;
let height = canvas.height;
const gravity = 0.3
canvas.style.marginTop = innerHeight / 2 - canvas.height / 2 + "px";

/*
    https://www.toptal.com/developers/keycode
    up: 38
    down: 40
    left: 37
    right : 39
*/
window.addEventListener('keydown', (e) => {
    console.log(e)
    switch (e.key) {
        case "ArrowLeft":
            player.velocity.x = -player.moveSpeed
            break
        case "ArrowRight":
            player.velocity.x = player.moveSpeed
            break
        case "ArrowUp":
            player.velocity.y = -player.jumpSpeed
            break
        case "ArrowDown":
            player.velocity.y += player.moveSpeed
            break
    }
})

window.addEventListener('keyup', (e) => {
    switch (e.key) {
        case "ArrowLeft":
            player.velocity.x = 0
            break
        case "ArrowRight":
            player.velocity.x = 0
            break
        case "ArrowUp":
            player.velocity.y += gravity;
            break
        case "ArrowDown":
            player.velocity.y = 0
            break
    }
})


class Player {
    //constructor initializes the values of the variables inside
    constructor() {
        this.position = {
            x: 100,
            y: 100
        }

        this.velocity = {
            x:0,
            y:0
        }

        this.width = 30
        this.height = 30
        this.moveSpeed = 4
        this.jumpSpeed = 5
    }

    //this is a function inside our class
    draw() {
        c.fillStyle = 'red'
        c.fillRect(this.position.x, this.position.y, this.width, this.height);
    }

    update() {
        this.draw()
        this.position.y += this.velocity.y
        this.position.x += this.velocity.x

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
//make a template of platform
class Platform {
    constructor() {
        this.position = {
            x: 200,
            y: 400
        }

        this.width = 200
        this.height = 20
    }

    draw() {
        c.fillStyle = 'blue'
        c.fillRect(this.position.x, this.position.y, this.width, this.height)
    }
}


let player = new Player()
//make a platform object
let platform = new Platform()

function animate() {
    //if other object such as obstacle needs to be drawn
    c.clearRect(0, 0, width, height)
    player.update()
    platform.draw()
    requestAnimationFrame(animate)
}

animate()