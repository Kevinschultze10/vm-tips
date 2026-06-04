import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// ============================================================
// HJÄLPKLASS: En bok har titel + författare som "nyckel"
// Vi behöver en klass så att vi kan jämföra böcker med
// equals() och hashCode() — annars fungerar inte HashMap.
// ============================================================
class Book {
    private String title;
    private String author;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public String getTitle()  { return title; }
    public String getAuthor() { return author; }

    // equals() och hashCode() måste överskuggas så att
    // två Book-objekt med samma titel+författare anses vara lika.
    // HashMap använder hashCode för att hitta rätt "hink",
    // och equals för att kolla om nyckeln verkligen matchar.
    @Override
    public boolean equals(Object other) {
        if (other instanceof Book b) {
            return title.equals(b.title) && author.equals(b.author);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(title, author);
    }
}

public class BockerGUI extends Application {

    // ============================================================
    // INSTANSVARIABEL: En Map som lagrar böcker och deras betyg.
    // Nyckel = Book-objekt (titel + författare)
    // Värde  = betyg (int)
    // Vi deklarerar den här så att alla knapp-handlers
    // kan komma åt den (de körs senare, utanför start()).
    // ============================================================
    private Map<Book, Integer> books = new HashMap<>();

    @Override
    public void start(Stage primaryStage) throws IOException {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        VBox center = new VBox(5);
        center.setPadding(new Insets(20));
        center.setAlignment(Pos.CENTER);

        center.getChildren().add(new Label("Boktitel:"));
        TextField titleField = new TextField();
        titleField.setMaxWidth(200);
        center.getChildren().add(titleField);

        center.getChildren().add(new Label("Författare:"));
        TextField authorField = new TextField();
        authorField.setMaxWidth(200);
        center.getChildren().add(authorField);

        center.getChildren().add(new Label("Betyg (1-10):"));
        TextField ratingField = new TextField();
        ratingField.setMaxWidth(100);
        center.getChildren().add(ratingField);
        root.setCenter(center);

        VBox bottom = new VBox();
        HBox buttons = new HBox(5);
        buttons.setPadding(new Insets(10));

        // ============================================================
        // Vi skapar knapparna som variabler så att vi kan
        // sätta setOnAction på dem (istället för new Button direkt).
        // ============================================================
        Button saveBtn    = new Button("Spara");
        Button showBtn    = new Button("Visa");
        Button showAllBtn = new Button("Visa alla");
        Button removeBtn  = new Button("Ta bort");

        buttons.getChildren().addAll(saveBtn, showBtn, showAllBtn, removeBtn);
        bottom.getChildren().add(buttons);
        root.setBottom(bottom);

        // ============================================================
        // SPARA-knappen (uppgift a)
        // Läser titel, författare och betyg från textfälten.
        // Skapar ett Book-objekt och lägger in det i mappen.
        // put() uppdaterar betyget automatiskt om boken redan finns
        // (det är HashMap:s beteende — gamla värdet skrivs över).
        // ============================================================
        saveBtn.setOnAction(e -> {
            String title  = titleField.getText();
            String author = authorField.getText();
            int rating    = Integer.parseInt(ratingField.getText());

            Book book = new Book(title, author);
            books.put(book, rating); // uppdaterar om boken redan finns
        });

        // ============================================================
        // VISA-knappen (uppgift b)
        // Skapar ett Book-objekt med titel+författare och letar upp
        // det i mappen. Om boken finns visas betyg + författare
        // i en Alert-dialog. Om boken inte finns — gör ingenting.
        // ============================================================
        showBtn.setOnAction(e -> {
            String title  = titleField.getText();
            String author = authorField.getText();
            Book key = new Book(title, author);

            if (books.containsKey(key)) {
                int rating = books.get(key);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Författare: " + author + "\nBetyg: " + rating);
                alert.show();
            }
            // Om boken inte finns: gör ingenting (uppgiften säger så)
        });

        // ============================================================
        // VISA ALLA-knappen (uppgift c)
        // Itererar över hela mappen med entrySet().
        // Bygger upp en sträng med alla böcker och visar i en Alert.
        // entrySet() ger oss par av (nyckel, värde) — dvs (Book, int).
        // ============================================================
        showAllBtn.setOnAction(e -> {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<Book, Integer> entry : books.entrySet()) {
                Book b = entry.getKey();
                int rating = entry.getValue();
                sb.append(b.getTitle())
                  .append(" av ")
                  .append(b.getAuthor())
                  .append(" — Betyg: ")
                  .append(rating)
                  .append("\n");
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(sb.toString());
            alert.show();
        });

        // ============================================================
        // TA BORT-knappen (uppgift d)
        // Skapar ett Book-objekt med titel+författare och tar bort
        // det från mappen med remove(). Fungerar tack vare att
        // vi implementerade equals() och hashCode() i Book-klassen.
        // ============================================================
        removeBtn.setOnAction(e -> {
            String title  = titleField.getText();
            String author = authorField.getText();
            Book key = new Book(title, author);
            books.remove(key);
        });

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
