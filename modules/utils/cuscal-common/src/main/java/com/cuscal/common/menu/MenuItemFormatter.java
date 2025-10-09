//
// Decompiled by Procyon v0.6-prerelease

//

package com.cuscal.common.menu;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;

public class MenuItemFormatter {

	public static void main(final String[] args) {
		final MenuItem main = new MenuItem("home", "/");
		final MenuItem item1 = new MenuItem("label1", "/label1", "user");
		item1.addRole("admin");
		final MenuItem item2 = new MenuItem("label2", "/label2", "user");
		item2.addRole("admin");
		final MenuItem item3 = new MenuItem("label3", "/label3", "user");
		item3.addRole("admin");
		final MenuItem item4 = new MenuItem("label4", "/label4", "user");
		item4.addRole("admin");
		final MenuItem item5 = new MenuItem("label5", "/label5", "user");
		item5.addRole("admin");
		final MenuItem item6 = new MenuItem("label6", "/label6", "user");
		item6.addRole("admin");
		final MenuItem item7 = new MenuItem("label7", "/label7", "admin");
		final MenuItem item8 = new MenuItem("label8", "/label8", "admin");
		final MenuItem item9 = new MenuItem("label9", "/label9", "admin");
		final MenuItem item10 = new MenuItem("label10", "/label10", "admin");
		final MenuItem item11 = new MenuItem("label11", "/label11", "admin");
		final MenuItem item12 = new MenuItem(
			"label12hhhhhhhhhhhhhhhhhhhhhhhh", "/label12", "admin");
		final MenuItem item13 = new MenuItem("label13", "/label13", "admin");
		final MenuItem item14 = new MenuItem("label14", "/label14", "admin");
		final MenuItem item15 = new MenuItem("label15", "/label15", "admin");
		main.addSubMenu(item1);
		item1.addSubMenu(item2);
		item2.addSubMenu(item10);
		item10.addSubMenu(item11);
		item11.addSubMenu(item12);
		item10.addSubMenu(item13);
		item2.addSubMenu(item14);
		item2.addSubMenu(item15);
		item1.addSubMenu(item3);
		item1.addSubMenu(item6);
		item1.addSubMenu(item8);
		main.addSubMenu(item4);
		item4.addSubMenu(item5);
		main.addSubMenu(item7);
		item7.addSubMenu(item9);
		final MenuItemFormatter outty = new MenuItemFormatter(main);
		System.out.println(outty.getHTML("1", "/label11", "admin"));
		System.out.println(outty.getHTML("2", "/label11", "admin"));
		System.out.println(outty.getHTML("X", "/label11", "admin"));
		System.out.println(main);
	}

	public MenuItemFormatter(final MenuItem item) {
		this.widthArray = new int[10];
		this.getRoles(this.item = item);
		this.generateHTML();
	}

	public String getHTML(
		final String level, final String page, final String role) {

		final String lookUpKey =
			String.valueOf(level) + "|" + page + "|" + role;

		if (MenuItemFormatter.htmlBits.containsKey(lookUpKey)) {
			return (String)MenuItemFormatter.htmlBits.get(lookUpKey);
		}

		return "<!-- cant find " + lookUpKey + "-->";
	}

	private String applyLevelWidths(
		final LinkedList levelTwoList, final String roleName) {

		String HTMLinsert = "";
		this.obligatoryCounter = 0;

		for (int i = 0; i < 10; ++i) {
			this.widthArray[1] = 0;
		}

		final int[] levelwidths = this.getLevelWidths(levelTwoList, roleName);

		for (int j = 0; j < levelwidths.length; ++j) {
			final int width = levelwidths[j];

			if ((width != 0) && (j != 0)) {
				HTMLinsert =
					String.valueOf(HTMLinsert) +
						this.getMenuLevelInitJavascript(j, width);
			}
		}

		return HTMLinsert;
	}

	private void applyToAllBelow(
		final String level, final MenuItem base, final Object roleName,
		final String HTMLinsert) {

		final String lookUpKey =
			String.valueOf(level) + "|" + base.getItemURI() + "|" + roleName;
		MenuItemFormatter.htmlBits.put(lookUpKey, HTMLinsert);
		final LinkedList items = this.removeProhibitedItems(
			base.getSubMenus(), roleName);
		final ListIterator itemListIter = items.listIterator();

		while (itemListIter.hasNext()) {
			final MenuItem branch = (MenuItem)itemListIter.next();
			this.applyToAllBelow(level, branch, roleName, HTMLinsert);
		}
	}

	private void generateHTML() {
		this.generateLevelOne();
		this.generateLevelTwo();
		this.generateLevelX();
	}

