package com.finan.DispatchLoadBalancer.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DispatcherUtilTest {
    @Test
    void testZeroDistance() {
        // identical points → zero distance
        double d = DispatcherUtil.haversine(0.0, 0.0, 0.0, 0.0);
        assertEquals(0.0, d, 1e-6, "Distance between identical coords must be 0");
    }

    @Test
    void testOneDegreeLongitudeAtEquator() {
        // from (0°,0°) to (0°,1°) ≈ 111.195 km on a great circle
        double d = DispatcherUtil.haversine(0.0, 0.0, 0.0, 1.0);
        assertEquals(111.195, d, 0.01, "1° of longitude at equator ≈111.195 km");
    }

    @Test
    void testNorthPoleToEquator() {
        // from (90°,0°) to (0°,0°) is a quarter meridian: π·R/2 ≈ 10007.543 km
        double d = DispatcherUtil.haversine(90.0, 0.0, 0.0, 0.0);
        assertEquals(10007.543, d, 0.1, "North pole to equator should be ≈10007.543 km");
    }

    @Test
    void testLondonToParis() {
        // London (51.5074N,0.1278W) → Paris (48.8566N,2.3522E)
        // expected ≈343.8 km (using R=6371km)
        double londonLat  = 51.5074;
        double londonLon  = -0.1278;
        double parisLat   = 48.8566;
        double parisLon   =  2.3522;
        double d = DispatcherUtil.haversine(
                londonLat, londonLon, parisLat, parisLon);
        assertEquals(343.8, d, 1.0, "London→Paris ≈343.8 km");
    }

    @Test
    void testAntipodalPoints() {
        // antipodes: (0°,0°) ↔ (0°,180°) → π·R ≈ 20015.087 km
        double d = DispatcherUtil.haversine(0.0, 0.0, 0.0, 180.0);
        assertEquals(Math.PI * 6371, d, 1e-3, "Antipodal points must be half Earth's circumference");
    }

}
