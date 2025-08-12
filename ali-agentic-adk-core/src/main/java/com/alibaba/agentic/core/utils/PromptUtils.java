package com.alibaba.agentic.core.utils;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.text.StrSubstitutor;

import java.util.Map;
import java.util.function.Supplier;

public class PromptUtils {

    public static String generatePrompt(String promptTemplate, Map<String, String> params) {
        return replace(promptTemplate, params);
    }

    public static String generatePrompt(String promptTemplate, Supplier<Map<String, String>> paramSupplier) {
        return replace(promptTemplate, paramSupplier.get());
    }

    public static String generatePrompt(Supplier<String> promptTemplateSupplier, Supplier<Map<String, String>> paramSupplier) {
        return replace(promptTemplateSupplier.get(), paramSupplier.get());
    }

    private static String replace(String originContent, Map<String, String> promptParams) {
        //替换prompt中的占位符变量，兼容一下两种类型的占位符
        if(MapUtils.isNotEmpty(promptParams)) {
            StrSubstitutor strSubstitutor = new StrSubstitutor(promptParams);
            String replacedPrompt = strSubstitutor.replace(originContent);

            strSubstitutor = new StrSubstitutor(promptParams, "$!{", "}");
            replacedPrompt =strSubstitutor.replace(replacedPrompt);

            strSubstitutor = new StrSubstitutor(promptParams, "{", "}");
            replacedPrompt =strSubstitutor.replace(replacedPrompt);
            return replacedPrompt;
        }
        return originContent;
    }

}
