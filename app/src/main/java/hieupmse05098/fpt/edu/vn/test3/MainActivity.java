package hieupmse05098.fpt.edu.vn.test3;

import android.app.Dialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import model.MyTask;

public class MainActivity extends AppCompatActivity {

    private ArrayList<MyTask> taskList;
    private CustomAdapter adapter;
    private ListView listView;

    private String URL = "http://jsonplaceholder.typicode.com/todos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskList = new ArrayList();
        adapter = new CustomAdapter(getApplicationContext(), taskList);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showCustomDialog(position);
            }
        });

        GetDataTask getDataTask = new GetDataTask();
        getDataTask.execute(URL);
    }

    private void showCustomDialog(int pos) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);

        MyTask task = taskList.get(pos);

        TextView id = dialog.findViewById(R.id.id);
        TextView title = dialog.findViewById(R.id.title);
        TextView userId = dialog.findViewById(R.id.userId);
        TextView completed = dialog.findViewById(R.id.completed);
        Button dialogButtonOK = dialog.findViewById(R.id.dialogButtonOK);

        id.setText("ID: " + task.id);
        title.setText("Title: " + task.title);
        userId.setText("User ID: " + task.userId);
        completed.setText(task.completed ? "Completed" : "Not completed");

        dialogButtonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setTitle("Detail");
        dialog.show();
    }

    private class GetDataTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                String url = strings[0];
                String jsonString = getJsonStringFromUrl(url);
                JSONArray root = new JSONArray(jsonString);

                for (int i = 0; i < root.length(); i++) {
                    try {
                        JSONObject task = root.getJSONObject(i);
                        int id = Integer.parseInt(task.getString("id"));
                        int userId = Integer.parseInt(task.getString("userId"));
                        String title = task.getString("title");
                        boolean completed = task.getBoolean("completed");
                        MyTask newTask = new MyTask(userId, id, title, completed);
                        taskList.add(newTask);
                    } catch (NumberFormatException ex) {
                        ex.printStackTrace();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        // update ListView after each Task inserted to taskList
        @Override
        protected void onProgressUpdate(Void... values) {
            listView.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(String s) {
            listView.setAdapter(adapter);
        }

        // get raw JSON
        String getJsonStringFromUrl(String url) throws IOException {
            BufferedReader inputStream = null;

            URL jsonUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) jsonUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            inputStream = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));

            // read the JSON results into a string
            String line = null;
            StringBuffer jsonResult = new StringBuffer();
            while ((line = inputStream.readLine()) != null) {
                jsonResult.append(line);
            }
            return jsonResult.toString().trim();
        }
    }
}
