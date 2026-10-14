package lahiru_rajakaruna.retail_manager.Authentication;

public enum SecurityConstants {
    TRACE_ID("trace_id"),
    AUTH_COOKIE_NAME("access_token");

    private final String value;

    private SecurityConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
