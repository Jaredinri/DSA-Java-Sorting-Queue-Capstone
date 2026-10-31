import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Scanner;


public class SitioFatimaRestaurant {



    static Scanner input = new Scanner(System.in);


    static ArrayList<Integer> orderNumbers  = new ArrayList<>();
    static ArrayList<String>  customerNames = new ArrayList<>();
    static ArrayList<String>  orderDetails  = new ArrayList<>();
    static ArrayList<Double>  totalAmounts  = new ArrayList<>();
    static ArrayList<String>  statuses      = new ArrayList<>();   


    static Queue<Integer> prepQueue = new LinkedList<>();

    static int nextOrderNumber = 1001;   
    static int swapCount = 0;            

    
    static final String[] MENU_ITEMS = {
        "Chicken Adobo with Rice", "Pork Sinigang", "Pancit Canton", "Lumpia (5 pcs)",
        "Fried Chicken with Rice", "Halo-Halo", "Iced Tea", "Bottled Water"
    };
    static final double[] MENU_PRICES = { 85.00, 95.00, 70.00, 50.00, 90.00, 60.00, 30.00, 20.00 };



    public static void main(String[] args) {
        loadSampleOrders(); 

        boolean running = true;
        while (running) {
            showMainMenu();
            int choice = readInt("Enter your choice: ", 0, 8);

            switch (choice) {
                case 1: takeNewOrder();       break;
                case 2: displayMenuOptions(); break;
                case 3: searchMenu();         break;
                case 4: sortMenu();           break;
                case 5: queueMenu();          break;
                case 6: updateCustomerName(); break;
                case 7: removeOrder();        break;
                case 8: showSortingInfo();    break;
                case 0:
                    running = false;
                    break;
            }
        }
        System.out.println("\nThank you for using the Sitio Fatima Restaurant system. Goodbye!");
        input.close();
    }

    static void showMainMenu() {
        System.out.println("\n==========================================");
        System.out.println("     SITIO FATIMA RESTAURANT");
        System.out.println("  Food Ordering & Preparation Queue System");
        System.out.println("==========================================");
        System.out.println(" 1. Take a new order (adds to queue)");
        System.out.println(" 2. Display orders");
        System.out.println(" 3. Search for an order");
        System.out.println(" 4. Sort orders");
        System.out.println(" 5. Kitchen queue (prepare / peek / view)");
        System.out.println(" 6. Update a customer name");
        System.out.println(" 7. Remove / cancel an order");
        System.out.println(" 8. About the sorting algorithms");
        System.out.println(" 0. Exit");
        System.out.println("------------------------------------------");
    }



    static String readLine(String prompt) {
        System.out.print(prompt);
        try {
            return input.nextLine().trim();
        } catch (NoSuchElementException e) {
            System.out.println("\nInput closed. Exiting program.");
            System.exit(0);
            return "";
        }
    }


