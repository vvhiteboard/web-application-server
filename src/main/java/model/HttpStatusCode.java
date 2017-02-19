package model;

/**
 * Created by loopin on 2017-02-19.
 */
public enum HttpStatusCode {
    OK(200, "Ok"),

    FOUND(302, "Redirect");

    private String responseString;
    private String responseCode;

    HttpStatusCode(int responseCode, String responseString) {
        this.responseCode = Integer.toString(responseCode);
        this.responseString = responseString;
    }

    @Override
    public String toString() {
        return this.responseCode + " " + this.responseString;
    }
}
