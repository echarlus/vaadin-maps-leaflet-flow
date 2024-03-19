/*
 * Copyright © 2019 XDEV Software (https://xdev.software)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package software.xdev.vaadin.maps.leaflet.flow.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class LMarker implements LComponent
{

	private static final String MARKER_TYPE = "Point";
	private LMarkerGeometry geometry;
	private LMarkerOptions properties;
	/**
	 * Tag for custom meta-data
	 */
	private String tag;


	/**
	 * Creates a new Marker at the latitude, longitude
	 *
	 * @param lat
	 * @param lon
	 */
	public LMarker(final double lat, final double lon)
	{
		this(lat, lon, null);
	}

	public LMarker(final double lat, final double lon, final String tag)
	{
		this.geometry = new LMarkerGeometry(MARKER_TYPE, lat, lon);
		this.properties = new LMarkerOptions();
		this.tag = tag;
	}


	public LIcon getIcon()
	{
		return this.properties.getIcon();
	}

	public void setDivIcon(final LDivIcon icon)
	{
		this.properties.setIcon(icon);
	}

	public LIcon getDivIcon()
	{
		return this.properties.getIcon();
	}

	public void setIcon(final LIcon icon)
	{
		this.properties.setIcon(icon);
	}

	public LMarkerGeometry getGeometry()
	{
		return this.geometry;
	}

	public void setGeometry(final LMarkerGeometry geometry)
	{
		this.geometry = geometry;
	}

	public LMarkerOptions getProperties()
	{
		return this.properties;
	}

	public void setProperties(final LMarkerOptions properties)
	{
		this.properties = properties;
	}

	public double getLat()
	{
		return this.geometry.getCoordinates().get(0);
	}

	public void setLat(final double lat)
	{
		this.geometry.getCoordinates().set(0, lat);
	}
	
	public double getLon()
	{
		return this.geometry.getCoordinates().get(1);
	}
	
	public void setLon(final double lon)
	{
		this.geometry.getCoordinates().set(1, lon);
	}
	
	@Override
	public String getPopup()
	{
		return this.properties.getPopup();
	}
	
	/**
	 * Sets a Pop-up to the Marker
	 *
	 * @param popup
	 */
	public void setPopup(final String popup)
	{
		this.properties.setPopup(popup);
	}
	
	@Override
	public String getTooltip()
	{
		return this.properties.getTooltip();
	}
	
	public void setDraggable(boolean draggable) {
		this.properties.setDraggable(draggable);
	}
	
	public boolean getDraggable() {
		return this.properties.getDraggable();
	}
	
	/**
	 * Sets a Tooltip to the Marker
	 *
	 * @param toolip
	 */
	public void setToolip(final String toolip)
	{
		this.properties.setTooltip(toolip);
	}
	
	public String getTag()
	{
		return this.tag;
	}
	
	public void setTag(final String tag)
	{
		this.tag = tag;
	}
	
	/**
	 * Sets this LMarkers alert state to be used when alertMode has been enabled on MarkerCluster.
	 * This needs to be called prior to adding the component to the map
	 * @param state
	 */
	public void setAlertState(boolean state) {
		this.properties.setAlertState(state);
	}
	
	/**
	 * @return Marker's alert state
	 */
	public boolean getAlertState() {
		return this.properties.getAlertState();
	}
	
	@Override
	public String buildClientJSItems() throws JsonProcessingException
	{
		final ObjectMapper mapper = new ObjectMapper();
		return "let item = L.marker("
			+ mapper.writeValueAsString(this.geometry.getCoordinates()) + ","
			+ "{draggable:"+this.properties.getDraggable()+","
			+ "icon: "
			+ "new L."
			+ (this.properties.getIcon() instanceof LDivIcon ? "divIcon" : "Icon")
			+ "(" + mapper.writeValueAsString(this.properties.getIcon()) + ")"
			+ " }"
			+ ");"
			+ "item.options.alert_state="+this.properties.getAlertState()+";"
			+ (this.getTag() != null && !this.getTag().isBlank()
			? ("\nvar vaadinServer = this.$server;"
			+ "\nitem.on('click', function (e) { vaadinServer.onMarkerClick('" + this.tag + "')});"
			+ "\nitem.on('dragend', function (e) { var pos = e.target.getLatLng(); vaadinServer.onMarkerDragged('" + this.tag + "', pos.lat,pos.lng)});"
		)
			: "");
	}
}
