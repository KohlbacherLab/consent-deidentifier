package de.bfarm.mvh.grz;

import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Provenance;

import java.io.FileReader;
import java.io.FileWriter;

import static ca.uhn.fhir.context.FhirContext.forR4Cached;

public class ConsentDeidentifier {

  public static void main(String[] args) throws Exception {
    var parser = forR4Cached().newJsonParser();

    var root = parser.parseResource(new FileReader(args[0]));

    var fhirPath = forR4Cached().newFhirPath();

    fhirPath.evaluate(root, "descendants().ofType(Patient)", Patient.class)
        .forEach(p -> p.setIdentifier(null));

    fhirPath.evaluate(root, "descendants().ofType(Provenance)", Provenance.class)
        .forEach(p -> p.setSignature(null));

    fhirPath.evaluate(root, "descendants().where(reference.startsWith('Patient'))", Reference.class)
        .forEach(r -> r.setDisplay(null));

    parser.encodeResourceToWriter(root, new FileWriter(args[1]));
  }

}