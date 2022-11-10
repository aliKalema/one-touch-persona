package co.ke.phenomenal.onetouchpesona.service;

import co.ke.phenomenal.onetouchpesona.model.EnrollRequest;
import co.ke.phenomenal.onetouchpesona.model.Finger;
import com.digitalpersona.onetouch.*;
import com.digitalpersona.onetouch.processing.DPFPEnrollment;
import com.digitalpersona.onetouch.processing.DPFPFeatureExtraction;
import com.digitalpersona.onetouch.processing.DPFPImageQualityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.io.InputStream;
import java.util.List;
import java.util.Vector;

@Service
@Slf4j
@RequiredArgsConstructor
public class EnrollmentService {
    private final ScanService scanService;
    private final RestTemplate restTemplate;
    public void enroll(EnrollRequest enrollRequest) throws DPFPImageQualityException, InterruptedException {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<byte[]> entity = new HttpEntity<>(createTemplate(scanService.listReaders().get(0)), headers);
        restTemplate.exchange(enrollRequest.getCallbackRequest(),HttpMethod.POST, entity , String.class);
    }

    private  byte[] createTemplate(String activeReader) throws InterruptedException, DPFPImageQualityException {
        DPFPFeatureExtraction featureExtractor = DPFPGlobal.getFeatureExtractionFactory().createFeatureExtraction();
        DPFPEnrollment enrollment = DPFPGlobal.getEnrollmentFactory().createEnrollment();
        List<Finger> fingers = List.of(Finger.values());
        Finger currentFinger = Finger.RightThumb;
        do{
            DPFPSample sample = scanService.getSample(activeReader);
            DPFPFeatureSet featureSet;
            try {
                featureSet = featureExtractor.createFeatureSet(sample, DPFPDataPurpose.DATA_PURPOSE_ENROLLMENT);
            } catch (DPFPImageQualityException e) {
                continue;
            }
            enrollment.addFeatures(featureSet);
        }while (currentFinger.next() != null);
        return enrollment.getTemplate().serialize();
    }
}
