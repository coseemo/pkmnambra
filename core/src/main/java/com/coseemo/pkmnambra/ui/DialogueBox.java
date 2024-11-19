package com.coseemo.pkmnambra.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class DialogueBox extends Table {
    private String targetText = "";
    private float animTimer = 0f;
    private final float TIME_PER_CHARACTER = 0.05f;
    private STATE currentState = STATE.IDLE;
    private final Label textLabel;
    private boolean isTextComplete = false;

    private enum STATE {
        ANIMATING,
        IDLE
    }

    public DialogueBox(Skin skin) {
        super(skin);
        this.setBackground("dialoguebox");

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = skin.getFont("font");

        textLabel = new Label("", style);
        textLabel.setWrap(true);

        this.add(textLabel)
            .expand()
            .fill()
            .pad(10f);

        this.setSize(300f, 50f);
    }

    public void animateText(String text) {
        if (text == null || text.isEmpty()) {
            System.err.println("Warning: Attempted to animate empty or null text");
            return;
        }

        targetText = text;
        animTimer = 0f;
        currentState = STATE.ANIMATING;
        isTextComplete = false;
        textLabel.setText("");
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (currentState == STATE.ANIMATING) {
            animTimer += delta;
            int charsToShow = (int) (animTimer / TIME_PER_CHARACTER);

            if (charsToShow >= targetText.length()) {
                // Animazione completata
                textLabel.setText(targetText);
                currentState = STATE.IDLE;
                isTextComplete = true;
            } else {
                // Mostra i caratteri progressivamente
                textLabel.setText(targetText.substring(0, charsToShow));
            }
        }
    }

    public void completeAnimation() {
        if (currentState == STATE.ANIMATING) {
            textLabel.setText(targetText);
            currentState = STATE.IDLE;
            isTextComplete = true;
        }
    }

    public boolean isAnimating() {
        return currentState == STATE.ANIMATING;
    }

    public boolean isTextComplete() {
        return isTextComplete;
    }

    public void reset() {
        currentState = STATE.IDLE;
        animTimer = 0f;
        targetText = "";
        isTextComplete = false;
        textLabel.setText("");
    }

    @Override
    public float getPrefWidth() {
        return 300f;
    }

    @Override
    public float getPrefHeight() {
        return 50f;
    }
}
