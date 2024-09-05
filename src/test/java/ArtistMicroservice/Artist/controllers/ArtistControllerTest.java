package ArtistMicroservice.Artist.controllers;

import ArtistMicroservice.Artist.entities.Artist;
import ArtistMicroservice.Artist.services.ArtistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ArtistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArtistService artistService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createArtistTest() throws Exception {
        List<Long> albumIds = Arrays.asList(1L, 2L, 3L);
        Artist artist = new Artist("Artist Name", albumIds);
        artist.setId(1L);

        Mockito.when(artistService.createArtist(any(Artist.class))).thenReturn(artist);

        mockMvc.perform(post("/artist/createArtist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(artist)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(artist.getId()))
                .andExpect(jsonPath("$.name").value(artist.getName()))
                .andExpect(jsonPath("$.albumIds").isArray())
                .andExpect(jsonPath("$.albumIds[0]").value(albumIds.get(0)))
                .andExpect(jsonPath("$.albumIds[1]").value(albumIds.get(1)))
                .andExpect(jsonPath("$.albumIds[2]").value(albumIds.get(2)));
    }

    @Test
    void getArtistByIdTest() throws Exception {
        List<Long> albumIds = Arrays.asList(4L, 5L);
        Artist artist = new Artist("Artist Name", albumIds);
        artist.setId(1L);

        Mockito.when(artistService.getArtistById(anyLong())).thenReturn(artist);

        mockMvc.perform(get("/artist/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(artist.getId()))
                .andExpect(jsonPath("$.name").value(artist.getName()))
                .andExpect(jsonPath("$.albumIds").isArray())
                .andExpect(jsonPath("$.albumIds[0]").value(albumIds.get(0)))
                .andExpect(jsonPath("$.albumIds[1]").value(albumIds.get(1)));
    }

    @Test
    void getAllArtistsTest() throws Exception {
        Artist artist1 = new Artist("Artist One", Arrays.asList(1L, 2L));
        artist1.setId(1L);

        Artist artist2 = new Artist("Artist Two", Arrays.asList(3L, 4L));
        artist2.setId(2L);

        List<Artist> artists = Arrays.asList(artist1, artist2);
        Mockito.when(artistService.getAllArtists()).thenReturn(artists);

        mockMvc.perform(get("/artist/getAllArtists")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(artist1.getId()))
                .andExpect(jsonPath("$[0].name").value(artist1.getName()))
                .andExpect(jsonPath("$[0].albumIds").isArray())
                .andExpect(jsonPath("$[0].albumIds[0]").value(1L))
                .andExpect(jsonPath("$[0].albumIds[1]").value(2L))
                .andExpect(jsonPath("$[1].id").value(artist2.getId()))
                .andExpect(jsonPath("$[1].name").value(artist2.getName()))
                .andExpect(jsonPath("$[1].albumIds").isArray())
                .andExpect(jsonPath("$[1].albumIds[0]").value(3L))
                .andExpect(jsonPath("$[1].albumIds[1]").value(4L));
    }

    @Test
    void updateArtistTest() throws Exception {
        List<Long> albumIds = Arrays.asList(1L, 2L);
        Artist updatedArtist = new Artist("Updated Artist Name", albumIds);
        updatedArtist.setId(1L);

        Mockito.when(artistService.updateArtist(anyLong(), any(Artist.class))).thenReturn(updatedArtist);

        mockMvc.perform(put("/artist/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedArtist)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedArtist.getId()))
                .andExpect(jsonPath("$.name").value(updatedArtist.getName()))
                .andExpect(jsonPath("$.albumIds").isArray())
                .andExpect(jsonPath("$.albumIds[0]").value(albumIds.get(0)))
                .andExpect(jsonPath("$.albumIds[1]").value(albumIds.get(1)));
    }

    @Test
    void deleteArtistTest() throws Exception {
        Mockito.doNothing().when(artistService).deleteArtist(anyLong());

        mockMvc.perform(delete("/artist/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}