    static int readInt(String prompt, int min, int max) {
        while (true) {
            String text = readLine(prompt);
            try {
                int value = Integer.parseInt(text);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Invalid choice. Please enter a number from " + min + " to " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

    
    static String readNonEmpty(String prompt) {
        while (true) {
            String text = readLine(prompt);
            if (!text.isEmpty()) {
                return text;
            }
            System.out.println("This field cannot be empty.");
        }
    }

    
    static boolean readYesNo(String prompt) {
        while (true) {
            String text = readLine(prompt + " (y/n): ").toLowerCase();
            if (text.equals("y") || text.equals("yes")) return true;
            if (text.equals("n") || text.equals("no"))  return false;
            System.out.println("Please type y or n.");
        }
    }

    

    
    static void addRecord(int number, String name, String details, double total, String status) {
        orderNumbers.add(number);
        customerNames.add(name);
        orderDetails.add(details);
        totalAmounts.add(total);
        statuses.add(status);
    }

    
    static void removeRecordAt(int index) {
        orderNumbers.remove(index);
        customerNames.remove(index);
        orderDetails.remove(index);
        totalAmounts.remove(index);
        statuses.remove(index);
    }

    
    static void swapRecords(int i, int j) {
        int tempNumber = orderNumbers.get(i);
        orderNumbers.set(i, orderNumbers.get(j));
        orderNumbers.set(j, tempNumber);

        String tempName = customerNames.get(i);
        customerNames.set(i, customerNames.get(j));
        customerNames.set(j, tempName);

        String tempDetails = orderDetails.get(i);
        orderDetails.set(i, orderDetails.get(j));
        orderDetails.set(j, tempDetails);

        double tempTotal = totalAmounts.get(i);
        totalAmounts.set(i, totalAmounts.get(j));
        totalAmounts.set(j, tempTotal);

        String tempStatus = statuses.get(i);
        statuses.set(i, statuses.get(j));
        statuses.set(j, tempStatus);

        swapCount++;
    }

    
    static int findIndexByOrderNumber(int number) {
        for (int i = 0; i < orderNumbers.size(); i++) {
            if (orderNumbers.get(i) == number) {
                return i;
            }
        }
        return -1;
    }

    static void loadSampleOrders() {
        
        addRecord(nextOrderNumber, "Jomar De Castro", "2x Chicken Adobo with Rice, 1x Iced Tea", 200.00, "Pending");
        prepQueue.add(nextOrderNumber++);
        addRecord(nextOrderNumber, "Kenneth C. Valerio", "1x Pork Sinigang, 1x Bottled Water", 115.00, "Pending");
        prepQueue.add(nextOrderNumber++);
        addRecord(nextOrderNumber, "Joaquin Pino", "3x Halo-Halo", 180.00, "Pending");
        prepQueue.add(nextOrderNumber++);
        addRecord(nextOrderNumber, "Zared Husmillo", "1x Pancit Canton, 2x Lumpia (5 pcs)", 170.00, "Pending");
        prepQueue.add(nextOrderNumber++);
    }

    

    static void takeNewOrder() {
        System.out.println("\n--- NEW ORDER ---");
        String name = readNonEmpty("Customer name: ");

        StringBuilder details = new StringBuilder();
        double total = 0;
        int itemsAdded = 0;

        boolean ordering = true;
        while (ordering) {
            System.out.println("\nMENU");
            for (int i = 0; i < MENU_ITEMS.length; i++) {
                System.out.printf(" %d. %-26s PHP %.2f%n", (i + 1), MENU_ITEMS[i], MENU_PRICES[i]);
            }
            System.out.println(" 0. Done ordering");

            int item = readInt("Choose an item (0 to finish): ", 0, MENU_ITEMS.length);
            if (item == 0) {
                ordering = false;
            } else {
                int qty = readInt("Quantity (1-20): ", 1, 20);
                if (itemsAdded > 0) {
                    details.append(", ");
                }
                details.append(qty).append("x ").append(MENU_ITEMS[item - 1]);
                total += qty * MENU_PRICES[item - 1];
                itemsAdded++;
                System.out.printf("Added. Current total: PHP %.2f%n", total);
            }
        }

        if (itemsAdded == 0) {
            System.out.println("No items selected. Order was not created.");
            return;
        }

        int number = nextOrderNumber++;
        addRecord(number, name, details.toString(), total, "Pending");
        prepQueue.add(number);   

        System.out.println("\nOrder #" + number + " recorded for " + name + ".");
        System.out.printf("Total: PHP %.2f%n", total);
        System.out.println("Added to the preparation queue. Position in line: " + prepQueue.size());
    }

    

    static void displayMenuOptions() {
        System.out.println("\n--- DISPLAY ORDERS ---");
        System.out.println(" 1. All orders");
        System.out.println(" 2. Pending orders only");
        System.out.println(" 3. Completed orders only");
        System.out.println(" 0. Back");
        int choice = readInt("Enter your choice: ", 0, 3);

        if (choice == 1) displayOrders("ALL ORDERS", null);
        if (choice == 2) displayOrders("PENDING ORDERS", "Pending");
        if (choice == 3) displayOrders("COMPLETED ORDERS", "Completed");
    }

    
    static void displayOrders(String title, String statusFilter) {
        System.out.println("\n=== " + title + " ===");
        if (orderNumbers.isEmpty()) {
            System.out.println("There are no orders recorded yet.");
            return;
        }

        printTableHeader();
        int shown = 0;
        double runningTotal = 0;
        for (int i = 0; i < orderNumbers.size(); i++) {
            if (statusFilter == null || statuses.get(i).equals(statusFilter)) {
                printRow(i);
                runningTotal += totalAmounts.get(i);
                shown++;
            }
        }

        if (shown == 0) {
            System.out.println("No " + statusFilter.toLowerCase() + " orders found.");
        } else {
            System.out.println("------------------------------------------------------------------------------------------");
            System.out.printf("Orders shown: %d | Combined amount: PHP %.2f%n", shown, runningTotal);
        }
    }

    static void printTableHeader() {
        System.out.printf("%-8s %-18s %-42s %10s  %-9s%n", "Order #", "Customer", "Items", "Total", "Status");
        System.out.println("------------------------------------------------------------------------------------------");
    }

    static void printRow(int i) {
        String details = orderDetails.get(i);
        if (details.length() > 41) {
            details = details.substring(0, 38) + "...";
        }
        System.out.printf("%-8d %-18s %-42s %10.2f  %-9s%n",
                orderNumbers.get(i), customerNames.get(i), details, totalAmounts.get(i), statuses.get(i));
    }

    

    static void searchMenu() {
        System.out.println("\n--- SEARCH ---");
        if (orderNumbers.isEmpty()) {
            System.out.println("There are no orders to search.");
            return;
        }
        System.out.println(" 1. Search by order number");
        System.out.println(" 2. Search by customer name (partial match allowed)");
        System.out.println(" 0. Back");
        int choice = readInt("Enter your choice: ", 0, 2);

        if (choice == 1) {
            int number = readInt("Enter order number: ", 1, Integer.MAX_VALUE);
            int index = findIndexByOrderNumber(number);
            if (index == -1) {
                System.out.println("No order found with number " + number + ".");
            } else {
                System.out.println();
                printTableHeader();
                printRow(index);
                if (statuses.get(index).equals("Pending")) {
                    int place = queuePositionOf(number);
                    System.out.println("This order is #" + place + " in the kitchen queue.");
                }
            }
        } else if (choice == 2) {
            String keyword = readNonEmpty("Enter name or part of a name: ").toLowerCase();
            int found = 0;
            for (int i = 0; i < customerNames.size(); i++) {
                if (customerNames.get(i).toLowerCase().contains(keyword)) {
                    if (found == 0) {
                        System.out.println();
                        printTableHeader();
                    }
                    printRow(i);
                    found++;
                }
            }
            if (found == 0) {
                System.out.println("No customer matching \"" + keyword + "\" was found.");
            } else {
                System.out.println(found + " record(s) found.");
            }
        }
    }

    
    static int queuePositionOf(int number) {
        int position = 1;
        for (int queued : prepQueue) {   
            if (queued == number) {
                return position;
            }
            position++;
        }
        return -1;
    }

    

    static void sortMenu() {
        System.out.println("\n--- SORT ORDERS ---");
        if (orderNumbers.isEmpty()) {
            System.out.println("There are no orders to sort.");
            return;
        }
        if (orderNumbers.size() == 1) {
            System.out.println("Only one order is recorded - nothing to sort.");
            return;
        }

        System.out.println(" 1. Bubble Sort    -> by order number");
        System.out.println(" 2. Insertion Sort -> by customer name");
        System.out.println(" 3. Selection Sort -> by total amount");
        System.out.println(" 0. Back");
        int choice = readInt("Enter your choice: ", 0, 3);
        if (choice == 0) {
            return;
        }

        System.out.println(" 1. Ascending (low to high / A to Z)");
        System.out.println(" 2. Descending (high to low / Z to A)");
        boolean ascending = readInt("Sort direction: ", 1, 2) == 1;

        System.out.println("\nBEFORE SORTING:");
        displayOrders("ALL ORDERS", null);

        swapCount = 0;
        String used = "";
        if (choice == 1) {
            bubbleSortByOrderNumber(ascending);
            used = "Bubble Sort (order number)";
        } else if (choice == 2) {
            insertionSortByName(ascending);
            used = "Insertion Sort (customer name)";
        } else {
            selectionSortByTotal(ascending);
            used = "Selection Sort (total amount)";
        }

        System.out.println("\nAFTER SORTING with " + used + ":");
        displayOrders("ALL ORDERS", null);
        System.out.println("Swaps performed: " + swapCount);
        System.out.println("(The kitchen queue is not affected - it still follows first-come, first-served.)");
    }

    
    static void bubbleSortByOrderNumber(boolean ascending) {
        int n = orderNumbers.size();
        for (int pass = 0; pass < n - 1; pass++) {
            boolean swapped = false;
            for (int j = 0; j < n - 1 - pass; j++) {
                int left = orderNumbers.get(j);
                int right = orderNumbers.get(j + 1);
                boolean outOfOrder = ascending ? (left > right) : (left < right);
                if (outOfOrder) {
                    swapRecords(j, j + 1);
                    swapped = true;
                }
            }
            if (!swapped) {
                break;
            }
        }
    }

    
    static void insertionSortByName(boolean ascending) {
        int n = customerNames.size();
        for (int i = 1; i < n; i++) {
            int j = i;
            while (j > 0) {
                int comparison = customerNames.get(j - 1).compareToIgnoreCase(customerNames.get(j));
                boolean outOfOrder = ascending ? (comparison > 0) : (comparison < 0);
                if (!outOfOrder) {
                    break;
                }
                swapRecords(j - 1, j);
                j--;
            }
        }
    }

    
    static void selectionSortByTotal(boolean ascending) {
        int n = totalAmounts.size();
        for (int i = 0; i < n - 1; i++) {
            int best = i;
            for (int j = i + 1; j < n; j++) {
                boolean better = ascending
                        ? (totalAmounts.get(j) < totalAmounts.get(best))
                        : (totalAmounts.get(j) > totalAmounts.get(best));
                if (better) {
                    best = j;
                }
            }
            if (best != i) {
                swapRecords(i, best);
            }
        }
    }

    

    static void queueMenu() {
        boolean inQueueMenu = true;
        while (inQueueMenu) {
            System.out.println("\n--- KITCHEN PREPARATION QUEUE (FIFO) ---");
            System.out.println(" 1. Prepare next order (dequeue)");
            System.out.println(" 2. Peek at next order");
            System.out.println(" 3. Display current queue");
            System.out.println(" 4. Check if queue is empty");
            System.out.println(" 0. Back to main menu");
            int choice = readInt("Enter your choice: ", 0, 4);

            switch (choice) {
                case 1: prepareNextOrder(); break;
                case 2: peekNextOrder();    break;
                case 3: displayQueue();     break;
                case 4:
                    if (prepQueue.isEmpty()) {
                        System.out.println("Yes, the queue is empty. No pending orders in the kitchen.");
                    } else {
                        System.out.println("No, there are " + prepQueue.size() + " order(s) waiting.");
                    }
                    break;
                case 0: inQueueMenu = false; break;
            }
        }
    }

    
    static void prepareNextOrder() {
        if (prepQueue.isEmpty()) {
            System.out.println("The queue is empty. There is no order to prepare.");
            return;
        }
        int number = prepQueue.poll();               
        int index = findIndexByOrderNumber(number);  

        if (index == -1) {
            System.out.println("Error: order #" + number + " was in the queue but has no record.");
            return;
        }
        statuses.set(index, "Completed");
        System.out.println("\nNow preparing Order #" + number + " for " + customerNames.get(index));
        System.out.println("Items: " + orderDetails.get(index));
        System.out.println("Order marked as COMPLETED. Orders still waiting: " + prepQueue.size());
    }

    
    static void peekNextOrder() {
        if (prepQueue.isEmpty()) {
            System.out.println("The queue is empty. Nothing to peek at.");
            return;
        }
        int number = prepQueue.peek();
        int index = findIndexByOrderNumber(number);
        System.out.println("\nNext order to prepare (not removed from queue):");
        printTableHeader();
        printRow(index);
    }

    static void displayQueue() {
        if (prepQueue.isEmpty()) {
            System.out.println("The queue is empty. No pending orders.");
            return;
        }
        System.out.println("\nFRONT -> BACK (first in, first out)");
        System.out.printf("%-6s %-8s %-18s %10s%n", "Place", "Order #", "Customer", "Total");
        System.out.println("--------------------------------------------");
        int place = 1;
        for (int number : prepQueue) {
            int index = findIndexByOrderNumber(number);
            System.out.printf("%-6d %-8d %-18s %10.2f%n",
                    place, number, customerNames.get(index), totalAmounts.get(index));
            place++;
        }
    }

    

    static void updateCustomerName() {
        System.out.println("\n--- UPDATE CUSTOMER NAME ---");
        if (orderNumbers.isEmpty()) {
            System.out.println("There are no orders to update.");
            return;
        }
        int number = readInt("Enter order number: ", 1, Integer.MAX_VALUE);
        int index = findIndexByOrderNumber(number);
        if (index == -1) {
            System.out.println("No order found with number " + number + ".");
            return;
        }
        System.out.println("Current name: " + customerNames.get(index));
        String newName = readNonEmpty("New name: ");
        customerNames.set(index, newName);
        System.out.println("Customer name updated.");
    }

    

    static void removeOrder() {
        System.out.println("\n--- REMOVE / CANCEL ORDER ---");
        if (orderNumbers.isEmpty()) {
            System.out.println("There are no orders to remove.");
            return;
        }
        int number = readInt("Enter order number: ", 1, Integer.MAX_VALUE);
        int index = findIndexByOrderNumber(number);
        if (index == -1) {
            System.out.println("No order found with number " + number + ".");
            return;
        }
        printTableHeader();
        printRow(index);
        if (!readYesNo("Remove this order?")) {
            System.out.println("Removal cancelled.");
            return;
        }

        removeRecordAt(index);
        
        boolean wasQueued = prepQueue.remove(Integer.valueOf(number));
        System.out.println("Order #" + number + " removed" + (wasQueued ? " and taken out of the kitchen queue." : "."));
    }


    static void showSortingInfo() {
        System.out.println("\n=== WHY THESE SORTING ALGORITHMS? ===");
        System.out.println("\n1. BUBBLE SORT  (used on: order number)");
        System.out.println("   Purpose : Compares neighbouring records and swaps them when out of order.");
        System.out.println("   Why here: Orders are created in increasing number, so the list is usually");
        System.out.println("             almost sorted. Bubble Sort stops early after a pass with no swaps,");
        System.out.println("             so it is fast on nearly-sorted data and easy to trace.");
        System.out.println("\n2. INSERTION SORT  (used on: customer name)");
        System.out.println("   Purpose : Inserts each record into its correct place among the earlier ones.");
        System.out.println("   Why here: A small restaurant has a small, constantly growing list. Insertion");
        System.out.println("             Sort works well on small lists and keeps equal names in the order");
        System.out.println("             they were taken (stable), so same-name orders stay in arrival order.");
        System.out.println("\n3. SELECTION SORT  (used on: total amount)");
        System.out.println("   Purpose : Repeatedly selects the smallest/largest remaining amount.");
        System.out.println("   Why here: It makes the fewest swaps. Every swap moves five parallel lists,");
        System.out.println("             so fewer swaps means less work. Useful for finding the biggest");
        System.out.println("             or smallest orders for the day.");
        System.out.println("\nWHY A QUEUE?");
        System.out.println("   Kitchen orders must be cooked first-come, first-served (FIFO). The queue holds");
        System.out.println("   order NUMBERS, so sorting the records never changes who is served next.");
    }
}