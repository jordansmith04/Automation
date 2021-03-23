using OpenQA.Selenium;
using OpenQA.Selenium.Chrome;
using OpenQA.Selenium.Firefox;
using OpenQA.Selenium.IE;
using OpenQA.Selenium.Interactions;
using OpenQA.Selenium.Internal;
using OpenQA.Selenium.Support.UI;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Configuration;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading;
using System.Threading.Tasks;
using TechTalk.SpecRun.Common.Helper;
using Xunit;

namespace EHBsBDDFramework.Utility
{
    public static class Driver
    {
        #region Class Values
        public static IWebDriver Instance { get; set; }

        public static BrowserType CurrentBrowser = 0;
        public static string PlatformVersion = string.Empty;
        public static string ParentWindowHandler = string.Empty;
        public static string ProductName = string.Empty;
        public static string BuildNumber = string.Empty;
        public static string DownloadDirectory = string.Empty;
        public static string CurrentFrameName = string.Empty;
        //internal static object instace;
        #endregion

        public static bool ErrorMessageDisplayed()
        {
            return !(Driver.Instance.FindElements(By.ClassName("msgFail")).Where(x => x.Displayed).Count() < 1);
        }

        #region Open and Close

        /// <summary> Gets the download path. </summary>
        /// <returns> The path for Chrome </returns>
        private static string GetDownloadPathChrome()
        {
            if (string.IsNullOrEmpty(ConfigurationManager.AppSettings["DownloadDirectory"]))
                DownloadDirectory = Path.Combine(Path.GetFullPath(Path.Combine(AppDomain.CurrentDomain.BaseDirectory + @"\..\..\..\Bin\Debug\net472")));
            else
                DownloadDirectory = ConfigurationManager.AppSettings["DownloadDirectory"];
            return Driver.DownloadDirectory;
        }

        internal static IWebElement Find(object p)
        {
            throw new NotImplementedException();
        }


        /// <summary>   This method navigates to a particular URL. </summary>
        /// <param name="url"> URL to navigate to </param>
        /// <exception cref="NoSuchElementException"></exception>
        /// <exception cref="ElementNotVisibleException">Page Could not load the url : " + url + " :: " + "Generated from Login.Goto. " + ex.Message</exception>
        public static void GoToUrl(string url)
        {
            //instace = OpenQA.Selenium.Chrome.ChromeDriver();
            Instance.Navigate().GoToUrl(url);
        }


        /// <summary>
        /// Opens the chrome.
        /// </summary>
        /// <param name="url">The URL.</param>
        public static void OpenChrome(string url)
        {
            //added try catch to handle failed chrome driver launch 8/25/2019 @varshaj
            //try
            //{

            var timeOutTime = TimeSpan.FromMinutes(5);
            CurrentBrowser = BrowserType.Chrome;
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.AddUserProfilePreference("download.default_directory", GetDownloadPathChrome());
            chromeOptions.AddUserProfilePreference("intl.accept_languages", "nl");
            chromeOptions.AddUserProfilePreference("disable-popup-blocking", "true");
            chromeOptions.AddUserProfilePreference("download.prompt_for_download", "false");

            //chromeOptions.AddUserProfilePreference("javascript.enabled", "false");
            //chromeOptions.AddArguments("--disable-local-storage");
            //  chromeOptions.AddArguments("--headless");
            //chromeOptions.AddExtensions("Y:\\Solutions\\TFS\\GAMAutomation\\BHCMISBDD\\packages\\WAVE-Evaluation-Tool_v1.0.9.crx");
            chromeOptions.AddArguments("--enable-extensions");
            chromeOptions.AddArguments("test-type");
            chromeOptions.AddArguments("--start-maximized");
            chromeOptions.AddArguments("--disable-web-security");
            chromeOptions.AddArguments("--allow-running-insecure-content");

            //chromeOptions.AddArguments("--disable-javascript");

            //chromeOptions.AddArguments("no - sandbox");
            Driver.Instance = new ChromeDriver(GetDownloadPathChrome(), chromeOptions, timeOutTime);
            Driver.Instance.Manage().Timeouts().PageLoad = (TimeSpan.FromMinutes(1));
            Driver.Instance.Manage().Timeouts().ImplicitWait = (TimeSpan.FromMinutes(1));
            //Driver.Instance.Manage().Timeouts().AsynchronousJavaScript = (TimeSpan.FromSeconds(400));
            //Driver.MaximizeWindow();
            Driver.PlatformVersion = ConfigurationManager.AppSettings["PlatformVersion"];
            Driver.BuildNumber = ConfigurationManager.AppSettings["BuildNumber"];
            Driver.ProductName = ConfigurationManager.AppSettings["ProductName"];
            GoToUrl(url);

            //}
            //catch
            //{
            //    Driver.Instance.Close();
            //    Driver.Instance.Quit();
            //}
        }
        private static InternetExplorerOptions IeSettings()
        {
            var options = new InternetExplorerOptions();
            options.IgnoreZoomLevel = true;
            options.IntroduceInstabilityByIgnoringProtectedModeSettings = true;
            options.EnsureCleanSession = true;
            options.EnableNativeEvents = true;
            options.UnhandledPromptBehavior = UnhandledPromptBehavior.Accept;
            options.BrowserCommandLineArguments = "-private";
            options.RequireWindowFocus = true; //Required for it to run propertly maybe
            return options;
        }
        /// <summary>
        /// Opens the IE.
        /// </summary>
        /// <param name="url">The URL.</param>
        public static void OpenIE(string url)
        {
            CurrentBrowser = BrowserType.IE;
            Driver.Instance = new InternetExplorerDriver(IeSettings());
            GoToUrl(url);
        }
        /// <summary>
        /// Opens the firefox.
        /// </summary>
        /// <param name="url">The URL.</param>
        public static void OpenFireFox(string url)
        {
            FirefoxDriverService service = FirefoxDriverService.CreateDefaultService();
            service.FirefoxBinaryPath = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
            FirefoxProfile profile = new FirefoxProfile();
            Driver.Instance = new FirefoxDriver(service);
            //string downloadPath = GetDownloadPathChrome();
            //Driver.CurrentBrowser = BrowserType.Firefox;
            //// FirefoxProfile is depricated
            ////FirefoxProfile firefoxProfile = new FirefoxProfile();
            ////firefoxProfile.SetPreference("browser.download.folderList", 2);
            //////firefoxProfile.SetPreference("browser.download.manager.showWhenStarting","false");
            ////firefoxProfile.SetPreference("browser.download.dir", downloadPath); //Driver.DownloadDirectory
            ////                                                                    //path = [location to save the downloaded files]
            ////firefoxProfile.SetPreference("browser.helperApps.neverAsk.saveToDisk", "application/vnd.ms-excel,application/xls,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/octet-stream doc xls pdf txt");
            ////Driver.Instance = new FirefoxDriver(firefoxProfile);
            //var options = new FirefoxOptions();
            //options.UseLegacyImplementation = true;
            //var service = FirefoxDriverService.CreateDefaultService(downloadPath);
            //Driver.Instance = new FirefoxDriver(service, options, TimeSpan.FromMinutes(1));
            ///************ This code may be required in the future to solve Sporadic Problems,  By Santosh 4/30/2016 **************/
            ////string firefox_path = @"C:\Program Files (x86)\Mozilla Firefox\firefox.exe";
            ////FirefoxBinary binary = new FirefoxBinary(firefox_path);
            ////FirefoxProfile profile = new FirefoxProfile();
            ////profile.SetPreference("network.proxy.type", 0);
            ////Driver.Instance = new FirefoxDriver(binary, profile, TimeSpan.FromSeconds(120));
            ///**********************************************************************************************************************/
            Driver.Instance.Navigate().GoToUrl(url);


            // GoToUrl(url);
        }





