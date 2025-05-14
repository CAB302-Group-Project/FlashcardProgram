package ai;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class pdfReader {

    private static String findFilePath() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select a PDF file");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        // Filter to only choose pdfs
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF files", "pdf"));

        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();

            return selectedFile.getAbsolutePath();
        }

        System.out.println("No file selected.");
        return null;
    }

    // Only this method needs to be called. It automatically calls findfilepath within it.
    public static String pdfExtract() throws IOException {

        File pdf = new File(findFilePath()); // Grabs the file from findFilePath().
        PDDocument document = PDDocument.load(pdf); // Loading the file into the method

        PDFTextStripper pdfStripper = new PDFTextStripper(); // Just instantiates the extraction tool

        String pdfContents = pdfStripper.getText(document); //Extracting the text from the pdf

        document.close(); // close it up

        return pdfContents; // Money time
    }
}