package csc2a.px.model.visitor;

import csc2a.px.model.shape.CarriageShape;
import csc2a.px.model.shape.Circle;
import csc2a.px.model.shape.Line;
import csc2a.px.model.shape.Rectangle;
import csc2a.px.model.shape.Triangle;

public interface IDrawVisitor {
	public void visit(Line l, boolean hasFill);
	public void visit(Circle c, boolean hasFill);
	public void visit(Rectangle r, boolean hasFill);
	public void visit(Triangle t, boolean hasFill);
	public void visit(CarriageShape cs, boolean hasFill);
}