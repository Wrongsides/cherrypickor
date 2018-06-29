package wrongsides.cherrypickor.controller.resource;

import org.springframework.hateoas.ResourceSupport;

public class NamedResource extends ResourceSupport {

    private String name;
    private String message;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
