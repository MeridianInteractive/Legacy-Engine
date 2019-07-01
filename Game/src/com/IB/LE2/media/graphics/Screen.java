package com.IB.LE2.media.graphics;

import java.util.List;
import java.util.Random;

import com.IB.LE2.Boot;
import com.IB.LE2.input.hardware.Mouse;
import com.IB.LE2.media.graphics.lighting.TileLighting;
import com.IB.LE2.util.VARS;
import com.IB.LE2.util.Vector2i;
import com.IB.LE2.world.entity.Entity;
import com.IB.LE2.world.entity.mob.Mob;
import com.IB.LE2.world.entity.mob.Player;
import com.IB.LE2.world.entity.projectile.Projectile;
import com.IB.LE2.world.level.TileCoord;
import com.IB.LE2.world.level.tile.Tile;

public class Screen {

	public int width, height; // 20 TILES LONG
	public int[] pixels;
	public static int xOffset;
	public static int yOffset;
	private Random random = new Random();
	private static int s = 16;
	private static int ss = 15;
	public static int xo, yo;
	private int[][] lightlevels;
	public final static  int ALPHA_COL = 0xffFF00FF;
	
	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height] ; // 50,400
		//lightlevels = new int[(int)(Math.sqrt(Game.getGame().getLevel().tiles.length))][(int)(Math.sqrt(Game.getGame().getLevel().tiles.length))];
	}
	
	public void clear() {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
	}
	
	
	public void blendTiles(int xp, int yp, Tile tile1, Tile tile2){
	       xp -= xOffset;
	       yp -= yOffset;
	      
	       int tileDict1[] = new int[tile1.sprite.SIZE - 1];
	      int tileDict2[] = new int[tile2.sprite.SIZE - 1];
	         
	      for(int t1 = 0; t1 < tileDict1.length; t1++){
	         tileDict1[t1] = tile1.sprite.pixels[t1 * tile1.sprite.SIZE];
	      }
	      
	      for(int t2 = 0; t2 < tileDict2.length; t2++){
	         tileDict2[t2] = tile2.sprite.pixels[t2 * tile2.sprite.SIZE];
	      }
	      int col = 0;  
	      int type, color, colVarry;
	      for (int y = 0; y < tile1.sprite.SIZE; y++) {
	            int ya = y + yp;
	            for (int x = 0; x < tile1.sprite.SIZE; x++) {
	               int xa = x + xp;
	               if (xa < -tile1.sprite.SIZE || xa >= width || ya < 0 || ya >= height) break;
	               if (xa < 0) xa = 0;
	               type = random.nextInt(2 + y);
	               color = random.nextInt(15);
	               colVarry = random.nextInt(10);
	               if(type < 5) col = tileDict1[color];
	               if(type >= 5) col = tileDict2[color];
	              // col = MyColor.changeBrightness(col, Level.brightness, false);
	               pixels[xa + ya * width] = col;
	        }
	      }
	    }
	
	public void blendSprite(int xp, int yp, Sprite s1, Sprite s2){
	      xp -= xOffset;
	       yp -= yOffset;
	      
	       int tileDict1[] = new int[s1.SIZE - 1];
	      int tileDict2[] = new int[s2.SIZE - 1];
	         
	      for(int t1 = 0; t1 < tileDict1.length; t1++){
	         tileDict1[t1] = s1.pixels[t1 * s1.SIZE];
	      }
	      
	      for(int t2 = 0; t2 < tileDict2.length; t2++){
	         tileDict2[t2] = s2.pixels[t2 * s2.SIZE];
	      }
	      int col = 0;  
	      int type, color, colVarry;
	      for (int y = 0; y < s1.SIZE; y++) {
	            int ya = y + yp;
	            for (int x = 0; x < s1.SIZE; x++) {
	               int xa = x + xp;
	               if (xa < - s1.SIZE || xa >= width || ya < 0 || ya >= height) break;
	               if (xa < 0) xa = 0;
	               type = random.nextInt(2 + y);
	               color = random.nextInt(15);
	               colVarry = random.nextInt(10);
	               if(type < 5) col = tileDict1[color];
	               if(type >= 5) col = tileDict2[color];
	              // col = MyColor.changeBrightness(col, Level.brightness, false);
	               pixels[xa + ya * width] = col;
	            }
	      	}
	    }

	public void renderSheet(int xp, int yp, SpriteSheet sheet, boolean fixed) {
		if(fixed) {
		xp -= xOffset;
		yp -= yOffset;
		}
		for (int y = 0; y < sheet.SPRITE_HEIGHT; y++) {
			int ya = y + yp;
			for (int x = 0; x < sheet.SPRITE_WIDTH; x++) {
				int xa = x + xp;
				if (xa < 0 || xa >= width || ya < 0 || ya >= height) continue;
				int color = sheet.pixels[x + y * sheet.SPRITE_WIDTH];
				if (color != ALPHA_COL) {
				     pixels[xa + ya * width] = color;
				}
			}
		}
	}
	
	
	public void renderParallax(int bg_hex, SpriteSheet f, SpriteSheet s, SpriteSheet t, int yo) {
		int color = 0;
		for (int y = 0; y < height; y++) {
			int ya = y;
			for (int x = 0; x < width; x++) {
				int xa = x;
				if (xa < 0 || xa >= width || ya < 0 || ya >= height)
					continue;

				color = bg_hex;
				
				if (x + y * f.SPRITE_WIDTH < f.pixels.length) {
				int color_f = f.pixels[x + y * f.SPRITE_WIDTH];
				if (color_f != ALPHA_COL) { color = color_f; }
				}
				
				if (x + y * s.SPRITE_WIDTH < s.pixels.length) {
					int color_s = s.pixels[xa + ya * s.SPRITE_WIDTH];
					if (color_s != ALPHA_COL) { color = color_s; }
				}
				
				int color_t = t.pixels[xa + ya * t.SPRITE_WIDTH];
				if (color_t != ALPHA_COL) { color = color_t; }
				
				
				//System.out.println("Y: " + y + " YA: " + ya);


				pixels[xa + (ya + yo) * width] = color;
			}
		}
	}
	
	/**
	 * 		for (int y = 0; y < s.SPRITE_HEIGHT; y++) {
			int ya = y + sf;
			for (int x = 0; x < width; x++) {
				int xa = x;
				if (xa < 0 || xa >= width || ya < 0 || ya >= height) continue;
				color = s.pixels[x + y * s.SPRITE_WIDTH];
				if (color != ALPHA_COL) {
				     pixels[xa + ya * width] = color;
				}
			}
		}
	 */
	
	public void renderSprite(int xp, int yp, Sprite sprite, boolean fixed) {
		if(fixed) {
		xp -= xOffset;
		yp -= yOffset;
		}
		for (int y = 0; y < sprite.getHeight(); y++) {
			double ya = y + yp;
			for (int x = 0; x < sprite.getWidth(); x++) {
				int xa = x + xp;
				if (xa < 0 || xa >= width || ya < 0 || ya >= height) continue;
				int color = sprite.pixels[x + y * sprite.getWidth()];
				if (color != ALPHA_COL) {
				     pixels[(int) (xa + ya * width)] = (int) (color);//+ (0x110000 * ((x - y + Math.round(Math.random() * 10)) / 2)));
				}
			}
		}		
	}
	
	
	public void renderSprite(int xp, int yp, int xx, int yy, int w, int h, Sprite sprite, boolean fixed) {
		if(fixed) {
		xp -= xOffset;
		yp -= yOffset;
		}
		xp -= xx;
		yp -= yy;
		w += xx;
		h += yy;
		for (int y = yy; y < h; y++) {
			double ya = y + yp;
			for (int x = xx; x < w; x++) {
				int xa = x + xp;
				if (xa < 0 || xa >= width || ya < 0 || ya >= height) continue;
				int color = sprite.pixels[x + y * sprite.getWidth()];
				if (color != ALPHA_COL) { // 
				     pixels[(int) (xa + ya * width)] = color;
				}
			}
		}		
	}

	   private int blendColors(int[] rgb1, int[] rgb2){
		      int newR = (rgb1[1] * rgb1[0] + rgb2[1] * (255 - rgb1[0])) / 255;
		      int newG = (rgb1[2] * rgb1[0] + rgb2[2] * (255 - rgb1[0])) / 255;
		      int newB = (rgb1[3] * rgb1[0] + rgb2[3] * (255 - rgb1[0])) / 255;

		      return ((newR & 0xFF) << 16) | ((newG & 0xFF) << 8) | ((newB & 0xFF) << 0);
		   }
	
	   public boolean hasAlpha(int hex){
		      if(((hex >> 24) & 0xFF) < 255) return true;
		      return false;
	   }
	
	   private int[] hexToRGB(int hex){
		      int[] rgb = new int[4];
		      rgb[0] = (hex >> 24) & 0xFF;   //alpha
		      rgb[1] = (hex >> 16) & 0xFF;   //red
		      rgb[2] = (hex >> 8) & 0xFF;    //green
		      rgb[3] = (hex >> 0) & 0xFF;    //blue

		      return rgb;
		   }
	   
	   public void renderAlphaSprite(int xp, int yp, Sprite sprite) {
		   renderAlphaSprite(sprite, xp, yp);
		  // System.out.println("true");
	   }
	
	   public void renderAlphaSprite(Sprite sprite, int xp, int yp){
		   renderAlphaSprite(xp, yp, sprite, false);
	   }
	   
	   public void renderAlphaSprite(int xp, int yp, Sprite sprite, boolean fixed){
		   if(fixed) {
				xp -= xOffset;
				yp -= yOffset;
			}
		      for(int y = 0; y < sprite.getHeight(); y++){
		         int ya = y + yp;
		         for(int x = 0; x < sprite.getWidth(); x++){
		            int xa = x + xp;
		            if(xa < 0 || xa >= width || ya < 0 || ya >= height) continue;
		            int color = sprite.pixels[x + y * sprite.getWidth()];
		            if(color != ALPHA_COL) {
		            if(hasAlpha(color)){
		               int[] rgb1 = hexToRGB(color);            //spirte color as int array
		               int[] rgb2 = hexToRGB(pixels[xa + ya * width]);   //pixel color as int array
		               pixels[xa + ya * width] = blendColors(rgb1, rgb2);
		            }else{
		               pixels[xa + ya * width] = color;
		            }
		            }
		         }
		      }
		   }
	
	public void renderText(int xp, int yp, Sprite sprite, int color, boolean fixed) {
		if(fixed) {
		xp -= xOffset;
		yp -= yOffset;
		}
		for (int y = 0; y < sprite.getHeight(); y++) {
			double ya = y + yp;
			for (int x = 0; x < sprite.getWidth(); x++) {
				int xa = x + xp;
				if (xa < 0 || xa >= width || ya < 0 || ya >= height) continue;
				int col = sprite.pixels[x + y * sprite.getWidth()];
				if (col != ALPHA_COL) pixels[(int) (xa + ya * width)] = color;
				
				}
			}
		}		
	
	public void renderParticle(int xp, int yp, Sprite sprite, boolean fixed) {
	      int tilesx = xp;
	      int tilesy = yp;
		if(fixed) {
		xp -= xOffset;
		yp -= yOffset;
		}
		for (int y = 0; y < sprite.getHeight(); y++) {
			double ya = y + yp;
			for (int x = 0; x < sprite.getWidth(); x++) {
				int xa = x + xp;
				if (xa < 0 || xa >= width || ya < 0 || ya >= height) continue;
				int col = sprite.pixels[x + y * 16];
				 if (col != ALPHA_COL) {
					col = colSwitch(col, tilesx, tilesy);
	                pixels[(int) (xa + ya * width)] = col;
				 	}
				}
			}
		}		
	
