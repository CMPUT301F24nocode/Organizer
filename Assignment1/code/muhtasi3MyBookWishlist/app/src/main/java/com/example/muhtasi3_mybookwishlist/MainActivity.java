package com.example.muhtasi3_mybookwishlist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

//MainActivity is the main class entry point of the app.
public class MainActivity extends AppCompatActivity {

    private BookArrayAdapter bookArrayAdapter;
    private ArrayList<Book> bookList;
    private TextView totalBooks, totalReadBooks;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookList = new ArrayList<>();//An arraylist to contain the book information

        //Recycler view allows to scroll through the list of books
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Adapter defined to follow the recyclerView format
        bookArrayAdapter = new BookArrayAdapter(this, bookList);
        recyclerView.setAdapter(bookArrayAdapter);

        //TextView defined to show total books added and the books already read from them
        totalBooks = findViewById(R.id.totalBooks);
        totalReadBooks = findViewById(R.id.totalReadBooks);
        updateBookCounts();//this updates the book counts

        //Button defined to click and add new book information
        FloatingActionButton addBookButton = findViewById(R.id.addBookButton);
        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditBook.class);//Opens AddEditBook when button clicked
                startActivityForResult(intent, 1); // 1 for adding a new book
            }
        });
    }

    /* Works on the result from AddEditBook:
     * Add a new Book and its information
     * Edit an existing book and its information
     */
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Book book = (Book) data.getSerializableExtra("book");

            if (requestCode == 1) {
                bookList.add(book);
                bookArrayAdapter.notifyDataSetChanged();
            } else if (requestCode == 2) {// 2 for editing an existing book
                int index = data.getIntExtra("index", -1);
                if (index != -1) {
                    bookList.set(index, book);
                    bookArrayAdapter.notifyItemChanged(index);//notifying for any changes made in an existing book
                }
            }
            updateBookCounts();//updating book count after adding or editing a book
        }
    }

    @SuppressLint("DefaultLocale")
    //Updates book count
    void updateBookCounts() {
        totalBooks.setText(String.format("Total Books: %d", bookList.size()));//Displays Total books added

        //Calculating books read
        long readCount = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            readCount = bookList.stream().filter(Book::isRead).count();
        }
        totalReadBooks.setText(String.format("Books Read: %d", readCount));//Displays Total books read
    }
}