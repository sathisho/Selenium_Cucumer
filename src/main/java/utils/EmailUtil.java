package utils;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Email Utility for sending test reports via email
 */
public class EmailUtil {
    
    private static final Logger logger = LogManager.getLogger(EmailUtil.class);
    private static Properties emailProperties;
    
    static {
        emailProperties = new Properties();
        try {
            FileInputStream fis = new FileInputStream("src/test/resources/email.properties");
            emailProperties.load(fis);
            fis.close();
        } catch (IOException e) {
            logger.error("Error loading email.properties file", e);
        }
    }
    
    /**
     * Send test report via email
     * @param testStatus - "PASSED" or "FAILED"
     * @param reportPath - Path to the report file
     */
    public static void sendReportEmail(String testStatus, String reportPath) {
        try {
            // Check if email is enabled
            String emailEnabledStr = emailProperties.getProperty("email.enabled", "false");
            boolean emailEnabled = Boolean.parseBoolean(emailEnabledStr);
            
            System.out.println("DEBUG: Email enabled property = " + emailEnabledStr);
            System.out.println("DEBUG: Email enabled parsed = " + emailEnabled);
            
            if (!emailEnabled) {
                System.out.println("ERROR: Email sending is disabled in email.properties");
                logger.warn("Email sending is disabled in email.properties");
                return;
            }
            
            // Check if email should be sent based on test status
            boolean sendOnSuccess = Boolean.parseBoolean(emailProperties.getProperty("send.on.success", "true"));
            boolean sendOnFailure = Boolean.parseBoolean(emailProperties.getProperty("send.on.failure", "true"));
            
            System.out.println("DEBUG: Test status = " + testStatus);
            System.out.println("DEBUG: Send on success = " + sendOnSuccess);
            System.out.println("DEBUG: Send on failure = " + sendOnFailure);
            
            if (testStatus.equals("FAILED") && !sendOnFailure) {
                logger.info("Skipping email for failed tests (send.on.failure=false)");
                return;
            }
            
            if (testStatus.equals("PASSED") && !sendOnSuccess) {
                logger.info("Skipping email for passed tests (send.on.success=false)");
                return;
            }
            
            MultiPartEmail email = new MultiPartEmail();
            email.setHostName(emailProperties.getProperty("smtp.host"));
            email.setSmtpPort(Integer.parseInt(emailProperties.getProperty("smtp.port")));
            
            // Get password from environment variable or properties file
            String password = System.getenv("GMAIL_APP_PASSWORD");
            System.out.println("DEBUG: Password from env variable = " + (password != null ? "SET" : "NULL"));
            
            if (password == null || password.isEmpty()) {
                password = emailProperties.getProperty("sender.password");
                System.out.println("DEBUG: Password from properties = " + (password != null && !password.isEmpty() ? "SET" : "EMPTY"));
            }
            
            String senderEmail = emailProperties.getProperty("sender.email");
            System.out.println("DEBUG: Sender email = " + senderEmail);
            System.out.println("DEBUG: Password available = " + (password != null && !password.isEmpty()));
            
            email.setAuthenticator(new org.apache.commons.mail.DefaultAuthenticator(
                    senderEmail,
                    password
            ));
            email.setSSLOnConnect(false);
            email.setStartTLSEnabled(true);
            email.setStartTLSRequired(true);
            email.setSSLOnConnect(false);
            
            System.out.println("DEBUG: SMTP Host = " + emailProperties.getProperty("smtp.host"));
            System.out.println("DEBUG: SMTP Port = " + emailProperties.getProperty("smtp.port"));
            System.out.println("DEBUG: Start TLS enabled");
            
            // Set From
            email.setFrom(emailProperties.getProperty("sender.email"));
            
            // Set To (recipient)
            String[] toEmails = emailProperties.getProperty("recipient.emails").split(",");
            System.out.println("DEBUG: Recipient emails count = " + toEmails.length);
            for (String toEmail : toEmails) {
                email.addTo(toEmail.trim());
                System.out.println("DEBUG: Added recipient = " + toEmail.trim());
            }
            
            // Set CC (if configured)
            String ccEmails = emailProperties.getProperty("cc.emails", "");
            if (!ccEmails.isEmpty()) {
                for (String ccEmail : ccEmails.split(",")) {
                    email.addCc(ccEmail.trim());
                }
            }
            
            // Set BCC (if configured)
            String bccEmails = emailProperties.getProperty("bcc.emails", "");
            if (!bccEmails.isEmpty()) {
                for (String bccEmail : bccEmails.split(",")) {
                    email.addBcc(bccEmail.trim());
                }
            }
            
            // Set Subject
            String subject = emailProperties.getProperty("email.subject") + " - " + testStatus;
            email.setSubject(subject);
            
            // Set Body
            String body = emailProperties.getProperty("email.body.prefix") + "\n\n" +
                    "Test Status: " + testStatus + "\n" +
                    "Timestamp: " + new java.util.Date() + "\n\n" +
                    "Please see the attached report for details.";
            email.setMsg(body);
            
            // Attach Report File
            File reportFile = new File(reportPath);
            System.out.println("DEBUG: Report file path = " + reportPath);
            System.out.println("DEBUG: Report file exists = " + reportFile.exists());
            if (reportFile.exists()) {
                email.attach(reportFile);
                logger.info("Attaching report file: " + reportPath);
            } else {
                logger.warn("Report file not found: " + reportPath);
                System.out.println("WARNING: Report file not found at: " + reportPath);
            }
            
            // Send Email
            System.out.println("DEBUG: Attempting to send email...");
            String messageId = email.send();
            System.out.println("SUCCESS: Email sent successfully! Message ID: " + messageId);
            logger.info("Email sent successfully! Message ID: " + messageId);
            
        } catch (EmailException e) {
            System.out.println("ERROR: Failed to send email - " + e.getMessage());
            if (e.getCause() != null) {
                System.out.println("ROOT CAUSE: " + e.getCause().getMessage());
                e.getCause().printStackTrace();
            }
            e.printStackTrace();
            logger.error("Error sending email report", e);
        } catch (Exception e) {
            System.out.println("ERROR: Unexpected error - " + e.getMessage());
            e.printStackTrace();
            logger.error("Unexpected error in email sending", e);
        }
    }
    
