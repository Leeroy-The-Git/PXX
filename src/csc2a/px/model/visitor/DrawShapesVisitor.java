package csc2a.px.model.visitor;

import csc2a.px.model.Common;
import csc2a.px.model.game.Carriage;
import csc2a.px.model.shape.Circle;
import csc2a.px.model.shape.Line;
import csc2a.px.model.shape.Polygon;
import csc2a.px.model.shape.Rectangle;
import csc2a.px.model.shape.Triangle;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Rotate;


/**
 * @author JC Swanzen (220134523)
 * @version PXX
 *
 */
public class DrawShapesVisitor implements IDrawVisitor {
	private GraphicsContext gc;
	
	/**
	 * @param gc the gc to set
	 */
	public void setGc(GraphicsContext gc, double w, double h) {
		this.gc = gc;
		this.gc.clearRect(0, 0, w, h);
	}
	
	@Override
	public void visit(Line l) {
		clear();
		gc.setStroke(l.getC());
		gc.setLineWidth(5);
		
		for (int i = 1; i < l.getxCoords().length; i++) {
			gc.strokeLine(l.getxCoords()[i - 1], l.getyCoords()[i - 1], l.getxCoords()[i], l.getyCoords()[i]);
		}
		
		gc.stroke();
	}

	@Override
	public void visit(Circle c, boolean hasFill) {
		clear();
		gc.setLineWidth(3);
		if (hasFill) {
			gc.setFill(c.getC());
			gc.fillOval(c.getRefPos().getX(), c.getRefPos().getY(), c.getR() * 2, c.getR() * 2);
		} else {
			gc.setStroke(c.getC());
			gc.strokeOval(c.getRefPos().getX(), c.getRefPos().getY(), c.getR() * 2, c.getR() * 2);
		}
	}

	@Override
	public void visit(Rectangle r, boolean hasFill) {
		clear();
		gc.setLineWidth(3);
		if (hasFill) {
			gc.setFill(r.getC());
			gc.fillRect(r.getPos().getX() - r.getW()/2, r.getPos().getY() - r.getH()/2, r.getW(), r.getH());
		} else {
			gc.setStroke(r.getC());
			gc.strokeRect(r.getPos().getX() - r.getW()/2, r.getPos().getY() - r.getH()/2, r.getW(), r.getH());
		}
	}

	@Override
	public void visit(Triangle t, boolean hasFill) {
		clear();
		gc.setLineWidth(3);
		if (hasFill) {
			gc.setFill(t.getC());
			gc.fillPolygon(t.getxCoords(), t.getyCoords(), 3);
		} else {
			gc.setStroke(t.getC());
			gc.strokePolygon(t.getxCoords(), t.getyCoords(), 3);
		}		
	}

	@Override
	public void visit(Carriage c) {
		gc.save();
		
		gc.setEffect(Common.getColorAdjust(c.getC()));
		
		transformContext(c);
		gc.drawImage(c.getCarriageImage(), c.getRefPos().getX(), c.getRefPos().getY(), c.getW(), c.getH());
		gc.restore();
		clear();
	}
	
	public void clear() {
		gc.setEffect(null);
	}
	
	
	/**
	 * @param c Carriage to transform context for
	 * This function rotates the transform to ensure a carriage is rendered in the right direction
	 */
	private void transformContext(Carriage c){
        Point2D centre = c.getCenter();
        Rotate r = new Rotate(c.getRotation() + 90, centre.getX(), centre.getY());
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }

	@Override
	public void visit(Polygon p, boolean hasFill) {
		clear();
		gc.setLineWidth(3);
		if (hasFill) {
			gc.setFill(p.getC());
			gc.fillPolygon(p.getxCoords(), p.getyCoords(), p.getCoords().length);
		} else {
			gc.setStroke(p.getC());
			gc.strokePolygon(p.getxCoords(), p.getyCoords(), p.getCoords().length);
		}
	}
	
}
