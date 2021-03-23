

class GetEnvironment
    {
        public static string[] getEnvironmentCredentials()
        {
            dynamic Obj = JsonHelper.GetValue(Constants.Constants.APP_JSON, "appSettings");
            String Environment = (String)Obj["Environment"];
            String BrowserType = (String)Obj["Browser"];
            String[] EnvironmentCredentials = new String[5];

            switch (Environment.ToLower())
            {
                case TestEnvironment.ExternalUTL2:
                    EnvironmentCredentials[0] = (String)Obj["ExternalLoginURLUTL2"];
                    EnvironmentCredentials[1] = (String)Obj["ExternalUserUTL2"];
                    EnvironmentCredentials[2] = (String)Obj["ExternalPasswordUTL2"];
                    EnvironmentCredentials[3] = (String)Obj["ExternalbaseURLUTL2"];
                    break;
                case TestEnvironment.ExternalUTL9:
                    EnvironmentCredentials[0] = (String)Obj["ExternalLoginURLUTL9"];
                    EnvironmentCredentials[1] = (String)Obj["ExternalUserUTL9"];
                    EnvironmentCredentials[2] = (String)Obj["ExternalPasswordUTL9"];
                    EnvironmentCredentials[3] = (String)Obj["ExternalbaseURLUTL9"];
                    break;
                case TestEnvironment.ExternalUTL16:
                    EnvironmentCredentials[0] = (String)Obj["ExternalLoginURLUTL16"];
                    EnvironmentCredentials[1] = (String)Obj["ExternalUserUTL16"];
                    EnvironmentCredentials[2] = (String)Obj["ExternalPasswordUTL16"];
                    EnvironmentCredentials[3] = (String)Obj["ExternalbaseURLUTL16"];
                    break;
                case TestEnvironment.ExternalREIINT:
                    EnvironmentCredentials[0] = (String)Obj["ExternalLoginURLREIIntegration"];
                    EnvironmentCredentials[1] = (String)Obj["ExternalUserREIIntegration"];
                    EnvironmentCredentials[2] = (String)Obj["ExternalPasswordREIIntegration"];
                    EnvironmentCredentials[3] = (String)Obj["ExternalbaseURLREIIntegration"];
                    break;
                case TestEnvironment.InternalUTL2:
                    EnvironmentCredentials[0] = (String)Obj["InternalLoginURLUTL2"];
                    EnvironmentCredentials[1] = (String)Obj["InternalUserUTL2"];
                    EnvironmentCredentials[2] = (String)Obj["InternalPasswordUTL2"];
                    EnvironmentCredentials[3] = (String)Obj["InternalbaseURLUTL2"];
                    break;
                case TestEnvironment.InternalUTL9:
                    EnvironmentCredentials[0] = (String)Obj["InternalLoginURLUTL9"];
                    EnvironmentCredentials[1] = (String)Obj["InternalUserUTL9"];
                    EnvironmentCredentials[2] = (String)Obj["InternalPasswordUTL9"];
                    EnvironmentCredentials[3] = (String)Obj["InternalbaseURLUTL9"];
                    break;
                case TestEnvironment.InternalUTL16:
                    EnvironmentCredentials[0] = (String)Obj["InternalLoginURLUTL16"];
                    EnvironmentCredentials[1] = (String)Obj["InternalUserUTL16"];
                    EnvironmentCredentials[2] = (String)Obj["InternalPasswordUTL16"];
                    EnvironmentCredentials[3] = (String)Obj["InternalbaseURLUTL16"];
                    break;
                case TestEnvironment.InternalSBX8:
                    EnvironmentCredentials[0] = (String)Obj["InternalLoginURLSBX8"];
                    EnvironmentCredentials[1] = (String)Obj["InternalUserSBX8"];
                    EnvironmentCredentials[2] = (String)Obj["InternalPasswordSBX8"];
                    EnvironmentCredentials[3] = (String)Obj["InternalbaseURLSBX8"];
                    break;
                case TestEnvironment.InternalREIINT:
                    EnvironmentCredentials[0] = (String)Obj["InternalLoginURLREIIntegration"];
                    EnvironmentCredentials[1] = (String)Obj["InternalUserREIIntegration"];
                    EnvironmentCredentials[2] = (String)Obj["InternalPasswordREIIntegration"];
                    EnvironmentCredentials[3] = (String)Obj["InternalbaseURLREIIntegration"];
                    break;
                case TestEnvironment.InternalHRSAINT:
                    EnvironmentCredentials[0] = (String)Obj["InternalLoginURLHRSAIntegration"];
                    EnvironmentCredentials[1] = (String)Obj["InternalUserHRSAIntegration"];
                    EnvironmentCredentials[2] = (String)Obj["InternalPasswordHRSAIntegration"];
                    EnvironmentCredentials[3] = (String)Obj["InternalbaseURLHRSAIntegration"];
                    break;
                case TestEnvironment.InternalHRSAQA:
                    EnvironmentCredentials[0] = (String)Obj["InternalLoginURLHRSAQA"];
                    EnvironmentCredentials[1] = (String)Obj["InternalUserHRSAQA"];
                    EnvironmentCredentials[2] = (String)Obj["InternalPasswordHRSAQA"];
                    EnvironmentCredentials[3] = (String)Obj["InternalbaseURLHRSAQA"];
                    break;
                case TestEnvironment.InternalHRSAOS:
                    EnvironmentCredentials[0] = (String)Obj["InternalLoginURLHRSAOS"];
                    EnvironmentCredentials[1] = (String)Obj["InternalUserHRSAOS"];
                    EnvironmentCredentials[2] = (String)Obj["InternalPasswordHRSAOS"];
                    EnvironmentCredentials[3] = (String)Obj["InternalbaseURLHRSAOS"];
                    break;
                case TestEnvironment.PMS:
                    EnvironmentCredentials[0] = (String)Obj["PMSBaseURL"];
                    EnvironmentCredentials[1] = (String)Obj["PMSTestLoginURL"];
                    EnvironmentCredentials[2] = (String)Obj["PMSTestPassword"];
                    break;
                case TestEnvironment.GrantdotGov:
                    EnvironmentCredentials[0] = (String)Obj["LoginURLGrantsdotGov"];
                    EnvironmentCredentials[1] = (String)Obj["UserGrantsdotGov"];
                    EnvironmentCredentials[2] = (String)Obj["PasswordGrantsdotGov"];
                    EnvironmentCredentials[3] = (String)Obj["BaseURLGrantsdotGov"];
                    break;
                default:
                    throw new Exception("Environment not found");
            }
            EnvironmentCredentials[4] = BrowserType.ToLower();
            return EnvironmentCredentials;
        }
    }
}