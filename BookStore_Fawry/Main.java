import java.util.*;

public class Main {

  public static void buyBook(BookStore store, String isbn, int num, String email, String address) {
    try {
      double cost = store.buyBook(isbn, num, email, address);
      System.out.println("bought it! cost was $" + cost);
    } catch (BookNotAvailableException e) {
      System.out.println("couldnt buy: " + e.getMessage());
    }
  }

  public static void main(String[] args) {
    BookStore store = new BookStore();

    PaperBook book1 = new PaperBook("book1", "java stuff", 2020, 25.0, "some guy", 5);
    EBook book2 = new EBook("book2", "python thing", 2021, 20.0, "mary", "pdf");
    ShowcaseBook book3 = new ShowcaseBook("book3", "demo", 2023, 50.0, "bob");

    store.addBook(book1);
    store.addBook(book2);
    store.addBook(book3);

    System.out.println("here are the books:");
    store.displayInventory();

    System.out.println();
    buyBook(store, "book1", 2, "me@email.com", "my house");

    System.out.println();
    System.out.println("books now:");
    store.displayInventory();
  }
}