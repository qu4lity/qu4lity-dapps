package it.eng.sidrestapi.model.crypto;

import java.math.BigInteger;

public class RSAPublicKeyEN {

    private Integer e;
    private BigInteger n;

    public RSAPublicKeyEN(BigInteger n) {
        this.n = n;
    }

    public RSAPublicKeyEN(Integer e, BigInteger n) {
        this.e = e;
        this.n = n;
    }

    public Integer getE() {
        return e;
    }

    public void setE(Integer e) {
        this.e = e;
    }

    public BigInteger getN() {
        return n;
    }

    public void setN(BigInteger n) {
        this.n = n;
    }
}
