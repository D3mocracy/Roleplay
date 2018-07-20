package me.d3mocracy.roleplay;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

public class PlayerListener implements Listener {
    public static HashMap<Player, ItemStack[]> items = new HashMap<>();
    public static HashMap<Player, ItemStack[]> armor = new HashMap<>();
    public int number = 1;

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event){

        if (items.containsKey(event.getPlayer()) && armor.containsKey(event.getPlayer())) {
            clearInv(event.getPlayer());

            for(ItemStack stack : items.get(event.getPlayer())){
                if (stack == null) continue;
                event.getPlayer().getInventory().addItem(stack);
            }

            for(ItemStack stack : armor.get(event.getPlayer())){
                if (stack == null) continue;
                event.getPlayer().getInventory().setArmorContents(armor.get(event.getPlayer()));
            }

            armor.remove(event.getPlayer());
            items.remove(event.getPlayer());
        }

        if (Main.rp.contains(event.getPlayer())) {
            number = 1;
            if (number != -1) {
                Main.main.getServer().getScheduler().scheduleSyncRepeatingTask(Main.main, new Runnable() {
                    @Override
                    public void run() {
                        if (number != 0) {
                            number--;
                        } else {
                            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1000000, 3000));
                            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1000000, 4000));
                            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 1000000, 3000));
                            number--;
                        }
                    }
                }, 0L, 20L);
            }
        }


    }





    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        if (Main.rp.contains(event.getEntity())) {

            ItemStack[] content = event.getEntity().getInventory().getContents();
            ItemStack[] arm = event.getEntity().getInventory().getArmorContents();

            items.put(event.getEntity(), content);
            armor.put(event.getEntity(), arm);

            event.getDrops().clear();
            clearInv(event.getEntity());
        }
    }

    @EventHandler()
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (Main.rp.contains(e.getPlayer())) {
            Main.rp.remove(e.getPlayer());
            //e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 0, 0));
            e.getPlayer().removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            e.getPlayer().removePotionEffect(PotionEffectType.REGENERATION);
            e.getPlayer().removePotionEffect(PotionEffectType.SATURATION);
            if (Main.hasHelmet(e.getPlayer())) e.getPlayer().getInventory().getHelmet().removeEnchantment(Enchantment.DURABILITY);
            if (Main.hasChestplate(e.getPlayer())) e.getPlayer().getInventory().getChestplate().removeEnchantment(Enchantment.DURABILITY);
            if (Main.hasLeggings(e.getPlayer())) e.getPlayer().getInventory().getLeggings().removeEnchantment(Enchantment.DURABILITY);
            if (Main.hasBoots(e.getPlayer())) e.getPlayer().getInventory().getBoots().removeEnchantment(Enchantment.DURABILITY);
            remSwordEnchant(e.getPlayer());
        }
    }

    @EventHandler
    public void onDurabilty(PlayerItemDamageEvent e) {
        if (Main.rp.contains(e.getPlayer())) {
            e.setCancelled(true);
            e.getItem().setDurability((short) (e.getItem().getDurability() - 1));
            if (e.getItem().getType().name().toLowerCase().contains("bow")) {
                e.getItem().setDurability((short) (e.getItem().getDurability() - 1));
            }
        }

    }


    public static void remSwordEnchant(Player p) {
        for (ItemStack item : p.getInventory().getContents()) {
            try {
                if (item.getType().name().toLowerCase().contains("sword") || item.getType().name().toLowerCase().contains("bow")) {
                    item.removeEnchantment(Enchantment.DURABILITY);
                }
            } catch (Exception e) {

            }
        }
    }

    public static void clearInv(Player p) {
        PlayerInventory inv = p.getInventory();
        inv.clear();
        inv.setArmorContents(null);
    }
}
