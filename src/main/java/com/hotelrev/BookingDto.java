package com.hotelrev;

public class BookingDto {

    public int numOfPax;
    public String specialRequest;
    public boolean complete = false;
    public String roomType;

    public BookingDto() {
    }

    public BookingDto(int numOfPax, String specialRequest, String roomType) {
        this.numOfPax = numOfPax;
        this.specialRequest = specialRequest;
        this.roomType = roomType;
    }
}
