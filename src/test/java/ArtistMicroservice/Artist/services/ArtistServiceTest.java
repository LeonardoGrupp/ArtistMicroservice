package ArtistMicroservice.Artist.services;

import ArtistMicroservice.Artist.entities.Artist;
import ArtistMicroservice.Artist.repositories.ArtistRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ArtistServiceTest {

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private AlbumServiceClient albumServiceClient;

    @InjectMocks
    private ArtistService artistService;

    @Test
    void testCreateArtistWithValidAlbumIds() {
        List<Long> albumIds = Arrays.asList(1L, 2L, 3L);
        Artist artist = new Artist("Artist Name", albumIds);

        when(albumServiceClient.validateAlbumIds(albumIds)).thenReturn(true);
        when(artistRepository.save(any(Artist.class))).thenReturn(artist);

        Artist createdArtist = artistService.createArtist(artist);

        assertEquals(artist.getName(), createdArtist.getName());
        assertEquals(artist.getAlbumIds(), createdArtist.getAlbumIds());
        verify(artistRepository, times(1)).save(artist);
    }

    @Test
    void testCreateArtistWithInvalidAlbumIds() {
        List<Long> albumIds = Arrays.asList(1L, 2L, 3L);
        Artist artist = new Artist("Artist Name", albumIds);

        when(albumServiceClient.validateAlbumIds(albumIds)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> artistService.createArtist(artist));

        assertEquals("Invalid album IDs", exception.getMessage());
        verify(artistRepository, never()).save(any(Artist.class));
    }

    @Test
    void testGetArtistByIdSuccess() {
        Artist artist = new Artist("Artist Name", Arrays.asList(1L, 2L, 3L));
        artist.setId(1L);

        when(artistRepository.findById(artist.getId())).thenReturn(Optional.of(artist));

        Artist foundArtist = artistService.getArtistById(artist.getId());

        assertEquals(artist.getId(), foundArtist.getId());
        assertEquals(artist.getName(), foundArtist.getName());
        verify(artistRepository, times(1)).findById(artist.getId());
    }

    @Test
    void testGetArtistByIdNotFound() {
        when(artistRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> artistService.getArtistById(1L));

        assertEquals("Artist not found", exception.getMessage());
        verify(artistRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetAllArtistsWithResults() {
        Artist artist1 = new Artist("Artist One", Arrays.asList(1L, 2L));
        artist1.setId(1L);

        Artist artist2 = new Artist("Artist Two", Arrays.asList(3L, 4L));
        artist2.setId(2L);

        when(artistRepository.findAll()).thenReturn(Arrays.asList(artist1, artist2));

        List<Artist> artists = artistService.getAllArtists();

        assertEquals(2, artists.size());
        assertEquals("Artist One", artists.get(0).getName());
        assertEquals("Artist Two", artists.get(1).getName());
        verify(artistRepository, times(1)).findAll();
    }

    @Test
    void testGetAllArtistsWithNoResults() {
        when(artistRepository.findAll()).thenReturn(List.of());

        List<Artist> artists = artistService.getAllArtists();

        assertTrue(artists.isEmpty());
        verify(artistRepository, times(1)).findAll();
    }

    @Test
    void testUpdateArtistSuccess() {
        List<Long> newAlbumIds = Arrays.asList(1L, 2L, 3L);
        Artist existingArtist = new Artist("Existing Artist", Arrays.asList(4L, 5L));
        existingArtist.setId(1L);

        Artist updatedArtist = new Artist("Updated Artist", newAlbumIds);
        updatedArtist.setId(1L);

        when(artistRepository.findById(existingArtist.getId())).thenReturn(Optional.of(existingArtist));
        when(artistRepository.save(any(Artist.class))).thenReturn(updatedArtist);

        Artist result = artistService.updateArtist(existingArtist.getId(), updatedArtist);

        assertEquals("Updated Artist", result.getName());
        assertEquals(newAlbumIds, result.getAlbumIds());
        verify(artistRepository, times(1)).findById(existingArtist.getId());
        verify(artistRepository, times(1)).save(existingArtist);
    }

    @Test
    void testUpdateArtistNotFound() {
        List<Long> newAlbumIds = Arrays.asList(1L, 2L, 3L);
        Artist updatedArtist = new Artist("Updated Artist", newAlbumIds);
        updatedArtist.setId(1L);

        when(artistRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> artistService.updateArtist(1L, updatedArtist));

        assertEquals("Artist not found", exception.getMessage());
        verify(artistRepository, never()).save(any(Artist.class));
    }

    @Test
    void testDeleteArtistSuccess() {
        Long artistId = 1L;

        doNothing().when(artistRepository).deleteById(artistId);

        artistService.deleteArtist(artistId);

        verify(artistRepository, times(1)).deleteById(artistId);
    }

    @Test
    void testDeleteArtistNotFound() {
        Long artistId = 1L;

        doNothing().when(artistRepository).deleteById(artistId);

        artistService.deleteArtist(artistId);

        verify(artistRepository, times(1)).deleteById(artistId);
    }
}