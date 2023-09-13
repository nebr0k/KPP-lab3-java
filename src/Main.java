import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Store implements Serializable {
    private String name;
    private String address;
    private List<String> phones;
    private String specialization;
    private String workingHours;

    public Store(String name, String address, String specialization, String workingHours) {
        this.name = name;
        this.address = address;
        this.specialization = specialization;
        this.workingHours = workingHours;
        this.phones = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addPhone(String phone) {
        phones.add(phone);
    }

    @Override
    public String toString() {
        return "Store{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phones=" + phones +
                ", specialization='" + specialization + '\'' +
                ", workingHours='" + workingHours + '\'' +
                '}';
    }
}

class StoreContainer implements Iterable<Store>, Serializable {
    private List<Store> stores;

    public StoreContainer() {
        stores = new ArrayList<>();
    }

    public void addStore(Store store) {
        stores.add(store);
    }

    public void removeStoreByName(String storeName) {
        stores.removeIf(store -> store.getName().equalsIgnoreCase(storeName));
    }

    @Override
    public java.util.Iterator<Store> iterator() {
        return stores.iterator();
    }
}

public class Main {
    public static void main(String[] args) {
        StoreContainer storeContainer = loadStoreContainer();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Menu:");
            System.out.println("1. Add a new store");
            System.out.println("2. View list of stores");
            System.out.println("3. Delete store by name");
            System.out.println("4. Exit program");
            System.out.print("Select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear the buffer

            switch (choice) {
                case 1:
                    System.out.print("Store name: ");
                    String name = scanner.nextLine();
                    System.out.print("Address: ");
                    String address = scanner.nextLine();
                    System.out.print("Specialization: ");
                    String specialization = scanner.nextLine();
                    System.out.print("Working hours: ");
                    String workingHours = scanner.nextLine();

                    Store newStore = new Store(name, address, specialization, workingHours);

                    System.out.print("Add phone number (Y/N)? ");
                    String addPhoneChoice = scanner.nextLine();
                    while (addPhoneChoice.equalsIgnoreCase("Y")) {
                        System.out.print("Phone number: ");
                        String phone = scanner.nextLine();
                        newStore.addPhone(phone);
                        System.out.print("Add another phone number (Y/N)? ");
                        addPhoneChoice = scanner.nextLine();
                    }

                    storeContainer.addStore(newStore);
                    System.out.println("Store added!");
                    break;

                case 2:
                    System.out.println("List of stores:");
                    for (Store store : storeContainer) {
                        System.out.println(store);
                    }
                    break;

                case 3:
                    System.out.print("Enter the store name to delete: ");
                    String storeNameToDelete = scanner.nextLine();
                    storeContainer.removeStoreByName(storeNameToDelete);
                    System.out.println("Store(s) with name " + storeNameToDelete + " deleted (if found).");
                    break;

                case 4:
                    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("stores.dat"))) {
                        oos.writeObject(storeContainer);
                        System.out.println("Data successfully saved to file stores.dat");
                    } catch (IOException e) {
                        System.err.println("Error saving data to file: " + e.getMessage());
                    }

                    System.out.println("Program terminated.");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
                    break;
            }
        }
    }

    private static StoreContainer loadStoreContainer() {
        StoreContainer container;
        File file = new File("stores.dat");
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                container = (StoreContainer) ois.readObject();
                System.out.println("Data loaded successfully from stores.dat");
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading data from stores.dat. Starting with an empty container.");
                container = new StoreContainer();
            }
        } else {
            System.out.println("stores.dat not found. Starting with an empty container.");
            container = new StoreContainer();
        }
        return container;
    }
}
