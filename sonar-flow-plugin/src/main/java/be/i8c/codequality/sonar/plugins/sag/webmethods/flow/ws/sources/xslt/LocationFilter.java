package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.ws.sources.xslt;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.Attributes2Impl;
import org.xml.sax.helpers.XMLFilterImpl;

public class LocationFilter extends XMLFilterImpl {

  public LocationFilter(XMLReader xmlReader) {
    super(xmlReader);
  }

  private Locator locator = null;

  @Override
  public void setDocumentLocator(Locator locator) {
    super.setDocumentLocator(locator);
    this.locator = locator;
  }

  @Override
  public void startElement(String uri, String localName,
      String qualifiedName, Attributes attributes)
      throws SAXException {

    // Add extra attribute to elements to hold location
    String location = String.valueOf(locator.getLineNumber());
    Attributes2Impl attrs = new Attributes2Impl(attributes);
    attrs.addAttribute("", "location", "location", "String", location);
    super.startElement(uri, localName, qualifiedName, attrs);
  }

}
