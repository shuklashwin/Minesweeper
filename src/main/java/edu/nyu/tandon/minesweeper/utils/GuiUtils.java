package edu.nyu.tandon.minesweeper.utils;

import edu.nyu.tandon.minesweeper.constants.Constants;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GuiUtils {

	private static final List<ImageIcon> imageIconList = new ArrayList<>(13);

	public static void loadAllImages() {
		for (int index = 0; index < 13; index++) {
			imageIconList.add(new ImageIcon(new ImageIcon(getImagePath(index)).getImage().getScaledInstance(Constants.TILE_SIZE, Constants.TILE_SIZE, Image.SCALE_SMOOTH)));
		}
	}

	public static ImageIcon getImageIcon(int tile) {
		return imageIconList.get(tile);
	}

	public static String getImagePath(int tile) {
		return Constants.IMAGES_PATH + tile + Constants.IMAGE_EXTENSION;
	}

}
