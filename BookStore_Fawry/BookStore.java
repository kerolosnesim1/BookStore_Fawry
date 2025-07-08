import java.util.*;

class ShippingService {
  public static void shipping(String bookName, String address, int howMany) {
    System.out.println("Shipping " + howMany + " copies of '" + bookName + "' to: " + address);
  }
}

class MailService {
  public static void sendEmail(String bookName, String fileType, String email, int howMany) {
    System.out.println("Emailing " + howMany + " copies of '" + bookName + "' (" + fileType + " file) to: " + email);
  }
}

class BookNotAvailableException extends Exception {
  public BookNotAvailableException(String message) {
    super(message);
  }
}

abstract class Book {
  public String isbn;
  public String title;
  public int publishYear;
  public double price;
  public String author;

  public Book(String isbn, String title, int publishYear, double price, String author) {
    this.isbn = isbn;
    this.title = title;
    this.publishYear = publishYear;
    this.price = price;
    this.author = author;
  }

  public abstract boolean isAvailableForPurchase();

  public abstract void processPurchase(int quantity, String email, String address) throws BookNotAvailableException;

  public abstract boolean hasStock(int quantity);

  public String toString() {
    return "ISBN: " + isbn + ", Title: " + title + ", Author: " + author + ", Year: " + publishYear + ", Price: $"
        + price;
  }
}

class PaperBook extends Book {
  public int stock;

  public PaperBook(String isbn, String title, int publishYear, double price, String author, int stock) {
    super(isbn, title, publishYear, price, author);
    this.stock = stock;
  }

  public boolean isAvailableForPurchase() {
    return stock > 0;
  }

  public boolean hasStock(int quantity) {
    return stock >= quantity;
  }

  public void processPurchase(int quantity, String email, String address) throws BookNotAvailableException {
    if (!hasStock(quantity)) {
      throw new BookNotAvailableException(
          "Insufficient stock for " + title + ". Available: " + stock + ", Requested: " + quantity);
    }
    stock = stock - quantity;
    ShippingService.shipping(title, address, quantity);
  }

  public String toString() {
    return super.toString() + ", Stock: " + stock;
  }
}

class EBook extends Book {
  public String fileType;

  public EBook(String isbn, String title, int publishYear, double price, String author, String fileType) {
    super(isbn, title, publishYear, price, author);
    this.fileType = fileType;
  }

  public boolean isAvailableForPurchase() {
    return true;
  }

  public boolean hasStock(int quantity) {
    return true;
  }

  public void processPurchase(int quantity, String email, String address) throws BookNotAvailableException {
    MailService.sendEmail(title, fileType, email, quantity);
  }

  public String toString() {
    return super.toString() + ", File Type: " + fileType;
  }
}

class ShowcaseBook extends Book {

  public ShowcaseBook(String isbn, String title, int publishYear, double price, String author) {
    super(isbn, title, publishYear, price, author);
  }

  public boolean isAvailableForPurchase() {
    return false;
  }

  public boolean hasStock(int quantity) {
    return false;
  }

  public void processPurchase(int quantity, String email, String address) throws BookNotAvailableException {
    throw new BookNotAvailableException("Showcase book " + title + " is not for sale");
  }

  public String toString() {
    return super.toString() + " [SHOWCASE - NOT FOR SALE]";
  }
}

public class BookStore {
  public ArrayList<Book> inventory;

  public BookStore() {
    inventory = new ArrayList<Book>();
  }

  public void addBook(Book book) {
    inventory.add(book);
  }

  public ArrayList<Book> removeOutdatedBooks(int maxAgeInYears) {
    ArrayList<Book> removedBooks = new ArrayList<Book>();
    int currentYear = 2024;

    for (int i = 0; i < inventory.size(); i++) {
      Book book = inventory.get(i);
      int bookAge = currentYear - book.publishYear;
      if (bookAge > maxAgeInYears) {
        removedBooks.add(book);
        inventory.remove(i);
        i--;
      }
    }
    return removedBooks;
  }

  public double buyBook(String isbn, int quantity, String email, String address) throws BookNotAvailableException {
    Book foundBook = null;

    for (int i = 0; i < inventory.size(); i++) {
      if (inventory.get(i).isbn.equals(isbn)) {
        foundBook = inventory.get(i);
        break;
      }
    }

    if (foundBook == null) {
      throw new BookNotAvailableException("Book with ISBN " + isbn + " not found in inventory");
    }

    if (!foundBook.isAvailableForPurchase()) {
      throw new BookNotAvailableException("Book " + foundBook.title + " is not available for purchase");
    }

    if (!foundBook.hasStock(quantity)) {
      throw new BookNotAvailableException("Insufficient stock for " + foundBook.title);
    }

    foundBook.processPurchase(quantity, email, address);
    double totalAmount = foundBook.price * quantity;
    return totalAmount;
  }

  public void displayInventory() {
    for (int i = 0; i < inventory.size(); i++) {
      System.out.println(inventory.get(i).toString());
    }
  }

  public int getInventorySize() {
    return inventory.size();
  }
}