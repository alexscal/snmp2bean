
package com.rogueai.snmp2bean.codegen.model;

/**
 * 
 * @author zugnom
 * 
 */
public class MibValueNode extends MibNode
{

    private boolean index;
    private String  javaType;
    private String  smiType;

    public boolean isIndex()
    {
        return index;
    }

    public void setIndex(boolean index)
    {
        this.index = index;
    }

    public String getJavaType()
    {
        return javaType;
    }

    public void setJavaType(String javaType)
    {
        this.javaType = javaType;
    }

    public String getSmiType()
    {
        return smiType;
    }

    public void setSmiType(String smiType)
    {
        this.smiType = smiType;
    }

}
