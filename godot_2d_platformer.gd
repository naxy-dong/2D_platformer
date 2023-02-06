extends KinematicBody2D

# Export makes it shows up in the inspector
###################### IMPORTANT VARIABLES ####################
export(float) var move_speed = 50
# Jump
var jump_count = 0
var jump_max = 2
var jump_impulse = 600
# Flip
var flip_count : int = 0
var flip_max : int = 2
var can_flip : bool = false
# Wall Slide
var wall_slide_speed : int = 30
# Wall jump
var wall_cooldown : float = 0.2
var can_wall_jump : bool = true
var repelForce_x : int = 800
# Dash 
var dash_direction : Vector2 
var dash_force : int = 1000
var can_dash : bool = true
#### States
# IDLE = 0, RUN = 1 ... and so on
enum STATE {IDLE, RUN, JUMP, DOUBLE_JUMP, FALL, HIT, WALL_JUMP, DASHING}
var current_state = STATE.IDLE setget set_current_state
signal changed_state(new_state_str, new_state_id)
# Physics
var velocity : Vector2
var reset_position : Vector2 = Vector2(250,0)
var up_direction : Vector2 = Vector2.UP

onready	var animation_tree = $AnimationTree
onready	var animated_sprite = $AnimatedSprite

func _physics_process(delta):
	var input = get_player_input()
	adjust_flip_direction(input)
	pick_next_state()
	set_anim_parameter()
	
	if Input.is_action_just_pressed("dash") and can_dash:
		dash()
	if Awall () and not is_on_floor(): 
		wall_slide()
	# Condition for Wall Jump		
	if Input.is_action_just_pressed("jump") and !is_on_floor() and Awall():
		wall_jump()
	elif Input.is_action_just_pressed("jump") and jump_count < jump_max:
		jump()
	if Input.is_action_just_pressed("flip") and flip_count < flip_max:
		flip()
	if is_on_floor():
		flip_count=0
		jump_count=0
		
	velocity.x = lerp(velocity.x, 0, GameSettings.friction)
	
	velocity = Vector2(
		clamp(velocity.x + input.x * move_speed, -GameSettings.terminal_velocity_x, GameSettings.terminal_velocity_x),
		clamp(velocity.y + GameSettings.gravity, -GameSettings.terminal_velocity_y, GameSettings.terminal_velocity_y)
	)
	velocity = move_and_slide(velocity, up_direction)

	
######################### FLIP METHOD #######################
func flip():
	GameSettings.gravity = -GameSettings.gravity
	can_flip = !can_flip
	animated_sprite.flip_v = !animated_sprite.flip_v
	flip_count += 1
	jump_impulse = -jump_impulse
	up_direction.rotated(PI)
	wall_slide_speed = -wall_slide_speed
	
########################### Jump #########################
func jump():
	# a normal Jump
	velocity.y = -jump_impulse
	jump_count += 1
#################### WALL RAYCAST/JUMPS/SLIDES #######################
func Rwall ():
	return $RightWall.is_colliding()
func Lwall ():
	return $LeftWall.is_colliding()
func Awall ():
	return Rwall() or Lwall()
func wall_jump():
	if can_wall_jump:
		can_wall_jump = false
		if Rwall() :
			velocity.x = -repelForce_x
		if Lwall() :
			velocity.x = repelForce_x
		velocity.y = -jump_impulse
		yield(get_tree().create_timer(0.1), "timeout")
		can_wall_jump = true
func wall_slide():
	if velocity.y > 0:
		velocity.y = wall_slide_speed
########################### Dash #########################
func dash():
	if animated_sprite.flip_h == false:
		dash_direction = Vector2.RIGHT
	else:
		dash_direction = Vector2.LEFT
	# dash_direction = get_local_mouse_position()
	velocity = dash_direction.normalized() * dash_force
	can_dash = false
	yield(get_tree().create_timer(1), "timeout")
	can_dash = true
########################### MOVEMENT INPUTS #########################
func get_player_input():
	var input : Vector2
	input.x = Input.get_action_strength("right") - Input.get_action_strength("left")
	input.y = Input.get_action_strength("down") - Input.get_action_strength("up")
	return input
	
#SPRITES
func adjust_flip_direction(input : Vector2):
	if(input.x == 1):
		animated_sprite.flip_h = false
	elif(input.x == -1):
		animated_sprite.flip_h = true
############################ ANIMATIONS #############################
func set_anim_parameter():
	if(abs(velocity.x) < 20):
		animation_tree.set("parameters/x_move/blend_position", 0)
	else:
		animation_tree.set("parameters/x_move/blend_position", sign(velocity.x))
	animation_tree.set("parameters/y_sign/blend_amount", sign(velocity.y))

########################## STATES ################################
func pick_next_state():
	if(is_on_floor()):
		if(Input.is_action_just_pressed("jump")):
			self.current_state = STATE.JUMP
		elif(abs(velocity.x) < 20):
			self.current_state = STATE.IDLE
		else:
			self.current_state = STATE.RUN
	else:
		if(Input.is_action_just_pressed("jump") and jump_count < jump_max):
			self.current_state = STATE.DOUBLE_JUMP

# SETTERS
func set_current_state(new_state):
	match(new_state):
		STATE.JUMP:
			jump()
		STATE.DOUBLE_JUMP:
			animation_tree.set("parameters/double_jump/active", true)
			jump()
	current_state = new_state
	emit_signal("changed_state", STATE.keys()[new_state], new_state)
