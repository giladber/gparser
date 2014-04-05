package com.gparser.ui;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Main application window.
 * Created by Gilad Ber on 3/29/14.
 */
public class MainWindow extends JFrame
{
	private final JTable fileTable;
	private final JProgressBar progressBar;
	private final JPanel mainPanel;
	private final JSplitPane tableSplitPane;
	private final JTabbedPane tabbedPane;
	private final JScrollPane scrollPane;
	private final JPanel fileViewerPanel;
	private final JPanel fileEditPanel;

	public MainWindow() throws UnsupportedLookAndFeelException
	{
		mainPanel = new JPanel();
		tableSplitPane = new JSplitPane();
		tableSplitPane.setDividerLocation(0.2);
		tabbedPane = new JTabbedPane();
		progressBar = new JProgressBar();
		fileTable = new JTable(new DefaultTableModel());
		scrollPane = new JScrollPane(fileTable);
		fileViewerPanel = new JPanel();
		fileEditPanel = new JPanel();

		setLayout(new BorderLayout());
		setContentPane(mainPanel);

		createUIComponents();

		pack();
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setTitle("GParser");

		UIManager.setLookAndFeel(new NimbusLookAndFeel());

		Dimension maximumSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setPreferredSize(maximumSize);

		revalidate();
		repaint();
	}

	private void createUIComponents()
	{
		mainPanel.setLayout(new GridLayout(1, 1));
		mainPanel.add(tabbedPane);
		fileViewerPanel.setLayout(new GridLayout(2, 1));
		fileViewerPanel.add(tableSplitPane);
		fileViewerPanel.add(progressBar);
		scrollPane.setViewportView(fileTable);
		tableSplitPane.setRightComponent(scrollPane);
		tableSplitPane.setLeftComponent(fileEditPanel);
		tabbedPane.addTab("File Viewer", fileViewerPanel);
	}
}
