const canvas = document.getElementById("canvas");
const c = canvas.getContext("2d");

const width = canvas.width = 600;
const height = canvas.height = 500;

const fps = 60;
const secondsToUpdate = 0.06 * fps;
let frameIndex = 0;
let count = 0;
const gravity = 0.1
canvas.style.marginTop = window.innerHeight / 2 - height / 2 + "px";

const spriteSheet = new Image();
spriteSheet.src = "../assets/images/hero_spritesheet.png";

const State = {
  states: {},
  generateState: function(name, startIndex, endIndex) {
    if (!this.states[name]) {
      this.states[name] = {
        frameIndex: startIndex,
        startIndex: startIndex,
        endIndex: endIndex
      };
    }
  },
  getState: function(name) {
    if (this.states[name]) {
      return this.states[name];
    }
  }
};

State.generateState("breath", 0, 4);
State.generateState("angry", 4, 8);
State.generateState("jump", 8, 14);
currentState = "breath"

window.addEventListener("keydown", e =>{
  if(e.key == "ArrowLeft"){
    currentState = "jump"
  }
  else if(e.key == "ArrowRight"){
    currentState = "angry"
  }
  else{
    currentState = "breath"
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
      this.image = spriteSheet
      this.frameWidth = 64
      this.frameHeight = 100
      this.state = currentState;
      this.scale = 1;
      this.width = this.frameWidth * this.scale
      this.height = this.frameHeight * this.scale
      this.moveSpeed = 4
      this.jumpSpeed = 5
  }

  animate(state) {
    c.drawImage(
      this.image,
      state.frameIndex * this.frameWidth,
      0,
      this.frameWidth,
      this.frameHeight,
      this.position.x,
      this.position.y,
      this.width,
      this.height
    );
  
    count++;
    if (count > secondsToUpdate) {
      state.frameIndex ++;
      count = 0;
    }
  
    if (state.frameIndex > state.endIndex) {
      state.frameIndex = state.startIndex;
    }
  }

  update() {
      player.animate(State.getState(currentState));
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


let player = new Player()
function frame() {
  c.clearRect(0, 0, width, height);
  player.update();

  requestAnimationFrame(frame);
}

frame();