	private void generateLevelOne() {
		LinkedList listy = new LinkedList();

		listy = this.item.getSubMenus();
		final Object[] levelOneArray = listy.toArray();

		for (final Object roleName : MenuItemFormatter.roles.keySet()) {
			for (int n = 0; n < levelOneArray.length; ++n) {
				final String selected =
					((MenuItem)levelOneArray[n]).getItemURI();
				final String lookUpKey = "1|" + selected + "|" + roleName;
				String HTMLinsert =
					"<div class=\"cellBG\" STYLE=\"padding-top: 3px; padding-bottom: 3px;\">";

				for (int i = 0; i < levelOneArray.length; ++i) {
					if (((MenuItem)levelOneArray[i]).getAccess(
						).contains(
							roleName
						)) {

						String style = "";

						if (((MenuItem)levelOneArray[i]).getItemURI(
							).equals(
								selected
							)) {

							style = "appL1on";
						}
						else {
							style = "appL1off";
						}

						HTMLinsert =
							String.valueOf(HTMLinsert) + "<A HREF=\"" +
								((MenuItem)levelOneArray[i]).getItemURI() +
									"\" class=\"" + style + "\">" +
										((MenuItem)levelOneArray[i]).
											getItemLabel() + "</A>";
					}
				}

				HTMLinsert = String.valueOf(HTMLinsert) + "</div>";
				MenuItemFormatter.htmlBits.put(lookUpKey, HTMLinsert);
				this.applyToAllBelow(
					"1", (MenuItem)levelOneArray[n], roleName, HTMLinsert);
			}
		}
	}

	private void generateLevelTwo() {
		LinkedList listy = new LinkedList();

		listy = this.item.getSubMenus();
		final Object[] levelOneArray = listy.toArray();

		for (final Object roleName : MenuItemFormatter.roles.keySet()) {
			for (int n = 0; n < levelOneArray.length; ++n) {
				if (((MenuItem)levelOneArray[n]).getAccess(
					).contains(
						roleName
					)) {

					final LinkedList rawLevelTwoList =
						((MenuItem)levelOneArray[n]).getSubMenus();
					final LinkedList levelTwoList = this.removeProhibitedItems(
						rawLevelTwoList, roleName);
					final ListIterator l2IterChunk =
						levelTwoList.listIterator();
					int selected = 0;

					while (l2IterChunk.hasNext()) {
						final MenuItem chunkItem = (MenuItem)l2IterChunk.next();
						++selected;
						final String lookUpKey =
							"2|" + chunkItem.getItemURI() + "|" + roleName;
						final ListIterator l2IterBit =
							levelTwoList.listIterator();
						int current = 0;
						String HTMLinsert = this.getTabsHTMLchunkOne();

						while (l2IterBit.hasNext()) {
							++current;
							String width = "130";
							String image = "b-tab";
							final MenuItem bitItem = (MenuItem)l2IterBit.next();

							if (current == (selected - 1)) {
								image = "f-tab-left";
							}

							if (current == 1) {
								width = "140";
							}

							if (current == levelTwoList.size()) {
								image = "f-tab-right";
							}

							HTMLinsert =
								String.valueOf(HTMLinsert) + "<TD WIDTH=\"" +
									width + "\"><!--" + bitItem.getItemURI() +
										"--></TD><TD WIDTH=\"24\"><IMG SRC=\"images/app/" +
											image +
												".gif\" WIDTH=\"24\" HEIGHT=\"24\" ALT=\"\" BORDER=\"0\"></TD>\n";
						}

						final int width2 = (selected - 1) * 154;
						HTMLinsert =
							String.valueOf(HTMLinsert) +
								this.getTabsHTMLchunkTwo(width2);
						MenuItemFormatter.htmlBits.put(lookUpKey, HTMLinsert);
						this.applyToAllBelow(
							"2", chunkItem, roleName, HTMLinsert);
					}
				}
			}
		}
	}

	private void generateLevelX() {
		LinkedList listy = new LinkedList();

		listy = this.item.getSubMenus();
		final Object[] levelOneArray = listy.toArray();

		for (final Object roleName : MenuItemFormatter.roles.keySet()) {
			for (int n = 0; n < levelOneArray.length; ++n) {
				if (((MenuItem)levelOneArray[n]).getAccess(
					).contains(
						roleName
					)) {

					final LinkedList rawLevelTwoList =
						((MenuItem)levelOneArray[n]).getSubMenus();
					final LinkedList levelTwoList = this.removeProhibitedItems(
						rawLevelTwoList, roleName);
					final String lookUpKey =
						"X|" + ((MenuItem)levelOneArray[n]).getItemURI() + "|" +
							roleName;
					final String HTMLinsert = this.genLevelXJavaScript(
						levelTwoList, String.valueOf(roleName));
					MenuItemFormatter.htmlBits.put(lookUpKey, HTMLinsert);
					this.applyToAllBelow(
						"X", (MenuItem)levelOneArray[n], roleName, HTMLinsert);
				}
			}
		}
	}

