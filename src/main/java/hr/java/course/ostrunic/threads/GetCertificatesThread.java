package hr.java.course.ostrunic.threads;

import hr.java.course.ostrunic.model.Certificate;

import java.util.ArrayList;
import java.util.List;

public class GetCertificatesThread extends DatabaseThread implements Runnable {
    private List<Certificate> certificates = new ArrayList<>();
    public List<Certificate> getCertificates() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return certificates;
    }

    @Override
    public void run() {
        certificates = super.getCertificatesThread();
    }
}
