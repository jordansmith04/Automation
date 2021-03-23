using OpenQA.Selenium;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Xunit;

namespace EHBsBDDFramework.Utility
{
    class Login
    {
        public static class Se
        {
            public static IWebElement Header => Driver.Find(By.ClassName("main_title"));
            public static IWebElement logout => Driver.Find(By.ClassName("logout"));
            public static IWebElement expandHeader => Driver.Find(By.Id("expandCollapseAnchor"));

        }

        /// <summary> Do all of the login steps with one method call </summary>
        public void login(string username, string password)
        {
            CheckPreLoginWarning();
            EnterUserName(username);
            EnterPassword(password);
            DoLogin();
            AcknowledgeAgreement();
        }

        /// <summary> Login for PMS </summary>
        public void PMSlogin(string []username, string password)
        {
            CheckPreLoginWarning();
            for (int i = 0; i < username.Length; i++)
            {
                try
                {
                    EnterUserName(username[i]);
                    EnterPassword(password);
                    AcceptTerms();
                    Driver.Instance.FindElement(By.Id("submitBtn")).Click();

                   IWebElement errmsg = Driver.GetElement(ElementType.ClassName, "pms-article");
                   bool isError = errmsg.Text.Trim().Contains("You have exceeded the allowed concurrent login limit");
                    if (!isError)
                        break;
                    else if (i == username.Length - 1)
                        Console.WriteLine("Login Failed. No user available.");
                }
                catch (Exception ex)
                { throw new Exception("Login Failed. No user available.", ex); }
            }
        }

        public void PMSLogout(String baseURL)
        {
            try
            {
                Driver.GoToUrl(baseURL);
                Driver.Wait(2);
                Driver.Instance.FindElement(By.XPath("//*[@id='mm-0']/div[1]/div[1]/div[2]/div/a[3]/i")).Click();
                Driver.Wait(2);
            }
            catch (Exception ex)
            { 
                throw new Exception("Logout Failed.", ex); 
            }
        }

        #region login page fields and buttons

        /// <summary> Enter the username </summary>
        public void EnterUserName(string userName)
        {
            if (Driver.Instance.Url.ToLower().Contains("signin"))  // For New Login Page, UserName's Client Id is "UserName"
                Driver.Instance.FindElement(By.Id("UserName")).SendKeys(userName);
            else if (Driver.Instance.Url.ToLower().Contains("pms"))
            {
                Driver.Instance.FindElement(By.Id("username")).Clear();
                Driver.Instance.FindElement(By.Id("username")).SendKeys(userName);
            }
            else if (Driver.Instance.Url.ToLower().Contains("grants.gov"))
            {
                Driver.Instance.FindElement(By.Id("form:userId")).Clear();
                Driver.Instance.FindElement(By.Id("form:userId")).SendKeys(userName);
            }
            else
                Driver.Instance.FindElement(By.Id("ctl00_MainContent_loginControl_txtUserName")).SendKeys(userName);
        }

        /// <summary> Enters the password. </summary>
        public void EnterPassword(string password)
        {
            if (Driver.Instance.Url.ToLower().Contains("signin"))  // For New Login Page, Password field's Client Id is "Password"
                Driver.Instance.FindElement(By.Id("Password")).SendKeys(password);
            else if (Driver.Instance.Url.ToLower().Contains("pms"))
                Driver.Instance.FindElement(By.Id("password")).SendKeys(password);
            else if (Driver.Instance.Url.ToLower().Contains("grants.gov"))
                Driver.Instance.FindElement(By.Id("form:password")).SendKeys(password);
            else
                Driver.Instance.FindElement(By.Id("ctl00_MainContent_loginControl_txtPassword")).SendKeys(password);
        }
        
        /// <summary> Accepts terms (checkbox). </summary>
        public void AcceptTerms()
        {
                Driver.ClickElement("Id", "cbGovTermsAgree");
        }

        /// <summary> Click the OK button on the overlay that displays on the login page </summary>
        public void CheckPreLoginWarning()
        {
            Driver.Instance.Manage().Timeouts().ImplicitWait = TimeSpan.FromSeconds(1);
            var loginWarningMsgButton = Driver.Instance.FindElements(By.Id("btnPreLoginWarningMessageOK"));
            if (loginWarningMsgButton.Count > 0)
                loginWarningMsgButton[0].Click();
            Driver.Wait(1);
        }

