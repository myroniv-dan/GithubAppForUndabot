package com.example.bogdan.githubapp;

public class ReposData {

    private String repoName, authorName, imgLink;
    private int numOfWatches, numOfForks, numOfIssues, id;

    public ReposData(String repoName, String authorName, String imgLink, int numOfWatches, int numOfForks, int numOfIssues, int id) {
        this.repoName = repoName;
        this.authorName = authorName;
        this.imgLink = imgLink;
        this.numOfWatches = numOfWatches;
        this.numOfForks = numOfForks;
        this.numOfIssues = numOfIssues;
        this.id = id;
    }

    public String getRepoName() {
        return repoName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getImgLink() {
        return imgLink;
    }

    public int getNumOfWatches() {
        return numOfWatches;
    }

    public int getNumOfForks() {
        return numOfForks;
    }

    public int getNumOfIssues() {
        return numOfIssues;
    }

    public int getId() {
        return id;
    }
}
