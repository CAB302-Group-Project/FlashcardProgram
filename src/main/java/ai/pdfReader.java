package ai;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * A simple class to find and open files for further use as strings. (pdfs in creating decks, etc.)
 */

public class pdfReader {

    /**
     * Opens a file explorer to grab the file path of a document.
     * .pdfs only.
     *
     * @return The file path of the document opened in the file explorer.
     */

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

    /**
     * Opens the file from the findFilePath() method to strip the pdf into a text string.
     *
     * @return A plain-text string of the contents of a pdf.
     */

    public static String pdfExtract() throws IOException {



        File pdf = new File(findFilePath()); // Grabs the file from findFilePath().
        PDDocument document = PDDocument.load(pdf); // Loading the file into the method

        PDFTextStripper pdfStripper = new PDFTextStripper(); // Just instantiates the extraction tool

        String pdfContents = pdfStripper.getText(document); //Extracting the text from the pdf

        document.close(); // close it up

        return pdfContents; // Money time
    }
}
