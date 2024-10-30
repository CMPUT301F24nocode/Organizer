package com.example.muhtasi3_mybookwishlist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//AddEditBook adds or edits a book in the app
public class AddEditBook extends AppCompatActivity {

    private EditText title, author, year, genre;
    private CheckBox readOrUnread;
    private Book editingBook;
    private int index = -1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_books);

        title = findViewById(R.id.BookTitleText);
        author = findViewById(R.id.AuthorText);
        genre = findViewById(R.id.genreText);
        year = findViewById(R.id.YearText);
        readOrUnread = findViewById(R.id.ReadOrUnread);
        Button saveButton = findViewById(R.id.saveButton);

        //Checks to see if user is adding or editing a book
        Intent intent = getIntent();
        if (intent.hasExtra("book")) {
            //If editing book, previous information in all fields retained
            editingBook = (Book) intent.getSerializableExtra("book");
            index = intent.getIntExtra("index", -1);
            title.setText(editingBook.getTitle());
            author.setText(editingBook.getAuthor());
            genre.setText(editingBook.getGenre());
            year.setText(String.valueOf(editingBook.getYear()));
            readOrUnread.setChecked(editingBook.isRead());
        }

        //Clicking button to save information inputted
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = AddEditBook.this.title.getText().toString();
                String author = AddEditBook.this.author.getText().toString();
                String genre = AddEditBook.this.genre.getText().toString();
                boolean isRead = readOrUnread.isChecked();

                //Input Validation for Title of Book
                if (title.isEmpty() || title.length() > 50) {
                    Toast.makeText(AddEditBook.this, "Title cannot be empty and cannot exceed 50 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Input Validation for Author Name
                if (author.isEmpty() || author.length() > 30) {
                    Toast.makeText(AddEditBook.this, "Author Name cannot be empty and cannot exceed 30 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Input Validation for Genre
                if (genre.isEmpty()) {
                    Toast.makeText(AddEditBook.this, "Genre Field cannot be Empty! e.g., Fiction, Non-Fiction, Mystery", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Input Validation for Year
                String yearString = AddEditBook.this.year.getText().toString();
                if (yearString.length() != 4 || !yearString.matches("\\d+")) {
                    Toast.makeText(AddEditBook.this, "Year must be a valid 4-digit number!", Toast.LENGTH_SHORT).show();
                    return;
                }
                int year = Integer.parseInt(yearString);

                //Adding/Updating fields of new/existing book
                if (editingBook != null) {
                    editingBook.setTitle(title);
                    editingBook.setAuthor(author);
                    editingBook.setGenre(genre);
                    editingBook.setYear(year);
                    editingBook.setRead(isRead);
                } else {
                    editingBook = new Book(title, author, genre, year, isRead);
                }

                //Result returned to MainActivity
                Intent result = new Intent();
                result.putExtra("book", editingBook);
                result.putExtra("index", index);//passing index to know if book is new or not
                setResult(RESULT_OK, result);
                finish();//Closing activity
            }
        });
    }
}
