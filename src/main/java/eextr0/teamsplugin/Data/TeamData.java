package eextr0.teamsplugin.Data;

import org.bukkit.ChatColor;

public class TeamData {

    private String displayName;
    private ChatColor color;
    private int points;

    public TeamData(String displayName, int points, ChatColor color) {
        this.displayName = displayName;
        this.color = color;
        this.points = points;
    }
    public void setName(String displayName) {
        this.displayName = displayName;
    }

    public ChatColor getColor() {
        return color;
    }

    public int getPoints() {return points;}

    public void setColor(ChatColor color) {
        this.color = color;
    }
    public void setPoints (int points) {this.points = points;}
    public void updatePoints (int pointsToUpdate) {this.points += pointsToUpdate;}
    public String getDisplayName () {return displayName;}
}
