package guavacache.cache.controller;

import guavacache.cache.model.Airport;
import guavacache.cache.service.AirportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/airports")
@RequiredArgsConstructor
public class AirportController {
    private final AirportService airportService;

    @GetMapping("/{code}")
    public Airport getAirport(@PathVariable String code) {
        return airportService.getAirportByCode(code);
    }

    @GetMapping
    public List<Airport> getAllAirports() {
        return airportService.getAllAirports();
    }

    @DeleteMapping("/cache")
    public String clearCache() {
        airportService.clearCache();
        return "Cache cleared!";
    }

    @GetMapping("/stats")
    public String getCacheStats() {
        return airportService.getCacheStats();
    }

}
