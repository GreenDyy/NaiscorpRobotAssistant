package com.naiscorp.robotapp.model;

public class HomeCard {
    private String title;
    private String description;
    private int iconResId;
    private String badge;
    private boolean hasBadge;

    public HomeCard(String title, String description, int iconResId) {
        this.title = title;
        this.description = description;
        this.iconResId = iconResId;
        this.hasBadge = false;
    }

    public HomeCard(String title, String description, int iconResId, String badge) {
        this.title = title;
        this.description = description;
        this.iconResId = iconResId;
        this.badge = badge;
        this.hasBadge = true;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getIconResId() {
        return iconResId;
    }

    public String getBadge() {
        return badge;
    }

    public boolean hasBadge() {
        return hasBadge;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public void setBadge(String badge) {
        this.badge = badge;
        this.hasBadge = badge != null && !badge.isEmpty();
    }
}
