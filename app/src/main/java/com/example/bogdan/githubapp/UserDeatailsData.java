package com.example.bogdan.githubapp;

public class UserDeatailsData {

    private String userOutURL, userName, userCompany, userLocation, userBio;
    private int userPublicReposNumber, userFollowersNUmber;

    public UserDeatailsData(String userOutURL, String userName, String userCompany, String userLocation, String userBio, int userPublicReposNumber, int userFollowersNUmber) {
        this.userOutURL = userOutURL;
        this.userName = userName;
        this.userCompany = userCompany;
        this.userLocation = userLocation;
        this.userBio = userBio;
        this.userPublicReposNumber = userPublicReposNumber;
        this.userFollowersNUmber = userFollowersNUmber;
    }

    public String getUserOutURL() {
        return userOutURL;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserCompany() {

        if(userCompany.equals("null")){
            return "Company not specified";
        }else {
            return userCompany;
        }
    }

    public String getUserLocation() {

        if(userLocation.equals("null")){
            return "Location is not specified";
        }else{
            return userLocation;
        }
    }

    public String getUserBio() {

        if(userBio.equals("null")){
            return "Bio is not specified";
        }else{
            return userBio;
        }
    }

    public int getUserPublicReposNumber() {
        return userPublicReposNumber;
    }

    public int getUserFollowersNUmber() {
        return userFollowersNUmber;
    }
}
