package me.johnny.simplestars.util;

import org.bukkit.ChatColor;

public final class SimpleStarsUtil {

    public static String cc(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static int calculateExpForLevel(int level, int baseExp) {
        return SimpleStarsUtil.getFactorial(level) * baseExp;
    }

    public static int getFactorial(int base) {
        int fact = 1;
        for (int i = 1; i < base + 1; i++) {
            fact *= i;
        }
        return fact;
    }

}
