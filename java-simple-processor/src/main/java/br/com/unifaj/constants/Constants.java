package br.com.unifaj.constants;

public enum Constants {

    HASH_PREFIX("UNIFAJ"),
    HASH_SUFFIX("TCC"),
    MD5("MD5"),
    OUTPUT_FILE("processed_date");

    private String constant;

    Constants(String constant) {
        this.constant = constant;
    }

    public String value() {
        return this.constant;
    }

}
