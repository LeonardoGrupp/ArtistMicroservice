package ArtistMicroservice.Artist.Repository;

import ArtistMicroservice.Artist.Entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
Artist findArtistByName(String name);
}