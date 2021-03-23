using EHBsBDDFramework.Constants;
using EHBsBDDFramework.Utility;
using System;
using System.Data;
using System.Data.SqlClient;

namespace Framework.Utility
{

    public class DBConnection : IDisposable
    {

        string connectionString;
        SqlConnection con;

        public DBConnection(String databaseType = "GEMS")
        {
            dynamic Obj = JsonHelper.GetValue(Constants.APP_JSON, "appSettings");
            String Environment = (string)Obj.Environment;
            switch (Environment.ToLower())
            {
                case TestEnvironment.ExternalUTL2:
                    connectionString = (String)Obj[databaseType + "DatabaseUTL2"];
                    break;
                case TestEnvironment.ExternalUTL9:
                    connectionString = (String)Obj[databaseType + "DatabaseUTL9"];
                    break;
                case TestEnvironment.ExternalUTL16:
                    connectionString = (String)Obj[databaseType + "DatabaseUTL16"];
                    break;
                case TestEnvironment.ExternalREIINT:
                    connectionString = (String)Obj[databaseType + "DatabaseREIINT"];
                    break;
                case TestEnvironment.InternalUTL2:
                    connectionString = (String)Obj[databaseType + "DatabaseUTL2"];
                    break;
                case TestEnvironment.InternalUTL9:
                    connectionString = (String)Obj[databaseType + "DatabaseUTL9"];
                    break;
                case TestEnvironment.InternalUTL16:
                    connectionString = (String)Obj[databaseType + "DatabaseUTL16"];
                    break;
                case TestEnvironment.InternalSBX8:
                    connectionString = (String)Obj[databaseType + "DatabaseSBX8"];
                    break;
                case TestEnvironment.InternalREIINT:
                    connectionString = (String)Obj[databaseType + "DatabaseREIINT"];
                    break;
                case TestEnvironment.InternalHRSAINT:
                    Console.WriteLine("As there is no access to DB fill the respective json manually and run the automation");
                    break;
                case TestEnvironment.InternalHRSAQA:
                    Console.WriteLine("As there is no access to DB fill the respective json manually and run the automation");
                    break;
                case TestEnvironment.InternalHRSAOS:
                    Console.WriteLine("As there is no access to DB fill the respective json manually and run the automation");
                    break;
                case TestEnvironment.GrantdotGov:
                    Console.WriteLine("As there is no access to DB fill the respective json manually and run the automation");
                    break;
                case TestEnvironment.PMS:
                    connectionString = (String)Obj[databaseType + "DatabasePMS"];
                    break;
                default:
                    throw new Exception("DB Environment not found");
            }
            con = new SqlConnection(connectionString);
        }

        public void Open()
        {
            if (con.State != ConnectionState.Open)
            {
                con.Open();
            }
        }

        public void Close()
        {
            if (con.State != ConnectionState.Closed)
            {
                con.Close();
            }
        }

        public DataTable GetTable(string query)
        {
            return GetTable(query, CommandType.Text);
        }

        public DataTable GetTable(string query, CommandType commandType)
        {
            IDbCommand cmd = con.CreateCommand();
            cmd.CommandText = query;
            cmd.CommandType = commandType;
            try
            {
                DataSet ds = new DataSet();
                DataTable dt = new DataTable();

                SqlDataAdapter dadapter = new SqlDataAdapter();
                dadapter.SelectCommand = new SqlCommand(query, con);
                dadapter.Fill(ds);

                if (ds.Tables.Count > 0)
                {
                    dt = ds.Tables[0];
                }
                else
                {
                    dt = null;
                }
                return dt;
            }
            catch (Exception ex)
            {
                throw ex;
            }

        }
        public void Dispose()
        {
            con.Dispose();
        }

        public static string convertDataTableToString(DataTable dataTable)
        {
            string data = string.Empty;
            for (int i = 0; i < dataTable.Rows.Count; i++)
            {
                DataRow row = dataTable.Rows[i];
                for (int j = 0; j < dataTable.Columns.Count; j++)
                {
                    data += dataTable.Columns[j].ColumnName + "~" + row[j];
                    if (j == dataTable.Columns.Count - 1)
                    {
                        if (i != (dataTable.Rows.Count - 1))
                            data += "$";
                    }
                    else
                        data += "|";
                }
            }
            return data;
        }
    }
}
