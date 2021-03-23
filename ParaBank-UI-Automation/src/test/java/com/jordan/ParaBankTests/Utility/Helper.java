using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Data;
using System.Reflection;
using EHBsBDDFramework.DataModel;

namespace EHBsBDDFramework.Utility
{

    public static class Helper
    {
        public static List<T> ToList<T>(this DataTable table) where T : class, new()
        {
            try
            {
                List<T> list = new List<T>();

                foreach (var row in table.AsEnumerable())
                {
                    T obj = new T();

                    PropertyInfo[] collumns = obj.GetType().GetProperties();

                    foreach (var prop in obj.GetType().GetProperties())
                    {
                        try
                        {
                            PropertyInfo propertyInfo = obj.GetType().GetProperty(prop.Name);
                            propertyInfo.SetValue(obj, Convert.ChangeType(row[prop.Name], propertyInfo.PropertyType), null);
                        }
                        catch
                        {
                            continue;
                        }
                    }

                    list.Add(obj);
                }

                return list;
            }
            catch
            {
                return null;
            }
        }

        public static void ShowTestHeader()
        {
            Console.WriteLine(String.Format("{0,-68} {1,-25} {2,-15} {3,-15}", "TestName".PadRight(68, '.'), "DateTimeStamp".PadRight(25, '.'), "Result".PadRight(15, '.'), "ErrorFlag"));
            Console.WriteLine("-------------------------------------------------------------------------------------------------------------------------");
        }

    }

}
