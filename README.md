You only have one... FLAMETHROWER!!!
------------------------------------

You're in the level.  It's dark as hell.  The game says "Use it...".  You "use it" and the room lights up from... YOUR GIANT FLAMETHROWER.  Lights go out and these white evil eyes start to swarm.  They turn dark red and shake as they get closer.  A tense sound is growing intensely and your mind says, "Run!".  

Player can use the flamethrower to light the way, but there is limited fuel.  If you run out, you must traverse the levels in the dark.  Staying in the dark isn't an option because the little beady eyes keep coming.  It's a race against your flamethrower's fuel limit to get to the end of each level before the beady eyes get ya!

Insert 9 more levels and a big-ass boss at the end and call EA Games.. This shit's in the bag.

## Entities

**Dynamic**
Player
Flame
Boogiemen
Big-ass Boss?

**Traps**
Spikes
Spinning Saw-wheels?

## Game plan

- Rendering the level (2 hours)
	- Game should accept levelPath as argument to autoload levels (command from Tiled)
	- Level loader should load map data
	- Level should render map. Should render debug physics areas as well.
	- Load/create physics objects (platforms), load/create entities.
	- Create sample level that has 2 platforms, a gap, a player spawn location, and a trigger area.

- Player movement and jumping on platforms (3 hours)
	- Component to mark the player.
		- Orientation? {FacingLeft, FacingRight}
		- State {Running, Jumping, Shooting, Idle}
	- System to render player on screen.
	- System for catching key events and moving player.
		- Use EventManager
		- Create KeyPressedEvent
		- Needs physics body to scoot with (forever, this will take.)

- Collision Handling (3 hours)
	- Physics system issues CollisionEvents when collisions happen in the world.
		- Create collision event
			- colliding entities
			- when?
	- ScriptComponents that handle events that are published
		- ScriptComponent is a bag of scripts? 		

- Flamethrower Effect (3 hours)
	- Has a lifetime
		- Lifetime component?
	- Floats
		- AntigravityComponent {strength, fluctuation (sin wave 0..1f)}
	- Shitty graphic
	- Particle effect? If time.

- Boogie-men (3 hours)
	- Spawn when a player moves within a "Spawn Range" (easy to edit) TriggerEntities?
	- AI {CHASE, ONFIRESHIT}
		- Chase is something like 0.5d + x (move fast when far away, slow down when closer)
		- On fire shit is erratic movement
	- Floating through level
		- They don't participate in gravity? AntiGravity Component, zero flux.
		- Sensors
	- Blinking eyes
		- Just turn them off and on periodically.

- Art
	- Inkscape vector

- SFX
	sfxr.exe

- Music
	http://www.earslap.com/projectslab/circuli ???
