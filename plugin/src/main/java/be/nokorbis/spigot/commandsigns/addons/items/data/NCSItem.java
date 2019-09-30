package be.nokorbis.spigot.commandsigns.addons.items.data;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public final class NCSItem {

    private Material type;
    private int quantity = 1;
    private String name;
    private List<String> lore = new ArrayList<>(1);
    private boolean handOnly = false;

    public NCSItem(Material type) {
        this.type = type;
    }

    public Material getType() {
        return type;
    }

    public void setType(Material type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public boolean isHandOnly() {
        return handOnly;
    }

    public void setHandOnly(boolean handOnly) {
        this.handOnly = handOnly;
    }
}
