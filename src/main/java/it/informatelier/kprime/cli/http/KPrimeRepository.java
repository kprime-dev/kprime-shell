package it.informatelier.kprime.cli.http;

import com.google.gson.Gson;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class KPrimeRepository implements ModelRepository {
    LocalDateTime localDateTime = LocalDateTime.now();
    String kprimeAddress = "http://localhost:7000";
    String contextName = "kprime";

    @Override
    public ModelResponse ask(ModelRequest request) {
        String question = request.getQuestion();
        if (question.startsWith("POST ")) {
            return new ModelResponse(askKPrime("post", question.substring(5)).getResponse());
        } else if (question.startsWith("PUT ")) {
                return new ModelResponse(askKPrime("put",question.substring(4)).getResponse());
        } else {
            return new ModelResponse(askKPrime("get",question.substring(4)).getResponse());
        }
    }

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    // https://mkyong.com/java/java-11-httpclient-examples/

    private KPrimeDTO askKPrime(String requestType, String request) {
        //System.out.println(this.getClass().getName()+":askKPrime ["+request+"]");
        KPrimeDTO kPrimeDTO = new KPrimeDTO();
        kPrimeDTO.setRequest(request);
        // if cache is good return from cache;
        //if (!isFetchPortfolio(kPrimeDTO)) return kPrimeDTO;

        // try do a remote request to get it.
        // POST /parse command=add-table Address:city,street
        try {
            //System.out.println(this.getClass().getName() + " mapping request.");
            //Map<Object, Object> data = new HashMap<>();
            //data.put("command", request);
            //System.out.println("KPRIME [" + requestType + "] request:[" + request + "]");
            HttpRequest.BodyPublisher data = HttpRequest.BodyPublishers.ofString(request);
            HttpRequest httpRequest = null;
            if (requestType.equals("post")) {
                String requestUri = kprimeAddress + "/parse";
                System.out.println("KPRIME POST [" + requestUri + "] request:[" + request + "]");
                httpRequest = HttpRequest.newBuilder()
                        .POST(data)
                        .uri(URI.create(requestUri))
                        .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .build();
            } else if (requestType.equals("put")) {
                String requestUri = kprimeAddress + "/expert/" + contextName + "/tracecommand";
                System.out.println("KPRIME PUT [" + requestUri + "] request:[" + request + "]");
                httpRequest = HttpRequest.newBuilder()
                        .PUT(data)
                        .uri(URI.create(requestUri))
                        .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                        .header("Content-Type", "application/json;charset=utf-8")
                        .build();
            } else {
                String requestUri = kprimeAddress + request;
                System.out.println("KPRIME GET [" + requestUri + "] ");
                httpRequest = HttpRequest.newBuilder()
                        .GET()
                        .uri(URI.create(requestUri))
                        .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                        .build();
            }
            //System.out.println(this.getClass().getName()+"Sending "+requestType);
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            //System.out.println(httpResponse.body());
            kPrimeDTO.setResponse(httpResponse.body().replaceAll("\"","").replaceAll("\\\\n", "\n"));
        } catch (Exception e) {
            e.printStackTrace();
            return new KPrimeDTO();
        }
        Gson gson = new Gson();
        //Type listType = new TypeToken<ArrayList<PortfolioDTO>>(){}.getType();
        //gson.fromJson(content.toString(), KPrimeDTO.class);
        //System.out.println(kPrimeDTO);
        return kPrimeDTO;
    }

    private boolean isFetchPortfolio(KPrimeDTO kPrimeDTO) {
        boolean fetchPortfolio = false;
        if (kPrimeDTO == null) fetchPortfolio = true;
        if (!LocalDateTime.now().isAfter(localDateTime.plusMinutes(5))) fetchPortfolio = true;
        return fetchPortfolio;
    }


    // Sample: 'password=123&custom=secret&username=abc&ts=1570704369823'
    public static HttpRequest.BodyPublisher ofFormData(Map<Object, Object> data) {
        var builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }

}
