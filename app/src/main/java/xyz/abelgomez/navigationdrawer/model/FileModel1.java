package xyz.abelgomez.navigationdrawer.model;
import java.io.Serializable;

public class FileModel1 implements Serializable {
    private String name;
    private String url;

    public FileModel1() {
    }

    public FileModel1(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
