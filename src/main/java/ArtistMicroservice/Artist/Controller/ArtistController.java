package ArtistMicroservice.Artist.Controller;

import ArtistMicroservice.Artist.Entity.Artist;
import ArtistMicroservice.Artist.Service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    @PostMapping("/createArtist")
    public ResponseEntity<Artist> createArtist(@RequestBody Artist artist) {
        return ResponseEntity.ok(artistService.createArtist(artist));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Artist> getArtistById(@PathVariable Long id) {
        return ResponseEntity.ok(artistService.getArtistById(id));
    }

    @GetMapping("/getAllArtists")
    public ResponseEntity<List<Artist>> getAllArtists() {
        return ResponseEntity.ok(artistService.getAllArtists());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Artist> updateArtist(@PathVariable Long id, @RequestBody Artist artist) {
        return ResponseEntity.ok(artistService.updateArtist(id, artist));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtist(@PathVariable Long id) {
        artistService.deleteArtist(id);
        return ResponseEntity.noContent().build();
    }
}
