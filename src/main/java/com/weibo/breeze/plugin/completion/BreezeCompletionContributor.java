package com.weibo.breeze.plugin.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.weibo.breeze.plugin.psi.BreezeFile;
import com.weibo.breeze.plugin.psi.BreezeTypes;
import com.weibo.breeze.plugin.setting.BreezeSettingState;
import com.weibo.breeze.plugin.utils.BreezePsiImplUtil;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.weibo.breeze.plugin.psi.BreezeTypes.*;

/**
 * @author zhanglei28
 * @date 2021/12/8.
 */
public class BreezeCompletionContributor extends CompletionContributor {
    private static final String[] KEYWORDS = {
            "option", "package", "service", "message", "enum", "config"
    };
    private static final String[] OPTION_KEYS = {
            "java_package", "with_package_dir", "go_package_prefix", "with_motan_config", "motan_config_type", "default_config_id_prefix"
    };
    private static final String[] INNER_OPTION_KEYS = {"config", "alias"};
    private static final String[] BASIC_TYPES = {
            "bool", "string", "byte", "bytes", "int16", "int32", "int64", "float32", "float64", "array", "map"
    };
    private static final List<String> MATCH_CONFIG_VALUE_KEYS = Arrays.asList("config", "basicConfig", "service.registryConfigName", "referer.registryConfigName", "service.protocolConfigName", "referer.protocolConfigName");
    private static final Map<String, String> MOTAN_CONFIG_KEY_MAP = new HashMap<>();
    private static final Map<String, String> MOTAN_BASIC_CONFIG_KEY_MAP = new HashMap<>();
    private static final Map<String, String> MOTAN_CONFIG_NAME_MAP = new HashMap<>();
    private static final Map<String, String> MOTAN_CONFIG_REGISTRY_KEY_MAP = new HashMap<>();
    private static final Map<String, String> MOTAN_CONFIG_PROTOCOL_KEY_MAP = new HashMap<>();
    private static final Map<String, String> MOTAN_SERVICE_BASIC_CONFIG_KEY_MAP = new HashMap<>();
    private static final Map<String, String[]> MOTAN_CONFIG_KEY_VALUES = new HashMap<>();
    private static final Map<String, String[]> MOTAN_OPTION_VALUES = new HashMap<>();

