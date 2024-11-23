package com.coseemo.pkmnambra.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

public class DialogueBox extends Table {

    private String targetText = "";         // Testo che deve essere animato
    private float animTimer = 0f;           // Timer per l'animazione del testo
    private float animationTotalTime = 0f;  // Tempo totale per l'animazione in base alla lunghezza del testo
    private STATE state = STATE.IDLE;      // Stato dell'animazione: IDLE (fermo) o ANIMATING (in corso)

    private final Label textLabel;         // Etichetta per visualizzare il testo

    // Definisce gli stati possibili per l'animazione del testo
    private enum STATE {
        ANIMATING,  // Lo stato in cui il testo è in animazione
        IDLE,       // Lo stato in cui non c'è animazione
    }

    // Costruttore: inizializza la finestra del dialogo con uno sfondo e una label per il testo
    public DialogueBox(Skin skin) {
        super(skin);
        this.setBackground("dialoguebox");  // Imposta lo sfondo della finestra del dialogo
        textLabel = new Label("\n", skin);   // Crea un'etichetta vuota per il testo
        this.add(textLabel).expand().align(Align.left).pad(5f);  // Aggiunge la label alla tabella (con un po' di padding)
    }

    // Metodo per iniziare l'animazione del testo
    public void animateText(String text) {
        targetText = text;  // Imposta il testo da animare
        float TIME_PER_CHARACTER = 0.05f;  // Tempo per ogni carattere dell'animazione
        animationTotalTime = text.length() * TIME_PER_CHARACTER;  // Calcola il tempo totale per animare tutto il testo
        state = STATE.ANIMATING;  // Cambia lo stato a ANIMATING per avviare l'animazione
        animTimer = 0f;  // Resetta il timer dell'animazione
    }

    // Verifica se l'animazione del testo è completata
    public boolean isFinished() {
        return state == STATE.IDLE;  // Se lo stato è IDLE, l'animazione è terminata
    }

    // Imposta il testo nell'etichetta
    private void setText(String text) {
        if (!text.contains("\n")) {
            text += "\n";  // Aggiunge un ritorno a capo se non presente
        }
        this.textLabel.setText(text);  // Imposta il testo nell'etichetta
    }

    @Override
    public void act(float delta) {
        if (state == STATE.ANIMATING) {
            animTimer += delta;  // Aumenta il timer in base al tempo passato
            if (animTimer > animationTotalTime) {
                state = STATE.IDLE;  // Se il timer supera il tempo totale, finisce l'animazione
                animTimer = animationTotalTime;
            }

            // Calcola quanti caratteri devono essere mostrati in base al progresso dell'animazione
            StringBuilder actuallyDisplayedText = new StringBuilder();
            int charactersToDisplay = (int) ((animTimer / animationTotalTime) * targetText.length());
            for (int i = 0; i < charactersToDisplay; i++) {
                actuallyDisplayedText.append(targetText.charAt(i));  // Aggiungi ogni carattere da visualizzare
            }

            // Se il testo visualizzato è cambiato, aggiorna la label
            if (!actuallyDisplayedText.toString().equals(textLabel.getText().toString())) {
                setText(actuallyDisplayedText.toString());
            }
        }
    }

    @Override
    public float getPrefWidth() {
        return 200f;  // Imposta la larghezza preferita della finestra del dialogo
    }
}
