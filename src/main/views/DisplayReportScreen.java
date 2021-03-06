package main.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import main.models.Penalty;
import main.util.FileUtil;

public class DisplayReportScreen extends JFrame {

    JTextArea taFinal = new JTextArea();
    JButton save = new JButton("Save");
    JButton cancel = new JButton("Cancel");

    public DisplayReportScreen(Penalty data) {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setTitle("Warning Report");
        ImageIcon imageIcon = new ImageIcon(FileUtil.LOGO_PATH);
        JLabel label = new JLabel(imageIcon);

        String text = " From:\n" + " "
                + data.getAdminName() + "\n" + " "
                + data.getAdminPosition() + "\n" + " "
                + data.getDepartment() + "\n" + " "
                + data.getDate() + "\n" + "\n"
                + " To:\n" + " "
                + data.getStudent().getFullName() + "\n" + " "
                + data.getStudent().getMatrixId() + "\n" + "\n" + " "
                + data.getWarning() + "\n\n";

        double totalFine = data.calcTotalFine();
        if (totalFine <= 0.0) {
            text += "This is a warning. No charges applied.\n";
        } else {
            text += String.format("You are charged RM %.2f in total. Please pay as soon as possible.",
                     totalFine);
        }

        taFinal = new JTextArea(text);
        taFinal.setFont(new Font("Arial", Font.BOLD, 12));
        taFinal.setBounds(10, 10, 1000, 850);
        taFinal.setLineWrap(true);
        taFinal.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(taFinal);
        add(scrollPane, BorderLayout.CENTER);

        //save.addActionListener(new ActionListener(){
        //  public void actionPerformed(ActionEvent arg0)
        // {
        // showSaveFileDialog();
        // }
        // }); 
        save.addActionListener((ActionEvent e) -> {
            String output;
            output = JOptionPane.showInputDialog(null, "File Name", JOptionPane.INFORMATION_MESSAGE);

            FileUtil.writeToTxtFile(output, taFinal.getText());
        });

        cancel.addActionListener((ActionEvent e) -> {
            int n = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to exit?", "Exit?", JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION) {
                dispose();
            }
        });

        add(save);
        add(cancel);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1030, 400);
    }
}
