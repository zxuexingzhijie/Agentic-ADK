package com.alibaba.agentic.computer.use.utils;

public class EcdConstant {

    public static String pythonFileUploadFormat = "$pythonCode = @\"\n%s\n\"@\n$filePath = %s\n $pythonCode | Out-File -FilePath $filePath -Encoding utf8";

}
