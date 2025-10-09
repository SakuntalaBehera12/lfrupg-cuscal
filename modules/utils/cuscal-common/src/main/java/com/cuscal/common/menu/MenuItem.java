//
// Decompiled by Procyon v0.6-prerelease

//

package com.cuscal.common.menu;

import java.util.LinkedList;
import java.util.ListIterator;

public class MenuItem {

	public static void main(final String[] args) {
	}

	public MenuItem(final String label, final String URI) {
		this.subMenus = new LinkedList();
		this.itemLabel = "";
		this.itemURI = "";
		this.access = new LinkedList();
		this.currentLevel = 0;
		this.itemLabel = label;
		this.itemURI = URI;
	}

	public MenuItem(
		final String label, final String URI, final LinkedList subMenus) {

		this.subMenus = subMenus;

		this.subMenus = new LinkedList();
		this.itemLabel = "";
		this.itemURI = "";
		this.access = new LinkedList();
		this.currentLevel = 0;
		this.itemLabel = label;
		this.itemURI = URI;
	}

	public MenuItem(final String label, final String URI, final String access) {
		this.subMenus = new LinkedList();
		this.itemLabel = "";
		this.itemURI = "";
		this.access = new LinkedList();
		this.currentLevel = 0;
		this.itemLabel = label;
		this.itemURI = URI;
		this.access.addLast(access);
	}

	public MenuItem(
		final String label, final String URI, final String access,
		final LinkedList subMenus) {

		this.subMenus = new LinkedList();
		this.itemLabel = "";
		this.itemURI = "";
		this.access = new LinkedList();
		this.currentLevel = 0;
		this.itemLabel = label;
		this.itemURI = URI;
		this.access.addLast(access);
		this.subMenus = subMenus;
	}

	public void addRole(final String access) {
		this.access.addLast(access);
	}

	public void addSubMenu(final MenuItem menu) {
		this.subMenus.addLast(menu);
	}

	public LinkedList getAccess() {
		return this.access;
	}

	public String getItemLabel() {
		return this.itemLabel;
	}

	public String getItemURI() {
		return this.itemURI;
	}

	public LinkedList getSubMenus() {
		return this.subMenus;
	}

	public void setAccess(final LinkedList access) {
		this.access = access;
	}

	public void setItemLabel(final String itemLabel) {
		this.itemLabel = itemLabel;
	}

	public void setItemURI(final String itemURI) {
		this.itemURI = itemURI;
	}

	public void setSubMenus(final LinkedList subMenus) {
		this.subMenus = subMenus;
	}

	@Override
	public String toString() {
		return String.valueOf(this.itemLabel) + ": " + this.itemURI + " : " +
			this.access + "\n" + this.render(this);
	}

	private String render(final MenuItem item) {
		++this.currentLevel;
		String returnString = "";
		LinkedList listy = new LinkedList();

		listy = item.getSubMenus();
		final ListIterator iter = listy.listIterator();

		while (iter.hasNext()) {
			final MenuItem currentItem = (MenuItem)iter.next();

			for (int i = 0; i < this.currentLevel; ++i) {
				returnString = String.valueOf(returnString) + "\t";
			}

			returnString =
				String.valueOf(returnString) + currentItem.getItemLabel() +
					": " + currentItem.getItemURI() + " : " +
						currentItem.getAccess() + "\n" +
							this.render(currentItem);
		}

		--this.currentLevel;

		return returnString;
	}

	private LinkedList access;
	private int currentLevel;
	private String itemLabel;
	private String itemURI;
	private LinkedList subMenus;

}