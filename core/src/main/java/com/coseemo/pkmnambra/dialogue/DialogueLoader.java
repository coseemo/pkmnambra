package com.coseemo.pkmnambra.dialogue;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class DialogueLoader extends AsynchronousAssetLoader<DialogueDb, DialogueLoader.DialogueParameter> {
    private final DialogueDb dialogueDb;

    public DialogueLoader(FileHandleResolver resolver) {
        super(resolver);
        this.dialogueDb = DialogueDb.getInstance();
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, DialogueParameter parameter) {
        try {
            XmlReader reader = new XmlReader();
            Element root = reader.parse(file);

            if (!root.getName().equals("Dialogues")) {
                throw new IllegalStateException("Root node must be 'Dialogues' in " + fileName);
            }

            for (Element dialogueElement : root.getChildrenByName("dialogue")) {
                loadDialogue(dialogueElement);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading dialogues from " + fileName, e);
        }
    }

    private void loadDialogue(Element dialogueElement) {
        String name = dialogueElement.getAttribute("name");
        Dialogue dialogue = new Dialogue();

        // Per i dialoghi lineari
        Array<Element> linearNodes = dialogueElement.getChildrenByName("linear");
        for (Element node : linearNodes) {
            int id = Integer.parseInt(node.getAttribute("id"));
            String text = node.getAttribute("text");

            DialogueNode dialogueNode = new DialogueNode(text, id);

            // Gestione puntatore al prossimo nodo
            Element pointer = node.getChildByName("pointer");
            if (pointer != null) {
                int target = Integer.parseInt(pointer.getAttribute("target"));
                dialogueNode.makeLinear(target);
            }

            dialogue.addNode(dialogueNode);
        }

        // Per i dialoghi a scelta multipla
        Array<Element> choiceNodes = dialogueElement.getChildrenByName("choice");
        for (Element node : choiceNodes) {
            int id = Integer.parseInt(node.getAttribute("id"));
            String text = node.getAttribute("text");

            DialogueNode dialogueNode = new DialogueNode(text, id);

            // Carica le opzioni
            Array<Element> options = node.getChildrenByName("option");
            for (Element option : options) {
                String optionText = option.getAttribute("text");
                int target = Integer.parseInt(option.getAttribute("target"));
                dialogueNode.addChoice(optionText, target);  // Corretto: passa il testo dell'opzione e il target
            }

            dialogue.addNode(dialogueNode);
        }

        dialogueDb.addDialogue(name, dialogue);
    }

    @Override
    public DialogueDb loadSync(AssetManager manager, String fileName, FileHandle file, DialogueParameter parameter) {
        return dialogueDb;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, DialogueParameter parameter) {
        return null;
    }

    static public class DialogueParameter extends AssetLoaderParameters<DialogueDb> {
    }
}
