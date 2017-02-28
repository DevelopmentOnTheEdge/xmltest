package com.developmentontheedge.xmltest;

import static com.developmentontheedge.xmltest.XmlConstants.*;
import junit.framework.TestCase;
import junit.framework.TestResult;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlTest extends TestCase
{
    protected MessageBundle messageBundle = new MessageBundle();

    protected Element element;

    public XmlTest(XmlTestSuite suite, Element element)
    {
        this.suite = suite;
        this.element = element;

        if( element.hasAttribute(NAME_ATTR) )
            setName(element.getAttribute(NAME_ATTR));
        else
        {
            suite.error( "не указано имя теста", element );
            setName("unknown");
        }
    }

    @Override
    public String toString()
    {
        return getName();
    }
    
    ///////////////////////////////////////////////////////////////////////////
    // Properties
    //

    protected XmlTestSuite suite;
    public XmlTestSuite getSuite()
    {
        return suite;
    }

    protected TestResult result;
    public TestResult getResult()
    {
        return result;
    }

    @Override
    public int countTestCases()
    {
        return 1;
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     * @todo
     */
    @Override
	protected void setUp() throws Exception 
    {
    }

    /**
     * @todo
     */
    @Override
	protected void tearDown() throws Exception 
    {
    }


    @Override
    public void run(TestResult result)
    {
        this.result = result; 
        result.startTest(this);

        try
        {
            NodeList childs = element.getChildNodes();
            for( int i = 0; i < childs.getLength(); i++ )
            {
                if( childs.item(i) instanceof Element )
                    processTarget( (Element)childs.item(i) );
            }
        }
        catch(Throwable t)
        {
            result.addError(this, t);
        }

        result.endTest(this);
    }

    protected void processTarget(Element elem) throws Exception
    {
        String tagetName = elem.getTagName();

        Target target = suite.targetFactory.getTarget(tagetName, this);
        target.process(elem);
    }


    String elementToString( Element elem )
    {
        String ret = "<";
        ret += elem.getTagName();
        NamedNodeMap namedNodeMap = elem.getAttributes();
        int i;
        for( i = 0; i < namedNodeMap.getLength(); i++ )
        {
            Node node = namedNodeMap.item( i );
            String nodeName = node.getNodeName();
            String value = node.getNodeValue();
            ret += " " + nodeName + " = \"" + value + "\"";
        }
        ret += ">";
        return ret;
    }

    // ////////////////////////////////////////////////////////////////////////
    // utility methods
    // 
    
    public static String getCData(Element root)
    {
        String cdata = null;

        NodeList childList = root.getChildNodes();
        for( int n = 0; n < childList.getLength(); ++n )
        {
            Node child = childList.item( n );
            if( child.getNodeType() == Node.CDATA_SECTION_NODE )
            {
                cdata = child.getNodeValue();
                break;
            }
        }

        return cdata;
    }

}