	private String genLevelXJavaScript(
		final LinkedList levelTwoList, final String roleName) {

		String HTMLinsert = this.getMenuPrelimJavascript();

		HTMLinsert =
			String.valueOf(HTMLinsert) +
				this.applyLevelWidths(levelTwoList, roleName);
		final ListIterator menuListIterator = levelTwoList.listIterator();
		int counter = 0;

		while (menuListIterator.hasNext()) {
			final MenuItem targetmenuItem = (MenuItem)menuListIterator.next();
			HTMLinsert =
				String.valueOf(HTMLinsert) +
					this.levelXdrillDownSubs(
						targetmenuItem, roleName, counter, "");
			++counter;
		}

		HTMLinsert =
			String.valueOf(HTMLinsert) +
				"oCMenu.makeStyle(); oCMenu.construct()\n</script>\n";

		return HTMLinsert;
	}

	private int[] getLevelWidths(
		final LinkedList levelList, final String roleName) {

		++this.obligatoryCounter;
		final int minimumSizeForFirst = 153;
		final int letterSpacingAllowance = 3;
		final String thirteen = "W";
		final String nine = "AMRVmw";
		final String eight = "GKOQ";
		final String seven = "BCDEFHNPSTUXYZ";
		final String six = "L234567890";
		final String five = "Jabcdeghknopqsuvxyz";
		final String one = "Iil";
		final LinkedList items = this.removeProhibitedItems(
			levelList, roleName);
		final ListIterator itemsIterator = items.listIterator();

		while (itemsIterator.hasNext()) {
			final MenuItem subItem = (MenuItem)itemsIterator.next();
			int proposedWidth = 0;

			for (int counter = 0;
				 counter < subItem.getItemLabel(
				 ).length(); ++counter) {

				final String letter = subItem.getItemLabel(
				).substring(
					counter, counter + 1
				);
				proposedWidth += (thirteen.indexOf(letter) != -1) ? 13 :
					((nine.indexOf(letter) != -1) ? 9 :
						((eight.indexOf(letter) != -1) ? 8 :
							((seven.indexOf(letter) != -1) ? 7 :
								((six.indexOf(letter) != -1) ? 6 :
									((five.indexOf(letter) != -1) ? 5 :
										((one.indexOf(letter) != -1) ? 1 :
											3))))));
				proposedWidth += letterSpacingAllowance;
			}

			this.widthArray[this.obligatoryCounter - 1] =
				(this.widthArray[this.obligatoryCounter - 1] > proposedWidth) ?
					this.widthArray[this.obligatoryCounter - 1] : proposedWidth;
			this.getLevelWidths(subItem.getSubMenus(), roleName);
		}

		--this.obligatoryCounter;
		this.widthArray[1] =
			(minimumSizeForFirst < this.widthArray[1]) ? this.widthArray[1] :
				minimumSizeForFirst;

		return this.widthArray;
	}

	private String getmenuItemJavascript(
		final String name, final String parent, final String label,
		final String URI) {

		return "oCMenu.makeMenu('" + name + "','" + parent + "','" + label +
			"','" + URI + "','')\n";
	}

	private String getMenuLevelInitJavascript(
		final int level, final int width) {

		return "oCMenu.level[" + level + "]=new Array()\n" + "oCMenu.level[" +
			level + "].width=" + width + "\noCMenu.level[" + level +
				"].height=23\n" + "oCMenu.level[" + level +
					"].bgcoloroff=oCMenulevel1bgcoloroff\n" + "oCMenu.level[" +
						level + "].bgcoloron=oCMenulevel1bgcoloron\n" +
							"oCMenu.level[" + level +
								"].textcolor=oCMenulevel1textcolor\n" +
									"oCMenu.level[" + level +
										"].hovercolor=oCMenulevel1hovercolor\n" +
											"oCMenu.level[" + level +
												"].style=\"padding-top: 3px; padding-left: 5px; font-family:Arial, Helvetica, sans-serif; font-size:13px;\"\n" +
													"oCMenu.level[" + level +
														"].align=\"right\"\n" +
															"oCMenu.level[" +
																level +
																	"].offsetX=0\n" +
																		"oCMenu.level[" +
																			level +
																				"].offsetY=0\n" +
																					"oCMenu.level[" +
																						level +
																							"].border=1\n" +
																								"oCMenu.level[" +
																									level +
																										"].bordercolor=oCMenulevel1bordercolor\n" +
																											"oCMenu.level[" +
																												level +
																													"].NS4font=\"Verdana\"\n" +
																														"oCMenu.level[" +
																															level +
																																"].NS4fontSize=\"2\"\n" +
																																	"oCMenu.level[" +
																																		level +
																																			"].NS4fontColor=oCMenulevel1NS4fontColor\n\n";
	}

