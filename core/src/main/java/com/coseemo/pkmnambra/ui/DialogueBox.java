package com.coseemo.pkmnambra.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

public class DialogueBox extends Table {

    private String targetText = "";
    private float animTimer = 0f;
    private float animationTotalTime = 0f;
    private STATE state = STATE.IDLE;

    private final Label textLabel;

    private enum STATE {
        ANIMATING,
        IDLE,
        ;
    }

    public DialogueBox(Skin skin) {
        super(skin);
        this.setBackground("dialoguebox");
        textLabel = new Label("\n", skin);
        this.add(textLabel).expand().align(Align.left).pad(5f);
    }

    public void animateText(String text) {
        targetText = text;
        float TIME_PER_CHARACTER = 0.05f;
        animationTotalTime = text.length()* TIME_PER_CHARACTER;
        state = STATE.ANIMATING;
        animTimer = 0f;
    }

    public boolean isFinished() {
        return state == STATE.IDLE;
    }

    private void setText(String text) {
        if (!text.contains("\n")) {
            text += "\n";
        }
        this.textLabel.setText(text);
    }

    @Override
    public void act(float delta) {
        if (state == STATE.ANIMATING) {
            animTimer += delta;
            if (animTimer > animationTotalTime) {
                state = STATE.IDLE;
                animTimer = animationTotalTime;
            }
            StringBuilder actuallyDisplayedText = new StringBuilder();
            int charactersToDisplay = (int)((animTimer/animationTotalTime)*targetText.length());
            for (int i = 0; i < charactersToDisplay; i++) {
                actuallyDisplayedText.append(targetText.charAt(i));
            }
            if (!actuallyDisplayedText.toString().equals(textLabel.getText().toString())) {
                setText(actuallyDisplayedText.toString());
            }
        }
    }

    @Override
    public float getPrefWidth() {
        return 200f;
    }
}
