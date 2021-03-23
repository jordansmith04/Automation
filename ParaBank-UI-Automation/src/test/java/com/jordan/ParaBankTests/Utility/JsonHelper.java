using System;
using System.IO;
using System.Net;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace EHBsBDDFramework.Utility
{
    class JsonHelper
    {
        public static T Download_Serialized_Json_Data<T>(string jsonPath) where T : new()
        {
            using (var w = new WebClient())
            {
                var jsonData = string.Empty;
                // attempt to download JSON data as a string
                try
                {
                    //Console.WriteLine("Started downloading json data");
                    jsonData = w.DownloadString(jsonPath);
                    //Console.WriteLine("Completed downloading");
                }
                catch (Exception)
                {
                    // ignored
                }
                // if string with JSON data is not empty, deserialize it to class and return its instance 
                return !string.IsNullOrEmpty(jsonData) ? JsonConvert.DeserializeObject<T>(jsonData) : new T();
            }
        }

        public static void UpdateJsonFile(string jsonPath, object jsonData)
        {
            string output = JsonConvert.SerializeObject(jsonData, Formatting.Indented);
            File.WriteAllText(jsonPath, output);
        }
        public static void EditJsonFile(string path, string field, string value)
        {
            dynamic jsonData = JsonConvert.DeserializeObject(File.ReadAllText(path));
            jsonData[field] = value;
            string newJsonResult = Newtonsoft.Json.JsonConvert.SerializeObject(jsonData, Newtonsoft.Json.Formatting.Indented);
            File.WriteAllText(path, newJsonResult);
            Driver.Wait(2);
        }
        public static void EditJsonFileArray(string path, string field, string value,int ArrayNumber)
        {
            dynamic jsonData = JsonConvert.DeserializeObject(File.ReadAllText(path));
            jsonData[ArrayNumber][field] = value;
            string newJsonResult = Newtonsoft.Json.JsonConvert.SerializeObject(jsonData, Newtonsoft.Json.Formatting.Indented);
            File.WriteAllText(path, newJsonResult);
            Driver.Wait(2);
        }

        public static T ReadJsonObject<T>(string jsonPath) where T : new()
        {
            using (var w = new WebClient())
            {
                var jsonData = string.Empty;
                // attempt to download JSON data as a string
                try
                {
                    jsonData = w.DownloadString(jsonPath);
                }
                catch (Exception)
                {
                    // ignored
                }

                // if string with JSON data is not empty, deserialize it to class and return its instance 
                dynamic obj = JObject.Parse(jsonData);
                return !string.IsNullOrEmpty(jsonData) ? JsonConvert.DeserializeObject<T>(jsonData) : new T();
            }
        }

        public static dynamic GetValue(string filePath, string key)
        {
            dynamic jsonData = JsonConvert.DeserializeObject(File.ReadAllText(filePath));
            if (jsonData[key] != null)
                return jsonData[key];
            else
                return null;
        }
    }

}
