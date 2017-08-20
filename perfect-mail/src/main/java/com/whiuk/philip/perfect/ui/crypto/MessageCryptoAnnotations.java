package com.whiuk.philip.perfect.ui.crypto;


import java.util.HashMap;

import com.whiuk.philip.perfect.mail.Part;
import com.whiuk.philip.perfect.mailstore.CryptoResultAnnotation;


public class MessageCryptoAnnotations {
    private HashMap<Part, CryptoResultAnnotation> annotations = new HashMap<>();

    MessageCryptoAnnotations() {
        // Package-private constructor
    }

    void put(Part part, CryptoResultAnnotation annotation) {
        annotations.put(part, annotation);
    }

    public CryptoResultAnnotation get(Part part) {
        return annotations.get(part);
    }

    public boolean has(Part part) {
        return annotations.containsKey(part);
    }

    public boolean isEmpty() {
        return annotations.isEmpty();
    }

    public Part findKeyForAnnotationWithReplacementPart(Part part) {
        for (HashMap.Entry<Part, CryptoResultAnnotation> entry : annotations.entrySet()) {
            if (part == entry.getValue().getReplacementData()) {
                return entry.getKey();
            }
        }
        return null;
    }
}
