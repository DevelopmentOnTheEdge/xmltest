package com.developmentontheedge.xmltest;

import java.util.ListResourceBundle;
import java.util.MissingResourceException;

/**
 * Stub.
 */
public class MessageBundle extends ListResourceBundle
{
    /**
     * Returns string from the resource bundle for the specified key.
     * 
     * @param key resource key
     * @return string from the resource bundle
     */
    public String getResourceString(String key)
    {
        try
        {
            return getString(key);
        }
        catch (MissingResourceException t)
        {
        	// ignore
        	// TODO: handle (log?)
        }
        return key;
    }

    @Override
    protected Object[][] getContents()
    {
        return new Object[][] 
        {
            //--- Errors --------------------------------------------
            {"ERROR_TEST_NAME_MISSING",            "DiagramType {0}: can not read element <{2}> in <{1}>, error: {3}"},
        };
    }

}
