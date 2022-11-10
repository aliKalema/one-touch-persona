package co.ke.phenomenal.onetouchpesona.service;

import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPSample;
import com.digitalpersona.onetouch.capture.DPFPCapture;
import com.digitalpersona.onetouch.capture.DPFPCapturePriority;
import com.digitalpersona.onetouch.capture.event.DPFPDataEvent;
import com.digitalpersona.onetouch.capture.event.DPFPDataListener;
import com.digitalpersona.onetouch.capture.event.DPFPReaderStatusAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPReaderStatusEvent;
import com.digitalpersona.onetouch.readers.DPFPReaderDescription;
import com.digitalpersona.onetouch.readers.DPFPReadersCollection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ScanService {
    public DPFPSample getSample(String activeReader) throws InterruptedException {
        final LinkedBlockingQueue<DPFPSample> samples = new LinkedBlockingQueue<DPFPSample>();
        DPFPCapture capture = DPFPGlobal.getCaptureFactory().createCapture();
        capture.setReaderSerialNumber(activeReader);
        capture.setPriority(DPFPCapturePriority.CAPTURE_PRIORITY_LOW);
        capture.addDataListener(new DPFPDataListener(){
            public void dataAcquired(DPFPDataEvent e) {
                if (e != null && e.getSample() != null) {
                    try {
                        samples.put(e.getSample());
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        capture.addReaderStatusListener(new DPFPReaderStatusAdapter(){
            int lastStatus = DPFPReaderStatusEvent.READER_CONNECTED;
            public void readerConnected(DPFPReaderStatusEvent e) {
                if (lastStatus != e.getReaderStatus())
                    log.info("Reader is connected");
                lastStatus = e.getReaderStatus();
            }
            public void readerDisconnected(DPFPReaderStatusEvent e) {
                if (lastStatus != e.getReaderStatus())
                    log.error("Reader is disconnected");
                lastStatus = e.getReaderStatus();
            }

        });
        try {
            capture.startCapture();
            return samples.take();
        }
        catch (RuntimeException e) {
            log.error("Failed to start capture. Check that reader is not used by another application");
            throw e;
        }
        finally { capture.stopCapture();}
    }

    public List<String> listReaders() {
        DPFPReadersCollection readers = DPFPGlobal.getReadersFactory().getReaders();
        if (readers == null) {
           throw new RuntimeException("NULL POINTER ON READERS");
        }
        return readers.stream().map(DPFPReaderDescription:: getSerialNumber).collect(Collectors.toList());
    }
}
