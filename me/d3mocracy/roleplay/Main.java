package me.d3mocracy.roleplay;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class Main extends JavaPlugin {
    public static ArrayList<Player> rp = new ArrayList<>();
    public static Main main;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        main = this;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player p = (Player) sender;

        if (cmd.equalsIgnoreCase("roleplay")) {
            if (!p.hasPermission("roleplay.manage")) p.sendMessage("You don't have enough permissions to use this command.");
            if (rp.contains(p)) {p.sendMessage("You are already in roleplay mode"); return false;}
            rp.add(p);
            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1000000, 3000));
            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1000000, 4000));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 1000000, 3000));

            if (hasHelmet(p)) p.getInventory().getHelmet().addEnchantment(Enchantment.DURABILITY, 3);
            if (hasChestplate(p)) p.getInventory().getChestplate().addEnchantment(Enchantment.DURABILITY, 3);
            if (hasLeggings(p)) p.getInventory().getLeggings().addEnchantment(Enchantment.DURABILITY, 3);
            if (hasBoots(p)) p.getInventory().getBoots().addEnchantment(Enchantment.DURABILITY, 3);
            addSwordEnchant(p);
        }
        if (cmd.equalsIgnoreCase("clearr")) {
            PlayerListener.clearInv(p);
        }
        return false;
    }

    public static boolean hasHelmet(Player p) {
        if (p.getInventory().getHelmet() != null) {
            return true;
        } else
            return false;
    }
    public static boolean hasChestplate(Player p) {
        if (p.getInventory().getChestplate() != null) {
            return true;
        } else
            return false;
    }
    public static boolean hasLeggings(Player p) {
        if (p.getInventory().getLeggings() != null) {
            return true;
        } else
            return false;
    }
    public static boolean hasBoots(Player p) {
        if (p.getInventory().getBoots() != null) {
            return true;
        } else
            return false;
    }

    public static void addSwordEnchant(Player p) {
        for (ItemStack item : p.getInventory().getContents()) {
            try {
                if (item.getType().name().toLowerCase().contains("sword") || item.getType().name().toLowerCase().contains("bow")) {
                    item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
                }
            } catch (Exception e) {

            }
        }
    }
}
