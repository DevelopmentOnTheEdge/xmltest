package com.developmentontheedge.xmltest;

import junit.framework.ComparisonFailure;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import static junit.framework.TestCase.assertEquals;

public class ParserTarget extends Target
{
    static final String TAG_PARSER      = "parser";
    static final String TAG_QUERY       = "query";
    static final String TAG_FORMAT      = "format";

    static final String ATTR_DBMS  = "dbms";

    private String query;

    public ParserTarget(XmlTest xmlTest)
    {
        super(xmlTest);
    }

    @Override
    public void process(Element element) throws Exception
    {
        try
        {
            NodeList childs = element.getChildNodes();
            for( int i = 0; i < childs.getLength(); i++ )
            {
                if( childs.item(i) instanceof Element )
                    processChild( (Element)childs.item(i) );
            }
        }
        catch(Throwable t)
        {
            test.getResult().addError(test, t);
        }
    }

    protected void processChild(Element element) throws Exception
    {
        switch(element.getTagName())
        {
            case TAG_QUERY:
                loadAndParseQuery(element); break;

            case TAG_FORMAT:
                format(element); break;
        }
    }

    protected void loadAndParseQuery(Element element)
    {
        query = XmlTest.getCData(element);
    }

    protected void format(Element element)
    {
        String dbmsNames = element.getAttribute(ATTR_DBMS);
        if( dbmsNames == null )
        {
            test.getResult().addError(test, new Exception("Attribute \"" + ATTR_DBMS + "\" should be specified in task <" + TAG_FORMAT + ">."));
            return;
        }

        for(String dbmsName : dbmsNames.split(","))
        {
            format(dbmsName, element);
        }
    }

    protected void format(String dbmsName, Element element)
    {
        String formatResult = new TestClass().format(query, dbmsName);

        String result = XmlTest.getCData(element);

        assertEquals("Incorrect result, dbms=" + dbmsName, result, formatResult);

        if( !formatResult.equals(result) )
            test.getResult().addFailure(test, new ComparisonFailure("Incorrect result, dbms=" + dbmsName , result, formatResult));
    }
}
