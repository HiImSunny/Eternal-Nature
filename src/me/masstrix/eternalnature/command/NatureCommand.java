package me.masstrix.eternalnature.command;

import me.masstrix.eternalnature.EternalNature;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class NatureCommand extends EternalCommand {

    private EternalNature plugin;

    public NatureCommand(EternalNature plugin) {
        super("eternalnature");
        this.plugin = plugin;
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            msg("");
            msg("     &2&lEternal Nature");
            msg("     &7&oby Masstrix");
            msg("");
            msg("&a/eternal reload &7- Reloads all config files.");
            msg("&a/eternal stats &7- Shows background stats.");
            msg("&a/eternal version &7- View version and update info.");
            msg("&a/eternal setting &7- Opens a GUI to edit settings.");
            msg("&a/hydrate <user> &7- Hydrates a user to max.");
            msg("");
            return;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            plugin.getEngine().getTemperatureData().loadConfigData();
            plugin.getSystemConfig().reload();
            msg("Reloading files...");
            msg("&aReloaded config files");
        }

        else if (args[0].equalsIgnoreCase("setting")) {
            if (wasPlayer()) plugin.getSettingsGUI().open((Player) getSender());
        }

        else if (args[0].equalsIgnoreCase("stats")) {
            msg("");
            msg("     &e&lEternal Nature");
            msg("     &7Background Stats");
            msg("");
            msg("Players cached: &7" + plugin.getEngine().getCashedUsers().size());
            msg("Worlds Loaded: &7" + plugin.getEngine().getWorldProvider().getLoaded());
            msg("");
        }

        else if (args[0].equalsIgnoreCase("version")) {
            msg("");
            msg("     &e&lEternal Nature");
            msg("     &7Version Info");
            msg("");
            msg(" Current Version: &7" + plugin.getDescription().getVersion());
            if (plugin.getVersionMeta() == null) {
                if (plugin.getSystemConfig().checkForUpdates()) {
                    msg("&cUnable to check plugin version.");
                } else {
                    msg("&cVersion checking is disabled.");
                }
            } else {
                switch (plugin.getVersionMeta().getState()) {
                    case UNKNOWN: {
                        msg("&cError trying to check version.");
                        break;
                    }
                    case BEHIND: {
                        msg(" Latest: &7" + plugin.getVersionMeta().getLatestVersion() + " &6(update available)");

                        ClickEvent click = new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/natural-environment.43290/history");
                        HoverEvent hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[] {
                                new TextComponent("\u00A7eClick to view latest release.\n\u00A77Redirects to spigot.org")
                        });

                        TextComponent text = new TextComponent(" ");
                        TextComponent info = new TextComponent("CLICK HERE TO UPDATE");
                        info.setBold(true);
                        info.setColor(ChatColor.GOLD);
                        info.setClickEvent(click);
                        info.setHoverEvent(hover);

                        if (wasPlayer()) {
                            ((Player) getSender()).spigot().sendMessage(text, info);
                        }
                        break;
                    }
                    case DEV_BUILD: {
                        msg("&c This is a development build and may be unstable. Please report any bugs.");
                        break;
                    }
                    case LATEST: {
                        msg("&a Plugin is up to date.");
                    }
                }
            }
            msg("");
        }
    }

    @Override
    public List<String> tabComplete(String[] args) {
        if (args.length == 1) {
            return Arrays.asList("reload", "stats", "version", "setting");
        }
        return super.tabComplete(args);
    }
}