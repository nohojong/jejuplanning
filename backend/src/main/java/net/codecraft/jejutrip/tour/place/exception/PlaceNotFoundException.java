package net.codecraft.jejutrip.tour.place.exception;

public class PlaceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public PlaceNotFoundException(String message) {
        super(message);
    }
}

