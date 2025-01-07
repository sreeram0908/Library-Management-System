package library.management;

public class RegularBook extends Book {
    public RegularBook(String title, String author, String publicationDate, int copiesAvailable) {
        super(title, author, publicationDate, copiesAvailable);
    }

    @Override
    public void displayInfo() {
        System.out.println("Regular Book - Title: " + getTitle() + ", Author: " + getAuthor() + ", Published: " + getPublicationDate()+ ", Copies Available: " + getCopiesAvailable());
    }
}
