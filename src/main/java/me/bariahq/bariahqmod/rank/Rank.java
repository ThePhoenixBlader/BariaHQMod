package me.bariahq.bariahqmod.rank;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;

public enum Rank implements Displayable
{

    IMPOSTOR("an", "Impostor", Type.PLAYER, "IMP", ChatColor.DARK_AQUA),
    NON_OP("a", "Non-OP", Type.PLAYER, "", ChatColor.WHITE),
    OP("an", "OP", Type.PLAYER, "OP", ChatColor.DARK_AQUA),
    DONATOR("a", "Donator", Type.PLAYER, "Donator", ChatColor.GREEN),
    MOD("a", "Moderator", Type.STAFF, "Mod", ChatColor.GOLD),
    ADMIN("an", "Administrator", Type.STAFF, "Admin", ChatColor.RED),
    SENIOR_ADMIN("a", "Senior Admin", Type.STAFF, "SrA", ChatColor.LIGHT_PURPLE),
    ADMIN_CONSOLE("the", "Console", Type.STAFF_CONSOLE, "Console", ChatColor.DARK_PURPLE),
    SENIOR_CONSOLE("the", "Console", Type.STAFF_CONSOLE, "Console", ChatColor.DARK_PURPLE);
    @Getter
    private final Type type;
    @Getter
    private final String name;
    private final String determiner;
    @Getter
    private final String tag;
    @Getter
    private final String coloredTag;
    @Getter
    private final String abbr;
    @Getter 
    private final ChatColor color;

    private Rank(String determiner, String name, Type type, String abbr, ChatColor color)
    {
        this.type = type;
        this.name = name;
        this.determiner = determiner;
        this.tag = abbr.isEmpty() ? "" : abbr.toUpperCase();
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
    
    public boolean isConsole()
    {
        return getType() == Type.STAFF_CONSOLE;
    }

    public int getLevel()
    {
        return ordinal();
    }

    public boolean isAtLeast(Rank rank)
    {
        if (getLevel() < rank.getLevel())
        {
            return false;
        }

        if (!hasConsoleVariant() || !rank.hasConsoleVariant())
        {
            return true;
        }

        return getConsoleVariant().getLevel() >= rank.getConsoleVariant().getLevel();
    }

    public boolean isStaff()
    {
        return getType() == Type.STAFF || getType() == Type.STAFF_CONSOLE;
    }

    public boolean hasConsoleVariant()
    {
        return getConsoleVariant() != null;
    }

    public Rank getConsoleVariant()
    {
        switch (this)
        {
            case ADMIN:
            case ADMIN_CONSOLE:
                return ADMIN_CONSOLE;
            case SENIOR_ADMIN:
            case SENIOR_CONSOLE:
                return SENIOR_CONSOLE;
            default:
                return null;
        }
    }

    public static Rank findRank(String string)
    {
        try
        {
            return Rank.valueOf(string.toUpperCase());
        }
        catch (Exception ignored)
        {
        }

        return Rank.NON_OP;
    }

    public static enum Type
    {

        PLAYER,
        STAFF,
        STAFF_CONSOLE;

        public boolean isStaff()
        {
            return this != PLAYER;
        }
    }

}
