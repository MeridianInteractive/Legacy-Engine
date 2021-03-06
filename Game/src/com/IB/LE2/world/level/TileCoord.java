package com.IB.LE2.world.level;

public class TileCoord {

	private int x, y;
	private int tx, ty;
	public final static int TILE_SIZE = 32;
	
	public TileCoord(int x, int y) {
		this.tx = x;
		this.ty = y;
		this.x = x * getTILE_SIZE();
		this.y = y * getTILE_SIZE();
	}
	
	public int tx() {
		return tx;
	}
	
	public int ty() {
		return ty;
	}
	
	public int x() {
		return x;
	}
	
	public int y() {
		return y;
	}
	
	public int[] xy() {
		int[] r = new int[2];
		r[0] = x;
		r[1] = y;
		return r;
	}



	public static int getTILE_SIZE() {
		return TILE_SIZE;
	}
	
	
	
}
