/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package groupproject;
import java.util.Scanner;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
/**
 *
 * @author User
 */
public class GiftDelExpress {
    private static String customerName; // Variable to store the customer's name
private static String customerContact; // Variable to store the customer's contact information

    public static void main(String[] args) {
    
        Scanner scanner = new Scanner(System.in);
    
        boolean isRunning = true; 
        String[] currentOrder = null; // Store current order details
        List<String[]> orders = new ArrayList<>(); // List to store all orders

        // Stock management: Initializing the product stock
        Map<String, Integer> stock = new HashMap<>();
        stock.put("Flower Bouquet", 100);
        stock.put("Chocolate Box", 50);
        stock.put("Customized Mug", 75);
        stock.put("Teddy Bear", 40);
        stock.put("Gift Card", 200);
        stock.put("Perfume Set", 30);
        stock.put("Snack Hamper", 25);

        System.out.println("*************************************");
        System.out.println("*      Gift Delivery Express     *");
        System.out.println("**************************************");
        System.out.println("     ~ Making Moments Special ~  \n");   
        // Function to get customer details (name, contact)
        getCustomerDetails(scanner);

        while (isRunning) {
            try {
                // Insert order data and get the current order details
                currentOrder = insertData(scanner, stock);
            
                // If current order is valid (not null), proceed with further steps
                if (currentOrder != null) {
                    orders.add(currentOrder); // Add the current order to the orders list
                
                    // Generate and display the receipt for the current order
                    String receipt = generateReceipt(orders);
                    System.out.println("\n" + receipt);

                    // Ask user if they want to cancel the current order
                    System.out.print("Do you want to cancel or delete the order? (yes/no): ");
                    String cancelResponse = scanner.next().trim().toLowerCase();

                    // If the user chooses "yes", remove the order from the list
                    if (cancelResponse.equals("yes")) {
                        orders.remove(currentOrder);
                        System.out.println("Your order has been canceled.\n");
                        continue; // Skip to the next iteration (prompt for new order)
                    }

                    // Ask user if they want to update the current order
                    System.out.print("Do you want to update the order? (yes/no): ");
                    String updateResponse = scanner.next().trim().toLowerCase();

                    // If the user chooses "yes", update the current order
                    if (updateResponse.equals("yes")) {
                        updateData(scanner, currentOrder);
                    }

                    // Generate and display the updated receipt
                    receipt = generateReceipt(orders);
                    System.out.println("\n" + receipt);

                    // Save the receipt to a file using a file writer class
                    GiftReceiptWriter.writeReceiptToFile(receipt);

                    // Ask the user if they want to place another order
                    System.out.print("Place another order? (yes/no): ");
                    String continueResponse = scanner.next().trim().toLowerCase();
                
                    // If the response is not "yes", stop the order process
                    if (!continueResponse.equals("yes")) {
                        isRunning = false;
                    }
                }
            } catch (Exception e) {
                // Handle any exceptions that may occur during the order process
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine(); // Consume the invalid input
            }
        }
        System.out.println("Thank you for using Gift Delivery Express!");
    }

    public static String[] selectGift(Scanner scanner, Map<String, Integer> stock) {
        while (true) {
            try {
                // Prompt the user for help choosing a gift
                System.out.print("Do you need help choosing a gift? (yes/no): ");
                String response = scanner.next();
                // Retrieve available gift options
                String[][] gifts = getGiftOptions();
                
                // If the user wants a recommendation, suggest a random gift
                if (response.equalsIgnoreCase("yes")) {
                    int randomIndex = (int) (Math.random() * gifts.length);
                    System.out.println("Recommended: " + gifts[randomIndex][0] + " - RM" + gifts[randomIndex][1] + "\n");
                    return gifts[randomIndex];
                } else if (response.equalsIgnoreCase("no")) {
                    // Display available gifts and their stock
                    System.out.println("Available Gifts:");
                    for (int i = 0; i < gifts.length; i++) {
                        String giftName = gifts[i][0];
                        int giftStock = stock.getOrDefault(giftName, 0);
                        System.out.println((i + 1) + ". " + giftName + " - RM" + gifts[i][1] + " (stock: " + giftStock + ")");
                    }
                    // Get valid input for user's gift choice
                    int choice = getValidInput(scanner, 1, gifts.length);
                    String selectedGift = gifts[choice - 1][0];
                    // Check if the selected gift is in stock
                    if (stock.get(selectedGift) > 0) {
                        return gifts[choice - 1];
                    } else {
                        System.out.println("Sorry, " + selectedGift + " is out of stock.");
                        return null;// Return null if out of stock
                    }
                } else {
                    throw new InputMismatchException("Invalid response. Please enter 'yes' or 'no'.");
                }
            } catch (InputMismatchException e) {
                // Catch invalid input and prompt again
                System.out.println(e.getMessage());
                scanner.nextLine();
            }
        }
    }

