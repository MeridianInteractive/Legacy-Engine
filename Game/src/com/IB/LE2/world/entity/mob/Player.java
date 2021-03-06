package com.IB.LE2.world.entity.mob;

import java.io.Serializable;

import org.luaj.vm2.LuaValue;

import com.IB.LE2.Boot;
import com.IB.LE2.Game;
import com.IB.LE2.asset.graphics.AnimatedSprite;
import com.IB.LE2.asset.graphics.Screen;
import com.IB.LE2.asset.graphics.Sprite;
import com.IB.LE2.input.UI.UI_Manager;
import com.IB.LE2.input.UI.menu.TagMenu;
import com.IB.LE2.input.hardware.Keyboard;
import com.IB.LE2.input.hardware.Mouse;
import com.IB.LE2.util.Debug;
import com.IB.LE2.util.VARS;
import com.IB.LE2.util.math.PVector;
import com.IB.LE2.util.shape.Rectangle;
import com.IB.LE2.world.entity.projectile.Selector;
import com.IB.LE2.world.entity.projectile.TagProjectile;
import com.IB.LE2.world.inventory.Inventory;
import com.IB.LE2.world.inventory.Item;
import com.IB.LE2.world.level.Level;
import com.IB.LE2.world.level.TileCoord;
import com.IB.LE2.world.level.tile.Tile;
import com.IB.LE2.world.level.worlds.TiledLevel;

public class Player extends Mob implements Serializable {
	private transient static final long serialVersionUID = -8911018741301426797L;

	public transient Keyboard input;
	public transient Tile tile;

	public transient AnimatedSprite
		idle_left  = (AnimatedSprite) Sprite.getNewAnim("PlayerIdleLeft"),
		idle_right = (AnimatedSprite) Sprite.getNewAnim("PlayerIdleRight"),
		idle_up = (AnimatedSprite) Sprite.getNewAnim("PlayerIdleUp"),
		idle_down = (AnimatedSprite) Sprite.getNewAnim("PlayerIdleDown"),
		
		up  = (AnimatedSprite) Sprite.getNewAnim("PlayerUp"),
		down  = (AnimatedSprite) Sprite.getNewAnim("PlayerDown"),
		left  = (AnimatedSprite) Sprite.getNewAnim("PlayerLeft"),
		right = (AnimatedSprite) Sprite.getNewAnim("PlayerRight");

	public transient AnimatedSprite animSprite = down;

	private transient TagMenu HUD;
	
	private Inventory inventory;
	
	private int facing = 0;
		// 0 = right
		// 1 = up
		// 2 = left
		// 3 = down

	public transient int cam_xOff = 0;
	public transient int cam_yOff = 0;

	// TODO: Generate UUID and send instead of Username

	public Player(double x, double y, Keyboard input, String username) {
		this.setX(x);
		this.setY(y);
		this.name = username;
		this.input = input;
		init();
	}

	public void init() {
		
		this.speed = 2;
		this.xBound = 8;
		this.yBound = 8;
		this.health = 50;
		
		EntWidth = 19;
		EntHeight = 51;
		xOffset = 22;
		yOffset = 11;
		
		super.set("name", "" + name);
		super.set("health", "" + health);
		super.set("speed", "" + speed);
		super.set("mass", "" + mass);
		
		body.bounds = new Rectangle((float) x(), (float) y(), 32, 64);
		body.set(VARS.PHYS_NOGRAV, true);
		body.bounds.set(VARS.REND_LOCALLY, true);
		
		this.inventory = new Inventory(this, "Inventory", 10);
		this.HUD = new TagMenu("HUD");

		System.out.println("ADDING NEW PLAYER: " + this.x() + "," + this.y());
	}
	
	public void ShowHUD() {
		UI_Manager.Load(HUD);
	}
	
	public void ShowInventory() {
		inventory.show();
	}
	
	public void AddItem(Item item) {
		inventory.addItem(item);
	}
	
	public boolean remove() {
		return super.remove();
	}
	
	public void set(String key, String val) {
		String from = this.svar(key);
		super.set(key, val);
		
		HUD.script.call("VarChanged", key, from, val);
	}

	public void MouseClicked(int btn) {
		LuaValue res = Boot.get().getMenu().script.call("onClick", btn);
		if (res != null) {
			return;
		}

		switch (btn) {
		case Mouse.LEFT_CLICK:
			updateShooting();
			break;
		case Mouse.RIGHT_CLICK:
			break;
		case Mouse.MIDDLE_CLICK:
			break;
		}
		
	}

