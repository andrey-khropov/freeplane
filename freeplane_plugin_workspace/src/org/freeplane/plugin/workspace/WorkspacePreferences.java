package org.freeplane.plugin.workspace;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;

import org.freeplane.core.resources.ResourceBundles;
import org.freeplane.core.resources.ResourceController;
import org.freeplane.core.ui.AFreeplaneAction;
import org.freeplane.core.ui.IMenuContributor;
import org.freeplane.core.ui.MenuBuilder;
import org.freeplane.core.util.LogUtils;
import org.freeplane.features.mode.Controller;
import org.freeplane.features.mode.ModeController;
import org.freeplane.features.mode.mindmapmode.MModeController;
import org.freeplane.plugin.workspace.imageviewer.ImageViewer;

public class WorkspacePreferences {

	public static final String VIEW_ACTION = "viewaction";
	public static final String MENU_BAR = "/menu_bar";
	public static final String VIEW_MENU = "/view";

	public static final String SHOW_WORKSPACE_TEXT = "show_workspace";
	public static final String SHOW_WORKSPACE_RESOURCE = "show_workspace";
	public static final String WORKSPACE_WIDTH_PROPERTY_KEY = "workspace_view_width";
	public static final String WORKSPACE_LOCATION = "workspace_location";

	private ModeController modeController;

	public WorkspacePreferences() {
		LogUtils.info("WorkspacePreferences");
		this.modeController = Controller.getCurrentModeController();
		addLanguageResources();
		addMenuEntries();
		addDefaultPreferences();
		addPreferencesToOptionsPanel();
	}
	
	private void addDefaultPreferences() {
		final URL defaults = this.getClass().getResource(ResourceController.PLUGIN_DEFAULTS_RESOURCE);
		if (defaults == null)
			throw new RuntimeException("cannot open " + ResourceController.PLUGIN_DEFAULTS_RESOURCE);
		Controller.getCurrentController().getResourceController().addDefaults(defaults);
	}

	private void addLanguageResources() {
		ResourceBundles resBundle = ((ResourceBundles) modeController.getController().getResourceController().getResources());

		String lang = resBundle.getLanguageCode();
		if (lang == null || lang.equals(ResourceBundles.LANGUAGE_AUTOMATIC)) {
			lang = "en";
		}

		final URL res = this.getClass().getResource("/translations/Resources_" + lang + ".properties");
		resBundle.addResources(resBundle.getLanguageCode(), res);
	}
	
	private void addPreferencesToOptionsPanel() {
		final URL preferences = this.getClass().getResource("preferences.xml");
		if (preferences == null)
			throw new RuntimeException("cannot open preferences");
		MModeController modeController = (MModeController) Controller.getCurrentModeController();
		
		modeController.getOptionPanelBuilder().load(preferences);
	}

	private void addMenuEntries() {

		this.modeController.addMenuContributor(new IMenuContributor() {
			ResourceController resourceController = Controller.getCurrentController().getResourceController();

			public void updateMenus(ModeController modeController, MenuBuilder builder) {
				builder.addCheckboxItem(MENU_BAR + VIEW_MENU, new CheckBoxAction(SHOW_WORKSPACE_TEXT, SHOW_WORKSPACE_RESOURCE),
						resourceController.getBooleanProperty(SHOW_WORKSPACE_RESOURCE));
				builder.addAction(MENU_BAR + VIEW_MENU, new ButtonAction("BLUBB"), MenuBuilder.AS_CHILD);

			}
		});
	}
	
	
	
	
	private class CheckBoxAction extends AFreeplaneAction {

		private static final long serialVersionUID = 1256514415353330887L;
		private String propertyKey;

		public CheckBoxAction(String key, String propertyKey) {
			super(key);
			this.propertyKey = propertyKey;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			boolean checked = ((JCheckBoxMenuItem) e.getSource()).isSelected();
			Controller.getCurrentController().getResourceController().setProperty(this.propertyKey, checked);

			if (checked) {
				WorkspaceEnvironment.getCurrentWorkspaceEnvironment().showWorkspaceView(true);
			}
			else {
				WorkspaceEnvironment.getCurrentWorkspaceEnvironment().showWorkspaceView(false);
			}
		}
	}

	private class ButtonAction extends AFreeplaneAction {		
		private static final long serialVersionUID = 1L;

		public ButtonAction(String key) {
			super(key);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("DEBUG ACTION");

			Image image = Toolkit.getDefaultToolkit().getImage("/home/stefan/stefan.jpg");

			final JFrame f = new ImageViewer(image, true, "PICTURE");
			//f.setMinimumSize(new Dimension(500, 300));
			f.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					f.dispose();
				}
			});
			f.pack();
			f.setVisible(true);

			// Controller.getCurrentController().getViewController().getContentPane().add(imageViewer);
			// imageViewer.setVisible(true);
			// ImageViewer
		}
	}
}


