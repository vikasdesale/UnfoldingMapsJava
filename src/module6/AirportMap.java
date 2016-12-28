package module6;

import java.util.*;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.geo.Location;
import parsing.ParseFeed;
import processing.core.PApplet;

/** An applet that shows airports (and routes)
 * on a world map.  
 * @author Adam Setters and the UC San Diego Intermediate Software Development
 * MOOC team
 *
 */
public class AirportMap extends PApplet {
	
	UnfoldingMap map;
	private List<Marker> airList;
	List<Marker> routeList;
	
	private CommonMarker lastSelected;
	private CommonMarker lastClicked;
	
	private HashMap<Integer, AirportMarker> airRoutes;
	
	public void setup() {
		// setting up PAppler
		size(850,850, OPENGL);
		
		// setting up map and default events
		map = new UnfoldingMap(this, 200, 20, 850, 650);
		MapUtils.createDefaultEventDispatcher(this, map);
		
		// get features from airport data
		List<PointFeature> features = ParseFeed.parseAirports(this, "airports.dat");
		
		// list for markers, hashmap for quicker access when matching with routes
		airList = new ArrayList<Marker>();
		HashMap<Integer, Location> airports = new HashMap<Integer, Location>();
		airRoutes = new HashMap<Integer, AirportMarker>();
		
		// create markers from features
		for(PointFeature feature : features) {
			AirportMarker m = new AirportMarker(feature);
			//System.out.println(m);
	
			m.setRadius(3);
			airList.add(m);
			
			// put airport in hashmap with OpenFlights unique id for key
			airports.put(Integer.parseInt(feature.getId()), feature.getLocation());
			airRoutes.put(Integer.parseInt(feature.getId()), m);
		
		}
		
		
		// parse route data
		List<ShapeFeature> routes = ParseFeed.parseRoutes(this, "routes.dat");
		routeList = new ArrayList<Marker>();
		for(ShapeFeature route : routes) {
			
			// get source and destination airportIds
			int source = Integer.parseInt((String)route.getProperty("source"));
			int dest = Integer.parseInt((String)route.getProperty("destination"));
			
			// get locations for airports on route
			if(airports.containsKey(source) && airports.containsKey(dest)) {
				route.addLocation(airports.get(source));
				route.addLocation(airports.get(dest));
			}
			
			SimpleLinesMarker sl = new SimpleLinesMarker(route.getLocations(), route.getProperties());
		
			//System.out.println(sl.getProperties());
			
			sl.setHidden(true);
			routeList.add(sl);
			if (airRoutes.containsKey(source) && airRoutes.containsKey(dest)) {
				//System.out.println(airRoutes.get(dest).getLocation());
				airRoutes.get(source).addRoute(sl);
				airRoutes.get(dest).addRoute(sl);
			}
			
		}
		
		sortAndPrint(20);
		
		//UNCOMMENT IF YOU WANT TO SEE ALL ROUTES
		map.addMarkers(routeList);
		
		map.addMarkers(airList);
		
	}
	
	public void draw() {
		background(200);
		map.draw();
		addKey();
		
	}
	
	/** Event handler that gets called automatically when the 
	 * mouse moves.
	 */
	@Override
	public void mouseMoved()
	{
		// clear the last selection
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected = null;
		
		}
		selectMarkerIfHover(airList);
	}
	
	// If there is a marker selected 
	private void selectMarkerIfHover(List<Marker> markers)
	{
		// Abort if there's already a marker selected
		if (lastSelected != null) {
			return;
		}
		
		for (Marker m : markers) 
		{
			CommonMarker marker = (CommonMarker)m;
			if (marker.isInside(map,  mouseX, mouseY)) {
				lastSelected = marker;
				marker.setSelected(true);
				return;
			}
		}
	}
	
	@Override
	public void mouseClicked()
	{
		if (lastClicked != null) {
			setInitialHiddenMarkers();
			lastClicked = null;
		}
		else if (lastClicked == null) 
		{
			checkMarkersForClick();
		}
	}
	
	private void setInitialHiddenMarkers() {
		for (Marker marker : airList) {
			marker.setHidden(false);
		}
		
		for (Marker marker : routeList) {
			marker.setHidden(true);
		}
	}
	
	private void checkMarkersForClick() {
		if (lastClicked != null) return;
		for (Marker marker : airList) {
			if (!marker.isHidden() && marker.isInside(map, mouseX, mouseY)) {
				lastClicked = (CommonMarker)marker;
				for (Marker sl : ((AirportMarker)lastClicked).routes) {
					sl.setHidden(false);
					
				}
				for (Marker mk : airList) {
					if (mk != lastClicked) {
						mk.setHidden(true);
					}
				}
				return;
			}
		}
	}
	
	private void sortAndPrint(int numToPrint){
		  List<AirportMarker> quakeMarkerList = new ArrayList<AirportMarker>();
		  AirportMarker eqm;
			for (Marker m : airList) {
				eqm = (AirportMarker)m;
				quakeMarkerList.add(eqm);
			}
			Collections.sort(quakeMarkerList);
			int actualNumToPrint = (numToPrint >= quakeMarkerList.size() ? quakeMarkerList.size() : numToPrint); 
			for (int index = 0; index < actualNumToPrint; index++) {
				System.out.println(quakeMarkerList.get(index));
			}
	  }
	
	private void addKey() {	
		// Remember you can use Processing's graphics methods here
		fill(200);
		
		int xb = 20;
		int yb = 20;
		
		rect(xb, yb, 170, 200);
		
		fill(0);
		textAlign(LEFT, CENTER);
		textSize(12);
		text("Airport Key", xb+13, yb+13);
		
		fill(250,250,210);
		stroke(250,250,210);
		ellipse(xb+8, 
				yb+52, 
				3, 
				3);
		
		fill(255,182,193);
		stroke(255,182,193);
		ellipse(xb+8, 
				yb+82, 
				3, 
				3);
		
		fill(135,206,250);
		stroke(135,206,250);
		ellipse(xb+8, 
				yb+112, 
				3, 
				3);

		fill(0);
		textAlign(LEFT, CENTER);
		textSize(12);
		text("Airport with less than", xb+20, yb+50);
		text("10 routes", xb+20, yb+65);
		text("Airport with more than", xb+20, yb+80);
		text("10, less than 30 routes", xb+20, yb+95);
		text("Airport with more than", xb+20, yb+110);
		text("30 routes", xb+20, yb+125);
		
	}

}