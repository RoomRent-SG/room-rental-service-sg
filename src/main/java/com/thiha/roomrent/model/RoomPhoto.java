package com.thiha.roomrent.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "room_photos")
@NoArgsConstructor
@AllArgsConstructor
public class RoomPhoto {
    @Id
    @Column(name = "photo_id")
    private long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "room_post_id")
    private RoomPost roomPost;
}
