package eextr0.teamsplugin.Config;

import eextr0.teamsplugin.TeamsPlugin;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class MessagesConfigManager extends ConfigManager {

    private final Map<String, String> errorMessages = new HashMap<>();
    private final Map<String, String> commandMessages = new HashMap<>();
    public MessagesConfigManager(TeamsPlugin plugin) {
        super(plugin);
        if(plugin.messagesFile != null && plugin.messagesConfigStream != null) {
            updateConfig(plugin.messagesFile, plugin.messagesConfigStream);
        }
        createConfig("messages.yml");

        load();
    }

    public void load() {
        ConfigurationSection errorSection = getConfig().getConfigurationSection("messages.error");
        for (String key : errorSection.getKeys(false)) {
            String message = translateText(errorSection, key);
            errorMessages.put(key, message);
        }

        ConfigurationSection commandSection = getConfig().getConfigurationSection("messages.commands");
        for (String key : commandSection.getKeys(false)) {
            String message = translateText(commandSection, key);
            commandMessages.put(key, message);
        }

    }
    public String translateText(ConfigurationSection config, String key) {

        String text = config.getString(key);
        String translatedText = "";
        if (!text.isEmpty()) {
            translatedText = ChatColor.translateAlternateColorCodes('&', text);
        }
        return translatedText;
    }

    public Map<String, String> getErrorMessages() {return errorMessages;}
    public Map<String, String> getCommandMessages() {return commandMessages;}

}
