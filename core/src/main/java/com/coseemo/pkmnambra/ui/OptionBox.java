package com.coseemo.pkmnambra.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class OptionBox extends Table {

    private int selectorIndex = 0;

    private List<Image> arrows = new ArrayList<>();
    private List<Label> options = new ArrayList<>();

    public OptionBox(Skin skin) {
        super(skin);
        this.setBackground("optionbox");
    }

    public void addOption(String option) {
        Label optionLabel = new Label(option, this.getSkin());
        options.add(optionLabel);

        Image selectorLabel = new Image(this.getSkin(), "arrow");
        selectorLabel.setScaling(Scaling.none);
        selectorLabel.setVisible(false);
        arrows.add(selectorLabel);

        // Creiamo una nuova riga per la freccia e l'opzione
        Table optionRow = new Table();
        optionRow.add(selectorLabel).width(30).height(30).align(Align.center).padRight(5f); // Spazio tra la freccia e l'etichetta
        optionRow.add(optionLabel).expandX().align(Align.left); // Espandi l'etichetta per occupare lo spazio

        // Aggiungi la riga alla OptionBox
        this.add(optionRow).fillX().pad(5f);
        this.row(); // Aggiungi una nuova riga per la prossima opzione

        // Aggiorna la visibilità della freccia
        updateArrowVisibility();
    }

    public void moveUp() {
        selectorIndex--;
        if (selectorIndex < 0) {
            selectorIndex = 0;
        }
        updateArrowVisibility();
    }

    public void moveDown() {
        selectorIndex++;
        if (selectorIndex >= arrows.size()) {
            selectorIndex = arrows.size() - 1;
        }
        updateArrowVisibility();
    }

    public void clearChoices() {
        this.clearChildren();
        options.clear();
        arrows.clear();
        selectorIndex = 0;
    }

    public int getIndex() {
        return selectorIndex;
    }

    // Metodo di utilità per aggiornare la visibilità delle frecce
    private void updateArrowVisibility() {
        for (int i = 0; i < arrows.size(); i++) {
            arrows.get(i).setVisible(i == selectorIndex);
        }
    }

    public List<String> getChoices() {
        List<String> choices = new ArrayList<>();
        for (Label option : options) {
            choices.add(option.getText().toString());
        }
        return choices;
    }

}