    static {
        // default config name
        MOTAN_CONFIG_NAME_MAP.put("MotanBasicConfig", "basic motan config for all service by default");
        MOTAN_CONFIG_NAME_MAP.put("MotanRegistry", "default registry or registry config name prefix");
        MOTAN_CONFIG_NAME_MAP.put("MotanProtocol", "default protocol or protocol config name prefix");

        // service config key
        MOTAN_SERVICE_BASIC_CONFIG_KEY_MAP.put("basicConfigName", "basic motan config name");

        // default config key in basic config
        MOTAN_BASIC_CONFIG_KEY_MAP.put("default.registry.id", "default registry: registry bean id");
        MOTAN_BASIC_CONFIG_KEY_MAP.put("default.registry.regProtocol", "default registry: registry protocol");
        MOTAN_BASIC_CONFIG_KEY_MAP.put("default.registry.address", "default registry: registry address");
        MOTAN_BASIC_CONFIG_KEY_MAP.put("default.protocol.id", "default protocol: protocol bean id");
        MOTAN_BASIC_CONFIG_KEY_MAP.put("default.protocol.name", "default protocol: protocol name");
        MOTAN_BASIC_CONFIG_KEY_MAP.put("default.protocol.loadbalance", "default client protocol: load balance");
        MOTAN_BASIC_CONFIG_KEY_MAP.put("default.protocol.haStrategy", "default client protocol: ha strategy");
        MOTAN_BASIC_CONFIG_KEY_MAP.put("default.protocol.minWorkerThread", "default server protocol: minWorkerThread");
        MOTAN_BASIC_CONFIG_KEY_MAP.put("default.protocol.maxWorkerThread", "default server protocol: maxWorkerThread");
        MOTAN_BASIC_CONFIG_KEY_MAP.put("default.protocol.workerQueueSize", "default server protocol: workerQueueSize");
        MOTAN_BASIC_CONFIG_KEY_MAP.put("default.protocol.maxServerConnection", "default server protocol: maxServerConnection");
        MOTAN_BASIC_CONFIG_KEY_MAP.put("default.protocol.minClientConnection", "default client protocol: min connection num per server node");
        MOTAN_BASIC_CONFIG_KEY_MAP.put("default.protocol.maxClientConnection", "default client protocol: max connection num per server node");

        // common config key
        MOTAN_CONFIG_KEY_MAP.put("referer.id", "client: referer config id");
        MOTAN_CONFIG_KEY_MAP.put("referer.requestTimeout", "client: requestTimeout");
        MOTAN_CONFIG_KEY_MAP.put("referer.registryConfigName", "client: registry config name");
        MOTAN_CONFIG_KEY_MAP.put("referer.protocolConfigName", "client: protocol config name");
        MOTAN_CONFIG_KEY_MAP.put("referer.registry", "client: registry id used by referer");
        MOTAN_CONFIG_KEY_MAP.put("referer.protocol", "client: protocol id used by referer");
        MOTAN_CONFIG_KEY_MAP.put("referer.group", "client: rpc group");
        MOTAN_CONFIG_KEY_MAP.put("referer.application", "client: rpc application");
        MOTAN_CONFIG_KEY_MAP.put("referer.check", "client: rpc init check");
        MOTAN_CONFIG_KEY_MAP.put("referer.accessLog", "client: if enable access log");
        MOTAN_CONFIG_KEY_MAP.put("referer.throwException", "client: if throwException when request fail");
        MOTAN_CONFIG_KEY_MAP.put("referer.retries", "client: request retry times");
        MOTAN_CONFIG_KEY_MAP.put("referer.asyncInitConnection", "client: init rpc connection async");
        MOTAN_CONFIG_KEY_MAP.put("service.id", "server: service config id");
        MOTAN_CONFIG_KEY_MAP.put("service.shareChannel", "server: if share server in same port");
        MOTAN_CONFIG_KEY_MAP.put("service.export", "server: server export protocol and port");
        MOTAN_CONFIG_KEY_MAP.put("service.accessLog", "server: if enable access log");
        MOTAN_CONFIG_KEY_MAP.put("service.registryConfigName", "server: registry config name");
        MOTAN_CONFIG_KEY_MAP.put("service.protocolConfigName", "server: protocol config name");
        MOTAN_CONFIG_KEY_MAP.put("service.registry", "server: registry id used by service");
        MOTAN_CONFIG_KEY_MAP.put("service.group", "server: rpc group");
        MOTAN_CONFIG_KEY_MAP.put("service.application", "server: rpc application");

        // registry config key
        MOTAN_CONFIG_REGISTRY_KEY_MAP.put("id", "registry: registry bean id");
        MOTAN_CONFIG_REGISTRY_KEY_MAP.put("regProtocol", "registry: registry protocol");
        MOTAN_CONFIG_REGISTRY_KEY_MAP.put("check", "registry: check registry init status");
        MOTAN_CONFIG_REGISTRY_KEY_MAP.put("excise", "registry: static or dynamic vintage");
        MOTAN_CONFIG_REGISTRY_KEY_MAP.put("address", "registry: address of registry");
        MOTAN_CONFIG_REGISTRY_KEY_MAP.put("port", "registry: port of registry");

        // protocol config key
        MOTAN_CONFIG_PROTOCOL_KEY_MAP.put("id", "protocol: protocol bean id");
        MOTAN_CONFIG_PROTOCOL_KEY_MAP.put("name", "protocol: protocol name");
        MOTAN_CONFIG_PROTOCOL_KEY_MAP.put("haStrategy", "client protocol: HA strategy");
        MOTAN_CONFIG_PROTOCOL_KEY_MAP.put("loadbalance", "client protocol: load balance");
        MOTAN_CONFIG_PROTOCOL_KEY_MAP.put("minWorkerThread", "server protocol: min worker num");
        MOTAN_CONFIG_PROTOCOL_KEY_MAP.put("maxWorkerThread", "server protocol: max worker num");
        MOTAN_CONFIG_PROTOCOL_KEY_MAP.put("workerQueueSize", "server protocol: worker queue size");
        MOTAN_CONFIG_PROTOCOL_KEY_MAP.put("maxServerConnection", "server protocol: max connection size");
        MOTAN_CONFIG_PROTOCOL_KEY_MAP.put("minClientConnection", "client protocol: min connection num per server node");
        MOTAN_CONFIG_PROTOCOL_KEY_MAP.put("maxClientConnection", "client protocol: max connection num per server node");
        MOTAN_CONFIG_PROTOCOL_KEY_MAP.put("serialization", "client protocol: serialization protocol");

        // common key's values
        MOTAN_CONFIG_KEY_VALUES.put("regProtocol", new String[]{"vintage"});
        MOTAN_CONFIG_KEY_VALUES.put("name", new String[]{"motan2"});
        MOTAN_CONFIG_KEY_VALUES.put("check", new String[]{"true", "false"});
        MOTAN_CONFIG_KEY_VALUES.put("excise", new String[]{"static", "dynamic"});
        MOTAN_CONFIG_KEY_VALUES.put("port", new String[]{"80"});
        MOTAN_CONFIG_KEY_VALUES.put("haStrategy", new String[]{"failover", "backupRequest", "failfast"});
        MOTAN_CONFIG_KEY_VALUES.put("loadbalance", new String[]{"roundrobin", "random", "configurableWeight", "consistent"});
        MOTAN_CONFIG_KEY_VALUES.put("accessLog", new String[]{"true", "false"});
        MOTAN_CONFIG_KEY_VALUES.put("throwException", new String[]{"true", "false"});
        MOTAN_CONFIG_KEY_VALUES.put("shareChannel", new String[]{"true", "false"});
        // option values
        MOTAN_OPTION_VALUES.put("with_motan_config", new String[]{"true", "false"});
        MOTAN_OPTION_VALUES.put("motan_config_type", new String[]{"xml", "yaml"});
    }


