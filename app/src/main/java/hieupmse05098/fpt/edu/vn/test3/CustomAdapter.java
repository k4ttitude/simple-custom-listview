package hieupmse05098.fpt.edu.vn.test3;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import model.MyTask;

public class CustomAdapter extends ArrayAdapter<MyTask> {

    Context context;
    ArrayList<MyTask> taskList;

    public CustomAdapter(@NonNull Context context, @NonNull List<MyTask> objects) {
        super(context, R.layout.list_row, objects);
        this.context = context;
        this.taskList = (ArrayList<MyTask>) objects;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MyTask task = getItem(position);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_row, parent, false);
            TextView label = convertView.findViewById(R.id.label);
            label.setText(task.getId() + ". " + task.getTitle());
        }

        return convertView;
    }
}
