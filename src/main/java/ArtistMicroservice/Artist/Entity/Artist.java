package ArtistMicroservice.Artist.Entity;

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
    private String albumIdsString; // Storing as a comma-separated string

    public Artist() {}

    public Artist(String name, List<Long> albumIdsString) {
        this.name = name;
        this.albumIdsString = albumIdsString.toString();
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
