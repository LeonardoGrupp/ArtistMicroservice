package ArtistMicroservice.Artist.repositories;

import ArtistMicroservice.Artist.entities.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
Artist findArtistByName(String name);
}