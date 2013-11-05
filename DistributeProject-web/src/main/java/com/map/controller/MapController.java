/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.map.controller;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import java.io.InputStream;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.LatLngBounds;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

/**
 *
 * @author citlalig
 */
@ManagedBean
public class MapController implements Serializable  {
    public static final String CERTIFICATE_PASSWORD = "dani3l";
    private MapModel markerModel; 
    private double lat;  
    private double lng;  
    private String title;
    private ApnsService service;
    
    public MapController() {  
        markerModel = new DefaultMapModel();  
        
        // TODO Remove when the call to the APNS is ready.
        String url = getClass().getResource("/certificates/distribuyeme.p12").getPath();
        System.out.println(url);
        
        // Build the APNS Service.
        InputStream certificateStream = getClass().getResourceAsStream("/certificates/distribuyeme.p12");
        service = APNS.newService().withCert(certificateStream, CERTIFICATE_PASSWORD).withSandboxDestination().build();
    }  
      
    public void pushMSG(String msg) {
        String payload = APNS.newPayload().alertBody(msg).build();
        String token = "distribuyeme";
        service.push(token, payload);
    }
    
    public void onStateChange(StateChangeEvent event) {  
        LatLngBounds bounds = event.getBounds();  
        int zoomLevel = event.getZoomLevel();  
          
        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Zoom Level", String.valueOf(zoomLevel)));  
        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Center", event.getCenter().toString()));  
        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "NorthEast", bounds.getNorthEast().toString()));  
        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "SouthWest", bounds.getSouthWest().toString()));  
    }  
      
    public void onPointSelect(PointSelectEvent event) {  
        LatLng latlng = event.getLatLng();  
          
        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Point Selected", "Lat:" + latlng.getLat() + ", Lng:" + latlng.getLng()));  
    }  
    
    public void addMarker(ActionEvent actionEvent) {  
        Marker marker = new Marker(new LatLng(lat, lng), title);  
        markerModel.addOverlay(marker);
        marker.setDraggable(true);
        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Marker Added", "Lat:" + lat + ", Lng:" + lng));  
    }  
    
    public void onMarkerDrag(MarkerDragEvent event) {  
        Marker marker = event.getMarker();  
        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Marker Dragged", "Lat:" + marker.getLatlng().getLat() + ", Lng:" + marker.getLatlng().getLng()));  
    }  
      
    public void addMessage(FacesMessage message) {  
        FacesContext.getCurrentInstance().addMessage(null, message);  
    }  

    /**
     * @return the emptyModel
     */
    public MapModel getMarkerModel() {
        return markerModel;
    }

    /**
     * @param emptyModel the emptyModel to set
     */
    public void setMarkerModel(MapModel markerModel) {
        this.markerModel = markerModel;
    }

    /**
     * @return the lat
     */
    public double getLat() {
        return lat;
    }

    /**
     * @param lat the lat to set
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

    /**
     * @return the lng
     */
    public double getLng() {
        return lng;
    }

    /**
     * @param lng the lng to set
     */
    public void setLng(double lng) {
        this.lng = lng;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
}
