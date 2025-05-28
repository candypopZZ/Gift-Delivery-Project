/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package groupproject;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
/**
 *
 * @author User
 */
public class GiftReceiptWriter {
    public static void writeReceiptToFile(String receipt) {
        String fileName = "receipt.txt";
        try (PrintWriter writer = new PrintWriter(new File(fileName))) {
            writer.println(receipt);
            System.out.println("Receipt has been saved to '" + fileName + "'.");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
            return;
        }

        // Open the receipt file in Notepad
        try {
            ProcessBuilder pb = new ProcessBuilder("notepad.exe", fileName);
            pb.start();
        } catch (IOException e) {
            System.out.println("Error opening receipt in Notepad: " + e.getMessage());
        }
    }
}
