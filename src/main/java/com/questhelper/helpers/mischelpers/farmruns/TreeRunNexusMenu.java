package com.questhelper.helpers.mischelpers.farmruns;

import net.runelite.api.Client;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.util.Text;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Read the player's displayed shortcut; never rewrite native menu labels or keybinds. */
final class TreeRunNexusMenu
{
	private static final Pattern KEY = Pattern.compile("^([a-z0-9])\\s*[:.)]\\s*(.+)$", Pattern.CASE_INSENSITIVE);

	static String hotkey(Client client, String destination)
	{
		Widget root = client.getWidget(InterfaceID.TelenexusTeleport.UNIVERSE);
		if (root == null || root.isHidden()) return null;
		return find(root, destination, Collections.newSetFromMap(new IdentityHashMap<>()));
	}

	private static String find(Widget widget, String destination, Set<Widget> visited)
	{
		if (widget == null || widget.isHidden() || !visited.add(widget)) return null;
		String text = widget.getText();
		if (text != null)
		{
			Matcher match = KEY.matcher(Text.removeTags(text).replace('\u00a0', ' ').trim());
			if (match.matches() && match.group(2).trim().equalsIgnoreCase(destination))
				return match.group(1).toUpperCase(java.util.Locale.ROOT);
		}
		Widget[][] children = {widget.getChildren(), widget.getStaticChildren(), widget.getDynamicChildren()};
		for (Widget[] group : children)
		{
			if (group == null) continue;
			for (Widget child : group)
			{
				String key = find(child, destination, visited);
				if (key != null) return key;
			}
		}
		return null;
	}
}