    public BreezeCompletionContributor() {
        completeKeyWords();
        completeOptionConfig();
        completeOption();
        completeType();
        completeMotanConfig();
    }

    void completeMotanConfig() {
        extend( // motan config name
                CompletionType.BASIC,
                PlatformPatterns.psiElement(NAME),
                new CompletionProvider<>() {
                    @Override
                    protected void addCompletions(
                            @NotNull CompletionParameters parameters,
                            @NotNull ProcessingContext context,
                            @NotNull CompletionResultSet result) {
                        PsiElement prevVisibleLeaf = PsiTreeUtil.prevVisibleLeaf(parameters.getPosition());
                        if (prevVisibleLeaf != null && "config".equals(prevVisibleLeaf.getText())) {
                            for (Map.Entry<String, String> entry : MOTAN_CONFIG_NAME_MAP.entrySet()) {
                                result.addElement(LookupElementBuilder.create(entry.getKey()).withTypeText(entry.getValue()));
                            }
                            addTypeNames(parameters, result, "MotanConfig", SERVICE);
                        }
                    }
                });
        extend( // motan config key
                CompletionType.BASIC,
                PlatformPatterns.psiElement(KEY),
                new CompletionProvider<>() {
                    @Override
                    protected void addCompletions(
                            @NotNull CompletionParameters parameters,
                            @NotNull ProcessingContext context,
                            @NotNull CompletionResultSet result) {
                        PsiElement nameElement = PsiTreeUtil.findSiblingBackward(parameters.getPosition(), NAME, null);
                        if (nameElement != null) {
                            String name = nameElement.getText();
                            if ("MotanBasicConfig".equals(name)) {
                                for (Map.Entry<String, String> entry : MOTAN_BASIC_CONFIG_KEY_MAP.entrySet()) {
                                    result.addElement(LookupElementBuilder.create(entry.getKey()).withTypeText(entry.getValue()));
                                }
                                for (Map.Entry<String, String> entry : MOTAN_CONFIG_KEY_MAP.entrySet()) {
                                    result.addElement(LookupElementBuilder.create(entry.getKey()).withTypeText(entry.getValue()));
                                }

                            } else if (name.endsWith("MotanConfig")) {
                                for (Map.Entry<String, String> entry : MOTAN_CONFIG_KEY_MAP.entrySet()) {
                                    result.addElement(LookupElementBuilder.create(entry.getKey()).withTypeText(entry.getValue()));
                                }
                                // service config
                                for (Map.Entry<String, String> entry : MOTAN_SERVICE_BASIC_CONFIG_KEY_MAP.entrySet()) {
                                    result.addElement(LookupElementBuilder.create(entry.getKey()).withTypeText(entry.getValue()));
                                }
                            } else if (name.startsWith("MotanRegistry")) {
                                for (Map.Entry<String, String> entry : MOTAN_CONFIG_REGISTRY_KEY_MAP.entrySet()) {
                                    result.addElement(LookupElementBuilder.create(entry.getKey()).withTypeText(entry.getValue()));
                                }
                            } else if (name.startsWith("MotanProtocol")) {
                                for (Map.Entry<String, String> entry : MOTAN_CONFIG_PROTOCOL_KEY_MAP.entrySet()) {
                                    result.addElement(LookupElementBuilder.create(entry.getKey()).withTypeText(entry.getValue()));
                                }
                            }
                        }
                    }
                });
    }

