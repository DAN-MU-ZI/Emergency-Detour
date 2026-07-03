package com.example.demo.navigation;

public record RouteRequest(
        Origin origin,
        Destination destination
) {
    public record Origin(
            Double lat,
            Double lon,
            String name
    ) {
    }

    public record Destination(
        Double lat,
        Double lon,
        String name,
        String id,
        String address
    ) {

    }
}
