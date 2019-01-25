import com.sforce.soap.metadata.*;
import com.sforce.soap.metadata.Error;

/**
 * Sample that logs in and creates a custom object through the metadata api
 */
public class CRUDSample {
    private MetadataConnection metadataConnection;

    // one second in milliseconds
    private static final long ONE_SECOND = 1000;

    public CRUDSample() {
    }

    public static void main(String[] args) throws Exception {
        CRUDSample crudSample = new CRUDSample();
        crudSample.runCreate();
    }

    /**
     * Create a custom object. This method demonstrates usage of the
     * create() and checkStatus() calls.
     *
     * @param uniqueName Custom object name should be unique.
     */
    private void createCustomObject(final String uniqueName) throws Exception {
        final String label = "My Custom Object 2";
        CustomObject customObject = new CustomObject();
        customObject.setFullName(uniqueName);
        customObject.setDeploymentStatus(DeploymentStatus.Deployed);
        customObject.setDescription("Created by the Metadata API Asynchronous Sample");
        customObject.setLabel(label);
        customObject.setPluralLabel(label + "s");
        customObject.setSharingModel(SharingModel.ReadWrite);

        // The name field appears in page layouts, related lists, and elsewhere.
        CustomField nf = new CustomField();
        nf.setType(FieldType.Text);
        nf.setDescription("The custom object identifier on page layouts, related lists etc");
        nf.setLabel(label);
        nf.setFullName(uniqueName);
        customObject.setNameField(nf);

        SaveResult[] asyncResults = metadataConnection.createMetadata(
            new CustomObject[]{customObject});
        if (asyncResults == null) {
            System.out.println("The object was not created successfully");
            return;
        }

        long waitTimeMilliSecs = ONE_SECOND;
        printAsyncResultStatus(asyncResults);
 
    }

    private void printAsyncResultStatus(SaveResult[] asyncResults) throws Exception {
        if (asyncResults == null || asyncResults.length == 0 || asyncResults[0] == null) {
            throw new Exception("The object status cannot be retrieved");
        }

        SaveResult r = asyncResults[0]; //we are creating only 1 metadata object

        if (r.isSuccess()) {
            System.out.println("Created component: " + r.getFullName());
        } else {
            System.out
                    .println("Errors were encountered while creating "
                            + r.getFullName());
            for (Error e : r.getErrors()) {
                System.out.println("Error message: " + e.getMessage());
                System.out.println("Status code: " + e.getStatusCode());
            }
        }
    }

    private void runCreate() throws Exception {
        metadataConnection = MetadataLoginUtil.login();
        // Custom objects and fields must have __c suffix in the full name.
        final String uniqueObjectName = "MyCustomObject2__c";
        createCustomObject(uniqueObjectName);
    }
}