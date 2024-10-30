package com.example.muhtasi3_mybookwishlist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//BookArrayAdapter makes all book information follow the recyclerView format
public class BookArrayAdapter extends RecyclerView.Adapter<BookArrayAdapter.BookViewHolder> {

    private final ArrayList<Book> bookList;
    private final Context context;

    //A constructor for the adapter
    public BookArrayAdapter(Context context, ArrayList<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
    }

    //Displays Book in layout pattern accepted by recyclerView
    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);
        return new BookViewHolder(view);
    }

    //Binds each book with all information of individual book
    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, @SuppressLint("RecyclerView") int index) {
        Book currentBook = bookList.get(index);
        holder.titleTextView.setText(currentBook.getTitle());
        holder.authorTextView.setText(currentBook.getAuthor());
        holder.genreTextView.setText(currentBook.getGenre());
        holder.readOrUnreadTextView.setText(currentBook.isRead() ? "Read" : "Unread");

        // Book can be edited with a click on the book
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(context, AddEditBook.class);
                editIntent.putExtra("book", currentBook);
                editIntent.putExtra("index", index);
                ((MainActivity) context).startActivityForResult(editIntent, 2);
            }
        });

        // Book can be deleted with a long click on the book
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(context)
                        //To confirm deletion of book
                        .setTitle("Delete Book")
                        .setMessage("Are you sure you want to delete this book?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                bookList.remove(index);//removes the book and all its information
                                notifyDataSetChanged();//notifies the adapter about the deletion

                                ((MainActivity)context).updateBookCounts();//updates book count (In the MainActivity)
                            }
                        })
                        .setNegativeButton("No", null)//Not deleted if user clicks no
                        .show();

                return true;
            }
        });
    }

    //Returns total number of books in the book List
    @Override
    public int getItemCount() {
        return bookList.size();
    }

    //ViewHolder for the books
    static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, authorTextView, genreTextView, readOrUnreadTextView;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            authorTextView = itemView.findViewById(R.id.authorTextView);
            genreTextView = itemView.findViewById(R.id.genreTextView);
            readOrUnreadTextView = itemView.findViewById(R.id.readOrUnreadTextView);
        }
    }
}