        /// <summary>   This method maximizes the window. </summary>
        public static void MaximizeWindow()
        {
            Instance.Manage().Window.Maximize();
        }

        public static void Quit()
        {
            Driver.Instance.Quit();
        }

        #endregion

        #region Move to Element
        public static void MoveToIWebElement(IWebElement Se)
        {
            var element = Se;
            Actions actions = new Actions(Instance);
            actions.MoveToElement(element).Perform();
        }
        public static void MoveToElement(String locator, String elementValue)
        {
            switch (locator)
            {
                case "Id":
                    var element = Driver.Instance.FindElement(By.Id(elementValue));
                    Actions actions = new Actions(Instance);
                    actions.MoveToElement(element);
                    actions.Perform();
                    break;
                case "ClassName":
                    var element1 = Driver.Instance.FindElement(By.ClassName(elementValue));
                    Actions actions1 = new Actions(Instance);
                    actions1.MoveToElement(element1);
                    actions1.Perform();
                    break;
                case "LinkText":
                    var element2 = Driver.Instance.FindElement(By.LinkText(elementValue));
                    Actions actions2 = new Actions(Instance);
                    actions2.MoveToElement(element2);
                    actions2.Perform();
                    break;
                case "TagName":
                    var element3 = Driver.Instance.FindElement(By.TagName(elementValue));
                    Actions actions3 = new Actions(Instance);
                    actions3.MoveToElement(element3);
                    actions3.Perform();
                    break;
                case "XPath":
                    var element4 = Driver.Instance.FindElement(By.XPath(elementValue));
                    Actions actions4 = new Actions(Instance);
                    actions4.MoveToElement(element4);
                    actions4.Perform();
                    break;
                case "CssSelector":
                    var element5 = Driver.Instance.FindElement(By.CssSelector(elementValue));
                    //elementType = Instance.FindElement(By.CssSelector("[class='" + elementValue + "']"));
                    Actions actions5 = new Actions(Instance);
                    actions5.MoveToElement(element5);
                    actions5.Perform();
                    break;
                case "PartialLinkText":
                    var element6 = Driver.Instance.FindElement(By.PartialLinkText(elementValue));
                    Actions actions6 = new Actions(Instance);
                    actions6.MoveToElement(element6);
                    actions6.Perform();
                    break;

            }
        }

        public static void MoveToElement(By by)
        {
            var element = Driver.Instance.FindElement(by);
            Actions actions = new Actions(Instance);
            actions.MoveToElement(element);
            actions.Perform();
        }

        public static void MoveToElement(IWebElement element)
        {
            (Driver.Instance as IJavaScriptExecutor).ExecuteScript("window.scrollTo(" + element.Location.X + ", " + element.Location.Y + ")");
            Actions actions = new Actions(Instance);
            actions.MoveToElement(element);
            actions.Perform();
        }

        #endregion


        #region DavidFindElements



        /// <summary>  Same as FindElement only scrolls to the element as well </summary>
        /// <param name="by">The search string for finding element</param>
        /// <returns>Returns element</returns>
        public static IWebElement Find(By by)
        {
            var element = Driver.Instance.FindElement(by);
            Driver.WaitForElement(by, 60);
            MoveToElement(element);
            return element;
        }

        /// <summary>  Same as FindElement only scrolls to the element as well </summary>
        /// <param name="by">The search string for finding element</param>
        /// <returns>Returns element</returns>
        public static IWebElement Find(By by, IWebElement parent)
        {
            var element = parent.FindElement(by);
            MoveToElement(element);
            return element;
        }

        /// <summary>  Same as FindElement only scrolls to the element as well </summary>
        /// <param name="by">The search string for finding element</param>
        /// <returns>Returns element</returns>
        public static IWebElement FindWithAjaxWait(By by)
        {
            WaitForAjax(Instance);
            var element = Driver.Instance.FindElement(by);
            MoveToElement(element);
            return element;
        }

        #endregion


        #region Get and Return Elements


        /// <summary>
        /// This method gets the element on a web page depending on element type(id,classname, linktext,
        /// tagname,xpath,cssselector).
        /// </summary>
        /// <exception cref="Exception">    Thrown when an exception error condition occurs. </exception>
        /// <param name="elementType">  . </param>
        /// <param name="value">        . </param>
        /// <returns>   The element. </returns>
        public static IWebElement GetElement(ElementType elementType, string value)
        {
            IWebElement webElement = null;
            try
            {
                switch (elementType)
                {
                    case ElementType.Id:
                        webElement = Instance.FindElement(By.Id(value));
                        break;
                    case ElementType.ClassName:
                        webElement = Instance.FindElement(By.ClassName(value));
                        break;
                    case ElementType.LinkText:
                        webElement = Instance.FindElement(By.LinkText(value));
                        break;
                    case ElementType.TagName:
                        webElement = Instance.FindElement(By.TagName(value));
                        break;
                    case ElementType.XPath:
                        webElement = Instance.FindElement(By.XPath(value));
                        break;
                    case ElementType.CssSelector:
                        webElement = Instance.FindElement(By.CssSelector("[class='" + value + "']"));
                        break;
                }
            }
            catch (Exception ex)
            { throw ex; }
            // Scroll the browser to the element's Y position
            IWebDriver jsdriver = Driver.Instance; // assume assigned elsewhere
            IJavaScriptExecutor js = (IJavaScriptExecutor)jsdriver;
            js.ExecuteScript("window.scrollTo(0," + webElement.Location.Y + ")");
            return webElement;

        }
        /// <summary>
        /// This method gets a list of elements on a web page depending on element type(id, classname,
        /// linktext,tagname,xpath,cssselector).
        /// </summary>
        /// <exception cref="Exception">    Thrown when an exception error condition occurs. </exception>
        /// <param name="elementType">  . </param>
        /// <param name="value">        . </param>
        /// <returns>   The elements. </returns>
        public static List<IWebElement> GetElements(ElementType elementType, string value)
        {
            List<IWebElement> webElements = null;
            try
            {
                switch (elementType)
                {
                    case ElementType.Id:
                        webElements = Instance.FindElements(By.Id(value)).ToList<IWebElement>();
                        break;
                    case ElementType.ClassName:
                        webElements = Instance.FindElements(By.ClassName(value)).ToList<IWebElement>();
                        break;
                    case ElementType.LinkText:
                        webElements = Instance.FindElements(By.LinkText(value)).ToList<IWebElement>();
                        break;
                    case ElementType.TagName:
                        webElements = Instance.FindElements(By.TagName(value)).ToList<IWebElement>();
                        break;
                    case ElementType.XPath:
                        webElements = Instance.FindElements(By.XPath(value)).ToList<IWebElement>();
                        break;
                    case ElementType.CssSelector:
                        webElements = Instance.FindElements(By.CssSelector("[class='" + value + "']")).ToList<IWebElement>();
                        break;
                }
            }
            catch (Exception ex)
            { throw ex; }
            return webElements;

        }

