# clearing-house-qu4lity

## Introduzione e modello dati

Quality Clearing House (QCH)  è un servizio generalizzato per la gestione della qualità negli scenari della catena di fornitura, supportato dall'infrastruttura QU4LITY Blockchain.  Consente un sistema comune di registrazione per un ecosistema di produzione in cui gli attori devono valutare continuamente la qualità delle materie prime, delle parti e dei prodotti finali e abbinare i risultati con gli standard contrattuali che possono cambiare frequentemente. Grazie alla tecnologia Blockchain, i records QCH sono sicuri e affidabili: sono immutabili nel tempo e non ripudiabili. I Data Storage e la logica di business vengono replicati su tutti i nodi, che sono gestiti allo stesso modo da tutti i partecipanti, in modo che non esista un unico "proprietario" del sistema che può introdurre pregiudizi nel processo.

I processi della Supply Chain supportati da QCH seguono un modello semplice, il cui flusso di lavoro è descritto di seguito. Per semplificare il modello e per semplicità, abbiamo indicato distinti attori che interpretano i tre ruoli incarnati nel sistema (Quality Master, Producer, Quality Assessor). Tuttavia, nei processi della supply chain del mondo reale è probabile che più organizzazioni svolgano il ruolo di fornitore e/o che una singola organizzazione svolga i ruoli rimanenti.
Tutti i record sono strutturati in modo da avere un proprio identificatore univoco, che viene utilizzato internamente per il riferimento incrociato e sono di proprietà dell'entità che li crea.
I modelli che li rappresentano sono i seguenti:

### Piano di qualità per ogni tipologia di prodotto - Quality Model

```public class QualityModel {
    private UUID id;
    private Integer  version;
    private URL      contract;
    private List<QualityParameter> qualityParameters ;
    }
```

### Lotto di prodotti omogenei - Shipping Unit Manifest

```public class Shipment {
    private UUID id;
    private UUID model;
    private List<Item> items;
    }
```

### Rilevazione delle misurazioni per ogni elemento ricevuto (parte di un lotto)- Quality Assessment (QA)

```public class QualityAssessment {
    private UUID id;
    private UUID shipment;
    private List<ItemAssessment> itemsAssessment;
}
```

### Item

```public class Item {
    private String id;
}
```

### Parametri di qualità (range) - QualityParameter

```public class QualityParameter {
    private String contractPath;
    private Double minVal;
    private Double maxVal;
}
```

### Misurazioni Effettive - ItemAssessment

```public class ItemAssessment {
    private String       id;
    private List<Double> values;
}
```

## Operation

E' prevista la scrittura di un record di QualityModel per ogni codice prodotto, al suo interno è presente una lista di misure (QualityParameter) da rispettare affinchè venga rispettato il piano di qualità.

E' prevista la scrittura di un record di Shipment per ogni lotto di spedizione composto solo da oggetti omogenei per tipologia di prodotto (potrebbe essere un pancale di oggetti ad esempio).

E' prevista la scrittura di un record di QualityAssessement per un insieme di oggetti, ognuno dei quali con le sue misurazioni effettive, è presente anche un riferimento al QualityModel (attraverso il collegamento con Shipment) e le misurazioni registrate sono esattamente quelle che corrispondono al QualityModel collegato, che descrive anche la tipologia di prodotto a cui ogni signolo oggetto appartiene.

Questi record sono collegati in questo modo:

Ad 1 QualityModel corrispondono n QualityAssessment a lui collegati, uno per ogni oggettp fisico per cui si possano fare le misurazioni descritte.
Ad 1 QualityModel corrispondono n Shipment, uno per ogni lotto spedito con al suo interno un certo numero di oggetti omogenei.

## Subject

Quality Master: produce i record di tipo QualityModel e provvede al loro inserimento sul Ledger
Producer: produce i record di tipo Shipment e provvede alla loro registrazione sul Ledger
Quality Assessor legge dal Ledger i record Shipment, legge la chiave del modello che gli corrisponde e legge i record di tipo QualityModel, dopocichè effettua effettivamente le misurazioni secondo quelli che sono i dati scritti sul piano di qualità e produce un record di tipo QualityAssessment che verrà anch'esso registrato sul ledger.