    public static String[][] getGiftOptions() {
        return new String[][]{
                {"Flower Bouquet", "50.00"},
                {"Chocolate Box", "45.00"},
                {"Customized Mug", "30.00"},
                {"Teddy Bear", "40.00"},
                {"Gift Card", "10.00"},
                {"Perfume Set", "60.00"},
                {"Snack Hamper", "70.00"}
        };
    }

    public static String[] selectPackaging(Scanner scanner) {
        System.out.println("Choose packaging style:");
        String[][] packaging = {
                {"Standard", "5.00"},
                {"Premium", "15.00"},
                {"Luxury", "25.00"}
        };
        for (int i = 0; i < packaging.length; i++) {
            System.out.println((i + 1) + ". " + packaging[i][0] + " - RM" + packaging[i][1]);
        }
        int choice = getValidInput(scanner, 1, packaging.length);
        return packaging[choice - 1];
    }

    public static String[] selectLocation(Scanner scanner) {
        System.out.println("Choose delivery location:");
        String[][] locations = {
                {"Kuala Lumpur", "10.00"},
                {"Selangor", "8.00"},
                {"Johor", "12.00"},
                {"Penang", "15.00"}
        };
        for (int i = 0; i < locations.length; i++) {
            System.out.println((i + 1) + ". " + locations[i][0] + " - RM" + locations[i][1]);
        }
        int choice = getValidInput(scanner, 1, locations.length);
        return locations[choice - 1];
    }

