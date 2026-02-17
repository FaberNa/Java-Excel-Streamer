package org.github.fabercata.excelbatcher.poi;

import org.junit.jupiter.api.Test;
import org.xml.sax.InputSource;

import java.io.StringReader;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

class SecureSaxParserFactoryTest {


    @Test
    void newXmlReader_shouldReturnXmlReader() {
        var reader = SecureSaxParserFactory.newXmlReader();
        assertThat(reader).isNotNull();
    }

    @Test
    void newXmlReader_shouldParseSimpleXml() throws Exception {
        var reader = SecureSaxParserFactory.newXmlReader();

        // parse minimale (senza handler) per verificare che non esploda
        reader.setContentHandler(new org.xml.sax.helpers.DefaultHandler());

        assertThatCode(() ->
                reader.parse(new InputSource(new StringReader("<root><a>1</a></root>")))
        ).doesNotThrowAnyException();
    }

    @Test
    void xmlReader_shouldHaveXxeFeaturesDisabled() throws Exception {
        var reader = SecureSaxParserFactory.newXmlReader();

        assertThat(reader.getFeature("http://xml.org/sax/features/external-general-entities"))
                .isFalse();
        assertThat(reader.getFeature("http://xml.org/sax/features/external-parameter-entities"))
                .isFalse();
        assertThat(reader.getFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd"))
                .isFalse();
    }
}