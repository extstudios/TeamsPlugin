package eextr0.teamsplugin.Data;

import eextr0.teamsplugin.TeamsPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class TeamManager {

    private final TeamsPlugin plugin;
    private final HashMap<String, TeamData> teams;
    private final List<String> teamsList;
    private final HashMap<String, String> playerTeams;
    private int points;

    public TeamManager (TeamsPlugin plugin) {
        this.plugin = plugin;
        this.teams = new HashMap<>();
        this.teamsList = new ArrayList<>();
        this.playerTeams = new HashMap<>();
    }

    public void createTeam(String teamName, String displayName, ChatColor color) {
        if (!teams.containsKey(teamName)) {
            points = 0;
            teams.put(teamName, new TeamData(displayName, points, color));
            teamsList.add(teamName);
        }
    }
    public TeamData getTeamByName(String teamName) {return teams.get(teamName);}
    public HashMap<String, String> getPlayerTeams() {return playerTeams;}

    public void updatePoints(String teamName, int points) {
        TeamData team = teams.get(teamName);
        if (team != null) {
            team.updatePoints(points);
            if (team.getPoints() < 0) {
                team.setPoints(0);
            }
        }
    }
    public void addPlayer(String playerName, String teamName) {
            playerTeams.put(playerName, teamName);
        System.out.println(playerTeams);
    }
    public void removePlayer(String playerName) {

        for (Map.Entry<String, String> entry : playerTeams.entrySet()) {
            String player = entry.getValue();
            String team = entry.getKey();

            if (player.equalsIgnoreCase(playerName)) {
                playerTeams.remove(team);
                System.out.println("Player " + playerName + " removed from team " + team);
                return;
            }
        }
    }

    public List<String> getPlayersByTeamName(String teamName) {
        List<String> playersInTeam = new ArrayList<>();
        for (Map.Entry<String, String> entry : playerTeams.entrySet()) {
            String player = entry.getValue();
            String team = entry.getKey();
            if (team.equalsIgnoreCase(teamName)) {
                playersInTeam.add(player);
            }
        }
        return playersInTeam;
    }
    public String assignPlayerToTeamWithLeastPlayers(String playerName) {
        Map<String, Integer> teamPlayerCounts = new HashMap<>();

        for (String team : teamsList) {
            int playerCount = getPlayersByTeamName(team).size();
            teamPlayerCounts.put(team, playerCount);
        }

        int minPlayerCount = Integer.MAX_VALUE;
        for (int count : teamPlayerCounts.values()) {
            if (count < minPlayerCount) {
                minPlayerCount = count;
            }
        }

        List<String> teamsWithLeastPlayers = new ArrayList<>();
        for (Map.Entry<String,Integer> entry : teamPlayerCounts.entrySet()) {
            if (entry.getValue() == minPlayerCount) {
                teamsWithLeastPlayers.add(entry.getKey());
            }
        }

        if (!teamsWithLeastPlayers.isEmpty()) {
            Random random = new Random();
            String randomTeam = teamsWithLeastPlayers.get(random.nextInt(teamsWithLeastPlayers.size()));
            addPlayer(randomTeam, playerName);
            return randomTeam;
        } else {
            return null;
        }
    }

    public String assignPlayerToRandomTeam(String playerName) {
        Random random = new Random();
        String randomTeam = teamsList.get(random.nextInt());
        addPlayer(playerName, randomTeam);
        return randomTeam;
    }

    public List<String> removePlayersFromTeam(String teamName) {
        List<String> playersToRemove = new ArrayList<>();

        for (Map.Entry<String, String> entry : playerTeams.entrySet()) {
            String player = entry.getValue();
            String team = entry.getKey();

            if (team.equalsIgnoreCase(teamName)) {
                playersToRemove.add(player);
            }
        }
        return playersToRemove;
    }
    public HashMap<String, TeamData> getTeams() {
    return teams;
    }

    public boolean changeTeamName(String team, String newTeamName) {
        TeamData teamData= teams.get(team);
        if (teamData != null) {
            teamData.setName(newTeamName);
            return true;
        }else {
            return false;
        }
    }
}