public void renderTile(int xp, int yp, Sprite sprite) {
	xp -= xOffset;
	yp -= yOffset;
	for (int y = 0; y < sprite.SIZE; y++) {
		int ya = y + yp;
		for (int x = 0; x < sprite.SIZE; x++) {
			int xa = x + xp;
			if(xa < -sprite.SIZE || xa >= width || ya < 0 || ya >= height)break;
			if (xa < 0) xa = 0;
			int color = sprite.pixels[x + y * sprite.SIZE];
			//if (color != ALPHA_COL) { // 
			int col = sprite.pixels[x + y * sprite.SIZE];
			if (col != ALPHA_COL) pixels[(int) (xa + ya * width)] = col;
			//if (color != ALPHA_COL) pixels[xa + ya * width] = color;
				if (color != ALPHA_COL) {
					pixels[xa + ya * width] = color;
				    //color = MyColor.changeBrightness(color, Level.brightness, false);
				    pixels[(int) (xa + ya * width)] = color;
				}
	}
}
	}



	   public void renderTile(int xp, int yp, Tile tile) {
	      int tilesx = xp;
	      int tilesy = yp;
	      xp -= xOffset;
	      yp -= yOffset;
	      for (int y = 0; y <tile.sprite.SIZE; y++) {
	         int ya = y + yp;
	         for (int x = 0; x <tile.sprite.SIZE; x++) {
	            int xa = x + xp;
	            if (xa < -tile.sprite.SIZE || xa >= width || ya < 0 || ya >= height) break;
	            if (xa < 0) xa = 0;
	         //New way for Day / Night Cycles   
	            int col = tile.sprite.pixels[x + y * tile.sprite.SIZE];
	            //	col -= 0xffFFFFFF;
	            if (col != ALPHA_COL) {
					col = colSwitch(col, tilesx, tilesy);
	                  pixels[xa + ya * width] = col;
	            }
	         //--------------------------
	         //   pixels[xa + ya * width] = tile.sprite.pixels[x + y * tile.sprite.SIZE];
	         }
	      }
	   }   
	
		public static int Randomhex() {
			Random rand = new Random();
			int rhex = rand.nextInt(0x10) + 0x10; 
			return rhex;
		}
		
	   public void renderPlaceTile(int xp, int yp, Tile tile) {
		      int tilesx = xp;
		      int tilesy = yp;
		      xp -= xOffset;
		      yp -= yOffset;
		      for (int y = 0; y <tile.sprite.SIZE; y++) {
		         int ya = y + yp;
		         for (int x = 0; x <tile.sprite.SIZE; x++) {
		            int xa = x + xp;
		            if (xa < -tile.sprite.SIZE || xa >= width || ya < 0 || ya >= height) break;
		            if (xa < 0) xa = 0;
		         //New way for Day / Night Cycles   
		            int col = tile.sprite.pixels[x + y * tile.sprite.SIZE];
		            if (y == 0 || x == 0) {
		            	col = 0xff000000;
		            }
		            
		            if (y == tile.sprite.SIZE - 1|| x == tile.sprite.SIZE - 1) {
		            	col = 0xffFFFFFF;
		            }
		            
		           /* if (col == 0xff1F1F1F) {
		            	col += 0xff00FF00;
		            }*/
		            if (col != ALPHA_COL) {
						col = colSwitch(col, tilesx, tilesy);
		                  pixels[xa + ya * width] = col;
		            }
		         //--------------------------
		         //   pixels[xa + ya * width] = tile.sprite.pixels[x + y * tile.sprite.SIZE];
		         }
		      }
		   }   
	   

