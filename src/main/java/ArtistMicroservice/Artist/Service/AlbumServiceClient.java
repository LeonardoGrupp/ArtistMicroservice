package ArtistMicroservice.Artist.Service;

import ArtistMicroservice.Artist.Entity.Album;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
public class AlbumServiceClient {

    @Autowired
    private RestTemplate restTemplate;

    private static final String ALBUM_SERVICE_URL = "http://album-service/album/GetAlbum";


    public boolean validateAlbumIds(List<Long> albumIds) {
        for (Long albumId : albumIds) {
            String url = ALBUM_SERVICE_URL + "/" + albumId;
            try {
                Album album = restTemplate.getForObject(url, Album.class);

                if (album != null && album.getId() != null && album.getId().equals(albumId)) {
                    continue;
                } else {
                    return false;
                }
            } catch (RestClientException e) {
                System.err.println("Error validating album ID " + albumId + ": " + e.getMessage());
                return false;
            }
        }
        return true;
    }
}