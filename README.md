# Teams Plugin
## Table of Contents
[Summary](#Summary)  
[Installation](#Installation)  
[Commands](#Commands)  
[Configuration](#Configuration)
[Permissions](#permissions)

## Summary
A simple team management plugin for Spigot/Paper 1.20. It lets you create teams via config, add/remove players to teams, auto-assign players, manage points, view a leaderboard, and spawn a TextDisplay scoreboard in-world.

## Installation
1. Build the plugin
```
git clone https://github.com/extstudios/TeamsPlugin.git
cd TeamsPlugin
mvn clean package
```
The project is a Maven build targeting Java 16 and depending on spigot-api:1.20-R0.1-SNAPSHOT (scope provided).
2. Drop into your server
    - Place the built JAR in your server’s plugins/ folder.
    - The plugin declares api-version: '1.20'.
3. Edit config.yml, teams.yml, and messages.yml as needed, then /reload or restart.

## Commands
All commands are under the base command /ms (alias: /mobsoccer). Usage and behavior below

 Command                                 | What it does                                                                 
-----------------------------------------|------------------------------------------------------------------------------
 /teams add {player} {team}              | shows plugin info                                     
 /teams remove {player} {team}           | Sadd player to a team         
 /teams view {team}                      | remove player from their team
 /teams view {team}                      | list players in a team
 /teams auto {player}                    | auto-assign a player to the team with the fewest members (falls back to random)
 /teams reset {team}                     | remove all players from a team
 /teams leaderboard                      | print a colorized points leaderboard in chat
 /teams set teamname {team} {newName}    | change a team’s display name and refresh the scoreboard
 /teams set scoreboardlocation           | set the in-world TextDisplay scoreboard to your current location (creates/refreshes it)
 /teams points add {team} {amount}       | add points to a team
 /teams points remove {team} {amount}    | remove points from a team
 /teams points reset {team}              | reset a team’s points to 0


## Configuration

#### config.yml
```
world: 'world'
scoreboardSize: 13              # scale multiplier applied to the TextDisplay
ScoreboardTitleColor: 'Gold'
scoreboardTeamNameColor: 'Light_Purple'
scoreboardPointsColor: 'Aqua'
```

#### teams.yml
```
Teams:
  Team1:
    name: team1
    color: Red         # ChatColor name (see note below)
  Team2:
    name: team2
    color: Dark_Purple
  Team3:
    name: team3
    color: Gold
  Team4:
    name: team4
    color: Green
```
#### messages.yml
```
messages:
  error:
    unknownCommand: "&cUnknown command."
    noPermission: "&cYou do not have permission to use that command"
    playerNotFound: "&cCould not find player with name: %player%"
    invalidTeam: "&cCould not find team with name: %team%"
    playerNotOnTeam: "&c%player% is not on a team"
    amountNotNumber: "&c the amount must be a number"
  commands:
    confirmPlayerAddedToTeam: "Player %player% has been added to team %team%."
    confirmPlayerRemovedFromTeam: "Player %player% has been removed from team %team%"
    tellPlayerAddedToTeam: "You have been added to team %team%."
    tellPlayerRemovedFromTeam: "You have been removed from team %team%"
    teamCleared: "All players have been removed from team %team%"
    teamNameChanged: "The display name for %team% has been changed to %newTeamName%"
    noPlayersInTeam: "There are no players in team %team%"
    scoreboardLocationSet: "The scoreboard location has been set to your current location"
    checkTeam: "%player% is on team: %team%"
    leaderboard: "%team%: %points%"
    pointsAdded: "%amount% points have been added to %team%"
    pointsRemoved: "%amount% points have been removed from %team%"
    pointsReset: "Points for %team% have been reset"
```
##Permissions
Declared in plugin.yml (all default to op). There’s also a wildcard node with children.
