package guavacache.cache.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import guavacache.cache.model.Airport;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AirportService {
    //kho tao cache Guava
    private Cache<String, Airport> airportCache;


    @PostConstruct
    public void init() {
        //config cache
        airportCache = CacheBuilder.newBuilder()
                .expireAfterWrite(2, TimeUnit.MINUTES)//
                .maximumSize(1000) //max size
                .recordStats()//record cache
                .build();
    }

    // set database
    private List<Airport> mockDatabase() {
        List<Airport> airports = new ArrayList<>();
        airports.add(new Airport("HAN", "Noi Bai International Airport"));
        airports.add(new Airport("SGN", "Tan Son Nhat International Airport"));
        airports.add(new Airport("DAD", "Da Nang International Airport"));
        return airports;
    }

    //read-aside cache pattern
    public Airport getAirportByCode(String code) {
        try {
            //check cache first
            Airport cachedAirport = airportCache.getIfPresent(code);
            if(cachedAirport != null) {
                log.info("getAirportByCache: code={} airport={}", code, cachedAirport);
                return cachedAirport;
            }

            log.info("Cache miss for : {}", code);
            //if not have cache, call from database
            Airport airport = mockDatabase().stream()
                    .filter(a -> a.getCode().equals(code))
                    .findFirst()
                    .orElse(null);
            //save into cache if have cache
            if(airport != null) {
                airportCache.put(code, airport);
                return airport;
            }

            log.warn("Airport not found for key: {}", code);
            return  null;
        } catch (Exception e) {
            log.error("Error retrieving airport for key: {}, error: {}", code, e.getMessage());
            return  null;
        }

    }

    //get all airports
    public  List<Airport> getAllAirports() {
        List<Airport> airports = mockDatabase();
        //update for all airports
        airports.forEach(airport -> airportCache.put(airport.getCode(), airport));
        return airports;
    }

    //cache delete
    public void clearCache() {
        airportCache.invalidateAll();
        log.info("Cache cleared");
    }

    //report cache
    public String getCacheStats() {
        return airportCache.stats().toString();
    }
}


