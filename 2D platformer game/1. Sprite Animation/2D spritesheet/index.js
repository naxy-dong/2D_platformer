const canvas = document.getElementById("canvas");
const c = canvas.getContext("2d");

const width = canvas.width = 600;
const height = canvas.height = 400;

const fps = 60;
const secondsToUpdate = 0.06 * fps;
let count = 0;
const gravity = 0.1
canvas.style.marginTop = window.innerHeight / 2 - height / 2 + "px";

const spriteSheet = new Image();
spriteSheet.src = "../assets/images/adventurer_sprite_sheet_v1.1.png";

const State = {
  states: {},
  generateState: function(name, startIndex, endIndex) {
    if (!this.states[name]) {//if the state did not exist yet
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

State.generateState("idle", 0, 12);
State.generateState("walk", 13, 20);
State.generateState("slash", 26, 35);

currentState = "idle"
keys = []

window.addEventListener("keydown", e =>{
  keys[e.key] = true;
  if(e.key == "ArrowLeft"){
    currentState = "walk"
  }
  else if(e.key == "ArrowRight"){
    currentState = "slash"
  }
  else{
    currentState = "idle"
  }
})

window.addEventListener("keyup", e =>{
  keys[e.key] = false;
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
      this.frameWidth = 32
      this.frameHeight = 32
      this.state = currentState;
      this.scale = 1.5;
      this.width = this.frameWidth * this.scale
      this.height = this.frameHeight * this.scale
      this.moveSpeed = 4
      this.jumpSpeed = 5
  }

  animate(state) {
    c.drawImage(
      this.image,
      (state.frameIndex % 13) * this.frameWidth,
      parseInt(state.frameIndex/13) * this.frameHeight,
      this.frameWidth,
      this.frameHeight,
      this.position.x,
      this.position.y,
      this.width,
      this.height
    );
    // console.log(state.frameIndex % 13)
    // console.log(parseInt(state.frameIndex /13))
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