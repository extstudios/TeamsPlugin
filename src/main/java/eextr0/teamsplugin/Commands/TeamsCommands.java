package eextr0.teamsplugin.Commands;

import eextr0.teamsplugin.Config.MessagesConfigManager;
import eextr0.teamsplugin.Data.TeamData;
import eextr0.teamsplugin.Data.TeamManager;
import eextr0.teamsplugin.TeamsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TeamsCommands implements TabExecutor {

    private final TeamsPlugin plugin;
    private List<String> playerList;

    public TeamsCommands (TeamsPlugin plugin) {
        this.plugin = plugin;
        this.playerList = new ArrayList<>();
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        TeamManager teamManager = plugin.getTeamManager();
        MessagesConfigManager messagesConfigManager = plugin.getMessagesConfigManager();

        if (args.length == 0) {
            commandSender.sendMessage("Plugin: TeamsPlugin");
            commandSender.sendMessage("Author: eextr0");
            commandSender.sendMessage("Version: 1.0");

            return true;
        }

        switch (args[0]) {
            case "add" -> {
                if (commandSender instanceof Player p && p.hasPermission("teams.add")) {
                    if (args.length < 3) {
                        commandSender.sendMessage("Usage: /teams add <player> <teamname>");
                        return true;
                    }
                    String targetPlayerName = args[1];
                    String teamName = args[2];
                    TeamData team = teamManager.getTeamByName(teamName);

                    if (team == null) {
                        p.sendMessage(messagesConfigManager.getErrorMessages().get("invalidTeam")
                                .replace("%team%", teamName));
                        return true;
                    }
                    Player checkPlayer = Bukkit.getPlayer(targetPlayerName);

                    if (checkPlayer == null) {
                        p.sendMessage(messagesConfigManager.getErrorMessages().get("playerNotFound")
                                .replace("%player%", targetPlayerName));
                        return true;
                    }
                    teamManager.addPlayer(teamName, targetPlayerName);
                    p.sendMessage(messagesConfigManager.getCommandMessages().get("confirmPlayerAddedToTeam")
                            .replace("%player%", targetPlayerName)
                            .replace("%team%", teamName));
                    checkPlayer.sendMessage(messagesConfigManager.getCommandMessages().get("tellPlayerAddedToTeam")
                            .replace("%team%", teamName));

                    return true;
                } else {
                    commandSender.sendMessage(messagesConfigManager.getErrorMessages().get("noPermission"));
                }
            }
            case "remove" -> {
                if (commandSender instanceof Player p && p.hasPermission("teams.remove")) {
                    if (args.length < 3) {
                        commandSender.sendMessage("Usage: /teams remove <player> teamname>");
                        return true;
                    }

                    String playerName = args[1];
                    String teamName = args[2];
                    TeamData team = teamManager.getTeamByName(teamName);

                    if (team == null) {
                        p.sendMessage(messagesConfigManager.getErrorMessages().get("invalidTeam")
                                .replace("%team%", teamName));
                        return true;
                    }
                    Player checkPlayer = Bukkit.getPlayer(playerName);

                    if (checkPlayer == null) {
                        p.sendMessage(messagesConfigManager.getErrorMessages().get("playerNotFound")
                                .replace("%player%", playerName));
                        return true;
                    }
                    teamManager.removePlayer(playerName);
                    commandSender.sendMessage(messagesConfigManager.getCommandMessages().get("confirmPlayerRemovedFromTeam")
                            .replace("%player%", playerName)
                            .replace("%team%", teamName));
                    checkPlayer.sendMessage(messagesConfigManager.getCommandMessages().get("tellPlayerRemovedFromTeam")
                            .replace("%team%", teamName));
                    return true;
                } else {
                    commandSender.sendMessage(messagesConfigManager.getErrorMessages().get("noPermission"));
                }
            }
            case "view" -> {
                if (commandSender instanceof Player p && p.hasPermission("teams.view")) {
                    if (args.length < 2) {
                        commandSender.sendMessage("Usage: /teams view <teamname>");
                        return true;
                    }
                    String teamName = args[1];

                    playerList = teamManager.getPlayersByTeamName(teamName);

                    if (!playerList.isEmpty()) {
                        StringBuilder message = new StringBuilder();
                        message.append("Players in ").append(teamName).append(": ");
                        for (String player : playerList) {
                            message.append(player).append(", ");
                        }
                        message.setLength(message.length() - 2);

                        p.sendMessage(message.toString());
                    } else {
                        p.sendMessage(messagesConfigManager.getCommandMessages().get("noPlayersInTeam").replace("%team%", teamName));
                    }

                    System.out.println(teamName);
                    System.out.println(playerList);
                    return true;
                } else {
                    commandSender.sendMessage(messagesConfigManager.getErrorMessages().get("noPermission"));
                }
            }
            case "auto" -> {
                if (commandSender instanceof Player p && p.hasPermission("teams.auto")) {
                    if (args.length < 2) {
                        commandSender.sendMessage("Usage: /teams auto <player>");
                        return true;
                    }
                    String playerName = args[1];
                    Player checkPlayer = Bukkit.getPlayer(playerName);

                    if (checkPlayer == null) {
                        p.sendMessage(messagesConfigManager.getErrorMessages().get("playerNotFound")
                                .replace("%player%", playerName));
                        return true;
                    }
                    String teamName = teamManager.assignPlayerToTeamWithLeastPlayers(playerName);
                    if (teamName != null) {
                        commandSender.sendMessage(messagesConfigManager.getCommandMessages().get("confirmPlayerAddedToTeam")
                                .replace("%player%", playerName)
                                .replace("%team%", teamName));
                        checkPlayer.sendMessage(messagesConfigManager.getCommandMessages().get("tellPlayerAddedToTeam")
                                .replace("%team%", teamName));
                        return true;
                    } else {
                        teamName = teamManager.assignPlayerToRandomTeam(playerName);
                        if (teamName != null) {
                            commandSender.sendMessage(messagesConfigManager.getCommandMessages().get("confirmPlayerAddedToTeam")
                                    .replace("%player%", playerName)
                                    .replace("%team%", teamName));
                            checkPlayer.sendMessage(messagesConfigManager.getCommandMessages().get("tellPlayerAddedToTeam")
                                    .replace("%team%", teamName));
                            return true;
                        }
                    }
                } else {
                    commandSender.sendMessage(messagesConfigManager.getErrorMessages().get("noPermission"));
                }
            }
            case "reset" -> {
                if (commandSender instanceof Player p && p.hasPermission("teams.reset")) {
                    if (args.length < 2) {
                        commandSender.sendMessage("Usage: /teams reset <teamName>");
                        return true;
                    }
                    String teamName = args[1];
                    List<String> playersToRemove = teamManager.removePlayersFromTeam(teamName);
                    for (String playerToRemove : playersToRemove) {
                        teamManager.removePlayer(playerToRemove);
                        Player removedPlayer = Bukkit.getPlayer(playerToRemove);
                        assert removedPlayer != null;
                        removedPlayer.sendMessage(messagesConfigManager.getCommandMessages().get("tellPlayerRemovedFromTeam")
                                .replace("%team%", teamName));
                    }
                    p.sendMessage(messagesConfigManager.getCommandMessages().get("teamCleared")
                            .replace("%team%", teamName));
                    return true;
                } else {
                    commandSender.sendMessage(messagesConfigManager.getErrorMessages().get("noPermission"));
                }
            }
            case "leaderboard" -> {
                if (commandSender instanceof Player p && p.hasPermission("teams.leaderboard")) {
                    if (args.length > 1) {
                        commandSender.sendMessage("Usage: /teams leaderboard");
                        return true;
                    }
                    StringBuilder sb = new StringBuilder();
                    ChatColor titleColor = plugin.getScoreboardTitleColor();
                    sb.append(titleColor).append("Team Points Leaderboard").append("\n");
                    List<TeamData> teams = new ArrayList<>(teamManager.getTeams().values());
                    teams.sort(Comparator.comparingInt(TeamData::getPoints).reversed());
                    for (TeamData team : teams) {
                        String teamName = team.getDisplayName();
                        int points = team.getPoints();
                        String message = messagesConfigManager.getCommandMessages().get("leaderboard")
                                .replace("%team%", teamName)
                                .replace("%points%", Integer.toString(points));

                        ChatColor teamColor = plugin.getScoreboardTeamNameColor();
                        ChatColor pointsColor = plugin.getScoreboardPointsColor();
                        sb.append(teamColor).append(teamName).append(": ").append(pointsColor).append(points).append(" Points").append("\n");
                    }
                    String teamScores = sb.toString().trim();
                    p.sendMessage(teamScores);
                    return true;
                } else {
                    commandSender.sendMessage(messagesConfigManager.getErrorMessages().get("noPermission"));
                }
            }
            case "set" -> {
                switch (args[1]) {
                    case "teamname" -> {
                        if (commandSender instanceof Player p && p.hasPermission("teams.set.teamname")) {
                            if (args.length != 4) {
                                commandSender.sendMessage("Usage: /teams set teamname <team> <newTeamName>");
                                return true;
                            }
                            String team = args[2];
                            String newTeamName = args[3];
                            boolean success = teamManager.changeTeamName(team, newTeamName);

                            if (success) {
                                p.sendMessage(messagesConfigManager.getCommandMessages().get("teamNameChanged")
                                        .replace("%team%", team)
                                        .replace("%newTeamName)", newTeamName)
                                );
                                plugin.createScoreboardDisplayTask().createDisplay(plugin.getScoreboardLocation());
                                return true;
                            } else {
                                p.sendMessage(messagesConfigManager.getErrorMessages().get("invalidTeam")
                                        .replace("%team%", team)
                                );
                                return true;
                            }
                        } else {
                            commandSender.sendMessage(messagesConfigManager.getErrorMessages().get("noPermission"));
                        }
                    }
                    case "scoreboardlocation" -> {
                        if (commandSender instanceof Player p && p.hasPermission("teams.set.scoreboardlocation")) {
                            if (args.length != 2) {
                                commandSender.sendMessage("Usage: /teams set scoreboardlocation");
                                return true;
                            }
                            Location location = p.getLocation();
                            plugin.createScoreboardDisplayTask().createDisplay(location);

                            p.sendMessage(messagesConfigManager.getCommandMessages().get("scoreboardLocationSet"));
                            return true;
                        } else {
                            commandSender.sendMessage(messagesConfigManager.getErrorMessages().get("noPermission"));
                        }
                    }
                }
            }

            case "points" -> {
                switch (args[1]) {
                    case "add" -> {
                        if (commandSender instanceof Player p && p.hasPermission("teams.points.add")) {
                            if (args.length > 4) {
                                commandSender.sendMessage("Usage: /teams points add <teamName> <amount>");
                                return true;
                            }
                            String teamName = args[2];
                            String amount = args[3];
                            int number = 0;
                            TeamData team = teamManager.getTeamByName(teamName);
                            if (team == null) {
                                p.sendMessage(messagesConfigManager.getErrorMessages().get("invalidTeam")
                                        .replace("%team%", teamName));
                                return true;
                            }
                            try {
                                number = Integer.parseInt(amount);
                                teamManager.updatePoints(teamName, number);
                                p.sendMessage(messagesConfigManager.getCommandMessages().get("pointsAdded")
                                        .replace("%amount%", amount)
                                        .replace("%team%", teamName));
                                plugin.createScoreboardDisplayTask().createDisplay(plugin.getScoreboardLocation());
                                return true;
                            } catch (NumberFormatException e) {
                                p.sendMessage(messagesConfigManager.getErrorMessages().get("amountNotNumber"));
                                return true;
                            }
                        } else {
                            commandSender.sendMessage(messagesConfigManager.getErrorMessages().get("noPermission"));
                        }
                    }
                    case "remove" -> {
                        if (commandSender instanceof Player p && p.hasPermission("teams.points.remove")) {
                            if (args.length > 4) {
                                commandSender.sendMessage("Usage: /teams points remove <teamName> <amount>");
                                return true;
                            }
                            String teamName = args[2];
                            String amount = args[3];
                            int number = 0;
                            TeamData team = teamManager.getTeamByName(teamName);
                            if (team == null) {
                                p.sendMessage(messagesConfigManager.getErrorMessages().get("invalidTeam")
                                        .replace("%team%", teamName));
                                return true;
                            }
                            try {
                                number = Integer.parseInt(amount);
                                teamManager.updatePoints(teamName, -number);
                                p.sendMessage(messagesConfigManager.getCommandMessages().get("pointsRemoved")
                                        .replace("%amount%", amount)
                                        .replace("%team%", teamName));
                                plugin.createScoreboardDisplayTask().createDisplay(plugin.getScoreboardLocation());
                                return true;
                            } catch (NumberFormatException e) {
                                p.sendMessage(messagesConfigManager.getErrorMessages().get("amountNotNumber"));
                                return true;
                            }
                        } else {
                            commandSender.sendMessage(messagesConfigManager.getErrorMessages().get("noPermission"));
                        }
                    }
                    case "reset" -> {
                        if (commandSender instanceof Player p && p.hasPermission("teams.points.reset")) {
                            if (args.length > 3) {
                                commandSender.sendMessage("Usage: /teams points reset <teamName>");
                                return true;
                            }
                            String teamName = args[2];
                            TeamData team = teamManager.getTeamByName(teamName);
                            if (team == null) {
                                p.sendMessage(messagesConfigManager.getErrorMessages().get("invalidTeam")
                                        .replace("%team%", teamName));
                                return true;
                            }
                            teamManager.updatePoints(teamName, -999999);
                            plugin.createScoreboardDisplayTask().createDisplay(plugin.getScoreboardLocation());
                            p.sendMessage(messagesConfigManager.getCommandMessages().get("pointsReset")
                                    .replace("%team%", teamName));
                        } else {
                            commandSender.sendMessage(messagesConfigManager.getErrorMessages().get("noPermission"));
                        }
                    }
                }
            }
            default -> {
                commandSender.sendMessage(plugin.getMessagesConfigManager().getErrorMessages().get("unknownCommand"));
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        return null;
    }
}
