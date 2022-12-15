package it.informatelier.kprime.cli.http;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.Ignore;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;

/**
 * Created by nipe on 24/11/16.
 */
public class GistsTest {

    @Test
    @Ignore
    // curl https://api.github.com/gists/ae856e105f1f1adb55311bf4be203abf
    public void test_fetch_gist() throws Exception{
        String remoteFileName = "https://api.github.com/gists/ae856e105f1f1adb55311bf4be203abf";
        String localFileName = "target/information.json";
        Gister.downloadFile(remoteFileName, localFileName);

        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(new FileReader(localFileName));
        System.out.println("jsonEle:"+jsonElement.toString());
        JsonElement url = jsonElement.getAsJsonObject().get("url");
        System.out.println("url:"+url);
        JsonElement files = jsonElement.getAsJsonObject().get("files");
        System.out.println("files:"+files);
        JsonElement file1 = files.getAsJsonObject().get("gson-dep.xml");
        System.out.println("file1:"+file1);
        JsonElement file1content = file1.getAsJsonObject().get("content");
        System.out.println("content1:"+file1content);


    }

    //curl -u "npedot" https://api.github.com/repos/npedot/jtoggl/readme
    //curl -u "npedot" https://api.github.com/user/repos
    //curl -u "npedot" https://api.github.com/repos/npedot/jtoggl/contents/jtoggl-api
    // type: "dir"
    //curl https://api.github.com/repos/npedot/jtoggl/contents/jtoggl-api
    // type: "file"
    // download_url:
    //curl https://raw.githubusercontent.com/npedot/jtoggl/master/jtoggl-api/pom.xml

    @Test
    @Ignore
    public void test_fetch_git() throws IOException {
        String remoteFileName = "https://api.github.com/repos/npedot/jtoggl/contents/jtoggl-api";
        String localFileName = "jtoggl.json";
        String localDirName = "target/out/";
        String workingDir = "target/tmp/";
        Gister.fetchGit(remoteFileName, localDirName, localFileName, workingDir);
    }

}
