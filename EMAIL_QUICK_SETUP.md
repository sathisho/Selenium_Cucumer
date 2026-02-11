# Email Report Setup - Quick Reference

## ⚡ Quick Setup (5 minutes)

### 1. Generate Gmail App Password
- Go to: https://myaccount.google.com/apppasswords
- Sign in if needed
- Select **Mail** and **Windows Computer**
- Copy the 16-character password

### 2. Update Configuration
Edit `src/test/resources/email.properties`:
```properties
sender.email=sathish.oruganti45@gmail.com
sender.password=PASTE_16_CHAR_PASSWORD_HERE
recipient.emails=sathish.oruganti45@gmail.com
email.enabled=true
```

### 3. Build & Run
```bash
cd /Users/origantisathish/Downloads/selenium-cucumber-framework
mvn clean test -Dcucumber.filter.tags="@smoke"
```

### 4. Check Email
- Open inbox: sathish.oruganti45@gmail.com
- Look for: "Selenium Test Automation Report - PASSED/FAILED"
- Download attached HTML report

---

## 🔧 Configuration Options

### Send Email Conditionally
```properties
send.on.success=true    # Email when all tests pass
send.on.failure=true    # Email when any test fails
email.enabled=true      # Master on/off switch
```

### Add CC/BCC Recipients
```properties
cc.emails=manager@company.com,lead@company.com
bcc.emails=archive@company.com
```

### Customize Subject & Body
```properties
email.subject=QA Test Results - Automation Suite
email.body.prefix=Hi Team, Here's the latest test execution report.
```

---

## 📧 What Gets Emailed

**Subject**: `Selenium Test Automation Report - PASSED/FAILED`

**Body**: 
```
Please find the attached test execution report for the Selenium automation tests.

Test Status: PASSED (or FAILED)
Timestamp: [Date and Time]

Please see the attached report for details.
```

**Attachment**: `ExtentReport.html`
- Full test execution details
- Screenshots of failed tests
- Step-by-step test logs
- Browser/OS information
- Test duration

---

## 🛠️ Troubleshooting

| Issue | Solution |
|-------|----------|
| **Auth failed** | Wrong app password - regenerate it |
| **Connection error** | Check internet, firewall port 587 |
| **Email not sent** | Check email.enabled=true and send.on.* settings |
| **Report not attached** | Run tests first to generate report |

---

## 📁 Files Created/Modified

**New Files:**
- ✨ `src/main/java/utils/EmailUtil.java`
- ✨ `src/test/java/runners/TestEmailListener.java`
- ✨ `src/test/resources/email.properties`
- ✨ `EMAIL_SETUP_GUIDE.md` (detailed guide)

**Modified:**
- 📝 `pom.xml` (added email dependencies)
- 📝 `src/test/java/runners/TestRunner.java` (added listener)

---

## 🚀 Running Tests with Email

```bash
# All tests with email
mvn clean test

# Only smoke tests with email
mvn test -Dcucumber.filter.tags="@smoke"

# Only regression tests with email
mvn test -Dcucumber.filter.tags="@regression"

# Specific feature with email
mvn test -Dtest=TestRunner -Dcucumber.features="src/test/resources/features/Login.feature"
```

---

## 🔐 Security Notes

⚠️ **Never share your app password!**

- Don't commit `email.properties` with real credentials
- Add to `.gitignore`: `src/test/resources/email.properties`
- Use environment variables in CI/CD pipelines

---

## ✅ Verification Checklist

- [ ] Gmail 2FA enabled
- [ ] App password generated (16 characters)
- [ ] email.properties updated with app password
- [ ] Build succeeds: `mvn clean compile`
- [ ] Tests run and send email: `mvn test -Dcucumber.filter.tags="@smoke"`
- [ ] Email received in inbox
- [ ] HTML report attachment opens correctly

---

**All Set!** 🎉 Your test reports will now be automatically emailed to sathish.oruganti45@gmail.com
