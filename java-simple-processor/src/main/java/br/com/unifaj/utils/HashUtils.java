package br.com.unifaj.utils;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

import java.security.MessageDigest;

import static br.com.unifaj.constants.Constants.HASH_PREFIX;
import static br.com.unifaj.constants.Constants.HASH_SUFFIX;

public class HashUtils {

    public static String hash(String value, String hashType) {

        String hash = "";

        try {

            MessageDigest md = MessageDigest.getInstance(hashType);
            String message = HASH_PREFIX.value() + value + HASH_SUFFIX.value();

            md.update(message.getBytes());

            hash =  HexBin.encode(md.digest());

        }catch (Exception ex) {
            System.out.println("Erro ao hashear informações");
        }

        return hash;
    }
}
