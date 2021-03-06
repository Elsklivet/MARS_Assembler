/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/. */
// adapted for MARS by Jarrett Billingsley

package mars.venus;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import mars.Globals;
import mars.Settings;

// import com.cburch.logisim.prefs.AppPreferences;

class OpenRecent extends JMenu { // implements PropertyChangeListener {
	private static final int MAX_ITEM_LENGTH = 50;

	private class RecentItem extends JMenuItem implements ActionListener {
		private File file;

		RecentItem(File file) {
			super(getFileText(file));
			this.file = file;
			setEnabled(file != null);
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent event) {
			Globals.getGui().editor.open(file);
		}
	}

	// private LogisimMenuBar menubar;
	private List<RecentItem> recentItems;

	OpenRecent(/*LogisimMenuBar menubar*/) {
		// this.menubar = menubar;
		this.recentItems = new ArrayList<RecentItem>();
		// AppPreferences.addPropertyChangeListener(AppPreferences.RECENT_PROJECTS, this);
		renewItems();
		localeChanged();
	}

	public void renewItems() {
		for (int index = recentItems.size() - 1; index >= 0; index--) {
			RecentItem item = recentItems.get(index);
			remove(item);
		}
		recentItems.clear();

		List<File> files = Globals.getSettings().getRecentFiles();
		if (files.isEmpty()) {
			recentItems.add(new RecentItem(null));
		} else {
			for (File file : files) {
				recentItems.add(new RecentItem(file));
			}
		}

		for (RecentItem item : recentItems) {
			add(item);
		}
	}

	private static String getFileText(File file) {
		if (file == null) {
			return "(None)";
		} else {
			String ret;
			try {
				ret = file.getCanonicalPath();
			} catch (IOException e) {
				ret = file.toString();
			}
			if (ret.length() <= MAX_ITEM_LENGTH) {
				return ret;
			} else {
				ret = ret.substring(ret.length() - MAX_ITEM_LENGTH + 3);
				int splitLoc = ret.indexOf(File.separatorChar);
				if (splitLoc >= 0) {
					ret = ret.substring(splitLoc);
				}
				return "..." + ret;
			}
		}
	}

	void localeChanged() {
		setText("Open Recent");
		for (RecentItem item : recentItems) {
			if (item.file == null) {
				item.setText("(None)");
			}
		}
	}

	// public void propertyChange(PropertyChangeEvent event) {
	// 	if (event.getPropertyName().equals(AppPreferences.RECENT_PROJECTS)) {
	// 		renewItems();
	// 	}
	// }
}
