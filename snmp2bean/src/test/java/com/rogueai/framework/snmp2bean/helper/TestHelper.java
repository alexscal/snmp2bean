package com.rogueai.framework.snmp2bean.helper;

import java.io.File;

public class TestHelper {
    
    public static File getProjectRelativePath(String projectPath, String relativePath)
    {
        File flowDir = new File(new File(projectPath), relativePath);
        while (!flowDir.exists() && projectPath.indexOf('/') != -1)
        {
            projectPath = projectPath.substring(projectPath.indexOf('/') + 1);
            flowDir = new File(new File(projectPath), relativePath);
        }
        if (!flowDir.exists())
        {
            flowDir = new File(relativePath);
        }
        if (!flowDir.exists())
        {
            throw new RuntimeException(relativePath + " not found.");
        }
        return flowDir;
    }
    
}
