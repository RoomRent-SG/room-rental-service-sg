package com.thiha.roomrent.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import com.thiha.roomrent.enums.AirConTime;
import com.thiha.roomrent.enums.CookingAllowance;
import com.thiha.roomrent.enums.Location;
import com.thiha.roomrent.enums.PropertyType;
import com.thiha.roomrent.enums.RoomType;
import com.thiha.roomrent.enums.SharePub;
import com.thiha.roomrent.enums.StationName;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "room_posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomPost implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "station_name")
    private StationName stationName;

    @Column(name = "price")
    private double price;

    @Column(name = "posted_at")
    private Date postedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "room_type")
    private RoomType roomType;

    @Column(name = "total_person")
    @Min(1)
    @Max(4)
    private int totalPax;

    @Column(name = "cooking_allowance")
    @Enumerated(EnumType.STRING)
    private CookingAllowance cookingAllowance;
   
    @Column(name = "share_PUB")
    @Enumerated(EnumType.STRING)
    private SharePub sharePub;

    @Column(name = "aircon_time")
    @Enumerated(EnumType.STRING)
    private AirConTime airConTime;

    @Column(name = "allow_visitor", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean allowVisitor;


    @Column(name = "location")
    @Enumerated(EnumType.STRING)
    private Location location;

    @Column(name = "property_type")
    @Enumerated(EnumType.STRING)
    private PropertyType propertyType;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "agent_id", referencedColumnName = "id")
    private Agent agent;

    /*
     * If roomPhotos are loaded lazily, the data will only be loaded if the object is called or used.So, if the code
     * doesn't use the roomPhoto objects, they will not loaded. owever, if roomPhotos are not accessed within an active Hibernate session,
     * As a result, trying to access them later (e.g., during serialization for caching) will cause a LazyInitializationExceptionit 
     * will become serialization error when the roompost data is cached in the redis. 
     * However, changing FetchType to EAGER will effect on performance of the app in other operations if the data is large.
     */
    @OneToMany(mappedBy = "roomPost", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<RoomPhoto> roomPhotos;

}
