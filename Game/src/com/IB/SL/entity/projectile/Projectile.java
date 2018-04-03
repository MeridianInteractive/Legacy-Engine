package com.IB.SL.entity.projectile;

import java.util.List;
import java.util.Random;

import javax.sound.sampled.Clip;

import com.IB.SL.Boot;
import com.IB.SL.Game;
import com.IB.SL.VARS;
import com.IB.SL.entity.Entity;
import com.IB.SL.entity.mob.Mob;
import com.IB.SL.entity.mob.PlayerMP;
import com.IB.SL.graphics.Sprite;
import com.IB.SL.input.Mouse;
import com.IB.SL.util.Sound;
import com.IB.SL.util.Vector2i;

public abstract class Projectile extends Entity {

	protected double xOrigin, yOrigin;
	public double angle;
	public double x, y;
	public double nx;
	public double ny;
	protected float zz, za;
	protected double distance;
	protected double range;
	public double damage;
	public double manaCost;
	protected Clip collisionSound;
	protected int ExpV;
	public Entity prevHit;
	public int id = -1;
	public int time = 0;
	protected Sprite master_sprite;
	
	public Entity origin;
	
	String MovementStyle = "simple";
	boolean initial_rotation = false;
	
	/**
	 * 1 = Rock
	 */
	protected int breakParticle;
	
	protected final Random random = new Random();
	protected final static Random randomFIRERATE = new Random();

	public Projectile() {
		
	}
	
	public Projectile (double x, double y, double dir) {
		xOrigin = x;
		yOrigin = y;
		angle = dir;
		this.x = x;
		this.y = y;
	}
	
	public Sprite getSprite() {
		return sprite;
	}
	
	public int getSpriteSize() {
		return sprite.SIZE;
	}
	
	protected void doEffect(Entity e) {}
	
	public Vector2i getCollisionPoint() {
		if (sprite != null) {
			return new Vector2i((int) x() + (sprite.SIZE / 2), (int) y() + (sprite.SIZE / 2));
		} else {
			return null;
		}
	}
	
