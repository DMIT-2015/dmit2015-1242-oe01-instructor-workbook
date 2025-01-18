package dmit2015.faces;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.omnifaces.util.Messages;

@Named("currentHelloView")
@RequestScoped  // create this object for one HTTP request and destroy after the HTTP response has been sent
public class HelloView {

    // Declare read/write properties (field + getter + setter) for each form field
    @Getter @Setter
    @NotBlank(message = "Please enter a name.")
    private String name;

    // Declare read only properties (field + getter) for data sources

    // Declare private fields for internal usage only objects

    @PostConstruct // This method is executed after DI is completed (fields with @Inject now have values)
    public void init() { // Use this method to initialized fields instead of a constructor
        // Code to access fields annotated with @Inject

    }

    public void onSubmit() {
        try {
//            Messages.addGlobalInfo("Hello " + name);
            Messages.addGlobalInfo("Hello {0} and welcome to Faces world!", name);
        } catch (Exception ex) {
            handleException(ex);
        }
    }

    public void onClear() {
        // Set all fields to default values
    }

    /**
     * This method is used to handle exceptions and display root cause to user.
     *
     * @param ex The Exception to handle.
     */
    protected void handleException(Exception ex) {
        StringBuilder details = new StringBuilder();
        Throwable causes = ex;
        while (causes.getCause() != null) {
            details.append(ex.getMessage());
            details.append("    Caused by:");
            details.append(causes.getCause().getMessage());
            causes = causes.getCause();
        }
        Messages.create(ex.getMessage()).detail(details.toString()).error().add("errors");
    }

}