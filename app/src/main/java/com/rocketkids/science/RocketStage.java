package com.rocketkids.science;

public class RocketStage {
    private final String id;
    private final String title;
    private final String description;
    private final int dotColor;
    private final String correctIdPosition;

    public RocketStage(String id, String title, String description, int dotColor, String correctIdPosition) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dotColor = dotColor;
        this.correctIdPosition = correctIdPosition;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getDotColor() { return dotColor; }
    public String getCorrectIdPosition() { return correctIdPosition; }
}