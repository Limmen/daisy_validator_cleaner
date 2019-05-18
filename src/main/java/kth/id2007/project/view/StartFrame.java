package kth.id2007.project.view;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Startup Frame
 */  
public class StartFrame extends JFrame
{
	private GUI gui;

	/**
	 * Class constructor. Initializes the frame.
	 *
	 * @param gui controller
	 */
	public StartFrame(GUI gui){
		super("Daisy Validator Cleaner");
		this.gui = gui;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new MigLayout("wrap 2, insets 50 50 50 50"));

		JTextField dirOfsmilFiles=new JTextField(200);
		dirOfsmilFiles.setEditable(false);
		JTextField validationFile=new JTextField(200);
		validationFile.setEditable(false);
		JTextField backupDir=new JTextField(200);
		backupDir.setEditable(false);


		JButton smilDirSelectorButton=new JButton("Select directory with the book (should contain smil siles, NCC file etc.)");
		smilDirSelectorButton.setName("select_smil_dir");
		smilDirSelectorButton.addActionListener(gui. new smilFileSelectorButton(dirOfsmilFiles));

		JButton errorFileSelectorButton=new JButton("Select XML file (output from Daisy Pipeline Validator) with errors to correct");
		errorFileSelectorButton.setName("select_error_file");
		errorFileSelectorButton.addActionListener(gui. new ErrorFileSelectorButton(validationFile));

		JButton backupDirSelectorButton=new JButton("Select backup directory (where the old smil files will be placed for backup)");
		backupDirSelectorButton.setName("select_backup_dir");
		backupDirSelectorButton.addActionListener(gui. new BackupDirSelectorButton(backupDir));

		JTextField logsField=new JTextField(200);
		logsField.setEditable(false);
		logsField.setText("Logs:");
		JTextArea logsArea=new JTextArea("");
		logsArea.setEditable(false);

		JScrollPane logsScroll = new JScrollPane (logsArea,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		logsScroll.setMinimumSize(new Dimension(800, 300));
		logsScroll.setPreferredSize(new Dimension(800, 300));


		JButton runButton=new JButton("Run");
		runButton.setName("run");
		runButton.addActionListener(gui. new RunButton(dirOfsmilFiles, validationFile, logsArea, this, backupDir));

		JButton clearnLogButton =new JButton("Clear Log");
		clearnLogButton.setName("clear_log");
		clearnLogButton.addActionListener(gui. new ClearLogButton(logsArea));

		JLabel lbl = new JLabel("Cleanup Daisy validator errors");
		lbl.setName("config_title");
		add(lbl,"span 2, center");
		lbl = new JLabel("Select Configuration Below.");
		lbl.setName("config_title");
		add(lbl,"span 2, center");
		lbl = new JLabel("1. Select folder with the book that contains the smil files");
		lbl.setName("config_title");
		add(lbl,"span 2, center");
		lbl = new JLabel("2. Select the XML file that contains the errors");
		lbl.setName("config_title");
		add(lbl,"span 2, center");
		lbl = new JLabel("3. Click 'Run'");
		lbl.setName("config_title");
		add(lbl,"span 2, center");
		lbl = new JLabel("Directory with smil Files:");
		lbl.setName("smil_files_dir_label");
		add(lbl,"span 1");
		dirOfsmilFiles.setName("smil_file_dir");
		add(dirOfsmilFiles,"span 1");
		lbl = new JLabel("File with validation errors (XML):");
		lbl.setName("validation_errors_label");
		add(lbl, "span 1");
		validationFile.setName("validation_errors_field");
		add(validationFile,"span 1");
		lbl = new JLabel("Backup directory:");
		lbl.setName("backup_dir_label");
		add(lbl, "span 1");
		validationFile.setName("backup_dir_field");
		add(backupDir,"span 1");
		add(smilDirSelectorButton,"span 2, gaptop 20");
		add(errorFileSelectorButton,"span 2, gaptop 20");
		add(backupDirSelectorButton,"span 2, gaptop 20");
		add(runButton,"span 2, gaptop 20");
		add(logsField,"span 2, gaptop 20");
		add(logsScroll,"span 2, gaptop 20");
		add(clearnLogButton,"span 2, gaptop 20");
		pack();
		setLocationRelativeTo(null); //center on screen
		setVisible(true);
	}

}
