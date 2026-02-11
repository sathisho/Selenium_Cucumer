# Email Report Configuration Guide

## Overview
This guide explains how to configure your Selenium Cucumber Framework to automatically send test reports via email to sathish.oruganti45@gmail.com

## Step 1: Enable 2-Factor Authentication in Gmail

For security reasons, Gmail doesn't allow regular passwords for third-party applications. You need to generate an **App Password**:

1. Go to your Google Account: https://myaccount.google.com/
2. Click **Security** in the left sidebar
3. Enable **2-Step Verification** (if not already enabled)
4. Go back to **Security**
5. Look for **App passwords** (this option appears after 2FA is enabled)
6. Select **Mail** and **Windows Computer** (or your device)
7. Google will generate a 16-character app password
8. Copy this password

## Step 2: Configure email.properties

The email configuration file is located at: `src/test/resources/email.properties`

### Update the following properties:

```properties
# SMTP Server Details (Gmail)
smtp.host=smtp.gmail.com
smtp.port=587
smtp.auth=true
smtp.starttls.enable=true
smtp.starttls.required=true
smtp.tls.protocol=TLSv1.2

# Gmail Credentials
sender.email=sathish.oruganti45@gmail.com
sender.password=YOUR_GMAIL_APP_PASSWORD   ← Paste your 16-char app password here

# Recipient Email(s)
recipient.emails=sathish.oruganti45@gmail.com

# Optional CC and BCC
cc.emails=
bcc.emails=

# Email Settings
email.subject=Selenium Test Automation Report
email.body.prefix=Please find the attached test execution report.

# When to send emails
send.on.failure=true    # Send when tests fail
send.on.success=true    # Send when tests pass
email.enabled=true      # Enable/disable email feature
```

## Step 3: Replace Your App Password

In `src/test/resources/email.properties`, replace:
```
sender.password=YOUR_GMAIL_APP_PASSWORD
```

With your actual 16-character app password from Step 1.

## Step 4: Build Project

```bash
cd /Users/origantisathish/Downloads/selenium-cucumber-framework
mvn clean install
```

## Step 5: Run Tests

The email will be sent automatically after test execution:

```bash
# Run all tests
mvn clean test

# Run specific tests
mvn test -Dcucumber.filter.tags="@smoke"
```

## What Gets Sent

### Email Details:
- **Subject**: "Selenium Test Automation Report - PASSED/FAILED"
- **Body**: Test execution summary with timestamp
- **Attachment**: ExtentReport.html (detailed HTML report)

### Report Attachment Includes:
- Test execution summary (passed/failed/skipped counts)
- Individual test results with timestamps
- Screenshots of failed tests
- Detailed step information
- Browser and OS information
- Execution logs

## Troubleshooting

### Issue: "Authentication failed" error
**Solution**: 
- Verify your app password is correct (16 characters)
- Make sure 2FA is enabled on your Google Account
- Try generating a new app password

### Issue: "Connection refused" error
**Solution**:
- Check your internet connection
- Ensure firewall doesn't block port 587
- Try with a different email provider

### Issue: "Email not sent but no error"
**Solution**:
- Check if `email.enabled=true` in email.properties
- Verify `send.on.success=true` or `send.on.failure=true`
- Check console logs for warnings

### Issue: "Report file not found"
**Solution**:
- Ensure `report.file.path=test-output/reports/ExtentReport.html` is correct
- Run tests first to generate the report

## Using Other Email Providers

### For Outlook/Microsoft:
```properties
smtp.host=smtp-mail.outlook.com
smtp.port=587
sender.email=your-outlook-email@outlook.com
sender.password=your-app-password
```

### For Yahoo Mail:
```properties
smtp.host=smtp.mail.yahoo.com
smtp.port=587
sender.email=your-yahoo-email@yahoo.com
sender.password=your-app-password
```

## Advanced Configuration

### Send Custom Email (Programmatically):

```java
EmailUtil.sendEmail(
    "recipient@example.com",
    "Custom Subject",
    "Custom message body",
    "path/to/attachment.pdf"
);
```

### Send Report Email (Programmatically):

```java
String testStatus = "PASSED"; // or "FAILED"
String reportPath = "test-output/reports/ExtentReport.html";
EmailUtil.sendReportEmail(testStatus, reportPath);
```

## Files Created/Modified

### New Files:
1. `src/main/java/utils/EmailUtil.java` - Email sending utility
2. `src/test/java/runners/TestEmailListener.java` - TestNG listener for email
3. `src/test/resources/email.properties` - Email configuration

### Modified Files:
1. `pom.xml` - Added email dependencies
2. `src/test/java/runners/TestRunner.java` - Added @Listeners annotation

## Email Dependencies Added

- `javax.mail` - Java Mail API
- `commons-email` - Apache Commons Email wrapper

## Testing Email Configuration

To verify email is working:

1. Run tests: `mvn test -Dcucumber.filter.tags="@smoke"`
2. Check console output for: `"Email sent successfully!"`
3. Check your email inbox for the report
4. Verify the attached HTML report opens correctly

## Security Best Practices

⚠️ **Important**:
- Never commit `email.properties` with real credentials to version control
- Use `.gitignore` to exclude sensitive files:
  ```
  src/test/resources/email.properties
  ```
- Store sensitive data in environment variables (optional)

### Using Environment Variables (Optional):

```java
String appPassword = System.getenv("GMAIL_APP_PASSWORD");
String senderEmail = System.getenv("GMAIL_SENDER_EMAIL");
```

## Support

If you encounter issues:
1. Check the console logs for error messages
2. Verify Gmail app password (not regular password)
3. Ensure 2FA is enabled on Gmail account
4. Check firewall/network settings
5. Review email.properties syntax

---

**Report Ready!** Your test reports will now be automatically sent to sathish.oruganti45@gmail.com 📧
