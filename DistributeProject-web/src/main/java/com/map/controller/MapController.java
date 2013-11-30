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
import javax.faces.bean.ViewScoped;
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
@ViewScoped
public class MapController implements Serializable  {
    public static final String CERTIFICATE_PASSWORD = "dani3l";
    private MapModel markerModel; 
    private double latA = 20.69484572250378;  
    private double lngA = -103.45468607149087;
    private double latB = 20.62256524328231;  
    private double lngB = -103.32113352022134; 
    private String title;
    private ApnsService service;
    
    //Lat:20.69484572250378, Lng:-103.45468607149087
    //Lat:20.62256524328231, Lng:-103.32113352022134
    
    public MapController() {  
        markerModel = new DefaultMapModel();  
        
        //Shared coordinates  
        LatLng coord1 = new LatLng(latA,lngA );  
        LatLng coord2 = new LatLng(latB, lngB );  
          
        //Draggable  
        markerModel.addOverlay(new Marker(coord1, "A"));  
        markerModel.addOverlay(new Marker(coord2, "B"));  
          
        for(Marker marker : markerModel.getMarkers()) {  
            marker.setDraggable(true);  
        }  
        
        // TODO Remove when the call to the APNS is ready.
        String url = getClass().getResource("/certificates/iphone_dev.p12").getPath();
        System.out.println(url);
        
        // Build the APNS Service.
        InputStream certificateStream = getClass().getResourceAsStream("/certificates/iphone_dev.p12");
        service = APNS.newService().withCert(certificateStream, CERTIFICATE_PASSWORD).withSandboxDestination().build();
        
        
        pushMSG("La app esta empezando");
    }  
      
    public void pushMSG(String msg) {
        System.out.println("msg: " + msg);
        String payload = APNS.newPayload().alertBody(msg).build();
        String token = "43FA3C3684D52B187F08122C4FA204F34A4DDA48DA72CF41353C9D4E0C568ACC";
        service.push(token, payload);
    }
    
   /* public void onStateChange(StateChangeEvent event) {  
        LatLngBounds bounds = event.getBounds();  
        int zoomLevel = event.getZoomLevel();  
          
        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Zoom Level", String.valueOf(zoomLevel)));  
        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Center", event.getCenter().toString()));  
        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "NorthEast", bounds.getNorthEast().toString()));  
        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "SouthWest", bounds.getSouthWest().toString()));  
    }*/  
      
    public void onPointSelect(PointSelectEvent event) {  
        LatLng latlng = event.getLatLng();  
          
        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Point Selected", "Lat:" + latlng.getLat() + ", Lng:" + latlng.getLng()));  
    }  
    
   public void addMarker(ActionEvent actionEvent) {  
   /*     Marker marker = new Marker(new LatLng(lat, lng), title);  
        markerModel.addOverlay(marker);
        marker.setDraggable(true);
        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Marker Added", "Lat:" + lat + ", Lng:" + lng));
     */
    }  
    
    public void onMarkerDrag(MarkerDragEvent event) {  
        Marker marker = event.getMarker(); 
        if(marker.getTitle().equals("A")){
            this.setLatA(marker.getLatlng().getLat());
            this.setLngA(marker.getLatlng().getLng());
        }
        else if (marker.getTitle().equals("B")){
            this.setLatB(marker.getLatlng().getLat());
            this.setLngB(marker.getLatlng().getLng());
        }
        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Marker Dragged", "Lat:" + marker.getLatlng().getLat() + ", Lng:" + marker.getLatlng().getLng()));  
        
        pushMSG(marker.getLatlng().getLat() + "/" + marker.getLatlng().getLng());
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

    /**
     * @return the latA
     */
    public double getLatA() {
        return latA;
    }

    /**
     * @param latA the latA to set
     */
    public void setLatA(double latA) {
        this.latA = latA;
    }

    /**
     * @return the lngA
     */
    public double getLngA() {
        return lngA;
    }

    /**
     * @param lngA the lngA to set
     */
    public void setLngA(double lngA) {
        this.lngA = lngA;
    }

    /**
     * @return the latB
     */
    public double getLatB() {
        return latB;
    }

    /**
     * @param latB the latB to set
     */
    public void setLatB(double latB) {
        this.latB = latB;
    }

    /**
     * @return the lngB
     */
    public double getLngB() {
        return lngB;
    }

    /**
     * @param lngB the lngB to set
     */
    public void setLngB(double lngB) {
        this.lngB = lngB;
    }
}
