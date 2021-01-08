package me.jishunamatata.plugingui.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;

public class Utils {

	public static List<String> splitString(String msg, int length, boolean keepColor) {
		List<String> res = new ArrayList<String>();

		Pattern p = Pattern.compile("(?<![\\w&\\u00A7]).{1," + length + "}(?![\\w&\\u00A7])");
		Matcher m = p.matcher(msg);
		int index = 0;

		while (m.find()) {
			if (keepColor && index > 0) {
				res.add(ChatColor.getLastColors(res.get(index - 1)) + m.group().trim());
			} else {
				res.add(m.group().trim());
			}
			index++;
		}
		return res;
	}

}
