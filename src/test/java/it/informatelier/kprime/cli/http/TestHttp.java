package it.informatelier.kprime.cli.http;

import com.google.gson.Gson;
import it.informatelier.kprime.cli.http.MpmHttpResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestHttp {

    @Test
    @Ignore
    public void testMavenHttp() throws IOException {
        OkHttpClient client = new OkHttpClient();
        String url="http://search.maven.org/solrsearch/select?q=g:%22com.google.code.gson%22+AND+a:%22gson%22&rows=20&wt=json";
        Request request = new Request.Builder()
                    .url(url)
                    .build();

        Response response = client.newCall(request).execute();
        Gson gson = new Gson();
        MpmHttpResponse mpmHttpResponse = gson.fromJson(response.body().string(), MpmHttpResponse.class);
        assertThat(mpmHttpResponse.getResponseHeader().getQTime(), is(12));
    }
}
