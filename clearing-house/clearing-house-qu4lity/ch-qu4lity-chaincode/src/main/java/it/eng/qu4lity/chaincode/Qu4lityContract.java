package it.eng.qu4lity.chaincode;

import com.owlike.genson.Genson;
import it.eng.qu4lity.model.QualityAssessment;
import it.eng.qu4lity.model.QualityModel;
import it.eng.qu4lity.model.Shipment;
import it.eng.qu4lity.model.utils.JsonHandler;
import org.hyperledger.fabric.contract.ClientIdentity;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Contract(
        name = "Qu4lityContract",
        info = @Info(
                title = "Clearing House Quality Chaincode",
                description = "The Clearing House Quality contract",
                version = "1.0.0-SNAPSHOT",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html")))
@Default
public final class Qu4lityContract implements ContractInterface {
    private static final Logger logger = Logger.getLogger(Qu4lityContract.class.getName());
    private final Genson genson = new Genson();

    private enum CHErrors {
        QUALITYMODEL_NOT_FOUND,
        QUALITYMODEL_ALREADY_EXISTS,

        SHIPMENT_NOT_FOUND,
        SHIPMENT_ALREADY_EXISTS,

        QUALITYASSESSMENT_NOT_FOUND,
        QUALITYASSESSMENT_ALREADY_EXISTS,
        CLASSNAME_NOTFOUND,
        RECORD_NOTFOUND

    }

    public enum QuorumMappingDir {
        QualityModel,
        Shipment,
        QualityAssessment
    }


    public Qu4lityContract() {
    }

    public void init(ChaincodeStub stub) {
        logger.fine("MethodName: INIT ->\n" );
        logger.fine("Everything will be fine!" );
    }

    @Override
    public void unknownTransaction(Context ctx) {
        writeMethodInfo("unknownTransaction");
    }

    /* Generic methods: editRecordQu4lity e retrieveRecordQu4lity */

    /**
     * @param context
     * @param record
     * @param typeRecord
     * @return
     */
    @Transaction
    public String editRecordQu4lity(final Context context, final String record, final String typeRecord ) throws Exception {
        ChaincodeStub stub = null;
        stub = context.getStub();
        ClientIdentity clientIdentity = new ClientIdentity(stub);
        final String mspId = clientIdentity.getMSPID();

        writeMethodInfo("editRecordQu4lity");
        // String typeRecordName = record.getClass().getSimpleName().toLowerCase();
        logger.fine("MethodName: editRecordQu4lity ->\n");
        logger.fine("Object arrived from JSONP Input ->\n" + typeRecord);
        String responseMethod = null;
        if (typeRecord.equals(QualityModel.class.getSimpleName().toLowerCase())) {

            QualityModel deserializeQM = (QualityModel) JsonHandler.convertFromJson(record, QualityModel.class);
            responseMethod = editQualityModel(context, stub, mspId, deserializeQM);
        } else {
            if (typeRecord.equals(Shipment.class.getSimpleName().toLowerCase())) {
                Shipment deserializeSH = (Shipment) JsonHandler.convertFromJson(record, Shipment.class);
                responseMethod = editShipment(context,  stub, mspId, deserializeSH);
            } else {
                if (typeRecord.equals(QualityAssessment.class.getSimpleName().toLowerCase())) {
                    QualityAssessment deserializeQA = (QualityAssessment) JsonHandler.convertFromJson(record, QualityAssessment.class);
                    responseMethod = editQualityAssessment(context,  stub, mspId, deserializeQA);
                } else {
                    String errorMessage = String.format("Class record %s not exists into the Ledger", typeRecord);
                    logger.severe(errorMessage);
                    throw new ChaincodeException(errorMessage, CHErrors.CLASSNAME_NOTFOUND.toString());
                }
            }
        }
        return responseMethod;
    }


    /**
     * @param context
     * @param id
     * @param typeRecord
     * @return
     */
    @Transaction
    private String retrieveRecordQu4lity(final Context context, final String id, final String typeRecord) throws Exception {
        ChaincodeStub stub = null;
        stub = context.getStub();
        ClientIdentity clientIdentity = new ClientIdentity(stub);
        final String mspId = clientIdentity.getMSPID();

        writeMethodInfo("retrieveRecordQu4lity");
        logger.fine("MethodName: retrieveRecordQu4lity ->\n");
        logger.fine("Object arrived from JSONP Input ->\n" + typeRecord);
        String responseMethod = null;
        if (typeRecord.toLowerCase().equals(QualityModel.class.getSimpleName().toLowerCase())) {
            responseMethod = getQualityModel(context, stub, mspId, id) + "=" + QualityModel.class.getSimpleName().toLowerCase();
        } else {
            if (typeRecord.toLowerCase().equals(Shipment.class.getSimpleName().toLowerCase())) {
                responseMethod = getShipment(context, stub, mspId, id) + "=" + Shipment.class.getSimpleName().toLowerCase();
            } else {
                if (typeRecord.toLowerCase().equals(QualityAssessment.class.getSimpleName().toLowerCase())) {
                    responseMethod = getQualityAssessment(context, stub, mspId, id) + "=" + QualityAssessment.class.getSimpleName().toLowerCase();
                } else {
                    String errorMessage = String.format("Class record %s not exists into the Ledger", typeRecord);
                    logger.severe(errorMessage);
                    throw new ChaincodeException(errorMessage, CHErrors.CLASSNAME_NOTFOUND.toString());
                }
            }
        }
        return responseMethod;
    }


    /* Specific methods: editQualityModel, editShipment e editQualityAssessment */

    /**
     * @param context
     * @param stub
     * @param mspId
     * @param qualityModel
     * @return
     */
    @Transaction
    private String editQualityModel(final Context context, final ChaincodeStub stub, final String mspId, final QualityModel qualityModel) throws Exception {

        writeMethodInfo("editQualityModel");
        logger.fine("MethodName: editQualityModel ->\n");
        logger.fine("QualityModel arrived from JSONP Input ->\n" + qualityModel);

        final String id = qualityModel.getId().toString();
        String qualityModelState = "";
        // qualityModelState = stub.getStringState(id);
        qualityModelState = stub.getPrivateDataUTF8(mspId, id);
        if (qualityModelState != null && !qualityModelState.isEmpty()) {
            // String errorMessage = String.format("QualityModel %s already exists", id);
            // logger.severe(errorMessage);
            // throw new ChaincodeException(errorMessage, CHErrors.QUALITYMODEL_ALREADY_EXISTS.toString());
            logger.fine("QualityModel %s already exists ->\n");
            logger.fine("QualityModel %s updated \n");
        }
        qualityModelState = JsonHandler.convertToJson(qualityModel);
        // stub.putStringState(id, qualityModelState);
        stub.putPrivateData(mspId, id, qualityModelState);
        logger.fine("QualityModel correctly inserted into Ledger :-) \n: " + id);
        return qualityModelState;
    }


    /**
     * @param context
     * @param stub
     * @param mspId
     * @param shipment
     * @return
     */
    @Transaction
    private String editShipment(final Context context,  final ChaincodeStub stub, final String mspId, final Shipment shipment) throws Exception {

        writeMethodInfo("editShipment");
        logger.fine("MethodName: editShipment ->\n");
        logger.fine("Shipment arrived from JSONP Input ->\n" + shipment);

        final String id = shipment.getId().toString();
        String shipmentState = "";
        shipmentState = stub.getStringState(id);
        if (shipmentState != null && !shipmentState.isEmpty()) {
            // String errorMessage = String.format("Shipment %s already exists", id);
            // logger.severe(errorMessage);
            // throw new ChaincodeException(errorMessage, CHErrors.SHIPMENT_ALREADY_EXISTS.toString());
            logger.fine("Shipment %s already exists ->\n");
            logger.fine("Shipment %s updated \n");
        }
        shipmentState = JsonHandler.convertToJson(shipment);
        // stub.putStringState(id, shipmentState);
        stub.putPrivateData(mspId, id, shipmentState);
        logger.fine("Shipment correctly inserted into Ledger :-) \n: " + id);
        return shipmentState;
    }


    /**
     * @param context
     * @param stub
     * @param mspId
     * @param qualityAssessment
     * @return
     */
    @Transaction
    private String editQualityAssessment(final Context context,  final ChaincodeStub stub, final String mspId, final QualityAssessment qualityAssessment) throws Exception {
        writeMethodInfo("editQualityAssessment");
        logger.fine("MethodName: editQualityAssessment ->\n");
        logger.fine("QualityAssessment arrived from JSONP Input ->\n" + qualityAssessment);

        final String id = qualityAssessment.getId().toString();
        String qualityAssessmentState = "";
        // qualityAssessmentState = stub.getStringState(id);
        qualityAssessmentState = stub.getPrivateDataUTF8(mspId, id);
        if (qualityAssessmentState != null && !qualityAssessmentState.isEmpty()) {
            // String errorMessage = String.format("QualityAssessment %s already exists", id);
            // logger.severe(errorMessage);
            // throw new ChaincodeException(errorMessage, CHErrors.QUALITYASSESSMENT_ALREADY_EXISTS.toString());
            logger.fine("QualityAssessment %s already exists ->\n");
            logger.fine("QualityAssessment %s updated \n");
        }
        qualityAssessmentState = JsonHandler.convertToJson(qualityAssessment);
        // stub.putStringState(id, qualityAssessmentState);
        stub.putPrivateData(mspId, id, qualityAssessmentState);
        logger.fine("QualityAssessment correctly inserted into Ledger :-) \n: " + id);
        return qualityAssessmentState;
    }


    /**
     * @param context
     * @param stub
     * @param mspId
     * @param id
     * @return
     */

    @Transaction()
    private String getQualityModel(final Context context,  final ChaincodeStub stub, final String mspId, final String id) throws Exception {
        writeMethodInfo("getQualityModel");
        logger.fine("Trying to get QualityModel with ID: " + id);
        String qualityModelState = null;
        // qualityModelState = stub.getStringState(id);
        qualityModelState = stub.getPrivateDataUTF8(mspId, id);
        stub.setEvent("event", null);
        if (qualityModelState == null || qualityModelState.isEmpty()) {
            String errorMessage = String.format("QualityModel %s does not exist", id);
            logger.severe(errorMessage);
            throw new ChaincodeException(errorMessage, CHErrors.QUALITYMODEL_NOT_FOUND.toString());
        }
        // If the type record is correct this Cast is ok.
        QualityModel qm = (QualityModel) JsonHandler.convertFromJson(qualityModelState, QualityModel.class);

        logger.info("QualityModel retrieved\n: " + qualityModelState);
        //return (LogNotification) JsonHandler.convertFromJson(messageState, LogNotification.class);
        return qualityModelState;
    }


    /**
     * @param context
     * @param stub
     * @param mspId
     * @param id
     * @return
     */

    @Transaction()
    private String getShipment(final Context context, final ChaincodeStub stub, final String mspId, final String id) throws Exception {
        writeMethodInfo("getShipment");
        logger.fine("Trying to get Shipment with ID: " + id);
        String shipmentState = null;
        // shipmentState = stub.getStringState(id);
        shipmentState = stub.getPrivateDataUTF8(mspId, id);
        stub.setEvent("event", null);
        if (shipmentState == null || shipmentState.isEmpty()) {
            String errorMessage = String.format("Shipment %s does not exist", id);
            logger.severe(errorMessage);
            throw new ChaincodeException(errorMessage, CHErrors.SHIPMENT_NOT_FOUND.toString());
        }

        // If the type record is correct this Cast is ok.
        Shipment sh = (Shipment) JsonHandler.convertFromJson(shipmentState, Shipment.class);

        logger.info("Shipment retrieved\n: " + shipmentState);
        //return (LogNotification) JsonHandler.convertFromJson(messageState, LogNotification.class);
        return shipmentState;
    }


    /**
     * @param context
     * @param stub
     * @param mspId
     * @param id
     * @return
     */

    @Transaction()
    private String getQualityAssessment(final Context context,  final ChaincodeStub stub, final String mspId, final String id) throws Exception {
        writeMethodInfo("getQualityAssessment");
        logger.fine("Trying to get QualityAssessment with ID: " + id);
        String qualityAssessmentState = null;
        // qualityAssessmentState = stub.getStringState(id);
        qualityAssessmentState = stub.getPrivateDataUTF8(mspId, id);
        stub.setEvent("event", null);
        if (qualityAssessmentState == null || qualityAssessmentState.isEmpty()) {
            String errorMessage = String.format("QualityAssessment %s does not exist", id);
            logger.severe(errorMessage);
            throw new ChaincodeException(errorMessage, CHErrors.QUALITYASSESSMENT_NOT_FOUND.toString());
        }

        // If the type record is correct this Cast is ok.
        QualityAssessment qa = (QualityAssessment) JsonHandler.convertFromJson(qualityAssessmentState, QualityAssessment.class);
        logger.info("QualityAssessment retrieved\n: " + qualityAssessmentState);
        //return (LogNotification) JsonHandler.convertFromJson(messageState, LogNotification.class);
        return qualityAssessmentState;
    }

    /**
     * @param context
     * @return
     */

    @Transaction
    public String[] getAllQualityModels(final Context context) throws Exception {
        writeMethodInfo("getAllQualityModels");
        ChaincodeStub stub = null;
        stub = context.getStub();
        List<String> stringArgs = null;
        String QUERY_ALL = "{\n" +
                "\"selector\": {}\n" +
                "}";
        final QueryResultsIterator<KeyValue> stateByRange = stub.getQueryResult(QUERY_ALL);
        if (null != stateByRange && stateByRange.iterator().hasNext()) {
            logger.info((" stateByRange is FULL !!!"));
            stringArgs = new ArrayList<>();
            for (KeyValue result : stateByRange) {
                logger.info((" stateByRange value " + result.getStringValue()));
                stringArgs.add(result.getStringValue());
            }
        }
        if (null == stringArgs || stringArgs.isEmpty()) {
            String errorMessage = String.format("No qualityModel in ledger");
            logger.severe(errorMessage);
            throw new ChaincodeException(errorMessage, CHErrors.QUALITYMODEL_NOT_FOUND.toString());
        }
        /*
        List<LogNotification> ledgerMessages = new ArrayList<>();
        for (String s : stringArgs
        ) {
            ledgerMessages.add((LogNotification) JsonHandler.convertFromJson(s, LogNotification.class));
        }*/
        return stringArgs.toArray(new String[stringArgs.size()]);

    }


    /**
     * @param context
     * @param stub
     * @param mspId
     * @param id
     * @param typeRecord
     * @return
     */

    @Transaction
    public void deleteRecordQuality(final Context context,  final ChaincodeStub stub, final String mspId, final String id, final String typeRecord) throws Exception {
        writeMethodInfo("deleteRecordQuality");
        logger.fine("Trying to Delete Record with ID: " + id);
        //String recordState = stub.getStringState(id);
        String recordState = stub.getPrivateDataUTF8(mspId, id);
        if (recordState == null || recordState.isEmpty()) {
            String errorMessage = String.format("Not exists the Record with id: ", id);
            logger.severe(errorMessage);
            throw new ChaincodeException(errorMessage, CHErrors.RECORD_NOTFOUND.toString());
        }
        stub.delState(id);
        stub.setEvent("event", null);
        logger.info("Record with id\n: " + id + " deleted!");
    }

    private void writeMethodInfo(String methodName) {
        logger.info(this.getClass().getSimpleName() + " " + methodName + " called at " + LocalDateTime.now());
    }


}
