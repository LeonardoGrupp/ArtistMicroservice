package ArtistMicroservice.Artist.Service;

import ArtistMicroservice.Artist.Entity.Artist;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ArtistServiceInterface {
        Artist createArtist(Artist artist);
        Artist getArtistById(Long id);
        List<Artist> getAllArtists();
        Artist updateArtist(Long id, Artist artist);
        void deleteArtist(Long id);

}