        #endregion




        #region All WAIT activities

        /// <summary>
        /// This method makes the execution wait for specified seconds.
        /// </summary>
        /// <param name="timeInSeconds">Time to wait</param>
        public static void Wait(int timeInSeconds)
        {
            Thread.Sleep((int)timeInSeconds * 1000);
            //WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.id(> someid >)));
        }



        /// <summary>
        /// To wait till the element is not fully loaded
        /// </summary>
        /// <param name="uniqueText"></param>
        /// <param name="timeInSeconds"></param>
        public static void WaitFor(string uniqueText, string timeInSeconds)
        {
            try
            {
                var wait = new WebDriverWait(Driver.Instance, TimeSpan.FromSeconds(int.Parse(timeInSeconds)));
                Debug.WriteLine(Driver.Instance.PageSource);
                wait.Until(d => d.PageSource.LastIndexOf(uniqueText) >= 0);
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
                throw new Exception("Wait time failed. Waited for the text '" + uniqueText + "' for " + timeInSeconds + " seconds.");
            }

        }

        //public static void WaitFor(string uniqueText)
        //{
        //    var wait = new WebDriverWait(Driver.Instance, TimeSpan.FromSeconds(60));
        //    //wait.Until(d => d.FindElement(By.TagName("body")).Text.ToLower().Contains(uniqueText.ToLower()));
        //    wait.Until(d => d.PageSource.LastIndexOf(uniqueText));
        //}

        /// <summary>   Makes execution fast for an action. </summary>
        /// <param name="action"> Action to do quickly </param>
        public static void NoWait(Action action)
        {
            TurnOffWait();
            action();
            TurnOnWait();
        }
        /// <summary>  Implicitly Waits for 5 Seconds. </summary>
        private static void TurnOnWait()
        {
            Instance.Manage().Timeouts().ImplicitWait = TimeSpan.FromSeconds(5);
        }
        /// <summary> Turns off the implicit wait.  </summary>
        private static void TurnOffWait()
        {
            Instance.Manage().Timeouts().ImplicitWait = TimeSpan.FromSeconds(0);
        }

        /// <summary>   This method sets up the timeouts. </summary>
        /// <param name="seconds"> Value for implicit waits </param>
        public static void SetTimeOuts(int seconds)
        {
            Instance.Manage().Timeouts().ImplicitWait = TimeSpan.FromSeconds(seconds);
        }

        public static int WaitUntil(Func<bool> condition, int seconds)
        {
            int elapsed = 0;
            bool condResult = false;
            while (elapsed <= seconds)
            {
                Thread.Sleep(1000);
                elapsed += 1;
                condResult = condition();

                if (condResult == true)
                {
                    break;
                }
            }
            return elapsed;
        }

        internal static void WaitForAjax(this IWebDriver driver, int timeoutSecs = 300, bool throwException = false, string script = "return ctl00_mainAjaxManager._isRequestInProgress")
        {
            try
            {
                for (var i = 0; i < (timeoutSecs * 10); i++)
                {
                    var javaScriptExecutor = driver as IJavaScriptExecutor;
                    bool ajaxIsWorking = javaScriptExecutor != null && (bool)javaScriptExecutor.ExecuteScript(script);
                    if (ajaxIsWorking == false) return;
                    Thread.Sleep(1000);
                }
            }
            catch (Exception)
            {
                throw new Exception("WebDriver timed out waiting for AJAX call to complete");
            }
        }



        /// <summary> Wait for upto 60 secs for the page to load . </summary>
        ///
        /// ### <remarks>   Tanuj Sharma, 03/21/2016. </remarks>        
        public static void WaitForPageElementByID(string ElementID)
        {
            for (int second = 0; ; second++)
            {
                if (second >= 60) break;
                //return false;
                try
                {
                    if (IsElementPresent(By.Id(ElementID)))
                    {

                        break;
                    }
                    Driver.Wait(1);
                }
                catch (Exception)
                { }
                //Driver.Wait(1);
            }
            //return true;            
        }

        public static int WaitForElementByID(string ElementID, int timeInSeconds)
        {
            int seconds = 1;
            for (seconds = 1; seconds <= timeInSeconds; seconds++)
            {
                if (seconds >= timeInSeconds) break;

                try
                {
                    if (IsElementPresent(By.Id(ElementID))) break;
                    Driver.Wait(1);
                }
                catch (StaleElementReferenceException)
                {
                    //Do nothing, it will continue looping
                }
                catch (Exception)
                {
                    seconds = -1;
                }

            }

            return seconds;
        }

        public static int WaitForElement(By by, int timeInSeconds)
        {
            IWait<IWebDriver> wait = new WebDriverWait(Driver.Instance, TimeSpan.FromSeconds(timeInSeconds));
            Stopwatch sw = new Stopwatch();
            sw.Start();
            wait.Until(driver => !(driver.FindElement(by) == null));
            sw.Stop();
            return (int)sw.Elapsed.TotalSeconds;
        }

        public static void waitForElementSeleniumExtras(By element)
        {
            WebDriverWait wait = new WebDriverWait(Driver.Instance, TimeSpan.FromSeconds(5));
            wait.Until(SeleniumExtras.WaitHelpers.ExpectedConditions.ElementExists(element));
        }

        /// <summary> Wait for upto 60 secs for the page to load - By LinkName . </summary>
        public static void WaitForPageElementByLinkName(string LinkName)
        {
            for (int second = 0; ; second++)
            {
                if (second >= 60) break;

                try
                {
                    if (IsElementPresent(By.LinkText(LinkName))) break;
                }
                catch (Exception)
                { }
            }
        }

        public static void WaitForPageTitle(string TitleName)
        {
            for (int second = 0; ; second++)
            {
                if (second >= 60) break;
                try
                {
                    if (Instance.Title.Contains(TitleName)) break;
                }
                catch (Exception)
                {

                }
            }
        }

        #endregion


        #region Window Management

