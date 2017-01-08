package com.example.laurabravopriegue.petiquiz;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ScoreRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL = "http://mfcfund.ml/petiapp/MaxScoreRegister.php";
    private Map<String, String> params;

    public ScoreRequest(int userid, int score, Response.Listener<String> listener){
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("userid", Integer.toString(userid));
        params.put("score", Integer.toString(score));
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
