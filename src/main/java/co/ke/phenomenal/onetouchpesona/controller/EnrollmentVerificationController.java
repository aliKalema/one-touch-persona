package co.ke.phenomenal.onetouchpesona.controller;

import co.ke.phenomenal.onetouchpesona.model.EnrollRequest;
import co.ke.phenomenal.onetouchpesona.service.EnrollmentService;
import co.ke.phenomenal.onetouchpesona.service.VerificationService;
import co.ke.phenomenal.onetouchpesona.utils.StringUtils;
import com.digitalpersona.onetouch.processing.DPFPImageQualityException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class EnrollmentVerificationController {
    private final EnrollmentService enrollmentService;
    private  final VerificationService verificationService;
    @PostMapping("/enroll")
    public ResponseEntity<?> enrollFingerPrint( @RequestBody EnrollRequest enrollRequest) throws DPFPImageQualityException, InterruptedException {
        if(enrollRequest==null || StringUtils.isNullOrEmpty(enrollRequest.getCallbackRequest()) || StringUtils.isNullOrEmpty(enrollRequest.getId())){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        enrollmentService.enroll(enrollRequest);
        return new ResponseEntity<>(null,HttpStatus.PROCESSING);
    }
    @PostMapping("/verify")
    public ResponseEntity<byte[]> enrollFingerPrint() throws Exception {
        return new ResponseEntity<>(verificationService.verify(),HttpStatus.OK);
    }
}