	private transient PVector pv = null;
	public void update() {
		//HUD.script.call("SetHealthbar", GUI.progressBar(61, 100, this.health));
		//raycastDIR = level.RayCast(new Vector2i(x(), y()), dirInt, (int) 3);

		((TiledLevel) level).TestEventVolumes(this);
		
		animSprite.update();
		
		if (!moving) {
			cam_xOff = 0;
			cam_yOff = 0;
		}

		double xa = 0;
		double ya = 0;
		double yv = 0;

		if (pv == null) {
			pv = new PVector(vel());
		}

		if (input != null) {
			if (input.getKeyState("sprint") && walking) { // 300
				speed = 4;
			} else {
				speed = 2;
			}

			if (this == level.getClientPlayer()) {
				if (input.getKeyState("up")) {
					this.facing = 1;
					animSprite = up;
					this.vel().y(-speed);
				} else if (input.getKeyState("down")) {
					this.facing = 3;
					animSprite = down;
					this.vel().y(+speed);
				}
				if (input.getKeyState("left")) {
					this.facing = 2;
					animSprite = left;
					this.vel().x(-speed);
				} else if (input.getKeyState("right")) {
					this.facing = 0;
					animSprite = right;
					this.vel().x(speed);
				}

				if (Mouse.getButton() == 1 && walking) {
					if (Screen.xo << VARS.TILE_BIT_SHIFT > this.x()) {
						this.animSprite = right;
					} else {
						this.animSprite = left;
					}
				}
			}
		} else {
			if (vel().x() > 0) {
				animSprite = right;
			} else if (vel().x() < 0) {
				animSprite = left;
			}
		}

		if (this.isClientPlayer()) {
			ya = vel().y();
			xa = vel().x();

			if (xa != 0 || ya != 0) {
				Game.DiscordPlayerPosPresence();
				HUD.script.call("Moving");
				//Audio.MoveListener(x(), y(), 1);
			}

			if (this.HUD.enabled) {
				if (!move(xa, ya)) {
					animSprite = getDirectionalIdleAnim();
					this.animSprite.setFrameRate(8);
					this.walking = false;
				} else {
					this.animSprite.setFrameRate(6 - (int) this.speed / 2);
					this.walking = true;
				}
			}

		} else {
			ya = vel().y();
			xa = vel().x();
			move(xa, ya);
		}

		this.vel().x(0);
		this.pv.x(0);
		this.vel().y(0);
		this.pv.y(0);

		if (VARS.do_possession && Selector.selected != null) {
			Selector.selected.pos().set((Mouse.getX() / Boot.scale + Screen.xOffset) + 0,
					(Mouse.getY() / Boot.scale + Screen.yOffset) - Selector.selected.sprite.getHeight());
		}
		
		if (hurt > 0)
			hurt--;

		HUD.script.call("Clock", Level.WorldTime);
	}
	
	public AnimatedSprite getDirectionalIdleAnim() {
		switch (this.facing) {
		case 1:
			return this.idle_up;
		case 2:
			return this.idle_left;
		case 3:
			return this.idle_down;
		default:
			return this.idle_right;
		}
	}

	public boolean isClientPlayer() {
		return this.equals(Boot.get().getPlayer());
	}

	private Selector selection_tool;

	public void updateShooting() {
		if (Mouse.getButton() == 2) {
			selection_tool = new Selector((Mouse.getX() / Boot.scale + Screen.xOffset) + 0, (Mouse.getY() / Boot.scale + Screen.yOffset) + 0);
			level.add(selection_tool);
			selection_tool.update();
		}

		if (Mouse.getButton() == 1) {
			//XML_Projectile Test_Arrow = new XML_Projectile((x()) + 32, y() + 32, "/Tags/Projectiles/Arrow.xml", this);
			TagProjectile Grenade = new TagProjectile(x() + 32, y() + 32, "Grenade", this);
			level.add(Grenade);
			Mouse.setMouseB(-1);
		}
	}

	public void setPosition(TileCoord tileCoord) {
		setPosition(tileCoord.x(), tileCoord.y());
	}
	
	public void setPosition(int x, int y) {
		setX(x);
		setY(y);
	}

	public void setPositionTiled(double x, double y, String XML, boolean tileMult) {
		TiledLevel newLevel = (TiledLevel)Game.getCreateLevel(XML);
		Level current = Game.getLevel();
		Game.setLevel(newLevel);
		Game.getLevel().add(this);
		current.remove(this);
		
		
		if (x == -1 && y == -1) {
			x = newLevel.Spawnpoint.x() / 32;
			y = newLevel.Spawnpoint.y() / 32;
		}

		if (tileMult) {
			x *= TileCoord.TILE_SIZE;
			y *= TileCoord.TILE_SIZE;
		}

		this.removed = false;
		this.setX((x));
		this.setY((y));
		
		Game.DiscordPlayerPosPresence();
	}

	public void render(Screen screen) {
		sprite = animSprite.getSprite();
		
		
		this.DrawXOffset = 0;
		this.DrawYOffset = 0;

		screen.DrawEntity(this, (int) (x() + DrawXOffset + cam_xOff), (int) (y() + DrawYOffset + cam_yOff));
	}

	public void renderGUI(Screen screen) {
		if (Boot.drawDebug) {
			if (BottomBound != null) {
				BottomBound.drawLine(screen, true);
			}

			Debug.drawRect(screen, (int) x() + DrawXOffset, (int) y() + DrawYOffset, sprite.getWidth(),
					sprite.getHeight(), 0xffFADE0F, true);
			Debug.drawRect(screen, (int) x() + xOffset, (int) y() + yOffset, EntWidth, EntHeight, 0xff00FFFF, true);
		}

			//body.draw(screen);
	}


	public void toggleNoclip() {
		noclip =! noclip;
	}
}