public void renderMob(double x2, double y2, Mob mob) {
	  int tilesx = (int) x2;
      int tilesy = (int) y2;
	x2 -= xOffset;
	y2 -= yOffset;
	for (int y = 0; y < 16; y++) {
		double ya = y + y2;
		int ys = y;
		for (int x = 0; x < 16; x++) {
			double xa = x + x2;
			int xs = x;
			if(xa < -16 || xa >= width || ya < 0 || ya >= height)break;
			if (xa < 0) xa = 0;
		//	if ((mob instanceof Tracker) && col == 0xffFF0C00) col = 0xffFF0C00;
		//	if (col != ALPHA_COL) pixels[(int) (xa + ya * width)] = col;
			int col = mob.getSprite().pixels[xs + ys * 16];
			   if (col != ALPHA_COL) {
					col = colSwitch(col, tilesx, tilesy);


					
	                  pixels[(int) (xa + ya * width)] = col;
	            }
		}
	}
}
public void render32Mob(double x2, double y2, Mob mob) {
	int tilesx = (int) x2;
	int tilesy = (int) y2;
	x2 -= xOffset;
	y2 -= yOffset;
	for (int y = 0; y < 32; y++) {
		double ya = y + y2;
		int ys = y;
		for (int x = 0; x < 32; x++) {
			double xa = x + x2;
			int xs = x;
			if(xa < -32 || xa >= width || ya < 0 || ya >= height)break;
			if (xa < 0) xa = 0;
			int col = mob.getSprite().pixels[xs + ys * 32];
		//	if ((mob instanceof Tracker) && col == 0xffFF0C00) col = 0xffFF0C00;
			 if (col != ALPHA_COL) {
					col = colSwitch(col, tilesx, tilesy);

                 pixels[(int) (xa + ya * width)] = col;
           }

		}
	}
}

	public void renderMobSprite(int xp, int yp, Sprite sprite) {
		int tilesx = xp;
		int tilesy = yp;
		xp -= xOffset;
		yp -= yOffset;
		for (int y = 0; y < 16; y++) {
			int ya = y + yp;
			for (int x = 0; x < 16; x++) {
				int xa = x + xp;
				if(xa < -16 || xa >= width || ya < 0 || ya >= height)break;
				if (xa < 0) xa = 0;
				int col = sprite.pixels[x + y * 16];
				 if (col != ALPHA_COL) {
						col = colSwitch(col, tilesx, tilesy);
	                  pixels[(int) (xa + ya * width)] = col;
	            }
		}

			}
	}
	
	public void renderMobSprite(int xp, int yp, Mob mob) {
		Sprite sprite = mob.getSprite();
		int tilesx = xp;
		int tilesy = yp;
		xp -= xOffset;
		yp -= yOffset;
		for (int y = 0; y < 16; y++) {
			int ya = y + yp;
			for (int x = 0; x < 16; x++) {
				int xa = x + xp;
				if(xa < -16 || xa >= width || ya < 0 || ya >= height)break;
				if (xa < 0) xa = 0;
				int col = sprite.pixels[x + y * 16];
				 if (col != ALPHA_COL) {
						col = colSwitch(col, tilesx, tilesy);
						if (mob.hurt) {
							col = 0xffFF0000;
						}
	                  pixels[(int) (xa + ya * width)] = col;
	            }
		}

			}
	}
	
	public void renderMobSpriteUniversal(int xp, int yp, Sprite sprite) {
		int tilesx = xp;
		int tilesy = yp;
		xp -= xOffset;
		yp -= yOffset;
		for (int y = 0; y < sprite.getHeight(); y++) {
			int ya = y + yp;
			for (int x = 0; x < sprite.getWidth(); x++) {
				int xa = x + xp;
				if(xa < -sprite.getWidth() || xa >= width || ya < 0 || ya >= height)break;
				if (xa < 0) xa = 0;
				int col = sprite.pixels[x + y * sprite.getWidth()];
				 if (col != ALPHA_COL) {
						col = colSwitch(col, tilesx, tilesy);

	                  pixels[(int) (xa + ya * width)] = col;
	            }
			}
		}
	}
	
	public void renderMobSpriteSwimming(int xp, int yp, Sprite sprite) {
		int tilesx = xp;
		int tilesy = yp;
		xp -= xOffset;
		yp -= yOffset;
		for (int y = 0; y < sprite.getHeight() / 1.4; y++) {
			int ya = y + yp;
			for (int x = 0; x < sprite.getWidth(); x++) {
				int xa = x + xp;
				if(xa < -sprite.getWidth() || xa >= width || ya < 0 || ya >= height / 1.4)break;
				if (xa < 0) xa = 0;
				int col = sprite.pixels[x + y * sprite.getWidth()];
				 if (col != ALPHA_COL) {
						col = colSwitch(col, tilesx, tilesy);

	                  pixels[(int) (xa + ya * width)] = col;
				}
			}
		}
	}

	public void renderMobSpriteUniversal(int xp, int yp, Mob mob) {
		Sprite sprite = mob.getSprite();
		int tilesx = xp;
		int tilesy = yp;
		xp -= xOffset;
		yp -= yOffset;
		for (int y = 0; y < sprite.getHeight(); y++) {
			int ya = y + yp;
			for (int x = 0; x < sprite.getWidth(); x++) {
				int xa = x + xp;
				if(xa < -sprite.getWidth() || xa >= width || ya < 0 || ya >= height)break;
				if (xa < 0) xa = 0;
				int col = sprite.pixels[x + y * sprite.getWidth()];
				 if (col != ALPHA_COL) {
						col = colSwitch(col, tilesx, tilesy);
						if (mob.hurt) {
							col = 0xffFF0000;
						}
	                  pixels[(int) (xa + ya * width)] = col;
				}
			}
		}
	}
	
	public void renderMobSpriteUniversal(int xp, int yp, Sprite sprite, int rr, int gg, int bb) {
		int tilesx = xp;
		int tilesy = yp;
		xp -= xOffset;
		yp -= yOffset;
		for (int y = 0; y < sprite.getHeight(); y++) {
			int ya = y + yp;
			for (int x = 0; x < sprite.getWidth(); x++) {
				int xa = x + xp;
				if(xa < -sprite.getWidth() || xa >= width || ya < 0 || ya >= height)break;
				if (xa < 0) xa = 0;
				int col = sprite.pixels[x + y * sprite.getWidth()];
				 if (col != ALPHA_COL) {
				      int r = (col & 0xff0000) >> 16;
				      int g = (col & 0xff00) >> 8;
				      int b = (col & 0xff);
				      if (r < 0) r = 0;
				      if (g < 0) g = 0;
				      //if (b < 0) b = 0;
				      if (r > 255) r = 255;
				      if (g > 255) g = 255;
				      if (b > 255) b = 255;
				      
					 //col = colSwitch(col, tilesx, tilesy);
						col = (r + rr)<< 16 | (g + gg) << 8 | b + bb;
						//col = MyColor.tint(col, r, g, b);

	                  pixels[(int) (xa + ya * width)] = col;
				}
			}
		}
	}
	
	
	public void renderAI(int xp, int yp, Sprite sprite) {
		int tilesx = xp;
		int tilesy = yp;
		xp -= xOffset;
		yp -= yOffset;
		for (int y = 0; y < 16; y++) {
			int ya = y + yp;
			for (int x = 0; x < 16; x++) {
				int xa = x + xp;
				if(xa < -16 || xa >= width || ya < 0 || ya >= height)break;
				if (xa < 0) xa = 0;
				int col = sprite.pixels[x + y * 16];
				 if (col != ALPHA_COL) {
						col = colSwitch(col, tilesx, tilesy);

	                  pixels[(int) (xa + ya * width)] = col;
	            }
		}

			}
		}
	
	public void renderLight(int xp, int yp, int radius) {
	      int col = 0;
	      xp -= xOffset;
	      yp -= yOffset;

	      for (int y = 0; y < radius * 2; y++) {
	         int ya = y + yp;
	         for (int x = 0; x < radius * 2; x++) {
	            int xa = x + xp;
	            if (xa < 0 || xa >= this.width || ya < 0 || ya >= this.height)
	               continue;
	            int d = (int) Math.sqrt((y - radius) * (y - radius) + (x - radius) * (x - radius));
	            if (d < radius) {
	               col = pixels[xa + ya * this.width];
	               //col = TileLighting.changeBrightness(col, Level.brightness, true);
	               pixels[xa + ya * this.width] = col;
	            }
	         }
	      }
	   }
	
	public void renderLight(int xp, int yp, int radius, int r, int g, int b) {
        int col = 0;
        xp -= xOffset;
        yp -= yOffset;
        
        for (int y = 0; y < radius * 2; y++) {
           int ya = y + yp;
           for (int x = 0; x < radius * 2; x++) {
              int xa = x + xp;
              if (xa < 0 || xa >= this.width || ya < 0 || ya >= this.height)
                 continue;
              int d = (int) Math.sqrt((y - radius) * (y - radius) + (x - radius) * (x - radius));
              if (d < radius) {
                 col = pixels[xa + ya * this.width];
                 //col = TileLighting.changeBrightnessNegative(col, Level.brightness, r - d, g - d, b - d);                
                 pixels[xa + ya * this.width] = col;
              }
           }
        }

	}
	
	
	
	
	public void renderLight(int xp, int yp, int radius, int r, int g, int b, boolean random) {
        int col = 0;
        xp -= xOffset;
        yp -= yOffset;
        
        for (int y = 0; y < radius * 2; y++) {
           int ya = y + yp;
           for (int x = 0; x < radius * 2; x++) {
              int xa = x + xp;
              if (xa < 0 || xa >= this.width || ya < 0 || ya >= this.height)
                 continue;
              int d = (int) Math.sqrt((y - radius) * (y - radius) + (x - radius) * (x - radius));
              if (d == radius) {
                 col = pixels[xa + ya * this.width];
                // col = TileLighting.changeBrightnessNegative(col, Level.brightness, r - d, g - d, b - d);                
                 pixels[xa + ya * this.width] = col;
              }
           }
        }

	}	
	
	public void renderLight(int xp, int yp, int radius, int r, int g, int b, String name) {
		 int col = 0;
	        xp -= xOffset;
	        yp -= yOffset;
	        
	        for (int y = 0; y < radius * 2; y++) {
	           int ya = y + yp;
	           for (int x = 0; x < radius * 2; x++) {
	              int xa = x + xp;
	              if (xa < 0 || xa >= this.width || ya < 0 || ya >= this.height)
	                 continue;
	              int d = (int) Math.sqrt((y - radius) * (y - radius) + (x - radius) * (x - radius));
	              if (d < radius) {
		double px1 = Boot.get().getPlayer().x();
        double py1 = Boot.get().getPlayer().y();
        double px2 = Boot.get().getPlayer().x() + 16;
        double py2 = Boot.get().getPlayer().y() + 32;
        double px3 = Boot.get().getPlayer().x() + 8;
        double py3 = Boot.get().getPlayer().y() + 16;
        
        double ang1R = Math.atan((py1 - (yp + radius)) /(px1 - (xp + radius)));
        double ang1P = ang1R + Math.PI;
        double ang1D = Math.toDegrees(ang1R);
        double ang2R = Math.atan((py2 - (yp + radius)) /(px2 - (xp + radius)));
        double ang2P = ang2R + Math.PI;
        double ang2D = Math.toDegrees(ang2R);
        
        double sectAng = ang2D - ang1D;
        double sectArea1 = (sectAng * Math.PI * radius * radius) / 360;
        double pDist = Math.sqrt(((px3 - (xp + radius)) * (px3 - (xp + radius))) + ((py3 - (yp + radius)) * (py3 - (yp + radius))));
        double sectArea2 = (sectAng * Math.PI * pDist * pDist) / 360;
  //      double shadowArea = sectArea1 - sectArea2;
        
        double testAngR = Math.atan((double)(ya + radius)/(double)(xa + radius));
        double testAngP = testAngR + Math.PI;
        double testAngD = Math.toDegrees(testAngR);

        if(testAngP < ang1P || testAngP > ang2P){
           double testDist = Math.sqrt(((double)(xa + radius)) * (double)(xa + radius) + (double)(ya + radius) * (double)(ya + radius));

           col = pixels[xa + ya * this.width];
          // col = TileLighting.changeBrightnessNegative(col, Level.brightness, r - (d - (radius/3 + 20)), g - (d - (radius/3 + 20)), b - (d - (radius/3 + 20)));
           pixels[xa + ya * this.width] = col;

        }
	   }
	}
}
}
	  public void renderLight(int xp, int yp, int radius, Mob mob, int r, int g, int b) {
          int col = 0;
          xp -= xOffset;
          yp -= yOffset;

          for (int y = 0; y < radius * 2; y++) {
             int ya = y + yp;
             for (int x = 0; x < radius * 2; x++) {
                int xa = x + xp;
                if (xa < 0 || xa >= this.width || ya < 0 || ya >= this.height)
                   continue;
                int d = (int) Math.sqrt((y - radius) * (y - radius) + (x - radius) * (x - radius));
                if (d < radius) {
                   int d2;
                   if(mob.dir == mob.dir.LEFT) d2 = (int) (((y - radius) * (y - radius)) / (10)) - ((x - radius) * -4);
                   else if(mob.dir == mob.dir.RIGHT) d2 = (int) (((y - radius) * (y - radius)) / (10)) - ((x - radius) * 4);
                   else if(mob.dir == mob.dir.UP) d2 = (int) (((x - radius) * (x - radius)) / (10)) - ((y - radius) * -4);
                   else if(mob.dir == mob.dir.DOWN) d2 = (int) (((x - radius) * (x - radius)) / (10)) - ((y - radius) * 4);
                   else if(mob.dir == mob.dir.UP) d2 = (int) (((x - radius) * (x - radius)) / (10)) - ((y - radius) * -4);
                   else if(mob.dir == mob.dir.DOWN) d2 = (int) (((x - radius) * (x - radius)) / (10)) - ((y - radius) * -4);
                   else if(mob.dir == mob.dir.LEFT) d2 = (int) (((x - radius) * (x - radius)) / (10)) - ((y - radius) * -4);
                   else if(mob.dir == mob.dir.RIGHT) d2 = (int) (((x - radius) * (x - radius)) / (10)) - ((y - radius) * 4);
                   else d2 = -1;
                   if(d2 < radius){
                   col = pixels[xa + ya * this.width];
                   col = TileLighting.tint(col, r, g, b);
                   pixels[xa + ya * this.width] = col;
                   }
                }
             }
          }

       }
	
	
	public void renderProjectile(int xp, int yp, Projectile p) {
		int tilesx = xp;
		int tilesy = yp;
		xp -= xOffset;
		yp -= yOffset;
		for (int y = 0; y < p.getSpriteSize(); y++) {
			int ya = y + yp;
			for (int x = 0; x < p.getSpriteSize(); x++) {
				int xa = x + xp;
				if(xa < -p.getSpriteSize() || xa >= width || ya < 0 || ya >= height)break;
				if (xa < 0) xa = 0;
				int col = p.getSprite().pixels[x + y * p.getSpriteSize()];
				 if (col != ALPHA_COL) {
					col = colSwitch(col, tilesx, tilesy);
	                  pixels[(int) (xa + ya * width)] = col;
				}
			}
		}
	}
	
	public void drawCir(int xp, int yp, int radius, int color, boolean fixed) {
		if (fixed) {
	        xp -= xOffset;
	        yp -= yOffset;
		}
		
		xp -= radius;
		yp -= radius;
		
		int col = 0;   
		for (int y = 0; y < radius * 2; y++) {
	           int ya = y + yp;
	           for (int x = 0; x < radius * 2; x++) {
	              int xa = x + xp;
	              if (xa < 0 || xa >= this.width || ya < 0 || ya >= this.height)
	                 continue;
	              int d = (int) Math.sqrt((y - radius) * (y - radius) + (x - radius) * (x - radius));
	              if (d == radius - 1) {
	                 col = pixels[xa + ya * this.width];
	                 col = color;
	                 pixels[xa + ya * this.width] = col;
	              }
	           }
	        }
	}
	
	public void drawRect(int xp, int yp, int width, int height, int color, boolean fixed) {
		if(fixed) {
			xp -= xOffset;
			yp -= yOffset;
		}
		for(int x = xp; x < xp + width; x++) {
			if (x < 0 | x >= this.width ||yp >= this.height) continue;
			if (yp > 0) pixels[x + yp * this.width] = color;
			if (yp + height >= this.height) continue;
			if (yp + height > 0) pixels[x + (yp + height) * this.width] = color;

		}
		for(int y = yp; y <= yp + height; y++) {
			if (xp >= this.width || y < 0 || y >= this.height) continue;
			if (xp > 0) pixels[xp + y * this.width] = color;
			if (xp + width >= this.width) continue;
			if (xp + width > 0) pixels[(xp + width) + y * this.width] = color;			

		}
	}

	public void drawFillRect(int xp, int yp, int width, int height, int color, boolean fixed) {
		if (fixed) {
			xp -= xOffset;
			yp -= yOffset;
		}
		for (int y = 0; y < height; y++) {
			int ya = y + yp;
			for (int x = 0; x < width; x++) {
				int xa = x + xp;
				if (xa < 0 || xa >= this.width || ya < 0 || ya >= this.height) continue;
				int col = color;
				pixels[xa + ya * this.width] = col;
			}
		}
	}
	
	public void drawFillRect(int xp, int yp, int width, int height, int color, int border_color, boolean fixed) {
		if (fixed) {
			xp -= xOffset;
			yp -= yOffset;
		}
		for (int y = 0; y < height; y++) {
			int ya = y + yp;
			for (int x = 0; x < width; x++) {
				int xa = x + xp;
				if (xa < 0 || xa >= this.width || ya < 0 || ya >= this.height) continue;
				int col = color;
				
				if (x == 0 || y == 0 || x == width - 1 || y == height - 1)
					col = border_color;
				
				pixels[xa + ya * this.width] = col;
			}
		}
	}
	
	public void drawVectors(List<Vector2i> list, int color, boolean fixed){
		   for(Vector2i vec : list){
		      int xPixel = vec.getX();
		      int yPixel = vec.getY();
		      if(fixed){
		         xPixel -= xOffset;
		         yPixel -= yOffset;
		      }
		      if(xPixel < 0 || xPixel >= this.width || yPixel < 0 || yPixel >= this.height) continue;
		      pixels[xPixel + yPixel * this.width] = color;
		   }
		}
	
