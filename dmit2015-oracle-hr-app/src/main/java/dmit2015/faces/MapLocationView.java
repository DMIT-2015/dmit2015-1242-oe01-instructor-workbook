package dmit2015.faces;

import dmit2015.model.MapLocation;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.omnifaces.util.Messages;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.annotation.PostConstruct;

import java.io.Serializable;
import java.util.List;

@Named("currentMapLocationView")
@ViewScoped // create this object for one HTTP request and keep in memory if the next is for the same page
// class must implement Serializable
public class MapLocationView implements Serializable {

    @Getter
    private List<MapLocation> locations = List.of(
            new MapLocation("Home Depot Edmonton Skyview",53.5997,-113.552_13360),
            new MapLocation("Home Depot Edmonton Westmount",53.560425,-113.573074),
            new MapLocation("Home Depot Edmonton Westend",53.5361,-113.621)
    );

}