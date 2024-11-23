#pkmnambra

progetto per l'esame di Ingegneria del Software.
RPG ispirato alla modalità safari dei giochi pokemon.

#feature

- caricamento di mappa da file txt
- caricamento di dialoghi da file xml
- sistema di salvataggio con json
- tiles animati
- sistema di cattura safari

#pattern utilizzati

- factory: oggetti inventario, oggetti mappa, pokemons
- observer: ui cattura, mondo di gioco (da definire i metodi di update)
- singleton: gamestate
- state: utilizzato per la gestione degli schermi, built-in di GDX

#librerie utilizzate

- GDX
- GSON

#unittesting

- eseguito principalmente su factory, observer e singleton


#video di presentazione

il video mostra i seguenti momenti di gioco:
- l'avvio di una nuova partita
- il primo dialogo col professore (non è stato possibile trovare una skin adatta)
- il sistema di cattura
- lo schermo d'inventario con aggiornamento lista pokemon da catturare
- il salvataggio di gioco (non notificato in quanto basta premere S)
- il caricamento da file di salvataggio precedente (per mostrare la coerenza dei dati salvati)

[ATTENZIONE: se il video non entra in riproduzione, ricaricare la pagina e premere play SENZA skippare a parti del video successive; questo dovrebbe risolvere il problema]


https://github.com/user-attachments/assets/caf4bdf1-b958-4e8a-86ca-b8fd0988e937


https://github.com/user-attachments/assets/6c222f24-223e-4291-bcfb-effbc609c473


