package com.sipesistemas;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ide.CopyPasteManager;

import java.awt.datatransfer.StringSelection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.intellij.openapi.actionSystem.CommonDataKeys.EDITOR;

public class CopyDjangoModelFieldsToTypescript extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        StringBuilder fields = new StringBuilder();
        Editor editor = e.getData(EDITOR);
        if (editor == null) {
            return;
        }
        String textoSelecionado = editor.getCaretModel().getCurrentCaret().getEditor()
                .getSelectionModel().getSelectedText();
        String regex = "(\\S+)\\s*=\\s*models\\.([a-zA-Z][a-zA-Z]+)[^(]*\\(([^)]*)\\)";
        Pattern pattern = Pattern.compile(regex);

        if (textoSelecionado == null) {
            return;
        }

        Matcher m = pattern.matcher(textoSelecionado);
        while (m.find()) {
            String nome = m.group(1);
            String tipo = m.group(2);

            fields.append(nome);
            fields.append(": ");
            fields.append(getTipo(tipo));
            fields.append(";");
            fields.append('\n');
        }
        CopyPasteManager.getInstance().setContents(new StringSelection(fields.toString()));
    }

    private String getTipo(String tipoDjango) {
        switch (tipoDjango) {
            case "CharField":
            case "TextField":
            case "FileField":
            case "ImageField":
                return "string";
            case "IntegerField":
            case "FloatField":
            case "PositiveIntegerField":
            case "SmallIntegerField":
            case "PositiveSmallIntegerField":
            case "DecimalField":
            case "TimeField":
                return "number";
            case "BooleanField":
                return "boolean";
            case "DateField":
            case "DateTimeField":
                return "Date";
            default:
                return "any";
        }
    }
}