    void completeKeyWords() {
        extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement(UNKNOWN_WORD),
                new CompletionProvider<>() {
                    @Override
                    protected void addCompletions(
                            @NotNull CompletionParameters parameters,
                            @NotNull ProcessingContext context,
                            @NotNull CompletionResultSet result) {
                        for (String text : KEYWORDS) {
                            result.addElement(LookupElementBuilder.create(text));
                        }
                    }
                });
    }

    void completeOptionConfig() {
        extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement(KEY),
                new CompletionProvider<>() {
                    @Override
                    protected void addCompletions(
                            @NotNull CompletionParameters parameters,
                            @NotNull ProcessingContext context,
                            @NotNull CompletionResultSet result) {
                        PsiElement prevVisibleLeaf = PsiTreeUtil.prevVisibleLeaf(parameters.getPosition());
                        if (prevVisibleLeaf != null
                                && ("(".equals(prevVisibleLeaf.getText())
                                || ",".equals(prevVisibleLeaf.getText()))) {
                            for (String text : INNER_OPTION_KEYS) {
                                result.addElement(LookupElementBuilder.create(text));
                            }
                        }
                    }
                });
        extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement(VALUE),
                new CompletionProvider<>() {
                    @Override
                    protected void addCompletions(
                            @NotNull CompletionParameters parameters,
                            @NotNull ProcessingContext context,
                            @NotNull CompletionResultSet result) {
                        PsiElement prevVisibleLeaf = PsiTreeUtil.prevVisibleLeaf(parameters.getPosition());
                        if (prevVisibleLeaf != null && "=".equals(prevVisibleLeaf.getText())) {
                            prevVisibleLeaf = PsiTreeUtil.prevVisibleLeaf(prevVisibleLeaf);
                            if (prevVisibleLeaf != null) {
                                // config name
                                if (MATCH_CONFIG_VALUE_KEYS.contains(prevVisibleLeaf.getText())) {
                                    addTypeNames(parameters, result, "", CONFIG, CONFIG_KEYWORD);
                                }
                                // config values
                                for (Map.Entry<String, String[]> entry : MOTAN_CONFIG_KEY_VALUES.entrySet()) {
                                    if (prevVisibleLeaf.getText().endsWith(entry.getKey())) {
                                        for (String text : entry.getValue()) {
                                            result.addElement(LookupElementBuilder.create(text));
                                        }
                                    }
                                }
                                // value from setting
                                if ("address".equals(prevVisibleLeaf.getText()) && StringUtils.isNotBlank(BreezeSettingState.getInstance().defaultRegistryHost)) {
                                    result.addElement(LookupElementBuilder.create(BreezeSettingState.getInstance().defaultRegistryHost.trim()));
                                }
                                // option value
                                for (Map.Entry<String, String[]> entry : MOTAN_OPTION_VALUES.entrySet()) {
                                    if (entry.getKey().equals(prevVisibleLeaf.getText())) {
                                        for (String text : entry.getValue()) {
                                            result.addElement(LookupElementBuilder.create(text));
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
    }

    void completeType() {
        extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement(TYPE),
                new CompletionProvider<>() {
                    @Override
                    protected void addCompletions(
                            @NotNull CompletionParameters parameters,
                            @NotNull ProcessingContext context,
                            @NotNull CompletionResultSet result) {
                        for (String text : BASIC_TYPES) {
                            result.addElement(LookupElementBuilder.create(text));
                        }
                        addTypeNames(parameters, result, "", MESSAGE, MESSAGE_KEYWORD, ENUM, ENUM_KEYWORD);
                    }
                });
    }

    void completeOption() {
        extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement(KEY),
                new CompletionProvider<>() {
                    @Override
                    protected void addCompletions(
                            @NotNull CompletionParameters parameters,
                            @NotNull ProcessingContext context,
                            @NotNull CompletionResultSet result) {
                        PsiElement prevVisibleLeaf = PsiTreeUtil.prevVisibleLeaf(parameters.getPosition());
                        if (prevVisibleLeaf != null && "option".equals(prevVisibleLeaf.getText())) {
                            for (String text : OPTION_KEYS) {
                                result.addElement(LookupElementBuilder.create(text));
                            }
                        }
                    }
                });
    }

    private void addTypeNames(
            CompletionParameters parameters,
            CompletionResultSet result,
            String suffix,
            IElementType @NotNull ... types) {
        List<String> names = getTypeNames(parameters, types);
        for (String name : names) {
            result.addElement(LookupElementBuilder.create(name + (suffix == null ? "" : suffix)));
        }
    }

    private List<String> getTypeNames(CompletionParameters parameters, IElementType @NotNull ... types) {
        List<String> result = new ArrayList<>();
        PsiElement top = PsiTreeUtil.getTopmostParentOfType(parameters.getPosition(), BreezeFile.class);
        if (top != null) {
            PsiElement[] psiElements =
                    PsiTreeUtil.collectElements(
                            top,
                            e -> {
                                for (IElementType type : types) {
                                    if (e.getNode().getElementType() == type) {
                                        return true;
                                    }
                                }
                                return false;
                            });
            for (PsiElement element : psiElements) {
                if (element instanceof ASTWrapperPsiElement) {
                    result.add(BreezePsiImplUtil.getByTypes(element, BreezeTypes.NAME));
                } else if (element instanceof LeafPsiElement) {
                    PsiElement name = PsiTreeUtil.nextVisibleLeaf(element);
                    if (name != null && name.getNode().getElementType() == NAME) {
                        result.add(name.getText());
                    }
                }
            }
        }
        return result;
    }
}