        /// <summary> Click the button to actually log in and submit username/password </summary>
        public void DoLogin()
        {
            try
            {
                Driver.Instance.FindElement(By.Id("Login")).Click();
            }
            catch
            {
                try
                { 
                    Driver.Instance.FindElement(By.Id("ctl00_MainContent_loginControl_btnLogin")).Click(); 
                }
                catch
                {
                    try
                    {
                        Driver.MoveToElement(By.Id("form:loginButton"));
                        Driver.Instance.FindElement(By.Id("form:loginButton")).Click();
                    }
                    catch
                    {
                        try
                        {
                            Driver.Wait(1);
                        }
                        catch (Exception ex)
                        {
                            throw new Exception("Login Failed. Could not click on the Login Button.", ex);
                        }
                    }
                }
            }
        }

        public void AcknowledgeAgreement()
        {
            //Driver.Instance.Manage().Timeouts().ImplicitWait = TimeSpan.FromSeconds(1);
            var AcceptButton = Driver.Instance.FindElements(By.Id("Accept"));
            if (AcceptButton.Count > 0)
                AcceptButton[0].Click();
            try
            {
                Driver.MoveToElement("Id", "ctl00_MainContent_userAgreementControl_pgeAction_ButtonRight1");
                Driver.ClickElement("Id", "ctl00_MainContent_userAgreementControl_pgeAction_ButtonRight1");
            }
            catch { }
            //Driver.Wait(1);
        }

        #endregion

        /// <summary> Logout by using log out URL, NOT BUTTON </summary>
        public void ExternalLogout(String URL)
        {
            Driver.GoToUrl(URL);
            Console.WriteLine("We logged out");
            Driver.Wait(1);
        }

        //<summary> Super Logout Method </summary>
        public void Logout(String baseURL)
        {

            try
            {
                Se.expandHeader.Click();
                Driver.Wait(1);
                Driver.MoveToIWebElement(Se.logout);
                Se.logout.Click();
            }
            catch
            {
                if (Driver.Instance.Url.ToLower().Contains("amer.reisystems.com/WebGAM2External"))
                    Driver.GoToUrl(baseURL + "/2010/WebEPSExternal/Interface/Common/AccessControl/Logout.aspx");
                else if (Driver.Instance.Url.ToLower().Contains("ehbint.hrsa.gov/WebGAM2External"))
                    Driver.GoToUrl(baseURL + "/EAuthNS//Account/Logout?controlName=TopMenu&PRoleId=1");
                else if (Driver.Instance.Url.ToLower().Contains("hrsa.gov"))
                    Driver.GoToUrl(baseURL + "/EAuthNS//Account/Logout?controlName=TopMenu&PRoleId=1");
                else if (Driver.Instance.Url.ToLower().Contains("amer.reisystems.com/epsexternal"))
                    Driver.GoToUrl(baseURL + "/2010/WebEPSExternal/Interface/Common/AccessControl/logout.aspx");
                else if (Driver.Instance.Url.ToLower().Contains("grants.gov"))
                    Driver.ClickElement("XPath", "//a[contains(text(),'Logout')]");
                else
                    Driver.GoToUrl(baseURL + "/2010/WebEPSInternal/Interface/Common/accessControl/logout.aspx");
            }
        }

        //Annual User acknowledgement
        public void actionLoginAgreement()
        {
            Driver.Instance.FindElement(By.XPath("//input[@Value='Accept']")).Click();
        }

        public static void login(object p1, object p2)
        {
            throw new NotImplementedException();
        }

        public void LogOutAndCloseDriver()
        {
            //Driver.GoToUrl("https://hrsautl26-is.amer.reisystems.com/EPSExternal/Home");
            var logOutLink = Driver.Find(By.XPath("//*[@id='appmenu']/li[3]/a"));
            logOutLink.Click();
            //Driver.Quit();
            Driver.Instance.Quit();

        }
    }
}
