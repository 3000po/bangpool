package kr.popcorn.sharoom.helper;


import android.widget.ImageView;

import java.util.ArrayList;

import kr.popcorn.sharoom.R;

/**
 * Created by user on 16. 3. 2.
 */

//TODO 방정보를 저장할 자료형 클래스
public class Helper_roomData {

    public int roomNumber;
    public int userID;
    public String title;
    public String address;
    public String price;
    public String roomKind;
    public String roomInfo;
    public String fac;
    public double lat;
    public double lng;
    public String sDate;
    public String eDate;
    public int isClosed;
    public int rUserID;


    public  ArrayList<String> image;

    public Helper_roomData(int roomNumber, int userID, String title, String address, String price, String roomKind, String roomInfo,
                           String fac, double lat, double lng, String sDate, String eDate, ArrayList<String> image, int isClosed, int rUserID) {
        this.roomNumber = roomNumber;
        this.userID = userID;
        this.title = title;
        this.address = address;
        this.price = price;
        this.roomKind = roomKind;
        this.roomInfo = roomInfo;
        this.fac = fac;
        this.lat = lat;
        this.lng = lng;
        this.sDate = sDate;
        this.eDate = eDate;
        this.image = image;
        this.isClosed = isClosed;
        this.rUserID = rUserID;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRoomKind() {
        return roomKind;
    }

    public void setRoomKind(String roomKind) {
        this.roomKind = roomKind;
    }

    public String getRoomInfo() {
        return roomInfo;
    }

    public void setRoomInfo(String roomInfo) {
        this.roomInfo = roomInfo;
    }

    public String getsDate() {
        return sDate;
    }

    public void setsDate(String sDate) {
        this.sDate = sDate;
    }

    public String geteDate() {
        return eDate;
    }

    public void seteDate(String eDate) {
        this.eDate = eDate;
    }

    public ArrayList<String> getImage() {
        return image;
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }
}
