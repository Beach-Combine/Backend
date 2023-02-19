package beachcombine.backend.common.entity;


import beachcombine.backend.domain.Trashcan;

public class Coordinates {

    public final String lat;
    public final String lng;

    public Coordinates(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public Coordinates(Trashcan trashcan){
        this.lat = String.valueOf(trashcan.getLat());
        this.lng = String.valueOf(trashcan.getLng());
    }
}
