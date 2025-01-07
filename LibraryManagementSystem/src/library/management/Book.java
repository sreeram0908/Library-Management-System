package library.management;

public abstract class Book {
    private String title;
    private String author;
    private String publicationDate;
    private int copiesAvailable;

    public Book(String title, String author, String publicationDate, int copiesAvailable) {
        this.title = title;
        this.author = author;
        this.publicationDate = publicationDate;
        this.copiesAvailable = copiesAvailable;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getPublicationDate() { return publicationDate; }
    public int getCopiesAvailable() { return copiesAvailable; }

    public void setCopiesAvailable(int copiesAvailable) { this.copiesAvailable = copiesAvailable; }

    public abstract void displayInfo();

}
