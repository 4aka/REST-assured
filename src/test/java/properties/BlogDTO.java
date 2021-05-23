package properties;

public class BlogDTO {

    private String name;
    private String description;
    private String url;

    //let your IDE generate the getters and fluent setters for your:

    public BlogDTO setName(String name) {
        this.name = name;
        return this;
    }

    public BlogDTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public BlogDTO setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }
}
