package guavacache.cache;

import guavacache.cache.model.Airport;
import guavacache.cache.service.AirportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import java.util.List;


@SpringBootTest
class CacheApplicationTests {
    @Autowired
    private AirportService airportService;

    @BeforeEach
    void setUp() {
        airportService.clearCache();
    }

    @Test
    void testGetAirportByCode_CacheMissThenHit() {
        Airport airport1 = airportService.getAirportByCode("HAN");
        assertNotNull(airport1, "Airport should be found");
        assertEquals("HAN", airport1.getCode());
        assertEquals("Noi Bai International Airport", airport1.getName());

        Airport airport2 = airportService.getAirportByCode("HAN");
        assertNotNull(airport2, "Airport should be found from cache");
        assertEquals(airport1, airport2, "Should return same object from cache");
    }

    @Test
    void testGetAirportByCode_NotFound() {
        Airport airport = airportService.getAirportByCode("XYZ");
        assertNull(airport, "Should return null for non-existent airport code");
    }

    @Test
    void testGetAllAirports() {
        List<Airport> airports = airportService.getAllAirports();
        assertNotNull(airports, "Airport list should not be null");
        assertFalse(airports.isEmpty(), "Airport list should not be empty");
        assertEquals(3, airports.size(), "Should return 3 airports from mock database");

        // Kiểm tra cache gián tiếp qua getAirportByCode
        Airport cachedAirport = airportService.getAirportByCode("HAN");
        assertNotNull(cachedAirport, "Should hit cache after getAllAirports");
    }

    @Test
    void testClearCache() {
        airportService.getAllAirports();
        Airport cachedAirport = airportService.getAirportByCode("HAN");
        assertNotNull(cachedAirport, "Cache should contain HAN");

        airportService.clearCache();
        // Không thể kiểm tra trực tiếp cache, nhưng có thể kiểm tra miss
        Airport airportAfterClear = airportService.getAirportByCode("HAN");
        assertNotNull(airportAfterClear, "Should still get airport, but from DB");
    }

    @Test
    void testCacheStats() {
        airportService.getAirportByCode("HAN");
        airportService.getAirportByCode("HAN"); // Cache hit
        airportService.getAirportByCode("SGN");

        String stats = airportService.getCacheStats();
        assertNotNull(stats, "Cache stats should not be null");
        assertTrue(stats.contains("hitCount"), "Stats should contain hitCount");
        assertTrue(stats.contains("missCount"), "Stats should contain missCount");
    }
}
