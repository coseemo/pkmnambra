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


https://github.com/user-attachments/assets/caf4bdf1-b958-4e8a-86ca-b8fd0988e937


https://github.com/user-attachments/assets/6c222f24-223e-4291-bcfb-effbc609c473


