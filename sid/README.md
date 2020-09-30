# SID

### Introduzione




TODO




### Operazioni

L'applicazione SID è composta da due soluzioni software lato client, e uno *smart contract* Blockchain. Il primo applicativo  client offre un interfaccia interettiva a linea di comando (*sid-command-line*) con la quale viene gestito il wallet personale dell utente. Con esso sarà possibile creare, visualizzare ed accedere alle proprie identità.  Il secondo applicativo (*sid-rest-api)* offre invece un intereffaccia visualizzabile sull indirizzo ***http://localhost:8080/swagger-ui.html***. Tale intefaccia espone alcune API per interagire con le funzionalità dello *smartcontract*. 
### Installazione del client

**Requisiti di sistema**

- Java JRE 1.8 o superiore
- Apache Maven 3.6.3 o superiore
- Connessione a Internet priva di proxy

**Installazione**

Una volta clonato i due progetti client in locale, è possibili tramite il comando da terminale (su ciascuna root dei due progettI)	***mvn install***   generare i due JAR.
Entrambi i JAR utilizzano le credenziali di  Fabric per connettersi alla Blockchain. Tali credenziali sono incampsulate all interno di una cartella, denominata Wallet, che è possibile generare seguendo la guida ufficiale a questo  [link](https://hyperledger-fabric.readthedocs.io/en/latest/write_first_app.html#first-the-application-enrolls-the-admin-user) link per creare tale cartella con le credenziali di Fabric. Una volta creata la cartella Wallet può essere salvata in qualsisi punto ma e il suo percorso andrà settato come variabile di ambiente del proprio sistema operativo (se non viene creata tale variabile di ambiente i due JAR cercheranno tali configurazioni all interno home utente).

#### sid-command-line


Il client non è interattivo: per eseguire una singola operazione, deve essere lanciato dalla shell di sistema con un comando specifico. Il comando è così strutturato (si assume che il Java JRE sia stato configurato correttamente):
	
	java - jar <percorso del file eseguibile>_
	
	- c <comando: list, stat, gen , show> (obbligatorio)
	- p <password: password per cifrare/decifrare il proprio wallet> (obbligatorio)
	- a <address: address della identity che vogliamo visualizzare, obbligatorio insieme al comando SHOW> 
	- h  <HELP: lista dei comandi> (opzionale)
	
Esempi:


		> java -jar wallet-tool.jar -c gen -p password
		> java -jar wallet-tool.jar -c list -p password
		> java -jar wallet-tool.jar -c stat -p password
		> java -jar wallet-tool.jar -c show -p password  -a 5aBCvP1QoskN5pSxL1nSA221utj5LBAfu



	
		
#### sid-rest-api

***

L'applicativo esponde tre API, Post, Get e Put:

 **POST** 
 Permette di censire da parte di un utente *controller* una nuova identità.

I paremetri richiesti sono:

- **ctrlAddr** : address del controller scelto tra quelli presenti nel proprio wallet
- **password** : password per  decodificare il proprio wallet
- **pubKey** : Publickey in formato Base64 appartenente al nuovo  utente da censire 
	
		Request URL: 
		http://localhost:8080/identity/?ctrlAddr%20=addressController&password=password&pubKey=public ke

**GET** 
Permette di recuperare le informazioni principale di una identity attiva. Se l'operazione termina con successo viene restituita la seguente struttura dati:

	SID {
		address	string
		controller	string
		created	string
		pubKey	string
		status	integer($int32)
	  }
L'unico paramentro richiesto è l'address di una identity presente ed attiva sul ledger.

	Request URL: http://localhost:8080/identity/address

**PUT**
Permette di modificare lo stato di una identity presente sul ledger. Ci sono tre operazioni possibile : **revoke** in grado di revocare MASSI TODO operazioni revoke activare e suspend

I parametri richiesti sono:

- **addr**: address delle identity al quale apportare un cambio di stato.
- **ctrlAddr** : address del controller scelto tra quelli presenti nel proprio wallet, e effettivamente controllore delle identiy da modificare.
- **op** : nome operazione  tra revoke, activate, suspend .
- **password** : password per  decodificare il proprio wallet.
	
	
		Request URL http://localhost:8080/identity/address/revoke?ctrlAddr=controllerAddress&password=password

#### Codici di risposta
 todo
