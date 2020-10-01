# clearing-house-qu4lity

## Introduzione e modello dati

Quality Clearing House (CHQ)  is a generalized service for quality management in supply chain scenarios, supported by the infrastructure QU4LITY Blockchain.  Enable a common registration system for a manufacturing ecosystem in which actors must continuously evaluate the quality of raw materials, parts and final products and match results with contractual standards that can change frequently. 
Thanks to Blockchain technology, CHQ records are safe and reliable: they are immutable over time and cannot be repudiated. 
Data Storage and business logic are replicated across all nodes, which are managed equally by all participants, so that there is no single "owner" of the system that can introduce bias into the process.

The Supply Chain processes supported by CQH follow a simple model, the workflow of which is described below. To simplify the model and for simplicity, we have indicated distinct actors who play the three roles embodied in the system (Quality Master, Producer, Quality Assessor). However, in real-world supply chain processes, multiple organizations are likely to play the supplier role and / or a single organization to play the remaining roles.

All records are structured to have their own unique identifier, which is used internally for cross-referencing and are owned by the entity that creates them.

The models that represent them are the following:

### Quality plan for each type of product - Quality Model

```
public class QualityModel {
    private UUID id;
    private Integer  version;
    private URL      contract;
    private List<QualityParameter> qualityParameters ;
    }
```

### Lot of homogeneous products - Shipping Unit Manifest

```
public class Shipment {
    private UUID id;
    private UUID model;
    private List<Item> items;
    }
```

### Taking measurements for each item received (part of a lot)- Quality Assessment (QA)

```
public class QualityAssessment {
    private UUID id;
    private UUID shipment;
    private List<ItemAssessment> itemsAssessment;
}
```

### Item

```
public class Item {
    private String id;
}
```

### Quality parameters (range) - QualityParameter

```
public class QualityParameter {
    private String contractPath;
    private Double minVal;
    private Double maxVal;
}
```

### Actual Measurements - ItemAssessment

```
public class ItemAssessment {
    private String       id;
    private List<Double> values;
}
```

## Operation

A QualityModel record is written for each product code, inside there is a list of measures (QualityParameter) to be respected in order to respect the quality plan.

It is foreseen the writing of a Shipment record for each shipment lot composed only of homogeneous objects by type of product (it could be a pallet of objects for example).

It is foreseen the writing of a QualityAssessement record for a set of objects, each of which with its actual measurements, there is also a reference to the QualityModel (through the connection with Shipment) and the recorded measurements are exactly those that correspond to the QualityModel connected, which also describes the type of product to which each individual object belongs.

These records are linked like this:

To a QualityModel correspond n QualityAssessments connected to it, one for each physical object for which the described measurements can be made.
A QualityModel corresponds to n Shipment, one for each lot shipped with a certain number of homogeneous objects inside.

## Subject

Quality Master: it produces the QualityModel type records and inserts them on the Ledger
Producer: produces the Shipment records and records them on the Ledger
Quality Assessor reads the Shipment records from the Ledger, reads the model key that corresponds to it and reads the QualityModel type records, after which it actually carries out the measurements according to the data written on the quality plan and produces a QualityAssessment record which will also it registered on the ledger.

## Chaincode

### Installation

#### Prerequisites

* Linux Environment.
* Administrative access to the machine.
* Access to Internet.
* Install **Hyperledger Fabric version 1.4** following this installation [guide](https://hyperledger-fabric.readthedocs.io/en/release-1.4/build_network.html#building-your-first-network).
* Clone the repository.: `git clone https://github.com/Engineering-Research-and-Development/qu4lity-dapps.git`
* Copy the `chaincode` folder of CHQ project under your HLF installation machine.
* To install the chaincode follow the instructions given in the following guide: [guide](https://hyperledger-fabric.readthedocs.io/en/release-1.4/chaincode4noah.html#installing-chaincode)
