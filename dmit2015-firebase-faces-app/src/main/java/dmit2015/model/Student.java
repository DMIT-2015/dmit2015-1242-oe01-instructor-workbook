package dmit2015.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Student {

    private String id;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, message = "Last name must contain 2 or more characters")
    private String lastName;

    @Pattern(regexp = "^[A-Za-z]{4}\\d{4}$",
        message = "Course section must have pattern AAAA9999")
    private String courseSection;

}
