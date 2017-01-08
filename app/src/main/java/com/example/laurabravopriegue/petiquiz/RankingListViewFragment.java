package com.example.laurabravopriegue.petiquiz;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;

public class RankingListViewFragment extends ListFragment {
    FragmentActivity faActivity;
    RelativeLayout llLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        faActivity = super.getActivity();
        llLayout = (RelativeLayout) inflater.inflate(R.layout.listview_ranking, container, false);

        RankingListViewFragment.DownloadQuestions downloadQuestions = new RankingListViewFragment.DownloadQuestions();
        downloadQuestions.execute();

        llLayout.findViewById(R.id.linLayout_quiz);
        return llLayout;
    }

    public void QuestionsLoaded(JSONArray response) throws JSONException {
        int len = response.length();
        ArrayList<Ranking> rankings = new ArrayList<Ranking>();
        for (int i = 0; i < len; i++) {
            JSONObject rankingJSON = response.getJSONObject(i);
            String name = rankingJSON.getString("username");
            Integer score = rankingJSON.getInt("maxscore");
            rankings.add(i, new Ranking(name, score));
        }
        // Construct the data source
        ArrayList<Ranking> arrayOfRankings = rankings;
        // Create the adapter to convert the array to views
        RankingAdapter adapter = new RankingAdapter(faActivity, arrayOfRankings);
        // Attach the adapter to a ListView
        getListView().setAdapter(adapter);
    }

    public class DownloadQuestions extends AsyncTask<Void, Void, JSONArray>
    {
        @Override
        public JSONArray doInBackground(Void... params)
        {
            String str="http://mfcfund.ml/petiapp/GetHighScores.php";
            URLConnection urlConn = null;
            BufferedReader bufferedReader = null;
            try
            {
                URL url = new URL(str);
                urlConn = url.openConnection();
                bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

                StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null)
                {
                    stringBuffer.append(line);
                }

                return new JSONArray(stringBuffer.toString());
            }
            catch(Exception ex)
            {
                Log.e("App", "yourDataTask", ex);
                return null;
            }
            finally
            {
                if(bufferedReader != null)
                {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        public void onPostExecute(JSONArray response)
        {
            if(response != null)
            {
                try {
                    QuestionsLoaded(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


