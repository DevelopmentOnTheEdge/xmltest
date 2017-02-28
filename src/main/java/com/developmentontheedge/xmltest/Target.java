package com.developmentontheedge.xmltest;

import org.w3c.dom.Element;

public abstract class Target
{
    protected XmlTest test;

    public Target(XmlTest xmlTest)
    {
        this.test = xmlTest;
    }

    public abstract void process(Element element) throws Exception;
}
