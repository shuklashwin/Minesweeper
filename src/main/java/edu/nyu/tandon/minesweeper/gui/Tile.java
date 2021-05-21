package edu.nyu.tandon.minesweeper.gui;

import edu.nyu.tandon.minesweeper.constants.Constants;
import edu.nyu.tandon.minesweeper.utils.GuiUtils;

import javax.swing.*;
import java.awt.*;

public class Tile extends JLabel {

	public Tile(int tileType) {
		setPreferredSize(new Dimension(Constants.TILE_SIZE, Constants.TILE_SIZE));
		setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		this.setIcon(GuiUtils.getImageIcon(tileType));
	}

}
