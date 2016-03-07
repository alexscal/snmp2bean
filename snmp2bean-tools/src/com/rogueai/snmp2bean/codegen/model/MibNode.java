
package com.rogueai.snmp2bean.codegen.model;

import org.apache.commons.lang.WordUtils;

/**
 * 
 * @author zugnom
 * 
 */
public abstract class MibNode
{

    private String name;

    private String description;

    private String oid;

    public String getName()
    {
        return name;
    }

    public String getCapitalizedName()
    {
        return WordUtils.capitalize(getName());
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getOid()
    {
        return oid;
    }

    public void setOid(String oid)
    {
        this.oid = oid;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

}