        /// <summary>   This method switches to the popup window. </summary>
        /// <returns>   An IWebDriver. </returns>
        public static IWebDriver SwitchToPopUpWindow()
        {
            ParentWindowHandler = Instance.CurrentWindowHandle;
            //string subWindowHandler = null;
            List<string> handles = Driver.Instance.WindowHandles.ToList(); // get all window handles

            //Instance.SwitchTo().Window(handles.Last()).Manage().Cookies.DeleteAllCookies();
            return Instance.SwitchTo().Window(handles.Last());
            //return Driver.Instance;
        }
        /// <summary>   This method switches to the popup window. </summary>
        /// <param name="currentWindowHandle">  The current window handle. </param>
        /// <returns>   An IWebDriver. </returns>
        public static IWebDriver PoppedUpWindow(string currentWindowHandle)
        {
            return Instance.SwitchTo().Window(currentWindowHandle);
        }

        /// <summary> Switching Between Windows  </summary>
        /// <param name="currentWindowHandle"></param>
        public static void SwitchToMainWindow()
        {
            CurrentFrameName = "";
            string ParentWindowHandler = "";
            if (string.IsNullOrEmpty(ParentWindowHandler))
            {
                ParentWindowHandler = Instance.WindowHandles.First();
            }
            Instance.SwitchTo().Window(ParentWindowHandler);



        }


        /// <summary>  Switch focus on the new window. </summary>
        ///
        /// ### <remarks>   Tanuj Sharma, 03/21/2016. </remarks>        
        public static void SwitchToWindow(string mainhandle)
        {
            string popupHandle = string.Empty;
            ReadOnlyCollection<string> windowHandles = Instance.WindowHandles;

            foreach (string handle in windowHandles)
            {
                if (handle != mainhandle)
                {
                    popupHandle = handle; break;
                }
            }
            //switch to new window 
            Instance.SwitchTo().Window(popupHandle);
        }


        /// <summary>
        /// Switches between iFrames. 
        /// </summary>
        /// <param name="frameName">string framName or frameId. For Nested frames,provide order of frames separated by '/'</param>
        /// <remarks>#Author: Santosh Thapa, 05/16/2016</remarks>
        public static void SwitchToFrame(string frameName)
        {
            if (frameName.Contains("/"))
            {
                SwitchToCurrentWindow();
                string[] frames = frameName.Split('/');
                IWebElement frame;
                CurrentFrameName = frames[frames.Length - 1];

                frame = Driver.Instance.FindElement(By.Id(frames[0]));
                Driver.Instance.SwitchTo().Frame(frame);
                for (int i = 1; i < frames.Length; i++)
                {
                    frame = Driver.Instance.FindElement(By.Id(frames[i]));
                    Driver.Instance.SwitchTo().Frame(frame);
                }
            }
            else if (string.IsNullOrEmpty(CurrentFrameName))
            {
                CurrentFrameName = frameName;
                Instance.SwitchTo().Frame(frameName);
            }
            else if (!CurrentFrameName.Equals(frameName))
            {
                SwitchToCurrentWindow();
                CurrentFrameName = frameName;
                Instance.SwitchTo().Frame(frameName);
            }

        }
        /// <summary>  Switch focus on the new window. </summary>
        ///
        /// ### <remarks>   Tanuj Sharma, 03/21/2016. </remarks>        
        public static void SwitchToCurrentWindow()
        {
            CurrentFrameName = "";
            Instance.SwitchTo().Window(Instance.WindowHandles.Last());
        }

        public static int WaitForElementByXpath(string ElementID, int timeInSeconds)
        {
            int seconds = 1;
            for (seconds = 1; seconds <= timeInSeconds; seconds++)
            {
                if (seconds >= timeInSeconds) break;

                try
                {
                    if (IsElementPresent((By.XPath(ElementID)))) break;
                    Driver.Wait(1);
                }
                catch (StaleElementReferenceException)
                {
                    //Do nothing, it will continue looping
                }
                catch (Exception)
                {
                    seconds = -1;
                }

            }

            return seconds;
        }



        #endregion


        #region Get Inner HTML Attribute

        public static string GetInnertHTMLAttributeByID(string locator)
        {
            string value = Driver.Instance.FindElement(By.Id(locator)).GetAttribute("innerHTML");
            return value;
        }

        public static string GetInnertHTMLAttributeByXPath(string locator)
        {
            string value = Driver.Instance.FindElement(By.XPath(locator)).GetAttribute("innerHTML");
            return value;
        }

        public static string GetALTAttributeByID(string locator)
        {
            string value = Driver.Instance.FindElement(By.Id(locator)).GetAttribute("alt");
            return value;
        }

        public static string GetHREFAttributeByXPath(string locator)
        {
            string value = Driver.Instance.FindElement(By.XPath(locator)).GetAttribute("href");
            return value;
        }

        public static void SetAttribute(this IWebElement element, string attributeName, string value)
        {
            IWrapsDriver wrappedElement = element as IWrapsDriver;
            if (wrappedElement == null)
                throw new ArgumentException("element", "Element must wrap a web driver");

            IWebDriver driver = wrappedElement.WrappedDriver;
            IJavaScriptExecutor javascript = driver as IJavaScriptExecutor;
            if (javascript == null)
                throw new ArgumentException("element", "Element must wrap a web driver that supports javascript execution");

            javascript.ExecuteScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, attributeName, value);
        }

        #endregion








        #region Send Keys INCLUDING SPECIFIC KEYS

        public static void SendKeysByID(String locator, String value)
        {
            try
            {
                Driver.Instance.FindElement(By.Id(locator)).Clear();
                Driver.Instance.FindElement(By.Id(locator)).SendKeys(value);
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
                Driver.GoToUrl(ConfigurationManager.AppSettings["ExternalLogoutURL"]);
                Driver.Quit();
                throw new Exception("Unable to SendKeys. Failing Test Case. Please Investigate");
            }
        }

        public static void SendKeysByXPath(String locator, String value)
        {
            try
            {
                Driver.Instance.FindElement(By.XPath(locator)).Clear();
                Driver.Instance.FindElement(By.XPath(locator)).SendKeys(value);
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
                Driver.GoToUrl(ConfigurationManager.AppSettings["ExternalLogoutURL"]);
                Driver.Quit();
                throw new Exception("Unable to SendKeys. Failing Test Case. Please Investigate");
            }
        }

        public static void SendKeysByIDReturnKey(String value)
        {
            try
            {
                Driver.Instance.FindElement(By.Id(value)).SendKeys(Keys.Return);
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
                Driver.GoToUrl(ConfigurationManager.AppSettings["ExternalLogoutURL"]);
                Driver.Quit();
                throw new Exception("Unable to SendKeys. Failing Test Case. Please Investigate");
            }
        }

        public static void SendKeysByIDArrowDown(String value)
        {
            Driver.Instance.FindElement(By.Id(value)).SendKeys(Keys.ArrowDown);
        }

        public static void SendKeysByIDArrowUp(String value)
        {
            Driver.Instance.FindElement(By.Id(value)).SendKeys(Keys.ArrowUp);
        }



        public static void SetText(this IWebElement element, string value)
        {
            element.SendKeys(value);
        }