    /**
     * Send email with custom message
     * @param to - Recipient email
     * @param subject - Email subject
     * @param message - Email body
     * @param attachmentPath - Optional attachment path
     */
    public static void sendEmail(String to, String subject, String message, String attachmentPath) {
        try {
            boolean emailEnabled = Boolean.parseBoolean(emailProperties.getProperty("email.enabled", "false"));
            if (!emailEnabled) {
                logger.warn("Email sending is disabled");
                return;
            }
            
            MultiPartEmail email = new MultiPartEmail();
            email.setHostName(emailProperties.getProperty("smtp.host"));
            email.setSmtpPort(Integer.parseInt(emailProperties.getProperty("smtp.port")));
            
            // Get password from environment variable or properties file
            String password = System.getenv("GMAIL_APP_PASSWORD");
            if (password == null || password.isEmpty()) {
                password = emailProperties.getProperty("sender.password");
            }
            
            email.setAuthenticator(new org.apache.commons.mail.DefaultAuthenticator(
                    emailProperties.getProperty("sender.email"),
                    password
            ));
            email.setSSLOnConnect(false);
            email.setStartTLSEnabled(Boolean.parseBoolean(emailProperties.getProperty("smtp.starttls.enable")));
            email.setStartTLSRequired(Boolean.parseBoolean(emailProperties.getProperty("smtp.starttls.required")));
            
            email.setFrom(emailProperties.getProperty("sender.email"));
            email.addTo(to);
            email.setSubject(subject);
            email.setMsg(message);
            
            // Attach file if provided
            if (attachmentPath != null && !attachmentPath.isEmpty()) {
                File file = new File(attachmentPath);
                if (file.exists()) {
                    email.attach(file);
                }
            }
            
            String messageId = email.send();
            logger.info("Email sent successfully to: " + to);
            
        } catch (EmailException e) {
            logger.error("Error sending email", e);
        }
    }
}
