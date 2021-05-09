package csc2a.px.model.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import csc2a.px.model.game.Map;
import csc2a.px.model.shape.ESHAPE_TYPE;
import csc2a.px.model.shape.Polygon;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class MapFileHandler {
	
	public static Map readMapFromFile(File handle) {
		Map map = null;
		DataInputStream reader = null;
		try {
			reader = new DataInputStream(new BufferedInputStream(new FileInputStream(handle)));
			
			// Read window width and height
			double ww = reader.readDouble();
			double wh = reader.readDouble();

			// Read Color Data for land
			int r = reader.readInt();
			int g = reader.readInt();
			int b = reader.readInt();
			Color landC = Color.rgb(r, g, b);
			
			// Read Color Data for river
			r = reader.readInt();
			g = reader.readInt();
			b = reader.readInt();
			Color riverC = Color.rgb(r, g, b);

			// Read Number of rivers
			int numRivers = reader.readInt();
			ArrayList<Polygon> rivers = new ArrayList<>();
			
			// Read river coordinates
			for (int i = 0; i < numRivers; i++) {
				// Read number of points
				int numPoints = reader.readInt();
				Point2D[] coords = new Point2D[numPoints];
				// Read each point's x and y
				for (int j = 0; j < numPoints; j++) {
					double x = reader.readDouble();
					double y = reader.readDouble();
					coords[j] = new Point2D(x, y);
				}
				Polygon river = new Polygon(riverC, ESHAPE_TYPE.POLYGON, coords[0]);
				river.setCoords(coords);
				rivers.add(river);
			}
			
			map = new Map(landC, riverC, ww, wh);
			map.setRivers(rivers);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return map;
	}
	
	public static boolean saveMapToFile(Map map, File handle) {
		boolean saved = false;
		DataOutputStream writer = null;
		try {
			writer = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(handle)));
			
			// Write window width and height
			writer.writeDouble(map.getWw());
			writer.writeDouble(map.getWh());
			
			// Write Color Data for land
			writer.writeInt((int) map.getLandC().getRed());
			writer.writeInt((int) map.getLandC().getGreen());
			writer.writeInt((int) map.getLandC().getBlue());
			
			// Write Color Data for river
			writer.writeInt((int) map.getRiverC().getRed());
			writer.writeInt((int) map.getRiverC().getGreen());
			writer.writeInt((int) map.getRiverC().getBlue());
			
			// Write Number of Rivers
			writer.writeInt(map.getRivers().size());
			
			// Write Coordinates for each river
			for (Polygon river : map.getRivers()) {
				// Write Number of Points
				writer.writeInt(river.getCoords().length);
				//Write each Point as x and y values
				for (Point2D point : river.getCoords()) {
					writer.writeDouble(point.getX());
					writer.writeDouble(point.getY());
				}
			}
			writer.flush();
			saved = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null)
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return saved;
	}
	
	
}