        #endregion



        #region Click a Specific Element

        public static void ClickElement(String locator, String value)
        {
            try
            {
                switch (locator)
                {
                    case "Id":
                        var element = Driver.Instance.FindElement(By.Id(value));
                        Actions actions = new Actions(Instance);
                        actions.MoveToElement(element);
                        actions.Perform();
                        element.Click();
                        break;
                    case "ClassName":
                        Driver.Instance.FindElement(By.ClassName(value)).Click();
                        break;
                    case "LinkText":
                        Driver.Instance.FindElement(By.LinkText(value)).Click();
                        break;
                    case "TagName":
                        Driver.Instance.FindElement(By.TagName(value)).Click();
                        break;
                    case "XPath":
                        Driver.Instance.FindElement(By.XPath(value)).Click();
                        break;
                    case "CssSelector":
                        Driver.Instance.FindElement(By.CssSelector(value)).Click();
                        break;
                    case "PartialLinkText":
                        Driver.Instance.FindElement(By.PartialLinkText(value)).Click();
                        break;
                }
            }
            catch (Exception ex)
            {
                Driver.GoToUrl(ConfigurationManager.AppSettings["ExternalLogoutURL"]);
                Driver.Quit();
                throw new Exception("Unable to click on Element. Failing test Case. Please Investigate.", ex);
            }


        }


        /// <summary>
        /// A CLICK --- A DRIVER LEVEL REGULAR CLICK
        /// </summary>
        public static void ClickOnHeaderToggler()
        {
            Driver.ClickElement("id", "MainContent_ctl01_tglHeader_toggler");
        }


        public static void ClickOnSave()
        {
            Driver.MoveToElement("Id", "Save");
            Driver.ClickElement("Id", "Save");
        }

        public static void ClickOnSaveAndContinue()
        {
            Driver.MoveToElement("Id", "SaveandContinue");
            Driver.ClickElement("Id", "SaveandContinue");
        }

        public static void ClickOnGoToPrevious()
        {
            Driver.ClickElement("Id", "Cancel");
        }

        public static void iFrameText(String text)
        {
            IWebElement frame = Driver.Find(By.XPath("//iframe[@title = 'Rich text content area. To move from edit content area to the FIRST toolbar item button press F10']"));
            Driver.Instance.SwitchTo().Frame(frame);
            IWebElement body = Driver.Find(By.TagName("body"));
            body.SendKeys(text);
            Driver.Instance.SwitchTo().DefaultContent();
        }


        #endregion

        #region Robot/Experiment

        public static void ShiftClick(string ElementID)
        {
            var element = Driver.Find(By.Id(ElementID));
            Actions actions = new Actions(Instance);
            actions.KeyDown(Keys.Shift).Click(element).KeyUp(Keys.Shift).Perform();
        }

        #endregion


        #region getters

        /// <summary>   Get the handle of the current window. </summary>
        ///
        /// ### <remarks>   Tanuj Sharma, 03/21/2016. </remarks>        
        public static string GetMainWindowHandle
        {
            get { return Instance.CurrentWindowHandle; }
        }

        /// <summary>  Return the title of the web page. </summary>
        ///
        /// ### <remarks>   Tanuj Sharma, 03/21/2016. </remarks>        
        public static string Title
        {
            get { return Instance.Title; }
        }


        /// <summary>   Return the URL of the web page. </summary>
        ///
        /// ### <remarks>   Tanuj Sharma, 03/21/2016. </remarks>    
        public static string URL
        {
            get { return Instance.Url; }
        }


        //public static string GetBrowserInfo()
        //{
        //    ICapabilities capabilities = ((RemoteWebDriver)Driver.Instance).Capabilities;
        //    return capabilities.BrowserName + "," + capabilities.Version;
        //    //return "";
        //}


        #endregion


        #region Kinda looking for elements --- existance

        /// <summary>   Check if element is present on the web page. </summary>
        ///
        /// ### <remarks>   Tanuj Sharma, 03/21/2016. </remarks>    
        public static bool IsElementPresent(By by)
        {
            try
            {
                Instance.FindElement(by);
                return true;
            }
            catch (NoSuchElementException)
            {
                return false;
            }
        }

        public static bool IsElementOnPage(By locator)
        {
            var element = Driver.Instance.FindElements(locator);
            var count = element.Count;
            return count > 0;
        }

        /// <summary>   Check if text exists on the page. </summary>
        ///
        /// ### <remarks>   Tanuj Sharma, 03/21/2016. </remarks>    
        public static bool ExistsInPageSource(string locateText)
        {
            try
            {
                Instance.PageSource.Contains(locateText);
                return true;
            }
            catch (NotFoundException)
            {
                return false;
            }
        }


        public static bool IsElementVisible(IWebElement element)
        {
            return element.Displayed && element.Enabled;
        }



        public static void checkElementDoesNotExist(string elementID)
        {
            try
            {
                bool element = Driver.Instance.FindElement(By.Id(elementID)).Displayed;
                if (element == true)
                {
                    throw new Exception("Element Exists when it shouldn't");
                }
            }
            catch
            {
                Console.WriteLine("Huh.");
            }
        }

        public static void checkElementDoesNotExistXPath(string elementXPath)
        {
            try
            {
                bool element = Driver.Instance.FindElement(By.XPath(elementXPath)).Displayed;
                if (element == true)
                {
                    throw new Exception("Element Exists when it shouldn't");
                }
            }
            catch
            {
                Console.WriteLine("Huh.");
            }
        }
        #endregion




        #region Compare text... using MS Test Assert

