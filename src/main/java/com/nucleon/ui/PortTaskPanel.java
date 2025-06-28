/*
 * Copyright (c) 2025, nucleon <https://github.com/nucleon>
 * Copyright (c) 2025, Cooper Morris <https://github.com/coopermor>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.nucleon.ui;

import com.nucleon.ui.adapters.HidePortTaskSlotOverlay;
import com.nucleon.ui.adapters.PortTaskSlotOverlayColorMouseAdapter;
import com.nucleon.PortTask;
import com.nucleon.SailingHelperPlugin;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.components.colorpicker.RuneliteColorPicker;
import net.runelite.client.util.ImageUtil;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
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
			INVISIBLE_ICON,
			ANCHOR,
			BOAT,
			DESTINATION,
			NOTICE,
			PACKAGE;

	public final JLabel
			PortTaskOverlayColor = new JLabel(),
			hidePortTaskSlotOverlay = new JLabel(),
			cargoRemainingText = new JLabel(),
			cargoLabel = new JLabel(),
			destinationLabel = new JLabel(),
			noticeLabel = new JLabel(),
			anchorLabel = new JLabel(),
			boatLabel = new JLabel(),
			taskName = new JLabel();

	private final JLabel
			save   = new JLabel("Save"),
			cancel = new JLabel("Cancel"),
			rename = new JLabel("Rename");

	public final JPanel PortTaskSlotContainer = new JPanel(new BorderLayout());
	private final PortTask portTask;

	static
	{
		final BufferedImage borderImg = ImageUtil.loadImageResource(SailingHelperPlugin.class, "border_color_icon.png");
		BORDER_COLOR_ICON = new ImageIcon(ImageUtil.alphaOffset(borderImg, -100));

		final BufferedImage visibleImg = ImageUtil.loadImageResource(SailingHelperPlugin.class, "visible_icon.png");
		VISIBLE_ICON = new ImageIcon(visibleImg);

		final BufferedImage invisibleImg = ImageUtil.loadImageResource(SailingHelperPlugin.class, "invisible_icon.png");
		INVISIBLE_ICON = new ImageIcon(invisibleImg);

		final BufferedImage boatImg = ImageUtil.loadImageResource(SailingHelperPlugin.class, "boat.png");
		BOAT = new ImageIcon(boatImg);

		final BufferedImage anchorImg = ImageUtil.loadImageResource(SailingHelperPlugin.class, "anchor.png");
		ANCHOR = new ImageIcon(anchorImg);

		final BufferedImage destImg = ImageUtil.loadImageResource(SailingHelperPlugin.class, "destination.png");
		DESTINATION = new ImageIcon(ImageUtil.alphaOffset(destImg, -100));

		final BufferedImage noticeImg = ImageUtil.loadImageResource(SailingHelperPlugin.class, "notice.png");
		NOTICE = new ImageIcon(ImageUtil.alphaOffset(noticeImg, -100));

		final BufferedImage cargoImg = ImageUtil.loadImageResource(SailingHelperPlugin.class, "package.png");
		PACKAGE = new ImageIcon(ImageUtil.alphaOffset(cargoImg, -100));
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

		JPanel noticeWrapper = new JPanel(new BorderLayout());
		noticeWrapper.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		JPanel cargoWrapper = new JPanel(new BorderLayout());
		cargoWrapper.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		JPanel destinationWrapper = new JPanel(new BorderLayout());
		destinationWrapper.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		JPanel hideOverlay = new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 3));
		hideOverlay.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		PortTaskSlotContainer.setBorder(new EmptyBorder(0, 0, 0, 0));
		PortTaskSlotContainer.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		JPanel PortTaskActionsLeftSide = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		PortTaskActionsLeftSide.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		JPanel PortTaskInformationCenter = new JPanel();
		PortTaskInformationCenter.setBackground(ColorScheme.DARKER_GRAY_COLOR);


		PortTaskOverlayColor.setToolTipText("edit portTask color");
		PortTaskOverlayColor.setForeground(portTask.getOverlayColor() == null ? Color.red : portTask.getOverlayColor());
		PortTaskOverlayColor.addMouseListener(new PortTaskSlotOverlayColorMouseAdapter(PortTaskOverlayColor, this));
		PortTaskActionsLeftSide.add(PortTaskOverlayColor);

		int taken = portTask.getCargoTaken();
		int required = portTask.getData().getCargoAmount();

		cargoRemainingText.setText("Cargo: " + taken + "/" + required);
		cargoRemainingText.setToolTipText("Remaining cargo");

		if (taken < required)
		{
			cargoRemainingText.setForeground(Color.RED);
		}
		else
		{
			cargoRemainingText.setForeground(UIManager.getColor("Label.foreground"));
		}
		PortTaskInformationCenter.add(cargoRemainingText);

		hidePortTaskSlotOverlay.setToolTipText((portTask.isTracking() ? "Hide" : "Show") + " portTask");
		hidePortTaskSlotOverlay.addMouseListener(new HidePortTaskSlotOverlay(hidePortTaskSlotOverlay, portTask, this, plugin));

		hideOverlay.add(hidePortTaskSlotOverlay);
		taskName.setText(portTask.getData().taskName);
		taskName.setHorizontalAlignment(SwingConstants.CENTER);
		taskName.setFont(taskName.getFont().deriveFont(18f)); // 18f = new font size

		nameWrapper.add(taskName, BorderLayout.CENTER);
		nameWrapper.add(hideOverlay, BorderLayout.EAST);
		nameWrapper.add(anchorLabel, BorderLayout.WEST);

		JPanel portSlotWrapper = new JPanel();
		portSlotWrapper.setLayout(new BoxLayout(portSlotWrapper, BoxLayout.Y_AXIS));
		portSlotWrapper.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		portSlotWrapper.add(nameWrapper);
		portSlotWrapper.add(PortTaskSlotContainer);

		noticeWrapper.add(noticeLabel, BorderLayout.WEST);
		noticeWrapper.setBorder(NAME_BOTTOM_BORDER);
		cargoWrapper.add(cargoLabel, BorderLayout.WEST);
		cargoWrapper.setBorder(NAME_BOTTOM_BORDER);
		destinationWrapper.add(destinationLabel, BorderLayout.WEST);
		destinationWrapper.setBorder(NAME_BOTTOM_BORDER);

		portSlotWrapper.add(noticeWrapper);
		portSlotWrapper.add(cargoWrapper);
		portSlotWrapper.add(destinationWrapper);

		PortTaskSlotContainer.setLayout(new BorderLayout());
		PortTaskSlotContainer.add(PortTaskActionsLeftSide, BorderLayout.WEST);
		PortTaskSlotContainer.add(PortTaskInformationCenter, BorderLayout.CENTER);
		PortTaskSlotContainer.setBorder(NAME_BOTTOM_BORDER);

		add(portSlotWrapper);

		updateVisibility();
		updateColorIndicators();
		updateImages(portTask);

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

	private void updateImages(PortTask portTask)
	{
		cargoLabel.setIcon(PACKAGE);
		destinationLabel.setIcon(DESTINATION);
		noticeLabel.setIcon(NOTICE);
		anchorLabel.setIcon(ANCHOR);
		boatLabel.setIcon(BOAT);

		cargoLabel.setText(portTask.getData().getCargoLocation().getName());
		destinationLabel.setText(portTask.getData().getDeliveryLocation().getName());
		noticeLabel.setText(portTask.getData().getNoticeBoard().getName());
	}



}
