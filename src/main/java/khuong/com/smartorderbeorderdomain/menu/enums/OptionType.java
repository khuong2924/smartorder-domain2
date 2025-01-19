package khuong.com.smartorderbeorderdomain.menu.enums;

public enum OptionType {
    SINGLE("Single selection"),
    MULTIPLE("Multiple selections"),
    REQUIRED("Required selection"),
    OPTIONAL("Optional selection");

    private final String description;

    OptionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}