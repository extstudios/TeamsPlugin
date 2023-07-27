package eextr0.teamsplugin.Tasks;

import eextr0.teamsplugin.Data.TeamData;
import eextr0.teamsplugin.Data.TeamManager;
import eextr0.teamsplugin.TeamsPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class CreateScoreboardDisplayTask {

    private final TeamsPlugin plugin;
    private final List<TextDisplay> textDisplays;

    private TextDisplay textDisplay;

    public CreateScoreboardDisplayTask(TeamsPlugin plugin) {
        this.plugin = plugin;
        this.textDisplays = new ArrayList<>();
    }

    public void createDisplay(Location location) {
        removeTextDisplay();
        TeamManager teamManager = plugin.getTeamManager();
        StringBuilder sb = new StringBuilder();
        ChatColor titleColor = plugin.getScoreboardTitleColor();
        sb.append(titleColor).append("Team Points Leaderboard").append("\n");
        List<TeamData> teams = new ArrayList<>(teamManager.getTeams().values());
        teams.sort(Comparator.comparingInt(TeamData::getPoints).reversed());
        for (TeamData team : teams) {
            String teamName = team.getDisplayName();
            int points = team.getPoints();
            ChatColor teamColor = plugin.getScoreboardTeamNameColor();
            ChatColor pointsColor = plugin.getScoreboardPointsColor();
            sb.append(teamColor).append(teamName).append(": ").append(pointsColor).append(points).append(" Points").append("\n");
        }
        String teamScores = sb.toString().trim();
        spawnTextDisplay(location, teamScores);
    }

    public void spawnTextDisplay(Location location, String text) {
        textDisplay = (TextDisplay) plugin.getWorld().spawnEntity(location, EntityType.TEXT_DISPLAY);
        Transformation transformation = textDisplay.getTransformation();
        Transformation newScale = new Transformation(transformation.getTranslation(),
                transformation.getLeftRotation(),
                transformation.getScale().mul(plugin.getScoreboardSize()),
                transformation.getRightRotation()
        );
        textDisplay.setText(text);
        textDisplay.setBillboard(Display.Billboard.VERTICAL);
        textDisplay.setVisibleByDefault(true);
        textDisplay.setGravity(false);
        textDisplay.setAlignment(TextDisplay.TextAlignment.CENTER);
        textDisplay.setTransformation(newScale);
        textDisplay.setDisplayHeight(plugin.getConfig().getInt("scoreboardHeight"));
        textDisplay.setDisplayWidth(plugin.getConfig().getInt("scoreboardWidth"));
        textDisplay.setCustomName("scoreboard");
        textDisplays.add(textDisplay);

        plugin.setScoreboardLocation(location);
        plugin.setScoreboardText(text);
    }
    public void removeTextDisplay() {
        for (TextDisplay textDisplay : textDisplays) {
            textDisplay.remove();
        }
        textDisplays.clear();
    }
}
