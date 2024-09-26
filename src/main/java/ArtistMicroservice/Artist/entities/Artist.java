package ArtistMicroservice.Artist.entities;

import jakarta.persistence.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "album_ids")
    private String albumIdsString;

    public Artist() {}

    public Artist(String name) {
        this.name = name;
    }

    public Artist(String name, List<Long> albumIds) {
        this.name = name;
        setAlbumIds(albumIds);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getAlbumIds() {
        if (albumIdsString == null || albumIdsString.isEmpty()) {
            return List.of();
        }
        return Arrays.stream(albumIdsString.split(","))
                .map(String::trim)
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    public void setAlbumIds(List<Long> albumIds) {
        if (albumIds == null || albumIds.isEmpty()) {
            this.albumIdsString = "";
        } else {
            this.albumIdsString = albumIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
        }
    }
}