	private String getMenuPrelimJavascript() {
		return "<script language=\"JavaScript1.2\" SRC=\"menu/coolmenus3.js\"></script>\n<SCRIPT LANGUAGE=\"JavaScript1.2\">\nfunction lib_bwcheck(){ //Browsercheck (needed)\nthis.ver=navigator.appVersion; this.agent=navigator.userAgent\nthis.dom=document.getElementById?1:0\nthis.ie5=(this.ver.indexOf(\"MSIE 5\")>-1 && this.dom)?1:0;\nthis.ie6=(this.ver.indexOf(\"MSIE 6\")>-1 && this.dom)?1:0;\nthis.ie4=(document.all && !this.dom)?1:0;\nthis.ie=this.ie4||this.ie5||this.ie6\nthis.mac=this.agent.indexOf(\"Mac\")>-1\nthis.opera5=this.agent.indexOf(\"Opera 5\")>-1\nthis.ns6=(this.dom && parseInt(this.ver) >= 5) ?1:0;\nthis.ns4=(document.layers && !this.dom)?1:0;\nthis.bw=(this.ie6 || this.ie5 || this.ie4 || this.ns4 || this.ns6 || this.opera5 || this.dom)\nreturn this\n}\nvar bw=new lib_bwcheck()\nvar mDebugging=2\n\noCMenu=new makeCoolMenu(\"oCMenu\")\noCMenu.useframes=0\noCMenu.frame=\"\"\noCMenu.useNS4links=1\noCMenu.NS4padding=2\noCMenu.checkselect=1\noCMenu.offlineUrl=\"file:///C:/WINNT/Profiles/jwhitfor/Desktop/eTreasury/html/scripts/\"\noCMenu.onlineUrl=\"http://interact.cu.net.au/coolmenus3/\"\n\noCMenu.pagecheck=0\noCMenu.checkscroll=0\noCMenu.resizecheck=1\noCMenu.wait=100\n\noCMenu.usebar=0\noCMenu.barcolor=oCMenubarcolor\noCMenu.barwidth=\"menu\"\noCMenu.barheight=\"menu\"\noCMenu.barx=\"menu\"\noCMenu.bary=\"menu\"\noCMenu.barinheritborder=1\n\noCMenu.rows=1\noCMenu.fromleft=1\noCMenu.fromtop=145\noCMenu.pxbetween=1\noCMenu.menuplacement=0\noCMenu.NS4hover=1\n\noCMenu.level[0]=new Array()\noCMenu.level[0].width=153\noCMenu.level[0].height=18\noCMenu.level[0].bgcoloroff=\"transparent\"\noCMenu.level[0].bgcoloron=\"transparent\"\noCMenu.level[0].textcolor=oCMenulevel0textcolor\noCMenu.level[0].hovercolor=oCMenulevel0hovercolor\noCMenu.level[0].style=\"text-align: center; font-family:Arial, Helvetica, sans-serif; font-size:13px;\"\noCMenu.level[0].border=0\noCMenu.level[0].bordercolor=\"\"\noCMenu.level[0].offsetX=-1\noCMenu.level[0].offsetY=1\noCMenu.level[0].NS4font=\"arial,helvetica\"\noCMenu.level[0].NS4fontSize=\"2\"\noCMenu.level[0].NS4fontColor=oCMenulevel0NS4fontColor\noCMenu.level[0].align=\"bottom\"\n\n//dynamic effect (controllable for each level)\noCMenu.level[0].clip=0\noCMenu.level[0].clippx=2\noCMenu.level[0].cliptim=1\n\n//special animation filters (IE5.5+ only, controllable for each level).\noCMenu.level[0].filter=0\n\n";
	}

	private void getRoles(final MenuItem item) {
		final ListIterator roleIter = item.getAccess(
		).listIterator();

		while (roleIter.hasNext()) {
			final String roley = String.valueOf(roleIter.next());
			MenuItemFormatter.roles.put(roley, roley);
		}

		final ListIterator iter = item.getSubMenus(
		).listIterator();

		while (iter.hasNext()) {
			final MenuItem currentItem = (MenuItem)iter.next();
			this.getRoles(currentItem);
		}
	}

