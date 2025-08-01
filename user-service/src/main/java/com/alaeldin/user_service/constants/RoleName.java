package com.alaeldin.user_service.constants;

/**
 * Enum representing role names in the system.
 */
public enum RoleName {
    ADMIN("Admin"),
    USER("User");

    private final String value;

    RoleName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public  static RoleName[] getValues()
    {
        return  RoleName.values();
    }

    public static RoleName fromValue(String value)
    {
        for (RoleName roleName : RoleName.getValues()) {
            if (roleName.getValue().equalsIgnoreCase(value)) {
                return roleName;
            }
        }
        throw new IllegalArgumentException("No constant with value " + value + " found");
    }
}
