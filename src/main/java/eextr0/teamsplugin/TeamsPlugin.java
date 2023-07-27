package eextr0.teamsplugin;

import eextr0.teamsplugin.Commands.TeamsCommands;
import eextr0.teamsplugin.Config.MessagesConfigManager;
import eextr0.teamsplugin.Config.TeamsConfigManager;
import eextr0.teamsplugin.Data.TeamManager;
import eextr0.teamsplugin.Tasks.CreateScoreboardDisplayTask;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;
import org.checkerframework.checker.units.qual.C;

import java.io.File;
import java.io.InputStream;

public final class TeamsPlugin extends JavaPlugin {

    public InputStream messagesConfigStream;
    public File messagesFile;
    public InputStream teamsConfigStream;
    public File teamsFile;
    public boolean scoreboardCommandExecuted = false;
    private MessagesConfigManager messagesConfigManager;
    private TeamsConfigManager teamsConfigManager;
    private TeamManager teamManager;
    private CreateScoreboardDisplayTask createScoreboardDisplayTask;
    private Location scoreboardLocation;
    private String scoreboardText;
    String world = getConfig().getString("world");

    ChatColor scoreboardTitleColor = ChatColor.valueOf(getConfig().getString("ScoreboardTitleColor").toUpperCase());
    ChatColor scoreboardTeamNameColor = ChatColor.valueOf(getConfig().getString("scoreboardTeamNameColor").toUpperCase());
    ChatColor scoreboardPointsColor = ChatColor.valueOf(getConfig().getString("scoreboardPointsColor").toUpperCase());

    Integer scoreboardSize = getConfig().getInt("scoreboardSize");

    //Getters
    public TeamManager getTeamManager() {return teamManager;}
    public MessagesConfigManager getMessagesConfigManager() {return messagesConfigManager;}
    public Location getScoreboardLocation() {return scoreboardLocation;}
    public String getScoreboardText() {return scoreboardText;}
    public World getWorld() {
        return Bukkit.getWorld(world);
    }
    public Integer getScoreboardSize() {return scoreboardSize;}
    public ChatColor getScoreboardTitleColor() {return scoreboardTitleColor;}
    public ChatColor getScoreboardTeamNameColor() {return scoreboardTeamNameColor;}
    public ChatColor getScoreboardPointsColor() {return scoreboardPointsColor;}
    public CreateScoreboardDisplayTask createScoreboardDisplayTask() {return createScoreboardDisplayTask;}
    //Setters
    public void setScoreboardLocation(Location scoreboardLocation) {this.scoreboardLocation = scoreboardLocation;
    }

    public void setScoreboardText(String scoreboardText) {this.scoreboardText = scoreboardText;}

    @Override
    public void onEnable() {

        getConfig().options().copyDefaults();
        saveDefaultConfig();
        this.messagesConfigStream = getResource("messages.yml");
        this.messagesFile = new File(getDataFolder(), "messages.yml");

        teamManager = new TeamManager(this);
        createScoreboardDisplayTask = new CreateScoreboardDisplayTask(this);
        this.messagesConfigManager = new MessagesConfigManager(this);
        this.teamsConfigManager = new TeamsConfigManager(this);

        teamsConfigManager.load();

        getCommand("teams").setExecutor(new TeamsCommands(this));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
