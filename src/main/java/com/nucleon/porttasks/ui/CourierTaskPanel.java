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
package com.nucleon.porttasks.ui;

import com.nucleon.porttasks.CourierTask;
import com.nucleon.porttasks.ui.adapters.HidePortTaskSlotOverlay;
import com.nucleon.porttasks.ui.adapters.PortTaskSlotOverlayColor;
import com.nucleon.porttasks.PortTasksPlugin;

import net.runelite.api.ItemComposition;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.components.colorpicker.RuneliteColorPicker;
import net.runelite.client.util.ImageUtil;
import net.runelite.http.api.chat.Task;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;

public class CourierTaskPanel extends JPanel implements TaskPanel
{
	public final PortTasksPlugin plugin;
	private final ClientThread clientThread;
	private final ItemManager itemManager;

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
	private final CourierTask courierTask;

	static
	{
		final BufferedImage borderImg = ImageUtil.loadImageResource(PortTasksPlugin.class, "border_color_icon.png");
		BORDER_COLOR_ICON = new ImageIcon(ImageUtil.alphaOffset(borderImg, -100));

		final BufferedImage visibleImg = ImageUtil.loadImageResource(PortTasksPlugin.class, "visible_icon.png");
		VISIBLE_ICON = new ImageIcon(visibleImg);

		final BufferedImage invisibleImg = ImageUtil.loadImageResource(PortTasksPlugin.class, "invisible_icon.png");
		INVISIBLE_ICON = new ImageIcon(invisibleImg);

		final BufferedImage boatImg = ImageUtil.loadImageResource(PortTasksPlugin.class, "boat.png");
		BOAT = new ImageIcon(boatImg);

		final BufferedImage anchorImg = ImageUtil.loadImageResource(PortTasksPlugin.class, "anchor.png");
		ANCHOR = new ImageIcon(anchorImg);

		final BufferedImage destImg = ImageUtil.loadImageResource(PortTasksPlugin.class, "destination.png");
		DESTINATION = new ImageIcon(ImageUtil.alphaOffset(destImg, -100));

		final BufferedImage noticeImg = ImageUtil.loadImageResource(PortTasksPlugin.class, "notice.png");
		NOTICE = new ImageIcon(ImageUtil.alphaOffset(noticeImg, -100));

		final BufferedImage cargoImg = ImageUtil.loadImageResource(PortTasksPlugin.class, "package.png");
		PACKAGE = new ImageIcon(ImageUtil.alphaOffset(cargoImg, -100));
	}

	public CourierTaskPanel(PortTasksPlugin plugin, CourierTask courierTask, ClientThread clientThread, ItemManager itemManager, int slot)
	{
		this.plugin = plugin;
		this.courierTask = courierTask;
		this.clientThread = clientThread;
		this.itemManager = itemManager;
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


		PortTaskOverlayColor.setToolTipText("edit courierTask color");
		PortTaskOverlayColor.setForeground(courierTask.getOverlayColor() == null ? Color.red : courierTask.getOverlayColor());
		PortTaskOverlayColor.addMouseListener(new PortTaskSlotOverlayColor(PortTaskOverlayColor, this));
		PortTaskActionsLeftSide.add(PortTaskOverlayColor);

		int cargoTaken = courierTask.getCargoTaken();
		int delivered = courierTask.getDelivered();
		int required = courierTask.getData().getCargoAmount();

		cargoRemainingText.setText("Cargo: " + cargoTaken + "/" + required);
		cargoRemainingText.setToolTipText("Remaining cargo");

		if (cargoTaken < required)
		{
			cargoRemainingText.setForeground(Color.RED);
		}

		if (cargoTaken == required)
		{
			cargoRemainingText.setText("Delivered: " + delivered + "/" + required);
		}
		if (delivered == required)
		{
			cargoRemainingText.setText("Claim Rewards!");
		}

		PortTaskInformationCenter.add(cargoRemainingText);

		hidePortTaskSlotOverlay.setToolTipText((courierTask.isTracking() ? "Hide" : "Show") + " courierTask");
		hidePortTaskSlotOverlay.addMouseListener(new HidePortTaskSlotOverlay(hidePortTaskSlotOverlay, courierTask, this, plugin));

		hideOverlay.add(hidePortTaskSlotOverlay);
		taskName.setText(courierTask.getData().taskName);
		taskName.setHorizontalAlignment(SwingConstants.CENTER);


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
		updateImages(courierTask);

	}

	public void openPortTaskColorPicker()
	{
		Color color = courierTask.getOverlayColor() == null ? Color.red : courierTask.getOverlayColor();
		RuneliteColorPicker colourPicker = getColorPicker(color);
		colourPicker.setOnColorChange(c ->
		{
			courierTask.setOverlayColor(c);
			PortTaskOverlayColor.setBorder(new MatteBorder(0, 0, 3, 0, courierTask.getOverlayColor()));
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
				courierTask.getData().taskName + " - overlay color",
				false);
		colorPicker.setLocationRelativeTo(this);
		colorPicker.setOnClose(c -> plugin.saveSlotSettings());
		return colorPicker;
	}


	private void updateColorIndicators()
	{
		PortTaskOverlayColor.setBorder(new MatteBorder(0, 0, 3, 0, courierTask.getOverlayColor()));
		PortTaskOverlayColor.setIcon(BORDER_COLOR_ICON);
	}

	public void updateVisibility()
	{
		hidePortTaskSlotOverlay.setIcon(courierTask.isTracking() ? VISIBLE_ICON : INVISIBLE_ICON);
	}

	private void updateImages(CourierTask courierTask)
	{
		cargoLabel.setIcon(PACKAGE);
		destinationLabel.setIcon(DESTINATION);
		noticeLabel.setIcon(NOTICE);
		anchorLabel.setIcon(ANCHOR);
		boatLabel.setIcon(BOAT);

		cargoLabel.setText(courierTask.getData().getCargoLocation().getName());
		destinationLabel.setText(courierTask.getData().getDeliveryLocation().getName());
		cargoLabel.setToolTipText("Cargo Location");
		destinationLabel.setToolTipText("Delivery Location");
		noticeLabel.setToolTipText("Cargo Item Needed");
		clientThread.invokeLater(() ->
		{
			final ItemComposition cargoComposition = itemManager.getItemComposition(courierTask.getData().cargo);
			noticeLabel.setText(courierTask.getData().getCargoAmount() + "x " + cargoComposition.getMembersName());
		});
	}

}
