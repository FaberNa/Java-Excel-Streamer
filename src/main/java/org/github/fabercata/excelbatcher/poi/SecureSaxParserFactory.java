package org.github.fabercata.excelbatcher.poi;

import org.github.fabercata.excelbatcher.exception.SaxFactoryException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Factory for creating secure SAX parsers.
 */
final class SecureSaxParserFactory {

    private static final SAXParserFactory FACTORY = createFactory();

    private SecureSaxParserFactory() {}

    static XMLReader newXmlReader() {
        try {
            SAXParser parser = FACTORY.newSAXParser();
            return parser.getXMLReader();
        } catch (Exception e) {
            throw new SaxFactoryException("Unable to create secure SAX parser", e);
        }
    }

    private static SAXParserFactory createFactory() {
        try {
            SAXParserFactory f = SAXParserFactory.newInstance();
            f.setNamespaceAware(true);

            // sicurezza base (XXE)
            f.setFeature("http://xml.org/sax/features/external-general-entities", false);
            f.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            f.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

            return f;
        } catch (Exception e) {
            throw new IllegalStateException("Unable to configure SAXParserFactory securely", e);
        }
    }
}