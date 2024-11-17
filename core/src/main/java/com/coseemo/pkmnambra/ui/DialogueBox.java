package com.coseemo.pkmnambra.ui;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

public class DialogueBox extends Table {
    private String targetText = "";
    private float animTimer = 0f;
    private float animationTotalTime = 0f;
    private float TIME_PER_CHARACTER = 0.05f;
    private STATE state = STATE.IDLE;
    private Label textLabel;
    private boolean debug = true;

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

        this.setSize(200f, 50f);
    }

    public void animateText(String text) {
        if (text == null || text.isEmpty()) {
            System.out.println("Warning: empty or null text!");
            return;
        }

        targetText = text;
        animationTotalTime = text.length() * TIME_PER_CHARACTER;
        state = STATE.ANIMATING;
        animTimer = 0f;

        // Mostra immediatamente il testo
        setText(text);

        if (debug) {
            System.out.println("Starting animation for text: " + text);
        }
    }

    public boolean isFinished() {
        return state == STATE.IDLE;
    }

    public void reset() {
        state = STATE.IDLE;
        animTimer = 0f;
        animationTotalTime = 0f;
        targetText = "";
        setText("");
    }

    private void setText(String text) {
        if (textLabel == null) {
            System.out.println("Warning: textLabel is null!");
            return;
        }

        if (text == null) {
            text = "";
        }

        textLabel.setText(text);

        if (debug) {
            System.out.println("Text set to: " + text);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (state == STATE.ANIMATING) {
            animTimer += delta;
            if (animTimer >= animationTotalTime) {
                state = STATE.IDLE;
                animTimer = animationTotalTime;
                setText(targetText);
                if (debug) {
                    System.out.println("Animation completed");
                }
            }
        }
    }

    public void completeAnimation() {
        animTimer = animationTotalTime;
        state = STATE.IDLE;
        setText(targetText);
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
