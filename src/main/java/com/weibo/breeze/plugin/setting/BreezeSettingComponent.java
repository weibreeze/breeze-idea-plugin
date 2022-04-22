package com.weibo.breeze.plugin.setting;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author zhanglei28
 * @date 2022/3/23.
 */
public class BreezeSettingComponent {
    private final JPanel breezeSettingPanel;
    private final JBTextField generateUrlText = new JBTextField();
    private final JBTextField generatedDirText = new JBTextField();
    private final JBTextField defaultRegistryHostText = new JBTextField();
    private final ComboBox targetLanguage = new ComboBox(new String[]{"auto", "java", "go", "php", "cpp", "lua"});

    public BreezeSettingComponent() {
        breezeSettingPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("Enter generate server URL: "), generateUrlText, 1, false)
                .addLabeledComponent(new JBLabel("Enter generated dir: "), generatedDirText, 1, false)
                .addLabeledComponent(new JBLabel("Enter default registry host: "), defaultRegistryHostText, 1, false)
                .addLabeledComponent(new JBLabel("Enter target language: "), targetLanguage, 1, false)
                .addComponentFillVertically(new JPanel(), 0).getPanel();
    }

    public JPanel getPanel() {
        return breezeSettingPanel;
    }

    public JComponent getPreferredFocusedComponent() {
        return generateUrlText;
    }

    @NotNull
    public String getGenerateUrlText() {
        return generateUrlText.getText();
    }

    public void setGenerateUrlText(@NotNull String newText) {
        generateUrlText.setText(newText);
    }

    public String getGeneratedDirText() {
        return generatedDirText.getText();
    }

    public void setGeneratedDirText(@NotNull String newText) {
        generatedDirText.setText(newText);
    }

    public String getDefaultRegistryHostText() {
        return defaultRegistryHostText.getText();
    }

    public void setDefaultRegistryHostText(@NotNull String newText) {
        defaultRegistryHostText.setText(newText);
    }

    public String getTargetLanguage() {
        return targetLanguage.getModel().getSelectedItem().toString();
    }

    public void setTargetLanguage(@NotNull String newText) {
        targetLanguage.getModel().setSelectedItem(newText);
    }
}