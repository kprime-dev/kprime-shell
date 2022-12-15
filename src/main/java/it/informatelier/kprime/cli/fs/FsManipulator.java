package it.informatelier.kprime.cli.fs;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class FsManipulator {

    public static void init() {
        mkdir("src/main/java");
        mkdir("src/test/java");
        //copyTo("DummyTest.java", "src/test/java/");
    }

    public static void initKotlinTree() {
        mkdir("src/main/kotlin");
        mkdir("src/test/kotlin");
        //copyTo("DummyTest.java", "src/test/java/");
    }

    public static void addHelloSparkjava() {
        mkdir("src/main/resources/public");
        copyTo("sparkjava/public/","index.html", "src/main/resources/public/");
        copyTo("sparkjava/","HelloSparkjava.java", "src/main/java/");
        copyTo("sparkjava/","system.properties", "");
        copyTo("sparkjava/","Procfile", "");
        copyTo("sparkjava/","readme.md", "");
    }

    public static void addHelloSparkjavaWeb() {
        mkdir("src/main/resources/public");
        copyTo("sparkjavaweb/public/","index.html", "src/main/resources/public/");
        copyTo("sparkjavaweb/java/","Application.java", "src/main/java/");
        copyTo("sparkjavaweb/java/","Constants.java", "src/main/java/");
        copyTo("sparkjavaweb/java/","Filters.java", "src/main/java/");
        copyTo("sparkjavaweb/java/","Router.java", "src/main/java/");
        mkdir("src/main/java/controller");
        copyTo("sparkjavaweb/java/controller/","SampleController.java", "src/main/java/controller/");
        copyTo("sparkjavaweb/","system.properties", "");
        copyTo("sparkjavaweb/","Procfile", "");
        copyTo("sparkjavaweb/","readme.md", "");
        mkdir("src/main/resources/templates");
        copyTo("sparkjavaweb/templates/","assets.hsb", "src/main/resources/templates/");
        copyTo("sparkjavaweb/templates/","list-books.hsb", "src/main/resources/templates/");
    }

    public static void addHelloDropwizard() {
        copyTo("dropwizard/src/","HelloWorldApplication.java", "src/main/java/");
        copyTo("dropwizard/src/","HelloWorldConfiguration.java", "src/main/java/");
        copyTo("dropwizard/src/","HelloWorldResource.java", "src/main/java/");
        copyTo("dropwizard/src/","Saying.java", "src/main/java/");
        copyTo("dropwizard/src/","TemplateHealthCheck.java", "src/main/java/");
        copyTo("dropwizard/src/","hello-world.yml", "src/main/resources/");
    }

    public static void addSite() {
        mkdir("src/site");
        mkdir("src/site/markdown");
        mkdir("src/site/resources/images");
        copyTo("site/","site.xml", "src/site/");
        copyTo("site/markdown/","changelog.md", "src/site/markdown/");
        copyTo("site/markdown/","index.md", "src/site/markdown/");
        copyTo("site/markdown/","license.md", "src/site/markdown/");
        copyTo("site/resources/images/","mpm-logo.png", "src/site/resources/images/");
    }

    public static void addGarage() {
        copyTo("garage/",".gitignore", "");

        mkdir("src/main/java/it/nipe/garage/domain");
        copyTo("garage/","readme-domain.md", "src/main/java/it/nipe/garage/domain/");
        //mkdir("src/main/java/"+groupDir+artifactDir+"/domain");

        mkdir("src/main/java/it/nipe/garage/usecase");
        copyTo("garage/","readme-usecase.md", "src/main/java/it/nipe/garage/usecase/");

        mkdir("src/main/java/it/nipe/garage/repository");
        copyTo("garage/","readme-repository.md", "src/main/java/it/nipe/garage/repository/");

        mkdir("src/main/java/it/nipe/garage/service");
        copyTo("garage/","readme-service.md", "src/main/java/it/nipe/garage/service/");

        mkdir("src/main/java/it/nipe/garage/support");
        copyTo("garage/","readme-support.md", "src/main/java/it/nipe/garage/support/");

        mkdir("src/main/java/it/nipe/garage/adapter/shell");
        copyTo("garage/","readme-adapter.md", "src/main/java/it/nipe/garage/adapter/");
        //copyTo("garage/","readme-adapter-shell.md", "src/main/java/it/nipe/garage/adapter/application/shell");
        copyTo("garage/","ShellApplication.java", "src/main/java/it/nipe/garage/adapter/shell/");

        copyTo("garage/","Application.java", "src/main/java/it/nipe/garage/");

        addResources();
        copyTo("garage/","application.properties", "src/main/resources/");
        copyTo("garage/","application-filtered.properties","src/main/resources-filtered/");

        mkdir("src/test/java/it/nipe/garage");
        copyTo("garage/","ApplicationTest.java", "src/test/java/it/nipe/garage/");
        mkdir("src/test/java/it/nipe/garage/domain");
        copyTo("garage/","QuizTest.java", "src/test/java/it/nipe/garage/domain/");
        mkdir("src/test/java/it/nipe/garage/usecase");
        copyTo("garage/","AskQuizByTopicUseCaseTest.java", "src/test/java/it/nipe/garage/usecase/");

    }
    public static void addHelloSpringBoot(){
        //InputStream sourceStream = FsManipulator.class.getClassLoader().getResourceAsStream(srcDir+filename);
        String sourcePath = "springboot/src/main/java/hallo/SampleController.java";
        Path source=null;
        try {
            URI uri = ClassLoader.getSystemResource(sourcePath).toURI();
            System.out.println("URI:["+uri.toString()+"]");
            source = Paths.get(sourcePath);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (source==null) return;
        Path target=Paths.get("src");
        boolean preserve=true;
        boolean prompt=false;
        TreeCopier.copy(source,target,prompt,preserve);
    }

    public static void addResources() {
        new File("src/main/resources").mkdir();
        new File("src/main/resources-filtered").mkdir();
    }

    public static List<String> listLocalJars(String repoPath) {
        File file = new File(repoPath);
        List<String> jarNames = new ArrayList<>();
        String[] listFiles = file.list();
        for (String fileName: listFiles) {
            if ((new File(repoPath+"/"+fileName)).isDirectory()) {
                jarNames.addAll(listLocalJars(repoPath+"/"+fileName));
            } else {
                if (fileName.endsWith(".jar"))
                    jarNames.add(fileName);
            }
        }
        return jarNames;
    }

    private static void mkdir(String dir) {
        new File(dir).mkdirs();
    }

    private static void copyTo(String filename, String destDir) {
        copyTo("", filename, destDir);
    }

    private static void copyTo(String srcDir, String filename, String destDir) {
        InputStream file = FsManipulator.class.getClassLoader().getResourceAsStream(srcDir+filename);
        if (file==null) throw  new IllegalArgumentException("File is wrong.");
        Path path = Paths.get(destDir +filename);
        if (path==null) throw  new IllegalArgumentException("Path is wrong.");
        try {
            Files.copy(file,path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void installDir(String sourcePathName) {
        final File sourceFile = new File(sourcePathName);
        final File destFolderFile = new File("garage");
        try {
            Files.copy(sourceFile.toPath(),destFolderFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        public static void installDir2(String sourcePathName) {
        System.out.println("install "+ sourcePathName);
            try {
                final File sourceFile = new File(sourcePathName);
                final File destFolderFile = new File("./");
                for (File fileToCopy : sourceFile.listFiles()) {
                    System.out.println(fileToCopy);
                    System.out.println("copying..." + fileToCopy.getPath() + " to " + destFolderFile.getPath());
                    try {
                        File newFile = new File(destFolderFile.getAbsoluteFile()+fileToCopy.getName());
                        if (fileToCopy.isDirectory()) {
//                            System.out.println("new dir: " + newFile.getPath() );
//                            newFile.mkdir();
//                            installDir(fileToCopy.getAbsolutePath());
                        } else {
                            System.out.println("new file: " + newFile.getAbsolutePath() + newFile.getName());
                            FileOutputStream resourceOS = new FileOutputStream(newFile);
                            byte[] byteArray = new byte[1024];
                            int i;
                            InputStream classIS = new FileInputStream(fileToCopy);
                            //While the input stream has bytes
                            while ((i = classIS.read(byteArray)) > 0)
                            {
                                //Write the bytes to the output stream
                                resourceOS.write(byteArray, 0, i);
                            }
                            //Close streams to prevent errors
                            classIS.close();
                            resourceOS.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
    }

    public static void initJava9() {
        init();
        copyTo("java9/","pom.xml", "./");
    }

    public static void initKotlinPom() {
        copyTo("kotlin/","pom.xml", "./");
    }

    public static void initKotlinSample() {
        mkdir("./src/main/kotlin/kds");
        copyTo("kotlin/","Starter.kt", "./src/main/kotlin/kds/");
        mkdir("./src/test/kotlin/kds");
        copyTo("kotlin/","StarterTest.kt", "./src/test/kotlin/kds/");

    }

    public static void addToolchains() {
        copyTo("java9/","toolchains.xml", "./");
    }
}
