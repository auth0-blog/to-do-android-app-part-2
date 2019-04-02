package com.auth0.todo.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.auth0.todo.R;
import com.auth0.todo.ToDoItem;

import java.util.ArrayList;
import java.util.List;

public class ToDoListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<ToDoItem> toDoList = new ArrayList<>();

    public ToDoListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ToDoItem toDoItem = (ToDoItem) getItem(position);
        if (view == null) {
            view = inflater.inflate(R.layout.to_do_item, null);
        }

        TextView textView = view.findViewById(R.id.to_do_message);
        textView.setText(toDoItem.getMessage());

        return view;
    }

    @Override
    public Object getItem(int position) {
        return toDoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return toDoList.size();
    }

    public void setToDoList(List<ToDoItem> toDoList) {
        this.toDoList = toDoList;
        notifyDataSetChanged();
    }
}