package csc2a.px.model.shape;

import csc2a.px.model.visitor.IDrawVisitor;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

/**
 * @author JC Swanzen (220134523)
 * @version PXX
 *
 */
public class Circle extends Shape {
	private double r;
	
	
	public Circle(Color c, Point2D pos, double r) {
		super(c, ESHAPE_TYPE.CIRCLE, pos);
		this.r = r;
	}

	@Override
	public void draw(IDrawVisitor v, boolean hasFill) {
		v.visit(this, hasFill);
	}

	public double getR() { return r; }
	public Point2D getRefPos() { return new Point2D(pos.getX() - r, pos.getY() - r); }
	
	@Override
	public void setSize(double size) {
		super.setSize(size);
		r = size/2;
	}
}
