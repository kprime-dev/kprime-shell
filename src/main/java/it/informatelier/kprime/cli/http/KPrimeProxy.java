package it.informatelier.kprime.cli.http;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class KPrimeProxy {
//    String kprimeAddress = "http://localhost:7000";
//    String contextName = "kprime";


    public ModelResponse ask(String address, String context, ModelRequest request) {
        String question = request.getQuestion();
        if (question.startsWith("POST ")) {
            return new ModelResponse(askKPrime(address, context,"post", question.substring(5)).getResponse());
        } else if (question.startsWith("GET ")) {
                return new ModelResponse(askKPrime(address, context,"get",question.substring(4)).getResponse());
        } else {
            return new ModelResponse(askKPrime(address, context,"put",question.substring(4)).getResponse());
        }
    }

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    // https://mkyong.com/java/java-11-httpclient-examples/

    private KPrimeDTO askKPrime(String address, String context, String requestType, String request) {
        //System.out.println(this.getClass().getName()+":askKPrime ["+request+"]");
        KPrimeDTO kPrimeDTO = new KPrimeDTO();
        kPrimeDTO.setRequest(request);

        // try do a remote request to get it.
        // POST /parse command=add-table Address:city,street
        try {
            //System.out.println(this.getClass().getName() + " mapping request.");
            //Map<Object, Object> data = new HashMap<>();
            //data.put("command", request);
            //System.out.println("KPRIME [" + requestType + "] request:[" + request + "]");
            HttpRequest.BodyPublisher data = HttpRequest.BodyPublishers.ofString(request);
            HttpRequest httpRequest;
            if (requestType.equals("post")) {
                String requestUri = address + "/parse";
                System.out.println("KPRIME POST [" + requestUri + "] request:[" + request + "]");
                httpRequest = HttpRequest.newBuilder()
                        .POST(data)
                        .uri(URI.create(requestUri))
                        .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .build();
            } else if (requestType.equals("put")) {
                String requestUri = address + "/expert/" + context + "/tracecommand";
                System.out.println("KPRIME PUT [" + requestUri + "] request:[" + request + "]");
                httpRequest = HttpRequest.newBuilder()
                        .PUT(data)
                        .uri(URI.create(requestUri))
                        .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                        .header("Content-Type", "application/json;charset=utf-8")
                        .build();
            } else {
                String requestUri = address + request;
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
            //e.printStackTrace();
            System.out.println(e.getCause().toString());
            return new KPrimeDTO();
        }
        //Gson gson = new Gson();
        //Type listType = new TypeToken<ArrayList<PortfolioDTO>>(){}.getType();
        //gson.fromJson(content.toString(), KPrimeDTO.class);
        //System.out.println(kPrimeDTO);
        return kPrimeDTO;
    }



    // Sample: 'password=123&custom=secret&username=abc&ts=1570704369823'
    /*
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
     */

}