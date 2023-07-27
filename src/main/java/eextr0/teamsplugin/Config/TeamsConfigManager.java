package eextr0.teamsplugin.Config;

import eextr0.teamsplugin.Config.ConfigManager;
import eextr0.teamsplugin.Data.TeamData;
import eextr0.teamsplugin.Data.TeamManager;
import eextr0.teamsplugin.TeamsPlugin;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;

public class TeamsConfigManager extends ConfigManager {

    private HashMap<String, TeamData> teams;

    public TeamsConfigManager(TeamsPlugin plugin) {

        super(plugin);
        if (plugin.teamsFile != null && plugin.teamsConfigStream != null) {
            updateConfig(plugin.teamsFile, plugin.teamsConfigStream);
        }
        createConfig("teams.yml");

        load();
    }

    public void load() {

        TeamManager teamManager = plugin.getTeamManager();
        ConfigurationSection teamSection = getConfig().getConfigurationSection("Teams");
        if (teamSection != null) {
            for (String key : teamSection.getKeys(false)) {
                ConfigurationSection teamConfig = teamSection.getConfigurationSection(key);
                String teamName = teamConfig.getString("name");
                String colorString = teamConfig.getString("color");
                ChatColor color = ChatColor.WHITE;

                if (colorString != null) {
                    try {
                        color = ChatColor.valueOf(colorString.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                }
                TeamData teamData = teamManager.getTeamByName(teamName);
                if (teamData != null) {
                    teamData.setName(teamName);
                    teamData.setPoints(0);
                    teamData.setColor(color);
                } else {
                    teamManager.createTeam(teamName, teamName, color);
                }
            }
        }
    }
}
