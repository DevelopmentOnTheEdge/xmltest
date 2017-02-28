package com.developmentontheedge.xmltest;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class TargetFactory
{
    protected Map<String, String> targetMap = new HashMap<>();

    public void addTarget(String targetName, String className)
    {
        targetMap.put(targetName, className);
    }

    public Target getTarget(String targetName, XmlTest test) throws Exception
    {
        String className = targetMap.get(targetName);
        if( className == null )
            throw new NoSuchElementException("Task <" + targetName + "> is not defined."); 

        Class<? extends Target> clazz = Class.forName( className ).asSubclass(Target.class);
        Constructor<? extends Target> constructor = clazz.getConstructor(XmlTest.class);
        return  constructor.newInstance(test);
    }
}