    public static int getQuantity(Scanner scanner) {
        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();
        return quantity > 0 ? quantity : getQuantity(scanner);
    }
    // Utility to get valid input from the user
    public static int getValidInput(Scanner scanner, int min, int max) {
        while (true) {
            try {
                int choice = scanner.nextInt();
                if (choice >= min && choice <= max) {
                    return choice;
                } else {
                    System.out.println("Invalid choice. Please select a valid option.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();  // Clear invalid input
            }
        }
    }
    public static String selectOccasion(Scanner scanner) {
        // Display available occasions for the gift and allow the user to choose
        System.out.println("Choose an occasion:");
        String[] occasions = {"Birthday", "Anniversary", "Mother's Day", "Father's Day", "Graduation"};
        for (int i = 0; i < occasions.length; i++) {
            System.out.println((i + 1) + ". " + occasions[i]);
        }
        int choice = getValidInput(scanner, 1, occasions.length);  // Get the user's valid choice
        return occasions[choice - 1];  // Return the selected occasion
    }

    // Insert data method (including name, contact, address, etc.)
    public static String[] insertData(Scanner scanner, Map<String, Integer> stock) {
    try {
        // Occasions, gifts, packaging, etc.
        String occasion = selectOccasion(scanner);
        String[] giftDetails = selectGift(scanner, stock);

        if (giftDetails == null) {
            System.out.println("No stock available for the selected gift.");
            return null;
        }

        String[] packagingDetails = selectPackaging(scanner);
        int quantity = getQuantity(scanner);

        // Check stock availability
        String giftName = giftDetails[0];
        int availableStock = stock.getOrDefault(giftName, 0);

        if (quantity > availableStock) {
            System.out.println("Insufficient stock. Available stock for " + giftName + ": " + availableStock);
            return null;
        }

        // Deduct stock
        stock.put(giftName, availableStock - quantity);

        String[] locationDetails = selectLocation(scanner);
        String location = locationDetails[0];

        System.out.print("Enter your further address (for " + location + "): ");
        scanner.nextLine(); // Consume the leftover newline
        String furtherAddress = scanner.nextLine();

        // Validate delivery date
        String deliveryDate;
        do {
            System.out.print("Enter delivery date (DD/MM/YYYY): ");
            deliveryDate = scanner.nextLine();
            if (!validateDate(deliveryDate)) {
                System.out.println("Invalid date or date is in the past. Please enter a future date.");
            }
        } while (!validateDate(deliveryDate));

        // Validate delivery time
        String deliveryTime;
        do {
            System.out.print("Enter delivery time (HH:mm): ");
            deliveryTime = scanner.nextLine();
            if (!validateTime(deliveryTime)) {
                System.out.println("Invalid time format. Please use HH:mm format.");
            }
        } while (!validateTime(deliveryTime));

        // Personal message
        System.out.print("Enter personal message (e.g., Congratulations!): ");
        String personalNote = scanner.nextLine();

        // Return all data in a single array
        return new String[] {
            customerName, customerContact, occasion, giftDetails[0], 
            packagingDetails[0], locationDetails[0], String.valueOf(quantity),
            deliveryDate, deliveryTime, personalNote, giftDetails[1], 
            packagingDetails[1], locationDetails[1], furtherAddress
        };
    } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
        return null;
    }
    }

    // Search for the order by customer name and allow updating it
    public static void updateData(Scanner scanner, String[] currentOrder) {
        
    boolean isUpdating = true; // To control the update menu loop

    // Loop until the user provides the correct name
    while (true) {
        System.out.println("\nUpdate your order:");
        System.out.print("Enter your name to search for your order: ");
        String searchName = scanner.next().trim();

        if (currentOrder[0].equalsIgnoreCase(searchName)) {
            // If the name matches, display the update menu
            System.out.println("Order found! What would you like to update?");
            
            // Loop to handle the update menu until the user selects option 7
            while (isUpdating) {
                System.out.println("1. Customer Name");
                System.out.println("2. Contact Number");
                System.out.println("3. Delivery Date");
                System.out.println("4. Delivery Time");
                System.out.println("5. Personal Note");
                System.out.println("6. Further Address");
                System.out.println("7. Delete Personal Note");
                System.out.println("8. Exit Update");
                
                int choice = getValidInput(scanner, 1, 8);

                switch (choice) {
                    case 1:
                        System.out.print("Enter new Customer Name: ");
                        currentOrder[0] = scanner.next();
                        System.out.println("Order updated!");
                        break;
                    case 2:
                        System.out.print("Enter new Contact Number: ");
                        currentOrder[1] = scanner.next();
                        System.out.println("Order updated!");
                        break;
                    case 3:
                        System.out.print("Enter new Delivery Date (DD/MM/YYYY): ");
                        currentOrder[7] = scanner.next();
                        System.out.println("Order updated!");
                        break;
                    case 4:
                        System.out.print("Enter new Delivery Time (HH:MM): ");
                        currentOrder[8] = scanner.next();
                        System.out.println("Order updated!");
                        break;
                    case 5:
                        System.out.print("Enter new Personal Note: ");
                        scanner.nextLine(); // Consume newline
                        currentOrder[9] = scanner.nextLine();
                        System.out.println("Order updated!");
                        break;
                    case 6:
                        System.out.print("Enter new Further Address (for " + currentOrder[5] + "): ");
                        scanner.nextLine(); // Consume newline
                        currentOrder[13] = scanner.nextLine();
                        System.out.println("Order updated!");
                        break;
                    case 7:
                        // Delete the personal note
                        System.out.println("Are you sure you want to delete the personal note? (yes/no)");
                        String confirmDelete = scanner.next().trim().toLowerCase();
                        if (confirmDelete.equals("yes")) {
                            currentOrder[9] = ""; // Remove the personal note
                            System.out.println("Personal note deleted!");
                        } else {
                            System.out.println("Deletion canceled.");
                        }
                        break;
                    case 8:
                        System.out.println("Exiting update menu. Printing the receipt...");
                        isUpdating = false; // Exit the update menu loop
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
            break; // Exit the name search loop once the update process is complete
        } else {
            System.out.println("Order not found! Enter the correct name:");
        }
    }
    }
    public static String generateReceipt(List<String[]> orders) {
        Set<String> uniqueLocations = new HashSet<>();
        double totalGiftCost = 0.0;
        double totalPackagingCost = 0.0;
        double totalServiceCharge = 0.0;
        double grandTotal = 0.0;  // To hold the total cost of all orders
    
        int orderNumber = (int)(Math.random() * 9000) + 1000;

        // Receipt header
        String receipt = "********** Gift Delivery Express **********\n";
        receipt += "--------- RECEIPT ---------\n";
        receipt += "Order No: " + orderNumber + "\n";
        receipt += "Customer Name: " + orders.get(0)[0] + "\n"; 
        receipt += "Contact Number: " + orders.get(0)[1] + "\n";  

        // Loop through each order in the orders list
        for (int i = 0; i < orders.size(); i++) {
            String[] order = orders.get(i);  // Get the current order
        
            // Calculate gift, packaging, and service charges for each order
            int quantity = Integer.parseInt(order[6]);
            double giftCost = Double.parseDouble(order[10]) * quantity;
            double packagingCost = Double.parseDouble(order[11]) * quantity;
            double serviceCharge = Double.parseDouble(order[12]);

            // If the delivery location is unique, add the service charge
            if (!uniqueLocations.contains(order[5])) {
                totalServiceCharge += serviceCharge;
                uniqueLocations.add(order[5]);
            }

            totalGiftCost += giftCost;
            totalPackagingCost += packagingCost;
        
            // Add current order details to the receipt
            receipt += "\n--- Order Details ---\n";
            receipt += (i + 1) + ". Occasion: " + order[2] + "\n";
            receipt += "   Gift: " + order[3] + "\n";
            receipt += "   Packaging: " + order[4] + "\n";
            receipt += "   Delivery Location: " + order[5] + "\n";
            receipt += "   Quantity: " + order[6] + "\n";
            receipt += "   Delivery Date: " + order[7] + "\n";
            receipt += "   Delivery Time: " + order[8] + "\n";
            receipt += "   Personal Note: " + order[9] + "\n";
            receipt += String.format("   Gift Cost: RM%.2f\n", giftCost);
            receipt += String.format("   Packaging Cost: RM%.2f\n", packagingCost);
            receipt += String.format("   Delivery Fee: RM%.2f\n", serviceCharge);

        }

        // Calculate the grand total for all orders
        grandTotal = totalGiftCost + totalPackagingCost + totalServiceCharge;

        // Add the summary details once after the last order
        receipt += "\n--- Summary ---\n";
        receipt += String.format("Total Gift Cost: RM%.2f\n", totalGiftCost);
        receipt += String.format("Total Packaging Cost: RM%.2f\n", totalPackagingCost);
        receipt += String.format("Total Delivery Fee: RM%.2f\n", totalServiceCharge);
        receipt += String.format("Grand Total: RM%.2f\n", grandTotal);
        receipt += "=================================\n";

        return receipt; // Return the full receipt string
    }
    private static void getCustomerDetails(Scanner scanner) {
            boolean validInput = false;
            System.out.println("Please enter your details for the order:");

            // Get customer name
            do {
                System.out.print("Customer Name: ");
                customerName = scanner.nextLine().trim();
                if (customerName.length() > 0) {
                    validInput = true;
                } else {
                    System.out.println("Name cannot be empty.");
                }
            } while (!validInput);

            // Get customer contact with validation
            validInput = false;
            do {
                try {
                    System.out.print("Customer Contact Number (10 digits): ");
                    customerContact = scanner.nextLine().trim();
                    if (customerContact.matches("\\d{10}")) {
                        validInput = true;
                    } else {
                        System.out.println("Please enter exactly 10 digits for phone number.");
                    }
                } catch (Exception e) {
                    System.out.println("Invalid input. Please enter numbers only.");
                }
            } while (!validInput);
    }

    private static boolean validateDate(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            Date inputDate = sdf.parse(date);
            Date currentDate = new Date();

            return !inputDate.before(currentDate);
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean validateTime(String time) {
        try {
            LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
