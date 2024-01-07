package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();
    private static int partId = 0;
    private static int productId = 0;

    private static Inventory instance = new Inventory();

    private Inventory() {

    }

    public static int getNewPartId() {
        return ++partId;
    }
    public static int getNewProductId() {
        return ++productId;
    }


    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    public static int nextPartId() {
        //Return next ID without incrementing it
        return partId+1;
    }

    public static int nextProductId() {
        return productId+1;
    }


    public ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> foundParts = FXCollections.observableArrayList();
        for (Part part : allParts) {
            if (part.getName().toLowerCase().contains(partName.toLowerCase())) {
                foundParts.add(part);
            }
        }
        return foundParts;
    }

    public void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    public void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }

    public static boolean deletePart(Part selectedPart) {
        return allParts.remove(selectedPart);
    }

    public static boolean deleteProduct(Product selectedProduct) {
        return allProducts.remove(selectedProduct);
    }

    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    public static Inventory getInstance() {
        return instance;
    }
}
