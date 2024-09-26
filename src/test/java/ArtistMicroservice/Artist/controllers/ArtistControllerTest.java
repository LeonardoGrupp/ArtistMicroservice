package ArtistMicroservice.Artist.controllers;

import ArtistMicroservice.Artist.entities.Artist;
import ArtistMicroservice.Artist.services.ArtistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ArtistController.class)
public class ArtistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArtistService artistService;

    private Artist artist;

    @BeforeEach
    public void setUp() {
        artist = new Artist();
        artist.setId(1L);
        artist.setName("ArtistName");
        artist.setAlbumIds(Arrays.asList(1L, 2L));
    }

    @Test
    public void testCreateArtist() throws Exception {
        when(artistService.createArtist(any(Artist.class))).thenReturn(artist);

        mockMvc.perform(post("/artist/createArtist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"ArtistName\", \"albumIds\": [1, 2]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ArtistName"))
                .andExpect(jsonPath("$.albumIds[0]").value(1));
    }

    @Test
    public void testGetArtistById() throws Exception {
        when(artistService.getArtistById(1L)).thenReturn(artist);

        mockMvc.perform(get("/artist/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ArtistName"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testGetAllArtists() throws Exception {
        List<Artist> artists = Arrays.asList(artist, new Artist());
        when(artistService.getAllArtists()).thenReturn(artists);

        mockMvc.perform(get("/artist/getAllArtists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("ArtistName"))
                .andExpect(jsonPath("$[0].albumIds[0]").value(1));
    }

    @Test
    public void testUpdateArtist() throws Exception {
        when(artistService.updateArtist(Mockito.eq(1L), any(Artist.class))).thenReturn(artist);

        mockMvc.perform(put("/artist/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"UpdatedName\", \"albumIds\": [1, 2]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ArtistName"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testDeleteArtist() throws Exception {
        Mockito.doNothing().when(artistService).deleteArtist(1L);

        mockMvc.perform(delete("/artist/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testArtistExist() throws Exception {
        when(artistService.checkIfArtistExistByName("ArtistName")).thenReturn(true);

        mockMvc.perform(get("/artist/exists/ArtistName"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}