package com.IB.LE2.util.math;

import com.IB.LE2.Boot;
import com.IB.LE2.asset.graphics.Screen;
import com.IB.LE2.util.Debug;

public class PVector
{

	private double x;
	private double y;

	public PVector()
		{
			set(0, 0);
		}

	public PVector(PVector vec)
		{
			set(vec);
		}

	public PVector(double x, double y)
		{
			set(x, y);
		}

	public void set(double x, double y)
		{
			this.x = x;
			this.y = y;
		}

	public void set(PVector vec)
		{
			this.x = vec.x;
			this.y = vec.y;
		}

	public PVector add(PVector vector)
		{
			this.x += vector.x;
			this.y += vector.y;
			return this;
		}

	public PVector addResult(PVector vector)
		{
			PVector p = new PVector(x + vector.x, y + vector.y);
			return p;
		}

	public PVector subtract(PVector vector)
		{
			this.x += vector.x;
			this.y += vector.y;
			return this;
		}
	
	public double calcMagn()
		{
			return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		}

	public boolean equals(Object object)
		{
			if (!(object instanceof PVector)) return false;
			PVector vec = (PVector) object;
			if (vec.getX() == this.getX() && vec.getY() == this.getY()) return true;
			return false;
		}

	public int hashCode()
		{
			assert false : "hashCode not designed";
			return 42; // any arbitrary constant will do
		}

	public String toString()
		{
			return "<" + x + ", " + y + ">";
		}

	public void draw(Screen screen)
		{
			Debug.drawTriangle(screen, Boot.get().font8bit, x, y, x * 1.5, y * 1.5);
		}
	
	public void drawVectors(Screen screen, PVector v)
		{
			Debug.drawLine(screen, (int)x(), (int)y(), (int)v.x(), (int)v.y(), 0xFF00FF, true);
		}
	
	public double dot(PVector v)
		{
			return (this.x() * v.x()) + (this.y() * v.y());
		}
	
	
	
	public double getX() { return x; }
	public double getY() { return y; }
	
	public double x() { return getX(); }
	public double y() { return getY(); }	
	
	public void x(double val) { this.setX(val); }
	public void y(double val) { this.setY(val); }
	
	public PVector setX(double x)
		{
			this.x = x;
			return this;
		}

	public PVector setY(double y)
		{
			this.y = y;
			return this;
		}
}
