import com.sforce.soap.metadata.*;
import com.sforce.soap.metadata.Error;
import com.sforce.ws.ConnectionException;

/**
 * Sample that logs in and creates a custom object through the metadata API
 */
public class UpdateWorkFlowAlert {
    private MetadataConnection metadataConnection;

    // one second in milliseconds
    private static final long ONE_SECOND = 1000;

    public UpdateWorkFlowAlert() {
    }

    public static void main(String[] args) throws Exception {
        UpdateWorkFlowAlert wf = new UpdateWorkFlowAlert();
        wf.runCreate();
    }

    /**
     * Create a custom object. This method demonstrates usage of the
     * create() and checkStatus() calls.
     *
     * @param uniqueName Custom object name should be unique.
     */
    public void Update() {
        try {
        	WorkflowAlert wfa = new WorkflowAlert();
            wfa.setFullName("Lead.Email_notification_when_new_lead_is_created");
            // Name field with a type and label is required
            wfa.setCcEmails(new String[] {"khannajeeb362@gmail.com"});
            wfa.setDescription("Updated description");
            wfa.setTemplate("Unfiled Public Classic Email Templates/new_email_on_lead");
            wfa.setProtected(false); 
            wfa.setSenderType(ActionEmailSenderType.CurrentUser);
            SaveResult[] results = metadataConnection
                    .updateMetadata(new Metadata[] { wfa });

            for (SaveResult r : results) {
                if (r.isSuccess()) {
                    System.out.println("Updated component: " + r.getFullName());
                } else {
                    System.out
                            .println("Errors were encountered while updating "
                                    + r.getFullName());
                    for (Error e : r.getErrors()) {
                        System.out.println("Error message: " + e.getMessage());
                        System.out.println("Status code: " + e.getStatusCode());
                    }
                }
            }
        } catch (ConnectionException ce) {
            ce.printStackTrace();
        }
    }

    private void runCreate() throws Exception {
        metadataConnection = MetadataLoginUtil.login();
        // Custom objects and fields must have __c suffix in the full name.
        Update();
    }
}