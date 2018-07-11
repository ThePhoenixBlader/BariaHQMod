package me.bariahq.bariahqmod.rank;

import lombok.Getter;
import org.bukkit.ChatColor;

public enum Title implements Displayable
{
    ARCHITECT("an", "Architect", ChatColor.GREEN, "Architect"),
    TFDEV("a", "TotalFreedom Developer", ChatColor.DARK_PURPLE, "TF-Dev"),
    BHQDEV("a", "BariaHQ Developer", ChatColor.DARK_PURPLE, "Dev"),
    MANAGER("a", "Manager", ChatColor.BLUE, "Manager"),
    FOUNDER("the", "Founder", ChatColor.DARK_AQUA, "Founder"),
    OWNER("an", "Owner", ChatColor.DARK_AQUA, "Owner");

    private final String determiner;
    @Getter
    private final String name;
    @Getter
    private final String tag;
    @Getter
    private final String coloredTag;
    @Getter
    private final String abbr;
    @Getter
    private final ChatColor color;

    private Title(String determiner, String name, ChatColor color, String abbr)
    {
        this.determiner = determiner;
        this.name = name;
        this.tag = abbr.toUpperCase();
        this.coloredTag = abbr.isEmpty() ? "" : color + "" + ChatColor.BOLD + abbr.toUpperCase();
        this.abbr = abbr;
        this.color = color;
    }

    @Override
    public String getColoredName()
    {
        return color + name;
    }

    @Override
    public String getColoredLoginMessage()
    {
        return determiner + " " + color + ChatColor.ITALIC + name;
    }

    @Override
    public String getDeterminer()
    {
        return determiner;
    }

    @Override
    public String getItalicColoredName()
    {
        return color.toString() + ChatColor.ITALIC + name;
    }

}
