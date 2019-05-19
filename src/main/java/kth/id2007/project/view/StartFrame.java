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
	private final Font Plain = new Font("Serif", Font.PLAIN, 12);
	private final Font Title = new Font("Serif", Font.BOLD, 18);
	private final Font PBold = Plain.deriveFont(Plain.getStyle() | Font.BOLD);

	/**
	 * Class constructor. Initializes the frame.
	 *
	 * @param gui controller
	 */
	public StartFrame(GUI gui){
		super("Daisy Validator Cleaner");
		this.gui = gui;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new MigLayout("wrap 6, insets 50 50 50 50"));

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

		JCheckBox elapsedTimeCheckBox = new JCheckBox("elapsed time error (smil files)");
		JCheckBox totalTimeCheckBox = new JCheckBox("total time error (ncc files)");
		JCheckBox uniqueIdError = new JCheckBox("unique id error (smil files)");

		JButton runButton=new JButton("Run");
		runButton.setName("run");
		runButton.addActionListener(gui. new RunButton(dirOfsmilFiles, validationFile, logsArea, this, backupDir, elapsedTimeCheckBox, totalTimeCheckBox, uniqueIdError));

		JButton clearnLogButton =new JButton("Clear Log");
		clearnLogButton.setName("clear_log");
		clearnLogButton.addActionListener(gui. new ClearLogButton(logsArea));

		JLabel lbl = new JLabel("Cleanup Daisy validator errors");
		lbl.setName("config_title");
		lbl.setFont(Title);
		add(lbl,"span 6, center");
		lbl = new JLabel("Select Configuration Below.");
		lbl.setName("config_title");
		lbl.setFont(PBold);
		add(lbl,"span 6, gaptop 20");
		lbl = new JLabel("1. Select folder with the book that contains the smil files");
		lbl.setName("config_title");
		lbl.setFont(PBold);
		add(lbl,"span 6");
		lbl = new JLabel("2. Select the XML file that contains the errors");
		lbl.setName("config_title");
		lbl.setFont(PBold);
		add(lbl,"span 6");
		lbl = new JLabel("3. Select a directory to place the old XML files for backup in case something goes wrong");
		lbl.setName("config_title");
		lbl.setFont(PBold);
		add(lbl,"span 6");
		lbl = new JLabel("4. Select in the checkboxes which type of errors you want to correct (you can select more than one)");
		lbl.setName("config_title");
		lbl.setFont(PBold);
		add(lbl,"span 6");
		lbl = new JLabel("5. Click 'Run'");
		lbl.setName("config_title");
		lbl.setFont(PBold);
		add(lbl,"span 6, gapbottom 20");
		lbl = new JLabel("Directory with smil Files:");
		lbl.setName("smil_files_dir_label");
		lbl.setFont(Plain);
		add(lbl,"span 3");
		dirOfsmilFiles.setName("smil_file_dir");
		dirOfsmilFiles.setFont(Plain);
		add(dirOfsmilFiles,"span 3");
		lbl = new JLabel("File with validation errors (XML):");
		lbl.setName("validation_errors_label");
		lbl.setFont(Plain);
		add(lbl, "span 3");
		validationFile.setName("validation_errors_field");
		validationFile.setFont(Plain);
		add(validationFile,"span 3");
		lbl = new JLabel("Backup directory:");
		lbl.setName("backup_dir_label");
		lbl.setFont(Plain);
		add(lbl, "span 3");
		validationFile.setName("backup_dir_field");
		backupDir.setFont(Plain);
		add(backupDir,"span 3");
		smilDirSelectorButton.setFont(Plain);
		add(smilDirSelectorButton,"span 6, gaptop 20");
		errorFileSelectorButton.setFont(Plain);
		add(errorFileSelectorButton,"span 6, gaptop 20");
		backupDir.setFont(Plain);
		backupDirSelectorButton.setFont(Plain);
		add(backupDirSelectorButton,"span 6, gaptop 20");
		lbl = new JLabel("Select which type of errors you want to correct:");
		lbl.setName("error_type_label");
		lbl.setFont(Plain);
		add(lbl, "span 6, gaptop 20");
		elapsedTimeCheckBox.setFont(Plain);
		totalTimeCheckBox.setFont(Plain);
		uniqueIdError.setFont(Plain);
		runButton.setFont(Plain);
		logsField.setFont(Plain);
		logsScroll.setFont(Plain);
		clearnLogButton.setFont(Plain);
		add(elapsedTimeCheckBox, "span 2, gaptop 20");
		add(totalTimeCheckBox, "span 2, gaptop 20");
		add(uniqueIdError, "span 2, gaptop 20");
		add(runButton,"span 6, gaptop 20");
		add(logsField,"span 6, gaptop 20");
		add(logsScroll,"span 6, gaptop 20");
		add(clearnLogButton,"span 6, gaptop 20");
		pack();
		setLocationRelativeTo(null); //center on screen
		setVisible(true);
	}

}