	private String getTabsHTMLchunkOne() {
		return "<TABLE BGCOLOR=\"#ffffff\" BORDER=\"0\" CELLPADDING=\"0\" CELLSPACING=\"0\" WIDTH=\"100%\">\n\t<TR><TD HEIGHT=\"1\"><IMG SRC=\"images/spacer.gif\" WIDTH=\"1\" HEIGHT=\"1\" BORDER=\"0\" ALT=\"\"></TD></TR>\n\t<TR CLASS=\"cellBGAlt\">\n\t\t<TD>\n<!-- begin tabs. selected tab has the f-tab-right.gif image and the tab before the selected tab has the f-tab-left.gif image -->\n<TABLE BGCOLOR=\"#99ccff\" BORDER=\"0\" CELLPADDING=\"0\" CELLSPACING=\"0\" CLASS=\"cellBGAlt\"><TR>\n";
	}

	private String getTabsHTMLchunkTwo(final int width) {
		return "</TR></TABLE>\n<!-- end tabs -->\n\t\t</TD>\n\t</TR>\n\t<TR>\n\t\t<TD>\n\t\t\t<TABLE BGCOLOR=\"#99ccff\" BORDER=\"0\" CELLPADDING=\"0\" CELLSPACING=\"0\" CLASS=\"cellBGAlt\">\n\t\t\t\t<TR BGCOLOR=\"#FFFFFF\">\n<!-- begin thin blue line tab position. Add 154 each tab (154,308,462,616,etc-->\n\t\t\t\t\t<TD WIDTH=\"" +
			width +
				"\" HEIGHT=\"1\"><IMG SRC=\"images/spacer.gif\" WIDTH=\"1\" HEIGHT=\"1\" BORDER=\"0\" ALT=\"\"></TD>\n" +
					"<!-- end thin blue line tab position -->\n" +
						"\t\t\t\t\t<TD WIDTH=\"164\" CLASS=\"cellBGAlt\" HEIGHT=\"1\"><IMG SRC=\"images/spacer.gif\" WIDTH=\"1\" HEIGHT=\"1\" BORDER=\"0\" ALT=\"\"></TD>\n" +
							"\t\t\t\t</TR>\n" + "\t\t\t</TABLE>\n" +
								"\t\t</TD>\n" + "\t</TR>\n" +
									"\t<TR CLASS=\"cellBGAlt\" BGCOLOR=\"#99ccff\">\n" +
										"\t\t<TD BGCOLOR=\"#99ccff\" CLASS=\"cellBGAlt\" HEIGHT=\"2\"><IMG SRC=\"images/spacer.gif\" WIDTH=\"1\" HEIGHT=\"2\" BORDER=\"0\" ALT=\"\"></TD>\n" +
											"\t</TR>\n" + "</TABLE>\n";
	}

	private String levelXdrillDownSubs(
		final MenuItem targetmenuItem, final String roleName, int counter,
		final String parent) {

		final String name = String.valueOf(parent) + "sub" + counter;
		String HTMLinsert = this.getmenuItemJavascript(
			name, parent, targetmenuItem.getItemLabel(),
			targetmenuItem.getItemURI());
		final LinkedList items = this.removeProhibitedItems(
			targetmenuItem.getSubMenus(), roleName);
		final ListIterator currentIter = items.listIterator();

		while (currentIter.hasNext()) {
			++counter;
			final MenuItem subItem = (MenuItem)currentIter.next();
			HTMLinsert =
				String.valueOf(HTMLinsert) +
					this.levelXdrillDownSubs(subItem, roleName, counter, name);
		}

		return HTMLinsert;
	}

	private LinkedList removeProhibitedItems(
		final LinkedList origList, final Object roleName) {

		final ListIterator cleanUpIter = origList.listIterator();
		final LinkedList levelTwoList = new LinkedList();

		while (cleanUpIter.hasNext()) {
			final MenuItem itemInQuestion = (MenuItem)cleanUpIter.next();

			if (itemInQuestion.getAccess(
				).contains(
					roleName
				)) {

				levelTwoList.add(itemInQuestion);
			}
		}

		return levelTwoList;
	}

	private static HashMap htmlBits;
	private static HashMap roles;

	static {
		MenuItemFormatter.htmlBits = new HashMap();
		MenuItemFormatter.roles = new HashMap();
	}

	private MenuItem item;
	private int obligatoryCounter;
	private int[] widthArray;

}