package it.eng.qu4lity.client;

import it.eng.qu4lity.client.impl.CHQu4lityServiceImpl;
import it.eng.qu4lity.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class End2EndTest {
    private CHQu4lityServiceImpl chQu4lityService;


    @Before
    public void setup() {

        try {
            chQu4lityService = new CHQu4lityServiceImpl();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void editRetrieveTest() {

        QualityModel qm = new QualityModel();
        qm.setId(UUID.randomUUID());
        QualityParameter qualityParameter = new QualityParameter();
        qualityParameter.setId("qp");
        qualityParameter.setMaxVal(10.0);
        List<QualityParameter> qualityParameters = new ArrayList<>();
        qualityParameters.add(qualityParameter);
        qm.setQualityParameter(qualityParameters);

        try {
            chQu4lityService.editRecordQu4lity(qm);
            final QualityModel qualityModel = (QualityModel) chQu4lityService.retrieveRecordQu4lity(qm.getId().toString(), QualityModel.class.getName());
            System.out.println(qualityModel.getId() + qualityModel.getQualityParameter().get(0).getMaxVal().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Unit test for simple App.
     */



        @Test
        public void testEditRetrieveRecordQu4lity() throws Exception {

            CHQu4lityServiceImpl asi = new CHQu4lityServiceImpl();

            URL url = new URL("http://example.com/");

            UUID qmUid = UUID.randomUUID();


            QualityParameter qpTemp = new QualityParameter("temp",0.00,50.00);
            QualityParameter qpCap = new QualityParameter("cap",0.00,100.00);
            QualityParameter qpPres = new QualityParameter("pres",0.00,75.00);
            ArrayList<QualityParameter> values = new ArrayList<QualityParameter>()   ;
            values.add(qpTemp);
            values.add(qpCap);
            values.add(qpPres);
            QualityModel qm = new QualityModel(qmUid,1,url,values);

            System.out.println("CREATED QualityModel with id: " + qmUid.toString());
            try {
                asi.editRecordQu4lity(qm);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("EDITED QualityModel with id: " + qmUid.toString());

            UUID shUid = UUID.randomUUID();
            Shipment sh = new Shipment(shUid,qmUid);
            Item it = new Item("itemId");
            sh.additem(it);

            System.out.println("CREATED Shipment with id: " + shUid.toString());
            try {
                asi.editRecordQu4lity(sh);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("EDITED Shipment with id: " + shUid.toString());

            UUID qaUid = UUID.randomUUID();
            QualityAssessment qa = new QualityAssessment(qaUid,shUid);
            ItemAssessment ia = new ItemAssessment("itemAssessmentId");
            ArrayList<Double> valuesAL = new ArrayList<Double>();
            valuesAL.add(15.0);
            valuesAL.add(32.0);
            valuesAL.add(49.0);
            ia.setValues(valuesAL);
            qa.addItemAssessment(ia);

            System.out.println("CREATED QualityAssessment with id: " + qaUid.toString());
            try {
                asi.editRecordQu4lity(qa);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("EDITED QualityAssessment with id: " + qaUid.toString());



            // retrieveRecordQuality
            System.out.println("RETRIEVE QualityModel with id: " + qmUid.toString());
            QualityModel qmRetrieve = null;
            try {
                qmRetrieve = (QualityModel) asi.retrieveRecordQu4lity(qmUid.toString(),qm);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Assert.assertEquals(qmRetrieve,qm);
            System.out.println("RETRIEVED QualityModel with id: " + qmUid.toString());


        }

}

