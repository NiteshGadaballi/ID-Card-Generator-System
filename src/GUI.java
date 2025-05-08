import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class GUI extends JFrame {
    private JTextField nameField, idField, designationField, contactField;
    private JLabel photoLabel;
    private JButton viewButton, downloadButton; // Declare buttons here
    private File selectedPhoto;
    private File generatedPDF;

    public GUI() {
        setTitle("ID Card Generator");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("ID Number:"), gbc);
        gbc.gridx = 1;
        idField = new JTextField(20);
        add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Designation:"), gbc);
        gbc.gridx = 1;
        designationField = new JTextField(20);
        add(designationField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Contact (10 digits):"), gbc);
        gbc.gridx = 1;
        contactField = new JTextField(20);
        add(contactField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Photo:"), gbc);
        gbc.gridx = 1;
        photoLabel = new JLabel("No photo selected");
        add(photoLabel, gbc);

        JButton uploadButton = new JButton("Upload Photo");
        uploadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedPhoto = fileChooser.getSelectedFile();
                photoLabel.setText(selectedPhoto.getName());
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 5;
        add(uploadButton, gbc);

        JButton generateButton = new JButton("Generate ID Card");
        generateButton.addActionListener(e -> {
            String name = nameField.getText();
            String id = idField.getText();
            String designation = designationField.getText();
            String contact = contactField.getText();

            if (name.isEmpty() || id.isEmpty() || designation.isEmpty() || contact.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill all fields.");
                return;
            }

            if (!contact.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(null, "Invalid contact number. Enter exactly 10 digits.");
                return;
            }

            FileHandler.saveRecord(name, id, designation, contact);
            generatedPDF = IDCardGenerator.generatePDF(name, id, designation, contact, selectedPhoto);

            if (generatedPDF != null) {
                JOptionPane.showMessageDialog(null, "ID Card Generated Successfully!");

                // Enable View and Download Buttons
                viewButton.setEnabled(true);
                downloadButton.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to generate ID Card.");
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 6;
        add(generateButton, gbc);

        // Initialize View Button
        viewButton = new JButton("View ID Card");
        viewButton.setEnabled(false);
        viewButton.addActionListener(e -> {
            if (generatedPDF != null && generatedPDF.exists()) {
                try {
                    Desktop.getDesktop().open(generatedPDF);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 7;
        add(viewButton, gbc);

        // Initialize Download Button
        downloadButton = new JButton("Download ID Card");
        downloadButton.setEnabled(false);
        downloadButton.addActionListener(e -> {
            if (generatedPDF != null && generatedPDF.exists()) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setSelectedFile(new File(generatedPDF.getName()));
                int userSelection = fileChooser.showSaveDialog(null);
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File saveFile = fileChooser.getSelectedFile();
                    try {
                        Files.copy(generatedPDF.toPath(), saveFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        JOptionPane.showMessageDialog(null, "File downloaded successfully!");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error saving file.");
                        ex.printStackTrace();
                    }
                }
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 8;
        add(downloadButton, gbc);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI().setVisible(true));
    }
}
