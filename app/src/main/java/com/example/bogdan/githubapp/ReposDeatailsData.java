package com.example.bogdan.githubapp;

public class ReposDeatailsData {

    private String repoName, repoAuthor, repoImgLink, repoOutLink, repoDescription, repoCreatedDate, repoUpdatedDate, repoLanguage;
    private int repoWatches, repoForks, repoIssues;

    public ReposDeatailsData(String repoName, String repoAuthor, String repoImgLink, String repoOutLink, String repoDescription, String repoCreatedDate, String repoUpdatedDate, String repoLanguage, int repoWatches, int repoForks, int repoIssues) {
        this.repoName = repoName;
        this.repoAuthor = repoAuthor;
        this.repoImgLink = repoImgLink;
        this.repoOutLink = repoOutLink;
        this.repoDescription = repoDescription;
        this.repoCreatedDate = repoCreatedDate;
        this.repoUpdatedDate = repoUpdatedDate;
        this.repoLanguage = repoLanguage;
        this.repoWatches = repoWatches;
        this.repoForks = repoForks;
        this.repoIssues = repoIssues;
    }

    public String getRepoName() {
        return repoName;
    }

    public String getRepoAuthor() {
        return repoAuthor;
    }

    public String getRepoImgLink() {
        return repoImgLink;
    }

    public String getRepoOutLink() {
        return repoOutLink;
    }

    public String getRepoDescription() {
        return repoDescription;
    }

    public String getRepoCreatedDate() {
        return repoCreatedDate;
    }

    public String getRepoUpdatedDate() {
        return repoUpdatedDate;
    }

    public String getRepoLanguage() {
        return repoLanguage;
    }

    public int getRepoWatches() {
        return repoWatches;
    }

    public int getRepoForks() {
        return repoForks;
    }

    public int getRepoIssues() {
        return repoIssues;
    }
}
