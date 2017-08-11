package me.badbones69.crazyauctions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public class SettingsManager {

    static SettingsManager instance = new SettingsManager();

    public static SettingsManager getInstance() {
        return instance;
    }

    Plugin p;

    FileConfiguration config;
    File cfile;

    FileConfiguration data;
    File dfile;

    FileConfiguration msg;
    File mfile;

    public void setup(Plugin p) {
        if (!p.getDataFolder().exists()) {
            p.getDataFolder().mkdir();
        }

        cfile = new File(p.getDataFolder(), "config.yml");
        if (!cfile.exists()) {
            try {
                File en = new File(p.getDataFolder(), "/config.yml");
                InputStream E = getClass().getResourceAsStream("/config.yml");
                copyFile(E, en);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(cfile);

        dfile = new File(p.getDataFolder(), "Data.yml");
        if (!dfile.exists()) {
            try {
                File en = new File(p.getDataFolder(), "/Data.yml");
                InputStream E = getClass().getResourceAsStream("/Data.yml");
                copyFile(E, en);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        data = YamlConfiguration.loadConfiguration(dfile);

        mfile = new File(p.getDataFolder(), "Messages.yml");
        if (!mfile.exists()) {
            try {
                File en = new File(p.getDataFolder(), "/Messages.yml");
                InputStream E = getClass().getResourceAsStream("/Messages.yml");
                copyFile(E, en);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        msg = YamlConfiguration.loadConfiguration(mfile);
        updateFiles();
    }

    public FileConfiguration getData() {
        return data;
    }

    public FileConfiguration getMsg() {
        return msg;
    }

    public void saveData() {
        try {
            data.save(dfile);
        } catch (IOException e) {
            Bukkit.getServer().getLogger()
                    .severe(ChatColor.RED + "Could not save Data.yml!");
        }
    }

    public void saveMsg() {
        try {
            msg.save(mfile);
        } catch (IOException e) {
            Bukkit.getServer().getLogger()
                    .severe(ChatColor.RED + "Could not save Messages.yml!");
        }
    }

    public void reloadData() {
        data = YamlConfiguration.loadConfiguration(dfile);
    }

    public void reloadMsg() {
        msg = YamlConfiguration.loadConfiguration(mfile);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void saveConfig() {
        try {
            config.save(cfile);
        } catch (IOException e) {
            Bukkit.getServer().getLogger()
                    .severe(ChatColor.RED + "Could not save config.yml!");
        }
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(cfile);
    }

    public PluginDescriptionFile getDesc() {
        return p.getDescription();
    }

    public static void copyFile(InputStream in, File out) throws Exception { // https://bukkit.org/threads/extracting-file-from-jar.16962/
        InputStream fis = in;
        FileOutputStream fos = new FileOutputStream(out);
        try {
            byte[] buf = new byte[1024];
            int i = 0;
            while ((i = fis.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }

    private void updateFiles() {
        if (config != null) {
            if (!config.isSet("Settings.BlackListLore")) {
                List<String> blore = new ArrayList<>();
                blore.add("Event item");
                blore.add("[eE]vent [iItem]");
                blore.add(".*[eE]vent.*");
                config.set("Settings.BlackListLore", blore);
                saveConfig();
                reloadConfig();
            }
        }
    }
}
