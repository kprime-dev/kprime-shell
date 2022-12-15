package it.informatelier.kprime.cli.http;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Gister {

    // gistUrl:"ae856e105f1f1adb55311bf4be203abf"
    // fileName:"gson-dep.xml"
    public static String getGist(String gistUrl, String fileName) {
        URL website = null;
        try {
            website = new URL(gistUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        ReadableByteChannel rbc = null;
        try {
            new File(".mpm").mkdirs();
            rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(".mpm/"+fileName);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = null;
        try {
            jsonElement = jsonParser.parse(new FileReader(".mpm/"+fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        JsonElement url = jsonElement.getAsJsonObject().get("url");
        JsonElement files = jsonElement.getAsJsonObject().get("files");
        JsonElement file1 = files.getAsJsonObject().get(fileName);
        JsonElement file1content = file1.getAsJsonObject().get("content");
        return file1content.toString();
    }

    static void downloadFile(String remoteFileName, String localFileName) throws IOException {
        URL website = new URL(remoteFileName);
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream(localFileName);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    }

    public static void fetchGit(String remoteFileName, String localDirName, String localFileName, String workingDir) throws IOException {
        if (localFileName==null) localFileName="dir.json";
        downloadFile(remoteFileName, workingDir + localFileName);

        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(new FileReader(workingDir + localFileName));
        //System.out.println("jsonEle:"+jsonElement.toString());

        int size = jsonElement.getAsJsonArray().size();
        for (int i = 0; i<size; i++) {
            JsonElement jsonElement1 = jsonElement.getAsJsonArray().get(i);
            JsonObject asJsonObject = jsonElement1.getAsJsonObject();
            JsonElement type = asJsonObject.get("type");
            if (type.getAsString().equals("file")) {
                String download_url = asJsonObject.get("download_url").getAsString();
                String name = asJsonObject.get("name").getAsString();
                System.out.println("name:" + name);
                System.out.println("download url:" + download_url);
                downloadFile(download_url, localDirName + name);
            } else if (type.getAsString().equals("dir")) {
                String dirname = asJsonObject.get("name").getAsString();
                String dir = localDirName + dirname + "/";
                new File(dir).mkdirs();
                fetchGit(remoteFileName+"/"+dirname,dir,null, workingDir);

            }
        }
    }
}
