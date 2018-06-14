package me.bariahq.bariahqmod.rank;

import lombok.Getter;
import org.bukkit.ChatColor;

public enum Title implements Displayable
{
    TFDEV("a", "TotalFreedom Developer", ChatColor.DARK_PURPLE, "TF-Dev"),
    BHQDEV("an", "BariaHQ Developer", ChatColor.DARK_PURPLE, "BHQ-Dev"),
    EXEC("an", "Executive", ChatColor.RED, "Exec"),
    FOUNDER("the", "Founder", ChatColor.DARK_AQUA, "Founder"),
    OWNER("an", "Owner", ChatColor.DARK_AQUA, "Owner");

    @Getter
    private final String determiner;
    @Getter
    private final String name;
    @Getter
    private final String tag;
    @Getter
    private final String coloredTag;
    @Getter
    private final ChatColor color;

    private Title(String determiner, String name, ChatColor color, String tag)
    {
        this.determiner = determiner;
        this.name = name;
        this.tag = "[" + tag + "]";
        this.coloredTag = ChatColor.DARK_GRAY + "[" + color + tag + ChatColor.DARK_GRAY + "]" + color;
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