        public static void AssertThis(String actualText, String expectedText)
        {
            try
            {
                Assert.Contains(expectedText, actualText);
            }
            catch (Exception e)
            {
                throw new Exception(
                    "Text does not match the expected value." + " Expected Value is: " + expectedText + " >>>Actual Value is: " + actualText, e);


            }
        }
        public static void AssertAndAddTestResult(String AssertType, String Name, String actualText="", String expectedText="",bool boolResult=true)
        {
            try
            {
                bool error = false;
                String errorMessage = string.Empty;

                switch (AssertType)
                {                         
                    case "Equal":
                             if (expectedText.Equals(actualText))
                             {
                                 Assert.Equal(expectedText, actualText);
                                 error = false;
                                 Console.WriteLine(Name +"  "+(error ? "Failed" : "Passed") +"   " + DateTime.Now.ToString());
                             }
                             else
                             {
                                 Assert.NotEqual(expectedText, actualText);
                                 error = true;
                                 errorMessage = "Text does not match the expected value." + " Expected Value is: " + expectedText + " >>>Actual Value is: " + actualText;
                                 Console.WriteLine(Name + " " + (error ? "Failed" : "Passed")+ " Error: "+ errorMessage + " " + DateTime.Now.ToString());
                             }
                             break;
                    case "NotEqual":
                        if (!expectedText.Equals(actualText))
                        {
                            Assert.NotEqual(expectedText, actualText);
                            error = false;
                            Console.WriteLine(Name + "  " + (error ? "Failed" : "Passed") + "   " + DateTime.Now.ToString());
                        }
                        else
                        {
                            Assert.Equal(expectedText, actualText);
                            error = true;
                            errorMessage = "Text does not match the expected value." + " Expected Value is: " + expectedText + " >>>Actual Value is: " + actualText;
                            Console.WriteLine(Name + " " + (error ? "Failed" : "Passed") + " Error: " + errorMessage + " " + DateTime.Now.ToString());
                        }
                        break;
                    case "Contains":
                        if (actualText.Contains(expectedText))
                        {
                            Assert.Contains(expectedText, actualText);
                            error = false;
                            Console.WriteLine(Name + " " + (error ? "Failed" : "Passed") + " " + DateTime.Now.ToString());
                        }
                        else
                        {
                            Assert.DoesNotContain(expectedText, actualText);
                            error = true;
                            errorMessage = "Text does not match the expected value." + " Expected Value is: " + expectedText + " >>>Actual Value is: " + actualText;
                            Console.WriteLine(Name + " " + (error ? "Failed" : "Passed") + " Error: " + errorMessage + " " + DateTime.Now.ToString());
                        }
                        break;
                    case "Empty":
                        if (actualText.IsNullOrEmpty())
                        {
                            Assert.Empty(actualText);
                            error = false;
                            Console.WriteLine(Name + "  " + (error ? "Failed" : "Passed") + "   " + DateTime.Now.ToString());
                        }
                        else
                        {
                            Assert.NotEmpty(actualText);
                            error = true;
                            errorMessage = "Expected Empty";
                            Console.WriteLine(Name + " " + (error ? "Failed" : "Passed") + " Error: " + errorMessage + " " + DateTime.Now.ToString());
                        }
                        break;
                    case "True":
                        if(boolResult)
                        {
                            Assert.True(boolResult);
                            error = false;
                            Console.WriteLine(Name + " " + (error ? "Failed" : "Passed") + " " + DateTime.Now.ToString());
                        }
                        else
                        {
                            Assert.False(boolResult);
                            error = true;
                            errorMessage = "Expected True but it was False";
                            Console.WriteLine(Name + " " + (error ? "Failed" : "Passed") + " Error: " + errorMessage + " " + DateTime.Now.ToString());
                        }
                            break;
                    case "False":
                        if (!boolResult)
                        {
                            Assert.False(boolResult);
                            error = false;
                            Console.WriteLine(Name + " " + (error ? "Failed" : "Passed") + " " + DateTime.Now.ToString());
                        }
                        else
                        {
                            Assert.True(boolResult);
                            error = true;
                            errorMessage = "Expected False but it was True";
                            Console.WriteLine(Name + " " + (error ? "Failed" : "Passed") + " Error: " + errorMessage + " " + DateTime.Now.ToString());
                        }
                        break;
                    case "Null":
                        if (actualText.IsNullOrEmpty())
                        {
                            Assert.Null(actualText);
                            error = false;
                            Console.WriteLine(Name + " " + (error ? "Failed" : "Passed") + " " + DateTime.Now.ToString());
                        }
                        else
                        {
                            Assert.NotNull(actualText);
                            error = true;
                            errorMessage = "Text is Not Null :"+ actualText;
                            Console.WriteLine(Name + " " + (error ? "Failed" : "Passed") + " Error: " + errorMessage + " " + DateTime.Now.ToString());
                        }
                        break;
                }

            }
            catch (Exception e)
            {
                throw new Exception( "No matching Type ", e);
            }
        }
        public static void AssertByID(String elementID, String expectedText)
        {
            String actualText = Driver.Instance.FindElement(By.Id(elementID)).Text;
            IWebElement element = Driver.Instance.FindElement(By.Id(elementID));
            bool visible = IsElementVisible(element);
            if (visible)
            {
                try
                {
                    Assert.Contains(expectedText, actualText);
                }
                catch (Exception e)
                {
                    throw new Exception(
                        "Text does not match the expected value." + " Expected Value is: " + expectedText + " >>>Actual Value is: " +
                                actualText, e);
                }
            }

            else
            {
                throw new Exception();
                //throw new Exception("Text Not Found. You might have a UE :/");
            }
        }


        public static void AssertByCSS(String elementCSS, String expectedText)
        {
            String actualText = Driver.Instance.FindElement(By.CssSelector(elementCSS)).Text;
            IWebElement element = Driver.Instance.FindElement(By.CssSelector(elementCSS));
            bool visible = IsElementVisible(element);
            if (visible)
            {
                try
                {
                    Assert.Contains(expectedText, actualText);
                }
                catch (Exception e)
                {
                    throw new Exception(
                        "Text does not match the expected value." + " Expected Value is: " + expectedText + " >>>Actual Value is: " +

                                actualText, e);
                }
            }
            else
            {
                throw new Exception();
                //throw new Exception("Text Not Found. You might have a UE :/");
            }
        }

        public static void AssertByLinkText(String LinkText, String expectedText)
        {
            String actualText = Driver.Instance.FindElement(By.PartialLinkText(LinkText)).Text;
            IWebElement element = Driver.Instance.FindElement(By.PartialLinkText(LinkText));
            bool visible = IsElementVisible(element);
            if (visible)
            {
                try
                {
                    Assert.Contains(expectedText, actualText);
                }
                catch (Exception e)
                {
                    throw new Exception(
                        "Text does not match the expected value." + " Expected Value is: " + expectedText + " >>>Actual Value is: " +

                                actualText, e);
                }
            }
            else
            {
                throw new Exception();
                //throw new Exception("Text Not Found. You might have a UE :/");
            }
        }

        public static void AssertAttributeID(String elementID, String attribute, String expectedText)
        {
            String actualText = Driver.Instance.FindElement(By.Id(elementID)).GetAttribute(attribute);
            IWebElement element = Driver.Instance.FindElement(By.Id(elementID));
            bool visible = IsElementVisible(element);
            if (visible)
            {
                try
                {
                    Assert.Contains(expectedText, actualText);
                }
                catch (Exception e)
                {
                    throw new Exception(
                        "Text does not match the expected value." + " Expected Value is: " + expectedText + " >>>Actual Value is: " +
                                actualText, e);
                }
            }
            else
            {
                throw new Exception();
                //throw new Exception("Text Not Found. You might have a UE :/");
            }
        }

        public static void AssertByClass(String elementClass, String expectedText)
        {
            String actualText = Driver.Instance.FindElement(By.ClassName(elementClass)).Text;//.GetAttribute("text");
            IWebElement element = Driver.Instance.FindElement(By.ClassName(elementClass));
            bool visible = IsElementVisible(element);
            if (visible)
            {
                try
                {
                    Assert.Contains(expectedText, actualText);
                }
                catch (Exception e)
                {
                    throw new Exception(
                        "Text does not match the expected value." + " Expected Value is: " + expectedText + " >>>Actual Value is: " +
                                actualText, e);
                }
            }
            else
            {
                throw new Exception();
                //throw new Exception("Text Not Found. You might have a UE :/");
            }
        }

