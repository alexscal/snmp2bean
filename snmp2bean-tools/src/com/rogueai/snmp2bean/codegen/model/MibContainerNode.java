
package com.rogueai.snmp2bean.codegen.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author zugnom
 * 
 */
public class MibContainerNode extends MibNode
{

    private boolean       table;

    private List<MibNode> children = new ArrayList<MibNode>();

    public List<MibNode> getChildren()
    {
        return children;
    }

    public void setChildren(List<MibNode> children)
    {
        this.children = children;
    }

    public boolean isTable()
    {
        return table;
    }

    public void setTable(boolean table)
    {
        this.table = table;
    }

}
