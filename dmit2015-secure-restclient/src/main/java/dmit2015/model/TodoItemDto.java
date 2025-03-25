package dmit2015.model;

import jakarta.annotation.Generated;

@Generated("jsonschema2pojo")
public class TodoItemDto {

private Boolean complete;
private Long id;
private String name;
private Integer version;

public Boolean getComplete() {
return complete;
}

public void setComplete(Boolean complete) {
this.complete = complete;
}

public Long getId() {
return id;
}

public void setId(Long id) {
this.id = id;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public Integer getVersion() {
return version;
}

public void setVersion(Integer version) {
this.version = version;
}

}