        public static void AssertByXpath(String elementXpath, String expectedText)
        {
            String actualText = Driver.Instance.FindElement(By.XPath(elementXpath)).Text;//.GetAttribute("text");
            IWebElement element = Driver.Instance.FindElement(By.XPath(elementXpath));
            bool visible = IsElementVisible(element);
            if (visible)
            {
                try
                {
                    Assert.Contains(expectedText, actualText);
                }
                catch (Exception e)
                {
                    throw new Exception(
                        "Text does not match the expected value." + " Expected Value is: " + expectedText + " >>>Actual Value is: " +
                                actualText, e);
                }
            }
            else
            {
                throw new Exception();
                //throw new Exception("Text Not Found. You might have a UE :/");
            }
        }

        public static void assertPopUpHeaderAndClose(String expectedHeaderText, String mainWindowName)
        {
            String actualText = Driver.Find(By.ClassName("main_title")).Text;
            if (actualText.Contains(expectedHeaderText))
            {
                Driver.Instance.Close();
                Driver.SwitchToWindow(mainWindowName);
                //				asserts.AssertByLinkText("Search", "Search");
            }
            else
            {
                Driver.Instance.Close();
                Driver.SwitchToWindow(mainWindowName);
                throw new Exception("The Pop-Up window's Header didnt match what was expected." + " Try to check the '" + expectedHeaderText + "' report again." + " The text obtained by Selenium was : " + actualText);
            }
        }
        #endregion





        #region Checkbox Interactions ONLY ID

        public static void UnCheckTheCheckBox(string locator)
        {
            bool checkbox = Driver.Instance.FindElement(By.Id(locator)).Selected;
            if (checkbox == true)
            {
                Driver.Instance.FindElement(By.Id(locator)).SendKeys(Keys.Space);
            }
            else
            {
                //Do Nothing
            }
        }

        public static void checkBoxSelection(String XPath , String selection, String tag= "li")
        {
            var grid = Driver.Instance.FindElement(By.XPath(XPath));
            var gridList = grid.FindElements(By.TagName(tag));

            foreach(var element in gridList)
            {
                var e = element.FindElement(By.TagName("label")).GetAttribute("textContent");
                if (e.IsNotNullOrEmpty() && e.Contains(selection))
                {
                    bool checkbox = element.FindElement(By.TagName("label")).Selected;
                    if (checkbox == true)
                    {
                        //Do Nothing
                    }
                    else
                    {
                        element.FindElement(By.TagName("label")).SendKeys(Keys.Space);
                    }
                }
            }
        }

        public static void CheckTheCheckBox(string locator)
        {
            bool checkbox = Driver.Instance.FindElement(By.Id(locator)).Selected;
            if (checkbox == true)
            {
                //Do Nothing
            }
            else
            {
                Driver.Instance.FindElement(By.Id(locator)).SendKeys(Keys.Space);
            }
        }

        public class GrantsDotGov {
            public static void clearCheckBoxSelection(String XPath, String tag = "li")
            {
                var grid = Driver.Instance.FindElement(By.XPath(XPath));
                var gridList = grid.FindElements(By.TagName(tag));

                foreach (var element in gridList)
                {
                    try
                    {
                        bool checkbox = element.FindElement(By.TagName("label")).FindElement(By.TagName("input")).Selected;
                        if (checkbox == true)
                            element.FindElement(By.TagName("label")).SendKeys(Keys.Space);
                    }
                    catch
                    {
                        //
                    }
                }
                Driver.Wait(2);
            }
            public static void checkBoxSelection(String XPath, String selection, String tag = "li")
            {
                var grid = Driver.Instance.FindElement(By.XPath(XPath));
                var gridList = grid.FindElements(By.TagName(tag));
                String[] selectionArray = selection.Split('~');
                int count = 0;
                foreach (var element in gridList)
                {
                    var e = element.FindElement(By.TagName("label")).GetAttribute("textContent");
                    if (e.IsNotNullOrEmpty())
                    {
                        String temp = Regex.Replace(e, @"[^0-9a-zA-Z]+", "");
                        
                        for (int i = 0; i < selectionArray.Length;i++)
                        {
                            selection = Regex.Replace(selectionArray[i], @"[^0-9a-zA-Z]+", "");
                            if (temp.Equals(selection))
                            {
                                bool checkbox = element.FindElement(By.TagName("label")).FindElement(By.TagName("input")).Selected;
                                if (!checkbox)
                                { try { element.FindElement(By.TagName("label")).SendKeys(Keys.Space); } catch { element.FindElement(By.TagName("label")).Click(); } }
                                Driver.Wait(2);
                                ++count;
                            }
                        }
                        if ( count == selectionArray.Length)
                                break;
                    }
                }
                Driver.Wait(2);
            }
        }
        #endregion


        //For Radio Button -- Aveena
        public static void VerifyRadioButton(String locator)
        {
            bool radiobutton = Driver.Instance.FindElement(By.XPath(locator)).Selected;
            if (radiobutton == true)
            {
                //Do Nothing
            }
            else
            {
                Driver.Instance.FindElement(By.XPath(locator)).SendKeys(Keys.Space);
            }

        }



        public static void SendKeysByIDSpacebar(String value)
        {
            Driver.Instance.FindElement(By.Id(value)).SendKeys(Keys.Space);
        }

        internal class FindElement
        {
        }





        //public static void CompareValues(string firstValue, string secondValue)
        //{
        //    if (!(firstValue.ToLower() == secondValue.ToLower()))
        //    {
        //        throw new Exception("Comparision Failed");
        //    }
        //}




        //#region Get and Return Elements (Elements of elements)

        //public static IWebElement GetElement(this IWebElement element, By by)
        //{
        //    IWebElement webElement;
        //    try
        //    {
        //        webElement = element.FindElement(by);
        //    }
        //    catch (Exception ex)
        //    {
        //        throw ex;
        //    }
        //    // Scroll the browser to the element's Y position
        //    IWebDriver jsdriver = Driver.Instance; // assume assigned elsewhere
        //    IJavaScriptExecutor js = (IJavaScriptExecutor)jsdriver;
        //    js.ExecuteScript("window.scrollTo(0," + webElement.Location.Y + ")");
        //    return webElement;
        //}

        //public static List<IWebElement> GetElements(this IWebElement element, By by)
        //{
        //    List<IWebElement> webElements;
        //    try
        //    {
        //        webElements = element.FindElements(by).ToList();
        //    }
        //    catch (Exception ex)
        //    {
        //        throw ex;
        //    }

        //    return webElements;
        //}