public void setOffset(int xOffset, int yOffset) {
	this.xOffset = xOffset;
	this.yOffset = yOffset;
	xo = (Mouse.getX() / Boot.scale + xOffset) / TileCoord.TILE_SIZE;
    yo = (Mouse.getY() / Boot.scale + yOffset) / TileCoord.TILE_SIZE;
	}


	public int colSwitch(int col, int tilesx, int tilesy) {
		return col;
	}

	  public void fade(int r, int g, int b) {
		   int col = 0;
		      for (int y = 0; y < height * 2; y++) {
		         int ya = y;
		         for (int x = 0; x < width * 2; x++) {
		            int xa = x;
		            if (xa < 0 || xa >= this.width || ya < 0 || ya >= this.height)
		               continue;
		               col = pixels[xa + ya * this.width];
		               //col = TileLighting.changeBrightnessNegative(col, Level.brightness, r, g, b); 
		               pixels[xa + ya * this.width] = col;
		         }
		      }
		      }
		      
		      
		      
		      public void fade(int r, int g, int b, int timer) {
		    	  int col = 0;
				      for (int y = 0; y < height * 2; y++) {
				         int ya = y;
				         for (int x = 0; x < width * 2; x++) {
				            int xa = x;
				            if (xa < 0 || xa >= this.width || ya < 0 || ya >= this.height)
				               continue;
				               col = pixels[xa + ya * this.width];
				               //col = TileLighting.changeBrightnessNegative(col, Level.brightness, r, g, b); 
				               pixels[xa + ya * this.width] = col;
				         }
				      }
		      
	}

	@Deprecated //TODO: rewrite getEntities in level
	public void drawLine(Player player, List<Entity> entities) {
		int xp = (int) player.x();
		int yp = (int) player.y();
		xp -= xOffset;
		yp -= yOffset;
		int col = 0;
		//entities = Boot.get().getLevel().getEntities(player, 20);
		for (int i = 0; i < entities.size(); i++) {
			for (int y = (int) player.y(); y < entities.get(i).y(); y++) {
				int ya = y + yp;
				for (int x = (int) player.x(); x < entities.get(i).x(); x++) {
					int xa = x + xp;
					if (xa < 0 || xa >= this.width || ya < 0 || ya >= this.height)
						continue;
					col = pixels[xa + ya * this.width];
					pixels[xa + ya * this.width] = col;
				}
			}

		}
	}
	
	int renderX = 0, renderY = 0;

	public void renderMiniMap(int xp, int yp, int size) {
		int minimapSize = size;
		int xoff = 0, yoff = 0;
		try {
			Sprite map;
			int renderXt = (((int) Boot.get().getPlayer().x() >> VARS.TILE_BIT_SHIFT) - (minimapSize / 2));
			int renderYt = (((int) Boot.get().getPlayer().y() >> VARS.TILE_BIT_SHIFT) - (minimapSize / 2));

			int px = (((int) Boot.get().getPlayer().x() >> VARS.TILE_BIT_SHIFT) - renderXt);
			int py = (((int) Boot.get().getPlayer().y() >> VARS.TILE_BIT_SHIFT) - renderYt);
			int ex = 0, ey = 0;
			List<Entity> e = Boot.get().getLevel().entities;

			while (renderXt > 0) {
				renderX = renderXt;
				break;
			}

			while (renderXt > 0) {
				renderY = renderYt;
				break;
			}

			if (renderX <= 0) {
				renderX = 0;
			}
			if (renderY <= 0) {
				renderY = 0;
			}

			if (renderX >= Boot.get().getLevel().width - minimapSize) {
				renderX = Boot.get().getLevel().width - minimapSize;
			}
			if (renderY >= Boot.get().getLevel().height - minimapSize) {
				renderY = Boot.get().getLevel().height - minimapSize;
			}

			map = new Sprite(minimapSize, renderX, renderY, null, 7);

			for (int y = 0; y < map.SIZE; y++) {
				double ya = y + yp;
				for (int x = 0; x < map.SIZE; x++) {
					int xa = x + xp;
					if (xa < 0 || xa >= width || ya < 0 || ya >= height)
						continue;
					map.pixels[px + py * map.getWidth()] = 0xff191970;
					if (size == 75) {
						double dx = x - size / 2;
						double dy = y - size / 2;
						double dist = dx * dx + dy * dy;

						int color = 0;
						if (dist <= 1024) {
							color = map.pixels[x + y * map.getWidth()];
							if (dist >= 900) {
								color = 0xff000000;
							}
						}
						if (dist <= 1024) {
							pixels[(int) (xa + ya * width)] = color;
						}
					} else {
						int color = map.pixels[x + y * map.getWidth()];
						pixels[(int) (xa + ya * width)] = color;
					}
				}
			}
		} catch (Exception e) {
		}
	}
}





























