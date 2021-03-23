public class SetupTearDownHook
    {
        public static string Role;
    
        Login login = new Login();
        OpenApplication openapp = new OpenApplication();

        [BeforeScenario]
        [Scope(Tag = "@ParaExt")]
        public void BeforeIntScenario()
        {
            String[] EnvironmentCredentials = GetEnvironment.getEnvironmentCredentials();
            try
            {
                if (EnvironmentCredentials[4].Equals("ie"))
                    Driver.OpenIE(EnvironmentCredentials[0]);
                else if (EnvironmentCredentials[4].Equals("chrome"))
                    Driver.OpenChrome(EnvironmentCredentials[0]);
                else if (EnvironmentCredentials[4].Equals("firefox"))
                    Driver.OpenFireFox(EnvironmentCredentials[0]);
            }
            catch (Exception ex)
            {
                throw ex;
            }
            if (!string.IsNullOrEmpty(Role))
            {
                login.login(QueryScripttogetUsers.getUserNamefromDB(String.Format(QueryScripttogetUsers.InternalRolebasedUser, Role)), EnvironmentCredentials[2]);
            }
            else
            {
                login.login(EnvironmentCredentials[1], EnvironmentCredentials[2]);
            }
            Driver.Wait(5);
        }