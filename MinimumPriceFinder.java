import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MinimumPriceFinder {

    public static void main(String[] args) {
        String filePath = "data.csv"; // Path to the CSV file

        // Create a Scanner object for user input
        Scanner scanner = new Scanner(System.in);

        // Prompt user for food items
        System.out.println("Enter food item names separated by spaces:");
        String input = scanner.nextLine();

        // Split the input into an array of food items
        String[] foodItems = input.trim().split("\\s+");

        // Find the minimum price based on user input
        String result = findMinimumPrice(filePath, foodItems);
        System.out.println(result);

        // Close the scanner
        scanner.close();
    }

    public static String findMinimumPrice(String filePath, String[] foodItems) {
        // To hold restaurant data
        Map<String, Map<String, Double>> restaurantData = new HashMap<>();

        // Read the CSV file and populate the restaurant data
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Skip the header
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String restaurantId = parts[0];

                // Initialize the restaurant's menu if it doesn't exist
                restaurantData.putIfAbsent(restaurantId, new HashMap<>());

                // Read the price for the restaurant
                double price = Double.parseDouble(parts[1]);

                // Add food items with their prices
                for (int i = 2; i < parts.length; i++) {
                    String foodItemName = parts[i].trim();
                    if (!foodItemName.isEmpty()) {
                        restaurantData.get(restaurantId).put(foodItemName, price);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error reading the file.";
        }

        // Variables to track the best option found
        String bestRestaurantId = null;
        double minTotalPrice = Double.MAX_VALUE;

        // Loop through each restaurant's menu
        for (Map.Entry<String, Map<String, Double>> entry : restaurantData.entrySet()) {
            String restaurantId = entry.getKey();
            Map<String, Double> menu = entry.getValue();
            double totalPrice = 0;
            boolean allItemsAvailable = true;

            // Calculate total price if all food items are available in the restaurant's
            // menu
            for (String foodItem : foodItems) {
                if (menu.containsKey(foodItem)) {
                    totalPrice += menu.get(foodItem);
                } else {
                    allItemsAvailable = false;
                    break; // Break if any item is not available
                }
            }

            // Update the best restaurant if all items are available and total price is less
            // than current minimum
            if (allItemsAvailable && totalPrice < minTotalPrice) {
                minTotalPrice = totalPrice;
                bestRestaurantId = restaurantId;
            }
        }

        // Return the result
        if (bestRestaurantId != null) {
            return bestRestaurantId + ", " + String.format("%.2f", minTotalPrice);
        } else {
            return "No matching restaurant found";
        }
    }
}
