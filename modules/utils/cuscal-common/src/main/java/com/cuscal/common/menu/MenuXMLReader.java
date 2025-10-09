//
// Decompiled by Procyon v0.6-prerelease

//

package com.cuscal.common.menu;

import java.io.FileReader;

import java.util.LinkedList;

import org.apache.xerces.parsers.DOMParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import org.xml.sax.InputSource;

public class MenuXMLReader {

	public MenuXMLReader() {
	}

	public MenuXMLReader(final String menuXMLFilePath) {
		Document doc = null;

		try {
			final DOMParser dp = new DOMParser();
			dp.parse(new InputSource(new FileReader(menuXMLFilePath)));
			doc = dp.getDocument();
			this.menus = this.getMenu(doc);
		}
		catch (Exception e) {
			System.out.println("doc error=" + e.toString());
		}
	}

	public MenuItem getMenu(final Document doc) {
		final MenuItem homeItem = new MenuItem("home", "/");

		try {
			final NodeList nodelist = doc.getElementsByTagName("Menu");
			final Element elm = (Element)nodelist.item(0);
			final NodeList level1Menus = elm.getElementsByTagName("Level1");

			for (int j = 0; j < level1Menus.getLength(); ++j) {
				final Element level1Info = (Element)elm.getElementsByTagName(
					"Level1"
				).item(
					j
				);
				final MenuItem baseMenu = new MenuItem(
					level1Info.getAttribute("Label"),
					level1Info.getAttribute("Uri"));
				baseMenu.setAccess(
					this.roleList(level1Info.getAttribute("Role")));
				homeItem.addSubMenu(baseMenu);
				final NodeList level2Menus = level1Info.getElementsByTagName(
					"Level2");

				for (int k = 0; k < level2Menus.getLength(); ++k) {
					final Element level2Info =
						(Element)level1Info.getElementsByTagName(
							"Level2"
						).item(
							k
						);
					final MenuItem level2Menu = new MenuItem(
						level2Info.getAttribute("Label"),
						level2Info.getAttribute("Uri"));
					level2Menu.setAccess(
						this.roleList(level2Info.getAttribute("Role")));
					baseMenu.addSubMenu(level2Menu);
					final NodeList level3Menus =
						level2Info.getElementsByTagName("Level3");

					for (int l = 0; l < level3Menus.getLength(); ++l) {
						final Element level3Info =
							(Element)level2Info.getElementsByTagName(
								"Level3"
							).item(
								l
							);
						final MenuItem level3Menu = new MenuItem(
							level3Info.getAttribute("Label"),
							level3Info.getAttribute("Uri"));
						level3Menu.setAccess(
							this.roleList(level3Info.getAttribute("Role")));
						level2Menu.addSubMenu(level3Menu);
						final NodeList level4Menus =
							level3Info.getElementsByTagName("Level4");

						for (int m = 0; m < level4Menus.getLength(); ++m) {
							final Element level4Info =
								(Element)level3Info.getElementsByTagName(
									"Level4"
								).item(
									m
								);
							final MenuItem level4Menu = new MenuItem(
								level4Info.getAttribute("Label"),
								level4Info.getAttribute("Uri"));
							level4Menu.setAccess(
								this.roleList(level4Info.getAttribute("Role")));
							level3Menu.addSubMenu(level4Menu);
						}
					}
				}
			}
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}

		return homeItem;
	}

	public MenuItem getMenuItemObject() {
		return this.menus;
	}

	private LinkedList roleList(String roleString) {
		final LinkedList roles = new LinkedList();

		while (roleString.indexOf("|") != -1) {
			roles.addLast(roleString.substring(0, roleString.indexOf("|")));
			roleString = roleString.substring(
				roleString.indexOf("|") + 1, roleString.length());
		}

		roles.addLast(roleString);

		return roles;
	}

	private MenuItem menus;

}