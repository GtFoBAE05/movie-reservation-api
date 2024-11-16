package org.imannuel.moviereservationapi.utils;

public class PaginationUtil {
    public static int calculateOffset(int page, int size) {
        if (page < 0) {
            page = 0;
        }
        return page * size;
    }

    public static int calculateTotalPages(long totalElements, int size) {
        return (int) Math.ceil((double) totalElements / size);
    }
}
