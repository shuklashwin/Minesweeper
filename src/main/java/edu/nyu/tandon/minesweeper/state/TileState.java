package edu.nyu.tandon.minesweeper.state;

import edu.nyu.tandon.minesweeper.constants.Constants;

import java.io.Serializable;

public class TileState implements Serializable {

	private static final long serialVersionUID = 5230549922091722631L;

	private int tileType;
	private boolean flagged;
	private boolean revealed;

	public TileState(int tileType) {
		this.tileType = tileType;
	}

	public int getTileDisplayValue() {
		if (isFlagged()) {
			return Constants.FLAG;
		}

		return isRevealed() ? tileType : Constants.DEFAULT;
	}

	public void setTileType(int tileType) {
		this.tileType = tileType;
	}

	public boolean isMine() {
		return tileType == Constants.MINE;
	}

	public boolean isNumber() {
		return tileType >= Constants.ONE && tileType <= Constants.EIGHT;
	}

	public boolean isEmpty() {
		return tileType == Constants.ZERO;
	}

	public boolean isFlagged() {
		return flagged;
	}

	public void setFlag() {
		this.flagged = true;
	}

	public void unsetFlag() {
		this.flagged = false;
	}

	public boolean isRevealed() {
		return revealed;
	}

	public void reveal() {
		this.revealed = true;
	}

	public boolean isWronglyFlagged() {
		return this.isFlagged() && !this.isMine();
	}

	public String getDisplayString() {
		if (tileType == Constants.ZERO) {
			return " ";
		} else if (tileType == Constants.MINE) {
			return "x";
		} else {
			return String.valueOf(tileType);
		}
	}

	@Override
	public String toString() {
		return "TileState{" +
				"tileType=" + tileType +
				", flagged=" + flagged +
				", revealed=" + revealed +
				'}';
	}
}
