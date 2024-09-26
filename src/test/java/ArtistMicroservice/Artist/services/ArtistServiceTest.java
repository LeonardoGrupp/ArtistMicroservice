package ArtistMicroservice.Artist.services;

import ArtistMicroservice.Artist.entities.Artist;
import ArtistMicroservice.Artist.repositories.ArtistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ArtistServiceTest {

    @Mock
    private ArtistRepository artistRepository;

    @InjectMocks
    private ArtistService artistService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testCreateArtist_Success() {

        Artist artist = new Artist();
        artist.setName("New Artist");
        artist.setAlbumIds(Arrays.asList(1L, 2L));

        when(artistRepository.save(any(Artist.class))).thenReturn(artist);

        Artist createdArtist = artistService.createArtist(artist);

        assertNotNull(createdArtist);
        assertEquals("New Artist", createdArtist.getName());
        verify(artistRepository, times(1)).save(artist);
    }

    @Test
    public void testGetArtistById_Found() {
        Artist artist = new Artist();
        artist.setId(1L);

        when(artistRepository.findById(1L)).thenReturn(Optional.of(artist));

        Artist foundArtist = artistService.getArtistById(1L);

        assertNotNull(foundArtist);
        assertEquals(1L, foundArtist.getId());
    }

    @Test
    public void testGetArtistById_NotFound() {
        when(artistRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            artistService.getArtistById(1L);
        });

        assertEquals("Artist not found", exception.getMessage());
    }

    @Test
    public void testGetAllArtists() {
        List<Artist> artists = Arrays.asList(new Artist(), new Artist());

        when(artistRepository.findAll()).thenReturn(artists);

        List<Artist> allArtists = artistService.getAllArtists();

        assertEquals(2, allArtists.size());
        verify(artistRepository, times(1)).findAll();
    }

    @Test
    public void testUpdateArtist() {
        Artist existingArtist = new Artist();
        existingArtist.setId(1L);
        existingArtist.setName("Old Name");

        Artist updatedArtist = new Artist();
        updatedArtist.setName("New Name");

        when(artistRepository.findById(1L)).thenReturn(Optional.of(existingArtist));
        when(artistRepository.save(any(Artist.class))).thenReturn(existingArtist);

        Artist result = artistService.updateArtist(1L, updatedArtist);

        assertEquals("New Name", result.getName());
        verify(artistRepository, times(1)).save(existingArtist);
    }

    @Test
    public void testDeleteArtist() {
        artistService.deleteArtist(1L);

        verify(artistRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testCheckIfArtistExistByName_Exists() {
        Artist artist = new Artist();
        artist.setName("ArtistName");

        when(artistRepository.findArtistByName("ArtistName")).thenReturn(artist);

        Boolean exists = artistService.checkIfArtistExistByName("ArtistName");

        assertTrue(exists);
    }

    @Test
    public void testCheckIfArtistExistByName_NotExists() {
        when(artistRepository.findArtistByName("ArtistName")).thenReturn(null);

        Boolean exists = artistService.checkIfArtistExistByName("ArtistName");

        assertFalse(exists);
    }
}
