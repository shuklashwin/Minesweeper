package edu.nyu.tandon.minesweeper.gui;

import edu.nyu.tandon.minesweeper.staging.HighScoreInfo;
import edu.nyu.tandon.minesweeper.staging.SavedGameInfo;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HighScoresView extends JDialog {

	private JPanel panel1;
	private JScrollPane scrollPane1;
	private JTextArea textArea1;
	private JTable table;

	public HighScoresView(List<HighScoreInfo> list) {
		initComponents(list);
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	private void initComponents(List<HighScoreInfo> list) {
		panel1 = new JPanel();
		scrollPane1 = new JScrollPane();
		textArea1 = new JTextArea();
		table = new JTable();

		setTitle("Hall of Fame");
		Container contentPane = getContentPane();
		contentPane.setLayout(new FlowLayout());

		panel1.setLayout(new FlowLayout());

		textArea1.setColumns(45);
		textArea1.setRows(14);
		textArea1.setEditable(false);

		String[][] data = convertData(list);
		String[] columns = new String[]{"Player Name", "Score", "Date"};
		table = new JTable(data, columns);
		table.setBounds(new Rectangle(45, 14));

		scrollPane1.setViewportView(table);
		panel1.add(scrollPane1);
		contentPane.add(panel1);
		pack();
		setLocationRelativeTo(getOwner());
	}

	public JTextArea getTextArea() {
		return textArea1;
	}

	public String[][] convertData(List<HighScoreInfo> highScoreInfoList) {
		String[][] data = new String[highScoreInfoList.size()][3];
		for (int index = 0; index < highScoreInfoList.size(); index++) {
			data[index] = highScoreInfoList.get(index).getAsArray();
		}
		return data;
	}
}
