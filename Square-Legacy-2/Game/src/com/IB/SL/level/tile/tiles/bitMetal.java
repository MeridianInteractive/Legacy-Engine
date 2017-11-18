package com.IB.SL.level.tile.tiles;

import com.IB.SL.VARS;
import com.IB.SL.graphics.Screen;
import com.IB.SL.graphics.Sprite;
import com.IB.SL.level.Level;
import com.IB.SL.level.tile.Tile;

public class bitMetal extends Tile {

	public bitMetal(Sprite sprite) {
		super(sprite);

	}
	
	public void render(int x, int y, Screen screen) {
		screen.renderTile(x << VARS.TILE_BIT_SHIFT, y << VARS.TILE_BIT_SHIFT, bitMetal);
	


}
	
	public boolean solid() {
		return false;
}
	
	public boolean solidtwo() {
		return false;
}	
	public int  getHex() {
		return Level.bitMetalHex;
	}
}