        ///// <summary>
        ///// This method gets the element on a web page depending on element type(id,classname, linktext,
        ///// tagname,xpath,cssselector).
        ///// </summary>
        ///// <exception cref="Exception">    Thrown when an exception error condition occurs. </exception>
        ///// <param name="element">      The element to act on. </param>
        ///// <param name="elementType">  . </param>
        ///// <param name="value">        . </param>
        ///// <returns>   The element. </returns>
        //public static IWebElement GetElement(this IWebElement element, ElementType elementType, string value)
        //{
        //    IWebElement webElement = null;
        //    switch (elementType)
        //    {
        //        case ElementType.Id:
        //            webElement = element.FindElement(By.Id(value));
        //            break;
        //        case ElementType.ClassName:
        //            webElement = element.FindElement(By.ClassName(value));
        //            break;
        //        case ElementType.LinkText:
        //            webElement = element.FindElement(By.LinkText(value));
        //            break;
        //        case ElementType.TagName:
        //            webElement = element.FindElement(By.TagName(value));
        //            break;
        //        case ElementType.XPath:
        //            webElement = element.FindElement(By.XPath(value));
        //            break;
        //        case ElementType.CssSelector:
        //            webElement = element.FindElement(By.CssSelector("[class='" + value + "']"));
        //            break;
        //    }
        //    // Scroll the browser to the element's Y position
        //    IWebDriver jsdriver = Driver.Instance; // assume assigned elsewhere
        //    IJavaScriptExecutor js = (IJavaScriptExecutor)jsdriver;
        //    js.ExecuteScript("window.scrollTo(0," + webElement.Location.Y + ")");
        //    return webElement;
        //}
        ///// <summary>
        ///// This method gets the elements on a web page depending on element type(id,classname, linktext,
        ///// tagname,xpath,cssselector).
        ///// </summary>
        ///// <exception cref="Exception">    Thrown when an exception error condition occurs. </exception>
        ///// <param name="element">      The element to act on. </param>
        ///// <param name="elementType">  . </param>
        ///// <param name="value">        . </param>
        ///// <returns>   The elements. </returns>
        /////
        ///// ### <remarks>   Pritesh.bhere, 11/12/2015. </remarks>
        //public static List<IWebElement> GetElements(this IWebElement element, ElementType elementType, string value)
        //{
        //    List<IWebElement> webElements = null;
        //    try
        //    {
        //        switch (elementType)
        //        {
        //            case ElementType.Id:
        //                webElements = element.FindElements(By.Id(value)).ToList();
        //                break;
        //            case ElementType.ClassName:
        //                webElements = element.FindElements(By.ClassName(value)).ToList();
        //                break;
        //            case ElementType.LinkText:
        //                webElements = element.FindElements(By.LinkText(value)).ToList();
        //                break;
        //            case ElementType.TagName:
        //                webElements = element.FindElements(By.TagName(value)).ToList();
        //                break;
        //            case ElementType.XPath:
        //                webElements = element.FindElements(By.XPath(value)).ToList();
        //                break;
        //            case ElementType.CssSelector:
        //                webElements = element.FindElements(By.CssSelector("[class='" + value + "']")).ToList();
        //                break;
        //        }
        //    }
        //    catch (Exception ex)
        //    {
        //        throw ex;
        //    }

        //    return webElements;
        //}

        //#endregion




        ///// <summary>   Initializers the given instance. <y/summary>
        ///// <param name="instance"> The instance. </param>
        /////
        ///// ### <remarks>   Pritesh.bhere, 11/12/2015. </remarks>
        //public static void Initializer(IWebDriver instance)
        //{
        //    Instance = instance;
        //    SetTimeOuts(120);
        //    PlatformVersion = ConfigurationManager.AppSettings["PlatformVersion"];
        //}
        /// <summary>   This method closes the Driver Instance. </summary>
        ///
        /// ### <remarks>   Pritesh.bhere, 11/12/2015. </remarks>
        //public static void Cleanup()
        //{
        //    Instance.Close();
        //}




        ///// <summary>   Takes Screenshot of current screen and stores it in Jpeg format. </summary>
        ///// <param name="fileNamePath"> . </param>
        /////
        ///// ### <remarks>   Pritesh.bhere, 11/12/2015. </remarks>
        //public static void TakesScreenshot(string fileNamePath)
        //{
        //    ///TODO:Save Screenshots Landscap orientation.
        //    ///TODO:Put the location in AppSetting
        //    Driver.Instance.Manage().Window.Maximize();
        //    //((ITakesScreenshot)Driver.Instance).GetScreenshot().SaveAsFile(string.Format( @"Y:\DE\{0}.png",fileNamePath), ImageFormat.Jpeg); 
        //}










        //#region Check is page has a main title

        ///// <summary>   This method checks if the page is displayed. </summary>
        ///// <exception cref="Exception">    Thrown when an exception error condition occurs. </exception>
        ///// <param name="pageTitle">    . </param>
        /////
        ///// ### <remarks>   Pritesh.bhere, 11/12/2015. </remarks>
        //public static void CheckIfPageDisplays(string pageTitle)
        //{
        //    //var divPageTitle = Driver.Instance.FindElement(By.ClassName("main_title"));
        //    var divPageTitle = Driver.GetElement(ElementType.ClassName, "main_title");
        //    if (!divPageTitle.Text.Contains(pageTitle))
        //        throw new Exception("Failed to display " + pageTitle + " page.");
        //}
        ///// <summary>   This method checks if the popup is displayed. </summary>
        ///// <exception cref="Exception">    Thrown when an exception error condition occurs. </exception>
        ///// <param name="pageTitle">    . </param>
        /////
        ///// ### <remarks>   Pritesh.bhere, 11/12/2015. </remarks>
        //public static void CheckIfPopupDisplays(string pageTitle)
        //{
        //    var newWindow = Driver.PoppedUpWindow();
        //    var mainTitle = newWindow.FindElement(By.ClassName("main_title"));
        //    if (!mainTitle.Text.ToLower().Contains(pageTitle.ToLower()))
        //    {
        //        throw new Exception("Pop up Window did not Open on cliking the link: '" + pageTitle + "'");
        //    }
        //}

        //#endregion



    }

    #region Enums

    /// <summary>   Values that represent element types. </summary>
    ///
    /// ### <remarks>   Pritesh.bhere, 11/12/2015. </remarks>
    public enum ElementType : int
    {
        /// <summary>   An enum constant representing the identifier option. </summary>
        Id = 1,
        /// <summary>   An enum constant representing the link text option. </summary>
        LinkText = 2,
        /// <summary>   An enum constant representing the tag name= 3 option. </summary>
        TagName = 3,
        /// <summary>   An enum constant representing the class name= 4 option. </summary>
        ClassName = 4,
        /// <summary>   An enum constant representing the path= 5 option. </summary>
        XPath = 5,
        /// <summary>   An enum constant representing the CSS selector= 6 option. </summary>
        CssSelector = 6
    }
    /// <summary>
    /// Enum to check which browser is currently being used
    /// </summary>
    public enum BrowserType : int
    {
        IE = 1,
        Chrome = 2,
        Firefox = 3,
        Safari = 4
    }

    #endregion
}