	public Entity Collide(Projectile p, List<Entity> entities) {
		double mdpx = p.x;
		double mdpy = p.y;
		
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (e != null && e.getSprite() != null && !e.invulnerable) {
			if (mdpx   > (e.x() + e.xOffset)  && mdpx < (e.x() + e.xOffset)  + e.getSprite().getWidth()) {
				if (mdpy > (e.y() + e.yOffset)  && mdpy < (e.y() + e.yOffset)  + e.getSprite().getHeight() + 8) {
					int SP_X = (int) (mdpx - (e.x()) - e.xOffset);
					int SP_Y = (int) (mdpy - (e.y()) - (e.yOffset + 8));
					
					try {
					if (e.getSprite().pixels[(SP_X) + (SP_Y) * e.getSprite().getWidth()] != 0xffFF3AFB) {
						if (p.collisionSound != null) {
							Sound.Play(p.collisionSound, false);
						}
						if (p.breakParticle == 1) {
							//level.add(new RockShatterSpawner((int) (x + nx), (int) (y + ny), 20, 4, level));
						}
						//Boot.get().getLevel().add(new BleedSpawner((int) (p.x + p.nx), (int)(p.y + p.ny), 15, 8, level));
						return e;
							}
						} catch (Exception err) {
							//System.out.println("Object could not be cast to a mob!");
							return null;
						}
					}
				}
			}
		}
		return null;
	}
	
	public boolean Collision(Projectile p, List<Entity> entities) {
		Entity ee = Collide(p, entities);
		if (ee != null) {
			level.damage((int) (p.x + p.nx), (int) ((p.y + p.ny)), (Entity) ee, ee.Exp, p.damage, Boot.get().getPlayer().name, p.ExpV);
			doEffect(ee);
			p.remove();
			return true;
		}
		return false;
	}

	
	public boolean Collision_Pierce(Projectile p, List<Entity> entities) {
		Entity ee = Collide(p, entities);
		if (ee != null) {
			if (ee != p.prevHit) {				
			p.prevHit = ee;
			level.damage((int) (p.x + p.nx), (int) ((p.y + p.ny)), (Mob) ee, ee.Exp, p.damage, Boot.get().getPlayer().name, p.ExpV);
			doEffect(ee);
			return true;
			}
		}
		return false;
	}
	
	/*public Entity Collision_Player(List<Entity> entities, Projectile p, Mob mob) {
		double mdpx = p.x;
		double mdpy = p.y;
		for (int i = 0; i < entities.size(); i++) {
			if (!entities.get(i).invulnerable && entities.get(i) != mob && entities.get(i).hostility != mob.hostility) {	
			Entity e = entities.get(i);
			if (e != null && e.getSprite() != null) {				
			if (mdpx   > (e.getX() + e.xOffset)  && mdpx < (e.getX() + e.xOffset)  + e.getSprite().getWidth()) {
				if (mdpy > (e.getY() + e.yOffset)  && mdpy < (e.getY() + e.yOffset)  + e.getSprite().getHeight() + 8) {
					int SP_X = (int) (mdpx - (e.x) - e.xOffset);
					int SP_Y = (int) (mdpy - (e.y) - (e.yOffset + 8));
					
					try {
					if (e.getSprite().pixels[(SP_X) + (SP_Y) * e.getSprite().getWidth()] != 0xffFF3AFB) {
						if (p.collisionSound != null) {
							Sound.Play(p.collisionSound, false);
						}
						if (p.breakParticle == 1) {
							level.add(new RockShatterSpawner((int) (x + nx), (int) (y + ny), 20, 4, level));
						}
						Game.getGame().getLevel().add(new BleedSpawner((int) (p.x + p.nx), (int)(p.y + p.ny), 15, 8, level));
						return e;
							}
						} catch (Exception err) {
							System.out.println("Object could not be cast to a mob!");
							return null;
						}
					}
				}
			}
		}
		}
		return null;
	}*/
	
	
	protected void entityCollision(List<Entity> entities, Projectile proj, Mob mob) {
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i).invulnerable || entities.get(i) == mob || entities.get(i).hostility == mob.hostility) {	
			} else {
			if (x < entities.get(i).x() + 5
	            && x > entities.get(i).x() - 5
	            && y < entities.get(i).y() + 5
	            && y >  entities.get(i).y() - 5
	            ) {
				remove();
    				level.damage((int) (x + nx), (int)((y + ny)), (Mob) entities.get(i), entities.get(i).Exp, this.damage, "" + entities.get(0).UUID, 0);				
				//level.add(new BleedSpawner((int) (x + nx), (int) (y + ny), 10, 8, level));
				if (proj.collisionSound != null) {
					Sound.Play(proj.collisionSound, false);
				}
				if (proj.breakParticle == 1) {
					//level.add(new RockShatterSpawner((int) (x + nx), (int) (y + ny), 20, 4, level));
				}
				if (entities.get(i).mobhealth <= 0){
					entities.get(i).remove();
					//Boot.get().getLevel().add(new BleedSpawner((int) (x + nx), (int)(y + ny), 15, 8, level)); 

					}
				}
			}
		}
	}
	
	
	protected void PlayerCollision(List<PlayerMP> players, Projectile proj) {
		
		for (int i = 0; i < players.size(); i++) {
			if (x < players.get(i).x() + 5
					&& x > players.get(i).x() - 5
					&& y <  players.get(i).y() + 5
					&& y >  players.get(i).y() - 5
					) {
				if (!players.get(i).invulnerable) {
				remove();
				if (!Boot.get().devModeOn) {
					level.damagePlayer((int)proj.x(), (int)proj.y(), players.get(i), 0, proj.damage, "projectile", 0);
					proj.addEffect(players.get(i));
				}
				players.get(i).incombat = true;
				//level.add(new BleedSpawner((int) (x + nx), (int) (y + ny), 10, 8, level));
				if (proj.collisionSound != null) {
					Sound.Play(proj.collisionSound, false);
				}
				if (proj.breakParticle == 1) {
					//level.add(new RockShatterSpawner((int) (x + nx), (int) (y + ny), 20, 4, level));
				}
				if (players.get(i).mobhealth <= 0){
					//players.get(i).onPlayerDeath();
					//Game.switchState(Boot.get().gameState.DEATH);
					//level.add(new ParticleSpawner((int) (x + nx), (int) (y + ny), 30000, 200, level));
					System.out.println("Player " + players.get(i) + " Died");
					//Game.Dead = true;	
					}
				}
			}
		}
	}
	
	public double angle() {
			//TODO: Convert projectile x,y to PVector!
		double dx = Mouse.getX() - ((x - Boot.get().xScroll) * 2);
		double dy = Mouse.getY() - ((y - Boot.get().yScroll) * 2);
		///double dx = Mouse.getX() - Game.getWindowWidth() / 2;
		///double dy = Mouse.getY() - Game.getWindowHeight() / 2;
		double dir = Math.atan2(dy + 32, dx + 16);
		return dir;
	}
	
	public double distance() {
		double dist = 0;
		dist = Math.sqrt(Math.abs((xOrigin - x) * (xOrigin - x) + (yOrigin -y) * (yOrigin - y)));
		return dist;
	}
	
	public void addEffect(PlayerMP playerMP) {}
	protected void move() {}
	
	protected void moveSimple()
		{
			this.x += this.nx;
			this.y += this.ny;
			if (distance() > range) {
				remove();
			}
		}

	protected void moveDropArc()
		{
			x += nx;
			y += ny -= 1000;
			if (distance() > range) {
				// level.add(new RockSpawner((int) (x + nx), (int) (y + ny), 80, 1, level));
				remove();
			}
		}
	
	protected void moveArc() {
		this.range = 1000;
		//if (y + ny < yOrigin) {
		za += (VARS.Ag);
		if (zz < 0f) {
			zz = 0f;
			za *= -0f;
		}
		
		if (master_sprite == null) master_sprite = sprite;
		double deltaX = this.x - (this.x + this.nx);
		double deltaY = (this.y - (this.y + this.ny)) - (zz + za) / 900;
		this.angle = Math.atan2(deltaY, deltaX);
		this.x += this.nx;
		this.y += this.ny += (zz + za);
		this.sprite = Sprite.rotate(this.master_sprite, this.angle);	
		if (distance() > range) remove();
		//if (this.yOrigin < this.y) remove();
		if (time > 300) {
			remove();
		}
		//} else {
		//	moveSimple();
		//}
	}
	

	/*protected void PlayerCollision(List<PlayerMP> players, List<Projectile> projectiles, int width, int height) {
		for (int i = 0; i < players.size(); i++) {
			if (x < players.get(i).getX() + width
					&& x > players.get(i).getX() - width
					&& y <  players.get(i).getY() + height
					&& y >  players.get(i).getY() - height
					) {
				if (!players.get(i).invulnerable) {
				remove();
    			if (Game.getGame().gameState != gameState.INGAME_A) {
					players.get(i).mobhealth -= projectiles.get(i).damage;
				}
				players.get(i).incombat = true;
				level.add(new BleedSpawner((int) (x + nx), (int) (y + ny), 10, 8, level));
				if (projectiles.get(i).collisionSound != null) {
					Sound.Play(projectiles.get(i).collisionSound, false);
				}
				if (projectiles.get(i).breakParticle == 1) {
					level.add(new RockShatterSpawner((int) (x + nx), (int) (y + ny), 20, 4, level));
				}
				if (players.get(i).mobhealth <= 0){
					players.get(i).onPlayerDeath();
					Game.switchState(Game.getGame().gameState.DEATH);
					level.add(new ParticleSpawner((int) (x + nx), (int) (y + ny), 30000, 200, level));
					System.out.println("Player " + players.get(i) + " Died");
					Game.Dead = true;	
					}
				}
			}
		}
	}*/
}