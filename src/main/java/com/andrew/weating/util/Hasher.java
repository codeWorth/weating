package com.andrew.weating.util;

import static org.apache.commons.codec.binary.Base64.decodeBase64;
import static org.apache.commons.codec.binary.Base64.encodeBase64String;
import static org.apache.commons.codec.digest.DigestUtils.sha256;

public class Hasher {
    public static String hash(String value) {
        return encodeBase64String(sha256(decodeBase64(value)));
    }
}
