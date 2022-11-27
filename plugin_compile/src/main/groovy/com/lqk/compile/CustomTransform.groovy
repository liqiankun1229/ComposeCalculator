package com.lqk.compile


import javax.xml.crypto.Data
import javax.xml.crypto.XMLCryptoContext
import javax.xml.crypto.dsig.Transform
import javax.xml.crypto.dsig.TransformException
import java.security.spec.AlgorithmParameterSpec

class CustomTransform implements Transform {
    @Override
    AlgorithmParameterSpec getParameterSpec() {
        return null
    }

    @Override
    Data transform(Data data, XMLCryptoContext xmlCryptoContext) throws TransformException {
        return null
    }

    @Override
    Data transform(Data data, XMLCryptoContext xmlCryptoContext, OutputStream outputStream) throws TransformException {
        return null
    }

    @Override
    String getAlgorithm() {
        return null
    }

    @Override
    boolean isFeatureSupported(String s) {
        return false
    }
}