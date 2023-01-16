let canvas = document.getElementById("game");
let c = canvas.getContext("2d");
let width = canvas.width;
let height = canvas.height;

canvas.style.marginTop = innerHeight / 2 - canvas.height /2 + "px";


function animate(){
    requestAnimationFrame(animate)
}