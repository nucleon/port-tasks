package com.nucleon.ui;


import com.nucleon.ui.adapters.HidePortTaskSlotOverlay;
import com.nucleon.ui.adapters.PortTaskSlotOverlayColorMouseAdapter;
import com.nucleon.PortTask;
import com.nucleon.SailingHelperPlugin;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.components.FlatTextField;
import net.runelite.client.ui.components.colorpicker.RuneliteColorPicker;
import net.runelite.client.util.ImageUtil;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;

public class PortTaskPanel extends JPanel
{
	public final SailingHelperPlugin plugin;

	private static final Border NAME_BOTTOM_BORDER = new CompoundBorder(
			BorderFactory.createMatteBorder(0, 0, 1, 0, ColorScheme.DARK_GRAY_COLOR),
			BorderFactory.createLineBorder(ColorScheme.DARKER_GRAY_COLOR));

	private static final ImageIcon
			BORDER_COLOR_ICON,
			VISIBLE_ICON,
			INVISIBLE_ICON;

	public final JLabel
			PortTaskOverlayColor = new JLabel(),
			hidePortTaskSlotOverlay = new JLabel();

	private final JLabel
			save   = new JLabel("Save"),
			cancel = new JLabel("Cancel"),
			rename = new JLabel("Rename");

	public final JPanel PortTaskSlotContainer = new JPanel(new BorderLayout());
	private final FlatTextField nameInput = new FlatTextField();
	private final PortTask portTask;

	static
	{
		final BufferedImage borderImg = ImageUtil.loadImageResource(SailingHelperPlugin.class, "border_color_icon.png");
		BORDER_COLOR_ICON = new ImageIcon(borderImg);

		final BufferedImage visibleImg = ImageUtil.loadImageResource(SailingHelperPlugin.class, "visible_icon.png");
		VISIBLE_ICON = new ImageIcon(visibleImg);

		final BufferedImage invisibleImg = ImageUtil.loadImageResource(SailingHelperPlugin.class, "invisible_icon.png");
		INVISIBLE_ICON = new ImageIcon(invisibleImg);
	}

	public PortTaskPanel(SailingHelperPlugin plugin, PortTask portTask, int slot)
	{
		this.plugin = plugin;
		this.portTask = portTask;

		setLayout(new BorderLayout());
		setBackground(ColorScheme.DARKER_GRAY_COLOR);
		setBorder(new EmptyBorder(0, 0, 0, 0));

		JPanel nameWrapper = new JPanel(new BorderLayout());
		nameWrapper.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		nameWrapper.setBorder(NAME_BOTTOM_BORDER);

		JPanel nameActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 3));
		nameActions.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		PortTaskSlotContainer.setBorder(new EmptyBorder(5, 0, 5, 0));
		PortTaskSlotContainer.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		JPanel leftActionsPrayer = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		leftActionsPrayer.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		JPanel rightActionsPrayer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		rightActionsPrayer.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		PortTaskOverlayColor.setToolTipText("edit portTask color");
		PortTaskOverlayColor.setForeground(portTask.getOverlayColor() == null ? Color.red : portTask.getOverlayColor());
		PortTaskOverlayColor.addMouseListener(new PortTaskSlotOverlayColorMouseAdapter(PortTaskOverlayColor, this));
		leftActionsPrayer.add(PortTaskOverlayColor);


		hidePortTaskSlotOverlay.setToolTipText((portTask.isTracking() ? "Hide" : "Show") + " portTask");
		hidePortTaskSlotOverlay.addMouseListener(new HidePortTaskSlotOverlay(hidePortTaskSlotOverlay, portTask, this, plugin));

		nameActions.add(hidePortTaskSlotOverlay);
		nameInput.setText(portTask.getData().taskName);
		nameWrapper.add(nameInput, BorderLayout.CENTER);
		nameWrapper.add(nameActions, BorderLayout.EAST);

		JPanel portSlotWrapper = new JPanel();
		portSlotWrapper.setLayout(new BoxLayout(portSlotWrapper, BoxLayout.Y_AXIS));
		portSlotWrapper.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		portSlotWrapper.add(nameWrapper);
		portSlotWrapper.add(PortTaskSlotContainer);

		PortTaskSlotContainer.setLayout(new BorderLayout());
		PortTaskSlotContainer.add(leftActionsPrayer, BorderLayout.WEST);
		PortTaskSlotContainer.add(rightActionsPrayer, BorderLayout.EAST);

		add(portSlotWrapper);

		updateVisibility();
		updateColorIndicators();
	}

	public void openPortTaskColorPicker()
	{
		Color color = portTask.getOverlayColor() == null ? Color.red : portTask.getOverlayColor();
		RuneliteColorPicker colourPicker = getColorPicker(color);
		colourPicker.setOnColorChange(c ->
		{
			portTask.setOverlayColor(c);
			PortTaskOverlayColor.setBorder(new MatteBorder(0, 0, 3, 0, portTask.getOverlayColor()));
			PortTaskOverlayColor.setIcon(BORDER_COLOR_ICON);
			updateColorIndicators();
		});
		colourPicker.setVisible(true);
	}

	private RuneliteColorPicker getColorPicker(Color color)
	{
		RuneliteColorPicker colorPicker = plugin.getColorPickerManager().create(
				SwingUtilities.windowForComponent(this),
				color,
				portTask.getData().taskName + " - overlay color",
				false);
		colorPicker.setLocationRelativeTo(this);
		colorPicker.setOnClose(c -> plugin.saveSlotSettings());
		return colorPicker;
	}


	private void updateColorIndicators()
	{
		PortTaskOverlayColor.setBorder(new MatteBorder(0, 0, 3, 0, portTask.getOverlayColor()));
		PortTaskOverlayColor.setIcon(BORDER_COLOR_ICON);
	}

	public void updateVisibility()
	{
		hidePortTaskSlotOverlay.setIcon(portTask.isTracking() ? VISIBLE_ICON : INVISIBLE_ICON);
	}


}
