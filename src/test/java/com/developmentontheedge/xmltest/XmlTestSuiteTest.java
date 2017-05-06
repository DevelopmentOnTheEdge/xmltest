package com.developmentontheedge.xmltest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class XmlTestSuiteTest extends TestCase
{
    protected static final String relativePath = "./src/test/resources/";

    /** Standart JUnit constructor */
    public XmlTestSuiteTest(String name)
    {
        super(name);
    }

    /** Make suite of tests. */
    public static Test suite()
    {
        TestSuite suite = new TestSuite(XmlTestSuiteTest.class.getName());

        TargetFactory targetFactory = new TargetFactory();
        targetFactory.addTarget(ParserTarget.TAG_PARSER, ParserTarget.class.getName());

        suite.addTest(new XmlTestSuite(relativePath + "core.xml", targetFactory));

        return suite;
    }

}