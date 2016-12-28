package module5;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.ScreenPosition;
import processing.core.PGraphics;

/** Implements a visual marker for ocean earthquakes on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 *
 */
public class OceanQuakeMarker extends EarthquakeMarker {
	
	public UnfoldingMap location;

	public OceanQuakeMarker(PointFeature quake,UnfoldingMap map) {
		super(quake);
		
		// setting field in earthquake marker
		isOnLand = false;
		location=map;
	}

	private ScreenPosition getScreenPosition(Marker marker){
		return location.getScreenPosition(marker.getLocation());
	}
	
	

	/** Draw the earthquake as a square */
	@Override
	public void drawEarthquake(PGraphics pg, float x, float y) {
		pg.rect(x-radius, y-radius, 2*radius, 2*radius);		
		// Drawing lines between the threatenedCityMarkers and all the OceanQuakeMarker
		if (this.getClicked()){
			pg.stroke(pg.color(46, 255, 67));
			for (Marker marker: threatenedCityMarkers){
				lineBetweenMarker(pg, x, y, marker);
			}
			pg.stroke(pg.color(0, 0, 0));
		}
	
	}
	private void lineBetweenMarker(PGraphics pg, float x, float y, Marker marker) {
		// TODO Auto-generated method stub
		float X = ((CityMarker)marker).X;
		float Y = ((CityMarker)marker).Y;
		pg.line(x, y, X, Y);
		
	}
	

	

}
