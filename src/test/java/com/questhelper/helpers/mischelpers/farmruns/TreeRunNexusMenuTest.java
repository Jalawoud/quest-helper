package com.questhelper.helpers.mischelpers.farmruns;

import net.runelite.api.Client;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.Widget;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.anyString;

class TreeRunNexusMenuTest
{
	@Test
	void readsNativeLetterWithoutChangingTheMenu()
	{
		Client client = mock(Client.class);
		Widget root = mock(Widget.class), row = mock(Widget.class);
		when(client.getWidget(InterfaceID.TelenexusTeleport.UNIVERSE)).thenReturn(root);
		when(root.getChildren()).thenReturn(new Widget[]{row});
		when(row.getText()).thenReturn("<col=ff9040>G:</col> Varrock");
		assertEquals("G", TreeRunNexusMenu.hotkey(client, "Varrock"));
		assertNull(TreeRunNexusMenu.hotkey(client, "Falador"));
		verify(row, never()).setText(anyString());
	}

	@Test
	void doesNotInventAKeyForClosedOrHiddenMenus()
	{
		Client client = mock(Client.class);
		assertNull(TreeRunNexusMenu.hotkey(client, "Varrock"));
		Widget root = mock(Widget.class);
		when(client.getWidget(InterfaceID.TelenexusTeleport.UNIVERSE)).thenReturn(root);
		when(root.isHidden()).thenReturn(true);
		assertNull(TreeRunNexusMenu.hotkey(client, "Varrock"));
	}
}
