package com.thiha.roomrent.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "mrt_lines")
public class MrtLine implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "mrt_line_name", unique = true)
    private String name;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.DETACH})
    @JoinTable(
        name = "line_stations",
        joinColumns = @JoinColumn(name = "mrt_line_id"),
        inverseJoinColumns = @JoinColumn(name = "station_id")
    )
    private Set<MrtStation> stations;

    @Override
    public int hashCode() {
    return Objects.hash(id, name);
    }
}
