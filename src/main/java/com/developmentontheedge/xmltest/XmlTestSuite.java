package com.developmentontheedge.xmltest;

import static com.developmentontheedge.xmltest.XmlConstants.*;

import java.io.File;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import junit.framework.TestSuite;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 
 * @todo load messages from messageBundle
 */
public class XmlTestSuite extends TestSuite
{
    protected TargetFactory targetFactory;

    private File _file;
    protected Document doc;
    protected Element setupElement;
    protected Element tearDownElement;

    public XmlTestSuite(String testFileName, TargetFactory targetFactory)
    {
        this.targetFactory = targetFactory;
        
        initDocument( testFileName );
        initSuite();
    }

    public void info( String msg )
    {
        System.out.println( msg );
    }

    public void error( String msg )
    {
        error( false, msg, null );
    }

    public void error( boolean fatal, String msg )
    {
        error( fatal, msg, null );
    }

    public void error( String msg, Element e )
    {
        error( false, msg, e );
    }

    public void error( boolean fatal, String msg, Element e )
    {
        if( msg == null )
        {
            msg = "Ошибка";
        }
        if( e == null )
        {
            msg += ".";
        }
        else
        {
            msg += ", элемент: " + toString( e );
        }

        if( fatal )
        {
            throw new Error( msg );
        }
    }

    public String toString( Element element )
    {
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document docHtml = builder.newDocument();

            org.w3c.dom.Node node = docHtml.importNode( element, true );
            docHtml.appendChild( node );

            DOMSource source = new DOMSource( docHtml );
            StringWriter string = new StringWriter();
            StreamResult result = new StreamResult( string );

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.transform( source, result );

            String str = string.toString();
            return str.substring( str.indexOf( "?>" ) + 2 );
        }
        catch( Throwable t )
        {
            return null;
        }
    }

    public void initDocument( String testFileName )
    {
        if( testFileName == null || testFileName.length() == 0 )
        {
            error( true, "Test file name is not specified." );
        }

        _file = new File( testFileName );
        if( !_file.exists() )
        {
            error( true, "Test file '" + testFileName + "' is not found." );
        }

        info( "Test file: '" + _file.getAbsolutePath() + "'." );

        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse( _file );
        }
        catch( Throwable t )
        {
            String msg = "xml parsing error: " + t;
            addTest( warning(msg) );
        }
    }

    public void initSuite()
    {
        try
        {
            Element root = doc.getDocumentElement();
            if( !root.getTagName().equals( TEST_SUITE_ELEMENT ) )
            {
                error( true, "XML error - root element should be <" + TEST_SUITE_ELEMENT + "> element." );
            }

            setup(root);
            initTests(root);
        }
        catch( Throwable t )
        {
            String msg = "cannot init test suite";
            addTest( warning(msg) );
        }
    }

    public void setup( Element root )
    {
        setupElement = null;
        NodeList nodes = root.getElementsByTagName( SETUP_ELEMENT );
        if( nodes.getLength() > 1 )
        {
            error( "More then one <setup> element." );
            return;
        }
        setupElement = ( Element )nodes.item( 0 );

        tearDownElement = null;
        nodes = root.getElementsByTagName( TEARDOWN_ELEMENT );
        if( nodes.getLength() > 1 )
        {
            error( "More then one <tearDown> element." );
            return;
        }
        tearDownElement = ( Element )nodes.item( 0 );
    }

    public void initTests( Element root )
    {
        NodeList nodes = root.getElementsByTagName(TEST_ELEMENT);
        Element element;
        for( int i = 0; i < nodes.getLength(); i++ )
        {
            element = (Element)nodes.item(i);
            try
            {
                addTest( new XmlTest(this, element) );
            }
            catch( Exception e )
            {
                error( "Test parsing erro: " + e, element );
            }
        }
    }
}
