package org.github.faberNa.excelbatcher.poi;

import org.github.faberNa.excelbatcher.exception.SaxFactoryException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Factory for creating secure SAX parsers.
 */
final class SecureSaxParserFactory implements XmlReaderProvider {

    private static final SAXParserFactory FACTORY = createFactory();

    private SecureSaxParserFactory() {}


    @Override
    public XMLReader create() {
        return SecureSaxParserFactory.newXmlReader();
    }

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